package com.yunnong.utils;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

@Component
public class ConnectionPool {
    private Vector<Connection> pool;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int poolSize = 1;
    private static ConnectionPool instance = null;

    //私有构造方法，禁止外部创建本类的对象，要想获得本类的对象，通过<code>getInstance</code>方法
    private ConnectionPool(){
    }

    /**
     * @param url 数据库服务器链接地址
     * @param username 用户名
     * @param password 密码
     * @param driverClassName 对应的数据库驱动
     * @param poolSize 连接线程池
     */
    public ConnectionPool(String url, String username, String password
            , String driverClassName, int poolSize) {
        System.out.println("构造函数");
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
        this.poolSize = poolSize;
        init();
    }

    //连接池初始化方法，读取属性文件的内容，建立连接池中的初始连接
    private void init(){
        pool = new Vector<Connection>(poolSize);
        addConnection();
    }

    //返回连接到连接池中
    public synchronized void release(Connection coon){
        pool.add(coon);
    }

    //关闭连接池中的所有数据库连接
    public synchronized void closePool(){
        for (int i = 0; i < pool.size(); i++) {
            try {
                ((Connection)pool.get(i)).close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            pool.remove(i);
        }
    }

    //返回当前连接池的一个对象
    public static ConnectionPool getInstance(){
        if (instance == null) {
            instance = (new ClassPathXmlApplicationContext("META-INF/mysql-db-config.xml"))
                    .getBean("connectionPool", ConnectionPool.class);
        }
        return instance;
    }

    //返回连接池中的一个数据库连接
    public synchronized Connection getConnection(){
        if (pool.size() > 0) {
            Connection conn = pool.get(0);
            pool.remove(conn);
            return conn;
        }else {
            return null;
        }
    }

    //在连接池中创建初始设置的数据库连接
    private void addConnection(){
        Connection coon = null;
        for (int i = 0; i < poolSize; i++) {
            try {
                Class.forName(driverClassName);
                coon = java.sql.DriverManager.getConnection(url, username, password);
                pool.add(coon);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
