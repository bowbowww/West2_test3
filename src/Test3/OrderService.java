package Test3;

import java.util.ArrayList;
import java.util.Scanner;

public class OrderService {

    GoodsDaoOrder goodsDaoOrder = new GoodsDaoOrder();

    //从数据库中导出物品信息
    public ArrayList<Good> outGoodsInfo(){
        return goodsDaoOrder.outGoods();
    }

    //从数据库中导出订单信息
    public ArrayList<Order> outOrdersInfo(ArrayList<Good> list){
        return goodsDaoOrder.outOrders(list);
    }

    //插入商品功能
    public ArrayList<Good> insertGoodInfo(ArrayList<Good> list){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入导入商品的名称");
        String name = sc.next();
        System.out.println("请输入商品库存");
        int number = sc.nextInt();
        return goodsDaoOrder.insertGood(list,name,number);
    }

    //删除商品
    public ArrayList<Good> deleteGoodInfo(ArrayList<Good> list){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要删除商品的名称");
        String name = sc.next();
        return goodsDaoOrder.deleteGood(list,name);
    }

    //插入订单
    public ArrayList<Order> insertOrderInfo(ArrayList<Good> list, ArrayList<Order>orders){
        return goodsDaoOrder.insertOrder(list,orders);
    }

    //查询单个订单
    public void queryOneOrderInfo(){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要查询的订单id");
        int id = sc.nextInt();
        goodsDaoOrder.queryOneOrder(id);
    }

    //查询全部订单
    public void queryOrderInfo(){
        goodsDaoOrder.queryOrder();
    }

    //增添某订单中的商品
    public ArrayList<Order> insertOrderGoodInfo(ArrayList<Order> orders, ArrayList<Good> goods){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要修改的订单id");
        int id = sc.nextInt();
        System.out.println("请输入想要增添的商品名称");
        String name = sc.next();
        return goodsDaoOrder.insertOrderGood(orders,goods,id,name);
    }

    //删除某订单中商品
    public void returnOrderGoodInfo(ArrayList<Order> orders, ArrayList<Good> goods) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入想要删除订单的id");
        int id = sc.nextInt();
        System.out.println("请输入想要删除的商品名称");
        String name = sc.next();
        goodsDaoOrder.returnOrderGood(orders,goods,id,name);
    }

    //查询所有商品信息
    public void queryGoodInfo(){
        goodsDaoOrder.queryGood();
    }

    //更新订单中价格
   /* public ArrayList<Order> updatePriceInfo(ArrayList<Order>orders, ArrayList<Good> goods) {
        Scanner sc = new Scanner(System.in);
        return goodsDaoOrder.updatePrice(orders,goods,conn);
    }*/



}
