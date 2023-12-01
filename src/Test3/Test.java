package Test3;

import Test3.util.JdbcUtil;

import java.sql.*;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        Scanner sc = new Scanner(System.in);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        l:
        while (true) {
            //从数据库中导出物品信息
            ArrayList<Goods> list = orderService.outGoods();
            //从数据库中导出订单信息
            ArrayList<Order> list1 = orderService.outOrders(list);

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
                    orderService.insertGoodInfo(list);
                }
                case 2 -> {
                    System.out.println("正在进行删除商品");
                    orderService.deleteGoodInfo(list);
                }
                case 3 -> {
                    System.out.println("正在进行插入订单");
                    orderService.insertOrderInfo(list);
                }
                case 4 -> {
                    System.out.println("正在进行查询订单");
                    orderService.queryOrderInfo();
                }
                case 5 -> {
                    System.out.println("正在进行更新订单中价格");
                    orderService.updatePriceInfo();
                }
                case 6 -> {
                    System.out.println("正在进行删除订单中商品");
                    orderService.deleteOrderGoodInfo(list);
                }
                case 7 -> {
                    System.exit(0);
                    System.out.println("退出成功");
                }
                default -> System.out.println("您输入的数字有误,请重新输入");
            }
        }
    }

}