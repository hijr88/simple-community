package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
import me.yh.community.dto.post.PostRequestDto;
import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import me.yh.community.entity.PostFile;
import me.yh.community.repository.PostRepository;
import me.yh.community.service.FileService;
import me.yh.community.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@RequiredArgsConstructor

@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    //TODO 게시판 리스트


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

    private boolean createPostFile(Post savePost, MultipartFile mf) {

        if (!mf.isEmpty()) {
            String folderPath = FileService.postPath + File.separator + savePost.getId();
            String saveFileName = fileService.upload(folderPath, mf);
            if(saveFileName == null) return false;

            PostFile postFile = new PostFile(savePost, mf.getOriginalFilename(), saveFileName, mf.getSize());

            savePost.changeFile(postFile);
        }
        return true;
    }
}
