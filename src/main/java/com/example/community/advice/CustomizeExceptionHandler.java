package com.example.community.advice;

import com.alibaba.fastjson.JSON;
import com.example.community.dto.ResultDTO;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(Throwable e, Model model, HttpServletRequest request,
                  HttpServletResponse response) {
        String contentType=request.getContentType();
        ResultDTO resultDTO;
        if("application/json".equals(contentType)){
            //返回json
            if (e instanceof CustomizeException)
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            else
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer=response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return  null;
        }else{
            //返回错误页面跳转
            if (e instanceof CustomizeException)
                model.addAttribute("message", e.getMessage());
            else
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            return new ModelAndView("error");
        }

    }


}
