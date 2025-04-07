package allynn.alvarico.gui;

import allynn.alvarico.Chef;
import allynn.alvarico.OrderItem;
import allynn.alvarico.SimulationClock;
import allynn.alvarico.Waiter;
import allynn.alvarico.customs.CustomButton;
import allynn.alvarico.product.Product;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * References
 * *https://stackoverflow.com/questions/2533650/transparent-jframe-background
 * https://loading.io/color/feature/mcdonalds/
 * https://stackoverflow.com/questions/6635730/how-do-i-put-html-in-a-jlabel-in-java-swing
 * https://stackoverflow.com/questions/39584096/using-a-swing-timer-in-java
*/

public class UserGraphicsInterface extends JFrame implements ActionListener {
    private final ArrayList<Product> products;
    private ArrayList<OrderItem> basket;
    private HashMap<Integer, ArrayList<OrderItem>> allOrder;
    private final int HEIGHT = 1000, WIDTH = 720, Yaxis = 50, Xaxis = 600;
    private int orderedQuantity = 1, orderNumber = 1;
    private CustomButton start, confirmButton, continueButton, cancelButton, addToCart, goBack, add, minus, removeProduct;
    private Font appFont;
    private JTextField amount;
    private String North = "North", South = "South", East = "East", West = "West", Center = "Center";
    private String choosenCategory;
    private String iconDirectory;
    String[] categories = {
            "What's New", "Sharers & Bundles", "Burgers", "McNuggets and Selects", "Wraps and Salads",
            "McCafe", "Breakfast Menu", "Vegetarian", "Vegan", "Eurosaver Menu", "Happy Meal",
            "Fries & Sides", "Desserts", "Milkshakes & Cold Drinks", "Condiments & Sauces"};
    private OrderListPanel olp;
    private GraphicUtilities gutils;
    private Waiter waiter;
    private Chef chef;

    public UserGraphicsInterface(ArrayList<Product> byRef_products, Font byRef_font, Color byRef_background, String iconPath) {
        iconDirectory = iconPath;
        this.appFont = byRef_font;
        this.getContentPane().setBackground(byRef_background);
        this.products = byRef_products;
        this.basket = new ArrayList<>();
        this.allOrder = new HashMap<>();
        gutils = new GraphicUtilities(byRef_font, WIDTH, HEIGHT);
        this.olp = new OrderListPanel(byRef_font, basket, gutils.getBackground());
        this.initialising();

        this.waiter = new Waiter(byRef_font, 200, Yaxis, iconDirectory);
        this.chef = new Chef(Xaxis + WIDTH, Yaxis, iconDirectory);
        this.waiter.setChef(chef);
        this.chef.setWaiter(waiter);
        this.waiter.start();
        this.chef.start();
    }

