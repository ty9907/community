package com.example.community.dto;

import lombok.Data;

/**
 * 封装的页面传送过来的评论信息
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
