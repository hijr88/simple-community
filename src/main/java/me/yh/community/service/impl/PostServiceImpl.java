package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor

@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    @Transactional
    @Override
    public boolean createNewPost(Post newPost, Member member, MultipartFile mf) {

        Post post = Post.createNewPost(newPost.getTitle(), newPost.getContent(), member);
        Post savePost = postRepository.save(post);
        if (savePost.getGroupNo() == 0)
            savePost.setGroupNo(savePost.getId());

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
