package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by a2558 on 2/21/2016.
 */
public class MyApplication {

    private static List<Level> levels = new ArrayList<Level>();

    private static Level curLevel;
    public static int curLevelIndex = 50;

    public static List<Level> levels() {
        if (levels == null || levels.size() == 0) {
            try {
                loadAllLevels();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return levels;
    }

    public static Level getCurLevel() {
        if (curLevel == null) {
            curLevel = levels().get(curLevelIndex);
        }
        return curLevel;
    }

    //returns success - true or false
    public static boolean setCurLevel(int level) {
        if (level >= 0 && level < levels().size()) {
            curLevelIndex = level;
            curLevel = levels.get(level);
            return true;
        }
        return false;
    }

    public static boolean incrementCurLevel() {
        return setCurLevel(curLevelIndex + 1);
    }

    private static void loadAllLevels() throws IOException {
        levels = new Json().fromJson(ArrayList.class, Level.class, Gdx.files.internal("allBoards.json"));
    }

}

