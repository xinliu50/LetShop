package com.example.letshop.Model;

public class Products {

    private String category, date, image, pid, time, pname, description, price, productState;

    public Products(){}

    public Products(String category, String date, String image, String pid, String time, String pname, String description, String price, String productState) {
        this.category = category;
        this.date = date;
        this.image = image;
        this.pid = pid;
        this.time = time;
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.productState = productState;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getPid() {
        return pid;
    }

    public String getTime() {
        return time;
    }

    public String getPname() {
        return pname;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getProductState() {
        return productState;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }
}
