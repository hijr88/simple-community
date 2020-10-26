package me.yh.community.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostDetailDto {

    private Long postId;
    private String title;
    private String content;
    private String memberId;
    private String nickname;
    private String profileImage;
    private LocalDateTime createDate;
    private Long hit;
    private Long recommend;
    private String fileName;
    private String originalFileName;

    @QueryProjection
    public PostDetailDto(Long postId, String title, String content, String memberId, String nickname, String profileImage, LocalDateTime createDate, Long hit, Long recommend, String fileName, String originalFileName) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createDate = createDate;
        this.hit = hit;
        this.recommend = recommend;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
    }
}
