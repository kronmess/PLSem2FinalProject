package chessProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//This is the code for the bishop piece
public class Bishop extends Piece{
	//These are the coordinate offsets calculated for a bishop using our one dimensional approach
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES={ -9, -7, 7, 9 };
    //A bishop is constructed with its side and its position on the board
    //It also inherits from its piece superclass
    public Bishop(final Alliance pieceAlliance,
                  final int piecePosition) {

        super(PieceType.BISHOP, piecePosition, pieceAlliance, true);
    }
    public Bishop(final Alliance pieceAlliance,
                  final int piecePosition,
                  final boolean isFirstMove) {

        super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);
    }
    //This the code to calculate the legal moves for a bishop
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    	//An arraylist of legal moves is used to store all of possible legal moves
        final List<Move> legalMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate=this.piecePosition;
            //If the bishop is on a valid tile and once the illegal moves have been excluded, the moves can be calculated
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)||
                        isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate+=candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                	//Assuming that no tile occupies the tile that the bishop is going to be at, a normal move is added to the 
                	//legal moves array list
                    final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else{
                    	//Here a calculation is made to check whether the tile that the bishop is attacking, has a piece of the opposite
                    	//color. If so, an attacking move towards that piece is added to the legal moves array list
                        final Piece pieceAtDestination= candidateDestinationTile.getPiece();
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
    //An overridden method describing the movement of a bishop
    @Override
    public Bishop movePiece(final Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    //An overridden method describing the toString() method for a bishop
    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }
    //A bishop has some edge cases regarding the first column and on the eight, the following offsets are invalid
    //because they would cause the piece to move off the board and onto a wrong position.
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition]&&(candidateOffset==-9 ||candidateOffset==7);
    }
    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition]&&(candidateOffset==-7 ||candidateOffset==9);
    }
}
