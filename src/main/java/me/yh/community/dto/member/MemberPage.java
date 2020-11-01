package me.yh.community.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.yh.community.dto.post.PostListDto;


import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPage {

    private String field;
    private String query;
    private Long current;  //현재 페이지 번호

    private List<MemberListDto> content;
    private Long totalCount;
    private Long pageMaxNum;

    public MemberPage(String field, String query, Long current) {
        this.field = field;
        this.query = query;
        this.current = current;
    }
}
