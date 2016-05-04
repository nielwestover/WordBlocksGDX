package solver;

import com.wordblocks.gdx.LetterBlock;
import datatypes.Level;

import java.util.ArrayList;

import helpers.RowColPair;

/**
 * Created by a2558 on 4/26/2016.
 */
public class SolverBoard {

    public ArrayList<Integer> solveOrder = new ArrayList<Integer>();
    public ArrayList<String> words;
    public RowColPair curCell;

    public int dim;
    LetterBlock[][] board;
    public String curWord;
    public int curLetterIndex;

    public SolverBoard copy() {
        SolverBoard b = new SolverBoard(dim, dim);

        for(int i = 0; i < board.length; i++)
            for(int j = 0; j < board.length; j++)
                b.board[i][j] = new LetterBlock(board[i][j]);
        //System.arraycopy(board, 0, b.board, 0, board.length);
        b.words = new ArrayList<String>(words);
        b.solveOrder = new ArrayList<Integer>(solveOrder);
        if (curCell == null)
            b.curCell = null;
        else
            b.curCell = new RowColPair(curCell.Row, curCell.Col);
        b.curWord = curWord;
        b.curLetterIndex = curLetterIndex;
        //b.uniqueLetters = uniqueLetters;
        return b;
    }

    public LetterBlock getCell(int row, int col) {
        if (row < 0 || row >= dim || col < 0 || col >= dim)
            try {
                throw new Exception("Row or Col are out of bounds: " + row + ", " + col);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return board[row][col];
    }

    public SolverBoard(Level level) {
        dim = (int) Math.sqrt((double) level.board.size());

        words = new ArrayList<String>(level.words);
        board = new LetterBlock[dim][dim];

        int index = 0;
        for (int row = 0; row < dim; ++row) {
            for (int col = 0; col < dim; ++col) {
                //LetterBlock c = new LetterBlock();
                //c.c = level.board.get(index).c;
                //c.id = index;
                board[row][col] = level.board.get(index);
                index++;

            }
        }
    }

    public ArrayList<RowColPair> getLetterLocationsNearby(String candidate, RowColPair cell) {
        //RowColPair uniqueLoc = null;
        //if (uniqueLetters[curWord] != null)
        //	getLocationByID(uniqueLetters[curWord].id);
        ArrayList<RowColPair> locs = new ArrayList<RowColPair>();
        for (int row = -1; row < 2; ++row) {
            for (int col = -1; col < 2; ++col) {
                int candRow = cell.Row + row;
                int candCol = cell.Col + col;
                if (candRow >= 0 && candRow < dim &&
                        candCol >= 0 && candCol < dim &&
                        board[candRow][candCol] != null &&
                        board[candRow][candCol].c == candidate.charAt(0) &&
                        !solveOrder.contains(board[candRow][candCol].id)) {
                    //if (uniqueLoc == null || closeEnoughToUniqueLetter(uniqueLoc, candRow, candCol))
                    locs.add(new RowColPair(candRow, candCol));
                }
            }
        }
        return locs;
    }

    public ArrayList<RowColPair> getLetterLocations(String letter) {
        //RowColPair uniqueLoc = null;
        ArrayList<RowColPair> locs = new ArrayList<RowColPair>();
        for (int row = 0; row < dim; ++row) {
            for (int col = 0; col < dim; ++col) {
                if (board[row][col] != null && board[row][col].c == letter.charAt(0)) {
                    //if (uniqueLoc == null || closeEnoughToUniqueLetter(uniqueLoc, row, col))
                    locs.add(new RowColPair(row, col));
                }
            }
        }
        return locs;
    }

    public SolverBoard(int rows, int cols) {
        board = new LetterBlock[rows][cols];
        dim = rows;
    }

    public void dropWords() {
        for (int col = 0; col < dim; ++col) {
            for (int row = 0; row < dim; ++row) {
                if (board[row][col] != null && solveOrder.contains(board[row][col].id))
                    board[row][col] = null;
            }
        }
        boolean blockDropped = true;
        while (blockDropped) {
            blockDropped = false;
            for (int col = 0; col < dim; ++col) {
                for (int row = 0; row < dim - 1; ++row) {
                    if (board[row][col] == null) {
                        if (row + 1 < dim && board[row + 1][col] != null) {
                            board[row][col] = board[row + 1][col];
                            board[row + 1][col] = null;
                            blockDropped = true;
                        }
                    }
                }
            }
        }
    }
}
