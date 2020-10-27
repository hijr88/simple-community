package me.yh.community.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.yh.community.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/files")
@RestController
public class FileController {

    private final String SE = FileService.SE;
    private final Set<String> entityType = Set.of("profile","posts","gallery");

    private final FileService fileService;

    /**
     * 타입,식별자,이미지 이름 받아서 경로 지정 해주고 썸네일 메서드 호출
     */
    @GetMapping("/thumb/{type}/{id}/{fileName}")
    public ResponseEntity<String> printThumbImage(HttpServletResponse response
            , @ModelAttribute FileInfo info
            , @RequestParam("w") int width
            , @RequestParam("h") int height) throws Exception {

        if (width > 500 || height > 500) {
            return new ResponseEntity<>("not allowed, Please check size", HttpStatus.FORBIDDEN);
        }

        if (!entityType.contains(info.getType())) {
            return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        String filePath;

        if (info.getType().equals("profile")) {
            if (info.getFileName().equals("none")) {
                filePath = FileService.defaultProfile;
            } else {
                filePath = FileService.profilePath + SE + info.getId() + SE + info.getFileName();
            }
            File file = new File(filePath);
            if (!file.exists())
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            fileService.toThumbnail(response, file, width, height);
            return new ResponseEntity<>("Good", HttpStatus.OK);
        }

        return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/down/{type}/{id}/{fileName}")
    public ResponseEntity<String> downloadFile(HttpServletResponse response,
                        @ModelAttribute FileInfo info) throws IOException {

        String filePath = FileService.postPath + SE + info.id + SE + info.fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        fileService.download(response, file, info.o);

        return new ResponseEntity<>("Good", HttpStatus.OK);
    }




    @Data
    static class FileInfo {
        private String type;      // profile","board","gallery
        private String id;        // 고유 번호, 회원은 username
        private String fileName;  // 파일 이름
        private String o;         // 원래 이름
        //private String fm;      // 포맷
    }
}
