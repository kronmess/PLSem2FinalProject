package chessProject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import chessProject.Table.MoveLog;
//the class used to show the pieces that have been taken on the board 
public class TakenPiecesPanel extends JPanel{
	//here two panels are defined, a north and south panel for the black and white pieces respectively.
    private final JPanel northPanel;
    private final JPanel southPanel;
    //the panel color, taken pieces size and border are defined here
    private static final Color PANEL_COLOR = Color.decode("#f5f5dc");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(30, 60);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    //here is the constructor for the panel , the grid is 8x2 for each since there are 16 pieces on each side
    //of a chess board and no more than that, the backgrounds and layouts are then set.
    public TakenPiecesPanel(){
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }
    //this function is used to update the move log 
    public void redo(final MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        //list of the white and black taken pieces
        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();
        //a for loop that checks if the piece that was taken was a black or white piece
        //if the move was a white piece it would be added to the white taken pieces and vice-versa
        for(final Move move : moveLog.getMoves()){
            if (move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }else if (takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }else{
                    throw new RuntimeException("invalid alliance");
                }
            }
        }
        //traversing through each of the white taken pieces, it draws the taken pieces onto the panel, the white pieces taken 
        //will then appear on the south side or bottom side of the panel.
        for (final Piece takenPiece : whiteTakenPieces){
            try {
                File f = new File("art/pieces/" + takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString() + ".png");
                final BufferedImage image = ImageIO.read(f);
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            }catch(final IOException e){
                e.printStackTrace();;
            }
        }
        //traversing through each of the black taken pieces, it draws the taken pieces onto the panel, the black pieces taken 
        //will then appear on the south side or bottom side of the panel.
        for (final Piece takenPiece : blackTakenPieces){
            try {
                File f = new File("art/pieces/" + takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString() + ".png");
                final BufferedImage image = ImageIO.read(f);
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            }catch(final IOException e){
                e.printStackTrace();;
            }
        }
        validate();
    }
}
