package com.yunnong.dao;

import com.yunnong.utils.ConnectionPool;
import com.yunnong.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by joker on 2016/3/22.
 */
public class OperDao {
    private final static Logger logger = LoggerFactory.getLogger(OperDao.class);

    private ConnectionPool connectionPool = null;

    public OperDao() {
        connectionPool = ConnectionPool.getInstance();
    }

    public void destroyPool() {
        connectionPool.closePool();
    }

    public int updateConsultant(String sql) {
        Connection conn = connectionPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            LogUtils.LogError(logger, "UPDATE DB ERROR!", e);
        }
        return 0;
    }

    public ResultSet findExcuteSql(String sql) {
        Connection conn = connectionPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            LogUtils.LogError(logger, "QUERY DB ERROR!", e);
        }
        return null;
    }
}
