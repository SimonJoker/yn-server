package com.yunnong.logic;

import com.yunnong.dao.OperDao;
import com.yunnong.utils.DateUtil;
import com.yunnong.utils.LogUtils;
import com.yunnong.utils.UidUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by joker on 2016/3/28.
 * 老版本，此文件暂停使用
 */
public class ServiceLogic {

    private final static org.slf4j.Logger logger
            = LoggerFactory.getLogger(ServiceLogic.class);

    private final static String SYS_ORDER = "sys_order";
    private final static String PRO_TIME  = "pro_time";

    private final static String TEN_AM = "10:00";
    private final static String TWO_PM = "14:00";
    private final static String THREE_HALF_PM = "15:30";

    private OperDao operDao;

    public ServiceLogic(){
        operDao = new OperDao();
    }

    /**
     * @param body 新订单
     *
     * @return 返回callback
     */
    public String orderInsert(JSONObject body) {
        JSONObject callback = new JSONObject();
        callback.put("api", "order");
        callback.put("result", 602);

        StringBuffer sql = null;
        StringBuffer proSql = null;
        try {
            sql = new StringBuffer("INSERT INTO ").append(SYS_ORDER)
                    .append("(oid,pid,uid,date,time) VALUE (").append(UidUtils.chOIdNext())
                    .append(",").append(body.getLong("pid")).append(",")
                    .append(body.getLong("uid")).append(",\'")
                    .append(body.getString("date")).append("\',");
            String time = null;
            String pro_time_time = null;
            int time_value = body.getInt("time");
            if (time_value == 10){
                time = TEN_AM;
                pro_time_time = "ten_am";
            }else if (time_value == 14){
                time = TWO_PM;
                pro_time_time = "two_pm";
            }else {
                time = THREE_HALF_PM;
                pro_time_time = "three_h_pm";
            }
            sql.append("\'").append(time).append("\')");

            //判断pro_time是否已有
            StringBuffer sqlExist = new StringBuffer("SELECT tid FROM ")
                    .append(PRO_TIME).append(" where ").append("pid=").append(body.getLong("pid"))
                    .append(" AND date=").append("\'").append(body.getString("date")).append("\'");

            proSql = new StringBuffer();
            //判断pro_time是否已经存在该用户，该天的表
            if (isExistInProTime(sqlExist.toString())
                    && pro_time_time != null && !"".equals(pro_time_time)){
                proSql.append("UPDATE ").append(PRO_TIME)
                        .append(" SET ").append(pro_time_time).append("=1 WHERE ")
                        .append("pid=").append(body.getLong("pid")).append(" AND ")
                        .append("date=").append("\'").append(body.getString("date")).append("\'");
            }else {
                proSql.append("INSERT INTO ").append(PRO_TIME)
                        .append("(pid,date,").append(pro_time_time).append(") VALUE (").append(body.getLong("pid"))
                        .append(",\'").append(body.getString("date")).append("\',").append(1).append(")");
            }
        } catch (Exception e) {
            callback.replace("result", 603);
            LogUtils.LogError(logger, "update pro_time,sys_order failed", e);
        }

        String sqlChar = sql.toString();
        String proSqlCh = proSql.toString();
        LogUtils.LogInfo(logger, "insert pro_time, sql = " + proSqlCh);
        LogUtils.LogInfo(logger, "insert sys_order, sql = " + sqlChar);

        if (0 != operDao.updateConsultant(sqlChar)
                && 0 != operDao.updateConsultant(proSqlCh)) {
            callback.replace("result", 200);
        }
        return callback.toString();

    }

    /**
     * 判断是否存在满足条件的pro_time
     * @param sql
     * @return
     */
    private boolean isExistInProTime(String sql){
        LogUtils.LogInfo(logger, "query db, sql = " + sql);
        if (operDao.findExcuteSql(sql) != null){
            return true;
        }
        return false;
    }

    /**
     * @return 所有的咨询师列表
     */
    public String consultantList(){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_list");
        callback.put("result", 602);
        String sql = "SELECT p_name,image,description FROM sys_consultant" ;
        ResultSet rs = operDao.findExcuteSql(sql);
        try {
            JSONArray data = new JSONArray();
            while(rs.next()){
                JSONObject con = new JSONObject();
                con.put("name", rs.getString(1));
                con.put("image", rs.getString(2));
                con.put("description", rs.getString(3));
                data.add(con);
            }
            callback.put("data",data);
            callback.replace("result", 200);
        } catch (SQLException e) {
            callback.replace("result",603);
            LogUtils.LogError(logger, "query consults list  ERROR!", e);
        }
        LogUtils.LogInfo(logger, "consultant list callback---:" + callback.toString());
        return callback.toString();
    }


