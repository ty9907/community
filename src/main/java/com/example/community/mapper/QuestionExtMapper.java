package com.example.community.mapper;

import com.example.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
        int incView(Question question);
        int incCommentCount(Question question);
        List<Question> selectRelated(Question question);        //根据问题标签查询相关问题
}
