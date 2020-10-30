package me.yh.community.controller;

import me.yh.community.repository.GalleryRepository;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.GalleryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GalleryController.class)
class GalleryControllerTest {

    @MockBean
    MemberRepository memberRepository;
    @MockBean
    GalleryRepository galleryRepository;
    @MockBean
    GalleryService galleryService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void galleryList() throws Exception {
        mockMvc.perform(get("/galleries"));
    }
}