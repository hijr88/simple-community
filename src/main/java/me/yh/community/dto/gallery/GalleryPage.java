package me.yh.community.dto.gallery;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GalleryPage {

    private Long current;  //현재 페이지 번호

    private List<GalleryListDto> content;
    private Long totalCount;
    private Long pageMaxNum;

    public GalleryPage(Long current) {
        this.current = current;
    }

    public GalleryPage(Long current, List<GalleryListDto> content, Long totalCount, Long pageMaxNum) {
        this.current = current;
        this.content = content;
        this.totalCount = totalCount;
        this.pageMaxNum = pageMaxNum;
    }
}
