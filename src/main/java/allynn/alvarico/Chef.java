package allynn.alvarico;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Chef extends Thread {
    private JFrame frame;
    private JPanel ordersPanel;
    private JLabel timerLabel;
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    private final Font font;
    private ConcurrentHashMap<Integer, ArrayList<OrderItem>> orderQueue;
    private Waiter waiter;
    private boolean isProcessing;
    private Integer currentOrder;

    public Chef(int x, int y) {
        this.font = new Font("Comic Sans MS", Font.BOLD, 18);
        this.orderQueue = new ConcurrentHashMap<>();
        this.isProcessing = false;
        initializeGUI(x, y);
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    private void initializeGUI(int x, int y) {
        frame = new JFrame("Kitchen Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(x, y);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Current Order", SwingConstants.CENTER);
        headerLabel.setFont(font.deriveFont(24f));
        headerLabel.setBackground(new Color(255, 198, 0));
        headerLabel.setOpaque(true);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(Color.WHITE);
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timerLabel = new JLabel("No order in preparation", SwingConstants.CENTER);
        timerLabel.setFont(font);
        timerLabel.setForeground(new Color(255, 0, 0));
        ordersPanel.add(timerLabel);

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public synchronized void receiveOrder(Integer orderNumber, ArrayList<OrderItem> items) {
        if (currentOrder != null) {
            // If there's already an order being processed, add to queue and return
            orderQueue.put(orderNumber, items);
            return;
        }

        currentOrder = orderNumber;
        orderQueue.put(orderNumber, items);

        SwingUtilities.invokeLater(() -> {
            ordersPanel.removeAll();
            JLabel preparingLabel = new JLabel("Preparing Order #" + orderNumber);
            preparingLabel.setFont(font);
            ordersPanel.add(preparingLabel);
            ordersPanel.revalidate();
            ordersPanel.repaint();

            // Add initial delay before starting countdown
            Timer initialDelay = new Timer(5000, e -> {
                startCountdown(orderNumber, items);
                ((Timer) e.getSource()).stop();
            });
            initialDelay.setRepeats(false);
            initialDelay.start();
        });
    }


    private void startCountdown(Integer orderNumber, ArrayList<OrderItem> items) {
        int totalTime = calculatePrepTime(items);
        Timer timer = new Timer(1000, new ActionListener() {
            int timeLeft = totalTime;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    updateCountdownDisplay(orderNumber, timeLeft);
                    timeLeft--;
                } else {
                    ((Timer) e.getSource()).stop();
                    orderCompleted(orderNumber);
                }
            }
        });
        timer.start();
    }

    private void updateOrdersDisplay() {

        new Timer(5000, evt -> {
            SwingUtilities.invokeLater(() -> {
                ordersPanel.removeAll();
                if (currentOrder != null) {
                    JLabel orderLabel = new JLabel(String.format("Preparing Order #%03d", currentOrder));
                    orderLabel.setFont(font);
                    orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    orderLabel.setBorder(new LineBorder(new Color(255, 198, 0), 2));
                    orderLabel.setBackground(Color.WHITE);
                    orderLabel.setOpaque(true);
                    ordersPanel.add(orderLabel);
                    ordersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    ordersPanel.add(timerLabel);
                } else {
                    timerLabel.setText("No order in preparation");
                    ordersPanel.add(timerLabel);
                }
                ordersPanel.revalidate();
                ordersPanel.repaint();
            });
        }).start();

    }

    private int calculatePrepTime(ArrayList<OrderItem> items) {
        int totalPrepTime = 0;
        for (OrderItem item : items) {
            totalPrepTime += item.getProduct().productPrepTime() * item.getProductQuantity();
        }
        return totalPrepTime;
    }

    private void updateTimer(int secondsLeft) {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText(String.format("Time remaining: %d seconds", secondsLeft));
        });
    }

    public synchronized void waitForOrder() throws InterruptedException {
        while (!isProcessing) {
            wait();
        }
    }

    private void prepareOrder(Integer orderNumber, ArrayList<OrderItem> items) {

        try {
            Thread.sleep(5000);
            int prepTime = calculatePrepTime(items);
            System.out.println("Preparing Order #" + orderNumber + " (Prep time: " + prepTime + " seconds)");

            for (int i = prepTime; i > 0; i--) {
                updateTimer(i);
                Thread.sleep(1000);
            }

            orderQueue.remove(orderNumber);
            currentOrder = null;
            updateOrdersDisplay();

            synchronized (waiter) {
                waiter.orderCompleted(orderNumber);
                waiter.notify();
            }

            isProcessing = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void updateCountdownDisplay(Integer orderNumber, int timeLeft) {
        SwingUtilities.invokeLater(() -> {
            ordersPanel.removeAll();
            JLabel orderLabel = new JLabel("Preparing Order #" + orderNumber);
            orderLabel.setFont(font);
            ordersPanel.add(orderLabel);

            JLabel timerLabel = new JLabel("Time remaining: " + timeLeft + " seconds");
            timerLabel.setFont(font);
            timerLabel.setForeground(Color.RED);
            ordersPanel.add(timerLabel);

            ordersPanel.revalidate();
            ordersPanel.repaint();
        });
    }

    private void orderCompleted(Integer orderNumber) {
        SwingUtilities.invokeLater(() -> {
            ordersPanel.removeAll();
            JLabel completedLabel = new JLabel("Order #" + orderNumber + " is ready!");
            completedLabel.setFont(font);
            ordersPanel.add(completedLabel);
            ordersPanel.revalidate();
            ordersPanel.repaint();

            // After 2 seconds, update the display
            Timer timer = new Timer(2000, e -> {
                synchronized (waiter) {
                    waiter.orderCompleted(orderNumber);
                    waiter.notify();

                    ordersPanel.removeAll();
                    if (orderQueue.isEmpty()) {
                        JLabel noOrderLabel = new JLabel("No order in preparation");
                        noOrderLabel.setFont(font);
                        ordersPanel.add(noOrderLabel);
                    } else {
                        // Process next order if available
                        currentOrder = orderQueue.keys().nextElement();
                        ArrayList<OrderItem> nextItems = orderQueue.get(currentOrder);
                        receiveOrder(currentOrder, nextItems);
                    }
                    ordersPanel.revalidate();
                    ordersPanel.repaint();
                    ((Timer) e.getSource()).stop();
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                waitForOrder();
                if (currentOrder != null) {
                    prepareOrder(currentOrder, orderQueue.get(currentOrder));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}