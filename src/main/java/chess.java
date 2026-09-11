import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

public class chess {

    Scanner scanner = new Scanner(System.in);

    // true means white turn false means blacks turn
    boolean whiteTurn = true;

    boolean whiteKingInCheck = false;
    boolean blackKingInCheck = false;

    String whiteKingPosition = "e1";
    String blackKingPosition = "e8";

    String blackCheckingPiece;
    String whiteCheckingPiece;

    int blackCheckingPieceType;
    int whiteCheckingPieceType;

    int numberOfBlackCheckingPieces;
    int numberOfWhiteCheckingPieces;

    ArrayList<String> pathToBlackCheckingPieces = new ArrayList<>();
    ArrayList<String> pathToWhiteCheckingPieces = new ArrayList<>();

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

    HashMap<Integer, String> numberConversionMap =  new HashMap<>();

    final static int ROW_COUNT = 8;
    final static int COLUMN_COUNT = 8;

    private int[][] board = new int[8][8];

    public final String ANSI_Reset = "\u001B[0m";
    public final String ANSI_Black = "\u001B[90m";
    public final String ANSI_Red = "\u001B[31m";
    public static final String ANSI_Brown = "\u001b[38;2;139;69;19m";
    public static final String ANSI_Tan = "\u001b[38;2;210;180;140m";

    JFrame frame = new JFrame();

    Rectangle size;

    static void main(String[] args) {

        chess game = new chess();

        game.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        game.frame.setLayout(new BorderLayout());

//        game.frame.setVisible(true);

        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.setupScreen();

        game.numberConversionMap.put(0, "a");
        game.numberConversionMap.put(1, "b");
        game.numberConversionMap.put(2, "c");
        game.numberConversionMap.put(3, "d");
        game.numberConversionMap.put(4, "e");
        game.numberConversionMap.put(5, "f");
        game.numberConversionMap.put(6, "g");
        game.numberConversionMap.put(7, "h");

        game.resetBoard();

        game.printBoard();

        game.movePiece();

        System.exit(0);
    }

    public void printBoard() {
        boolean squareIsLight;
        for (int i=0; i<ROW_COUNT; i++) {
            System.out.print(8-i + " ");
            for (int k=0; k<COLUMN_COUNT; k++) {
                squareIsLight = (i + k) % 2 == 0;
                if (squareIsLight) {
                    System.out.print(ANSI_Tan + "[");
                } else {
                    System.out.print(ANSI_Brown + "[");
                }
                if (board[i][k] > 5 && board[i][k] != 12) {
                    System.out.print(ANSI_Black + board[i][k] + ANSI_Reset);
                } else if (board[i][k] > 0 && board[i][k] < 6 || board[i][k] == 12) {
                    System.out.print(ANSI_Reset + board[i][k] + ANSI_Reset);
                } else {
                    if (squareIsLight) {
                        System.out.print(ANSI_Tan + board[i][k] + ANSI_Reset);
                    } else {
                        System.out.print(ANSI_Brown + board[i][k] + ANSI_Reset);
                    }
                }
                if (squareIsLight) {
                    System.out.print(ANSI_Tan + "]");
                } else {
                    System.out.print(ANSI_Brown + "]");
                }
                System.out.print(ANSI_Reset);
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

    public String convertToString(int[] pos) {
        String row;
        String column;

        row = (8-pos[0]) + "";
        column = numberConversionMap.get(pos[1]);
        return column + row;
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
                    success = true;
                } else {
                    System.out.println("Invalid location! Try again!");
                    moveToLocation = scanner.next();
                }
            } while (!success);

            if (isMoveLegal(movingPiece, moveToLocation, true)) {
                if (board[validTestRow][validTestColumn] == 12) {
                    whiteKingPosition = convertToString(moveToLocationArray);
                } else if (board[validTestRow][validTestColumn] == 11) {
                    blackKingPosition = convertToString(moveToLocationArray);
                }
                board[moveToLocationRow][moveToLocationColumn] = board[validTestRow][validTestColumn];
                board[validTestRow][validTestColumn] = 0;
                if (pieceType == 1 && moveToLocationRow == 0 || pieceType == 6 && moveToLocationRow == 7) {
                    System.out.println("Promote Pawn");
                    promotePawn(validTestBoth);
                }
                if (!whiteTurn) {
                    // check if white is in checkmate after black's turn
                    whiteKingInCheck();
                    blackKingInCheck();
                    parentSuccess = whiteInCheckmate();
                    System.out.println("I just checked if white is in checkmate. It is " + parentSuccess + " that white is in checkmate.");
                    System.out.println("Is the move e4 to king location valid? " + isMoveLegal("e4", whiteKingPosition, false));
                } else {
                    whiteKingInCheck();
                    blackKingInCheck();
                    parentSuccess = blackInCheckmate();
                    System.out.println("I just checked if black is in checkmate. It is " + parentSuccess + " that black is in checkmate.");
                }

                whiteTurn = !whiteTurn;

            } else {
                System.out.println("Illegal move! Try again!");
            }

            printBoard();
        } while (!parentSuccess);

        if (whiteTurn) {
            System.out.println("Black wins");
        } else {
            System.out.println("White wins");
        }
    }

