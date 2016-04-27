package com.wordblocks.gdx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import helpers.RowColPair;

/**
 * Created by a2558 on 4/20/2016.
 */
public class Utils {

    public static int getDimension(List<String> level)
    {
        int count = 0;
        for (int i = 0; i < level.size(); ++i)
        {
            count += level.get(i).length();
        }
        return (int)Math.sqrt(count);
    }
    public static String ReverseString(String s)
    {
        return new StringBuilder(s).reverse().toString();
    }

    public static SolverBoard GetNewBoardState(SolverBoard board, RowColPair item)
    {
        //Make a copy of the board we can manipulate
        SolverBoard b = board.copy();

        //Remove the first letter and add it to the solve order
        b.words.set(0, board.words.get(0).substring(1));
        b.solveOrder.add(board.getCell(item.Row, item.Col).id);

        //Make curCell point to location of letter we just processed
        b.curCell = item;

        return b;
    }

    private static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }
}
