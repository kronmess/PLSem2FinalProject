package chessProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//This is the code for the knight piece
public class Knight extends Piece{
	//These are the coordinate offsets calculated for a knight using our one dimensional approach
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};
    //A knight is constructed with its side and its position on the board
    //It also inherits from its piece superclass
    public Knight(final Alliance pieceAlliance,
                  final int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }

    public Knight(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

  //This the code to calculate the legal moves for a knight
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    	//An arraylist of legal moves is used to store all of possible legal moves
        final List<Move> legalMoves=new ArrayList<>();
        for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate=this.piecePosition+currentCandidateOffset;
          //If the knight is on a valid tile and once the illegal moves have been excluded, the moves can be calculated
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){


                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }
                final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);
                //Assuming that no tile occupies the tile that the knight is going to be at, a normal move is added to the 
            	//legal moves array list
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else{
                	//Assuming that no tile occupies the tile that the knight is going to be at, a normal move is added to the 
                	//legal moves array list
                    final Piece pieceAtDestination= candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance= pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
      //Once the moves have been collected they are returned as an immutable list of moves
        return Collections.unmodifiableList(legalMoves);
    }
  //An overridden method describing the movement of a knight
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
  //An overridden method describing the toString() method for a knight
    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }
    //A knight has some edge cases regarding the first column,second, seventh and on the eight, the following offsets are invalid
    //because they would cause the piece to move off the board and onto a wrong position.
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 ||
                candidateOffset == 10 || candidateOffset == 17);
    }

}
