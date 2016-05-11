package com.yunnong.logic;

import com.google.gson.Gson;
import com.yunnong.domain.*;
import com.yunnong.mapper.ConMapper;
import com.yunnong.mapper.OrderMapper;
import com.yunnong.mapper.OrderTimeMapper;
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

/**
 * Created by joker on 2016/4/27.
 */
public class SerMybatiesLogic {
    private final static org.slf4j.Logger logger
            = LoggerFactory.getLogger(SerMybatiesLogic.class);
    private SqlSessionFactory sqlSessionFactory;
    private OrderMapper orderMapper;
    private OrderTimeMapper orderTimeMapper;
    private ConMapper conMapper;

    private final static String TEN_AM = "10:00";
    private final static String TWO_PM = "14:00";
    private final static String THREE_HALF_PM = "15:30";

    public SerMybatiesLogic(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
        this.orderMapper = sqlSessionFactory.openSession().getMapper(OrderMapper.class);
        this.orderTimeMapper = sqlSessionFactory.openSession().getMapper(OrderTimeMapper.class);
        this.conMapper = sqlSessionFactory.openSession().getMapper(ConMapper.class);
    }

    /**
     * @param body 新订单
     * @return 返回callback
     */
    public String orderInsert(JSONObject body) {
        JSONObject callback = new JSONObject();
        callback.put("api", "order");
        callback.put("result", 602);

        Gson gson = new Gson();
        Order order = gson.fromJson(body.toString(), Order.class);
        order.setOid(UidUtils.chOIdNext());
        try {
            OrderTime orderTime = new OrderTime();
            int time_value = body.getInt("time");
            String time = null;
            if (time_value == 10) {
                orderTime.setTen_am(1);
                order.setDate(order.getDate() + " " + TEN_AM);
            } else if (time_value == 14) {
                orderTime.setTwo_pm(1);
                order.setDate(order.getDate() + " " + TWO_PM);
            } else {
                orderTime.setThree_h_pm(1);
                order.setDate(order.getDate()+" "+THREE_HALF_PM);
            }
            //生成新的订单
            orderMapper.create(order);

            //判断pro_time是否已有
            OrderTime ot = new OrderTime();
            ot.setPid(body.getLong("pid"));
            ot.setDate(body.getString("date"));
            List<Long> result = orderTimeMapper.findTidByPidDate(ot);

            //判断pro_time是否已经存在该用户，该天的表
            if (result != null && result.size() != 0) {
                orderTime.setTid(result.get(0));
                orderTimeMapper.modify(orderTime);
            } else {
                orderTime.setPid(body.getLong("pid"));
                orderTime.setDate(body.getString("date"));
                orderTime.setTid(UidUtils.chTIdNext());
                orderTimeMapper.create(orderTime);
            }
            callback.replace("result",200);
        } catch (Exception e) {
            callback.replace("result", 603);
            LogUtils.LogError(logger, "update pro_time,sys_order failed", e);
        }
        return callback.toString();
    }

    /**
     * @return 所有的咨询师信息
     */
    public String consultantlist(){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_list");
        callback.put("result", 602);
        try {
            List<ConsultantTem> cons= conMapper.findNameImgDes();
            JSONArray data = JSONArray.fromObject(cons);
            callback.put("data", data);
            callback.replace("result",200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "find all consultant failed", e);
        }
        return callback.toString();
    }

    public String consultantInfo(Long pid){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_info");
        callback.put("result", 602);
        try {
            HashMap<String, Object> parm = new HashMap<String, Object>();
            parm.put("pid", pid);
            parm.put("date",  DateUtil.getCurrentDate());
            Map<String, Object> consultantTime = conMapper.findConTimeById(parm);
            consultantTime.replace("date", consultantTime.get("date").toString());
            JSONObject data = JSONObject.fromObject(consultantTime);
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger,
                    "find  consultant's order time by pid and date, failed", e);
        }
        return callback.toString();
    }

    /**
     * @param pid
     * @return
     */
    public String consultantBookInfo(Long pid){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_book");
        callback.put("result", 602);
        try {
            JSONArray data = new JSONArray();
            int offset = 0 ;
            for (int i=0;i<5;i++) {
                JSONObject book = new JSONObject();
                String date = DateUtil.getAnyDate(i + offset);
                if (DateUtil.getDayofweek(date) == 7) {
                    offset = 2;
                } else if (DateUtil.getDayofweek(date) == 1) {
                    offset = 1;
                } else {}
                date = DateUtil.getAnyDate(i + offset);
                OrderTime orderTime = new OrderTime();
                orderTime.setPid(pid);
                orderTime.setDate(date);
                OrderTimeTem ot = orderTimeMapper.findByPidDate(orderTime);

                if (ot != null){
                    book = JSONObject.fromObject(ot);
                    System.out.println("book---:"+book.toString());
                }else {
                    book.put("pid", pid);
                    book.put("date", date);
                    book.put("ten_am", 0);
                    book.put("two_pm", 0);
                    book.put("three_h_pm", 0);
                }
                data.add(book);
            }
            callback.put("data", data);
            callback.replace("result", 200);
        } catch (Exception e) {
            LogUtils.LogError(logger, "query consults " + pid + " book info ERROR!", e);
        }
        return callback.toString();
    }
}
