package com.yunnong.controller;

import com.yunnong.logic.UserMybatisLogic;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by joker on 2016/4/13.
 */
@Controller
@RequestMapping("/usermsg")
public class UserMsgController {

//    @Autowired
//    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private UserMybatisLogic userMybatisLogic ;

    @RequestMapping(value = "/u_info", method = RequestMethod.GET)
    @ResponseBody
    public String userInfo(@RequestParam long uid) {
        String result = userMybatisLogic.findUserByid(uid).toString();
        return result;
    }

    @RequestMapping(value = "/alluser", method = RequestMethod.GET)
    @ResponseBody
    public String allUser() {
        String result = userMybatisLogic.allUsers().toString();
        System.out.println("allUser---:"+result);
        return result;
    }
    /*@RequestMapping(value = "/u_edit", method = RequestMethod.POST)
    @ResponseBody
    public String userEdit(@RequestBody JSONObject body) {
        UserManagerLogic userManagerLogic = new UserManagerLogic();
        String result = userManagerLogic.userInfo(uid);
        return result;
    }*/
}
