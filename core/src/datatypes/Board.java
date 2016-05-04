package datatypes;

import com.wordblocks.gdx.LetterBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import helpers.RowColPair;

/**
 * Created by a2558 on 4/25/2016.
 */
public class Board {

    public LetterBlock[][] board;
    int dim;
    Random rand;

    public List<LetterBlock> ToList()
    {
        List<LetterBlock> l = new ArrayList<LetterBlock>();
        int index = 0;
        for (int row = 0; row < dim; ++row)
        {
            for (int col = 0; col < dim; ++col)
            {
                board[row][col].id = index++;
                l.add(board[row][col]);
            }
        }
        return l;
    }
    public Board(int dim, long seed) {
        this.dim = dim;
        board = new LetterBlock[dim][dim];

        rand = new Random(seed);
    }

    public boolean AddAllWords(List<String> level) {
        for (String word : level) {
            if (!insertWord(word))
                return false;
        }
        return isQualityBoard(level);
    }

    private boolean isQualityBoard(List<String> level) {
        for (String word : level) {
            {
                if (word.length() <= dim && wordEasyToFind(word))
                    return false;
            }
        }
        return true;

    }

    private boolean wordEasyToFind(String item) {
        for (int row = 0; row < dim; ++row) {
            String rowWord = "";
            for (int col = 0; col < dim; ++col) {
                rowWord += board[row][col].c;
            }
            if (rowWord.contains(item) || utils.Utils.ReverseString(rowWord).contains(item))
                return true;
        }
        for (int col = 0; col < dim; ++col) {
            String colWord = "";
            for (int row = 0; row < dim; ++row) {
                colWord += board[row][col].c;
            }
            if (colWord.contains(item) || utils.Utils.ReverseString(colWord).contains(item))
                return true;
        }
        return false;
    }


    public void reset() {
        hintIndex = dim * dim - 1;
        for (int row = 0; row < dim; ++row) {
            for (int col = 0; col < dim; ++col) {
                board[row][col] = null;
            }
        }
    }

//    public void print() {
//        //row
//        for (int row = dim - 1; row >= 0; --row) {
//            //col
//            for (int col = 0; col < dim; ++col) {
//                if (board[row,col]!=null)
//                System.Console.Write(board[row, col].c);
//                else
//                System.Console.Write(".");
//            }
//            System.console().
//        }
//        System.Console.WriteLine();
//    }

    private int RandomNumber(int min, int count) {
        int n = rand.nextInt(count - min) + min;
        return n;
    }

    private boolean insertWord(String word) {
        //start with a random location
        RowColPair cell = new RowColPair(RandomNumber(0, dim), RandomNumber(0, dim));
        //we're building the level from the final state to the initial state, so iterate over the letters in reverse order
        for (int i = word.length() - 1; i >= 0; --i) {
            cell = getNextCell(cell, word);
            //Got into a bad state, so bail and start over!
            if (cell == null)
                return false;
            insertCharacter(word.charAt(i), word, cell);
        }
        return true;
    }



    int hintIndex;

    private void insertCharacter(char ch, String word, RowColPair cell) {
        if (board[cell.Row][cell.Col] != null) {
            //raise the characters to make room
            int curRow = dim - 1;
            while (curRow != cell.Row) {
                if (board[curRow][cell.Col] == null && board[curRow - 1][cell.Col] != null) {
                    board[curRow][cell.Col] = board[curRow - 1][cell.Col];
                    board[curRow - 1][cell.Col] = null;
                }
                --curRow;
            }
        }
        LetterBlock lb = new LetterBlock();
        lb.c = ch;
        lb.h = hintIndex--;
        lb.word = word;

        board[cell.Row][cell.Col] = lb;
    }

    private RowColPair getNextCell(RowColPair cell, String word) {
        int count = 0;
        while (++count < 50) {
            RowColPair newCell = getRandomAdjacentCell(cell);
            if (cellIsValid(newCell, word))
                return newCell;
        }
        return null;
    }

    private boolean cellIsValid(RowColPair cell, String word) {
        //check it's inside grid
        if (cell.Row >= dim || cell.Row < 0 || cell.Col >= dim || cell.Col < 0)
            return false;

        //check there is no null space immediately below the cell
        // -- you can only build up on top of other blocks
        if (cell.Row > 0 && board[cell.Row - 1][cell.Col] == null)
            return false;

        //can push up ONLY if there is space - if top space is null, then there's room.  If not, return false
        if (board[dim - 1][cell.Col] != null)
            return false;
        //AND if no Cells above contain the current word (or it will be unsolvable!)
        for (int row = dim - 1; row >= cell.Row; --row) {
            //null cells are good for business, move along
            if (board[row][cell.Col] == null)
                continue;
            //not valid!
            if (board[row][cell.Col].word.equals(word))
                return false;
        }
        return true;
    }

    //returns 1 of 9 possible locations
    private RowColPair getRandomAdjacentCell(RowColPair cell) {
        int pos = RandomNumber(0, 9);
        switch (pos) {
            case 0:
                return new RowColPair(cell.Row - 1, cell.Col - 1);
            case 1:
                return new RowColPair(cell.Row - 1, cell.Col);
            case 2:
                return new RowColPair(cell.Row - 1, cell.Col + 1);
            case 3:
                return new RowColPair(cell.Row, cell.Col - 1);
            case 4:
                return new RowColPair(cell.Row, cell.Col);
            case 5:
                return new RowColPair(cell.Row, cell.Col + 1);
            case 6:
                return new RowColPair(cell.Row + 1, cell.Col - 1);
            case 7:
                return new RowColPair(cell.Row + 1, cell.Col);
            case 8:
                return new RowColPair(cell.Row + 1, cell.Col + 1);
            default:
                return null;
        }
    }
    //SOLVER FUNCTIONS


}
