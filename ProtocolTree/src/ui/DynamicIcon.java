package ui;

import javax.swing.*;
import java.awt.*;

class DynamicIcon implements Icon {
    private Dimension iconDimension = new Dimension(20, 10);
    String iconLetter = "x";

    public DynamicIcon(String s, Dimension dim) {
        iconDimension = dim;
    }

    @Override
	public int getIconWidth() {
        return iconDimension.width;
    }

    @Override
	public int getIconHeight() {
        return iconDimension.height;
    }

    @Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
        g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
        g.drawString(iconLetter, 0, iconDimension.height / 2);
    }
}