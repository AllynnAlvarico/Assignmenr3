package allynn.alvarico.gui;

import allynn.alvarico.OrderItem;
import allynn.alvarico.customs.CustomButton;

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
    private CustomButton removeButton;
    GraphicUtilities gUtils;
    Font font;

    public OrderListPanel(Font f, ArrayList<OrderItem> customerOrder, Color white) {
        gUtils = new GraphicUtilities();
        font = f;
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

        gUtils.addComponent(
                totalsPanel, totalItemsLabel, Box.createVerticalStrut(5),
                totalPriceLabel, Box.createVerticalStrut(5), totalPrepTimeLabel
        );

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

        removeButton = new CustomButton("Remove");

        removeButton.setFont(font.deriveFont(10f));
        removeButton.setPreferredSize(new Dimension(80, 10));
        removeButton.setActionCommand("remove" + orderItem.getProduct().productID());

        removeButton.addActionListener(e -> {
            removeOrderItem(orderItem);
        });
        gUtils.addComponent(panel, lblName,
                Box.createHorizontalStrut(10), lblAmount,
                Box.createHorizontalStrut(10), lblTotal,
                Box.createHorizontalStrut(10), removeButton,
                Box.createHorizontalGlue());
        gUtils.addComponent(orderPanel, panel, Box.createVerticalStrut(5));
        orderPanel.setBackground(bgWhite);
        updateTotals();
        gUtils.refreshComponent(this);
    }

    public CustomButton getRemoveCmd(){
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
        gUtils.refreshComponent(this);
    }
}