package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import datatypes.Board;
import datatypes.Level;
import datatypes.LevelPack;
import java.util.ArrayList;
import java.util.List;
import utils.Utils;

public class MyApplication {
    private static Level curLevel;
    public static int curLevelIndex = 7;
    public static int curPackIndex = 0;
    private static List<LevelPack> levelPacks = new ArrayList();
    private static Preferences preferences;
    public static long randomSeed = 0;

    public static List<LevelPack> packs() {
        if (levelPacks == null || levelPacks.size() == 0) {
            loadAllLevels();
        }
        return levelPacks;
    }

    public static Level getCurLevel() {
        if (curLevel == null) {
            curLevel = generateLevel((List) ((LevelPack) packs().get(curPackIndex)).levels.get(curLevelIndex), (randomSeed + ((long) (curPackIndex * 100))) + ((long) curLevelIndex));
        }
        return curLevel;
    }

    public static boolean setCurLevel(int level) {
        if (level < 0 || level >= ((LevelPack) packs().get(curPackIndex)).levels.size()) {
            return false;
        }
        curLevelIndex = level;
        curLevel = generateLevel((List) ((LevelPack) packs().get(curPackIndex)).levels.get(curLevelIndex), (randomSeed + ((long) (curPackIndex * 100))) + ((long) curLevelIndex));
        return true;
    }

    public static boolean incrementCurLevel() {
        if (curLevelIndex + 1 < 20) {
            return setCurLevel(curLevelIndex + 1);
        }
        if (curPackIndex + 1 >= packs().size()) {
            return false;
        }
        curPackIndex++;
        return setCurLevel(0);
    }

    public static void loadAllLevels() {
        levelPacks = (List) new Json().fromJson(ArrayList.class, LevelPack.class, Gdx.files.internal("completePackList.json"));
    }

    static Level generateLevel(List<String> level, long seed) {
        Board board = new Board(Utils.getDimension(level), seed);
        do {
            board.reset();
        } while (!board.AddAllWords(level));
        Level l = new Level();
        l.board = board.ToList();
        l.words = level;
        return l;
    }

    public static Preferences getPreferences() {
        if (preferences == null) {
            preferences = Gdx.app.getPreferences("wordPrefs");
        }
        return preferences;
    }

    public static void loadProfile() {
        curPackIndex = getPreferences().getInteger("pack");
        curLevelIndex = getPreferences().getInteger("level");
        randomSeed = getPreferences().getLong("randomSeed");
        if (randomSeed == 0) {
            randomSeed = TimeUtils.millis();
            getPreferences().putLong("randomSeed", randomSeed);
            getPreferences().flush();
        }
    }
}
