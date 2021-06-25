package Gui;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;

public class MyButton extends JButton {

    private final int SIZE = 30;

    public MyButton() {
        super();
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        this.setFocusPainted(false);
        this.setBackground(new Color(205, 205, 205));
    }

}
