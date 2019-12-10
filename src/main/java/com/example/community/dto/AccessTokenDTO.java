package com.example.community.dto;

import lombok.Data;

/**
 * 授权对象传输模型
 *封装GitHub授权登录传入的数据
 */
@Data
public class AccessTokenDTO {
    private String client_id;   //github识别请求客户端的id
    private String client_secret;
    private String code;        //GitHub授权后传回，客户端再将code发送至GitHub来请求token
    private String redirect_uri;    //用户确认授权后GitHub的回调路径
    private String state;

}
