package chessProject;
// An enum is used here to define the black side and the white side in the game of chess.
// All functions defined within this enum are abstract so they can be overridden within the black and white enum values

public enum Alliance {
    WHITE {
    	
        @Override
        public int getDirection() {
            return -1;
        }
      
        @Override
        public int getOppositeDirection() {
            return 1;
        }
        
        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
        @Override
        public String toString() {
            return "White";
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
        @Override
        public String toString() {
            return "Black";
        }
    };

	//function used to obtain the direction since the white pieces must move towards the black pieces
    public abstract int getDirection();
    
    //function used to obtain the opposite to allow backwards movement
    public abstract int getOppositeDirection();
    
    //a function later used to check whether a piece on the board is a white piece
    public abstract boolean isWhite();
    
    //a function later used to check whether a piece on the board is a black piece
    public abstract boolean isBlack();
    
    //a function used to check whether the following square is a square that is valid for pawn promotion
    //This square would be the square on the opposite side of the board for each side
    public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}