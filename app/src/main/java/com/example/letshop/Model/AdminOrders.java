package com.example.letshop.Model;

public class AdminOrders {
    private String name, phone, date, time, address, city, state;
    private double totalAmount;

    public AdminOrders(){}

    public AdminOrders(String name, String phone, String date, String time, String address, double totalAmount, String city, String state) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.address = address;
        this.totalAmount = totalAmount;
        this.city = city;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }
}