    /**
     * @param pid 咨询师id
     * @return 返回咨询师信息，以及当天的预约情况
     */
    public String consultantInfo(String pid){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_info");
        callback.put("result", 602);
        String sql = "SELECT sys_consultant.pid,sys_consultant.p_name,sys_consultant.phone," +
                "sys_consultant.sex,sys_consultant.image,sys_consultant.weixin," +
                "sys_consultant.business_skills,sys_consultant.business_scope," +
                "pro_time.date,pro_time.ten_am,pro_time.two_pm,pro_time.three_h_pm " +
                "FROM sys_consultant, pro_time " + "WHERE  sys_consultant.pid = pro_time.pid AND " +
                "sys_consultant.pid = " + pid + " AND pro_time.date = \'" + DateUtil.getCurrentDate() +"\'";
        try {
            ResultSet rs = operDao.findExcuteSql(sql);
            JSONObject data = new JSONObject();
            if (rs != null && rs.next()){
                data.put("pid", rs.getLong(1));
                data.put("name", rs.getString(2));
                data.put("phone", rs.getString(3));
                data.put("sex", rs.getString(4));
                data.put("image", rs.getString(5));
                data.put("weixin", rs.getString(6));
                data.put("skills", rs.getString(7));
                data.put("scope", rs.getString(8));
                data.put("date", rs.getString(9));
                data.put("ten_am", rs.getInt(10));
                data.put("two_pm", rs.getInt(11));
                data.put("three_h_pm", rs.getInt(12));
                callback.put("data", data);
                callback.replace("result", 200);
            }else {
                String sqlConsult = "SELECT sys_consultant.pid,sys_consultant.p_name,sys_consultant.phone," +
                        "sys_consultant.sex,sys_consultant.image,sys_consultant.weixin," +
                        "sys_consultant.business_skills,sys_consultant.business_scope " +
                        "FROM sys_consultant "+
                        "WHERE sys_consultant.pid=" + pid;
                ResultSet rsCon = operDao.findExcuteSql(sqlConsult);
                if (rs != null && rsCon.next()) {
                    data.put("pid", rsCon.getLong(1));
                    data.put("name", rsCon.getString(2));
                    data.put("phone", rsCon.getString(3));
                    data.put("sex", rsCon.getString(4));
                    data.put("image", rsCon.getString(5));
                    data.put("weixin", rsCon.getString(6));
                    data.put("skills", rsCon.getString(7));
                    data.put("scope", rsCon.getString(8));
                    data.put("date", DateUtil.getCurrentDate());
                    data.put("ten_am", 0);
                    data.put("two_pm", 0);
                    data.put("three_h_pm", 0);
                    callback.put("data", data);
                    callback.replace("result", 200);
                }else {
                    callback.replace("result", 603);
                }

            }
        } catch (SQLException e) {
            LogUtils.LogError(logger, "query consults "+pid+" ERROR!", e);
        }
        return callback.toString();
    }

    /**
     * @param pid
     * @return 咨询师预定信息
     */
    public String consultantBookInfo(String pid){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_book");
        callback.put("result", 602);
        JSONArray data = new JSONArray();
        int offset = 0 ;
        try {
            for (int i=0;i<5;i++){
                JSONObject book = new JSONObject();
                String date = DateUtil.getAnyDate(i+offset);
                if (DateUtil.getDayofweek(date) == 7){
                    offset = 2;
                }else if (DateUtil.getDayofweek(date) == 1){
                    offset = 1;
                }else{}

                date = DateUtil.getAnyDate(i+offset);
                String sql = "SELECT pid,date,ten_am,two_pm,three_h_pm FROM pro_time WHERE pid="+
                        pid + " AND date=\'" + date +"\'";
                ResultSet rs = operDao.findExcuteSql(sql);
                if (rs != null && rs.next()) {
                    book.put("pid", rs.getLong(1));
                    book.put("date", rs.getString(9));
                    book.put("ten_am", rs.getInt(10));
                    book.put("two_pm", rs.getInt(11));
                    book.put("three_h_pm", rs.getInt(12));
                }else {
                    book.put("pid", Long.valueOf(pid));
                    book.put("date", date);
                    book.put("ten_am", 0);
                    book.put("two_pm", 0);
                    book.put("three_h_pm", 0);
                }
                data.add(book);
            }
            callback.replace("result", 200);
            callback.put("data", data);
        } catch (SQLException e) {
            LogUtils.LogError(logger, "query consults " + pid + " book info ERROR!", e);
        }
        return  callback.toString();
    }

    /**
     * 释放数据库连接资源
     */
    public void destroyResource(){
        operDao.destroyPool();
    }


}
