package allynn.alvarico;

import allynn.alvarico.product.Product;

import java.util.ArrayList;

/**
 * Author: Allynn Alvarico
 * Date: 01-03-2025
 * Class: OrderItem
 * Student ID: C23768861@mytudublin.ie
 */

// OrderItem class
/*
* OrderItem class is a class that extends ArrayList<Product> class.
* This class is used to store the products that the customer wants to order.
*
* OrderItem class has the following attributes:
* - orderNumber: an integer that represents the order number of the customer.
* - ArrayList<Product>: an ArrayList of Product objects that stores the products that the customer wants to order.
*
 */

public class OrderItem extends ArrayList<Product> {

    private int orderNumber;

    public OrderItem(){
        this.orderNumber = 0;
    }

    public void incrementOrderNumber(){
        this.orderNumber++;
    }

    public int getOrderNumber(){
        return this.orderNumber;
    }

    public void addToBasket(Product byRef_product){
        this.add(byRef_product);
    }

    public String getOrderedItems(){
        StringBuilder orderedItems = new StringBuilder();
        this.forEach(product -> orderedItems.append(product.productName()).append("\n"));
        return orderedItems.toString();
    }

    public void removeFromBasket(Product byRef_product){
        this.remove(byRef_product);
    }

    public void clearBasket(){
        this.clear();
    }

    public double getTotalPrice(){
        return this.stream().mapToDouble(Product::productPrice).sum();
    }

    public int getTotalPrepTime(){
        return this.stream().mapToInt(Product::productPrepTime).sum();
    }

    public void printBasket(){
        System.out.println("Order Number: " + this.orderNumber);
        System.out.println("Basket:");
        this.forEach(product -> System.out.println(product.productName()));
        System.out.println("Total Price: " + this.getTotalPrice());
        System.out.println("Total Prep Time: " + this.getTotalPrepTime());
    }
    public ArrayList<Product> customerBasket(){
        return this;
    }
}
