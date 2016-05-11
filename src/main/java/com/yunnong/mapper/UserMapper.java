package com.yunnong.mapper;

import com.yunnong.domain.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by joker on 2016/4/26.
 */
@Transactional
@Repository(value = "userMapper")
public interface UserMapper {

//    @Select("SELECT * FROM sys_user")
    List<User> queryAllUsers();

//    @Select("SELECT * FROM sys_user WHERE uid=#{uid}")
    User queryUser(long uid);

    Integer count();
}
