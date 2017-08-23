package solver;

import com.badlogic.gdx.Gdx;
import datatypes.Level;
import helpers.RowColPair;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import utils.Utils;

public class LevelSolver {
    static int maxLoopCount = 0;

    public static ArrayList<Integer> Solve(Level level) {
        return new LevelSolver().solveBoard(new SolverBoard(level));
    }

    void permute(ArrayList<String> arr, int k){
        if (permutations.size() >= 500)
            return;
        for(int i = k; i < arr.size(); i++){
            Collections.swap(arr, i, k);
            permute(arr, k+1);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            permutations.add(new ArrayList<String>(arr));
        }
    }
    ArrayList<List<String>> permutations;
    private ArrayList<Integer> solveBoard(SolverBoard b) {
        int count;
        ArrayList<Integer> solveOrder;
        ArrayList<String> words = new ArrayList(b.words);
        Collections.reverse(words);
        permutations = new ArrayList<List<String>>();
        permute(words, 0);
        int i = 0;
        while (true) {
            count = i + 1;
            if (i < permutations.size()) {
                List<String> curPerm = permutations.get(i);
                SolverBoard copy = b.copy();
                copy.words = new ArrayList<String>(curPerm);
                copy.curCell = null;
                solveOrder = solve(copy);
                if (solveOrder != null) {
                    break;
                }
                i = count;
            } else {
                Gdx.app.log("MyTag", "NOT FOUND: Loop count: " + count);
                return null;
            }
        }
        Gdx.app.log("MyTag", "Loop count: " + count);
        if (count > maxLoopCount) {
            maxLoopCount = count;
        }
        Gdx.app.log("MyTag", "Max count---: " + maxLoopCount);
        return solveOrder;
    }
//    private ArrayList<Integer> solveBoard(SolverBoard b) {
//        int count;
//        ArrayList<Integer> solveOrder;
//        ArrayList<String> shuffledWords = new ArrayList(b.words);
//        int i = 0;
//        while (true) {
//            count = i + 1;
//            if (i < 100) {
//                System.out.println("");
//                //Collections.shuffle(shuffledWords);
//                //Collections.reverse(shuffledWords);
//                SolverBoard copy = b.copy();
//                copy.words = new ArrayList(shuffledWords);
//                copy.curCell = null;
//                solveOrder = solve(copy);
//                if (solveOrder != null) {
//                    break;
//                }
//                i = count;
//            } else {
//                Gdx.app.log("MyTag", "NOT FOUND: Loop count: " + count);
//                return null;
//            }
//        }
//        Gdx.app.log("MyTag", "Loop count: " + count);
//        if (count > maxLoopCount) {
//            maxLoopCount = count;
//        }
//        Gdx.app.log("MyTag", "Max count---: " + maxLoopCount);
//        return solveOrder;
//    }
    int iterCount = 0;
    public ArrayList<Integer> solve(SolverBoard b) {
        if (b.words.get(0).length() == 0) {
            b.words.remove(0);
            b.curCell = null;
            b.dropWords();
        }
        //iterCount++;
        //b.print(iterCount);
        if (b.words.size() == 0) {
            return b.solveOrder;
        }
        ArrayList<RowColPair> startLocations;
        String candidate = ((String) b.words.get(0)).substring(0, 1);
        if (b.curCell == null) {
            b.curWord = (String) b.words.get(0);
            b.curLetterIndex = 0;
            startLocations = b.getLetterLocations(candidate);
        } else {
            b.curLetterIndex++;
            startLocations = b.getLetterLocationsNearby(candidate, b.curCell);
        }
        Iterator it = startLocations.iterator();
        while (it.hasNext()) {
            ArrayList<Integer> solution = solve(Utils.GetNewBoardState(b, (RowColPair) it.next()));
            if (solution != null) {
                return solution;
            }
        }
        return null;
    }
}
