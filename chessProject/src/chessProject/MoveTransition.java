package chessProject;
//a class used to transition the board from its former state when a move has not been made
//to the new redrawn board
public final class MoveTransition {
	
	//since the board before the move and the board after the move are different we have 
	//the past board and the future board here. we also have the transition move here which is the move
	//being made by a piece and its move status.
    private final Board fromBoard;
    private final Board toBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;
    
    //constructor for each of the class variables
    public MoveTransition(final Board fromBoard,
                          final Board toBoard,
                          final Move transitionMove,
                          final MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }
    //getter method for the past board
    public Board getFromBoard() {
        return this.fromBoard;
    }
  //getter method for the future board
    public Board getToBoard() {
         return this.toBoard;
    }
  //getter method for transition move
    public Move getTransitionMove() {
        return this.transitionMove;
    }
  //getter method for the move status
    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}