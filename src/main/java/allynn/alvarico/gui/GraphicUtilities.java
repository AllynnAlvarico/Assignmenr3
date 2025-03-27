package allynn.alvarico.gui;

import allynn.alvarico.customs.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class GraphicUtilities {

    private int WIDTH;
    private int HEIGHT;
    private Font f;

    public GraphicUtilities(Font font, int byRef_width, int byRef_height){
        HEIGHT = byRef_height;
        WIDTH = byRef_width;
        f = font;

    }

    public JPanel header(String selectedCategory){
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(WIDTH, 100));
        headerPanel.setBackground(Color.WHITE);


        JLabel headerLabel = new JLabel(selectedCategory);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        headerLabel.setFont(f);

        headerPanel.add(headerLabel);
        return headerPanel;
    }
    public JButton thumbnail(String source){
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

    public void switchPanel(JFrame frame, JPanel panel) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    public JScrollPane scrollPane(JPanel panel){
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    public void background(JFrame frame){
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setBounds(0, 0, WIDTH, HEIGHT);
        frame.add(bgPanel);
    }

    public void categoryView(JPanel target){
        target.setLayout(new BoxLayout(target, BoxLayout.Y_AXIS));
        target.setBorder(BorderFactory.createEmptyBorder(11, 30, 11, 11));
        target.setPreferredSize(new Dimension(200, HEIGHT * 2));
    }

    public void mainPanelSetup(JPanel mainPanel, JPanel north, JScrollPane west, JScrollPane center, JPanel south){
        mainPanel.setSize(WIDTH, HEIGHT);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(north, BorderLayout.NORTH);
        mainPanel.add(west, BorderLayout.WEST);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(south, BorderLayout.SOUTH);
    }
}
