package org.example;

import org.example.customs.BackgroundPanel;
import org.example.customs.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * References
 * *https://stackoverflow.com/questions/2533650/transparent-jframe-background
 * https://loading.io/color/feature/mcdonalds/
*/

public class UserGraphicsInterface extends JFrame implements ActionListener {

    private final int HEIGHT = 1000;
    private final int WIDTH = 700;
    private int Yaxis = 50;
    private int Xaxis = 650;
    private JButton start, confirmButton, cancelButton;
    private Font f = new Font("Comic Sans MS", Font.BOLD, 18);

    public UserGraphicsInterface(){
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
        panel.setBounds(160, 450, 400, 200);
        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setBackground(new Color(0,0,0,0));

        start = new CustomButton("Start", "#ffc600", "#e76a05", "#bf0c0c");
        start.setBounds(90, 40, 220, 50);

        start.addActionListener(this);

        panel.add(start);

        return panel;
    }

    public JScrollPane foodCategory(){
        JPanel categoryPanel = new JPanel();
        String filepath = "categories\\";
        String[] categories = {
                "What's New", "Sharers & Bundles", "Burgers", "McNuggets and Selects", "Eurosaver Menu", "Happy Meal",
                "Fries & Sides", "Desserts", "Milkshakes & Cold Drinks", "Vegan", "Vegetarian",
                "McCafe", "Breakfast Menu", "Wraps and Salads", "Condiments & Sauces"};
        String[] imagesource = {
                "whatsnew", "sharer", "burger", "mcnuggets", "eurosaver", "happymeal", "fries", "desserts",
                "drinks", "vegan", "vegetarian", "mccafe", "breakfast","wraps", "condiments"};

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

        JScrollPane catScroll = new JScrollPane(categoryPanel);
        catScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        catScroll.getVerticalScrollBar().setUnitIncrement(20);

        return catScroll;
    }

    private JPanel menu() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setSize(WIDTH, HEIGHT);


        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(header(), BorderLayout.NORTH);
        mainPanel.add(foodCategory(), BorderLayout.WEST);
        mainPanel.add(foodDisplay(), BorderLayout.CENTER);
        mainPanel.add(buttonContainer(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel header(){
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(WIDTH, 100));
        headerPanel.setBackground(Color.WHITE);


        JLabel headerLabel = new JLabel("FOOD MENU");
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        headerLabel.setFont(f);

        headerPanel.add(headerLabel);
        return headerPanel;
    }

    private JScrollPane foodDisplay(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        for (int i = 0; i < 20; i++) {
            JPanel fc = foodCard(null, "Burger", 3.50);
            panel.add(fc);
        }
        panel.setLayout(new GridLayout(0, 2, 20, 20));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 90, 10, 50)); // Top, Left, Bottom, Right padding
        wrapperPanel.add(panel, BorderLayout.CENTER);
        wrapperPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        return scrollPane;
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

    private JPanel foodCard(String imagePath, String name, Double price) {

        JPanel foodCardLayout = new JPanel();
        foodCardLayout.setLayout(new BoxLayout(foodCardLayout,BoxLayout.Y_AXIS));
        foodCardLayout.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel foodName = new JLabel("Name: " + name);
        JLabel foodPrice = new JLabel("Price: " + price);

        JButton product = thumbnail("hamburger.jpeg");
        product.setActionCommand(name + "," + price);
        product.addActionListener(this);

        foodCardLayout.add(product,BorderLayout.NORTH);
        foodCardLayout.add(foodName, BorderLayout.CENTER);
        foodCardLayout.add(foodPrice, BorderLayout.SOUTH);

        foodCardLayout.setBackground(Color.WHITE);
        return foodCardLayout;
    }

    // Utilities
    private JButton thumbnail(String source){
        String rsc = "resource\\images\\" + source;
        ImageIcon img = new ImageIcon(rsc);
        JButton thumbnail = new JButton();

        thumbnail.setIcon(img);
        thumbnail.setPreferredSize(new Dimension(WIDTH / 4, 100));
        thumbnail.setBackground(Color.WHITE);
        thumbnail.setFocusable(false);
        //thumbnail.addActionListener(this);
        return thumbnail;
    }

    public void switchPanel(JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        this.revalidate();
        this.repaint();
    }
    public void switchFoodPanel(JPanel panel) {

        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        this.revalidate();
        this.repaint();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (e.getSource() == start){
            switchPanel(menu());
            System.out.println("Switched to Food Menu!");
        } else if (e.getSource() == cancelButton) {
            switchPanel(start());
            background();
            System.out.println("Switched to Home Menu!");
        } else if(cmd != null && cmd.contains(",")){
            System.out.println(cmd);
        } else if (cmd != null && cmd.contains("*")) {
            System.out.println(cmd);
        }
    }
}