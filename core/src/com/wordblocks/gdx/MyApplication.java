package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by a2558 on 2/21/2016.
 */
public class MyApplication {

    private static List<List<String>> wordLists = new ArrayList<List<String>>();
    //private static List<Level> levels = new ArrayList<Level>();

    private static Level curLevel;
    public static int curLevelIndex = 0;

    public static List<List<String>> levels() {
        if (wordLists == null || wordLists.size() == 0) {
           loadAllLevels();

        }
        return wordLists;
    }

    public static Level getCurLevel() {
        if (curLevel == null) {
            curLevel = generateLevel(levels().get(curLevelIndex), curLevelIndex);
        }
        return curLevel;
    }

    //returns success - true or false
    public static boolean setCurLevel(int level) {
        if (level >= 0 && level < levels().size()) {
            curLevelIndex = level;
            curLevel = generateLevel(levels().get(level), curLevelIndex);
            return true;
        }
        return false;
    }

    public static boolean incrementCurLevel() {
        return setCurLevel(curLevelIndex + 1);
    }

    public static void loadAllLevels() {
           wordLists = new Json().fromJson(ArrayList.class, ArrayList.class, Gdx.files.internal("usedWordsSorted.json"));//gson.fromJson(br, new TypeToken<List<JsonLog>>(){}.getType());
        //levels = new Json().fromJson(ArrayList.class, Level.class, Gdx.files.internal("45_levels_3_game_ideas.json"));
    }

    static Level generateLevel(List<String> level, int seed) {
        int dim = Utils.getDimension(level);
        Board board = new Board(dim, seed);

        while (true) {
            board.reset();
            if (board.AddAllWords(level)) {
                Level l = new Level();
                l.board = board.ToList();
                l.words = level;
                return l;
            }
        }
    }
}

