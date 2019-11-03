package com.example.letshop.Model;

public class Cart {
    private String pid, pname, discount, price, quantity;

    public Cart(){}

    public Cart(String pid, String pname, String discount, String price, String quantity) {
        this.pid = pid;
        this.pname = pname;
        this.discount = discount;
        this.price = price;
        this.quantity = quantity;
    }

    public String getPid() {
        return pid;
    }

    public String getPname() {
        return pname;
    }

    public String getDiscount() {
        return discount;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
