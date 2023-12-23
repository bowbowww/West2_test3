package Test3.Test;

import Test3.pojo.Good;
import Test3.pojo.Order;
import Test3.service.OrderService;

import java.util.*;

public class Test {
    public static void main(String[] args) {

        OrderService orderService = new OrderService();
        Scanner sc = new Scanner(System.in);
        //从数据库中导出物品信息
        ArrayList<Good> goods = orderService.outGoodsInfo();
        //从数据库中导出订单信息
        ArrayList<Order> orders = orderService.outOrdersInfo(goods);
        while (true) {
            System.out.println("请输入你想进行的操作: " + "\r\n"
                    + "1: 插入商品" + "\r\n"
                    + "2: 删除商品" + "\r\n"
                    + "3: 插入订单" + "\r\n"
                    + "4: 查询单个订单" + "\r\n"
                    + "5: 查询全部订单" + "\r\n"
                    + "6: 增加订单中商品" + "\r\n"
                    + "7: 删除订单中商品" + "\r\n"
                    + "8: 查询所有商品" + "\r\n"
                    + "9: 退出");

            int a = sc.nextInt();
            switch (a) {
                case 1 -> {
                    goods = orderService.outGoodsInfo();
                    System.out.println("正在进行插入商品");
                    orderService.insertGoodInfo(goods);
                }
                case 2 -> {
                    goods = orderService.outGoodsInfo();
                    System.out.println("正在进行删除商品");
                    orderService.deleteGoodInfo(goods);
                }
                case 3 -> {
                    goods = orderService.outGoodsInfo();
                    System.out.println("正在进行插入订单");
                    orderService.insertOrderInfo(goods,orders);
                }
                case 4 -> {
                    System.out.println("正在进行查询单个订单");
                    orderService.queryOneOrderInfo();
                }
                case 5 -> {
                    System.out.println("正在进行查询全部订单");
                    orderService.queryOrderInfo();
                }
                case 6 -> {
                    goods = orderService.outGoodsInfo();
                    System.out.println("正在进行增加订单中商品");
                    orderService.insertOrderGoodInfo(orders,goods);
                }
                case 7 -> {
                    goods = orderService.outGoodsInfo();
                    System.out.println("正在进行删除订单中商品");
                    orderService.returnOrderGoodInfo(orders,goods);
                }
                case 8 -> {
                    System.out.println("正在进行查询所有商品");
                    orderService.queryGoodInfo();
                }
                case 9 -> {
                    System.exit(0);
                    System.out.println("退出成功");
                }
                default -> System.out.println("您输入的选项有误,请重新输入");
            }
        }
    }

}