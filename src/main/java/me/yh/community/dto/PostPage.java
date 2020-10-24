package me.yh.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostPage {

    private String field;
    private String query;
    private Long num;
}
