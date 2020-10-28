package me.yh.community.dto.comment;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentList {
    private Long id;  //cno
    private String content;
    private String memberId; //id
    private String nickname; //name
    private String profileImage;
    private LocalDateTime createDate; //regDate
    private boolean delete;  //delete
    private Long childCount; //count

    @QueryProjection
    public CommentList(Long id, String content, String memberId, String nickname, String profileImage, LocalDateTime createDate, boolean delete, Long childCount) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createDate = createDate;
        this.delete = delete;
        this.childCount = childCount;
    }

    @QueryProjection
    public CommentList(Long id, String content, String memberId, String nickname, String profileImage, LocalDateTime createDate, boolean delete) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createDate = createDate;
        this.delete = delete;
    }
}
