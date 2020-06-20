package chessProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import chessProject.Piece.PieceType;

//The abstract class pkayer which the black and white player classes inherit from
public abstract class Player {
	//The member variables of a player, the board, each player's respective king and the amount of legal moves they have,
	//a boolean value is also used to see whether the king piece is being attacked
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    protected final boolean isInCheck;
    //Within the constructor, we have the board and two collections of moves, the amount of legal moves that player has 
    //and their oppopnent's legal moves. 
    Player(final Board board,
           final Collection<Move> playerLegals,
           final Collection<Move> opponentLegals) {
        this.board = board;
        //a function used to establish a king on either player's side
        this.playerKing = establishKing();
        //a function used to narrow down the meaning of a check which in this case we deduce whether the king is being attacked
        //and this would mean the king is in check
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
        this.legalMoves = Collections.unmodifiableCollection(playerLegals);
    }
    //function used to check 
    public boolean isInCheck() {
        return this.isInCheck;
    }
    //function used to check for checkmate, like in real chess, checkmate occurs when the king is currently under check
    //and it has no where else to move or no legal moves to make. this results in a game over for the player.
    public boolean isInCheckMate() {
       return this.isInCheck && !hasEscapeMoves();
    }
    //function used to check for stalemate, in chess a stalemate occurs when the king is not being checked. However, it is unable
    //to move because the tiles within its legal moves are being attacked and so it is unable to move. This is also a game over
    //for either player
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }
    
    //function used to check if the king is castled
    public boolean isCastled() {
        return this.playerKing.isCastled();
    }
  //function used to check if the king can castle on its king side
    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }
  //function used to check if the king can castle on its queen side
    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }
    //getter method for the player king
    public King getPlayerKing() {
        return this.playerKing;
    }
    // a function used that runs through all the pieces and filters it by the piece type until it finds the king piece
    //other wise it will throw a runtime exception if no piece is valid.
    private King establishKing() {
        return (King) getActivePieces().stream()
                                       .filter(piece -> piece.getPieceType() == PieceType.KING)
                                       .findAny()
                                       .orElseThrow(RuntimeException::new);
    }
    //a function which checks whether or not the king has any moves to escape an attack, it checks and compares the player's
    //legal moves and checks whether any move is possible.
    private boolean hasEscapeMoves() {
        return this.legalMoves.stream()
                              .anyMatch(move -> makeMove(move)
                              .getMoveStatus().isDone());
    }
    //getter method for legal moves
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }
    //the function used to calculate the amount of attacks on a given tile, it checks whether a move is possible at the destination
    //tile which is the tile being attacked and then this legal move is filtered into a colllection of moves.
    static Collection<Move> calculateAttacksOnTile(final int tile,
                                                   final Collection<Move> moves) {
        return moves.stream()
                    .filter(move -> move.getDestinationCoordinate() == tile)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
    //the function that defines what happens when a move is made
    public MoveTransition makeMove(final Move move) {
    	//an if condition that asks that if the move attempted is one that is not contained within the legal moves, the move is deemed
    	//illegal and it cannot be made
        if (!this.legalMoves.contains(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        
        //whenever a move is made on our chessboard, what actually happens is that the pieces are redrawn 
        //and the board with it to show the new position
        final Board transitionedBoard = move.execute();
        
        //here a ternary expression is used to check whether the opponent is in check, in chess if you check your opponent
        //it is considered a special game state where the opponent player has to prevent the attack on the king and no other
        //piece can move.
        return transitionedBoard.currentPlayer().getOpponent().isInCheck() ?
                new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK) :
                new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
    }


    //an abstract method used to retrieve the active pieces of the player
    public abstract Collection<Piece> getActivePieces();
    //an abstract method used to retrieve the alliance/side of the player
    public abstract Alliance getAlliance();
    //an abstract method used to retrieve the opponent player
    public abstract Player getOpponent();
    //an abstract method used to calculate the legal moves for a king castle
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                             Collection<Move> opponentLegals);
    //a function that checks whether a castle is possible by checking whether or not the king is in check or already castled.
    //if neither have happened, it checks whether the castle is possible on the king side or the queen side of the board.
    protected boolean hasCastleOpportunities() {
        return !this.isInCheck && !this.playerKing.isCastled() &&
                (this.playerKing.isKingSideCastleCapable() || this.playerKing.isQueenSideCastleCapable());
    }

}
