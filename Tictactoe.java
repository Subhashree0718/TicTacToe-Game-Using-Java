import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TicTacToe extends JFrame implements ActionListener {
    private static final int BOARD_SIZE = 3;
    private JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private boolean isPlayer1Turn = true;
    private int[][] winLine = null;

    public TicTacToe() {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException("No graphical environment available");
        }
        setTitle("Tic Tac Toe");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        BoardPanel boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);
        initializeBoard(boardPanel);
    }

    private void initializeBoard(BoardPanel boardPanel) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].addActionListener(this);
                boardPanel.add(buttons[row][col]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        if (buttonClicked.getText().equals("")) {
            buttonClicked.setText(isPlayer1Turn ? "X" : "O");
            isPlayer1Turn = !isPlayer1Turn;
            winLine = checkWinner();
            if (winLine != null) {
                JOptionPane.showMessageDialog(this, "Player " + (isPlayer1Turn ? "2 (O)" : "1 (X)") + " wins!");
                ((BoardPanel) getContentPane().getComponent(0)).setWinLine(winLine);
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "The game is a draw!");
                resetBoard();
            }
        }
    }

    private int[][] checkWinner() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                !buttons[i][0].getText().equals("")) {
                return new int[][]{{i, 0}, {i, 2}};
            }
            if (buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                buttons[1][i].getText().equals(buttons[2][i].getText()) &&
                !buttons[0][i].getText().equals("")) {
                return new int[][]{{0, i}, {2, i}};
            }
        }
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][2].getText()) &&
            !buttons[0][0].getText().equals("")) {
            return new int[][]{{0, 0}, {2, 2}};
        }
        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][0].getText()) &&
            !buttons[0][2].getText().equals("")) {
            return new int[][]{{0, 2}, {2, 0}};
        }
        return null;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (buttons[row][col].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col].setText("");
            }
        }
        winLine = null;
        isPlayer1Turn = true;
        repaint();
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("No graphical environment available");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            TicTacToe game = new TicTacToe();
            game.setVisible(true);
        });
    }

    class BoardPanel extends JPanel {
        private int[][] winLine;

        public BoardPanel() {
            setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        }

        public void setWinLine(int[][] winLine) {
            this.winLine = winLine;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (winLine != null) {
                g.setColor(Color.RED);
                int startX = winLine[0][1] * getWidth() / BOARD_SIZE + getWidth() / (2 * BOARD_SIZE);
                int startY = winLine[0][0] * getHeight() / BOARD_SIZE + getHeight() / (2 * BOARD_SIZE);
                int endX = winLine[1][1] * getWidth() / BOARD_SIZE + getWidth() / (2 * BOARD_SIZE);
                int endY = winLine[1][0] * getHeight() / BOARD_SIZE + getHeight() / (2 * BOARD_SIZE);
                g.drawLine(startX, startY, endX, endY);
            }
        }
    }
}

