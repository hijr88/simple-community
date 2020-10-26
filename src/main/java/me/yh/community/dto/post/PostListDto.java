package me.yh.community.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostListDto {

    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createDate;
    private Long recommend;
    private Long hit;
    private int dept;
    private Long comment;

    @QueryProjection
    public PostListDto(Long id, String title, String writer, LocalDateTime createDate, Long recommend, Long hit, int dept) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createDate = createDate;
        this.recommend = recommend;
        this.hit = hit;
        this.dept = dept;
    }
}
