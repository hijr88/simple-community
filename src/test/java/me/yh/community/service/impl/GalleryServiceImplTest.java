package me.yh.community.service.impl;

import me.yh.community.entity.Gallery;
import me.yh.community.entity.GalleryFile;
import me.yh.community.entity.Member;
import me.yh.community.repository.GalleryRepository;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.GalleryService;
import me.yh.community.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class GalleryServiceImplTest {

    @Autowired
    GalleryService galleryService;

    @Autowired
    GalleryRepository galleryRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Rollback(value = false)
    @Transactional
    @Test
    void deleteGallery() {
        Member member = Member.testUser("haha");
        memberRepository.save(member);

        Gallery gallery = new Gallery("제목1",member);
        GalleryFile file1 = new GalleryFile(gallery,"fileName","fff",1L);
        GalleryFile file2 = new GalleryFile(gallery,"fileName","fff",1L);
        GalleryFile file3 = new GalleryFile(gallery,"fileName","fff",1L);
        GalleryFile file4 = new GalleryFile(gallery,"fileName","fff",1L);
        gallery.getFiles().add(file1);
        gallery.getFiles().add(file2);
        gallery.getFiles().add(file3);
        gallery.getFiles().add(file4);
        galleryRepository.save(gallery);

        Gallery save = galleryRepository.findById(gallery.getId()).get();
        int size = save.getFiles().size();
        assertEquals(4,size);

        em.flush();
        em.clear();

        System.out.println("============================================");
        Gallery g = galleryRepository.findById(save.getId()).get();

        List<Long> fileIds = g.getFiles().stream().map(GalleryFile::getId).collect(Collectors.toList());

        galleryRepository.deleteAllFileByIds(fileIds);
        galleryRepository.deleteById(g.getId());
    }
}