package com.example.community.mapper;

import com.example.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user values(null,#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified})")
    int insert(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);
}
