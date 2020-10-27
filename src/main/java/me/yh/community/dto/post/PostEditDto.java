package me.yh.community.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostEditDto {

    private String title;
    private String content;
    private String memberId;
    private String originalFileName;

    @QueryProjection
    public PostEditDto(String title, String content, String memberId, String originalFileName) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.originalFileName = originalFileName;
    }
}
