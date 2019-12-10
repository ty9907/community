package com.example.community.controller;

import com.example.community.dto.AccessTokenDTO;
import com.example.community.dto.GithubUser;
import com.example.community.mapper.UserMapper;
import com.example.community.model.User;
import com.example.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirectUrl}")
    private String redirectUrl;

    /**
     * github回调的URL处理事件
     * 通过GitHub传回的数据查找到GitHub的token
     * 利用token查找用户数据
     * 将GitHub用户数据提取出来存入本地数据库
     * 同时随机生成一个token字符串，并存入本地用户中
     * 将随机生成的token存入cookies中，作为判断登录状态依据
     * 最后重定向回主页
     * @param code
     * @param state
     * @param response
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();   //ctrl+shift+v 快速创建变量
        accessTokenDTO.setCode(code);                           //shift+enter  光标移至下一行头
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null&& githubUser.getId()!=0){

            //登录成功，存入用户信息，处于登录状态
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败，返回主页，处于未登录状态
            return "redirect:/";
        }
    }
}
