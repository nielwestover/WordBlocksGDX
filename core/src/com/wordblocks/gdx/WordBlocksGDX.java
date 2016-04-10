package com.wordblocks.gdx;

import com.badlogic.gdx.Screen;

public class WordBlocksGDX extends com.badlogic.gdx.Game {
    static final int worldWidth = 1200;
    static final int worldHeight = 1600;

    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
    @Override
    public void dispose() {

    }
    @Override
    public Screen getScreen() {
        return super.getScreen();
    }
    @Override
    public void pause() {

    }
    @Override
    public void render() {
        getScreen().render(0);
    }
    @Override
    public void resize(int width, int height) {
        getScreen().resize(width, height);
    }
    @Override
    public void resume() {

    }
    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }
}
