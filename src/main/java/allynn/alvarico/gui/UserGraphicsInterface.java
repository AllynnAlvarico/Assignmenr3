package allynn.alvarico.gui;

import allynn.alvarico.OrderItem;
import allynn.alvarico.customs.BackgroundPanel;
import allynn.alvarico.customs.CartCustomButtons;
import allynn.alvarico.customs.CustomButton;
import allynn.alvarico.product.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * References
 * *https://stackoverflow.com/questions/2533650/transparent-jframe-background
 * https://loading.io/color/feature/mcdonalds/
*/

public class UserGraphicsInterface extends JFrame implements ActionListener {

    private final int HEIGHT = 1000;
    private final int WIDTH = 720;
    private int Yaxis = 50;
    private int Xaxis = 650;
    private JButton start, confirmButton, continueButton, cancelButton, addToCart, goBack, add, minus;
    private JTextField amount;
    private Font f = new Font("Comic Sans MS", Font.BOLD, 18);
    private final ArrayList<Product> products;
    private ArrayList<OrderItem> basket;
    private int orderedQuantity = 1;
    String[] categories = {
            "What's New", "Sharers & Bundles", "Burgers", "McNuggets and Selects", "Wraps and Salads",
            "McCafe", "Breakfast Menu", "Vegetarian", "Vegan", "Eurosaver Menu", "Happy Meal",
            "Fries & Sides", "Desserts", "Milkshakes & Cold Drinks", "Condiments & Sauces"};

    OrderListPanel olp;
    GraphicUtilities gutils;

    public UserGraphicsInterface(ArrayList<Product> byRef_products) {
        this.products = byRef_products;
        basket = new ArrayList<>();
        olp = new OrderListPanel(f, basket);
        gutils = new GraphicUtilities(f, WIDTH, HEIGHT);
        this.initialising();
    }

    private void initialising(){
        this.setTitle("Kiosk Ordering System");

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setLocation(Xaxis, Yaxis);
        this.setResizable(false);

        this.add(start());

        gutils.background(this);

        this.setVisible(true);
    }

    private JPanel start() {
        JPanel panel = new JPanel();
        panel.setBounds(160, 650, 400, 200);
        panel.setLayout(null);
        panel.setOpaque(false);

        start = new CustomButton("Start", "#ffc600", "#e76a05", "#bf0c0c");
        start.setBounds(90, 40, 220, 50);

        start.addActionListener(this);

        panel.add(start);

        return panel;
    }

