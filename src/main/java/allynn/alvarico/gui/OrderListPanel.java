package allynn.alvarico.gui;

import allynn.alvarico.OrderItem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OrderListPanel extends JPanel {

    private JLabel titleLabel;
    private JPanel orderPanel;
    private ArrayList<OrderItem> customerOrder;
    private JLabel totalItemsLabel;
    private JLabel totalPriceLabel;
    private JLabel totalPrepTimeLabel;
    private final Color bgWhite;
    private JButton removeButton;

    public OrderListPanel(Font f, ArrayList<OrderItem> customerOrder, Color white) {
        this.customerOrder = customerOrder;
        bgWhite = white;
        setLayout(new BorderLayout(5, 5));
        setBackground(bgWhite);
        setMaximumSize(new Dimension(400, 300));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("Order List", SwingConstants.CENTER);
        titleLabel.setFont(f);
        add(titleLabel, BorderLayout.NORTH);

        orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        orderPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(orderPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMaximumSize(new Dimension(380, 250));
        add(scrollPane, BorderLayout.CENTER);

        add(createTotalsPanel(f), BorderLayout.SOUTH);
        updateTotals();
    }

    private JPanel createTotalsPanel(Font f) {
        JPanel totalsPanel = new JPanel();
        totalsPanel.setBackground(bgWhite);
        totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        totalItemsLabel = new JLabel("Total Items: 0");
        totalPriceLabel = new JLabel("Total Price: € 0.00");
        totalPrepTimeLabel = new JLabel("Preparation Time: 0 mins");

        Font smallerFont = f.deriveFont(12f);
        totalItemsLabel.setFont(smallerFont);
        totalPriceLabel.setFont(smallerFont);
        totalPrepTimeLabel.setFont(smallerFont);

        totalsPanel.add(totalItemsLabel);
        totalsPanel.add(Box.createVerticalStrut(5));
        totalsPanel.add(totalPriceLabel);
        totalsPanel.add(Box.createVerticalStrut(5));
        totalsPanel.add(totalPrepTimeLabel);

        return totalsPanel;
    }

    private void updateTotals() {
        int totalItems = 0;
        double totalPrice = 0.0;
        int totalPrepTime = 0;

        if (customerOrder != null) {
            for (OrderItem item : customerOrder) {
                totalItems += item.getProductQuantity();
                totalPrice += item.getTotal();
                totalPrepTime += item.getProduct().productPrepTime() * item.getProductQuantity();
            }
        }

        totalItemsLabel.setText(String.format("Total Items: %d", totalItems));
        totalPriceLabel.setText(String.format("Total Price: € %.2f", totalPrice));
        totalPrepTimeLabel.setText(String.format("Preparation Time: %d mins", totalPrepTime));
    }

    public void addOrderItem(OrderItem orderItem) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setBackground(getBackground());
        panel.setMaximumSize(new Dimension(360, 40));
        panel.setPreferredSize(new Dimension(360, 40));

        JLabel lblName = new JLabel(orderItem.getProduct().productName());
        lblName.setPreferredSize(new Dimension(100, 30));

        JLabel lblAmount = new JLabel(String.valueOf(orderItem.getProductQuantity()));
        lblAmount.setPreferredSize(new Dimension(50, 30));

        JLabel lblTotal = new JLabel(String.format("€%.2f", orderItem.getTotal()));
        lblTotal.setPreferredSize(new Dimension(70, 30));

        removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(80, 30));
        removeButton.setActionCommand("remove" + orderItem.getProduct().productID());

        removeButton.addActionListener(e -> {
            removeOrderItem(orderItem);
        });

        panel.add(lblName);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(lblAmount);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(lblTotal);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(removeButton);
        panel.add(Box.createHorizontalGlue());

        orderPanel.add(panel);
        orderPanel.add(Box.createVerticalStrut(5));
        orderPanel.setBackground(bgWhite);

        updateTotals();

        revalidate();
        repaint();
    }

    public JButton getRemoveCmd(){
        return removeButton;
    }

    public void removeOrderItem(OrderItem toRemove) {
        customerOrder.remove(toRemove);

        Component[] components = orderPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel panel) {
                for (Component inner : panel.getComponents()) {
                    if (inner instanceof JLabel label) {
                        if (label.getText().equals(toRemove.getProduct().productName())) {
                            orderPanel.remove(panel);
                            break;
                        }
                    }
                }
            }
        }
        updateTotals();
        revalidate();
        repaint();
    }
}