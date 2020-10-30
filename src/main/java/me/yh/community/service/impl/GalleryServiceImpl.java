package me.yh.community.service.impl;

import lombok.RequiredArgsConstructor;
import me.yh.community.Utils;
import me.yh.community.entity.Gallery;
import me.yh.community.entity.GalleryFile;
import me.yh.community.repository.GalleryRepository;
import me.yh.community.service.FileService;
import me.yh.community.service.GalleryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryRepository;
    private final FileService fileService;

    private final String SE = File.separator;

    @Transactional
    @Override
    public boolean createNewGallery(Gallery gallery, List<MultipartFile> files) {

        Gallery saveGallery = galleryRepository.save(gallery);

        if (files.size() == 0) {
            return false;
        }

        String folderPath = FileService.galleryPath + SE + saveGallery.getId();
        for (MultipartFile mf : files) {
            if (mf.getSize() == 0) continue;

            String safeFileName = fileService.upload(folderPath, mf);
            if (safeFileName == null) {
                return false;
            }
            GalleryFile galleryFile = new GalleryFile(gallery, safeFileName, mf.getOriginalFilename(), mf.getSize());
            gallery.addFiles(galleryFile);
        }

        return true;
    }

    @Override
    public Gallery findGalleryDetail(long galleryId, boolean b) {

        boolean exists = galleryRepository.existsGalleryDetailByIdAndPub(galleryId, true);
        if (!exists) { //글이 존재 하지 않으면 에러 처리
           return null;
        }

        Optional<Gallery> gallery_ = galleryRepository.findGalleryDetailByIdAndPub(galleryId, true);
        if (gallery_.isEmpty()) {
            return null;
        }
        Gallery gallery = gallery_.get();
        gallery.getFiles().forEach(f -> {
            f.setFileName(Utils.urlEncode(f.getFileName()));
            f.setOriginalFileName(Utils.urlEncode(f.getOriginalFileName()));
        });
        return gallery;
    }

    @Transactional
    @Override
    public boolean deleteGallery(long id) {

        final Optional<Gallery> gallery_ = galleryRepository.findById(id);
        if (gallery_.isEmpty())
            return false;
        Gallery gallery = gallery_.get();
        List<Long> fileIds = gallery.getFiles().stream().map(GalleryFile::getId).collect(Collectors.toList());

        galleryRepository.deleteAllFileByIds(fileIds);
        galleryRepository.deleteById(gallery.getId());

        String folderPath = FileService.galleryPath + SE + id;
        fileService.deleteFolder(folderPath);

        return true;
    }
}
