package Test3;

import Test3.util.JDBCUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            l:
            while (true) {
                conn = JDBCUtil.getConnection();
                //从数据库中导出物品信息
                ArrayList<Goods> list = outGoods(conn);
                //从数据库中导出订单信息
                ArrayList<Order> list1 = outOrders(conn, list);
                System.out.println("请输入你想进行的操作：" + "\r\n"
                        + "1：插入商品" + "\r\n"
                        + "2: 删除商品" + "\r\n"
                        + "3: 插入订单" + "\r\n"
                        + "4: 查询订单" + "\r\n"
                        + "5: 更新订单中价格" + "\r\n"
                        + "6：删除订单中商品" + "\r\n"
                        + "7: 退出");

                int a = sc.nextInt();
                switch (a) {
                    case 1 -> {
                        System.out.println("正在进行插入商品");
                        list = insertGood(conn, ps, list);
                    }
                    case 2 -> {
                        System.out.println("正在进行删除商品");
                        list = deleteGood(conn, ps, list);
                    }
                    case 3 -> {
                        System.out.println("正在进行插入订单");
                        insertOrder(conn, ps,list);
                    }
                    case 4 -> {
                        System.out.println("正在进行查询订单");
                        queryOrder(conn, ps, rs);
                    }
                    case 5 -> {
                        System.out.println("正在进行更新订单中价格");
                        updatePrice(conn, ps);
                    }
                    case 6 -> {
                        System.out.println("正在进行删除订单中商品");
                        deleteOrderGood(conn, ps, list);
                    }
                    case 7 -> {
                        System.exit(0);
                        System.out.println("退出成功");
                    }
                    default -> System.out.println("您输入的数字有误,请重新输入");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.release(conn, ps, rs);
        }
    }

    //从数据库中导出物品信息
    public static ArrayList<Goods> outGoods(Connection conn) throws SQLException {
        ArrayList<Goods> goods = new ArrayList<>();
        String s = "SELECT `good_id`,`good_name`,`good_price` FROM goods";
        PreparedStatement ps = conn.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Goods g = new Goods();
            g.setId(rs.getInt(1));
            g.setName(rs.getString(2));
            g.setPrice(rs.getInt(3));
            goods.add(g);
        }
            System.out.println("店铺拥有的商品为："+goods);
        return goods;
    }

    //从数据库中导出订单信息
    public static ArrayList<Order> outOrders(Connection conn, ArrayList<Goods> list) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        String s = "SELECT `order_id`,`good_id`,`order_time`,`order_price` FROM orders";
        PreparedStatement ps = conn.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
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
            order.setTime(rs.getString(3));
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
    }

    //插入商品功能
    public static ArrayList<Goods> insertGood(Connection conn, PreparedStatement ps, ArrayList<Goods> list) throws SQLException {
        String s1 = "SELECT MAX(good_id) FROM goods";
        String s = "INSERT INTO goods (good_id,good_name,good_price) VALUES(?,?,?)";
        ps = conn.prepareStatement(s);
        PreparedStatement ps1 = conn.prepareStatement(s1);
        ResultSet rs = ps1.executeQuery();
        int id = 0;
        while (rs.next()) {
            id = rs.getInt(1) + 1;
            System.out.println("导入商品id为 " + id);
        }
        ps.setInt(1, id);
        Scanner sc = new Scanner(System.in);
        Goods g = new Goods();
        System.out.println("请输入导入商品的名称");
        String name = sc.next();
        System.out.println("导入商品名称为" + name);
        ps.setString(2, name);
        g.setName(name);
        System.out.println("请输入导入商品的价格 ");
        while (true) {
            int price = sc.nextInt();
            if (price > 0) {
                System.out.println("导入的商品价格为 " + price);
                g.setPrice(price);
                ps.setInt(3, price);
                break;
            } else {
                System.out.println("您导入的商品价格有误，请重新导入价格");
            }
        }
        ps.executeUpdate();
        g.setId(id);
        System.out.println("导入成功");
        ps = null;
        System.out.println("导入商品信息为");
        System.out.println(g.toString());
        list.add(g);
        ;
        return list;
    }

    //删除商品
    public static ArrayList<Goods> deleteGood(Connection conn, PreparedStatement ps, ArrayList<Goods> list) throws SQLException {
        String s = "delete from goods where good_name = ?";
        ps = conn.prepareStatement(s);
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要删除商品的名称");
        String name = sc.next();
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
    }

    //插入订单
    public static void insertOrder(Connection conn, PreparedStatement ps,ArrayList<Goods> list) throws SQLException {
        Scanner sc = new Scanner(System.in);
        Order order = new Order();
        ArrayList<Goods> list1 = new ArrayList<>();

        //获取最大订单号id
        int sum = 0,id = 0;
        String s1 = "SELECT MAX(order_id) FROM orders";
        PreparedStatement ps1 = conn.prepareStatement(s1);
        ResultSet rs = ps1.executeQuery();
        while(rs.next()){
            id = rs.getInt(1)+1;
        }

        System.out.println("请输入想要购入商品的名称");
        a:
        while(true){

            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(currentTime);
            System.out.println(dateString);
            String name = sc.next();
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
            LocalDate c = LocalDate.now();
            if(name.equals("e")){
                Goods [] goods = new Goods[list1.size()];
                for (int i = 0; i < list1.size(); i++) {
                    goods[i] = list1.get(i);
//将增添订单导入数据库中
                    String s = "INSERT INTO orders (`order_id`,`good_id`,`order_time`,`order_price`) \n" +
                            "VALUES("+id+","+list.get(i).getId()+",'"+ dateString +"',"+sum+")";
                    ps = conn.prepareStatement(s);
                    ps.executeUpdate();

                }
                order.setGoods(goods);
                order.setTime(dateString);
                order.setPrice(sum);
                order.setId(id);
                break a;
            }else if(!flog){
                System.out.println("未找到您想要选购的商品，如需继续选购，请继续输入商品名称（输入 e 退出选购）");
            }
        }

    }

    //查询订单
    public static void queryOrder(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        String s = "SELECT `id`,order_id`,o.good_id,`good_name`,`good_price`,`order_time`,`order_price`\n" +
                "from orders as o\n" +
                "left join goods as g\n" +
                "on o.good_id = g.good_id";
        ps = conn.prepareStatement(s);
        rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("id = " + rs.getObject(1));
        }
    }

    //更新订单中价格
    public static void updatePrice(Connection conn, PreparedStatement ps) {


    }

    //删除订单中商品
    public static void deleteOrderGood(Connection conn, PreparedStatement ps, ArrayList<Goods> list) throws SQLException {

    }
}