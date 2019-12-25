package com.example.community.service;

import com.example.community.dto.CommentDTO;
import com.example.community.enums.CommentTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.*;
import com.example.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;
    /**
     * 添加评论
     * @param comment
     */
    @Transactional      //使整个方法具有事务性
    public void insert(Comment comment) {
        //判断评论所属的问题是否存在
        if(comment.getParentId()==null||comment.getParentId()==0)
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);

        //判断评论类型是否正确
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType()))
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);

        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //评论为评论的子评论
            Comment dbComment=commentMapper.selectByPrimaryKey(comment.getParentId());
            //回复的评论不存在
            if(dbComment==null)
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);

            commentMapper.insert(comment);

            //使该评论的父评论评论数增加一
            Comment parentComment =new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
        }else{
            //评论为问题的评论
            Question question=questionMapper.selectByPrimaryKey(comment.getParentId());
            //回复的问题不存在
            if(question==null)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);

            commentMapper.insert(comment);
            question.setCommentCount(1);        //设置评论增加步长为一
            questionExtMapper.incCommentCount(question);    //增加问题评论数
        }
    }

    /**
     * 进入问题页面时自动查询评论
     * @param id    问题id
     * @param type  评论类型
     * @return
     */
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample=new CommentExample();
        //根据问题id获取该问题下的所有评论、保证回复类型为问题回复
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");  //查询按评论创建时间倒序排序
        List<Comment> comments=commentMapper.selectByExample(commentExample);

        if(comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人id
        Set<Long> commentators =comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转化为Map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users=userMapper.selectByExample(userExample);
        Map<Long,User> userMap=users.stream().collect(Collectors.toMap(user -> user.getId(),user->user));

        //转化comment为commentDTO
        List<CommentDTO> commentDTOS=comments.stream().map(comment->{
            CommentDTO commentDTO=new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
