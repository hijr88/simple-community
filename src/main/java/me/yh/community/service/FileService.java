package me.yh.community.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public interface FileService {
    String SE = File.separator;
    String uploadPath =         "C:" + SE + "upload";               //기본 저장위치
    String profilePath =        uploadPath  + SE + "profile";       //사용자 프로필 폴더 이름
    String defaultProfile =     profilePath + SE + "user.png";      //기본 이미지
    String boardPath =          uploadPath  + SE + "board";         //게시판 폴더 이름
    String galleryPath =        uploadPath  + SE + "gallery";       //갤러리 폴더 이름

    void toThumbnail(HttpServletResponse response, File file, int width, int height) throws IOException;

    boolean deleteFile(String filePath);

    String upload(String folderPath, MultipartFile mf);
}
