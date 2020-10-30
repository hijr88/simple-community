package me.yh.community.api;

import lombok.RequiredArgsConstructor;
import me.yh.community.service.GalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/galleries")
public class GalleryApiController {

    private final GalleryService galleryService;

    /**
     * @param id 갤러리 번호
     * @return 삭제 성공시 리스트 페이지
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Boolean> deleteGallery(@PathVariable long id) {

        boolean result = galleryService.deleteGallery(id);
        return ResponseEntity.ok(result);
    }
}
