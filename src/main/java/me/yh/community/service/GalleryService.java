package me.yh.community.service;

import me.yh.community.entity.Gallery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GalleryService {
    boolean createNewGallery(Gallery gallery, List<MultipartFile> files);

    Gallery findGalleryDetail(long galleryId, Boolean pub);

    boolean deleteGallery(long id);

    boolean modifyGallery(Long id, String title, List<Long> deleteNo, List<String> deleteFileName, List<MultipartFile> newFiles);
}
