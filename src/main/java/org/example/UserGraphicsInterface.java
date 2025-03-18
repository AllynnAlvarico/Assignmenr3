package org.example;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
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

    private final int HEIGHT = 900;
    private final int WIDTH = 700;
    private int Yaxis = 100;
    private int Xaxis = 500;
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
        panel.setBounds(50, 450, 400, 200);
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

        categoryView(categoryPanel);

        for (int repeat = 1; repeat <= 18; repeat++){
            categoryPanel.add(thumbnail("hamburger.jpeg"));
            categoryPanel.add(new JLabel("Hello"));
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

        int numItems = 0;

        for (int i = 0; i < 10; i++) {
            numItems = i; //getting the total number of panel
            JPanel fc = foodCard(null, "Burger", 3.50);
            panel.add(fc);
        }

        //re-calculate the size when the number of panel increase
        int rows = numItems;
        int panelHeight = rows * 235;
        panel.setLayout(new GridLayout(5, 6));

        panel.setPreferredSize(new Dimension(WIDTH, panelHeight));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
        foodCardLayout.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));

        JLabel foodPrice = new JLabel("Price: " + String.valueOf(price));
        foodCardLayout.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));

        foodCardLayout.add(thumbnail("hamburger.jpeg"),BorderLayout.NORTH);
        foodCardLayout.add(foodName, BorderLayout.CENTER);
        foodCardLayout.add(foodPrice, BorderLayout.SOUTH);

        foodCardLayout.setBackground(Color.WHITE);
        return foodCardLayout;
    }

    // Utilities
    private JLabel thumbnail(String source){
        String rsc = "resource\\images\\" + source;
        ImageIcon img = new ImageIcon(rsc);
        JLabel thumbnail = new JLabel();
        thumbnail.setIcon(img);
        thumbnail.setPreferredSize(new Dimension(WIDTH / 4, 100));
        return thumbnail;
    }

    public void switchPanel(JPanel panel) {
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start){
            switchPanel(menu());
            System.out.println("Switched to Food Menu!");
        } else if (e.getSource() == cancelButton) {
            switchPanel(start());
            background();
            System.out.println("Switched to Home Menu!");
        }
    }
}

class BackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = new ImageIcon("resource\\images\\background.png").getImage();
        Image icon = new ImageIcon("resource\\images\\icon.png").getImage();
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        g.drawImage(icon, 160, 330, this);
    }
}
class CustomButton extends JButton {
    private final Color normalColor;
    private final Color hoverColor;
    private final Color clickColor;

    public CustomButton(String text, String hexMaintone, String hexHovertone, String hexClicktone) {
        super(text);
        normalColor = Color.decode(hexMaintone);
        hoverColor = Color.decode(hexHovertone);
        clickColor = Color.decode(hexClicktone);

        setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        setForeground(Color.WHITE);
        setBackground(normalColor);

        setFocusable(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // Rounded corners

        super.paintComponent(g);
    }
}
