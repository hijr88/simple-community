package me.yh.community.controller.admin;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.dto.gallery.GalleryListDto;
import me.yh.community.dto.gallery.GalleryPage;
import me.yh.community.entity.Gallery;
import me.yh.community.service.AdminService;
import me.yh.community.service.GalleryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/galleries")
@Controller
public class AdminGalleryController {

    private final AdminService adminService;
    private final GalleryService galleryService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping({"","/"})
    public String  galleryList(Model model, GalleryPage page) {

        String[] ids = page.getContent().stream().map(GalleryListDto::getId).map(String::valueOf).toArray(String[]::new);
        String allNo = String.join(" ", ids);

        log.info("페이지 정보 {}",page.toString());
        model.addAttribute("page", page);
        model.addAttribute("allNo",allNo);

        return "/admin/galleries/index";
    }

    @GetMapping(path = "/{galleryId}")
    public String detailGallery(HttpServletRequest request, RedirectAttributes ra,
                                @PathVariable long galleryId, Model model) {

        Gallery gallery = galleryService.findGalleryDetail(galleryId, null);
        if (gallery == null) {
            ra.addFlashAttribute("type","BAD_REQUEST");
            return "redirect:/error-redirect";
        }
        model.addAttribute("g", gallery);
        model.addAttribute("qs", Utils.getPreQS(request));

        return "/admin/galleries/detail";
    }


    /**
     * 체크된 갤러리 번호는 공개처리 체크 안된건 비공개처리
     * allNo    현재 페이지 모든 갤러리 번호
     * openNo   체크된 갤러리 번호
     */
    @PutMapping("/edit/all-pub")
    public ResponseEntity<Boolean> updateGalleriesPub(@RequestBody Map<String, String> param) {

        String allNo = param.get("allNo");     //모든 글 번호
        String openNo = param.get("openNo");   //체크된 글 번호

        adminService.modifyGalleryAllPub(allNo, openNo);

        return ResponseEntity.ok(true);
    }
}
