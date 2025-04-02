package allynn.alvarico;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Waiter extends Thread {
    private JFrame frame;
    private JPanel ordersPanel;
    private JPanel preparingPanel;
    private JPanel collectionPanel;
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    private final Font font;
    private ConcurrentHashMap<Integer, ArrayList<OrderItem>> orderQueue;
    private boolean newOrder;
    private Chef chef;

    public Waiter(Font font, int x, int y) {
        this.font = font;
        this.orderQueue = new ConcurrentHashMap<>();
        this.newOrder = false;
        initializeGUI(x, y);
    }
    public void setChef(Chef chef) {
        this.chef = chef;
    }

    private void initializeGUI(int x, int y) {
        frame = new JFrame("Order Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(x, y);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Current Orders", SwingConstants.CENTER);
        headerLabel.setFont(font.deriveFont(24f));
        headerLabel.setBackground(Color.RED);
        headerLabel.setOpaque(true);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ordersPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        ordersPanel.setBackground(Color.WHITE);
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        JLabel preparingHeader = new JLabel("Preparing", SwingConstants.CENTER);
        preparingHeader.setFont(font);
        preparingHeader.setBackground(Color.LIGHT_GRAY);
        preparingHeader.setForeground(Color.WHITE);
        preparingHeader.setOpaque(true);
        preparingHeader.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        preparingPanel = new JPanel();
        preparingPanel.setLayout(new BoxLayout(preparingPanel, BoxLayout.Y_AXIS));
        preparingPanel.setBackground(Color.WHITE);
        preparingPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        leftPanel.add(preparingHeader, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(preparingPanel), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JLabel collectionHeader = new JLabel("Collection", SwingConstants.CENTER);
        collectionHeader.setFont(font);
        collectionHeader.setBackground(Color.DARK_GRAY);
        collectionHeader.setForeground(Color.WHITE);
        collectionHeader.setOpaque(true);
        collectionHeader.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        collectionPanel = new JPanel();
        collectionPanel.setLayout(new BoxLayout(collectionPanel, BoxLayout.Y_AXIS));
        collectionPanel.setBackground(Color.WHITE);
        collectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        rightPanel.add(collectionHeader, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(collectionPanel), BorderLayout.CENTER);

        ordersPanel.add(leftPanel);
        ordersPanel.add(rightPanel);

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(ordersPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public synchronized void addOrder(Integer orderNumber, ArrayList<OrderItem> items) {
        orderQueue.put(orderNumber, items);
        newOrder = true;
        this.notify();

        // Wait until GUI shows the receipt before displaying order
        Timer orderDisplayTimer = new Timer(8000, e -> { // 8 seconds to account for receipt display
            updateOrdersDisplay();

            // Send to chef after displaying order
            Timer chefTimer = new Timer(5000, e2 -> {
                chef.receiveOrder(orderNumber, items);
                ((Timer) e2.getSource()).stop();
            });
            chefTimer.setRepeats(false);
            chefTimer.start();

            ((Timer) e.getSource()).stop();
        });
        orderDisplayTimer.setRepeats(false);
        orderDisplayTimer.start();
    }

    public void orderCompleted(Integer orderNumber) {
        SwingUtilities.invokeLater(() -> {
            // Move order to collection panel
            JLabel collectLabel = new JLabel("Order #" + orderNumber);
            collectLabel.setFont(font);
            collectLabel.setHorizontalAlignment(SwingConstants.CENTER);
            collectLabel.setBorder(new LineBorder(new Color(0, 200, 0), 2));
            collectLabel.setBackground(new Color(220, 255, 220));
            collectLabel.setOpaque(true);

            // Remove from preparing panel first
            Component[] components = preparingPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    if (label.getText().equals(String.format("Order #%03d", orderNumber))) {
                        preparingPanel.removeAll(); // Remove all components
                        preparingPanel.revalidate();
                        preparingPanel.repaint();
                        break;
                    }
                }
            }

            // Add to collection panel
            collectionPanel.add(collectLabel);
            Component spacer = Box.createRigidArea(new Dimension(0, 5));
            collectionPanel.add(spacer);
            collectionPanel.revalidate();
            collectionPanel.repaint();

            // Remove from collection panel after 5 seconds
            Timer removalTimer = new Timer(5000, e -> {
                collectionPanel.remove(collectLabel);
                collectionPanel.remove(spacer);
                collectionPanel.revalidate();
                collectionPanel.repaint();
                ((Timer) e.getSource()).stop();
            });
            removalTimer.setRepeats(false);
            removalTimer.start();
        });
    }

    private void updateOrdersDisplay() {
        SwingUtilities.invokeLater(() -> {
            preparingPanel.removeAll();
            if (orderQueue.isEmpty()) {
                JLabel noOrderLabel = new JLabel("No orders in preparation");
                noOrderLabel.setFont(font);
                noOrderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                preparingPanel.add(noOrderLabel);
            } else {
                orderQueue.forEach((orderNum, items) -> {
                        JLabel orderLabel = new JLabel(String.format("Order #%03d", orderNum));
                        orderLabel.setFont(font);
                        orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        orderLabel.setBorder(new LineBorder(new Color(255, 198, 0), 2));
                        orderLabel.setBackground(Color.WHITE);
                        orderLabel.setOpaque(true);
                        preparingPanel.add(orderLabel);
                        preparingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                });
            }
            preparingPanel.revalidate();
            preparingPanel.repaint();
        });
    }


    public synchronized void waitForOrder() throws InterruptedException {
        while (!newOrder) {
            wait();
        }
        newOrder = false;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                waitForOrder();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}