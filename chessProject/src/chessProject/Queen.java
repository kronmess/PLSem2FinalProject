package chessProject;
//This is the code for the queen piece
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Queen extends Piece{
	//These are the coordinate offsets calculated for a queen using our one dimensional approach
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES={ -9, -8, -7, -1, 1, 7, 8, 9 };
  //A queen is constructed with its side and its position on the board
    //It also inherits from its piece superclass
    public Queen(final Alliance pieceAlliance,
                 final int piecePosition) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, true);
    }
    public Queen(final Alliance pieceAlliance,
                 final int piecePosition,
                 final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
    }
    //This the code to calculate the legal moves for a queen
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    	//An arraylist of legal moves is used to store all of possible legal moves
        final List<Move> legalMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate=this.piecePosition;
            //If the queen is on a valid tile and once the illegal moves have been excluded, the moves can be calculated
            while(true){
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)||
                        isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate+=candidateCoordinateOffset;
                if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    break;
                }else{
                	//Assuming that no tile occupies the tile that the queen is going to be at, a normal move is added to the 
                	//legal moves array list
                    final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);
                    final Piece pieceAtDestination= candidateDestinationTile.getPiece();
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else{
                    	//Here a calculation is made to check whether the tile that the queen is attacking, has a piece of the opposite
                    	//color. If so, an attacking move towards that piece is added to the legal moves array list
                        final Alliance pieceAlliance= pieceAtDestination.getPieceAlliance();
                        if(this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }

                }
            }
        }
      //Once the moves have been collected they are returned as an immutable list of moves 
        return Collections.unmodifiableList(legalMoves);
    }
    //An overridden method describing the movement of a queen
    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
  //An overridden method describing the toString() method for a queen
    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }
    //A queen has some edge cases regarding the first column and on the eight, the following offsets are invalid
    //because they would cause the piece to move off the board and onto a wrong position.
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset == -1 || candidateOffset==-9 ||candidateOffset==7);
    }
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset == 1 || candidateOffset==-7 ||candidateOffset==9);
    }
}
