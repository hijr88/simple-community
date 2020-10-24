package me.yh.community.controller;

import me.yh.community.entity.Member;
import me.yh.community.entity.Post;
import me.yh.community.repository.MemberRepository;
import me.yh.community.repository.PostRepository;
import me.yh.community.security.WithMockCustomUser;
import me.yh.community.service.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostRepository postRepository;
    @MockBean
    PostService postService;
    @MockBean
    MemberRepository memberRepository;

    @Test
    void index() throws Exception {
        //TODO 계층형 게시판
        //mockMvc.perform(get("/posts"))
    }

    @WithMockCustomUser
    @DisplayName("글 게시글 입력 페이지")
    @Test
    void newForm() throws Exception {
        mockMvc.perform(get("/posts/new"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @WithMockCustomUser
    @DisplayName("글 게시글 생성")
    @Test
    void createPost() throws Exception {

        //given
        MockMultipartFile file = new MockMultipartFile("input-file", "test.txt",
                "text/plain", "hello file".getBytes());

        Member member = Member.testUser("user");

        Post post = Post.createNewPost("제목","내용", member);

        given(memberRepository.findById("user")).willReturn(Optional.of(member));
        given(postService.createNewPost(post,member,file)).willReturn(true);

        //when
        mockMvc.perform(multipart("/posts/new")
                        .file(file)
                        .flashAttr("post", post))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/posts"));
    }
}