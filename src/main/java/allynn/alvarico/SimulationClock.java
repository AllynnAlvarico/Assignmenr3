package allynn.alvarico;

public class SimulationClock {
    /**
     * orderDisplay - time to display the order on the screen
     * -
     */
    public static int paymentDelay = seconds(3);
    public static int simWaiterDelay = seconds(5);

    public static int orderDisplay = seconds(3);
    public static int chefsTimer = seconds(1);
    public static int chefsDelayTimer = seconds(5);
    public static int removalTimer = seconds(5);

    public static int seconds(int seconds) {
        return seconds * 1000;
    }

    public void sleep(int seconds) {
        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

}
