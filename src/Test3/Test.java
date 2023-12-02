package Test3;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        Scanner sc = new Scanner(System.in);
        //从数据库中导出物品信息
        ArrayList<Good> list = orderService.outGoodsInfo();
        //从数据库中导出订单信息
        ArrayList<Order> orders = orderService.outOrdersInfo(list);
        while (true) {
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
                    orderService.insertOrderInfo(list,orders);
                }
                case 4 -> {
                    System.out.println("正在进行查询订单");
                    orderService.queryOrderInfo();
                }
                case 5 -> {
                    System.out.println("正在进行更新订单中价格");
                    orderService.updatePriceInfo(orders);
                }
                case 6 -> {
                    System.out.println("正在进行删除订单中商品");
                    orderService.deleteOrderGoodInfo(orders);
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