package com.example.community.service;

import com.example.community.dto.PaginationDTO;
import com.example.community.dto.QuestionDTO;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.QuestionExtMapper;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.QuestionExample;
import com.example.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 分页查找所有问题，首页显示
     * @param page 当前页码
     * @param size  每页显示问题条数
     * @return  页面封装对象
     */
    public PaginationDTO list(Integer page, Integer size) {

        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1)
            page = 1;
        if (page > totalPage)
            page = totalPage;

        paginationDTO.setPagination(totalPage, page);

        Integer offset = size * (page - 1);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");        //查询的问题按照倒序排列
        List<Question> questionList =
                questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDTO> questionDtoList = new ArrayList<>();

        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDto = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDto);     //将question属性拷贝到questionDto
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDTO.setData(questionDtoList);

        return paginationDTO;
    }

    /**
     * 根据用户id分页查找问题
     * @param userId    用户id
     * @param page      当前页码数
     * @param size      每页显示问题条数
     * @return
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;

        //根据用户id查询用户总提问数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1)
            page = 1;
        if (page > totalPage)
            page = totalPage;

        paginationDTO.setPagination(totalPage, page);

        //根据用户id分页查询用户的提问
        Integer offset = size * (page - 1);
        List<Question> questionList =
                questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));
        List<QuestionDTO> questionDtoList = new ArrayList<>();

        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDto = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDto);     //将question属性拷贝到questionDto
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDTO.setData(questionDtoList);

        return paginationDTO;
    }

    /**
     * 根据问题id查找问题
     * @param id    查找的问题id
     * @return
     */
    public QuestionDTO getById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        //根据问题id查询问题
        Question question = questionMapper.selectByPrimaryKey(id);
        //如果问题不存在，则抛出异常
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question, questionDTO);
        //根据问题创建者id查询提问用户
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 创建或者更新问题
     * @param question
     */
    public void createOrUpdate(Question question) {
        //问题id为空，新建问题
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            //问题id不为空，修改问题
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(question.getGmtCreate());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update=questionMapper.updateByExampleSelective(updateQuestion, example);
            //问题更新失败，抛出异常
            if(update !=1)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }

    /**
     * 增加问题阅读数
     * @param id    查看的问题id
     */
    public void incView(Long id) {
        //设置只需要更新的部分
        Question question=new Question();
        question.setId(id);         //设置更新的问题id
        question.setViewCount(1);   //设置阅读数增加步长
        questionExtMapper.incView(question);

    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag()))
            return new ArrayList<>();
        String [] tags=StringUtils.split(queryDTO.getTag(),",");
        String regexpTag= Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions=questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS=questions.stream().map(q->{
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
