package com.cis3296.virtualchess;

import com.cis3296.virtualchess.Pieces.*;
import javafx.geometry.Insets;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.Event;

public class Board {
    // In case we need to change these column/row/size values for any reason later on...
    public final int MAX_COL = 8, MAX_ROW = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;

    private GridPane chessBoard;

    public ArrayList<BoardSquare> boardSquares = new ArrayList<>();
    private ArrayList<Piece> pieces = new ArrayList<>();

    private BoardSettings settings;

    private Piece draggingPiece;

    //The border surround each of the board squares
    private final Border border = new Border(
                                new BorderStroke(
                                        Color.BLACK,
                                        BorderStrokeStyle.SOLID,
                                        CornerRadii.EMPTY,
                                        BorderWidths.DEFAULT
                                )
                        );

    /**
     *  Constructor for the Chess Board
     * @param chessBoard - A gridpane representing the chessboard
     */
    public Board(GridPane chessBoard, BoardSettings settings){

        this.chessBoard = chessBoard;
        this.settings = settings;
        init(this.chessBoard);
    }

    /**
     * Goes through each of the tiles in the board and sets them up to be displayed
     * @param chessBoard - A GridPane representing the chessboard
     */
    private void init(GridPane chessBoard){
        for(int col = 0; col < MAX_COL; col++){
            for(int row = 0; row < MAX_ROW; row++){
                Coordinates coordinates = new Coordinates(col, row);
                BoardSquare square = new BoardSquare(coordinates);
                square.setPrefHeight(SQUARE_SIZE);
                square.setPrefWidth(SQUARE_SIZE);
                square.setBorder(border);
                setSquareColor(square);
                chessBoard.add(square, col, row, 1, 1);
                boardSquares.add(square);
                square.setOnDragDropped(dragEvent ->{
                    square.getChildren().add(draggingPiece);
                });
            }
        }
        addPieces();
        // Add drag-and-drop event handlers for each Piece object
        for (Piece piece : pieces) {
            piece.setOnDragDetected(event -> {
                draggingPiece = piece;
                Dragboard db = piece.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(""); // You can put any content here if needed
                db.setContent(content);
                event.consume();
            });

            // Optionally, add other drag-and-drop event handlers (e.g., drag over, drag dropped)
            // if needed for additional functionality.
        }
    }

    /**
     *  Sets the color of a given square
     * @param square - The square that's color will be changed
     */
    private void setSquareColor(BoardSquare square){
        if((square.coordinates.getCol()+square.coordinates.getRow())%2==0){
            square.setBackground(
                    new Background(
                            new BackgroundFill(
                                    settings.currentBoardStyle.squareColor1,
                                    CornerRadii.EMPTY,
                                    Insets.EMPTY
                            )
                    )
            );
        }else{
            square.setBackground(
                    new Background(
                            new BackgroundFill(
                                    settings.currentBoardStyle.squareColor2,
                                    CornerRadii.EMPTY,
                                    Insets.EMPTY
                            )
                    )
            );
        }
    }

    /**
     * This method associates the objects that extend the Piece class to the BoardSquare objects.
     * It also sets the pieces to the correct chess start setup.
     */
    private void addPieces(){
        for(BoardSquare square : boardSquares){
            if(square.coordinates.getRow() == 0){
                if(square.coordinates.getCol() == 0 || square.coordinates.getCol() == 7){
                    Piece rookW = new Rook(square.coordinates, "white");
                    addPiece(square, rookW);
                }
                if(square.coordinates.getCol() == 1 || square.coordinates.getCol() == 6){
                    Piece knightW = new Knight(square.coordinates, "white");
                    addPiece(square, knightW);
                }
                if(square.coordinates.getCol() == 2 || square.coordinates.getCol() == 5){
                    Piece bishopW = new Bishop(square.coordinates, "white");
                    addPiece(square, bishopW);
                }
                if(square.coordinates.getCol() == 3){
                    Piece queenW = new Queen(square.coordinates, "white");
                    addPiece(square, queenW);
                }
                if(square.coordinates.getCol() == 4){
                    Piece kingW = new King(square.coordinates, "white");
                    addPiece(square, kingW);
                }

            }
            if(square.coordinates.getRow() == 1){
                Piece pawnW = new Pawn(square.coordinates, "white");
                addPiece(square, pawnW);
            }
            if(square.coordinates.getRow() == 6){
                Piece pawnB = new Pawn(square.coordinates, "black");
                addPiece(square, pawnB);
            }

            if(square.coordinates.getRow() == 7){
                if(square.coordinates.getCol() == 0 || square.coordinates.getCol() == 7){
                    Piece rookB = new Rook(square.coordinates, "black");
                    addPiece(square, rookB);
                }
                if(square.coordinates.getCol() == 1 || square.coordinates.getCol() == 6){
                    Piece knightB = new Knight(square.coordinates, "black");
                    addPiece(square, knightB);
                }
                if(square.coordinates.getCol() == 2 || square.coordinates.getCol() == 5){
                    Piece bishopB = new Bishop(square.coordinates, "black");
                    addPiece(square, bishopB);
                }
                if(square.coordinates.getCol() == 3){
                    Piece kingB = new King(square.coordinates, "black");
                    addPiece(square, kingB);
                }
                if(square.coordinates.getCol() == 4){
                    Piece queenB = new Queen(square.coordinates, "black");
                    addPiece(square, queenB);
                }
            }
        }


    }

    public void rerenderBoard() {
        for (BoardSquare square : boardSquares) {
            setSquareColor(square);
        }
    }

    private void addPiece(BoardSquare square, Piece piece){
        pieces.add(piece);
        square.getChildren().add(piece);
        square.containsPiece = true;
    }

    /**
     * Getter for the piece currently being dragged
     * @return dragged piece
     */
    public Piece getDraggingPiece() {
        return draggingPiece;
    }

    /**
     * Checks to see if teh move being made is valid
     * @param draggingPiece the piece currently being dragged
     * @return
     */
//    public boolean isValidMove(Piece draggingPiece) {
//        return draggingPiece.canMove(hoverCoordinates.getCol(), hoverCoordinates.getRow()) && isValidCoordinate(hoverCoordinates.getCol(), hoverCoordinates.getRow());
//    }

    /**
     * Checks to see if the coordinates are valid coordinates on the GridPane
     * @param col column the piece is being dragged over
     * @param row row the piece is being dragged over
     * @return
     */
    private boolean isValidCoordinate(int col, int row) {
        return col >= 0 && col < MAX_COL && row >= 0 && row < MAX_ROW;
    }

}
