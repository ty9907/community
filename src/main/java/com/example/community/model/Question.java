package com.example.community.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Question {

    private Integer id;     //问题id
    private String title;       //问题标题
    private String description;     //问题详细描述
    private Long gmtCreate;     //问题创建时间
    private Long gmtModified;   //创建时间戳
    private Integer creator;    //提问者id
    private Integer commentCount;   //问题评论数
    private Integer viewCount;      //问题浏览数
    private Integer likeCount;      //问题点赞数
    private String tag;             //问题标签

}
