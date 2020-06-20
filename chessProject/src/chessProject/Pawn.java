package chessProject;
import chessProject.Move.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//This is the code for the pawn piece
//a pawn is only able to move forward by one tile and so its exclusions only apply when attacking on its diagonals

public class Pawn extends Piece{
	
	//These are the coordinate offsets calculated for a pawn using our one dimensional approach
    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};
    
    //A pawn is constructed with its side and its position on the board
    //It also inherits from its piece superclass
    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }
    //This the code to calculate the legal moves for a bishop
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    	//An arraylist of legal moves is used to store all of possible legal moves
        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE){
            final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
            //If the pawn is on a valid tile and once the illegal moves have been excluded, the moves can be calculated
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            //This if check is used to check when it moves a tile forward whether or not that tile is being occupied or not
            //if the tile is on the eighth rank which is the tile on the other end of the board used for pawn promotion, the
            //pawn becomes eligible to promote, otherwise it moves forward normally
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                }else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            }//since the offset for a normal pawn move is 8, a pawn jump is simply twice that number, it checks whether the pawn is currently
            // on its starting position and it has not made its move yet
            else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                     (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))){
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                
                //by checking whether the tile in front of it and the tile ahead of that is occupied we can then add the
                //jump move to the legal moves
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                   !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }//an if check that checks for the pawn attacking move, a pawn attacks on its diagonals and so this checks whether
            //the opposite tile is occupied by another piece. If the piece it attacks is on the pawn promotion tile, the pawn
            //would capture on that tile and then promote because it is on the final rank
            
            //this is calculated for the left diagonal attack
            else if (currentCandidateOffset == 7 &&
                     !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }//one of the features is that since only one pawn at a time is capable of making an en passant move
                //, we can check whether the attacking pawn is qualified for en passant, assuming that the pawn is qualified for en passant
                //we can then add that attacking move as an en passant move instead of a normal attacking move
                else if(board.getEnPassantPawn() != null){
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
                
            }//an if check that checks for the pawn attacking move, a pawn attacks on its diagonals and so this checks whether
            //the opposite tile is occupied by another piece. If the piece it attacks is on the pawn promotion tile, the pawn
            //would capture on that tile and then promote because it is on the final rank.
            
          //this is calculated for the right diagonal attack
            else if (currentCandidateOffset == 9 &&
                     !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                     (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                  //one of the features is that since only one pawn at a time is capable of making an en passant move
                    //, we can check whether the attacking pawn is qualified for en passant, assuming that the pawn is qualified for en passant
                    //we can then add that attacking move as an enp assant move instead of a normal attacking move 
                }else if(board.getEnPassantPawn() != null){
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
      //Once the moves have been collected they are returned as an immutable list of moves 
        return Collections.unmodifiableList(legalMoves);
    }
  //An overriden method describing the movement of a pawn
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
  //An overriden method describing the toString() method for a bishop
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    //promotion is normally qualified for every other piece but the default promotion to a queen is used here
    public Piece getPromotionPiece(){
        return new Queen(this.pieceAlliance, this.piecePosition, false);
    }
    
}
