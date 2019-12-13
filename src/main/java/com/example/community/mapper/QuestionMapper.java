package com.example.community.mapper;

import com.example.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper()
public interface QuestionMapper {

    @Insert("insert into question values(null,#{title},#{description}," +
            "#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount}," +
            "#{likeCount},#{tag})")
    int create(Question question);

    /*发布问题时未设置commentCount，likeCount等，直接查询传入null
   采用ifnull或者nvl函数可将null转换为默认值，
   但必须设置别名写回，不然查询到的列为ifnull(like_count,0)或者nvl(like_count,0) */
    @Select("select id,title,description,gmt_create,gmt_modified,creator," +
            "ifnull(comment_count,0) comment_count," +
            "ifnull(view_count,0) view_count," +
            "ifnull(like_count,0) like_count,tag from question limit #{offset} ,#{size}")
    //传入非对象的参数时需要@Param进行参数映射
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select id,title,description,gmt_create,gmt_modified,creator," +
            "ifnull(comment_count,0) comment_count," +
            "ifnull(view_count,0) view_count," +
            "ifnull(like_count,0) like_count,tag from question " +
            "where creator=#{userId} limit #{offset} ,#{size}")
    List<Question> listByUserId(@Param(value="userId") Integer userId, @Param(value="offset") Integer offset,
                                @Param(value="size") Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer countByUserId(@Param(value="userId") Integer userId);
}
