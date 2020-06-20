package chessProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//This is the code for the king piece
public final class King extends Piece {
	//These are the coordinate offsets calculated for a king using our one dimensional approach
    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };
    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;
    //A king's constructor with its side, its position on the board and two boolean values
    //to check the possibility of a castle move on either side of the board
    //It also inherits from its piece superclass
    public King(final Alliance alliance,
                final int piecePosition,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, alliance, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }
  //A king's constructor with its side, its position on the board,two boolean values
    //a check to see whether the king has moved or is curently castled
    //to check the possibility of a castle move on either side of the board
    //It also inherits from its piece superclass
    public King(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove,
                final boolean isCastled,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, alliance, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }
    //Getter method for the isCastled variable
    public boolean isCastled() {
        return this.isCastled;
    }
  //Getter method for the kingSideCastleCapable variable
    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }
  //Getter method for the queenSideCastleCapable variable
    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }
  //This the code to calculate the legal moves for a king
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            //If the king is on a valid tile and once the illegal moves have been excluded, the moves can be calculated
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
            	//Assuming that no tile occupies the tile that the king is going to be at, a normal move is added to the 
            	//legal moves array list
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }else{
                	//Here a calculation is made to check whether the tile that the king is attacking, has a piece of the opposite
                	//color. If so, an attacking move towards that piece is added to the legal moves array list
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
      //Once the moves have been collected they are returned as an immutable list of moves 
        return Collections.unmodifiableList(legalMoves);
    }
  //An overridden method describing the toString() method for a King
    @Override
    public String toString() {
        return this.pieceType.toString();
    }

    
  //An overridden method describing the movement of a King
    @Override
    public King movePiece(final Move move) {
        return new King(this.pieceAlliance, move.getDestinationCoordinate(), false, move.isCastlingMove(), false, false);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof King)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final King king = (King) other;
        return isCastled == king.isCastled;
    }
    //An overridden function of the hash code function , we use our own implementation because a class will not be able to function properly
    //when working with hash based collections if .equals() is overridden.
    @Override
    public int hashCode() {
        return (31 * super.hashCode()) + (isCastled ? 1 : 0);
    }
  //A King has some edge cases regarding the first column and on the eight, the following offsets are invalid
    //because they would cause the piece to move off the board and onto a wrong position.
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }
}