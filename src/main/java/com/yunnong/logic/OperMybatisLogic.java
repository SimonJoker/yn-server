package com.yunnong.logic;

import com.google.gson.Gson;
import com.yunnong.domain.Consultant;
import com.yunnong.domain.ConsultantOp;
import com.yunnong.mapper.ConMapper;
import com.yunnong.mapper.OrderMapper;
import com.yunnong.mapper.UserMapper;
import com.yunnong.utils.DateUtil;
import com.yunnong.utils.LogUtils;
import com.yunnong.utils.UidUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by joker on 2016/4/27.
 */
public class OperMybatisLogic {
    private final static org.slf4j.Logger logger
            = LoggerFactory.getLogger(OperMybatisLogic.class);

    private SqlSessionFactory sqlSessionFactory;
    private ConMapper conMapper;
    private OrderMapper orderMapper;

    public OperMybatisLogic(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
        this.conMapper = sqlSessionFactory.openSession().getMapper(ConMapper.class);
        this.orderMapper = sqlSessionFactory.openSession().getMapper(OrderMapper.class);
    }

    public String consultantInsert(JSONObject body){
        JSONObject callback = new JSONObject();
        callback.put("api", "consultant");
        callback.put("result", 602);
        body.put("pid",UidUtils.chPIdNext());
        Gson gson = new Gson();
        Consultant consultant = gson.fromJson(body.toString(), Consultant.class);
        LogUtils.LogInfo(logger, "create consultant---:" + body.toString());
        try {
            conMapper.create(consultant);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "create consultant error!", e);
        }
        return callback.toString();
    }

    public String consultantUpdate(JSONObject body){
        JSONObject callback = new JSONObject();
        callback.put("api", "ch_consultant");
        callback.put("result", 602);
        try {
            Gson gson = new Gson();
            Consultant consultant = gson.fromJson(body.toString(), Consultant.class);
            conMapper.modify(consultant);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "update consult failed", e);
        }
        return callback.toString();
    }

    /**
     * @return All consultants
     */
    public String opConAll(){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_all");
        callback.put("result", 602);
        try {
            List<ConsultantOp> allCon = conMapper.findConAllOp();
            JSONArray data = JSONArray.fromObject(allCon);
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "oper query all consults failed", e);
        }
        return callback.toString();
    }


    /**
     * @return
     */
    public String opConTime(){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_time");
        callback.put("result", 602);
        try {
            JSONArray data = new JSONArray();

            List<Consultant> allCon = conMapper.findAll();
            allCon.forEach(c ->{
                int offset = 0;
                Map<String, Object> parm = new HashMap<String, Object>();
                parm.put("pid", c.getPid());
                for (int i=-2;i<3 ;i++) {
                    JSONObject book = new JSONObject();
                    String date = DateUtil.getAnyDate(i + offset);
                    if (DateUtil.getDayofweek(date) == 7) {
                        offset = 2;
                    } else if (DateUtil.getDayofweek(date) == 1) {
                        offset = 1;
                    } else {
                    }
                    date = DateUtil.getAnyDate(i + offset);
                    parm.put("date", date);
                    HashMap<String, Object> opConT
                            = conMapper.findConTimeAll(parm);
                    if (opConT != null){
                        opConT.replace("date",opConT.get("date").toString());
                        book = JSONObject.fromObject(opConT);
                    }else {
                        book.put("pid", c.getPid());
                        book.put("p_nam", c.getP_name());
                        book.put("date", date);
                        book.put("ten_am", 0);
                        book.put("two_pm", 0);
                        book.put("three_h_pm", 0);
                    }
                    data.add(book);
                }
            });
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "oper query all consults' time failed", e);
        }
        return callback.toString();
    }


    /**
     * 创建匹配 OrderMapper -> List<HashMap<String, Object>>的接口
     */
    @FunctionalInterface
    public interface QueryDB {
        List<HashMap<String, Object>>  process(OrderMapper om);
    }

    /**
     * @param q Lamda接口参数
     * @param api  处理的接口请求
     * @return
     */
    public String orderLamdaList(QueryDB q, String api){
        JSONObject callback = new JSONObject();
        callback.put("api", api);
        callback.put("result", 602);
        try {
            List<HashMap<String, Object>> newOrders
                    = q.process(orderMapper);
            newOrders.forEach(no -> {
                no.replace("date"
                        , DateUtil.timestamp2string(no.get("date")));
            });
            JSONArray data = JSONArray.fromObject(newOrders);
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "query new orders failed", e);
        }
        return callback.toString();
    }

    /**
     * @param oid
     * @return Detail of order.
     */
    public String orderDetail(Long oid){
        JSONObject callback = new JSONObject();
        callback.put("api", "or_detail");
        callback.put("result", 602);
        try {
            HashMap<String, Object> orders
                    = orderMapper.findOrderDetail(oid);
            orders.replace("date"
                        , DateUtil.timestamp2string(orders.get("date")));
            JSONObject data = JSONObject.fromObject(orders);
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "query new orders failed", e);
        }
        return callback.toString();
    }


}
