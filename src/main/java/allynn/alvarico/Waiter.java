package allynn.alvarico;

import allynn.alvarico.gui.GraphicUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Waiter extends Thread {
    private JFrame frame;
    private JPanel ordersPanel, preparingPanel, collectionPanel;
    private final int WIDTH = 400, HEIGHT = 600;
    private final Font font;
    private ConcurrentHashMap<Integer, ArrayList<OrderItem>> orderQueue;
    private boolean newOrder;
    private Chef chef;
    private GraphicUtilities gUtils;
    private SimulationClock clock;

    public Waiter(Font font, int x, int y, String iconDirectory) {
        this.font = font;
        this.orderQueue = new ConcurrentHashMap<>();
        this.newOrder = false;
        gUtils = new GraphicUtilities(font, WIDTH, HEIGHT);
        clock = new SimulationClock();
        initializeGUI(x, y, iconDirectory);
    }
    public void setChef(Chef chef) {
        this.chef = chef;
    }

    private void initializeGUI(int x, int y, String iconDirectory) {
        frame = new JFrame("Order Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(x, y);
        frame.setResizable(false);

        frame.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(iconDirectory));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Current Orders", SwingConstants.CENTER);
        gUtils.setComponentUI(headerLabel, font.deriveFont(24f), Color.RED, Color.WHITE, true);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        ordersPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        ordersPanel.setBackground(Color.WHITE);
        ordersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        JLabel preparingHeader = new JLabel("Preparing", SwingConstants.CENTER);
        gUtils.setComponentUI(preparingHeader, font, Color.LIGHT_GRAY, Color.WHITE,true);
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
        gUtils.setComponentUI(collectionHeader, font, Color.DARK_GRAY, Color.WHITE,true);
        collectionHeader.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        collectionPanel = new JPanel();
        collectionPanel.setLayout(new BoxLayout(collectionPanel, BoxLayout.Y_AXIS));
        collectionPanel.setBackground(Color.WHITE);
        collectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        rightPanel.add(collectionHeader, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(collectionPanel), BorderLayout.CENTER);

        gUtils.addComponent(ordersPanel, leftPanel, rightPanel);

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(ordersPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public synchronized void addOrder(Integer orderNumber, ArrayList<OrderItem> items) {
        orderQueue.put(orderNumber, items);
        newOrder = true;
        // this will wake the waiter class up
        // so it can process the order
        // this is a signal to the waiter that a new order has been added
        this.notify();

        // this will delay the display of the order by 5 seconds
        Timer displayDelay = new Timer(SimulationClock.simWaiterDelay, e -> {
            updateOrdersDisplay();
            orderDisplayTimer(orderNumber, items);
            ((Timer) e.getSource()).stop();

        });
        displayDelay.setRepeats(false);
        displayDelay.start();
    }

    private void terminalLog(){
        System.out.printf("========================== %s ==========================", this.getName());
        System.out.println("Order received from kiosk");
        System.out.println("Passing it to the chef class");
    }

    private void orderDisplayTimer(Integer orderNumber, ArrayList<OrderItem> items) {
        Timer orderDisplayTimer = new Timer(SimulationClock.orderDisplay, evt -> {
            updateOrdersDisplay();
            Timer chefTimer = new Timer(SimulationClock.chefsTimer, evt2 -> {
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
            gUtils.setComponentUI(collectLabel, font, new Color(220, 255, 220), Color.BLACK,true);
            collectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            collectLabel.setHorizontalAlignment(SwingConstants.CENTER);
            collectLabel.setBorder(new LineBorder(new Color(0, 200, 0), 2));

            orderQueue.remove(orderNumber);
            updateOrdersDisplay();

            gUtils.updatePreparringWindow(preparingPanel, orderNumber);

            collectionPanel.add(collectLabel);
            Component spacer = Box.createRigidArea(new Dimension(0, 5));
            collectionPanel.add(spacer);
            gUtils.refreshComponent(collectionPanel);

            Timer removalTimer = new Timer(SimulationClock.removalTimer, e -> {
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
                orderQueue.forEach((orderNum, items) -> {
                    JLabel orderLabel = new JLabel(String.format("Order #%03d", orderNum));
                    gUtils.setComponentUI(orderLabel, font, Color.WHITE, Color.BLACK, true);
                    orderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    orderLabel.setBorder(new LineBorder(new Color(255, 198, 0), 2));
                    gUtils.addComponent(preparingPanel, orderLabel, Box.createRigidArea(new Dimension(0, 5)));
                });
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
        // the thread will run until interrupted
        // it will wait for an order to be added and then process it
        // it will sleep for 5 seconds
        // to simulate the time it takes to process the order
        while (!Thread.interrupted()) {
            try {
                waitForOrder();
                clock.sleep(SimulationClock.simWaiterDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}