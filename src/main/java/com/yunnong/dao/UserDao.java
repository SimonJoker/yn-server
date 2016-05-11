package com.yunnong.dao;

import com.yunnong.utils.ConnectionPool;
import com.yunnong.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by joker on 2016/3/21.
 */
public class UserDao {
    private final static Logger logger = LoggerFactory.getLogger(UserDao.class);

    private ConnectionPool connectionPool = null;
    public UserDao(){
        connectionPool = ConnectionPool.getInstance();
    }

    public void insertUser (){
        String sql = "insert into sys_user(realname, nickname, sex, phone, company, position, role)" +
                " value ('张三', '三丰大爷', '男', '86-18518380900', '耘农企业管理咨询公司', '高级工程师', 'root')";
        Connection conn = connectionPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sql);
            System.out.println("insert rs --: "+rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateUser(String sql) {
        Connection conn = connectionPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            LogUtils.LogError(logger, "UPDATE DB ERROR!", e);
        }
        return 0;
    }

    public ResultSet queryUser(){
        String sql = "SELECT * FROM sys_user";
        Connection conn = connectionPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("insert rs --: "+rs);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param sql 自定义查询条件
     * @return
     */
    public ResultSet queryUser(String sql){
        Connection conn = connectionPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("insert rs --: "+rs);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void destroyPool(){
        connectionPool.closePool();
    }
}