    private JPanel menu(String selectedCategory) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        gutils.mainPanelSetup(
                mainPanel, gutils.header(selectedCategory),
                foodCategory(), foodDisplay(selectedCategory), buttonContainer());
        return mainPanel;
    }

    private JPanel itemDisplay(String selectedCategory, Product product) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        gutils.mainPanelSetup(
                mainPanel, gutils.header(selectedCategory),
                foodCategory(), singleDisplay(product), buttonContainer());
        return mainPanel;
    }

    public JScrollPane foodCategory(){
        JPanel categoryPanel = new JPanel();
        String filepath = "categories\\";
        String[] imagesource = {
                "whatsnew", "sharer", "burger", "mcnuggets", "wraps", "mccafe", "breakfast", "vegetarian",
                "vegan", "eurosaver", "happymeal", "fries", "desserts", "drinks",   "condiments"};

        gutils.categoryView(categoryPanel);
        JButton btnCategory;

        for (int repeat = 0; repeat < categories.length; repeat++){
            btnCategory = gutils.thumbnail(filepath + imagesource[repeat] + ".jpeg");
            btnCategory.setActionCommand("*" + categories[repeat]);
            btnCategory.addActionListener(this);

            categoryPanel.add(btnCategory);
            categoryPanel.add(new JLabel(categories[repeat]));
            categoryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        return gutils.scrollPane(categoryPanel);
    }

    private JScrollPane foodDisplay(String selectedCategory){
        String command;

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        if (selectedCategory.equalsIgnoreCase("Basket")){
            for (Product p : products){
                command = p.productID() + "," + p.productName();
                JPanel fc = foodCard("foodcard\\" + p.path(), p.productName(), p.productPrice(), command, true);
                panel.add(fc);
            }
        } else {
            for (Product p : products) {
                if (p.category().equalsIgnoreCase(selectedCategory)) {
                    command = p.productID() + "," + p.productName();
                    JPanel fc = foodCard("foodcard\\" + p.path(), p.productName(), p.productPrice(), command, true);
                    panel.add(fc);
                }
            }
        }

        panel.setLayout(new GridLayout(0, 2, 20, 20));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50)); // Top, Left, Bottom, Right padding
        wrapperPanel.add(panel, BorderLayout.CENTER);
        wrapperPanel.setBackground(Color.WHITE);

        return gutils.scrollPane(wrapperPanel);
    }

    private JScrollPane singleDisplay(Product byRef_product) {
        String main = "#ffc600";
        String hover = "#e76a05";
        String click = "#bf0c0c";
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        addToCart = new CartCustomButtons("Add to Cart", main, hover, click);
        goBack = new CartCustomButtons("Go Back", main, hover, click);
        add = new CartCustomButtons("+", main, hover, click);
        minus = new CartCustomButtons("-", main, hover, click);
        amount = new JTextField("1", 3);


        amount.setHorizontalAlignment(JTextField.CENTER);

        addToCart.setActionCommand(addToCart.getText() + byRef_product.productID());
        goBack.setActionCommand(goBack.getText());
        add.setActionCommand(add.getText());
        minus.setActionCommand(minus.getText());

        addToCart.addActionListener(this);
        goBack.addActionListener(this);
        add.addActionListener(this);
        minus.addActionListener(this);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(150, 150));

        for (Product p : products) {
            if (p == byRef_product) {
                JPanel fc = foodCard("foodcard\\" + p.path(), p.productName(), p.productPrice(), String.valueOf(p), false);
                fc.setPreferredSize(new Dimension(100, 100));
                imagePanel.add(fc, BorderLayout.CENTER);
            }
        }

        panel.add(imagePanel, gbc);

        gbc.gridy = 1;
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.add(minus);
        controlsPanel.add(amount);
        controlsPanel.add(add);
        controlsPanel.add(addToCart);
        controlsPanel.add(goBack);

        panel.add(controlsPanel, gbc);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 50));
        wrapperPanel.add(panel, BorderLayout.CENTER);
        wrapperPanel.setBackground(Color.WHITE);

        return gutils.scrollPane(wrapperPanel);
    }

    private JPanel foodCard(String imagePath, String name, Double price, String actionCommand, boolean btnState) {
        String priceFormat = String.format("Price: € %.2f", price);
        JPanel foodCardLayout = new JPanel();

        foodCardLayout.setLayout(new BoxLayout(foodCardLayout,BoxLayout.Y_AXIS));
        foodCardLayout.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel foodName = new JLabel(name);
        JLabel foodPrice = new JLabel(priceFormat);
        JButton product = gutils.thumbnail(imagePath +".jpeg");
        product.setEnabled(btnState);
        product.setDisabledIcon(product.getIcon());
        product.setOpaque(true);

        product.setActionCommand(actionCommand);
        product.addActionListener(this);

        foodCardLayout.add(product,BorderLayout.NORTH);
        foodCardLayout.add(foodName, BorderLayout.CENTER);
        foodCardLayout.add(foodPrice, BorderLayout.SOUTH);

        foodCardLayout.setBackground(Color.WHITE);
        return foodCardLayout;
    }

    public JPanel menuCart() {
        JPanel panel = new JPanel(new BorderLayout());
        gutils.mainPanelSetup(panel, gutils.header("Customer Basket"),
                foodCategory(), gutils.scrollPane(olp), buttonContainer());
        return panel;
    }
    public JPanel buttonContainer() {
        int btnWidth = 220;
        int btnHeight = 40;
        Dimension d = new Dimension(btnWidth, btnHeight);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        cancelButton = new CustomButton("Cancel", "#bf0c0c", "#ffc600", "#e76a05");
        confirmButton = new CustomButton("Confirm", "#bf0c0c", "#ffc600", "#e76a05");
        continueButton = new CustomButton("Continue Ordering", "#bf0c0c", "#ffc600", "#e76a05");

        cancelButton.setPreferredSize(d);
        confirmButton.setPreferredSize(d);
        continueButton.setPreferredSize(d);

        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(WIDTH, 100));

        bottomPanel.add(cancelButton);
        bottomPanel.add(continueButton);
        bottomPanel.add(confirmButton);

        cancelButton.addActionListener(this);
        confirmButton.addActionListener(this);
        continueButton.addActionListener(e -> gutils.switchPanel(this, menu(categories[0])));

        return bottomPanel;
    }

    private void addToCartItem(String byRef_cmd){
        String productId = byRef_cmd.replace("Add to Cart", "").trim();

        for (Product p : products) {
            if (productId.equals(String.valueOf(p.productID()))) {
                OrderItem orderItem = new OrderItem(p, orderedQuantity);
                basket.add(orderItem);
                olp.addOrderItem(orderItem);
                System.out.println("Added to cart: " + p.productName());
            }
        }
        orderedQuantity = 1;
    }

