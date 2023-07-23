import gameboard.LeaderBoard;
import gameboard.WinnerInput;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.Border;

import gameboard.game;

public class MinesweeperGame extends JFrame {
    private JPanel gamePanel, controlPanel;
    private JButton startButton, leaderBoardButton, exitButton;
    private JLabel timerLabel,background,txtTimer;
    private JButton [][] jbts;
    private int secondsLeft;
    private Timer timer;

    private game Game;
    private int mineCount = 7;
    private int result = 0;

    public MinesweeperGame() {
        this.setTitle("Mine Sweeper Game");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(null);

        Game = new game(6, 6, mineCount);

        //  Design Gridlayout
        gamePanel = new JPanel();
        gamePanel.setBackground(new Color(0, 0, 0, 80));
        gamePanel.setBounds(20, 130, 300, 300);
        GridLayout grid = new GridLayout(6, 6);
        gamePanel.setLayout(grid);
        gamePanel.setEnabled(false);

        gamePanel.setSize(300, 300);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        gamePanel.setBorder(blackline);
        this.add(gamePanel);

        //Tạo button cho gridlayout
        jbts = new JButton[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                this.jbts[i][j] = new JButton();
                this.jbts[i][j].setName("btn_" + i + "_" + j);
                this.jbts[i][j].setText("");
                this.jbts[i][j].setPreferredSize(new Dimension(10, 10));
                gamePanel.add(this.jbts[i][j]);
                jbts[i][j].setEnabled(false);
            }
        }

        //Thời gian
        timerLabel = new JLabel("", JLabel.CENTER);
        timerLabel.setBounds(120, 71, 100, 50);
        timerLabel.setForeground(Color.RED);
        timerLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        timerLabel.setBorder(blackline);
        timerLabel.setBackground(Color.white);
        timerLabel.setOpaque(true);
        this.add(timerLabel);
        timerLabel.setVisible(false);

        txtTimer = new JLabel("SCORE", JLabel.CENTER);
        txtTimer.setBounds(120, 30, 100, 50);
        txtTimer.setForeground(Color.BLUE);
        txtTimer.setFont(new Font("Tahoma", Font.BOLD, 16));
        this.add(txtTimer);
        txtTimer.setVisible(false);

        // Design các button Bắt đầu, bảng xếp hạng, thoát,...
        //Panel chứa các nút
        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBackground(new Color(0, 0, 0, 80));
        controlPanel.setBounds(400, 0, 200, 500);

