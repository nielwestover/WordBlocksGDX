package solver;

import com.wordblocks.gdx.LetterBlock;
import datatypes.Level;
import helpers.RowColPair;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SolverBoard {
    LetterBlock[][] board;
    public RowColPair curCell;
    public int curLetterIndex;
    public String curWord;
    public int dim;
    public ArrayList<Integer> solveOrder = new ArrayList();
    public ArrayList<String> words;

    public SolverBoard copy() {
        SolverBoard b = new SolverBoard(this.dim, this.dim);
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                b.board[i][j] = new LetterBlock(this.board[i][j]);
            }
        }
        b.words = new ArrayList(this.words);
        b.solveOrder = new ArrayList(this.solveOrder);
        if (this.curCell == null) {
            b.curCell = null;
        } else {
            b.curCell = new RowColPair(this.curCell.Row, this.curCell.Col);
        }
        b.curWord = this.curWord;
        b.curLetterIndex = this.curLetterIndex;
        return b;
    }

    public LetterBlock getCell(int row, int col) {
        if (row < 0 || row >= this.dim || col < 0 || col >= this.dim) {
            try {
                throw new Exception("Row or Col are out of bounds: " + row + ", " + col);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.board[row][col];
    }

    public SolverBoard(Level level) {
        this.dim = (int) Math.sqrt((double) level.board.size());
        this.words = new ArrayList(level.words);
        this.board = (LetterBlock[][]) Array.newInstance(LetterBlock.class, new int[]{this.dim, this.dim});
        int index = 0;
        for (int row = 0; row < this.dim; row++) {
            for (int col = 0; col < this.dim; col++) {
                this.board[row][col] = (LetterBlock) level.board.get(index);
                index++;
            }
        }
    }

    public ArrayList<RowColPair> getLetterLocationsNearby(String candidate, RowColPair cell) {
        ArrayList<RowColPair> locs = new ArrayList();
        for (int row = -1; row < 2; row++) {
            for (int col = -1; col < 2; col++) {
                int candRow = cell.Row + row;
                int candCol = cell.Col + col;
                if (candRow >= 0 && candRow < this.dim && candCol >= 0 && candCol < this.dim && this.board[candRow][candCol] != null && this.board[candRow][candCol].f83c == candidate.charAt(0) && !this.solveOrder.contains(Integer.valueOf(this.board[candRow][candCol].id))) {
                    locs.add(new RowColPair(candRow, candCol));
                }
            }
        }
        return locs;
    }

    public ArrayList<RowColPair> getLetterLocations(String letter) {
        ArrayList<RowColPair> locs = new ArrayList();
        int row = 0;
        while (row < this.dim) {
            int col = 0;
            while (col < this.dim) {
                if (this.board[row][col] != null && this.board[row][col].f83c == letter.charAt(0)) {
                    locs.add(new RowColPair(row, col));
                }
                col++;
            }
            row++;
        }
        return locs;
    }

    public SolverBoard(int rows, int cols) {
        this.board = (LetterBlock[][]) Array.newInstance(LetterBlock.class, new int[]{rows, cols});
        this.dim = rows;
    }

    public void dropWords() {
        int col = 0;
        int row = 0;
        while (col < this.dim) {
            while (row < this.dim) {
                if (this.board[row][col] != null && this.solveOrder.contains(Integer.valueOf(this.board[row][col].id))) {
                    this.board[row][col] = null;
                }
                row++;
            }
            col++;
        }
        boolean blockDropped = true;
        while (blockDropped) {
            blockDropped = false;
            col = 0;
            while (col < this.dim) {
                row = 0;
                while (row < this.dim - 1) {
                    if (this.board[row][col] == null && row + 1 < this.dim && this.board[row + 1][col] != null) {
                        this.board[row][col] = this.board[row + 1][col];
                        this.board[row + 1][col] = null;
                        blockDropped = true;
                    }
                    row++;
                }
                col++;
            }
        }
    }
}
