package me.yh.community.controller;

import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostRequestDto;
import me.yh.community.entity.Member;
import me.yh.community.repository.MemberRepository;
import me.yh.community.repository.PostRepository;
import me.yh.community.security.WithMockCustomUser;
import me.yh.community.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        MockMultipartFile file = new MockMultipartFile("input-file", "test.txt",
                "text/plain", "hello file".getBytes());

        Member member = Member.testUser("user");
        PostRequestDto post = new PostRequestDto("제목", "내용");

        given(memberRepository.findById("user")).willReturn(Optional.of(member));
        given(postService.createNewPost(post,member,file)).willReturn(true);

        mockMvc.perform(multipart("/posts/new")
                        .file(file)
                        .flashAttr("postRequestDto", post))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/posts"));
    }

    @WithMockCustomUser
    @DisplayName("부모글이 존재하지 않는 답글을 요청")
    @Test
    void replyForm_fail() throws Exception {

        given(postRepository.existsById(100L)).willReturn(false);

        // 숫자가 아닌 문자열을 url에 입력한 경우 에러 처리
        mockMvc.perform(get("/posts/ab/reply"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error-redirect"));

        mockMvc.perform(get("/posts/100/reply"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error-redirect"));
    }


    @WithMockCustomUser
    @DisplayName("부모글이 존재하는 답글 요청")
    @Test
    void replyForm() throws Exception {

        given(postRepository.existsById(100L)).willReturn(true);

        // 숫자가 아닌 문자열을 url에 입력한 경우 에러 처리
        mockMvc.perform(get("/posts/100/reply"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("title","답 글 쓰기"))
                .andExpect(model().attribute("action","new"))
                .andExpect(model().attribute("parent",100L));
    }

    @WithMockCustomUser
    @DisplayName("상세보기 페이지")
    @Test
    void postDetail() throws Exception {

        Long id = 100L;
        PostDetailDto postDetailDto = new PostDetailDto();

        given(postRepository.existsById(id)).willReturn(true);
        given(postRepository.findPostDetailById(id)).willReturn(postDetailDto);

        mockMvc.perform(get("/posts/100"))
                .andExpect(status().isOk());
    }
}