        //Button bắt đầu
        startButton = new JButton("Start");
        startButton.setBounds(30, 50, 130, 30);
        startButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        controlPanel.add(startButton);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtTimer.setVisible(true);
                timerLabel.setVisible(true);
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 6; j++) {
                        jbts[i][j].setEnabled(true);

                        jbts[i][j].addMouseListener(new MouseListener() {

                            @Override
                            public void mouseClicked(MouseEvent e) {
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                            }

                            @Override
                            public void mousePressed(MouseEvent e) {
                                if (result == 0) {
                                    if (e.getButton() == 1) {
                                        JButton btn = (JButton) e.getSource();
                                        if(btn.getText() == "!")
                                            btn.setText("");
                                        else {
                                            int[] pos = getButtonPosition((JButton) e.getSource());
                                            result = Game.revealWithResult(pos[0], pos[1]);

                                            for (int i = 0; i < 6; i++) {
                                                for (int j = 0; j < 6; j++) {
                                                    int a = Game.getMinesNumberForPos(i, j);

                                                    if (a == -1) {
                                                        jbts[i][j].setForeground(Color.RED);
                                                        jbts[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                                                        jbts[i][j].setText("X");
                                                        jbts[i][j].setOpaque(true);
                                                        jbts[i][j].setBackground(Color.LIGHT_GRAY);
                                                    } else if (a == 0) {
                                                        jbts[i][j].setEnabled(false);
                                                    } else if (a > 0) {
                                                        jbts[i][j].setForeground(Color.BLACK);
                                                        jbts[i][j].setFont(new Font("Arial", Font.BOLD, 25));
                                                        jbts[i][j].setText("" + a);
                                                        jbts[i][j].setOpaque(true);
                                                        jbts[i][j].setBackground(Color.WHITE);
                                                    }
                                                }
                                            }
                                        }
                                    } else if (e.getButton() == 3) {
                                        JButton btn = (JButton) e.getSource();

                                        if (btn.getText().equals("")) {
                                            btn.setForeground(Color.decode("#7a1b1b"));
                                            btn.setFont(new Font("Arial", Font.BOLD, 35));
                                            btn.setText("!");
                                        } else if (btn.getText().equals("!"))
                                            btn.setText("");
                                    }

                                    resultDialog();
                                }
                            }

                            @Override
                            public void mouseReleased(MouseEvent e) {
                            }
                        });
                    }
                }

                CountdownTimer(120, timerLabel);
                startButton.setEnabled(false);
            }
        });

        //Button BXH
        leaderBoardButton = new JButton("Leaderboard");
        leaderBoardButton.setBounds(30, 110, 130, 30);
        leaderBoardButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        controlPanel.add(leaderBoardButton);
        leaderBoardButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new LeaderBoard().setVisible(true);
            }
        });

        //Button thoát
        exitButton = new JButton("Exit");
        exitButton.setBounds(30, 170, 130, 30);
        exitButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        controlPanel.add(exitButton);
        exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.add(controlPanel);

        //Để background ở cuối
        ImageIcon bg = new ImageIcon("Background.jpg");
        background = new JLabel("", bg, JLabel.CENTER);
        background.setBounds(0, 0, 600, 500);
        Image img = bg.getImage();
        Image temp_img = img.getScaledInstance(600, 500, Image.SCALE_SMOOTH);
        bg = new ImageIcon(temp_img);

        this.add(background);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
    private void CountdownTimer(int seconds, JLabel label)
    {
        this.secondsLeft = seconds;
        this.timerLabel = label;
        this.timerLabel.setText("" + this.secondsLeft);
        
        if(this.timer != null)
            this.timer.stop();
        
        this.timer = new Timer(1000, e -> {
            if (this.secondsLeft > 0 && result == 0) {
                this.secondsLeft--;
                this.timerLabel.setText("" + this.secondsLeft);
                
                if(this.secondsLeft == 0)
                {
                    this.timer.stop();
                    result = -1;
                    resultDialog();
                }
            } else {
                this.timer.stop();
            }
        });

        this.timer.start();
    }

    private int[] getButtonPosition(JButton btn) {
        String[] a = btn.getName().split("_");
        return new int[] { Integer.parseInt(a[1]), Integer.parseInt(a[2]) };
    }

    private void resultDialog() {
        if (result != 0) {
            if (result == 1){
                JOptionPane.showMessageDialog(this, "Win !");
                
                new WinnerInput(secondsLeft).setVisible(true);
            }
            else if (result == -1)
                JOptionPane.showMessageDialog(this, "Lose !");
            
            clearMenu();
        }
    }

    private void clearMenu() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                gamePanel.remove(jbts[i][j]);
                jbts[i][j] = new JButton();
                jbts[i][j].setName("btn_" + i + "_" + j);
                jbts[i][j].setText("");
                jbts[i][j].setPreferredSize(new Dimension(10, 10));
                gamePanel.add(jbts[i][j]);
                jbts[i][j].setEnabled(false);
            }
        }

        timerLabel.setVisible(false);
        txtTimer.setVisible(false);

        startButton.setEnabled(true);

        result = 0;

        Game = new game(6, 6, mineCount);
    }
    
    public static void main(String[] args) {
        MinesweeperGame mineSweeperGUI = new MinesweeperGame();
    }
}