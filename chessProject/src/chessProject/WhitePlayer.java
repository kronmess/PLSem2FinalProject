package chessProject;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//This is the code for the white player

public class WhitePlayer extends Player{
	
	//This class extends from the superclass Player where the constructor defines a collection of legal moves for
	//the black and white players and inherits the constructor from the player class, the white player's legal moves
	//are its legal moves and the black player's legal moves are the opponent's
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }
    
  //An overridden function used to get all white pieces on the board
    
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }
    
  //An overridden function used to get the side of the white player
    
    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }
    
  //An overridden function used to get the side of the white player
    
    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }
    
  //An overridden function used to calculate the condition for a castling move	
    
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
        	
        	//The white king side castle
        	//The code checks whether the tile where the knight and bishop should be is empty or not.
        	//Assuming that this is the rook's first move and it is still on its tile, the castle move
        	//is added as a special kingside castle move into a collection of castle moves
        	
            if (!this.board.getTile(61).isTileOccupied() &&
                !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new Move.KingSideCastleMove(this.board,
                                                               this.playerKing,
                                                               62,
                                                               (Rook) rookTile.getPiece(),
                                                               rookTile.getTileCoordinate(),
                                                               61));
                    }
                }
            }
            
            //The white queen side castle
            //The code checks whether the tile where the queen, knight and bishop should be is empty or not.
        	//Assuming that this is the rook's first move and it is still on its tile, the castle move
        	//is added as a special kingside castle move into a collection of castle moves
            
            if (!this.board.getTile(59).isTileOccupied() &&
                !this.board.getTile(58).isTileOccupied() &&
                !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()){
                    
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                            58,
                                                            (Rook) rookTile.getPiece(),
                                                            rookTile.getTileCoordinate(),
                                                            59));
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }
}