    private void initialising() {
        this.setTitle("Kiosk Ordering System");

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setLocation(Xaxis, Yaxis);
        this.setResizable(false);

        this.add(start());

        gutils.background(this);

        this.setVisible(true);

        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(iconDirectory));
    }

    private JPanel start() {
        JPanel panel = new JPanel();
        panel.setBounds(160, 650, 400, 200);
        panel.setLayout(null);
        panel.setOpaque(false);

        start = new CustomButton("Start");
        start.setBounds(90, 40, 220, 50);

        start.addActionListener(this);

        panel.add(start);

        return panel;
    }

    private JPanel menu(String selectedCategory) {
        choosenCategory = selectedCategory;
        JPanel mainPanel = new JPanel(new BorderLayout());
        gutils.mainPanelSetup(
                mainPanel, selectedCategory, foodCategory(), foodDisplay(selectedCategory), buttonContainer());
        return mainPanel;
    }

    private JPanel itemDisplay(String selectedCategory, Product product) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        gutils.mainPanelSetup(
                mainPanel, selectedCategory, foodCategory(), singleDisplay(product), buttonContainer());
        return mainPanel;
    }

    public JScrollPane foodCategory() {
        JPanel categoryPanel = new JPanel();
        String filepath = "categories\\";
        String[] imagesource = {
                "whatsnew", "sharer", "burger", "mcnuggets", "wraps", "mccafe", "breakfast", "vegetarian",
                "vegan", "eurosaver", "happymeal", "fries", "desserts", "drinks", "condiments"};

        gutils.categoryView(categoryPanel);
        JButton btnCategory;

        for (int repeat = 0; repeat < categories.length; repeat++) {
            btnCategory = gutils.thumbnail(filepath + imagesource[repeat] + ".jpeg");
            btnCategory.setActionCommand("*" + categories[repeat]);
            btnCategory.addActionListener(this);

            gutils.addComponent(categoryPanel, btnCategory, new JLabel(categories[repeat]), Box.createRigidArea(new Dimension(0, 20)));
        }
        return gutils.scrollPane(categoryPanel);
    }

    private JScrollPane foodDisplay(String selectedCategory) {
        String command;

        JPanel panel = new JPanel();
        panel.setBackground(getBackground());

        if (selectedCategory.equalsIgnoreCase("Basket")) {
            for (Product p : products) {
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
        wrapperPanel.add(panel, Center);
        wrapperPanel.setBackground(getBackground());

        return gutils.scrollPane(wrapperPanel);
    }

    private JScrollPane singleDisplay(Product byRef_product) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(getBackground());

        addToCart = new CustomButton("Add to Cart");
        goBack = new CustomButton("Go Back");
        add = new CustomButton("+");
        minus = new CustomButton("-");
        amount = new JTextField("1", 3);


        amount.setHorizontalAlignment(JTextField.CENTER);

        addToCart.setActionCommand(addToCart.getText() + byRef_product.productID());

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
        controlsPanel.setBackground(getBackground());
        gutils.addComponent(controlsPanel, minus, amount, add, addToCart, goBack);
        panel.add(controlsPanel, gbc);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 50));
        wrapperPanel.add(panel, BorderLayout.CENTER);
        wrapperPanel.setBackground(getBackground());

        return gutils.scrollPane(wrapperPanel);
    }

    private JPanel foodCard(String imagePath, String name, Double price, String actionCommand, boolean btnState) {
        String priceFormat = String.format("Price: € %.2f", price);
        JPanel foodCardLayout = new JPanel();

        foodCardLayout.setLayout(new BoxLayout(foodCardLayout, BoxLayout.Y_AXIS));
        foodCardLayout.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel foodName = new JLabel(name);
        JLabel foodPrice = new JLabel(priceFormat);
        JButton product = gutils.thumbnail(imagePath + ".jpeg");
        product.setEnabled(btnState);
        product.setDisabledIcon(product.getIcon());
        product.setOpaque(true);

        product.setActionCommand(actionCommand);
        product.addActionListener(this);

        foodCardLayout.add(product, North);
        foodCardLayout.add(foodName, Center);
        foodCardLayout.add(foodPrice, South);

        foodCardLayout.setBackground(getBackground());
        return foodCardLayout;
    }

    public JPanel menuCart() {
        JPanel panel = new JPanel(new BorderLayout());
        gutils.mainPanelSetup(panel, "Customer Basket", foodCategory(), gutils.scrollPane(olp), buttonContainer());
        return panel;
    }

    public JPanel paymentSelectionMethod() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setBackground(gutils.getBackground());

        panel.setBounds((Xaxis/4) + 20, Yaxis + 100, WIDTH/2, HEIGHT/2);

        JLabel vatLabel, serviceTaxLabel, totalLabel;

        JPanel itemListPanel = orderListPanel();

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(gutils.getBackground());
        center.add(itemListPanel, Center);
        panel.add(center, Center);

        JPanel totalsPanel = new JPanel();
        totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
        totalsPanel.setBackground(gutils.getBackground());

        double subtotal = basket.stream().mapToDouble(OrderItem::getTotal).sum();
        double vat = subtotal * 0.12;
        double serviceTax = subtotal * 0.04;
        double total = subtotal + vat + serviceTax;

        vatLabel = new JLabel(String.format("VAT%%:  € %.2f", vat));
        serviceTaxLabel = new JLabel(String.format("Service Taxes:  € %.2f", serviceTax));
        totalLabel = new JLabel(String.format("Total:  € %.2f", total));

        gutils.addComponent(totalsPanel, Box.createVerticalStrut(10), vatLabel, serviceTaxLabel, totalLabel);

        JPanel paymentButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton payCard = new CustomButton("Pay by Card");
        JButton payCounter = new CustomButton("Pay at Counter");
        paymentButtons.setBackground(gutils.getBackground());
        gutils.addComponent(paymentButtons, payCard, payCounter);

        payCard.addActionListener(this);
        payCounter.addActionListener(this);

        gutils.addComponent(totalsPanel, Box.createVerticalStrut(10), paymentButtons);

        panel.add(totalsPanel, South);

        return panel;
    }

    public JPanel cardPaymentPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, WIDTH, HEIGHT);
        panel.setLayout(new BorderLayout());
        panel.setBackground(getBackground());

        JLabel instructionLabel = new JLabel("<html><div style='text-align: center;'>Place your card on the screen<br>of the keypad below</div></html>", SwingConstants.CENTER);
        instructionLabel.setFont(appFont);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        panel.add(instructionLabel, North);

        ImageIcon icon = new ImageIcon("resource\\images\\card_tap_icon.jpg");
        Image image = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, Center);

        CustomButton backButton = new CustomButton("Confirm Card");
        backButton.defaultSettings();
        backButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(getBackground());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        bottomPanel.add(backButton);

        panel.add(bottomPanel, South);

        return panel;
    }

    private JPanel orderListPanel() {
        JPanel itemListPanel = new JPanel();
        itemListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        itemListPanel.setLayout(new BoxLayout(itemListPanel, BoxLayout.Y_AXIS));
        itemListPanel.setBackground(gutils.getBackground());

        for (OrderItem item : basket) {
            JLabel label = new JLabel(item.getProduct().productName() + " x " + item.getProductQuantity());
            JLabel price = new JLabel(String.format("€ %.2f", item.getTotal()));
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.add(label, West);
            row.add(price, East);
            itemListPanel.add(row);
        }
        return itemListPanel;
    }


    public JPanel buttonContainer() {
        int btnWidth = 220;
        int btnHeight = 40;
        Dimension d = new Dimension(btnWidth, btnHeight);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        cancelButton = new CustomButton("Cancel");
        confirmButton = new CustomButton("Confirm");
        continueButton = new CustomButton("Continue Ordering");

        cancelButton.setPreferredSize(d);
        confirmButton.setPreferredSize(d);
        continueButton.setPreferredSize(d);

        bottomPanel.setBackground(getBackground());
        bottomPanel.setPreferredSize(new Dimension(WIDTH, 100));

        gutils.addComponent(bottomPanel, cancelButton, continueButton, confirmButton);

        cancelButton.addActionListener(this);
        confirmButton.addActionListener(this);
        continueButton.addActionListener(this);

        return bottomPanel;
    }

    private void addToCartItem(String byRef_cmd) {
        String productId = byRef_cmd.replace("Add to Cart", "").trim();

        for (Product p : products) {
            if (productId.equals(String.valueOf(p.productID()))) {
                orderedQuantity = Integer.parseInt(amount.getText());
                OrderItem orderItem = new OrderItem(p, orderedQuantity);
                orderItem.setOrderNumber(orderNumber);
                basket.add(orderItem);
                olp.addOrderItem(orderItem);
                removeProduct = olp.getRemoveCmd();
                removeProduct.addActionListener(this);
//                System.out.println("Added to cart: " + p.productName());
            }
        }

        orderedQuantity = 1;
    }

    private void resetOrder() {
        basket = new ArrayList<>();
        olp = new OrderListPanel(appFont, basket, gutils.getBackground());
        orderedQuantity = 1;
    }

    private void finishOrder() {
        allOrder.put(orderNumber, new ArrayList<>(basket));
        waiter.addOrder(orderNumber, new ArrayList<>(basket));
        orderNumber++;
        terminalLogs();
    }

    private void terminalLogs(){
        System.out.printf("========================== %s ==========================\n", this.getClass());
        System.out.println("Order Confirmed");
        System.out.printf("Thread Name: %s\n", this.getName());
        System.out.println("Passing to Waiter class");
    }

    private void handlePaymentCompletion() {
        gutils.switchPanel(this, gutils.receiptPromptPanel());
        Timer timer = new Timer(SimulationClock.paymentDelay, new ActionListener() {
            private int step = 0;
            @Override
            public void actionPerformed(ActionEvent evt) {
                step++;
                if (step == 1) {
                    gutils.switchPanel(UserGraphicsInterface.this, gutils.orderDisplay(allOrder));
                } else if (step == 2) {
                    resetOrder();
                    gutils.switchPanel(UserGraphicsInterface.this, start());
                    gutils.background(UserGraphicsInterface.this);
                    ((Timer) evt.getSource()).stop();
                }
            }
        });

        timer.setInitialDelay(3000);
        timer.start();
        finishOrder();
    }

    private Product searchProduct(String actionCommand) {
        String productId = actionCommand.replace("remove", "").trim();
        Product itemSearch = null;
        for (Product p : products) {
            if (actionCommand.equals(p.productID() + "," + p.productName())) {
                itemSearch = p;
            }
            if (productId.equalsIgnoreCase(String.valueOf(p.productID()))) {
                itemSearch = p;
            }
        }
        return itemSearch;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String selectedCategory = cmd.substring(1);
        if (e.getSource() == start) {
            gutils.switchPanel(this, menu(categories[0]));
//            System.out.println("Switched to Food Menu!");
        } else if (e.getSource() == cancelButton) {
            resetOrder();
            gutils.switchPanel(this, start());
            gutils.background(this);
//            System.out.println("Switched to Home Menu!");
        } else if (e.getSource() == continueButton) {
            System.out.println(choosenCategory);
            gutils.switchPanel(this, menu(choosenCategory));
        } else if (cmd.contains(",")) {
            Product p = searchProduct(cmd);
            if (p != null) {
                gutils.switchPanel(this, itemDisplay(p.category(), p));
            }
//            System.out.println(cmd);
        } else if (cmd.contains("*")) {
            choosenCategory = selectedCategory;
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
        } else if (e.getSource() == removeProduct) {
            Product p = searchProduct(removeProduct.getActionCommand());
            if (p != null) {
                OrderItem toRemove = null;
                for (OrderItem item : basket) {
                    if (item.getProduct().equals(p)) {
                        toRemove = item;
                        break;
                    }
                }
                if (toRemove != null) {
                    basket.remove(toRemove);
                    olp.removeOrderItem(toRemove);
                    System.out.println("Removed from cart: " + p.productName());
                    gutils.switchPanel(this, menuCart());
                }
            }
        } else if (e.getSource() == confirmButton){
//            System.out.println("Finish Order");
            gutils.switchPanel(this, paymentSelectionMethod());
        } else if (e.getActionCommand().equalsIgnoreCase("Pay by Card")){
            gutils.switchPanel(this, cardPaymentPanel());
        } else if (e.getActionCommand().equalsIgnoreCase("Pay at Counter") || e.getActionCommand().equalsIgnoreCase("Confirm Card")){
            handlePaymentCompletion();
        }
    }
}