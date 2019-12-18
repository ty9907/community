package com.example.community.controller;

import com.example.community.dto.PaginationDTO;
import com.example.community.model.User;
import com.example.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    /**
     * 个人操作页面
     * @param action    分为question(我的提问)和replies(最新回复)
     * @param model
     * @param request
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value="/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          Model model,HttpServletRequest request,
                          @RequestParam(name="page",defaultValue = "1")Integer page,
                          @RequestParam(name="size",defaultValue = "2")Integer size){
        User user = (User) request.getSession().getAttribute("user");

        if(user==null)
            return "redirect:/index";
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }
        if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        PaginationDTO pagination = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination",pagination);
        return "profile";
    }
}
