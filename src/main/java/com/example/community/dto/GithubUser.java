package com.example.community.dto;

import lombok.Data;

@Data
public class GithubUser {

    private String name;//GitHub用户名称
    private long id;//用户的Github id,唯一标识
    private String bio;//用户描述
    private String avatar_url;  //用户头像图片URL
}

