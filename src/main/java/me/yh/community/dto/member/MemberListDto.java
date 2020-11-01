package me.yh.community.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberListDto {

    private String id;
    private String nickname;
    private LocalDateTime createDate;
    private String role;

    @QueryProjection
    public MemberListDto(String id, String nickname, LocalDateTime createDate, String role) {
        this.id = id;
        this.nickname = nickname;
        this.createDate = createDate;
        this.role = role;
    }
}
