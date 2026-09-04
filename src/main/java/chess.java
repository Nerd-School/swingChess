import javax.swing.*;
import java.awt.*;
import java.util.Scanner;


public class chess {

    Scanner scanner = new Scanner(System.in);

    // true means white turn false means blacks turn
    boolean whiteTurn = true;

    boolean whiteKingInCheck = false;
    boolean blackKingInCheck = false;

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




    static void main(String[] args) {
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
        boolean parentSuccess = false;
        do {
            System.out.println("What piece would you like to move? ");
            String movingPiece = scanner.next();

            boolean success = false;

            do {
                if (movingPiece.length() == 2) {
                    success = true;
                }
            } while (!success);

            int[] validTestBoth = {0,0};
            int validTestRow = 0;
            int validTestColumn = 0;

            int pieceType = 0;

            success = false;
            // checks once and more times if needed
            do {
                validTestBoth = convertToNumber(movingPiece);
                validTestRow = validTestBoth[0];
                validTestColumn = validTestBoth[1];
                // try checks if the position is inside the bounds of the board
                try {
                    // if checks the content of the position (0 is blank and above 12 is invalid)
                    if (board[validTestRow][validTestColumn] == 0 || board[validTestRow][validTestColumn] > 12) {
                        System.out.println("Invalid location! Try again!");
                        movingPiece = scanner.next();
                    } else {
                        pieceType = board[validTestRow][validTestColumn];
                    }
                    // the try failed so the location is outside the bounds of the board
                } catch (Exception e) {
                    System.out.println("Invalid location! Try again!");
                    movingPiece = scanner.next();
                }
                success = true;
                if (!whiteTurn && pieceType > 0 && pieceType < 6 || !whiteTurn && pieceType == 12) {
                    System.out.println("It is black's move. Please enter the location of a black piece");
                    success = false;
                    movingPiece = scanner.next();
                } else if (whiteTurn && pieceType > 5 && pieceType < 12) {
                    System.out.println("It is white's move. Please enter the location of a white piece");
                    success = false;
                    movingPiece = scanner.next();

                }

            } while (!success);

            pieceType = board[validTestRow][validTestColumn];

            System.out.println("Where would you like to move that piece? ");
            String moveToLocation = scanner.next();
            int[] moveToLocationArray = convertToNumber(moveToLocation);
            int moveToLocationRow = moveToLocationArray[0];
            int moveToLocationColumn = moveToLocationArray[1];

            success = false;

            do {
                if (moveToLocation.length() == 2) {
                    //if (board[moveToLocationRow][moveToLocationColumn] == 0) {
                    success = true;
                    //}
//                    else {
//                        System.out.println("This location is taken! Try again!");
//                        moveToLocation = scanner.next();
//                        moveToLocationArray = convertToNumber(moveToLocation);
//                        moveToLocationRow = moveToLocationArray[0];
//                        moveToLocationColumn = moveToLocationArray[1];
//                    }
                } else {
                    System.out.println("Invalid location! Try again!");
                }
            } while (!success);


            if (isMoveLegal(movingPiece, moveToLocation)) {
                board[moveToLocationRow][moveToLocationColumn] = board[validTestRow][validTestColumn];
                board[validTestRow][validTestColumn] = 0;
                whiteTurn = !whiteTurn;
            } else {
                System.out.println("Illegal move! Try again!");
            }

            printBoard();
        } while (!parentSuccess);
    }

