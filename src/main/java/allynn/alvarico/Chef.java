package allynn.alvarico;

import allynn.alvarico.gui.GraphicUtilities;

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
    private final int WIDTH = 400, HEIGHT = 600;
    private final Font font;
    private ConcurrentHashMap<Integer, ArrayList<OrderItem>> orderQueue;
    private Waiter waiter;
    private boolean isProcessing;
    private Integer currentOrder;
    private GraphicUtilities gUtils;
    private SimulationClock clock;

    public Chef(int x, int y, String iconDirectory) {

        this.font = new Font("Comic Sans MS", Font.BOLD, 18);
        this.orderQueue = new ConcurrentHashMap<>();
        this.isProcessing = false;
        gUtils = new GraphicUtilities(font, WIDTH, HEIGHT);
        clock = new SimulationClock();
        initializeGUI(x, y, iconDirectory);
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    private void initializeGUI(int x, int y, String iconDirectory) {
        frame = new JFrame("Kitchen Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(x, y);
        frame.setResizable(false);
        frame.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(iconDirectory));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Current Order", SwingConstants.CENTER);
        gUtils.setComponentUI(headerLabel, font.deriveFont(24f), new Color(255, 198, 0), Color.WHITE,true);
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
        if (isProcessing || currentOrder != null) {
            orderQueue.put(orderNumber, items);
            return;
        }

        isProcessing = true;
        currentOrder = orderNumber;
        orderQueue.put(orderNumber, items);

            SwingUtilities.invokeLater(() -> {
                ordersPanel.removeAll();
                JLabel preparingLabel = new JLabel("Preparing Order #" + orderNumber);
                preparingLabel.setFont(font);
                ordersPanel.add(preparingLabel);
                gUtils.refreshComponent(ordersPanel);

                Timer initialDelay = new Timer(SimulationClock.seconds(1), e -> {
                    startCountdown(orderNumber, items);
                    ((Timer) e.getSource()).stop();
                });
                initialDelay.setRepeats(false);
                initialDelay.start();
            });
            currentOrder = null;
            synchronized (this) {
                notify();
            }


    }

    private void startCountdown(Integer orderNumber, ArrayList<OrderItem> items) {

        int totalTime = calculatePrepTime(items);
        new Timer(SimulationClock.chefsTimer, new ActionListener() {
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
        }).start();
    }

    private void updateOrdersDisplay() {
        new Timer(SimulationClock.chefsDelayTimer, evt -> {
            SwingUtilities.invokeLater(() -> {
                ordersPanel.removeAll();
                if (currentOrder != null) {
                    JLabel orderLabel = new JLabel(String.format("Preparing Order #%03d", currentOrder));
                    gUtils.setComponentUI(orderLabel, font, Color.WHITE, Color.BLACK,true);

                    orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    orderLabel.setBorder(new LineBorder(new Color(255, 198, 0), 2));

                    ordersPanel.add(orderLabel);
                    ordersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    ordersPanel.add(timerLabel);
                } else {
                    timerLabel.setText("No order in preparation");
                    ordersPanel.add(timerLabel);
                }
                gUtils.refreshComponent(ordersPanel);
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

        clock.sleep(2);
        int prepTime = calculatePrepTime(items);
        System.out.println("Preparing Order #" + orderNumber + " (Prep time: " + prepTime + " seconds)");

        for (int i = prepTime; i > 0; i--) {
            updateTimer(i);
            clock.sleep(1);
        }

        orderQueue.remove(orderNumber);
        updateOrdersDisplay();

        synchronized (waiter) {
            waiter.orderCompleted(orderNumber);
            waiter.notify();
        }

        isProcessing = false;
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

                gUtils.refreshComponent(ordersPanel);
            });
    }

    private void orderCompleted(Integer orderNumber) {
        SwingUtilities.invokeLater(() -> {
            ordersPanel.removeAll();
            JLabel completedLabel = new JLabel("Order #" + orderNumber + " is ready!");
            completedLabel.setFont(font);
            ordersPanel.add(completedLabel);
            gUtils.refreshComponent(ordersPanel);

            Timer timer = new Timer(clock.seconds(2), e -> {
                synchronized (waiter) {
                    waiter.orderCompleted(orderNumber);
                    waiter.notify();
                    orderQueue.remove(orderNumber);

                    ordersPanel.removeAll();
                    isProcessing = false;
                    currentOrder = null;

                    getNextOrder();
                    gUtils.refreshComponent(ordersPanel);
                    ((Timer) e.getSource()).stop();
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void getNextOrder(){
        if (!orderQueue.isEmpty()) {
            Integer nextOrder = orderQueue.keys().nextElement();
            ArrayList<OrderItem> nextItems = orderQueue.get(nextOrder);
            receiveOrder(nextOrder, nextItems);
        } else {
            JLabel noOrderLabel = new JLabel("No order in preparation");
            noOrderLabel.setFont(font);
            ordersPanel.add(noOrderLabel);
        }
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