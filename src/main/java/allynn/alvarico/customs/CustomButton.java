package allynn.alvarico.customs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    private final Color normalColor;
    private final Color hoverColor;
    private final Color clickColor;

    public CustomButton(String text, String hexMaintone, String hexHovertone, String hexClicktone) {
        super(text);
        normalColor = Color.decode(hexMaintone);
        hoverColor = Color.decode(hexHovertone);
        clickColor = Color.decode(hexClicktone);

        setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        setForeground(Color.WHITE);
        setBackground(normalColor);

        setFocusable(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // Rounded corners

        super.paintComponent(g);
    }
}

