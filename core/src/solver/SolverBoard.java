package solver;

import com.wordblocks.gdx.LetterBlock;

import datatypes.Level;
import helpers.RowColPair;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.System.in;

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
                if (candRow >= 0 && candRow < this.dim && candCol >= 0 && candCol < this.dim && this.board[candRow][candCol] != null && this.board[candRow][candCol].c == candidate.charAt(0) && !this.solveOrder.contains(Integer.valueOf(this.board[candRow][candCol].id))) {
                    locs.add(new RowColPair(candRow, candCol));
                }
            }
        }
        return locs;
    }

    public ArrayList<RowColPair> getLetterLocations(String letter) {
        ArrayList<RowColPair> locs = new ArrayList();
        for (int row = 0; row < dim; ++row) {
            for (int col = 0; col < dim; ++col) {
                if (this.board[row][col] != null && this.board[row][col].c == letter.charAt(0)) {
                    locs.add(new RowColPair(row, col));
                }
            }
        }
        return locs;
    }

    public SolverBoard(int rows, int cols) {
        this.board = (LetterBlock[][]) Array.newInstance(LetterBlock.class, new int[]{rows, cols});
        this.dim = rows;
    }

    public void dropWords() {
        for (int col = 0; col < this.dim; ++col)
        {
            for (int row = 0; row < this.dim; ++row)
            {
                if (this.board[row][col] != null && this.solveOrder.contains(Integer.valueOf(this.board[row][col].id))) {
                    this.board[row][col] = null;
                }
            }
        }
        boolean blockDropped = true;
        while (blockDropped) {
            blockDropped = false;
            for (int col = 0; col < this.dim; ++col)
            {
                for (int row = 0; row < this.dim - 1; ++row)
                {
                    if (this.board[row][col] == null && row + 1 < this.dim && this.board[row + 1][col] != null) {
                        this.board[row][col] = this.board[row + 1][col];
                        this.board[row + 1][col] = null;
                        blockDropped = true;
                    }
                }
            }
        }
    }

    public void printBoard() {
        //System.Console.WriteLine("******************************");
        for (int i = 0; i < words.size(); ++i) {
            System.out.print(words.get(i) + " ");
        }
        System.out.println();
        //row
        for (int row = dim - 1; row >= 0; --row) {
            //col
            for (int col = 0; col < dim; ++col) {
                if (board[row][col] != null)
                    if (curCell != null && curCell.equals(new RowColPair(row, col)))
                        System.out.print("*" + board[row][col].c + "*");
                    else if (solveOrder.indexOf(board[row][col].id) >= 0)
                        System.out.print("-" + board[row][col].c + "-");
                    else
                        System.out.print(" " + board[row][col].c + " ");
                else
                    System.out.print(" . ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void print(int iter) {
        System.out.println("Iteration: " + iter);
        printBoard();
        if (curCell != null)
            System.out.println("CurCell: " + curCell.Row + ", " + curCell.Col);
        System.out.println("CurLetterIndex: " + curLetterIndex);
        System.out.println("CurWord: " + curWord);
        //System.out.println("CurLetter: " + words.get(0).substring(0, 1));
        System.out.println();
    }
}
