package solver;

import java.util.ArrayList;
import java.util.Collections;

import helpers.RowColPair;

import com.badlogic.gdx.Gdx;
import datatypes.Level;

/**
 * Created by a2558 on 4/26/2016.
 */
public class LevelSolver {
    public static ArrayList<Integer> Solve(Level level)
    {
        LevelSolver s = new LevelSolver();
        SolverBoard b = new SolverBoard(level);

        ArrayList<Integer> solution = s.solveBoard(b);
        return solution;
    }
    static int maxLoopCount = 0;
    private ArrayList<Integer> solveBoard(SolverBoard b)
    {
        ArrayList<String> shuffledWords = new ArrayList<String>(b.words);
        int count = 0;
        while(count++ < 1000)
        {
            Collections.shuffle(shuffledWords);

            SolverBoard copy = b.copy();
            copy.words = new ArrayList<String>(shuffledWords);
            copy.curCell = null;
            ArrayList<Integer> solveOrder = solve(copy);
            if (solveOrder != null)
            {
                Gdx.app.log("MyTag", "Loop count: " + count);
                if (count > maxLoopCount)
                    maxLoopCount = count;
                Gdx.app.log("MyTag", "Max count---: " + maxLoopCount);
                return solveOrder;
            }
        }
        Gdx.app.log("MyTag", "NOT FOUND: Loop count: " + count);
        return null;
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
            SolverBoard newBoard = utils.Utils.GetNewBoardState(b, item);
            ArrayList<Integer> solution = solve(newBoard);
            if (solution != null)
                return solution;
        }
        return null;
    }
}
