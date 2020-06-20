package chessProject;


import javax.imageio.ImageIO;
import javax.swing.*;

import chessProject.Board.Builder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

//the table class we use for the gui of the game
public class Table {
	//the frame for the game
    private final JFrame gameFrame;
    //the game history panel 
    private final GameHistoryPanel gameHistoryPanel;
    //a panel that shows which pieces were taken on the board
    private final TakenPiecesPanel takenPiecesPanel;
    //the panel used to display the board
    private final BoardPanel boardPanel;
    //a log of moves which is used for the game history
    private final MoveLog moveLog;
    //the chessboard
    private Board chessBoard;
    //the tile that a piece is currently on
    private Tile sourceTile;
    //the tile that a piece is going to move to
    private Tile destinationTile;
    //the piece that is going to be moved
    private Piece humanMovedPiece;
    //the board direction
    private BoardDirection boardDirection;
    private boolean highLightlegalMoves;
    //the window dimensions for the gui program
    private final static Dimension OUTER_FRAME_DIMENSION=new Dimension(900, 800);
    //the dimensions for the chess board panel
    private final static Dimension BOARD_PANEL_DIMENTION=new Dimension(400, 350);
    //the dimensions for each tile on the board
    private final static Dimension TILE_PANEL_DIMENSION=new Dimension(50,50);
    //the path for the piece icons 
    private static String defaultPieceImagesPath="art/pieces/";
    private static String HighlightPath="art/misc/green_dot.png";
    //the default colors of the light and dark squares
    private final Color lightTileColor=Color.decode("#eeeed2");
    private final Color darkTileColor=Color.decode("#769656");
    public Table(){
    	//the constructor for the table class where all of the panels and menus are created
        this.gameFrame=new JFrame("OHChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar= createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard=Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardPanel=new BoardPanel();
        this.moveLog = new MoveLog();
        this.boardDirection=BoardDirection.NORMAL;
        this.highLightlegalMoves= false;
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.setVisible(true);
    }
    //the top bar of the gui known as the menu, it has a file menu and a preferences menu
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }
    //planned feature to add chess games by pgn file which is used to import pre-existing games of chess
    private JMenu createFileMenu() {
        final JMenu fileMenu=new JMenu("File");
        final JMenuItem openPGN= new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open up that PGN file!");
            }
        });
        fileMenu.add(openPGN);
        //an exit button that exits the program
        final JMenuItem exitMenuItem= new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }
    //the preferences menu, here the board perspective can be flipped and the legal moves of a piece can be highlighted
    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu= new JMenu("Preferences");
        //flips the board's perspective
        final JMenuItem flipBoardMenuItem=new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                boardDirection=boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
           }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        //the highlight feature is implemented as a toggle option
        final JCheckBoxMenuItem legalMoveHighLighterCheckBox = new JCheckBoxMenuItem("HighLight legal moves", false);
        legalMoveHighLighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highLightlegalMoves = legalMoveHighLighterCheckBox.isSelected();
            }
        });

        preferencesMenu.add(legalMoveHighLighterCheckBox);
        return preferencesMenu;
    }
    //an enum used to define the direction of the board
    public enum BoardDirection{
    	//the normal direction of the board on starting the program
        NORMAL{
            @Override
           List<TilePanel> traverse(final List<TilePanel> boardTiles){
                return boardTiles;
            }
            @Override
            BoardDirection opposite(){
                return FLIPPED;
            }
        },
        //the flipped direction of the board
        FLIPPED{
        	@Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                ArrayList<TilePanel> tiles = new ArrayList<TilePanel>();
                tiles.addAll(boardTiles);
                Collections.reverse(tiles);
                return tiles;
            }
            @Override
            BoardDirection opposite(){
               return NORMAL;
            }
       };
    	//an abstract method used to traverse the board, the normal board returns the board tiles but the flipped board
    	//is obtained by traversing the tiles backwards and then returning that value
        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        //an abstract method used to define the opposite of the current board direction and allows the board to be flipped
        //again and again
        abstract BoardDirection opposite();
    }
    //the board panel used to display the chess board
    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
        	//the board is being put on an 8x8 grid
            super(new GridLayout(8,8));
            this.boardTiles=new ArrayList<>();
            //keep track of each tile in the list of board tiles and also adding it to the JPanel
            for(int i=0; i< BoardUtils.NUM_TILES; i++){
                final TilePanel tilePanel=new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENTION);
            validate();
        }
        //function used to draw the current board, it calls the board direction traverse to check whether the board
        //is currently normal or flipped and then draws this on to the GUI
        public void drawBoard(final Board board){
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
            checkMate();
            staleMate();
        }
    }
    //a class that is used to store the moves previously made by a player
    public static class MoveLog{
    	private final List<Move> moves;
    	//functions used to get moves, add, return size,clear all moves, and remove moves by index or move
    	MoveLog(){
    		this.moves = new ArrayList<>();
    	}
    	public List<Move> getMoves(){
    		return this.moves;
    	}
    	public void addMove(final Move move) {
    		this.moves.add(move);
    	}
    	public int size() {
    		return this.moves.size();
    	}
    	public void clear() {
    		this.moves.clear();
    	}
    	public Move removeMove(int index) {
    		return this.moves.remove(index);
    	}
    	public boolean removeMove(final Move move) {
    		return this.moves.remove(move);
    	}
    }
    //the tile panel class where all of the tiles on the board are placed
    //each tile is assigned an id
    private class TilePanel extends JPanel{
        private final int tileId;
        TilePanel(final BoardPanel boardPanel,
                  final int tileId){
            super(new GridBagLayout());
            this.tileId=tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            highLightLegals(chessBoard);
            //here an event listener is used to check for mouse clicks to make movement
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    //here the right click button is used to de-select any tile by setting to null
                    if(isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    //if the click is a left mouse button, it retrieves the tile id and the piece, it checks whether that
                    //piece is on the board and valid to be moved or not.
                    else if(isLeftMouseButton(e)) {

                        if (sourceTile == null) {

                            
                            sourceTile = chessBoard.getTile(tileId);
                            

                            humanMovedPiece = sourceTile.getPiece();

                            if (humanMovedPiece == null) {
                                
                                sourceTile = null;
                            }
                        } else {

                            //if the tile is valid, a move can then be made, a move can be created so the piece can move from its
                        	//current tile to its destination
                            destinationTile = chessBoard.getTile(tileId);            
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), tileId);
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            //the move is then transitioned onto the new board and the player move is made
                            //the move is also added to the movelog defined earlier
                            if (transition.getMoveStatus().isDone()) {
                            	chessBoard = transition.getToBoard();
                            	moveLog.addMove(move);
                            }
                            //the tiles and pieces are then reset to null so another move can be selected
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                            //here the GUI is updated so when an action is performed, the board is redrawd and the panels are updated
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                	gameHistoryPanel.redo(chessBoard, moveLog);
                                	 takenPiecesPanel.redo(moveLog);
                                    boardPanel.drawBoard(chessBoard);
                                    
                                }
                            });
                        }
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();
        }
        //function used to draw in the tile and calls its functions to assign its icons and the icons of the pieces on the board
        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegals(board);
            validate();
            repaint();
            
        }
        //function used to highlight the legal moves on the board for a certain piece
        private void highLightLegals(final Board board){
            if (highLightlegalMoves) {
                for(final Move move : pieceLegalMoves(board)){
                    if(move.getDestinationCoordinate() == this.tileId){
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File(HighlightPath)))));
                        }catch (Exception e){
                            e.getStackTrace();
                        }
                    }
                }
            }
        }
        //function used to calculate a piece's legal moves
        private Collection<Move> pieceLegalMoves(final Board board) {
            //if alliance equals to the current player's alliance, then calculate legal moves
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        //function used to assign the icon of a piece
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            
            if(board.getTile(this.tileId).isTileOccupied()){

                
                try {
                    final BufferedImage image= ImageIO.read(new File(defaultPieceImagesPath+board.getTile(this.tileId)
                            .getPiece().getPieceAlliance().toString().substring(0, 1)+board.getTile(this.tileId).getPiece().toString()+".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //function used to assign the color of the light and dark squares
        private void assignTileColor() {
            
        	boolean isLight = ((tileId + tileId / 8) % 2 == 0);
            setBackground(isLight ? lightTileColor : darkTileColor);
        }
    }
    //game alert message when a checkmate occurs
    public void checkMate() {
    	if(chessBoard.currentPlayer().isInCheckMate()) {
    		JOptionPane.showMessageDialog(this.boardPanel,
                    "Game Over: Player " + chessBoard.currentPlayer() + " is in checkmate!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
    	}
    }
  //game alert message when a stalemate occurs
    public void staleMate() {
    	if(chessBoard.currentPlayer().isInStaleMate()) {
    		JOptionPane.showMessageDialog(this.boardPanel,
                    "Game Over: Player " + chessBoard.currentPlayer() + " is in stalemate!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
    	}
    }
}