package allynn.alvarico.gui;

import allynn.alvarico.OrderItem;
import allynn.alvarico.customs.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphicUtilities {

    private int WIDTH;
    private int HEIGHT;
    private Font f;

    public GraphicUtilities(){
        WIDTH = 800;
        HEIGHT = 600;
        f = new Font("Comic Sans MS", Font.PLAIN, 18);
    }

    public GraphicUtilities(Font font, int byRef_width, int byRef_height){
        HEIGHT = byRef_height;
        WIDTH = byRef_width;
        f = font;
    }

    public JPanel header(String selectedCategory){
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(WIDTH, 100));
        headerPanel.setBackground(getBackground());

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
        thumbnail.setBackground(getBackground());
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

    public void mainPanelSetup(JPanel mainPanel, String str_north, JScrollPane west, JScrollPane center, JPanel south){
        mainPanel.setSize(WIDTH, HEIGHT);
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(header(str_north), BorderLayout.NORTH);
        mainPanel.add(west, BorderLayout.WEST);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(south, BorderLayout.SOUTH);
    }

    public JPanel receiptPromptPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, WIDTH, HEIGHT);
        panel.setLayout(new BorderLayout());
        panel.setBackground(getBackground());

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>Please collect the receipt<br>and proceed to the cashier</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(f);
        messageLabel.setFont(messageLabel.getFont().deriveFont(32f));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        panel.add(messageLabel, BorderLayout.NORTH);

        ImageIcon icon = new ImageIcon("resource\\images\\receipt_icon.jpg");
        Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        return panel;
    }

    public JPanel orderDisplay(HashMap<Integer, ArrayList<OrderItem>> order) {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, WIDTH, HEIGHT);
        panel.setBackground(getBackground());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel thankYouLabel = new JLabel("Thank you! Please collect your");
        JLabel followUpLabel = new JLabel("number below and wait for it to");
        JLabel continueLabel = new JLabel("be called");

        setComponentFont(thankYouLabel, f, 32);
        setComponentFont(followUpLabel, f, 32);
        setComponentFont(continueLabel, f, 32);

        JLabel infoLabel = new JLabel("Your order number is");
        setComponentFont(infoLabel, f, 24);

        Integer lastOrderNumber = order.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0);

        JLabel numberLabel = new JLabel(String.format("%03d", lastOrderNumber));
        setComponentFont(numberLabel, f, 48);

        addComponent(panel,
                Box.createVerticalStrut(60), thankYouLabel, followUpLabel, continueLabel,
                Box.createVerticalStrut(40), infoLabel,
                Box.createVerticalStrut(10), numberLabel);
        return panel;
    }

    private void setComponentFont(JComponent component, Font font, float size){
            component.setFont(font);
            component.setFont(component.getFont().deriveFont(size));
            component.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        public void refreshComponent(JComponent component){
            component.revalidate();
            component.repaint();
        }

        public void setComponentUI(JComponent component, Font font, Color background, Color foreGround,boolean opaque){
            component.setFont(font);
            component.setBackground(background);
            component.setForeground(foreGround);
            component.setOpaque(opaque);
        }

        public void addComponent(JPanel panel, Component...components){
            for (Component component : components) {
                panel.add(component);
            }
        }

        public Color getBackground () {
            return Color.WHITE;
        }

        public void updatePreparringWindow (JPanel panel,int orderNumber){
            Component[] components = panel.getComponents();
            for (Component component : components) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    if (label.getText().equals(String.format("Order #%03d", orderNumber))) {
                        label.removeAll();
                        refreshComponent(label);
                        break;
                    }
                }
            }
        }
    }