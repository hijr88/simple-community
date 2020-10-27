package me.yh.community.api;

import me.yh.community.repository.PostRepository;
import me.yh.community.security.WithMockCustomUser;
import me.yh.community.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostApiController.class)
class PostApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostRepository postRepository;

    @MockBean
    PostService postService;

    @WithMockCustomUser
    @Test
    void postChangePub() throws Exception {
        mockMvc.perform(put("/api/posts/1/edit/pub")
                        .contentType("text/plain")
                        .content("true"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}