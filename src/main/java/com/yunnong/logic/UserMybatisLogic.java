package com.yunnong.logic;

import com.yunnong.domain.User;
import com.yunnong.mapper.UserMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by joker on 2016/4/26.
 */
public class UserMybatisLogic {
    private SqlSessionFactory sqlSessionFactory;
    private UserMapper userMapper;

    public UserMybatisLogic(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
        this.userMapper = sqlSessionFactory.openSession().getMapper(UserMapper.class);
    }

    public JSONObject allUsers(){
        JSONObject callback = new JSONObject();
        callback.put("api", "alluser");
        callback.put("result", 602);
        try {
            List<User> users = userMapper.queryAllUsers();
            JSONArray data = JSONArray.fromObject(users);
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return callback;
    }

    public JSONObject findUserByid(long uid){
        JSONObject callback = new JSONObject();
        callback.put("api", "u_info");
        callback.put("result", 602);
        try {
            User user = userMapper.queryUser(uid);
            JSONObject data = JSONObject.fromObject(user);
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return callback;
    }
}