//    private void addToCartItem(String byRef_cmd){
//        String productId = byRef_cmd.replace("Add to Cart", "").trim();
//        OrderItem orderItem = null;
//        for (Product p : products) {
//            if (basket.isEmpty()) {
//                orderItem = new OrderItem(p, orderedQuantity);
//            } else {
//                if (productId.equals(String.valueOf(p.productID()))) {
//                    if (!basket.contains(basket.get(orderItem.getItemNumber()))) {
//                        orderItem = new OrderItem(p, orderedQuantity);
//                    } else {
//                        orderItem = basket.get(orderItem.getItemNumber());
//                        orderItem.addQuantity(orderedQuantity);
//                    }
//
//                    //                System.out.println(orderItem);
//                    //                System.out.println("Added to cart: " + p.productName());
//                }
//
//            }
//
//        }
//        basket.add(orderItem);
//        olp.addOrderItem(orderItem);
//
//        orderedQuantity = 1;
//    }

    private Product searchProduct(String actionCommand){
        Product itemSearch = null;
        for (Product p: products) {
            if (actionCommand.equals(p.productID() + "," + p.productName())){
                itemSearch = p;
            }
        }
        return itemSearch;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String selectedCategory = cmd.substring(1);
        if (e.getSource() == start){
            gutils.switchPanel(this, menu(categories[0]));
            System.out.println("Switched to Food Menu!");
        } else if (e.getSource() == cancelButton) {
            gutils.switchPanel(this, start());
            gutils.background(this);
            System.out.println("Switched to Home Menu!");
        } else if (e.getSource() == continueButton) {
            gutils.switchPanel(this, menu(categories[0]));
            System.out.println("Continuing to order!");
        } else if(cmd.contains(",")){
            Product p = searchProduct(cmd);
            if(p != null){
                gutils.switchPanel(this, itemDisplay(p.category(), p));
            }
            System.out.println(cmd);
        } else if (cmd.contains("*")) {
            gutils.switchPanel(this, menu(selectedCategory));
        } else if (e.getSource() == addToCart) {
            addToCartItem(cmd);
            gutils.switchPanel(this, menuCart());
        } else if (e.getSource() == goBack) {
            gutils.switchPanel(this, menu("What's New"));
        } else if (e.getSource() == add) {
            orderedQuantity++;
            amount.setText(String.valueOf(orderedQuantity));
        } else if (e.getSource() == minus) {
            if (orderedQuantity > 0) {
                orderedQuantity--;
                amount.setText(String.valueOf(orderedQuantity));
            }
        }
    }
}