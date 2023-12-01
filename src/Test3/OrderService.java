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
    public ArrayList<Goods> outGoodsInfo(){
        return goodsDaoOrder.outGoods();
    }

    //从数据库中导出订单信息
    public ArrayList<Order> outOrdersInfo(ArrayList<Goods> list){
        return goodsDaoOrder.outOrders(list);
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
    public ArrayList<Order> updatePriceInfo(ArrayList<Order>orders) {
        Scanner sc = new Scanner(System.in);
        return goodsDaoOrder.updatePrice(orders);
    }

    //删除订单中商品
    public void deleteOrderGoodInfo(ArrayList<Order> orders) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要删除订单的id");
        int id = sc.nextInt();
        System.out.println("请输入想要删除的商品名称");
        String name = sc.next();
        goodsDaoOrder.deleteOrderGood(orders,id,name);
    }

}
