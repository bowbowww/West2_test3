package Test3;

import java.util.Date;

public class Order {
    private int id;
    private Goods[]goods;
    private String time;
    private int price;

    public Order() {
    }

    public Order(int id, Goods[] goods, String time, int price) {
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

    public Goods[] getGoods() {
        return goods;
    }

    public void setGoods(Goods[] goods) {
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
