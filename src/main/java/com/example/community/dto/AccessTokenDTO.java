package com.example.community.dto;

/**
 * 授权对象传输模型
 *封装GitHub授权登录传入的数据
 */
public class AccessTokenDTO {
    private String client_id;   //github识别请求客户端的id
    private String client_secret;
    private String code;        //GitHub授权后传回，客户端再将code发送至GitHub来请求token
    private String redirect_uri;    //用户确认授权后GitHub的回调路径
    private String state;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
