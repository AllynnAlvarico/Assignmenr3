package allynn.alvarico.customs;

import javax.swing.*;
import java.awt.*;

public class CartCustomButtons extends CustomButton {

    public CartCustomButtons(String text, String hexMaintone, String hexHovertone, String hexClicktone){
        super(text, hexMaintone, hexHovertone, hexClicktone);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
