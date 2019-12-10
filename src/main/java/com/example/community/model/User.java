package com.example.community.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class User {
    private Integer id;     //用户id
    private String accountId;       //用户GitHub id
    private String name;        //用户GitHub名称
    private String token;
    private Long gmtCreate;     //创建时间
    private Long gmtModified;   //创建时间戳
    private String bio;         //用户描述
    private String avatarUrl;      //用户头像图片URL

}
