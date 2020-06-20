
package chessProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chessProject.Board.Builder;
//the board class used for the chessboard
public class Board {
	
	//here we have a list of tiles that make up the game board, the white pieces and black pieces
	//, the white and black player and the current player which is the player eligible to move
	//there is also a check for the en passant pawn since there can only be one en passant pawn
	//on the board at any given time.
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;
    
    //the constructor for a chessboard using the builder class, the board, pieces and players are given their values here
    private Board(final Builder builder){
        this.gameBoard=createGameBoard(builder);
        this.whitePieces=calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces=calculateActivePieces(this.gameBoard, Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves=calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves=calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }
    
    //the to string method used to generate a string output of the chessbard's starting position, since each tile
    //has their own to string and the pieces have names, the board will be generator with the white pieces in upper case
    //and the black pieces in lower case and the empty tiles will have the - string
    @Override
    public String toString(){
        final StringBuilder builder=new StringBuilder();
        for(int i=0;i<BoardUtils.NUM_TILES;i++){
            final String tileText=this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i+1) % BoardUtils.NUM_TILES_PER_ROW==0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    
    //getter method for the white player
    public Player whitePlayer(){
        return this.whitePlayer;
    }
    
  //getter method for the black player
    public Player blackPlayer(){
        return this.blackPlayer;
    }
    
  //getter method for the current player
    public Player currentPlayer(){
        return this.getCurrentPlayer();
    }
    
  //getter method for the black pieces
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    
  //getter method for the black pieces
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    //a function where all of the pieces' legal moves are calculated here and added to a collection of all legal moves
    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {

        final List<Move> legalMoves=new ArrayList<>();
        for(final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    //a function that checks whether a piece is present on tile on the board and if that tile has a piece on it, it gets the
    //piece on that tile and is added to a collection of active pieces, this is so the board knows where a piece is on the board at
    //any given time
    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,
                                                           final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }
    
    //getter method for en passant pawn
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    
    //getter method for a tile on the board
    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }
    
    //this is the construction of the game board, the board is primed by first creating all the possible tiles within the
    //board numbered from 1 to 63.
    public static List<Tile> createGameBoard(Builder builder){
		Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		List<Tile> tilesList =  Arrays.asList(tiles);
		return tilesList;
	}
    
    //the function used to create the board that is going to be used to play,here every piece is assigned their squares and their
    //sides/alliances and the type of pieces they are
    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        //Black layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4, true, true));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        //White layout
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60, true, true));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        //white to move
        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }
    
    //function used to get all legal moves from both players
    public Collection<Move> getAllLegalMoves() {
        return Stream.concat(this.whitePlayer.getLegalMoves().stream(),
                             this.blackPlayer.getLegalMoves().stream()).collect(Collectors.toList());
    }
    
    //function used to get all black and white pieces on the board
    public Collection<Piece> getAllPieces() {
        return Stream.concat(this.whitePieces.stream(),
                             this.blackPieces.stream()).collect(Collectors.toList());
    }
    
    //getter method for the current player
    public Player getCurrentPlayer() {
		return currentPlayer;
	}
    
    //the builder class used as the controller for the game
	public static class Builder{
		//certain things are set here such as the board config which is the configuration of a chess board,
		//the current side that is eligible to move, the en passant pawn and the move that is going to be done
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;
        Move transitionMove;
        //the constructor for the board
        public Builder(){
            this.boardConfig=new HashMap<>();

        }
        //the function used to set a piece on the board
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        //a function that determines whose turn it is to move
        public Builder setMoveMaker(final Alliance alliance){
            this.nextMoveMaker=alliance;
            return this;
        }
        //the build function which returns the board to be built
        public Board build(){
            return new Board(this);
        }
        //the setter for the en passant pawn
        public void setEnPassantPawn(Pawn enPassantPawn){
            this.enPassantPawn= enPassantPawn;
        }
        //the function that sets the move to transition the board
        public Builder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }
    }

}
