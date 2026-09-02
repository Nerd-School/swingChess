import javax.swing.*;
import java.awt.*;
import java.util.Scanner;


public class chess {

    Scanner scanner = new Scanner(System.in);

    final int whitePawn = 1;
    final int whiteKnight = 2;
    final int whiteBishop = 3;
    final int whiteRook = 4;
    final int whiteQueen = 5;
    final int whiteKing = 12;
    final int blackPawn = 6;
    final int blackKnight = 7;
    final int blackBishop = 8;
    final int blackRook = 9;
    final int blackQueen = 10;
    final int blackKing = 11;

    final static int ROW_COUNT = 8;
    final static int COLUMN_COUNT = 8;

    private int[][] board = new int[8][8];

    public final String ANSI_Reset = "\u001B[0m";
    public final String ANSI_Red = "\u001B[31m";
    public final String ANSI_Green = "\u001B[32m";

    // 8 rows by 8 columns




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

        game.movePiece();

        System.exit(0);
    }

    public void printBoard() {
        for (int i=0; i<ROW_COUNT; i++) {
            System.out.print(8-i + " ");
            for (int k=0; k<COLUMN_COUNT; k++) {
                if (board[i][k] > 5 && board[i][k] != 12) {
                    System.out.print(ANSI_Red + "[" + board[i][k] + "]" + ANSI_Reset);
                } else if (board[i][k] > 0 && board[i][k] < 6 || board[i][k] == 12) {
                    System.out.print(ANSI_Green + "[" + board[i][k] + "]" + ANSI_Reset);
                } else {
                    System.out.print(ANSI_Reset + "[" + board[i][k] + "]");
                }
            }
            // next line after 8 pieces placed
            System.out.println();
        }
        System.out.println("   a  b  c  d  e  f  g  h");
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
        // represents the columns
        for (int k=0; k<8 /* pawns on row 2 and 7 */; k++) {
            //places the pawn

            board[6][k] = whitePawn;
        }
        for (int k=0; k<8 /* pawns on row 2 and 7 */; k++) {
            //places the pawn

            board[1][k] = blackPawn;
        }



        // Place all the pieces on the 1 and 8 row
        for (int col = 0; col<8; col++) {
            if (col == 0 || col == 7) {
                board[7][col] = whiteRook;
            } else if (col == 1 || col == 6) {
                board[7][col] = whiteKnight;
            } else if (col == 2 || col == 5) {
                board[7][col] = whiteBishop;
            } else if (col == 3) {
                board[7][col] = whiteQueen;
            } else {
                board[7][col] = whiteKing;
            }
        }

        for (int col = 0; col<8; col++) {
            if (col == 0 || col == 7) {
                board[0][col] = blackRook;
            } else if (col == 1 || col == 6) {
                board[0][col] = blackKnight;
            } else if (col == 2 || col == 5) {
                board[0][col] = blackBishop;
            } else if (col == 3) {
                board[0][col] = blackQueen;
            } else {
                board[0][col] = blackKing;
            }
        }
    }

    public int[] convertToNumber(String position) {
        int row;
        int column;

        column = position.charAt(0) - 'a';
        row = Character.getNumericValue(position.charAt(1));

        row = 8-row;

        int[] pos = {row, column};

        return pos;

    }

    public void movePiece() {
        System.out.println("What piece would you like to move? ");
        String movingPiece = scanner.next();

        int[] validTestBoth = convertToNumber(movingPiece);
        int validTestRow = validTestBoth[0];
        int validTestColumn = validTestBoth[1];

        boolean success = false;
        // checks once and more times if needed
         do {
             // try checks if the position is inside the bounds of the board
             try {
                 // if checks the content of the position (0 is blank and above 12 is invalid)
                 if (board[validTestRow][validTestColumn] == 0 || board[validTestRow][validTestColumn] > 12) {
                     System.out.println("Invalid location! Try again!");
                     movingPiece = scanner.next();
                 }
             // the try failed so the location is outside the bounds of the board
             } catch (Exception e) {
                 System.out.println("Invalid location! Try again!");
                 movingPiece = scanner.next();
             }
             // lastly checks if the move is legal before exiting the do while loop
             if (isMoveLegal(movingPiece, "a1")) {
                 success = true;
             }
         } while (!success);



        System.out.println("Where would you like to move that piece? ");
        String moveLocation = scanner.next();

    }

    public boolean isMoveLegal(String start, String end) {
        int[] startLocation = convertToNumber(start);
        int pieceType;
        try {
            pieceType = board[startLocation[0]][startLocation[1]];
        } catch(Exception e) {
            return false;
        }
        // tells me what piece type (will be changed to more important stuff later)
        if (pieceType == 1 || pieceType == 6) {
            System.out.println("It's a pawn!!");
        } else if (pieceType == 2 || pieceType == 7) {
            System.out.println("It's a knight");
        } else if (pieceType == 3 || pieceType == 8) {
            System.out.println("It's a bishop");
        } else if (pieceType ==4 || pieceType == 9) {
            System.out.println("It's a Rook");
        } else if (pieceType == 5 || pieceType == 10) {
            System.out.println("It's a queen");
        } else if (pieceType == 11 || pieceType == 12) {
            System.out.println("It's a King");
        } else {
            // there is no piece here that is in the library of pieces
            return(false);
        }
        return true;
    }
}
