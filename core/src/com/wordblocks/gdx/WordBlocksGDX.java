package com.wordblocks.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class WordBlocksGDX extends Game {
    public void create() {
        setScreen(new GroupChooser(this));
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
