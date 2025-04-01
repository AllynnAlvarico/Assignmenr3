package allynn.alvarico;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Waiter extends Thread {
    private JFrame frame;
    private JPanel ordersPanel;
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    private final Font font;
    private ConcurrentHashMap<Integer, ArrayList<OrderItem>> orderQueue;
    private boolean newOrder, isProcessingOrder;
    private Chef chef;

    public Waiter(Font font, int x, int y) {
        this.font = font;
        this.orderQueue = new ConcurrentHashMap<>();
        this.newOrder = false;
        this.isProcessingOrder = false;
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
        headerLabel.setBackground(new Color(255, 198, 0));
        headerLabel.setOpaque(true);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(Color.WHITE);
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public synchronized void addOrder(Integer orderNumber, ArrayList<OrderItem> items) {
            orderQueue.put(orderNumber, items);
            chef.receiveOrder(orderNumber, items);
            newOrder = true;
            this.notify();
            updateOrdersDisplay();
    }

    public synchronized void orderCompleted(Integer orderNumber) {
        SwingUtilities.invokeLater(() -> {
            orderQueue.remove(orderNumber);
            updateOrdersDisplay();
            isProcessingOrder = false;

            if (!orderQueue.isEmpty()) {
                Timer timer = new Timer(5000, event -> {
                    if (!isProcessingOrder) {
                        isProcessingOrder = true;
                        Integer nextOrder = orderQueue.keySet().iterator().next();
                        chef.receiveOrder(nextOrder, orderQueue.get(nextOrder));
                        ((Timer) event.getSource()).stop();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

    private void updateOrdersDisplay() {
        new Timer(5000, evt -> {
            SwingUtilities.invokeLater(() -> {
                ordersPanel.removeAll();
                orderQueue.forEach((orderNum, items) -> {
                    JLabel orderLabel = new JLabel(String.format("Order #%03d", orderNum));
                    orderLabel.setFont(font);
                    orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    orderLabel.setBorder(new LineBorder(new Color(255, 198, 0), 2));
                    orderLabel.setBackground(Color.WHITE);
                    orderLabel.setOpaque(true);
                    ordersPanel.add(orderLabel);
                    ordersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                });
                ordersPanel.revalidate();
                ordersPanel.repaint();
            });
        }).start();
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