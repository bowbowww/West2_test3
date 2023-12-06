package Test3.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtil {
    private static String driver = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;

    static{
        try{
            InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(in);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password  = properties.getProperty("password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*获取链接*/
    public static Connection getConnection() {
        try{
            return DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*创建sql命令*/
    public static PreparedStatement getPreparedStatement(String sql,Connection conn) {
        try{
            return conn.prepareStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*释放连接*/
    public static void release(Connection conn, PreparedStatement ps, ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    }

    /*更新*/
    public static int executeUpdate(Connection conn, String sql, Object... params){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            setParameters(ps, params);
            return ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
            return Integer.parseInt(null);
        }finally{
            JdbcUtil.release(null,ps,null);
        }
    }

    /*查询*/
    public static ResultSet executeQuery(Connection conn, String sql, Object... params) {
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            setParameters(pstmt, params);
            return pstmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*在解决SQL注入问题上,导入PreparedStatement的参数*/
    private static void setParameters(PreparedStatement pstmt, Object... params) {
        try{
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*开启事务*/
    public static void beginTransaction(Connection conn) {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*提交事务*/
    public static void commitTransaction(Connection conn) {
        try {
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*回滚*/
    public static void rollbackTransaction(Connection conn) {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
