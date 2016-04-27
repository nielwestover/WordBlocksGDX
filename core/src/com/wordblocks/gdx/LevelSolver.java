package com.wordblocks.gdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helpers.RowColPair;

/**
 * Created by a2558 on 4/26/2016.
 */
public class LevelSolver {
    static ArrayList<Integer> Solve(Level level)
    {
        LevelSolver s = new LevelSolver();
        SolverBoard b = new SolverBoard(level);

        ArrayList<Integer> solution = s.solveBoard(b);
        return solution;
    }

    private ArrayList<Integer> solveBoard(SolverBoard b)
    {
        ArrayList<String> shuffledWords = new ArrayList<String>(b.words);
        while(true)
        {
            Collections.shuffle(shuffledWords);

            ArrayList<Integer> curWordOrder = new ArrayList<Integer>();
            SolverBoard copy = b.copy();
            copy.words = new ArrayList<String>(shuffledWords);
            copy.curCell = null;
            ArrayList<Integer> solveOrder = solve(copy);
            if (solveOrder != null)
            {
                return solveOrder;
            }
        }
    }
    public ArrayList<Integer> solve(SolverBoard b)
    {
        if (b.words.get(0).isEmpty())
        {
            b.words.remove(0);
            b.curCell = null;//starting new word
            b.dropWords();
        }
        //first check if end condition is satisfied
        if (b.words.size() == 0)
        {
            return b.solveOrder;
        }
        //candidate is first letter of first word
        String candidate = b.words.get(0).substring(0, 1);

        ArrayList<RowColPair> startLocations;
        //new word, look at all candidate locations
        if (b.curCell == null)
        {
            b.curWord = b.words.get(0);
            b.curLetterIndex = 0;
            startLocations = b.getLetterLocations(candidate);
        }
        //not new word, look at all letters around curCell
        else
        {
            ++b.curLetterIndex;
            startLocations = b.getLetterLocationsNearby(candidate, b.curCell);
        }
        //remove dead-end paths
        //startLocations = b.removeBadLocations(startLocations);

        for (RowColPair item: startLocations) {
            SolverBoard newBoard = Utils.GetNewBoardState(b, item);
            ArrayList<Integer> solution = solve(newBoard);
            if (solution != null)
                return solution;
        }
        return null;
    }
}
