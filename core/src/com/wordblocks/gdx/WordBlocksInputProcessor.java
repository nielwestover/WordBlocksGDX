package com.wordblocks.gdx;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputProcessor;
/**
 * Created by a2558 on 3/21/2016.
 */
public abstract class WordBlocksInputProcessor implements InputProcessor {
    @Override
    public abstract boolean touchDown (int x, int y, int pointer, int button);

    @Override
    public abstract boolean touchUp (int x, int y, int pointer, int button);

    @Override
    public abstract boolean touchDragged (int x, int y, int pointer);

    @Override
    public boolean mouseMoved(int x, int y){
        return true;
    }

    @Override
    public boolean keyDown(int keycode){
        return true;
    }

    @Override
    public boolean keyUp(int keycode){
        return true;
    }

    @Override
    public boolean keyTyped(char character){
        return true;
    }

    @Override
    public boolean scrolled(int amount){
        return true;
    }
}
