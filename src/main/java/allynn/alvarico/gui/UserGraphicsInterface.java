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
    private JButton start, confirmButton, cancelButton, addToCart, goBack, add, minus;
    private Font f = new Font("Comic Sans MS", Font.BOLD, 18);
    private final ArrayList<Product> products;
    private final OrderItem orderedItems;
    String[] categories = {
            "What's New", "Sharers & Bundles", "Burgers", "McNuggets and Selects", "Wraps and Salads",
            "McCafe", "Breakfast Menu", "Vegetarian", "Vegan", "Eurosaver Menu", "Happy Meal",
            "Fries & Sides", "Desserts", "Milkshakes & Cold Drinks", "Condiments & Sauces"};

    public UserGraphicsInterface(ArrayList<Product> byRef_products, OrderItem byRef_orderedItems) {
        this.products = byRef_products;
        this.orderedItems = byRef_orderedItems;
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

        background();

        this.setVisible(true);
    }

    private JPanel start() {
        JPanel panel = new JPanel();
        panel.setBounds(160, 650, 400, 200);
        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setBackground(new Color(0,0,0,0));

        start = new CustomButton("Start", "#ffc600", "#e76a05", "#bf0c0c");
        start.setBounds(90, 40, 220, 50);

        start.addActionListener(this);

        panel.add(start);

        return panel;
    }

    private JPanel menu(String selectedCategory) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setSize(WIDTH, HEIGHT);

        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(header(selectedCategory), BorderLayout.NORTH);
        mainPanel.add(foodCategory(), BorderLayout.WEST);
        mainPanel.add(foodDisplay(selectedCategory), BorderLayout.CENTER);
        mainPanel.add(buttonContainer(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel menuCart(String selectedCategory, Product product) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setSize(WIDTH, HEIGHT);

        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(header(selectedCategory), BorderLayout.NORTH);
        mainPanel.add(foodCategory(), BorderLayout.WEST);
        mainPanel.add(singleDisplay(product), BorderLayout.CENTER);
        mainPanel.add(buttonContainer(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel header(String selectedCategory){
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(WIDTH, 100));
        headerPanel.setBackground(Color.WHITE);


        JLabel headerLabel = new JLabel(selectedCategory);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        headerLabel.setFont(f);

        headerPanel.add(headerLabel);
        return headerPanel;
    }

    public JScrollPane foodCategory(){
        JPanel categoryPanel = new JPanel();
        String filepath = "categories\\";
        String[] imagesource = {
                "whatsnew", "sharer", "burger", "mcnuggets", "wraps", "mccafe", "breakfast", "vegetarian",
                "vegan", "eurosaver", "happymeal", "fries", "desserts", "drinks",   "condiments"};

        categoryView(categoryPanel);
        JButton btnCategory;

        for (int repeat = 0; repeat < categories.length; repeat++){
            btnCategory = thumbnail(filepath + imagesource[repeat] + ".jpeg");
            btnCategory.setActionCommand("*" + categories[repeat]);
            btnCategory.addActionListener(this);

            categoryPanel.add(btnCategory);
            categoryPanel.add(new JLabel(categories[repeat]));
            categoryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        return scrollPane(categoryPanel);
    }

    private JScrollPane foodDisplay(String selectedCategory){
        String command;

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        for (Product p : products) {
            if (p.category().equalsIgnoreCase(selectedCategory)) {
                command = p.productID() + "," + p.productName();
                JPanel fc = foodCard("foodcard\\" + p.path(), p.productName(), p.productPrice(), command, true);
                panel.add(fc);
            }
        }
        panel.setLayout(new GridLayout(0, 2, 20, 20));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50)); // Top, Left, Bottom, Right padding
        wrapperPanel.add(panel, BorderLayout.CENTER);
        wrapperPanel.setBackground(Color.WHITE);

        return scrollPane(wrapperPanel);
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
        JTextField amount = new JTextField("1", 3);
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

        return scrollPane(wrapperPanel);
    }

    private JPanel foodCard(String imagePath, String name, Double price, String actionCommand, boolean btnState) {
        String priceFormat = String.format("Price: € %.2f", price);
        JPanel foodCardLayout = new JPanel();

        foodCardLayout.setLayout(new BoxLayout(foodCardLayout,BoxLayout.Y_AXIS));
        foodCardLayout.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel foodName = new JLabel(name);
        JLabel foodPrice = new JLabel(priceFormat);
        JButton product = thumbnail(imagePath +".jpeg");
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

    public JPanel buttonContainer(){
        int btnWidth = 220;
        int btnHeight = 40;
        Dimension d = new Dimension(btnWidth, btnHeight);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        cancelButton = new CustomButton("Cancel", "#bf0c0c", "#ffc600", "#e76a05");
        confirmButton = new CustomButton("Confirm", "#bf0c0c", "#ffc600", "#e76a05");

        cancelButton.setPreferredSize(d);
        confirmButton.setPreferredSize(d);

        bottomPanel.setBackground(Color.WHITE);

        bottomPanel.setPreferredSize(new Dimension(WIDTH, 100));
        bottomPanel.add(cancelButton);
        bottomPanel.add(confirmButton);

        cancelButton.addActionListener(this);
        confirmButton.addActionListener(this);

        return bottomPanel;
    }

    // Utilities
    private JButton thumbnail(String source){
        String rsc = "resource\\images\\" + source;
        ImageIcon img = new ImageIcon(rsc);
        JButton thumbnail = new JButton();

        thumbnail.setIcon(img);
        thumbnail.setPreferredSize(new Dimension(WIDTH / 4, 100));
        thumbnail.setBackground(Color.WHITE);
        thumbnail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        thumbnail.setFocusable(false);
        thumbnail.setContentAreaFilled(false);
        thumbnail.setBorderPainted(false);
        return thumbnail;
    }

    public void switchPanel(JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        this.revalidate();
        this.repaint();
    }

    public JScrollPane scrollPane(JPanel panel){
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    public void background(){
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(bgPanel);
    }

    private void categoryView(JPanel target){
        target.setLayout(new BoxLayout(target, BoxLayout.Y_AXIS));
        target.setBorder(BorderFactory.createEmptyBorder(11, 30, 11, 11));
        target.setPreferredSize(new Dimension(200, HEIGHT * 2));
    }

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
        String temp;
        String selectedCategory = cmd.substring(1);
        if (e.getSource() == start){
            switchPanel(menu(categories[0]));
            System.out.println("Switched to Food Menu!");
        } else if (e.getSource() == cancelButton) {
            switchPanel(start());
            background();
            System.out.println("Switched to Home Menu!");
        } else if(cmd.contains(",")){
            Product p = searchProduct(cmd);
            if(p != null){
                switchPanel(menuCart(p.category(), p));
            }
            System.out.println(cmd);
        } else if (cmd.contains("*")) {

            switchPanel(menu(selectedCategory));
            System.out.println(selectedCategory);
        } else if (e.getSource() == addToCart) {
            for (Product p: products){
                String number = String.valueOf(p.productID());
                if (cmd.contains(number)){
                    orderedItems.addToBasket(p);
//                    System.out.println(number);
                    System.out.println("prep time is " + orderedItems.getTotalPrepTime());
                    System.out.println("total amount € " + orderedItems.getTotalPrice());
//                    System.out.println(cmd.substring(11));
//                    System.out.println(orderedItems.size());
                }
            }
        }
    }
}