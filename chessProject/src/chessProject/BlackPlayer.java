package chessProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
//This is the code for the black player
public class BlackPlayer extends Player{
	//This class extends from the superclass Player where the constructor defines a collection of legal moves for
	//the black and white playersand inherits the constructor from the player class, the black player's legal moves
	//are its legal moves and the white player's legal moves are the opponent's
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board,blackStandardLegalMoves, whiteStandardLegalMoves);
    }
    //An overridden function used to get all black pieces on the board
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }
    //An overridden function used to get the side of the black player
    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }
    //An overridden function used to get the side of the black player
    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
  //An overridden function used to calculate the condition for a castling move
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //The black king side castle
        	//The code checks whether the tile where the knight and bishop should be is empty or not.
        	//Assuming that this is the rook's first move and it is still on its tile, the castle move
        	//is added as a special kingside castle move into a collection of castle moves
            if (!this.board.getTile(5).isTileOccupied() &&
                !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new Move.KingSideCastleMove(this.board,
                                                               this.playerKing,
                                                               6,
                                                               (Rook) rookTile.getPiece(),
                                                               rookTile.getTileCoordinate(),
                                                               5));
                    }
                }
            }
            //The black queen side castle
            //The code checks whether the tile where the queen, knight and bishop should be is empty or not.
        	//Assuming that this is the rook's first move and it is still on its tile, the castle move
        	//is added as a special kingside castle move into a collection of castle moves
            if (!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()){
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                            2,
                                                            (Rook) rookTile.getPiece(),
                                                            rookTile.getTileCoordinate(),
                                                            3));
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }
}