    public boolean isMoveLegal(String start, String end, boolean checkIfKingInCheck, boolean addCheckPath) {
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

        // temporally moves the piece to check if the king is in check

        if (checkIfKingInCheck) {
//            System.out.println(ANSI_Red + "DEBUG: isMoveLegal is checking is the king is in check after the move would be done");
            if (pieceType == 12) {
                whiteKingPosition =  convertToString(endLocation);
            }
            board[endLocation[0]][endLocation[1]] = pieceType;
            board[startLocation[0]][startLocation[1]] = 0;
            if (whiteTurn && whiteKingInCheck() || !whiteTurn && blackKingInCheck()) {
                board[startLocation[0]][startLocation[1]] = pieceType;
                board[endLocation[0]][endLocation[1]] = endPieceType;
                System.out.println("The king is in check");
                if (pieceType == 12) {
                    whiteKingPosition =  convertToString(startLocation);
                }
                return false;
            }
            board[startLocation[0]][startLocation[1]] = pieceType;
            board[endLocation[0]][endLocation[1]] = endPieceType;
            if (pieceType == 12) {
                whiteKingPosition =  convertToString(startLocation);
            }
        }


        // tells me what piece type (will be changed to more important stuff later)
        if (pieceType == whitePawn || pieceType == blackPawn) {
            return(pawnMoveLegal(startLocation, endLocation, pieceType, endPieceType));
        } else if (pieceType == whiteKnight || pieceType == blackKnight) {
            return(knightMoveLegal(startLocation, endLocation, addCheckPath));
        } else if (pieceType == whiteBishop || pieceType == blackBishop) {
            return(bishopMoveLegal(startLocation, endLocation, addCheckPath));
        } else if (pieceType == whiteRook || pieceType == blackRook) {
            return(rookMoveLegal(startLocation, endLocation, addCheckPath));
        } else if (pieceType == whiteQueen || pieceType == blackQueen) {
            return rookMoveLegal(startLocation, endLocation, addCheckPath) || bishopMoveLegal(startLocation, endLocation, addCheckPath);
        } else if (pieceType == blackKing || pieceType == whiteKing) {
            return kingMoveLegal(startLocation, endLocation);
        } else {
            // there is no piece here that is in the library of pieces
            return(false);
        }
    }

    public boolean isMoveLegal(String start, String end, boolean checkIfKingInCheck) {
        return(isMoveLegal(start, end, checkIfKingInCheck, false));
    }

    private boolean pawnMoveLegal(int[] startLocation, int[] endLocation, int pawnType, int endPieceType) {
        if (pawnType == whitePawn) {
            if (endPieceType == 0 && startLocation[1] == endLocation[1]) {
                if (startLocation[0] - 1 == endLocation[0]) {
                    return true;
                } else if (startLocation[0] - 2 == endLocation[0] && startLocation[0] == 6) {
                    return true;
                } else {
                    return false;
                }
            } else if (startLocation[0] - 1 == endLocation[0] && Math.abs(startLocation[1] - endLocation[1]) == 1 && endPieceType != 0) {
                return true;
            } else {
                return false;
            }
        } else if (pawnType == blackPawn) {
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
        } else {
            return false;
        }
    }

