package org.example;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class UserGraphicsInterface extends JFrame {

    private int WIDTH = 500;
    private int HEIGHT = 800;

    private int YAXIS = 100;
    private int XAXIS = 500;

    public UserGraphicsInterface(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setTitle("Ordering System");
        this.add(mainMenu());

        this.setSize(WIDTH, HEIGHT);
        this.setLocation(XAXIS, YAXIS);

        this.setVisible(true);
    }

    public JPanel mainMenu(){
        JPanel panel = new JPanel();

        this.setLayout(new BorderLayout());
        panel.setBorder(new EtchedBorder());
        panel.setLayout(null);


        JButton eatin = new JButton("Eat In");
        JButton takeaway = new JButton("Take Away");

        eatin.setLocation(100, 100);
        eatin.setSize(new Dimension(100, 40));

        takeaway.setLocation(230, 100);
        takeaway.setSize(new Dimension(100, 40));

        panel.add(eatin);
        panel.add(takeaway);

        return panel;
    }

}
