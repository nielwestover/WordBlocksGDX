package utils;

import helpers.RowColPair;
import java.util.List;
import java.util.Random;
import solver.SolverBoard;

public class Utils {
    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static float getRandomFloat(float max) {
        return rand.nextFloat() * max;
    }

    public static int getDimension(List<String> level) {
        int count = 0;
        for (int i = 0; i < level.size(); i++) {
            count += ((String) level.get(i)).length();
        }
        return (int) Math.sqrt((double) count);
    }

    public static String ReverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public static SolverBoard GetNewBoardState(SolverBoard board, RowColPair item) {
        SolverBoard b = board.copy();
        b.words.set(0, ((String) board.words.get(0)).substring(1));
        if (b == null || b.solveOrder == null || board == null || item == null || board.getCell(item.Row, item.Col) == null) {
            return null;
        }
        b.solveOrder.add(Integer.valueOf(board.getCell(item.Row, item.Col).id));
        b.curCell = item;
        return b;
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
