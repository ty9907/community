package com.example.community.service;

import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.User;
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

    public PaginationDTO list(Integer page, Integer size){

        Integer totalPage;
        PaginationDTO paginationDTO=new PaginationDTO();

        Integer totalCount=questionMapper.count();

        if(totalCount%size==0) {
            totalPage = totalCount / size;
        }
        else {
           totalPage = totalCount / size + 1;
        }

        if(page<1)
            page=1;
        if(page> totalPage)
            page=totalPage;

        paginationDTO.setPagination(totalPage,page);

        Integer offset=size*(page-1);
        List<Question> questionList=questionMapper.list(offset,size);
        List<QuestionDTO> questionDtoList=new ArrayList<>();

        for (Question question : questionList) {
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDto=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDto);     //将question属性拷贝到questionDto
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDTO.setQuestions(questionDtoList);

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalPage;
        Integer totalCount=questionMapper.countByUserId(userId);
        if(totalCount%size==0) {
            totalPage = totalCount / size;
        }
        else {
            totalPage = totalCount / size + 1;
        }

        if(page<1)
            page=1;
        if(page> totalPage)
            page=totalPage;

        paginationDTO.setPagination(totalPage,page);

        Integer offset=size*(page-1);
        List<Question> questionList=questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDtoList=new ArrayList<>();

        for (Question question : questionList) {
            User user=userMapper.findById(question.getCreator());
            QuestionDTO questionDto=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDto);     //将question属性拷贝到questionDto
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDTO.setQuestions(questionDtoList);

        return paginationDTO;
    }
}
