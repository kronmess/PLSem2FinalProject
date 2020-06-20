package chessProject;
//an enum used to check the status of a move, the 3 values here are the respective statuses
public enum MoveStatus {
	//a move is done
    DONE{
        @Override
        public boolean isDone(){
            return true;
        }
    },
    //an illegal move so it is invalid
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    //if a player is in check they cannot move their pieces without the king being safe first
    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            return false;
        }
    };
	//the abstract method that checks whether a move should be made or not
    public abstract boolean isDone();
}