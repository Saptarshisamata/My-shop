package com.example.myshop;

public class item {
    private String itemName ;
    private int quantity ;
    private int price ;

    public item(String itemName,int quantity,int price){
        this.itemName =itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
