package com.example.community.dto;

import lombok.Data;

import java.util.List;

/**
 * 问题标签模型
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
