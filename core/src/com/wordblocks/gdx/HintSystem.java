package com.wordblocks.gdx;

import java.util.ArrayList;
import solver.LevelSolver;

/**
 * Created by a2558 on 4/30/2016.
 */
public class HintSystem {
    int numHints;
    boolean infiniteHints = true;
    ArrayList<Integer> solveOrder = null;
    int unsolvedHintIndex = 0;
    int solverHintIndex;
    int dim;
    boolean unsolvable = false;
    Game game;

    public HintSystem(Game game) {
        this.game = game;
        dim = (int)Math.pow(game.grid.length, 2);
    }

    public boolean canGetHint() {
        if (unsolvable)
            return false;
        if  ((solveOrder != null && solverHintIndex >= solveOrder.size())
                || unsolvedHintIndex >= dim)
            return false;
        if (infiniteHints || numHints > 0)
            return true;
        return false;
    }

    public int getHintIndex(){
        return Math.max(solverHintIndex, unsolvedHintIndex);
    }

    public int getHint() {
        if (!canGetHint())
            return -2;
        //player hasn't removed any words yet - so use the board generation order as the hint
        if (game.blocksLeft() == dim) {
            return getUnsolvedHint();
        }
        //need to run the solver...
        else if (solveOrder == null && !unsolvable) {
            datatypes.Level curState = new datatypes.Level();
            curState.words = game.remainingWords();
            curState.board = game.boardToList();
            solveOrder = solver.LevelSolver.Solve(curState);
            if (solveOrder == null)
                unsolvable = true;
        }
        if (unsolvable)
            return -1;
        if (solveOrder != null && solverHintIndex < solveOrder.size()) {
            return getSolverHint();
        }
        return -1;
    }

    public int getUnsolvedHint(){
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                if (game.grid[i][j].block.hintIndex == unsolvedHintIndex) {
                    unsolvedHintIndex++;
                    return game.grid[i][j].block.id;
                }
            }
        }
        return -1;
    }

    public int getSolverHint(){
        Block b = game.getBlockByID(solveOrder.get(solverHintIndex));
        if (b == null)
            return -1;

        return solveOrder.get(solverHintIndex++);
    }
}
