package com.example.community.provider;

import com.alibaba.fastjson.JSON;
import com.example.community.dto.AccessTokenDTO;
import com.example.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    /**
     * 请求GitHub授权，返回access_token
     * @param accessTokenDTO  封装的GitHub请求数据
     * @return  用于获取用户数据的access_token
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType=MediaType.get("application/json; charset=utf-8");
        OkHttpClient client= new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request=new Request.Builder()
                .url("https://github.com/login/oauth/access_token")         //url写为http时，返回的string是HTML文档
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();     //获取token数据
                                //access_token=8aebae74c5b02be081bd9ac63ca1dd06824067d4&scope=user&token_type=bearer
            String[] split =string.split("&");  //分解GitHub返回值
            String tokenstr=split[0]; //获取GitHub返回token部分 access_token=8aebae74c5b02be081bd9ac63ca1dd06824067d4
            String token=tokenstr.split("=")[1];    //获取token值  8aebae74c5b02be081bd9ac63ca1dd06824067d4
            //String token=string.split(“&”)[0].split("=")[1]
            System.out.println(string);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * http://api.github.com/user?access_token=accessToken用以获取用户数据
     * @param accessToken  请求GitHub授权后饭后的access_token
     * @return  封装的GitHub用户数据
     */
    public GithubUser getUser(String accessToken){
        OkHttpClient client=new OkHttpClient();
        Request request =new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response reponse =client.newCall(request).execute();
            String string =reponse.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
