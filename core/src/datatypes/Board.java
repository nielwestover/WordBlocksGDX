package datatypes;

import com.wordblocks.gdx.BuildConfig;
import com.wordblocks.gdx.LetterBlock;
import helpers.RowColPair;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import utils.Utils;

public class Board {
    public LetterBlock[][] board;
    int dim;
    int hintIndex;
    Random rand;

    public List<LetterBlock> ToList() {
        List<LetterBlock> l = new ArrayList();
        int index = 0;
        for (int row = 0; row < this.dim; row++) {
            int col = 0;
            while (col < this.dim) {
                int index2 = index + 1;
                this.board[row][col].id = index;
                l.add(this.board[row][col]);
                col++;
                index = index2;
            }
        }
        return l;
    }

    public Board(int dim, long seed) {
        this.dim = dim;
        this.board = (LetterBlock[][]) Array.newInstance(LetterBlock.class, new int[]{dim, dim});
        this.rand = new Random(seed);
    }

    public boolean AddAllWords(List<String> level) {
        for (String word : level) {
            if (!insertWord(word)) {
                return false;
            }
        }
        return isQualityBoard(level);
    }

    private boolean isQualityBoard(List<String> level) {
        for (String word : level) {
            if (word.length() <= this.dim && wordEasyToFind(word)) {
                return false;
            }
        }
        return true;
    }

    private boolean wordEasyToFind(String item) {
        int row;
        int col;
        for (row = 0; row < this.dim; row++) {
            String rowWord = BuildConfig.FLAVOR;
            for (col = 0; col < this.dim; col++) {
                rowWord = rowWord + this.board[row][col].f83c;
            }
            if (rowWord.contains(item) || Utils.ReverseString(rowWord).contains(item)) {
                return true;
            }
        }
        for (col = 0; col < this.dim; col++) {
            String colWord = BuildConfig.FLAVOR;
            for (row = 0; row < this.dim; row++) {
                colWord = colWord + this.board[row][col].f83c;
            }
            if (colWord.contains(item) || Utils.ReverseString(colWord).contains(item)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        this.hintIndex = (this.dim * this.dim) - 1;
        for (int row = 0; row < this.dim; row++) {
            for (int col = 0; col < this.dim; col++) {
                this.board[row][col] = null;
            }
        }
    }

    private int RandomNumber(int min, int count) {
        return this.rand.nextInt(count - min) + min;
    }

    private boolean insertWord(String word) {
        RowColPair cell = new RowColPair(RandomNumber(0, this.dim), RandomNumber(0, this.dim));
        for (int i = word.length() - 1; i >= 0; i--) {
            cell = getNextCell(cell, word);
            if (cell == null) {
                return false;
            }
            insertCharacter(word.charAt(i), word, cell);
        }
        return true;
    }

    private void insertCharacter(char ch, String word, RowColPair cell) {
        if (this.board[cell.Row][cell.Col] != null) {
            int curRow = this.dim - 1;
            while (curRow != cell.Row) {
                if (this.board[curRow][cell.Col] == null && this.board[curRow - 1][cell.Col] != null) {
                    this.board[curRow][cell.Col] = this.board[curRow - 1][cell.Col];
                    this.board[curRow - 1][cell.Col] = null;
                }
                curRow--;
            }
        }
        LetterBlock lb = new LetterBlock();
        lb.f83c = ch;
        int i = this.hintIndex;
        this.hintIndex = i - 1;
        lb.f84h = i;
        lb.word = word;
        this.board[cell.Row][cell.Col] = lb;
    }

    private RowColPair getNextCell(RowColPair cell, String word) {
        RowColPair newCell;
        int count = 0;
        do {
            count++;
            if (count >= 50) {
                return null;
            }
            newCell = getRandomAdjacentCell(cell);
        } while (!cellIsValid(newCell, word));
        return newCell;
    }

    private boolean cellIsValid(RowColPair cell, String word) {
        if (cell.Row >= this.dim || cell.Row < 0 || cell.Col >= this.dim || cell.Col < 0) {
            return false;
        }
        if ((cell.Row > 0 && this.board[cell.Row - 1][cell.Col] == null) || this.board[this.dim - 1][cell.Col] != null) {
            return false;
        }
        int row = this.dim - 1;
        while (row >= cell.Row) {
            if (this.board[row][cell.Col] != null && this.board[row][cell.Col].word.equals(word)) {
                return false;
            }
            row--;
        }
        return true;
    }

    private RowColPair getRandomAdjacentCell(RowColPair cell) {
        switch (RandomNumber(0, 9)) {
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
}
