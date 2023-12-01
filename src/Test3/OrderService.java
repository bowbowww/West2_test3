package Test3;

import Test3.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class OrderService {

    GoodsDaoOrder goodsDaoOrder = new GoodsDaoOrder();

    //从数据库中导出物品信息
    public ArrayList<Goods> outGoods(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            ArrayList<Goods> goods = new ArrayList<>();
            String s = "SELECT `good_id`,`good_name`,`good_price` FROM goods";
            ps = JdbcUtil.getPreparedStatement(s,conn);
            rs = ps.executeQuery();
            while (rs.next()) {
                Goods g = new Goods();
                g.setId(rs.getInt(1));
                g.setName(rs.getString(2));
                g.setPrice(rs.getInt(3));
                goods.add(g);
            }
            System.out.println("店铺拥有的商品为：" + goods);
            return goods;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            JdbcUtil.release(conn, ps, rs);
        }
    }

    //从数据库中导出订单信息
    public ArrayList<Order> outOrders(ArrayList<Goods> list){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            ArrayList<Order> orders = new ArrayList<>();
            String s = "SELECT `order_id`,`good_id`,`order_time`,`order_price` FROM orders";
            ps = JdbcUtil.getPreparedStatement(s,conn);
            rs = ps.executeQuery();
            ArrayList<Goods> g = new ArrayList<>();
            Order order = new Order();
            while (rs.next()) {
                order.setId(rs.getInt(1));
                int id = rs.getInt(2);

                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getId() == id){
                        Goods good = list.get(i);
                        g.add(good);
                    }
                }
                order.setTime(String.valueOf(rs.getDate(3)));
                order.setPrice(rs.getInt(4));
                orders.add(order);
            }
            Goods []goods = new Goods[g.size()];
            for (int i = 0; i < goods.length; i++) {
                goods[i] = g.get(i);
            }
            order.setGoods(goods);
            System.out.println("店铺的订单为：");
            for (int i = 0; i < orders.size(); i++) {
                Order o = orders.get(i);
                System.out.println("订单号："+o.getId()+" 下单时间："+o.getTime()+" 订单价格："+o.getPrice());
                System.out.print("订单的商品信息为：");
                System.out.println(" 商品id："+goods[i].getId()+" 商品名称："+goods[i].getName()+" 商品单价："+goods[i].getPrice());
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            JdbcUtil.release(conn, ps, rs);
        }
    }

    //插入商品功能
    public ArrayList<Goods> insertGoodInfo(ArrayList<Goods> list){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入导入商品的名称");
        String name = sc.next();
        System.out.println("请输入导入商品的价格 ");
        int price = sc.nextInt();
        return goodsDaoOrder.insertGood(list,name,price);
    }

    //删除商品
    public ArrayList<Goods> deleteGoodInfo(ArrayList<Goods> list){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要删除商品的名称");
        String name = sc.next();
        return goodsDaoOrder.deleteGood(list,name);
    }

    //插入订单
    public Order insertOrderInfo(ArrayList<Goods> list){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要购入商品的名称");
        String name = sc.next();
        goodsDaoOrder.insertOrder(list,name);
        return goodsDaoOrder.insertOrder(list,name);
    }

    //查询订单
    public void queryOrderInfo(){
        goodsDaoOrder.queryOrder();
    }

    //更新订单中价格
    public void updatePriceInfo() {


    }

    //删除订单中商品
    public void deleteOrderGoodInfo( ArrayList<Goods> list) {

    }

}
