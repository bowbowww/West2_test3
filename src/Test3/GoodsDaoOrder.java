package Test3;

import Test3.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class GoodsDaoOrder {

    public ArrayList<Goods> insertGood(ArrayList<Goods> list,String name,int price){
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            String s1 = "SELECT MAX(good_id) FROM goods";
            String s = "INSERT INTO goods (good_id,good_name,good_price) VALUES(?,?,?)";
            ps = JdbcUtil.getPreparedStatement(s,conn);
            ps1 = JdbcUtil.getPreparedStatement(s1,conn);
            rs = ps1.executeQuery();
            int id = 0;
            while (rs.next()) {
                id = rs.getInt(1) + 1;
                System.out.println("导入商品id为 " + id);
            }
            ps.setInt(1, id);
            Scanner sc = new Scanner(System.in);
            Goods g = new Goods();
            System.out.println("导入商品名称为" + name);
            ps.setString(2, name);
            g.setName(name);
            while (true) {
                if (price > 0) {
                    System.out.println("导入的商品价格为 " + price);
                    g.setPrice(price);
                    ps.setInt(3, price);
                    break;
                } else {
                    System.out.println("您导入的商品价格有误，请重新导入价格");
                    price = sc.nextInt();
                }
            }
            ps.executeUpdate();
            g.setId(id);
            System.out.println("导入成功");
            ps = null;
            System.out.println("导入商品信息为");
            System.out.println(g.toString());
            list.add(g);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally{
            JdbcUtil.release(conn, ps, rs);
            if(ps1!=null){
                try {
                    ps1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }


    }


    public ArrayList<Goods> deleteGood(ArrayList<Goods> list,String name){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtil.getConnection();
            String s = "delete from goods where good_name = ?";
            ps = JdbcUtil.getPreparedStatement(s,conn);
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().equals(name)) {
                    index = i + 1;
                    list.remove(i);
                }
            }
            if (index == 0) {
                System.out.println("没有找到你想删除的商品");
            } else {
                ps.setString(1, name);
                ps.executeUpdate();
                System.out.println("删除成功");
                ps = null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally{
            JdbcUtil.release(conn, ps, null);
        }
    }


    public Order insertOrder(ArrayList<Goods> list,String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtil.getConnection();
            Order order = new Order();
            ArrayList<Goods> list1 = new ArrayList<>();

            //获取最大订单号id
            int sum = 0,id = 0;
            String s1 = "SELECT MAX(order_id) FROM orders";
            PreparedStatement ps1 = JdbcUtil.getPreparedStatement(s1,conn);
            ResultSet rs = ps1.executeQuery();
            while(rs.next()){
                id = rs.getInt(1)+1;
            }

            a:
            while(true){

                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(currentTime);
                System.out.println(dateString);

                boolean flog = false;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().equals(name)) {
                        Goods g = list.get(i);
                        sum += g.getPrice();
                        list1.add(g);
                        flog = true;
                        System.out.println("成功将商品加入订单中，如需继续选购，请继续输入商品名称（输入 e 退出选购）");
                        break;
                    }
                }
                if(name.equals("e")){
                    Goods [] goods = new Goods[list1.size()];
                    for (int i = 0; i < list1.size(); i++) {
                        goods[i] = list1.get(i);
//将增添订单导入数据库中
                        java.sql.Date date = new java.sql.Date(currentTime.getTime());
                        String s = "INSERT INTO orders (`order_id`,`good_id`,`order_time`,`order_price`) \n" +
                                "VALUES(" + id + "," + list.get(i).getId() + ",'" + date + "'," + sum + ")";
                        ps = JdbcUtil.getPreparedStatement(s,conn);
                        ps.executeUpdate();

                    }
                    order.setGoods(goods);
                    order.setTime(dateString);
                    order.setPrice(sum);
                    order.setId(id);
                    return order;
                }else if(!flog){
                    System.out.println("未找到您想要选购的商品，如需继续选购，请继续输入商品名称（输入 e 退出选购）");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            JdbcUtil.release(conn, ps, null);
        }
    }

    //查询订单
    public void queryOrder() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            String s = "SELECT `order_id`,o.good_id,`good_name`,`good_price`,`order_time`,`order_price`\n" +
                    "from orders as o\n" +
                    "left join goods as g\n" +
                    "on o.good_id = g.good_id";
            ps = JdbcUtil.getPreparedStatement(s,conn);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("订单号："+rs.getInt("order_id")+" 下单时间："+rs.getDate("order_time")+" 订单价格："+rs.getString("order_price"));
                System.out.print("订单的商品信息为：");
                System.out.println(" 商品id："+rs.getInt("good_id")+" 商品名称："+rs.getString("good_name")+" 商品单价："+rs.getInt("good_price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            JdbcUtil.release(conn, ps, rs);
        }
    }

    //更新订单中价格
    public void updatePrice() {
        String sql = "";
    }

    //删除订单中商品
    public void deleteOrderGood(Connection conn, PreparedStatement ps, ArrayList<Goods> list) throws SQLException {

    }
}
