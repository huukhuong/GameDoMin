package Gui;

import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {

    private final int SIZE = 30;

    public MyLabel() {
        this.setPreferredSize(new Dimension(SIZE, SIZE));
    }

    public MyLabel(String title) {
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        this.setText(title);
        this.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    }

}
