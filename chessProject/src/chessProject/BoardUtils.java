package chessProject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
//A utility class used to define parts of the board
public class BoardUtils {
	
	//Here the columns are initialized through a function
	//they are initialized as part of the exclusions used for the pieces
	//when handling edge cases,the board is also already drawn so
	//not all of the columns are initialized.
	//
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    
    //Here each of the rows within the chess board are initialized
    //the ranks are mostly used for certain type of moves. For example,
    //columns the eigth rank and first rank are used for pawn promotion
    //the other ranks are also used for the pawn jumps move
    
    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);
    
    //Within chess the tiles are not numbered from 1 to 64 but instead are called upon by their file then their rank
    //For example, a rook is located on a1 of the chessboard,this continues until h8. This function returns an array
    //of strings containing all of the notations for each tile
    
    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    
    //This function is then used to put a the notations into a hashmap indexed from 1 to 64 with its values assigned to their
    //respective notations. We can then use the notations for developing our move history in the gui
    
    public static final Map <String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
    
    //This variable describes the amount of tiles on a chessboard which is 64
    
    public static final int NUM_TILES = 64;
    
    //This variable describes the amount of tiles on a chessboard per row which is 8
    
    public static final int NUM_TILES_PER_ROW=8;
    //Constructor for board utils, a runtime exception is used if the class is initiated.
    private BoardUtils() {
        throw new RuntimeException("This class cannot be instantiated!");
    }
    //Function that returns the notations in a string of arrays
    private static String[] initializeAlgebraicNotation() {
        return new String[] {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }
    //Function assigning the notations to the tiles using a hashmap
    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++){
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }
    //function used to initialize a column
    private static boolean[] initColumn(int columnNumber){
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber]=true;
            columnNumber += NUM_TILES_PER_ROW;
        }while(columnNumber<NUM_TILES);
        return column;
    }
  //function used to initialize a row
    private static boolean[] initRow(int rowNumber){
        final boolean[] row=new boolean[NUM_TILES];
        do{
            row[rowNumber]=true;
            rowNumber++;
        }while(rowNumber % NUM_TILES_PER_ROW !=0);
        return row;
    }
    //a function which checks whether a tile corresponds to the coordinate on the chess board
    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >=0 && coordinate <NUM_TILES;
    }
    //a function used to obtain the coordinate of a piece using the notation value
	public static int getPositionAtCoordinate(final String position) {
		return POSITION_TO_COORDINATE.get(position);
	}
	//a function used to obtain the notation value using a coordinate
	public static String getPositionAtCoordinate(final int coordinate) {
		return ALGEBRAIC_NOTATION[coordinate];
	}
	

}
