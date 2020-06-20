package chessProject;

import java.util.Collection;
import java.util.List;
//the abstract class each of the piece classes inherit from
public abstract class Piece {
	//the piece has a piece type, its position on the board, the side/alliance it's on and a boolean value for its first move
	//and a hash code used when overriding the pre-defined hashcode() function
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;
    
    //the constructor for the piece class
    Piece(final PieceType pieceType,
          final int piecePosition,
          final Alliance pieceAlliance,
          final boolean isFirstMove) {
        this.pieceType=pieceType;
        this.pieceAlliance=pieceAlliance;
        this.piecePosition=piecePosition;
        this.isFirstMove=isFirstMove;
        this.cachedHashCode=computeHashCode();
    }
    //hash function used to calculate the hash code
    private int computeHashCode() {
        int result=pieceType.hashCode();
        result=31*result+pieceAlliance.hashCode();
        result=31*result+piecePosition;
        result=31*result+(isFirstMove? 1:0);
        return result;
    }
    //The equals function is being overridden for the code we are using to compare two objects
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece=(Piece) other;
        return piecePosition==otherPiece.getPiecePosition()&&pieceType==otherPiece.getPieceType()&&
                pieceAlliance==otherPiece.getPieceAlliance() && isFirstMove==otherPiece.isFirstMove();
    }
    //An overridden function of the hash code function , we use our own implementation because a class will not be able to function properly
    //when working with hash based collections if .equals() is overridden
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }
    //getter method for a piece's positions
    public int getPiecePosition(){
        return this.piecePosition;
    }
  //getter method for a piece's positions
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
  //getter method for whether this is a piece's first move
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
  //getter method for the type of a piece
    public PieceType getPieceType(){
        return this.pieceType;
    }
    //the abstract method each piece uses to calculate its own amount of legal moves
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    //the abstract method used to move a piece by creating a new piece after the move has been made
    public abstract Piece movePiece(Move move);
    // an enum that describes each piece type, the types are the pawn, knight, bishop,rook queen and king
    public enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing(){
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("H") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B"){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R"){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q"){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K"){
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        };
    	//each of the types have their own name 
        private String pieceName;
        //the piece type of a piece can be called by its name. For example, we want to check whether the piece we are interacting
        //with is a bishop, since each piece is already assigned their types we can use these names.
        PieceType(final String pieceName){
            this.pieceName=pieceName;
        }
        //the toString() method for the piece type which returns the name of that piece
        @Override
        public String toString() {
            return this.pieceName;
        }
        //two abstract methods used to check whether a piece is a king or rook,  
        //this is only used during the calculations of a castle move 
        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}
