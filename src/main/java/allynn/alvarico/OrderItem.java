package allynn.alvarico;

import allynn.alvarico.product.Product;

import javax.swing.*;
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

public class OrderItem {

    private int orderNumber;
    private int productQuantity;
    private Product product;

    public OrderItem(Product product, int quantity){
        this.product = product;
        this.productQuantity = quantity;
        this.orderNumber = 0;
    }

    public void incrementOrderNumber(){
        this.orderNumber++;
    }

    public int getOrderNumber(){
        return this.orderNumber;
    }

    public double getTotal() {
        return product.productPrice() * productQuantity;
    }

    public int getProductQuantity(){
        return this.productQuantity;
    }

    public Product getProduct(){
        return this.product;
    }
}
