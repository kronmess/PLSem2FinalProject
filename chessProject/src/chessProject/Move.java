package chessProject;

import chessProject.Board.Builder;
//the abstract class move, here all the types of moves are defined
public abstract class Move {
	//the board that the move is being made on is defined, the destination location of the move
	//the piece being moved and whether or not the piece being moved has moved yet
    protected final Board board;
    protected final int destinationCoordinate;
    protected final Piece movedPiece;
    protected final boolean isFirstMove;
    //constructor for the move
    private Move(final Board board,
                 final Piece pieceMoved,
                 final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = pieceMoved;
        this.isFirstMove = pieceMoved.isFirstMove();
    }
  //constructor for an invalid move
    private Move(final Board board,
                 final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    //An overridden function of the hash code function , we use our own implementation because a class will not be able to function properly
    //when working with hash based collections if .equals() is overridden.
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationCoordinate;
        result = 31 * result + this.movedPiece.hashCode();
        result = 31 * result + this.movedPiece.getPiecePosition();
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }
    //overridden equals function
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
               getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
               getMovedPiece().equals(otherMove.getMovedPiece());
    }
    //getter method for the board
    public Board getBoard() {
        return this.board;
    }
  //getter method for the current location of a piece
    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }
  //getter method for the destination location 
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
  //getter method for moved piece
    public Piece getMovedPiece() {
        return this.movedPiece;
    }
  //method to check for an attack
    public boolean isAttack() {
        return false;
    }
  //method to check for a castling move
    public boolean isCastlingMove() {
        return false;
    }
  //method to check where a piece is being attacked
    public Piece getAttackedPiece() {
        return null;
    }
    //this function is used to execute a move, a builder is initialized and then the active pieces for each player are
    //added and the moved piece is set on the board, the move is made by the current player and then transitioned into the new board
    //and the move is finally made when the builder is built again.
    public Board execute() {
        final Board.Builder builder = new Builder();
        this.board.currentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);
        this.board.currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        builder.setMoveTransition(this);
        return builder.build();
    }

   
    //a function used to return a string after the position of the pieces have been changed in order to keep track of the move and its
    //notation value
    String disambiguationFile() {
        for(final Move move : this.board.currentPlayer().getLegalMoves()) {
            if(move.getDestinationCoordinate() == this.destinationCoordinate && !this.equals(move) &&
               this.movedPiece.getPieceType().equals(move.getMovedPiece().getPieceType())) {
                return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1);
            }
        }
        return "";
    }
    

    //the funtion used for pawn promotion
    public static class PawnPromotion extends Move{
        //decorator design pattern used to add functionality during runtime
    	//the pawn being promoted is being kept track of
        final Move decoratedMove;
        final Pawn promotedPawn;
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }
      //An overridden function of the hash code function , we use our own implementation because a class will not be able to function properly
        //when working with hash based collections if .equals() is overridden.
        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }
      //overridden equals function
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnPromotion && this.decoratedMove.equals(other);
        }
        //the execute function 
        @Override
        public Board execute(){
        	//sets the pawn move and the board
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()){
                if (!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            //sets the piece of the opponent
            for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            //once the pawn has been promoted, the pawn becomes the promoted piece which in this case is the queen
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
            return builder.build();
        }
        //overridden function for is attack
        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }
      //overridden function for attacked piece
        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }
      //overridden function for toString() method
        @Override
        public String toString(){
            return "";
        }


    }
    //the most basic move, the major move is any move onto a tile that is not an attack or any other special move
    //such as a bishop moving on its diagonals
    public static class MajorMove
            extends Move {
    	//constructor for the major  move
        public MajorMove(final Board board,
                         final Piece pieceMoved,
                         final int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }
        //overridden equals function
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }
      //overridden toString() method
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + disambiguationFile() +
                   BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    //the class used for a basic attacking move or known as a capture in chess, it extends from the attack move class
    public static class MajorAttackMove
            extends AttackMove {
    	//constructor for the major attack move
        public MajorAttackMove(final Board board,
                               final Piece pieceMoved,
                               final int destinationCoordinate,
                               final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }
        //overridden equals function
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);

        }
        //overridden toString() method
        @Override
        public String toString() {
            return movedPiece.getPieceType() + disambiguationFile() + "x" +
                   BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    //a class used to define a pawn move, the reason why the pawn is different is because is has many kinds of moves such as 
    //a pawn jump, this is the most simple pawn move where it moves a tile forward
    public static class PawnMove
            extends Move {
    	//constructor for the pawn move
        public PawnMove(final Board board,
                        final Piece pieceMoved,
                        final int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }
        //overridden equals function
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }
        //overridden toString() method
        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    //a class used to define the pawn attacking move
    public static class PawnAttackMove
            extends AttackMove {
    	//constructor for the pawn attack move
        public PawnAttackMove(final Board board,
                              final Piece pieceMoved,
                              final int destinationCoordinate,
                              final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }
     	//overridden equals function
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }
        //overridden toString() method
        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" +
                   BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    //this is the definition for the special en passant move of a pawn, it extends from the pawn attacking move
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
    	//constructor for the en passant move
        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movedPiece,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
        //overridden equals function
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }
        //the execute function is overridden here 
        @Override
        public Board execute(){
        	//builder is instantiated here
            final Builder builder = new Builder();
            //the current player's pieces are get and then set by the builder
            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            //the opponent's pieces are also taken and checked for an attack
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
            //the piece is moved here and the en passant is completed
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

    }
    //here is the class for the pawn jump
    public static class PawnJump
            extends Move {
    	//constructor for the pawn jump class
        public PawnJump(final Board board,
                        final Pawn pieceMoved,
                        final int destinationCoordinate) {
            super(board, pieceMoved, destinationCoordinate);
        }
      //overridden equals function
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }
        //the execute function
        @Override
        public Board execute() {
        	//builder is instantiated
            final Board.Builder builder = new Builder();
            //current and opponent players pieces are taken and set
            this.board.currentPlayer().getActivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);
            this.board.currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);
            //the moved pawn is kept track of
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            //here we make room for en passant since en passant occurs when a pawn captures a pawn that has jumped from
            //its original position
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            //the move is then made by the player and built 
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }
        //overridden toString() method
        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }
    //the class used for the castle move
    static abstract class CastleMove
            extends Move {
    	//we keep track of the rook to be castled and its starting and end position
        final Rook castleRook;
        final int castleRookStart;
        final int castleRookDestination;
        
        //constructor for the castle move class
        CastleMove(final Board board,
                   final Piece pieceMoved,
                   final int destinationCoordinate,
                   final Rook castleRook,
                   final int castleRookStart,
                   final int castleRookDestination) {
            super(board, pieceMoved, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        //getter method for the castle rook
        Rook getCastleRook() {
            return this.castleRook;
        }
        //overridden method that returns true since this is a castling move
        @Override
        public boolean isCastlingMove() {
            return true;
        }
        //the execution of a castle move
        @Override
        public Board execute() {
            final Board.Builder builder = new Builder();
            for (final Piece piece : this.board.getAllPieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            //the castle cannot be made using the move piece function and instead a new rook is made at the
            //destination position  
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination, false));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            builder.setMoveTransition(this);
            return builder.build();
        }
      //An overridden function of the hash code function , we use our own implementation because a class will not be able to function properly
        //when working with hash based collections if .equals() is overridden.
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }
      //overridden equals function
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }

    }
    //type of castle move which occurs on the king side is defined in this class
    //it extends from the castle move
    public static class KingSideCastleMove
            extends CastleMove {
    	//constructor of king side castle move
        public KingSideCastleMove(final Board board,
                                  final Piece pieceMoved,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, pieceMoved, destinationCoordinate, castleRook, castleRookStart,
                    castleRookDestination);
        }
        //overridden equals function
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof KingSideCastleMove)) {
                return false;
            }
            final KingSideCastleMove otherKingSideCastleMove = (KingSideCastleMove) other;
            return super.equals(otherKingSideCastleMove) && this.castleRook.equals(otherKingSideCastleMove.getCastleRook());
        }
        //overridden toString() method
        @Override
        public String toString() {
            return "O-O";
        }

    }
    //type of castle move which occurs on the queen side is defined in this class
    //it extends from the castle move
    public static class QueenSideCastleMove
            extends CastleMove {
    	//constructor of a queen side castle move
        public QueenSideCastleMove(final Board board,
                                   final Piece pieceMoved,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int rookCastleDestination) {
            super(board, pieceMoved, destinationCoordinate, castleRook, castleRookStart,
                    rookCastleDestination);
        }
        //overridden equals function
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof QueenSideCastleMove)) {
                return false;
            }
            final QueenSideCastleMove otherQueenSideCastleMove = (QueenSideCastleMove) other;
            return super.equals(otherQueenSideCastleMove) && this.castleRook.equals(otherQueenSideCastleMove.getCastleRook());
        }
        //overridden toString() function
        @Override
        public String toString() {
            return "O-O-O";
        }

    }
    //class used for an attacking move
    static abstract class AttackMove
            extends Move {
    	//the piece that is being attacked for a move
        private final Piece attackedPiece;
        //constructor for an attack move
        AttackMove(final Board board,
                   final Piece pieceMoved,
                   final int destinationCoordinate,
                   final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate);
            this.attackedPiece = pieceAttacked;
        }
      //An overridden function of the hash code function , we use our own implementation because a class will not be able to function properly
        //when working with hash based collections if .equals() is overridden.
        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        //overridden equals function
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        //getter method for the attacked piece
        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
        //overridden function that returns true because a move is being attacked
        @Override
        public boolean isAttack() {
            return true;
        }

    }
    //the definition of an invalid move or a null move
    private static class NullMove
            extends Move {
    	//the constructor defines a null board and the position on the board to be -1 which is invalid
        private NullMove() {
            super(null, -1);
        }
        //this applies for the coordinates since there is no -1 coordinate on any part of the board
        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
        //this applies for the coordinates since there is no -1 coordinate on any part of the board
        @Override
        public int getDestinationCoordinate() {
            return -1;
        }
        //the move cannot be executed so it throws a runtime exception if the move is to be made
        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute null move!");
        }
        //if the move is a null move the toString() method returns null move
        @Override
        public String toString() {
            return "Null Move";
        }
    }
    //the move factory class is a class used to handle the moves
    public static class MoveFactory {
    	//null move is instantiated here
        private static final Move NULL_MOVE = new NullMove();
        //the constructor the move factory, it cannot be instantiated
        private MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }
        //getter method for the null move
        public static Move getNullMove() {
            return NULL_MOVE;
        }
        //the create move function used to create a move, it takes all the legal moves on the board
        //and if the move coordinates are valid, the move is created. Otherwise, the move is declared a null move
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                    move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}