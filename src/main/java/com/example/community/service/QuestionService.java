package com.example.community.service;

import com.example.community.dto.QuestionDto;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.User;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDto> list(){
        List<Question> questionList=questionMapper.list();
        List<QuestionDto> questionDtoList=new ArrayList<>();
        for (Question question : questionList) {
            User user=userMapper.findById(question.getCreator());
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);     //将question属性拷贝到questionDto
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        return questionDtoList;
    }
}
