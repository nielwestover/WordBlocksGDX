package com.wordblocks.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Block extends DrawableObject {
    char letter;
    int hintIndex;
    float letterWidth;
    float letterHeight;
    boolean selected;
    boolean found;
    Color blockColor = Color.WHITE.cpy();
    Color charColor = Color.BLACK.cpy();

    public Block(){
        curState = AnimState.ANIM_START;
    }
    public static enum AnimState {
        ANIM_START,
        NORMAL,
        NORMALTOSELECTED,
        SELECTED,
        FALLING,
        SELECTEDTONORMAL,
        TOFOUND,
        FOUND

    }

    private AnimState curState;
    public AnimState getCurState(){
        return curState;
    }
    public void setCurState(AnimState state){
        curState = state;
    }

    void setFound(boolean blockFound) {
        found = blockFound;

    }

    boolean isTouched(float X, float Y, float inset) {
        if (pos.x + inset < X && (pos.x + pos.width - inset) > X)
            if (pos.y + inset < Y && (pos.y + pos.height - inset) > Y)
                return true;
        return false;
    }

    @Override
    void update() {
        switch (curState) {
            case ANIM_START:
                curState = AnimState.NORMAL;
                break;
            case NORMAL:
                blockColor = Color.WHITE.cpy();
                charColor = Color.BLACK.cpy();
                if (selected)
                    curState = AnimState.NORMALTOSELECTED;
                break;
            case NORMALTOSELECTED:
                if (!blockColor.equals(Color.YELLOW)) {//0xffff00ff
                    if (blockColor.toIntBits() > Color.YELLOW.toIntBits())
                        blockColor.add(0, 0, -.1f, 0);
                } else
                    curState = AnimState.SELECTED;
                if (found)
                    curState = AnimState.TOFOUND;
                else if (!selected)
                    curState = AnimState.SELECTEDTONORMAL;
                break;
            case SELECTED:
                blockColor = Color.YELLOW.cpy();
                if (!selected)
                    curState = AnimState.SELECTEDTONORMAL;
                break;
            case FALLING:
                if (pos.y > targetPos.y) {
                    velocity += 3;
                    pos.setY(pos.y - velocity);
                    if (pos.y < targetPos.y) {
                        pos.set(targetPos);
                        velocity = 0;
                    }
                }
                else
                    curState = AnimState.NORMAL;
                break;
            case SELECTEDTONORMAL:
                if (!blockColor.equals(Color.WHITE)) {//0xffff00ff
                    if (blockColor.toIntBits() < Color.WHITE.toIntBits()) {
                        blockColor.add(0, 0, .1f, 0);
                        String s = blockColor.toString();
                    }
                } else
                    curState = AnimState.NORMAL;
                break;
            case TOFOUND:
                break;
            case FOUND:
                break;
        }
    }

    @Override
    void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(blockColor);
        shapeRenderer.identity();
        shapeRenderer.rect(pos.x, pos.y, pos.width, pos.height);
        shapeRenderer.end();

        spriteBatch.begin();
        font.setColor(charColor);
        font.draw(spriteBatch, letter + "", pos.x + pos.width / 2 - letterWidth / 2, pos.y + pos.height / 2 + letterHeight / 2);
        spriteBatch.end();
    }
}