    private boolean knightMoveLegal(int[] startLocation, int[] endLocation, boolean addCheckPath) {
        // knight logic
        if (Math.abs(startLocation[0] - endLocation[0]) == 1 && Math.abs(startLocation[1] - endLocation[1]) == 2 || Math.abs(startLocation[0] - endLocation[0]) == 2 && Math.abs(startLocation[1] - endLocation[1]) == 1) {
            if (addCheckPath) {
                pathToBlackCheckingPieces.add(convertToString(startLocation));
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean bishopMoveLegal(int[] startLocation, int[] endLocation, boolean addCheckPath) {
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
            } else {
                if (addCheckPath) {
                    pathToBlackCheckingPieces.add(convertToString(startLocation));
                }
            }
        }
        if (addCheckPath) {
            pathToBlackCheckingPieces.add(convertToString(startLocation));
        }
        return true;
    }

    private boolean rookMoveLegal(int[] startLocation, int[] endLocation, boolean addCheckPath) {
        // rook logic
        if (startLocation[0] == endLocation[0]) {
            // the rook is moving horizontally staying on the same row

            // rook moving positive (to the right)
            if (startLocation[1] < endLocation[1]) {
                for (int i = startLocation[1]+1;  i < endLocation[1]; i++) {
                    if (board[startLocation[0]][i] != 0) {
                        return false;
                    } else {
                        if (addCheckPath) {
                            pathToBlackCheckingPieces.add(convertToString(new int[]{startLocation[0], i}));
                        }
                    }
                }
            } else if (startLocation[1] > endLocation[1]) {
                for (int i = startLocation[1]-1; i > endLocation[1]; i--) {
                    if (board[startLocation[0]][i] != 0) {
                        return false;
                    } else {
                        if (addCheckPath) {
                            pathToBlackCheckingPieces.add(convertToString(new int[]{startLocation[0], i}));
                        }
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
                    } else {
                        if (addCheckPath) {
                            pathToBlackCheckingPieces.add(convertToString(new int[]{i, startLocation[1]}));
                        }
                    }
                }
            } else if (startLocation[0] > endLocation[0]) {
                for (int i = startLocation[0]-1;  i > endLocation[0]; i--) {
                    if (board[i][startLocation[1]] != 0) {
                        return false;
                    } else {
                        if (addCheckPath) {
                            pathToBlackCheckingPieces.add(convertToString(new int[]{i, startLocation[1]}));
                        }
                    }
                }
            }
        } else {
            // the rook can only move straight up or down so if the starting and ending row or column doesn't match it isn't moving straight
            return false;
        }
        if (addCheckPath) {
            pathToBlackCheckingPieces.add(convertToString(startLocation));
        }
        return true;

    }

    private boolean kingMoveLegal(int[] startLocation, int[] endLocation) {
        return(Math.abs(startLocation[0] - endLocation[0]) <= 1 && Math.abs(startLocation[1] - endLocation[1]) <= 1);
    }

    private boolean whiteKingInCheck() {
        int[] testPos;
        System.out.println(ANSI_Red + "DEBUG: CHECKING IF THE WHITE KING IS IN CHECK" + ANSI_Reset);
        numberOfBlackCheckingPieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                testPos = new int[]{i,j};
                String stringTestPos = convertToString(testPos);
                if (board[i][j] > 5 && board[i][j] < 12) {
                    if (isMoveLegal(stringTestPos, whiteKingPosition, false, true)) {
                        System.out.println("The white king is in check form " + stringTestPos);
                        blackCheckingPiece = stringTestPos;
                        blackCheckingPieceType = board[i][j];
                        numberOfBlackCheckingPieces++;
                    }
                }
            }
        }
        if (numberOfBlackCheckingPieces == 0) {
            whiteKingInCheck = false;
            return false;
        } else if (numberOfBlackCheckingPieces == 1) {
            whiteKingInCheck = true;
            return true;
        } else {
            whiteKingInCheck = true;
            return true;
        }
    }

    private boolean blackKingInCheck() {
        int[] testPos;
        System.out.println(ANSI_Red + "DEBUG: CHECKING IF THE Black KING IS IN CHECK" + ANSI_Reset);
        numberOfWhiteCheckingPieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                testPos = new int[]{i,j};
                String stringTestPos = convertToString(testPos);
                if (board[i][j] > 5 && board[i][j] < 12) {
                    if (isMoveLegal(stringTestPos, blackKingPosition, false, true)) {
                        System.out.println("The black king is in check form " + stringTestPos);
                        whiteCheckingPiece = stringTestPos;
                        whiteCheckingPieceType = board[i][j];
                        numberOfWhiteCheckingPieces++;
                    }
                }
            }
        }
        if (numberOfWhiteCheckingPieces == 0) {
            blackKingInCheck = false;
            return false;
        } else if (numberOfWhiteCheckingPieces == 1) {
            blackKingInCheck = true;
            return true;
        } else {
            blackKingInCheck = true;
            return true;
        }
    }

    private void setupScreen() {
        size = frame.getBounds();

        System.out.println("Width " + size.width);
        System.out.println("Height " + size.height);

        int spaceSize;
        if (size.width<size.height) {
            spaceSize = (size.width/8);

        } else {
            spaceSize = (size.height/8);
        }
        System.out.println(spaceSize);
        JLabel title = new JLabel("Chess");

        frame.add(title, BorderLayout.NORTH);

    }

    private void promotePawn(int[] promotionSquare) {
        boolean success = false;
        do {
            System.out.println("What piece would you like to become? (Q for queen; R for rook; K for knight; B for bishop)");
            char promotionPieceType = scanner.next().charAt(0);

            if (whiteTurn) {
                if (promotionPieceType == 'Q' || promotionPieceType == 'q') {
                    board[0][promotionSquare[1]] = whiteQueen;
                    success = true;
                } else if (promotionPieceType == 'R' || promotionPieceType == 'r') {
                    board[0][promotionSquare[1]] = whiteRook;
                    success = true;
                } else if (promotionPieceType == 'K' || promotionPieceType == 'k') {
                    board[0][promotionSquare[1]] = whiteKnight;
                    success = true;
                } else if (promotionPieceType == 'B' || promotionPieceType == 'b') {
                    board[0][promotionSquare[1]] = whiteBishop;
                    success = true;
                } else {
                    System.out.println("Invalid promotion piece type! Please enter a valid promotion piece type.");
                }
            } else {
                if (promotionPieceType == 'Q' || promotionPieceType == 'q') {
                    board[7][promotionSquare[1]] = blackQueen;
                    success = true;
                } else if (promotionPieceType == 'R' || promotionPieceType == 'r') {
                    board[7][promotionSquare[1]] = blackRook;
                    success = true;
                } else if (promotionPieceType == 'K' || promotionPieceType == 'k') {
                    board[7][promotionSquare[1]] = blackKnight;
                    success = true;
                } else if (promotionPieceType == 'B' || promotionPieceType == 'b') {
                    board[7][promotionSquare[1]] = blackBishop;
                    success = true;
                }
            }
        } while (!success);
    }

    private boolean whiteInCheckmate() {
        whiteTurn = true;
        // first check if the king can move out of checkmate
        if (whiteKingInCheck) {
            int[] testPos;

            // this will check if the king can move to a location out of check
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int[] intKingPos = convertToNumber(whiteKingPosition);
                    testPos = new int[]{i + intKingPos[0], j +  intKingPos[1]};
                    System.out.println("testing if the king can move from " + intKingPos[0] + ", " + intKingPos[1] + " to " + testPos[0] + ", " + testPos[1]);
                    try {
                        System.out.println(ANSI_Red + "DEBUG: Is move legal " + whiteKingPosition + " to " + convertToString(testPos) + "? " + isMoveLegal(whiteKingPosition, convertToString(testPos), true) + ANSI_Reset);
                        if (isMoveLegal(whiteKingPosition, convertToString(testPos), true)) {
                            System.out.println(ANSI_Red + "The king can move to " + testPos[0] + ", " + testPos[1] + ANSI_Reset);
                            whiteTurn = false;
                            whiteKingPosition = convertToString(intKingPos);
                            return false;
                        }
                    } catch (Exception e) {
                        if (!(e instanceof java.lang.ArrayIndexOutOfBoundsException)) {
                            throw e;
                        }
                    }
                }
                System.out.println("loop");
            }

            // if you are in double check you can't move another piece to get out
            if (numberOfBlackCheckingPieces == 1) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        // finds a white piece
                        if (board[i][j] < 6 && board[i][j] > 0) {
//                            System.out.println(ANSI_Red + "DEBUG: checking if the piece from ");
                            if (isMoveLegal(convertToString(new int[]{i, j}), blackCheckingPiece, false)) {
                                whiteTurn = false;
                                System.out.println("You can capture the attacking piece from the location of " + i + ", " + j);
                                return false;
                            }
                            for (int k = 0; k < pathToBlackCheckingPieces.size(); k++) {
                                if (isMoveLegal(convertToString(new int[]{i, j}), pathToBlackCheckingPieces.get(k), false)) {
                                    System.out.println("White is not in checkmate because you can block the attack with the piece at " + convertToString(new int[]{i, j}));
                                    return false;
                                }
                            }
                        }
                    }
                }
            }


            // if all possible ways to get out of checkmate fail, return true which means your in checkmate
            System.out.println(ANSI_Red + "DEBUG: returning true to white in checkmate" + ANSI_Reset);
            whiteTurn = false;
            return true;
        } else  {
            System.out.println(ANSI_Red + "DEBUG: returning false to white in checkmate because the variable whiteKingInCheck is false" +  ANSI_Reset);
            whiteTurn = false;
            return false;
        }
    }

    private boolean blackInCheckmate() {
        whiteTurn = false;
        // first check if the king can move out of checkmate
        if (blackKingInCheck) {
            int[] testPos;

            // this will check if the king can move to a location out of check
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int[] intKingPos = convertToNumber(blackKingPosition);
                    testPos = new int[]{i + intKingPos[0], j +  intKingPos[1]};
                    System.out.println("testing if the black king can move from " + intKingPos[0] + ", " + intKingPos[1] + " to " + testPos[0] + ", " + testPos[1]);
                    try {
                        System.out.println(ANSI_Red + "DEBUG: Is move legal " + blackKingPosition + " to " + convertToString(testPos) + "? " + isMoveLegal(blackKingPosition, convertToString(testPos), true) + ANSI_Reset);
                        if (isMoveLegal(blackKingPosition, convertToString(testPos), true)) {
                            System.out.println(ANSI_Red + "The black king can move to " + testPos[0] + ", " + testPos[1] + ANSI_Reset);
                            whiteTurn = false;
                            whiteKingPosition = convertToString(intKingPos);
                            return false;
                        }
                    } catch (Exception e) {
                        if (!(e instanceof java.lang.ArrayIndexOutOfBoundsException)) {
                            throw e;
                        }
                    }
                }
                System.out.println("loop");
            }

            // if you are in double check you can't move another piece to get out
            if (numberOfWhiteCheckingPieces == 1) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        // finds a black piece
                        if (board[i][j] < 6 && board[i][j] > 0) {
//                            System.out.println(ANSI_Red + "DEBUG: checking if the piece from ");
                            if (isMoveLegal(convertToString(new int[]{i, j}), whiteCheckingPiece, false)) {
                                whiteTurn = true;
                                System.out.println("You can capture the attacking piece from the location of " + i + ", " + j);
                                return false;
                            }
                            for (int k = 0; k < pathToWhiteCheckingPieces.size(); k++) {
                                if (isMoveLegal(convertToString(new int[]{i, j}), pathToWhiteCheckingPieces.get(k), false)) {
                                    System.out.println("White is not in checkmate because you can block the attack with the piece at " + convertToString(new int[]{i, j}));
                                    return false;
                                }
                            }
                        }
                    }
                }
            }


            // if all possible ways to get out of checkmate fail, return true which means your in checkmate
            System.out.println(ANSI_Red + "DEBUG: returning true to black in checkmate" + ANSI_Reset);
            whiteTurn = true;
            return true;
        } else  {
            System.out.println(ANSI_Red + "DEBUG: returning false to black in checkmate because the variable blackKingInCheck is false" +  ANSI_Reset);
            whiteTurn = true;
            return false;
        }
    }

}

