package org.example.customs;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = new ImageIcon("resource\\images\\background.png").getImage();
        Image icon = new ImageIcon("resource\\images\\icon.png").getImage();
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        g.drawImage(icon, 275, 330, this);
    }
}