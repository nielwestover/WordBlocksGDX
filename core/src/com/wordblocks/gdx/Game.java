package com.wordblocks.gdx;

import com.badlogic.gdx.math.Rectangle;
import com.wordblocks.gdx.BlockAnimation.BlockAnimType;
import datatypes.Level;
import helpers.RowColPair;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Game {
    public ArrayList<Answer> answers;
    BlockAnimType blockAnimType;
    public DrawDimensions dims;
    Rectangle giveHint;
    public Cell[][] grid;
    Rectangle refresh;
    public List<RowColPair> selectedChain = new ArrayList();
    public String selectedWord = BuildConfig.FLAVOR;

    public class Answer {
        public boolean found = false;
        public String word;

        public Answer(String word) {
            this.word = word;
        }
    }

    public class Cell {
        Block block;
        Rectangle cellPos;
    }

    public class DrawDimensions {
        float boxDim;
        float boxGap;
        float inset;
        float padding;
        float screenWidth;
        float topPadding;
    }

    public Game(Level level) {
        int dim = (int) Math.sqrt((double) level.board.size());
        this.blockAnimType = BlockAnimType.values()[new Random().nextInt(BlockAnimType.values().length)];
        this.grid = (Cell[][]) Array.newInstance(Cell.class, new int[]{dim, dim});
        initBoard();
        this.answers = new ArrayList();
        for (int i = 0; i < level.words.size(); i++) {
            this.answers.add(new Answer((String) level.words.get(i)));
        }
        initBoard(level.board);
    }

    private void initBoard() {
        for (int row = 0; row < this.grid.length; row++) {
            for (int col = 0; col < this.grid[row].length; col++) {
                this.grid[row][col] = new Cell();
            }
        }
        this.dims = new DrawDimensions();
    }

    private void initBoard(List<LetterBlock> board) {
        int index = 0;
        for (int j = this.grid.length - 1; j >= 0; j--) {
            for (int i = 0; i < this.grid[j].length; i++) {
                Block block = new Block(((LetterBlock) board.get(index)).id, this);
                block.letter = ((LetterBlock) board.get(index)).c;
                block.hintIndex = ((LetterBlock) board.get(index)).h;
                this.grid[i][j].block = block;
                index++;
            }
        }
    }

    public Answer getSelectedWord(String selectedWord) {
        for (int i = 0; i < this.answers.size(); i++) {
            Answer ans = (Answer) this.answers.get(i);
            if (ans.word.equals(selectedWord) && !ans.found) {
                return ans;
            }
        }
        return null;
    }

    public void removeWordFound() {
        for (int i = 0; i < this.selectedChain.size(); i++) {
            RowColPair curBlock = (RowColPair) this.selectedChain.get(i);
            this.grid[curBlock.Row][curBlock.Col].block = null;
        }
    }

    public void deselectAll() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.grid[i][j].block != null) {
                    this.grid[i][j].block.setSelected(false);
                }
            }
        }
        this.selectedWord = BuildConfig.FLAVOR;
        this.selectedChain = new ArrayList();
    }

    public void dropBlocks() {
        boolean blockDropped = true;
        while (blockDropped) {
            blockDropped = false;
            int i = this.grid.length - 1;
            while (i >= 0) {
                int j = this.grid[i].length - 1;
                while (j >= 0) {
                    if (this.grid[i][j].block == null && j - 1 >= 0 && this.grid[i][j - 1].block != null) {
                        this.grid[i][j].block = this.grid[i][j - 1].block;
                        this.grid[i][j - 1].block = null;
                        blockDropped = true;
                    }
                    j--;
                }
                i--;
            }
        }
    }

    public int blocksLeft() {
        int count = 0;
        for (int i = 0; i < this.grid.length; i++) {
            for (Cell cell : this.grid[i]) {
                if (cell.block != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public Block getBlockByID(int id) {
        int i = 0;
        while (i < this.grid.length) {
            int j = 0;
            while (j < this.grid[i].length) {
                if (this.grid[i][j].block != null && this.grid[i][j].block.id == id) {
                    return this.grid[i][j].block;
                }
                j++;
            }
            i++;
        }
        return null;
    }

    public ArrayList<String> remainingWords() {
        ArrayList remaining = new ArrayList();
        Iterator it = this.answers.iterator();
        while (it.hasNext()) {
            Answer ans = (Answer) it.next();
            if (!ans.found) {
                remaining.add(ans.word);
            }
        }
        return remaining;
    }

    public ArrayList<LetterBlock> boardToList() {
        ArrayList<LetterBlock> board = new ArrayList();
        for (int j = this.grid.length - 1; j >= 0; j--) {
            for (int i = 0; i < this.grid[j].length; i++) {
                LetterBlock l;
                if (this.grid[i][j].block == null) {
                    l = null;
                } else {
                    l = new LetterBlock();
                    l.id = this.grid[i][j].block.id;
                    l.c = this.grid[i][j].block.letter;
                }
                board.add(l);
            }
        }
        return board;
    }
}
