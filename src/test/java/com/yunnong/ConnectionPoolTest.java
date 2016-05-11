package com.yunnong;

import com.yunnong.dao.OperDao;
import com.yunnong.logic.UserMybatisLogic;
import com.yunnong.utils.ConnectionPool;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by joker on 2016/3/21.
 */
public class ConnectionPoolTest {
    @Autowired
    private UserMybatisLogic userMybatisLogic;

    @Test
    public void poolTest() throws SQLException {

    }
}
