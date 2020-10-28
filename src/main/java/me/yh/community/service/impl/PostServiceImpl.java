package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostPage;
import me.yh.community.dto.post.PostRequestDto;
import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import me.yh.community.entity.PostFile;
import me.yh.community.entity.PostRecommend;
import me.yh.community.repository.PostRecommendRepository;
import me.yh.community.repository.PostRepository;
import me.yh.community.service.FileService;
import me.yh.community.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.Optional;

@RequiredArgsConstructor

@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final EntityManager em;
    private final PostRepository postRepository;
    private final FileService fileService;
    private final PostRecommendRepository postRecommendRepository;
    private final String SE = FileService.SE;

    @Override
    public PostPage findPostList(PostPage page) {
        page.setContent(postRepository.findPostList(page));

        Long totalCount = postRepository.countPostList(page);
        page.setTotalCount(totalCount);

        long pageMaxNum =  (long) Math.ceil( (totalCount /10.0) );
        pageMaxNum = pageMaxNum == 0 ? 1 : pageMaxNum;
        page.setPageMaxNum(pageMaxNum);

        return page;
    }

    @Transactional
    @Override
    public boolean createNewPost(PostRequestDto newPost, Member member, MultipartFile mf) {
        Post post = PostRequestDto.createNewPost(newPost, member);

        Post savePost = postRepository.save(post);
        savePost.setGroupNo(savePost.getId());

        return createPostFile(savePost, mf);
    }

    @Transactional
    @Override
    public boolean createReplyPost(PostRequestDto newPost, Member member, MultipartFile mf) {
        Optional<Post> parentPost_ = postRepository.findById(newPost.getParent());
        if (parentPost_.isEmpty())
            return false;
        Post parentPost = parentPost_.get();

        long parentId = parentPost.getId();
        long groupNo = parentPost.getGroupNo();
        int groupOrder;
        int dept = parentPost.getDept() +1;

        Integer findLastChildOrder = postRepository.findLastChildOrderById(parentPost.getId());
        //부모글에 답글이 없을 경우 null 값이 들어온다. 그래서 부모 그룹 순서 + 1을 한다.
        if (findLastChildOrder == null) {
            groupOrder = parentPost.getGroupOrder() + 1;
        } else { //답글이 달려 있는 그릅 순서에 +1 하기
            groupOrder = findLastChildOrder + 1;
        }
        //자신이 들어갈 그룹의 순서 1칸씩 증가시킨다. ex) 자신이 들어갈 순서가 2이면 2이상부터 1씩 증가
        postRepository.incrementGroupOrder(groupNo, groupOrder);

        Post post = new Post(newPost.getTitle(), newPost.getContent(), member, parentId, groupNo, groupOrder, dept);
        Post savePost = postRepository.save(post);

        return createPostFile(savePost, mf);
    }

    @Transactional
    @Override
    public PostDetailDto findPostDetailByIdAndPub(long id, boolean pub) {
        postRepository.incrementHitById(id);

        return postRepository.findPostDetailByIdAndPub(id, true);
    }

    @Transactional
    @Override
    public boolean incrementRecommend(long postId, String userName) {
        boolean exists = postRecommendRepository.existsByPostIdAndMemberId(postId, userName);
        if (exists)
            return false;

        PostRecommend recommend = new PostRecommend(postId, userName);
        em.persist(recommend);
        return true;
    }

    @Transactional
    @Override
    public boolean modify(long id, PostRequestDto editPost, MultipartFile mf, boolean isDelete) {

        Optional<Post> findPost = postRepository.findById(id);
        if (findPost.isEmpty())
            return false;
        Post post = findPost.get();
        post.modify(editPost.getTitle(), editPost.getContent()); //글 수정

        //파일 수정
        String folderPath = FileService.postPath + SE + id;

        if (isDelete) { //파일 삭제인 경우
            PostFile postFile = post.getFiles().get(0); //파일 정보 가져오기

            //삭제 처리
            String filePath = folderPath + SE + postFile.getFileName();
            fileService.deleteFile(filePath);
            post.getFiles().remove(postFile);
        }
        if (!mf.isEmpty()) {//파일이 첨부된 경우

            // 기존 파일이 존재하면 삭제
            if (post.getFiles().size() != 0) {
                PostFile postFile = post.getFiles().get(0);
                String filePath = folderPath + SE + postFile.getFileName();
                fileService.deleteFile(filePath);
                post.getFiles().remove(postFile);
            }
            return createPostFile(post, mf);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean deletePost(long id) {
        int count = postRepository.countByParent(id);
        if (count !=0) { //답글이 있는경우
            return false;
        } else {
            fileService.deleteFolder(FileService.postPath + SE + id);
            Optional<Post> byId = postRepository.findById(id);
            byId.ifPresent(post -> post.setDelete(true));
            return true;
        }
    }

    private boolean createPostFile(Post savePost, MultipartFile mf) {
        if (!mf.isEmpty()) {
            String folderPath = FileService.postPath + SE + savePost.getId();
            String saveFileName = fileService.upload(folderPath, mf);
            if(saveFileName == null) return false;

            PostFile postFile = new PostFile(savePost, saveFileName, mf.getOriginalFilename(), mf.getSize());

            savePost.changeFile(postFile);
        }
        return true;
    }
}
