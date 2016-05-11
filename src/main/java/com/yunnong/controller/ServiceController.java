package com.yunnong.controller;

import com.yunnong.logic.SerMybatiesLogic;
import com.yunnong.logic.ServiceLogic;
import com.yunnong.utils.ErrorsCallbackUtils;
import com.yunnong.utils.LogUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by joker on 2016/3/28.
 */
@Controller
@RequestMapping("/ser")
public class ServiceController {
    private final static Logger logger = LoggerFactory.getLogger(ServiceController.class);
    @Autowired
    private SerMybatiesLogic serMybatiesLogic;

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public String newOrder(@RequestBody String body, HttpSession session) {
        LogUtils.LogInfo(logger, "new order body = " + body);
        if (body != null && !"".equals(body)){
            String callback =
                    serMybatiesLogic.orderInsert(JSONObject.fromObject(body));
            return callback;
        }
        return ErrorsCallbackUtils.requestbodyEmpty("order");
    }

    /**
     * @param session
     * @return 没有请求参数，返回所有的咨询师
     */
    @RequestMapping(value = "/con_list", method = RequestMethod.GET)
    @ResponseBody
    public String consultantList(HttpSession session) {
        LogUtils.LogInfo(logger, "consultant list get!" );
        String callback = serMybatiesLogic.consultantlist();
        return callback;
    }

    /**
     * @param pid
     * @param session
     * @return 获取咨询师个人信息，已经当天预约状况
     */
    @RequestMapping(value = "/con_info", method = RequestMethod.GET)
    @ResponseBody
    public String consultantInfo(@RequestParam String pid, HttpSession session) {
        LogUtils.LogInfo(logger, "consultant list get!" );
        String callback = serMybatiesLogic.consultantInfo(Long.valueOf(pid));
        System.out.println("consult info callback--:"+callback);
        return callback;
    }

    @RequestMapping(value = "/con_book", method = RequestMethod.GET)
    @ResponseBody
    public String consultantBook(@RequestParam Long pid, HttpSession session) {
        LogUtils.LogInfo(logger, "consultant book get!" );
        String callback = serMybatiesLogic.consultantBookInfo(pid);
        System.out.println("consult book callback--:"+callback);
        return callback;
    }

}
