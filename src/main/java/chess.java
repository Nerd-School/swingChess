import javax.swing.*;
import java.awt.*;
//
//public enum piece {
//    Pawn(1),
//    Knight(2),
//    Bishop(3),
//    Rook(4),
//    Queen(5),
//    King(6);
//
//    private final int type;
//
//    private piece(int type) {
//        this.type = type;
//    }
//
//    public int getType() {
//        return this.type;
//    }
//
//}


public class chess {

    final int pawn = 1;
    final int whiteKnight = 2;
    final int whiteBishop = 3;
    final int whiteRook = 4;
    final int whiteQueen = 5;
    final int whiteKing = 0;
    final int blackPawn = 6;
    final int blackKnight = 7;
    final int blackBishop = 8;
    final int blackRook = 9;
    final int blackQueen = 10;
    final int blackKing = 10;

    final static int ROW_COUNT = 8;
    final static int COLUMN_COUNT = 8;

    private int[][] board = new int[8][8];

    public final String ANSI_Reset = "\u001B[0m";
    public final String ANSI_Red = "\u001B[31m";// 8 rows by 8 columns




    public static void main(String[] args) {
        JFrame frame = new JFrame();



        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);



        frame.setLayout(null);

//        frame.setVisible(true);

        Rectangle size = frame.getBounds();

        System.out.println("Width " + size.width);
        System.out.println("Height " + size.height);

        int spaceSize;
        if (size.width<size.height) {
            spaceSize = (size.width/8);
        } else {
            spaceSize = (size.height/8);
            System.out.println(spaceSize);
        }

        chess game = new chess();

        game.resetBoard();
        game.printBoard();
    }

    public void printBoard() {
        for (int i=0; i<ROW_COUNT; i++) {
            for (int k=0; k<COLUMN_COUNT; k++) {
                System.out.print("[" + board[i][k] + "]");
            }
            System.out.println();
        }
    }

    public void resetBoard() {
        // sets every value to 0
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int k = 0; k < COLUMN_COUNT; k++) {
                board[i][k] = 0;
            }
        }

        // Place the pawns
        // represents the rows
        for (int i = 1; i<=6; i+=5) {
            // represents the columns
            for (int k=0; k<8 /* pawns on row 2 and 7 */; k++) {
                //places the pawn

                board[i][k] = i;
            }
        }



        // Place all the pieces on the 1 and 8 row
        for (int row = 0; row<=7; row+=7) {
            for (int col = 0; col<8; col++) {
                if (col == 0 || col == 7) {
                    board[row][col] = whiteRook;
                } else if (col == 1 || col == 6) {
                    board[row][col] = whiteKnight;
                } else if (col == 2 || col == 5) {
                    board[row][col] = whiteBishop;
                } else if (col == 3) {
                    board[row][col] = whiteQueen;
                } else {
                    board[row][col] = whiteKing;
                }
            }
        }
    }

}
