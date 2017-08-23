package com.wordblocks.gdx;

import datatypes.Level;
import java.util.ArrayList;
import solver.LevelSolver;

public class HintSystem {
    int dim;
    Game game;
    boolean infiniteHints = true;
    int numHints;
    ArrayList<Integer> solveOrder = null;
    int solverHintIndex;
    boolean unsolvable = false;
    int unsolvedHintIndex = 0;

    public HintSystem(Game game) {
        this.game = game;
        this.dim = (int) Math.pow((double) game.grid.length, 2.0d);
    }

    public boolean canGetHint() {
        if (this.unsolvable) {
            return false;
        }
        if ((this.solveOrder != null && this.solverHintIndex >= this.solveOrder.size()) || this.unsolvedHintIndex >= this.dim) {
            return false;
        }
        if (this.infiniteHints || this.numHints > 0) {
            return true;
        }
        return false;
    }

    public int getHintIndex() {
        return Math.max(this.solverHintIndex, this.unsolvedHintIndex);
    }

    public int getHint() {
        if (!canGetHint()) {
            return -2;
        }
        //if (this.game.blocksLeft() == this.dim) {
        //    return getUnsolvedHint();
        //}
        if (this.solveOrder == null && !this.unsolvable) {
            Level curState = new Level();
            curState.words = this.game.remainingWords();
            curState.board = this.game.boardToList();
            this.solveOrder = LevelSolver.Solve(curState);
            if (this.solveOrder == null) {
                this.unsolvable = true;
            }
        }
        if (this.unsolvable || this.solveOrder == null || this.solverHintIndex >= this.solveOrder.size()) {
            return -1;
        }
        return getSolverHint();
    }

    public int getUnsolvedHint() {
        int i = 0;
        while (i < this.game.grid.length) {
            int j = 0;
            while (j < this.game.grid[i].length) {
                if (this.game.grid[i][j].block != null && this.game.grid[i][j].block.hintIndex == this.unsolvedHintIndex) {
                    this.unsolvedHintIndex++;
                    return this.game.grid[i][j].block.id;
                }
                j++;
            }
            i++;
        }
        return -1;
    }

    public int getSolverHint() {
        if (this.game.getBlockByID(((Integer) this.solveOrder.get(this.solverHintIndex)).intValue()) == null) {
            return -1;
        }
        ArrayList arrayList = this.solveOrder;
        int i = this.solverHintIndex;
        this.solverHintIndex = i + 1;
        return ((Integer) arrayList.get(i)).intValue();
    }
}