    public boolean isMoveLegal(String start, String end) {
        int[] startLocation = convertToNumber(start);
        int[] endLocation = convertToNumber(end);
        int pieceType;
        int endPieceType;
        try {
            pieceType = board[startLocation[0]][startLocation[1]];
            endPieceType = board[endLocation[0]][endLocation[1]];
        } catch(Exception e) {
            return false;
        }
        // checks if you are capturing your own piece
        if (whiteTurn && endPieceType > 0 && endPieceType < 6 && endPieceType != 12 || !whiteTurn && endPieceType > 5 && endPieceType < 12) {
            return false;
        }

        // tells me what piece type (will be changed to more important stuff later)
        if (pieceType == whitePawn) {
            if (endPieceType == 0 && startLocation[1] == endLocation[1]) {
                if (startLocation[0] - 1 == endLocation[0]) {
                    return true;
                } else if (startLocation[0] - 2 == endLocation[0] && startLocation[0] == 6) {
                    return true;
                } else {
                    return false;
                }
            } else if (startLocation[0] - 1 == endLocation[0] && Math.abs(startLocation[1] - endLocation[1]) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (pieceType == blackPawn) {
            if (endPieceType == 0 && startLocation[1] == endLocation[1]) {
                if (startLocation[0] + 1 == endLocation[0]) {
                    return true;
                } else if (startLocation[0] + 2 == endLocation[0] && startLocation[0] == 1) {
                    return true;
                } else {
                    return false;
                }
            } else if (startLocation[0] + 1 == endLocation[0] && Math.abs(startLocation[1] - endLocation[1]) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (pieceType == whiteKnight || pieceType == blackKnight) {
            // knight logic
            if (Math.abs(startLocation[0] - endLocation[0]) == 1 && Math.abs(startLocation[1] - endLocation[1]) == 2 || Math.abs(startLocation[0] - endLocation[0]) == 2 && Math.abs(startLocation[1] - endLocation[1]) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (pieceType == whiteBishop || pieceType == blackBishop) {
            int rowMultiplier;
            int columnMultiplier;

            if (Math.abs(startLocation[0] - endLocation[0]) != Math.abs(startLocation[1] - endLocation[1])) {
                return false;
            }

            if (startLocation[0] < endLocation[0]) {
                rowMultiplier = 1;
            } else {
                rowMultiplier = -1;
            }

            if (startLocation[1] < endLocation[1]) {
                columnMultiplier = 1;
            } else {
                columnMultiplier = -1;
            }

            for (int i = 1; i < Math.abs(startLocation[0] - endLocation[0]); i++) {
                if ((board[startLocation[0] + (i*rowMultiplier)][startLocation[1]+(i*columnMultiplier)] != 0)) {
                    return false;
                }
            }
        } else if (pieceType == whiteRook || pieceType == blackRook) {
            // rook logic
            if (startLocation[0] == endLocation[0]) {
                // the rook is moving horizontally staying on the same row

                // rook moving positive (to the right)
                if (startLocation[1] < endLocation[1]) {
                    for (int i = startLocation[1]+1;  i < endLocation[1]; i++) {
                        if (board[startLocation[0]][i] != 0) {
                            return false;
                        }
                    }
                } else if (startLocation[1] > endLocation[1]) {
                    for (int i = startLocation[1]-1; i > endLocation[1]; i--) {
                        if (board[startLocation[0]][i] != 0) {
                            return false;
                        }
                    }
                }
            } else if ( startLocation[1] == endLocation[1]) {
                // the rook is moving vertically staying on the same column

                // moving upward (toward the black side)
                if (startLocation[0] < endLocation[0]) {
                    for (int i = startLocation[0]+1;  i < endLocation[0]; i++) {
                        if (board[i][startLocation[1]] != 0) {
                            return false;
                        }
                    }
                } else if (startLocation[0] > endLocation[0]) {
                    for (int i = startLocation[0]-1;  i > endLocation[0]; i--) {
                        if (board[i][startLocation[1]] != 0) {
                            return false;
                        }
                    }
                }
            } else {
                // the rook can only move straight up or down so if the starting and ending row or column doesn't match it isn't moving straight
                return false;
            }
        } else if (pieceType == whiteQueen || pieceType == blackQueen) {
            System.out.println("It's a queen");
        } else if (pieceType == blackKing || pieceType == whiteKing) {
            System.out.println("It's a King");
        } else {
            // there is no piece here that is in the library of pieces
            return(false);
        }
        return true;
    }
}
