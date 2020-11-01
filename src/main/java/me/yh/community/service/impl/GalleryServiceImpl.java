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
        if (createFiles(gallery, files, folderPath)) return false;

        return true;
    }

    @Override
    public Gallery findGalleryDetail(long galleryId, Boolean pub) {

        boolean exists;
        Optional<Gallery> gallery_;

        if (pub != null) {
            exists = galleryRepository.existsGalleryDetailByIdAndPub(galleryId, pub);
            gallery_ = galleryRepository.findGalleryDetailByIdAndPub(galleryId, pub);
        } else {
            exists = galleryRepository.existsById(galleryId);
            gallery_ = galleryRepository.findGalleryDetailById(galleryId);
        }
        if (!exists) { //글이 존재 하지 않으면 에러 처리
           return null;
        }

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
    public boolean modifyGallery(Long id, String title, List<Long> deleteNo, List<String> deleteFileName, List<MultipartFile> newFiles) {

        Optional<Gallery> gallery_ = galleryRepository.findById(id);
        if (gallery_.isEmpty()) {
            return false;
        }
        Gallery gallery = gallery_.get();
        gallery.setTitle(title);

        String folderPath = FileService.galleryPath + SE +id;
        if (deleteNo != null) {
            galleryRepository.deleteAllFileByIds(deleteNo);
            deleteFileName.forEach(s -> {
                String filePath = folderPath + SE + s;
                fileService.deleteFile(filePath);
            });
        }

        if (createFiles(gallery, newFiles, folderPath)) return false;

        return true;
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

    private boolean createFiles(Gallery gallery, List<MultipartFile> files, String folderPath) {
        for (MultipartFile mf : files) {
            if (mf.getSize() == 0) continue;

            String safeFileName = fileService.upload(folderPath, mf);
            if (safeFileName == null) {
                return true;
            }
            GalleryFile galleryFile = new GalleryFile(gallery, safeFileName, mf.getOriginalFilename(), mf.getSize());
            gallery.addFiles(galleryFile);
        }
        return false;
    }
}
