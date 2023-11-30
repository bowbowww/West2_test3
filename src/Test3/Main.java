import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver"); //加载驱动
        String url = "jdbc:mysql://localhost:3306/test3?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String username = "root";
        String password = "Chq20031129!";
        //连接成功，数据库对象
        Connection conn = DriverManager.getConnection(url,username,password);
        //执行sql的对象
        Statement statement = conn.createStatement();
        String sql = "select * from goods";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            System.out.println("id = "+resultSet.getObject("good_name"));
        }
//释放连接
        conn.close();
        statement.close();
        resultSet.close();
    }
}