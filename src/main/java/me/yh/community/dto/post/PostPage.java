package me.yh.community.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostPage {

    private String field;
    private String query;
    private Long current;  //현재 페이지 번호

    private List<PostListDto> content;
    private Long totalCount;
    private Long pageMaxNum;

    public PostPage(String field, String query, Long current) {
        this.field = field;
        this.query = query;
        this.current = current;
    }
}
