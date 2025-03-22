package org.example;

import java.util.ArrayList;

public class OrderItem {
    private ArrayList<Product> basket;

    public OrderItem(){
        basket = new ArrayList<>();
    }

    public void addToBasket(Product byRef_product){
        basket.add(byRef_product);
    }


}
