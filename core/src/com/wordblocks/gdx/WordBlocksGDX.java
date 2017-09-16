package com.wordblocks.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class WordBlocksGDX extends Game {
    GroupChooser groupChooser;
    LevelChooser levelChooser;
    public void create() {
        if (groupChooser == null)
            groupChooser = new GroupChooser(this);
        if (levelChooser == null)
            levelChooser = new LevelChooser(this);
        setScreen(groupChooser);
    }

    public void dispose() {
    }

    public Screen getScreen() {
        return super.getScreen();
    }

    public void pause() {
        getScreen().pause();
    }

    public void render() {
        getScreen().render(0.0f);
    }

    public void resize(int width, int height) {
        getScreen().resize(width, height);
    }

    public void resume() {
        getScreen().resume();
    }

    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }
}
