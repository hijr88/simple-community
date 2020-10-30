package me.yh.community.api;

import lombok.RequiredArgsConstructor;
import me.yh.community.repository.GalleryRepository;
import me.yh.community.service.GalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/galleries")
public class GalleryApiController {

    private final GalleryRepository galleryRepository;
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

    /**
     * 갤러리 pub 수정하는곳
     *
     * @param id   갤러리 번호
     * @param bool true or false
     * @return 성공시 true
     */
    @PutMapping(path = "{id}/edit/pub", consumes = "text/plain")
    public ResponseEntity<Boolean> galleryChangePub(@PathVariable long id, @RequestBody String bool) {

        boolean pub = Boolean.parseBoolean(bool);
        System.out.println(id +", "+ bool );
        galleryRepository.changePubById(id, pub);

        return ResponseEntity.ok(true);
    }
}
