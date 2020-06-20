package chessProject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//
//Tile is an abstract class so that child classes EmptyTile and OccupiedTile 
//can inherit the methods within the tile class 
//toString method for black - lowercase while white is uppercase

public abstract class Tile {
	
	//A chess tile has its own coordinate assigned to it
	
    protected final int tileCoordinate;
    
    //Here a cache of empty tiles using a map is generated
    
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE=createAllPossibleEmptyTiles();
    
    //The function is described here as a hash map is used. the integer value i is assigned the value 0
    //and the hashmap puts i as the index and each of the empty tiles until the number i reaches the number 63 where it stops.
    //The number 64 is the total amount of tiles on an 8x8 chessboard.
    
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles(){
        final Map<Integer, EmptyTile> emptyTileMap=new HashMap<>();
        for(int i=0;i<BoardUtils.NUM_TILES;i++){
            emptyTileMap.put(i, new EmptyTile(1));
        }
        //the map is then stored as an immutable map.
        
        return Collections.unmodifiableMap(emptyTileMap);
  
    }
    //this function is used to create the tile
    //a ternary expression is used here, if the piece is not null then a new tile is created otherwise we get the coordinate
    //of the empty tile
    
    public static Tile createTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    //Constructor of the tile, has its own coordinate
    
    private Tile(final int tileCoordinate) {
        this.tileCoordinate=tileCoordinate;
    }
    //An abstract method used to check whether a piece occupies a tile or not
    
    public abstract boolean isTileOccupied();
    //An abstract method used to get the piece on a given tile
    
    public abstract Piece getPiece();
    //A getter method used to return the coordinate of a tile
    
    public int getTileCoordinate(){
        return this.tileCoordinate;
    }
    //A class that inherits from the tiles class, it represents a tile that does not have a piece on it
    //or that it is empty
    
    public static final class EmptyTile extends Tile{
        private EmptyTile(final int coordinate) {
            super(coordinate);
        }
        @Override
        public String toString(){
            return"-";
        }
        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }
  //A class that inherits from the tiles class, it represents a tile that has a piece on it
    
    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;
        private OccupiedTile(int tileCoordinate, final Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile=pieceOnTile;
        }
        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack()? getPiece().toString().toLowerCase():
                   getPiece().toString();
        }
        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
