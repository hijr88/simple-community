package me.yh.community.controller;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.dto.gallery.GalleryPage;
import me.yh.community.entity.Gallery;
import me.yh.community.entity.Member;
import me.yh.community.repository.GalleryRepository;
import me.yh.community.repository.MemberRepository;
import me.yh.community.service.GalleryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/galleries")
@Controller
public class GalleryController {

    private final MemberRepository memberRepository;
    private final GalleryRepository galleryRepository;
    private final GalleryService galleryService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PreAuthorize("permitAll()")
    @GetMapping(path = "")
    public String  galleryList(Model model, GalleryPage page) {

        log.info("페이지 정보 {}",page.toString());
        model.addAttribute("page", page);

        return "galleries/index";
    }

    /**
     * @return 업로드 페이지
     */
    @GetMapping(path = "/new")
    public String  createGallery(Model model) {
        model.addAttribute("title", "업로드");
        model.addAttribute("action", "new");
        return "galleries/form";
    }

    /**
     * @param principal 사용자
     * @param title     제목
     * @param files     이미지 파일들
     * @return 성공시 리스트 페이지
     */
    @PostMapping(path = "/new")
    public String createGallery(RedirectAttributes ra, Principal principal,
                              @RequestParam("title") String title,
                              @RequestParam("file") List<MultipartFile> files) {

        Optional<Member> findMember = memberRepository.findById(principal.getName());
        if (findMember.isEmpty()) {
            ra.addFlashAttribute("type","FAIL_ADD_POST");
            return "redirect:/error-redirect";
        }
        Member member = findMember.get();
        Gallery gallery = new Gallery(title, member);

        boolean result = galleryService.createNewGallery(gallery, files);

        if (result) {
            return "redirect:/galleries";
        } else {
            ra.addFlashAttribute("type","FAIL_ADD_GALLERY");
            return "redirect:/error-redirect";
        }
    }

    /**
     * @param galleryId 갤러리 번호
     * @return 상세 페이지
     */
    @GetMapping(path = "/{galleryId}")
    public String detailGallery(HttpServletRequest request, RedirectAttributes ra,
                                      @PathVariable long galleryId, Model model) {

        Gallery gallery = galleryService.findGalleryDetail(galleryId, true);
        if (gallery == null) {
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }
        model.addAttribute("g", gallery);
        model.addAttribute("qs", Utils.getPreQS(request));

        return "galleries/detail";
    }

    /**
     * @param id      갤러리 번호
     * @param principal 유저
     * @return 갤러리 수정 페이지
     */
    @GetMapping("{id}/edit")
    public String editForm(Model model, RedirectAttributes ra,
        @PathVariable long id, Principal principal) {

        Optional<Gallery> gallery_ = galleryRepository.findById(id);
        if (gallery_.isEmpty()) {
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }
        Gallery gallery = gallery_.get();
        if (!gallery.getMember().getId().equals(principal.getName())) { //작성자와 로그인한 아이디가 불일치 하는 경우
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }
        gallery.getFiles().forEach(f -> {
            f.setFileName(Utils.urlEncode(f.getFileName()));
            f.setOriginalFileName(Utils.urlEncode(f.getOriginalFileName()));
        });

        model.addAttribute("title", "수정하기");
        model.addAttribute("action", gallery.getId() + "/edit");
        model.addAttribute("g",gallery);

        return "galleries/form";
    }

    /**
     * @param id            갤러리 번호
     * @param title          제목
     * @param deleteNo       삭제할 파일 번호
     * @param deleteFileName 삭제할 파일 이름
     * @param newFiles          추가 할 파일
     * @return 성공시 번호에 해당하는 상세 페이지 실패시 리스트 페이지
     */
    @PostMapping(path = "/{id}/edit")
    public String  modifyGallery(RedirectAttributes ra, @PathVariable long id
            , @RequestParam("title") String title
            , @RequestParam(value = "deleteNo", required = false) List<Long> deleteNo
            , @RequestParam(value = "deleteFileName", required = false) List<String> deleteFileName
            , @RequestParam(value = "file", required = false) List<MultipartFile> newFiles) {

        boolean result = galleryService.modifyGallery(id, title, deleteNo, deleteFileName, newFiles);

        if (result) {
            return "redirect:/galleries/" + id;
        } else {
            ra.addFlashAttribute("type", "FAIL_MODIFY_GALLERY");
            return "redirect:/error-redirect";
        }
    }
}
