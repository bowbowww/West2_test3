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

    //从数据库中导出物品信息
    public ArrayList<Good> outGoods() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            ArrayList<Good> goods = new ArrayList<>();
            String s = "SELECT `good_id`,`good_name`,`good_price` FROM goods";
            ps = JdbcUtil.getPreparedStatement(s, conn);
            rs = ps.executeQuery();
            while (rs.next()) {
                Good g = new Good();
                g.setId(rs.getInt(1));
                g.setName(rs.getString(2));
                g.setPrice(rs.getInt(3));
                goods.add(g);
            }
            System.out.println("店铺拥有的商品为：" + goods);
            return goods;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.release(conn, ps, rs);
        }
    }

    //从数据库中导出订单信息**
    public ArrayList<Order> outOrders(ArrayList<Good> list) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            String s = "SELECT `order_id`,`good_id`,`order_time`,`order_price` FROM orders";
            ps = JdbcUtil.getPreparedStatement(s, conn);
            rs = ps.executeQuery();
            ArrayList<Good> g = new ArrayList<>();
            ArrayList<Order> orders = new ArrayList<>();
            ArrayList<Object[]> l = new ArrayList<>();
            Order order = new Order();
            while (rs.next()) {
                Object[] object = new Object[4];
                object[0] = rs.getInt(1);
                object[1] = rs.getInt(2);
                object[2] = String.valueOf(rs.getDate(3));
                object[3] = rs.getInt(4);
                l.add(object);
            }
            for (int i = 0; i < l.size(); i++) {
                int orderId = (int) l.get(i)[0];
                int goodId = (int) l.get(i)[1];
                String orderTime = (String) l.get(i)[2];
                int orderPrice = (int) l.get(i)[3];
                //存储商品进入g
                for (int i1 = 0; i1 < list.size(); i1++) {
                    if (goodId == list.get(i1).getId()) {
                        g.add(list.get(i1));
                    }
                }
                //如果前后id不同，说明下一次换了其他订单
                if (i != l.size() - 1) {
                    if (orderId != (int) l.get(i + 1)[0]) {
                        Good[] goods = new Good[g.size()];
                        for (int i1 = 0; i1 < goods.length; i1++) {
                            goods[i1] = g.get(i1);
                        }
                        order.setId(orderId);
                        order.setGoods(goods);
                        order.setTime(orderTime);
                        order.setPrice(orderPrice);
                        orders.add(order);
                        order = new Order();
                        g = new ArrayList<>();

                    }
                }else {
                    if (orderId != (int) l.get(i - 1)[0]) {
                        Good[] goods = new Good[g.size()];
                        for (int i1 = 0; i1 < goods.length; i1++) {
                            goods[i1] = g.get(i1);
                        }
                        order.setId(orderId);
                        order.setGoods(goods);
                        order.setTime(orderTime);
                        order.setPrice(orderPrice);
                        orders.add(order);

                    }
                }

            }

            for (int i = 0; i < orders.size(); i++) {
                Order order1 = orders.get(i);
                Good[] goods = order1.getGoods();
                System.out.println("当前店铺订单为：");
                System.out.println("订单id：" + order1.getId() + " 订单价格：" + order1.getPrice() + " 订单时间：" + order1.getTime());
                System.out.println("订单中商品信息为：");
                for (int i1 = 0; i1 < goods.length; i1++) {
                    System.out.print("{ 商品id：" + goods[i1].getId() + " 商品名称：" + goods[i1].getName() + " 商品价格：" + goods[i1].getPrice() + " } ");
                }
                System.out.println("");
            }
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            JdbcUtil.release(conn, ps, rs);
        }
    }

    public ArrayList<Good> insertGood(ArrayList<Good> list, String name, int price) {
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            JdbcUtil.beginTransaction(conn);
            String s1 = "SELECT MAX(good_id) FROM goods";
            String s = "INSERT INTO goods (good_id,good_name,good_price) VALUES(?,?,?)";
            ps = JdbcUtil.getPreparedStatement(s, conn);
            ps1 = JdbcUtil.getPreparedStatement(s1, conn);
            rs = ps1.executeQuery();
            int id = 0;
            while (rs.next()) {
                id = rs.getInt(1) + 1;
                System.out.println("导入商品id为 " + id);
            }
            ps.setInt(1, id);
            Scanner sc = new Scanner(System.in);
            Good g = new Good();
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
            System.out.println(g);
            list.add(g);
            JdbcUtil.commitTransaction(conn);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            JdbcUtil.rollbackTransaction(conn);
            return null;
        } finally {
            JdbcUtil.release(conn, ps, rs);
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }


    }

    public ArrayList<Good> deleteGood(ArrayList<Good> list, String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JdbcUtil.getConnection();
            JdbcUtil.beginTransaction(conn);
            String s = "delete from goods where good_name = ?";
            ps = JdbcUtil.getPreparedStatement(s, conn);
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
            JdbcUtil.commitTransaction(conn);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            JdbcUtil.rollbackTransaction(conn);
            return null;
        } finally {
            JdbcUtil.release(conn, ps, null);
        }
    }

    public ArrayList<Order> insertOrder(ArrayList<Good> list, ArrayList<Order> orders) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Scanner sc = new Scanner(System.in);
            conn = JdbcUtil.getConnection();
            JdbcUtil.beginTransaction(conn);
            Order order = new Order();
            ArrayList<Good> list1 = new ArrayList<>();

            //获取最大订单号id
            int sum = 0, id = 0;
            String s1 = "SELECT MAX(order_id) FROM orders";
            PreparedStatement ps1 = JdbcUtil.getPreparedStatement(s1, conn);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1) + 1;
            }

            System.out.println("请输入想要购入商品的名称");
            a:
            while (true) {
                String name = sc.next();
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(currentTime);
                System.out.println(dateString);

                boolean flog = false;
                for (int i = 0; i < list.size(); i++) {
                    Good g = list.get(i);
                    if (g.getName().equals(name)) {
                        sum += g.getPrice();
                        list1.add(g);
                        flog = true;
                        System.out.println("成功将商品加入订单中，如需继续选购，请继续输入商品名称（输入 e 退出选购）");
                        break;
                    }
                }
                if (name.equals("e")) {
                    Good[] goods = new Good[list1.size()];
                    for (int i = 0; i < list1.size(); i++) {
                        goods[i] = list1.get(i);
//将增添订单导入数据库中
                        java.sql.Date date = new java.sql.Date(currentTime.getTime());
                        String s = "INSERT INTO orders (`order_id`,`good_id`,`order_time`,`order_price`) \n" +
                                "VALUES(" + id + "," + goods[i].getId() + ",'" + date + "'," + sum + ")";
                        ps = JdbcUtil.getPreparedStatement(s, conn);
                        ps.executeUpdate();

                    }
                    order.setGoods(goods);
                    order.setTime(dateString);
                    order.setPrice(sum);
                    order.setId(id);
                    orders.add(order);
                    JdbcUtil.commitTransaction(conn);
                    return orders;
                } else if (!flog) {
                    System.out.println("未找到您想要选购的商品，如需继续选购，请继续输入商品名称（输入 e 退出选购）");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JdbcUtil.rollbackTransaction(conn);
            return null;
        } finally {
            JdbcUtil.release(conn, ps, null);
        }
    }

    public void queryOrder() {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtil.getConnection();
            String s = "SELECT `order_id`,o.good_id,`good_name`,`good_price`,`order_time`,`order_price`\n" +
                    "from orders as o\n" +
                    "left join goods as g\n" +
                    "on o.good_id = g.good_id";
            rs = JdbcUtil.executeQuery(conn, s);
            while (rs.next()) {
                System.out.print("订单信息为：");
                System.out.println(" 订单号：" + rs.getInt("order_id") + " 下单时间：" + rs.getDate("order_time") + " 订单价格：" + rs.getString("order_price"));
                System.out.print("订单的商品信息为：");
                System.out.println(" 商品id：" + rs.getInt("good_id") + " 商品名称：" + rs.getString("good_name") + " 商品单价：" + rs.getInt("good_price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(conn, null, rs);
        }
    }

    public ArrayList<Order> updatePrice(ArrayList<Order> orders) {
        Connection conn = null;
        try {
            conn = JdbcUtil.getConnection();
            JdbcUtil.beginTransaction(conn);
            for (int i = 0; i < orders.size(); i++) {
                int price = 0;
                for (int j = 0; j < orders.size(); j++) {
                    if (orders.get(j).getId() == i) {
                        Good[] goods = orders.get(j).getGoods();
                        for (int i1 = 0; i1 < goods.length; i1++) {
                            price += goods[i1].getPrice();
                        }
                    }
                }
                for (int j = 0; j < orders.size(); j++) {
                    if (orders.get(j).getId() == i) {
                        orders.get(j).setPrice(price);
                    }
                }
                String sql = "UPDATE orders SET `order_price` = ? WHERE `order_id` = ?";
                JdbcUtil.executeUpdate(conn, sql, price, i);
            }
            System.out.println("订单更改成功");
            JdbcUtil.commitTransaction(conn);
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
            JdbcUtil.rollbackTransaction(conn);
            return null;
        } finally {
            JdbcUtil.release(conn, null, null);
        }
    }

    public ArrayList<Order> deleteOrderGood(ArrayList<Order> orders, int id, String name) {
        Connection conn = null;
        try {
            conn = JdbcUtil.getConnection();
            JdbcUtil.beginTransaction(conn);
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getId() == id) {
                    if (orders.get(i).getGoods()[i].getName().equals(name)) {
                        orders.remove(i);
                    }
                }
            }
            GoodsDaoOrder goodsDaoOrder = new GoodsDaoOrder();
            goodsDaoOrder.updatePrice(orders);
            String sql = "DELETE FROM orders WHERE `order_id` = ? AND `good_name` = ?";
            JdbcUtil.executeUpdate(conn, sql, id, name);
            JdbcUtil.commitTransaction(conn);
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
            JdbcUtil.rollbackTransaction(conn);
            return null;
        } finally {
            JdbcUtil.release(conn, null, null);
        }
    }
}
