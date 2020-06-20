package chessProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;
import chessProject.Table.MoveLog;

import java.awt.*;
//class used that shows the move history of the players
class GameHistoryPanel extends JPanel {
	//the dimensions and scroll pane are defined here
    private final DataModel model;
    private final JScrollPane scrollPane;
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100, 40);
    
    //the constructor for the panel, the panel is drawn out as a table that scrolls as more moves are made.
    GameHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
    //the function used to check for moves and what moves have been performed by either player
    void redo(final Board board,
              final MoveLog moveHistory) {
        int currentRow = 0;
        this.model.clear();
        //after checking the moves in the move history, it separates the black and white moves by setting different values 
        //for the data model
        for (final Move move : moveHistory.getMoves()) {
            final String moveText = move.toString();
            //if the move made was made by a white piece it is given the 0 value
            if (move.getMovedPiece().getPieceAlliance().isWhite()) {
                this.model.setValueAt(moveText, currentRow, 0);
            }
            //if the move made was made by a white piece it is given the 0 value
            else if (move.getMovedPiece().getPieceAlliance().isBlack()) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }
        //if the move history contains any number of moves that is not 0, it takes the last made move and then convers it to a string
        if(moveHistory.getMoves().size() > 0) {
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            final String moveText = lastMove.toString();
            //if the last move was made by a white piece, the text for that move is set and it is checked to see whether
            //that move was a normal move or a check or a checkmate and is then drawn on to the table
            if (lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
            }
          //if the last move was made by a black piece, the text for that move is set and it is checked to see whether
            //that move was a normal move or a check or a checkmate and is then drawn on to the table
            else if (lastMove.getMovedPiece().getPieceAlliance().isBlack()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
            }
        }

        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }
    
    //here the move history will make a special symbol to signify if a certain move is a check/checkmate or a normal move
    //a normal move will have no symbol while a checkmate will have a # symbol and a check will have + symbol
    private static String calculateCheckAndCheckMateHash(final Board board) {
        if(board.currentPlayer().isInCheckMate()) {
            return "#";
        } else if(board.currentPlayer().isInCheck()) {
            return "+";
        }
        return "";
    }
    //here a row class is used for white and black moves
    private static class Row {

        private String whiteMove;
        private String blackMove;

        Row() {
        }

        public String getWhiteMove() {
            return this.whiteMove;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setWhiteMove(final String move) {
            this.whiteMove = move;
        }

        public void setBlackMove(final String move) {
            this.blackMove = move;
        }

    }
    //this is the class used for the table that is drawn on the panel
    private static class DataModel extends DefaultTableModel {

        private final List<Row> values;
        private static final String[] NAMES = {"White", "Black"};

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }
        //function that counts the number of rows
        @Override
        public int getRowCount() {
            if(this.values == null) {
                return 0;
            }
            return this.values.size();
        }
        //function that counts the number of columns
        @Override
        public int getColumnCount() {
            return NAMES.length;
        }
        //function used to retrieve the move at a row and column, the first column is used for moves by the white player
        //and the second column is used for moves by the black player
        @Override
        public Object getValueAt(final int row, final int col) {
            final Row currentRow = this.values.get(row);
            if(col == 0) {
                return currentRow.getWhiteMove();
            } else if (col == 1) {
                return currentRow.getBlackMove();
            }
            return null;
        }
        //function used to set the move at a row
        @Override
        public void setValueAt(final Object aValue,
                               final int row,
                               final int col) {
            final Row currentRow;
            if(this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }
            //the first column is set with the moves made by the white player
            if(col == 0) {
                currentRow.setWhiteMove((String) aValue);
                fireTableRowsInserted(row, row);
            }//the second column is set with the moves made by the black player 
            else  if(col == 1) {
                currentRow.setBlackMove((String)aValue);
                fireTableCellUpdated(row, col);
            }
        }
        //getter method for the column class
        @Override
        public Class<?> getColumnClass(final int col) {
            return Move.class;
        }
        //getter method for the column name
        @Override
        public String getColumnName(final int col) {
            return NAMES[col];
        }
    }
}