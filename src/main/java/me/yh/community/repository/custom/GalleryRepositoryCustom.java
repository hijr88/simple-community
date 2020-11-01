package me.yh.community.repository.custom;

import me.yh.community.dto.gallery.GalleryListDto;

import java.util.List;

public interface GalleryRepositoryCustom {

    List<GalleryListDto> findListByPageAndPub(long page, Boolean pub);

    List<GalleryListDto> findListByPage(long page);
}
