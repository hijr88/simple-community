package me.yh.community.dto.gallery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryListDto {
    private Long id;
    private String fileName;
    private int childCount;
}
