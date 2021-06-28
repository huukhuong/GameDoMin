package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameBoard extends JFrame {

    private final int MAX = 17;
    private final int MINES_COUNT = 40;

    public GameBoard() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
        initMines();
        addControls();
        showWindow();
    }

    private int matrix[][];
    private JButton minesMatrix[][];
    private int opened[][];

    private void initMines() {
        matrix = new int[MAX][MAX];
        opened = new int[MAX][MAX];

        Random rd = new Random();

        int count = 0;

        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if (count == MINES_COUNT) {
                    break;
                }
                matrix[i][j] = 9;
                count++;
            }
        }

        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                int ird = rd.nextInt(MAX);
                int jrd = rd.nextInt(MAX);
                int temp = matrix[i][j];
                matrix[i][j] = matrix[ird][jrd];
                matrix[ird][jrd] = temp;
            }
        }

        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if (matrix[i][j] != 9)
                    matrix[i][j] = mi_count(i, j);
            }
        }

//        for (int i = 0; i < MAX; i++) {
//            for (int j = 0; j < MAX; j++)
//                System.out.print(matrix[i][j] + "  ");
//            System.out.println();
//        }

    }

    int mi_count(int row, int col) {
        int near_minum = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (row + i >= 0 && row + i < MAX && col + j >= 0 && col + j < MAX) {
                    if (matrix[row + i][col + j] == 9)
                        near_minum += 1;
                }
            }
        }

        return near_minum;
    }

    private JPanel pnMain;
    private Timer timer;

    private void addControls() {
        Container con = getContentPane();
        con.setLayout(new BoxLayout(con, BoxLayout.Y_AXIS));

        JPanel pnTime = new JPanel();
        pnTime.add(new JLabel("Thời gian: "));
        JLabel lblTime = new JLabel("0");

        pnTime.add(lblTime);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblTime.setText((Integer.parseInt(lblTime.getText()) + 1) + "");
            }
        });
        timer.start();

        pnMain = new JPanel();
        pnMain.setLayout(new GridLayout(MAX, MAX));

        minesMatrix = new MyButton[MAX][MAX];

        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                minesMatrix[i][j] = new MyButton();
                buttonClick(i, j);
                pnMain.add(minesMatrix[i][j]);
            }
        }

        con.add(pnTime);
        con.add(pnMain);

        pack();

    }

    private void buttonClick(int i, int j) {
        minesMatrix[i][j].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (MouseEvent.BUTTON3 == e.getButton()) {
                    check(i, j, true);
                } else {
                    check(i, j, false);
                }
                pnMain.repaint();
            }
        });
    }

    private final ImageIcon NUM_1 = new ImageIcon("img/mines_1.png");
    private final ImageIcon NUM_2 = new ImageIcon("img/mines_2.png");
    private final ImageIcon NUM_3 = new ImageIcon("img/mines_3.png");
    private final ImageIcon NUM_4 = new ImageIcon("img/mines_4.png");
    private final ImageIcon NUM_9 = new ImageIcon("img/mines_bomb.png");
    private final ImageIcon FLAG = new ImageIcon("img/mines_flag.png");
    private final ImageIcon DEFAULT = new ImageIcon("");

    private void check(int i, int j, boolean isRightClick) {
        int value = matrix[i][j];

        if (isRightClick) {
            opened[i][j] = 5;
            minesMatrix[i][j].setIcon(FLAG);
            return;
        }

        if (opened[i][j] == 5) {
            opened[i][j] = 0;
            minesMatrix[i][j].setIcon(DEFAULT);
            return;
        }

        minesMatrix[i][j].setBackground(Color.white);

        switch (value) {
            case 1:
                minesMatrix[i][j].setIcon(NUM_1);
                break;
            case 2:
                minesMatrix[i][j].setIcon(NUM_2);
                break;
            case 3:
                minesMatrix[i][j].setIcon(NUM_3);
                break;
            case 4:
                minesMatrix[i][j].setIcon(NUM_4);
                break;
            case 9:
                timer.stop();
                showAllMines();
                minesMatrix[i][j].setBackground(Color.RED);
                minesMatrix[i][j].setIcon(NUM_9);

                int x = JOptionPane.showConfirmDialog(null,
                        "Bạn đã thua! Chơi lại?",
                        "Game over",
                        JOptionPane.OK_CANCEL_OPTION);
                if (x == JOptionPane.OK_OPTION) {
                    new GameBoard();
                    this.dispose();
                } else {
                    System.exit(0);
                }
                break;
        }

        opened[i][j] = 1;

        if (value == 0) {
            for (int t = -1; t < 2; t++) {
                for (int k = -1; k < 2; k++) {
                    if (t != 0 || k != 0) {
                        if (i + t >= 0 && i + t < MAX && j + k >= 0 && j + k < MAX) {
                            if (opened[i + t][j + k] == 0) {
                                check(i + t, j + k, false);
                            }
                        }
                    }
                }
            }
        }

        int count = 0;
        for (int t = 0; t < MAX; t++) {
            for (int k = 0; k < MAX; k++) {
                if (opened[t][k] == 1)
                    count++;
            }
        }
        System.out.println(count);
        if (count == 249) {
            int x = JOptionPane.showConfirmDialog(null,
                    "Bạn đã thắng! Chơi lại?",
                    "Game over",
                    JOptionPane.OK_CANCEL_OPTION);
            if (x == JOptionPane.OK_OPTION) {
                new GameBoard();
                this.dispose();
            }
        }
    }

    private void showAllMines() {
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                if (matrix[i][j] == 9) {
                    minesMatrix[i][j].setIcon(NUM_9);
                    minesMatrix[i][j].setBackground(Color.white);
                }
            }
        }
    }


    private void showWindow() {
        this.setTitle("Dò mìn");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

}
