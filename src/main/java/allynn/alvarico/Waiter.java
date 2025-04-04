package allynn.alvarico;

import allynn.alvarico.gui.GraphicUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

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
    private GraphicUtilities gUtils;

    public Waiter(Font font, int x, int y) {
        this.font = font;
        this.orderQueue = new ConcurrentHashMap<>();
        this.newOrder = false;
        gUtils = new GraphicUtilities(font, WIDTH, HEIGHT);
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
        gUtils.setComponentUI(headerLabel, font.deriveFont(24f), Color.RED, true);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ordersPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        ordersPanel.setBackground(Color.WHITE);
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        JLabel preparingHeader = new JLabel("Preparing", SwingConstants.CENTER);
        gUtils.setComponentUI(preparingHeader, font, Color.LIGHT_GRAY, true);
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
        gUtils.setComponentUI(collectionHeader, font, Color.DARK_GRAY, true);
        collectionHeader.setForeground(Color.WHITE);
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
        this.notify(); // this will wake the waiter class

        updateOrdersDisplay();

//        if (chef.isAlive()){
//            System.out.println("kitchen is operating");
//            chef.receiveOrder(orderNumber, items);
//        }


        Timer orderDisplayTimer = new Timer(1000, evt -> {
            updateOrdersDisplay();
            Timer chefTimer = new Timer(1000, evt2 -> {
                chef.receiveOrder(orderNumber, items);
                ((Timer) evt2.getSource()).stop();
            });
            chefTimer.setRepeats(false);
            chefTimer.start();

            ((Timer) evt.getSource()).stop();
        });
        orderDisplayTimer.setRepeats(false);
        orderDisplayTimer.start();
    }

    public void orderCompleted(Integer orderNumber) {
        SwingUtilities.invokeLater(() -> {
            JLabel collectLabel = new JLabel("Order #" + orderNumber);
            gUtils.setComponentUI(collectLabel, font, new Color(220, 255, 220), true);
            collectLabel.setHorizontalAlignment(SwingConstants.CENTER);
            collectLabel.setBorder(new LineBorder(new Color(0, 200, 0), 2));

            orderQueue.remove(orderNumber);
            updateOrdersDisplay();

            gUtils.updatePreparringWindow(preparingPanel, orderNumber);

            collectionPanel.add(collectLabel);
            Component spacer = Box.createRigidArea(new Dimension(0, 5));
            collectionPanel.add(spacer);
            gUtils.refreshComponent(collectionPanel);

            Timer removalTimer = new Timer(5000, e -> {
                collectionPanel.remove(collectLabel);
                collectionPanel.remove(spacer);
                gUtils.refreshComponent(collectionPanel);
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
                    gUtils.setComponentUI(orderLabel, font, Color.WHITE, true);
                    orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    orderLabel.setBorder(new LineBorder(new Color(255, 198, 0), 2));
                    preparingPanel.add(orderLabel);
                    preparingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                });
            }
            gUtils.refreshComponent(preparingPanel);
        });
    }

    public synchronized void waitForOrder() throws InterruptedException {
        while (!newOrder) {
            this.wait();
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