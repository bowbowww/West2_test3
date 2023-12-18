package Test3.dao;

import Test3.dao.GoodDao;

public class OrderDao {
    private int id;
    private GoodDao[]goods;
    private String time;
    private int price;

    public OrderDao() {
    }

    public OrderDao(int id, GoodDao[] goods, String time, int price) {
        this.id = id;
        this.goods = goods;
        this.time = time;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GoodDao[] getGoods() {
        return goods;
    }

    public void setGoods(GoodDao[] goods) {
        this.goods = goods;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
