package com.yunnong.controller;

import com.yunnong.logic.OperMybatisLogic;
import com.yunnong.mapper.OrderMapper;
import com.yunnong.utils.DateUtil;
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
 * Created by joker on 2016/3/22.
 */
@Controller
@RequestMapping("/op")
public class OperationsController {

    private final static Logger logger = LoggerFactory.getLogger(OperationsController.class);
    private final static Integer ORDER_OVERTIME = 4;

    @Autowired
    private OperMybatisLogic operMybatisLogic;

    /**
     *新建“咨询师”时，上传咨询师信息
     * @param body post 中的具体内容
     * @param session  用于验证用户信息
     * @return
     */
    @RequestMapping(value = "/consultant", method = RequestMethod.POST)
    @ResponseBody
    public String consultantUpload(@RequestBody String body, HttpSession session) {
        LogUtils.LogInfo(logger, "insert consultant body = "+body);
        if (body != null && !"".equals(body)){
            String callback =
                    operMybatisLogic.consultantInsert(JSONObject.fromObject(body));
            return callback;
        }
        return ErrorsCallbackUtils.requestbodyEmpty("consultant");
    }

    @RequestMapping(value = "/ch_consultant", method = RequestMethod.POST)
    @ResponseBody
    public String consultantChange(@RequestBody String body, HttpSession session) {
        if (body != null && !"".equals(body)){
            String callback =
                    operMybatisLogic.consultantUpdate(JSONObject.fromObject(body));
            return callback;
        }
        return ErrorsCallbackUtils.requestbodyEmpty("consultant");
    }

    @RequestMapping(value = "con_all", method = RequestMethod.GET)
    @ResponseBody
    public String consultantAll(HttpSession session) {
        String callback =
                operMybatisLogic.opConAll();
        return callback;
    }

    @RequestMapping(value = "con_time", method = RequestMethod.GET)
    @ResponseBody
    public String consultantTime(HttpSession session) {
        String callback =
                operMybatisLogic.opConTime();
        return callback;
    }

    @RequestMapping(value = "or_sta", method = RequestMethod.GET)
    @ResponseBody
    public String orderNew(@RequestParam Integer status, HttpSession session) {
        String callback = null;
        if(status != ORDER_OVERTIME ){
            callback = operMybatisLogic
                    .orderLamdaList((OrderMapper or) ->
                            or.findOrderByStatus(status), "or_sta");
        }else {
            callback = operMybatisLogic
                    .orderLamdaList((OrderMapper or) ->
                            or.findOrderOverTime(DateUtil.getCurrentDate()), "or_sta");
        }
        return callback;
    }


    @RequestMapping(value = "or_cancle", method = RequestMethod.GET)
    @ResponseBody
    public String orderCancle(HttpSession session) {
        String callback =
                operMybatisLogic.orderLamdaList((OrderMapper or)
                        -> or.findOrderCancle(), "or_cancle");
        return callback;
    }

    @RequestMapping(value = "or_detail", method = RequestMethod.GET)
    @ResponseBody
    public String orderDetail(@RequestParam Long oid, HttpSession session) {
        String callback =
                operMybatisLogic.orderDetail(oid);
        return callback;
    }

    @RequestMapping(value = "ch_constatus", method = RequestMethod.POST)
    @ResponseBody
    public String conChStatus(@RequestBody JSONObject body, HttpSession session) {
        String callback =
                operMybatisLogic.updateConStatus(body);
        return callback;
    }

    @RequestMapping(value = "ch_orstatus", method = RequestMethod.POST)
    @ResponseBody
    public String orderChStatus(@RequestBody JSONObject body, HttpSession session) {
        String callback =
                operMybatisLogic.updateOrdersStatus(body);
        return callback;
    }
}
