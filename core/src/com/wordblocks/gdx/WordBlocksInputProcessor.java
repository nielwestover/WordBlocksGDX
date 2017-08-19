package com.wordblocks.gdx;

import com.badlogic.gdx.InputProcessor;

public abstract class WordBlocksInputProcessor implements InputProcessor {
    public abstract boolean touchDown(int i, int i2, int i3, int i4);

    public abstract boolean touchDragged(int i, int i2, int i3);

    public abstract boolean touchUp(int i, int i2, int i3, int i4);

    public boolean mouseMoved(int x, int y) {
        return true;
    }

    public boolean keyDown(int keycode) {
        return true;
    }

    public boolean keyUp(int keycode) {
        return true;
    }

    public boolean keyTyped(char character) {
        return true;
    }

    public boolean scrolled(int amount) {
        return true;
    }
}
