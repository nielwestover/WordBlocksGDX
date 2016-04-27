package com.wordblocks.gdx;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

import helpers.RowColPair;

/**
 * Created by a2558 on 2/5/2016.
 */
public class Game {

    Rectangle refresh;
    Rectangle skipNext;
    public Game(Level level){
        int dim = (int)Math.sqrt(level.board.size());
        grid = new Cell[dim][dim];
        initBoard();
        answers = new ArrayList();
        for (int i = 0; i < level.words.size(); ++i)
            answers.add(new Answer(level.words.get(i)));
        initBoard(level.board);
    }

    private void initBoard(){
        for (int row = 0; row < grid.length; row ++)
            for (int col = 0; col < grid[row].length; col++)
                grid[row][col] = new Cell();
        dims = new DrawDimensions();
    }

    List<Answer> answers;
    private void initBoard(List<LetterBlock> board){
        int index = 0;
        for (int j = grid.length - 1; j >= 0; --j){
            for (int i = 0; i < grid[j].length; ++i){
                Block block = new Block();
                block.letter = board.get(index).c;
                block.hintIndex = board.get(index).h;
                index++;
                grid[i][j].block = block;
            }
        }
    }

    public DrawDimensions dims;
    public String selectedWord = "";
    public List<RowColPair> selectedChain = new ArrayList<RowColPair>();

    public Answer getSelectedWord(String selectedWord){
        for (int i = 0; i < answers.size(); ++i){
            Answer ans = answers.get(i);
            if (ans.word.equals(selectedWord) && !ans.found)
                return ans;
        }
        return null;
    }

    public void removeWordFound(){
        for (int i = 0; i < selectedChain.size(); ++i){
            RowColPair curBlock = selectedChain.get(i);
            grid[curBlock.Row][curBlock.Col].block = null;
        }
        getSelectedWord(selectedWord).found = true;
    }

    public void deselectAll(){
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                if (grid[i][j].block != null)
                    grid[i][j].block.setSelected(false);
            }
        }
        selectedWord = "";
        selectedChain = new ArrayList<RowColPair>();
    }

    public void dropBlocks(){
        boolean blockDropped = true;
        while(blockDropped) {
            blockDropped = false;
            for (int i = grid.length - 1; i >= 0; --i) {
                for (int j = grid[i].length - 1; j >= 0; --j) {
                    if (grid[i][j].block == null){
                        if (j - 1 >= 0 && grid[i][j-1].block != null) {
                            grid[i][j].block = grid[i][j-1].block;
                            grid[i][j-1].block = null;
                            blockDropped = true;
                        }
                    }
                }
            }
        }
    }

    public int blocksLeft(){
        int count = 0;
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j){
                if (grid[i][j].block != null)
                    ++count;
            }
        }
        return count;
    }



    public class Cell{
        Block block;
        Rectangle cellPos;
    }

    public class DrawDimensions{
        float screenWidth;
        float scalar;
        float boxGap;
        float padding;
        float boxRounding;
        float boxDim;
        float inset;
    }

    public class Answer{
        boolean found = false;
        String word;

        public Answer(String word){
            this.word = word;
        }
    }

    public Cell [][] grid;
}

