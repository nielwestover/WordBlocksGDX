package com.wordblocks.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block extends DrawableObject {
    char letter;
    int hintIndex;
    float letterWidth;
    float letterHeight;
    private boolean selected;
    private boolean found;
    Color blockColor = Color.WHITE.cpy();
    Color charColor = Color.BLACK.cpy();
    float blockScale;
    Vector2 center;

    public Block() {
        curState = AnimState.ANIM_START;
        blockScale = 1;
    }

    public static enum AnimState {
        ANIM_START,
        NORMAL,
        NORMALTOSELECTED,
        NORMALTOSELECTED_ANIM,
        SELECTED,
        FALLING,
        SELECTEDTONORMAL,
        SELECTEDTONORMAL_ANIM,
        TOFOUND,
        FOUND

    }

    private AnimState curState;

    public AnimState getCurState() {
        return curState;
    }

    public void setCurState(AnimState state) {

        curState = state;
    }

    void setFound(boolean blockFound) {
        found = blockFound;

    }

    void setSelected(boolean sel) {
        selected = sel;
    }

    boolean getSelected() {
        return selected;
    }

    boolean isTouched(float X, float Y, float inset) {
        if (pos.x + inset < X && (pos.x + pos.width - inset) > X)
            if (pos.y + inset < Y && (pos.y + pos.height - inset) > Y)
                return true;
        return false;
    }

    private float maxInterpSteps = 20;
    private int interpSteps = 0;

    @Override
    void update() {
        switch (curState) {
            case ANIM_START:
                curState = AnimState.NORMAL;
                break;
            case NORMAL:
                blockColor = Color.WHITE.cpy();
                charColor = Color.BLACK.cpy();
                if (selected) {
                    curState = AnimState.NORMALTOSELECTED;
                    update();
                }
                break;
            case NORMALTOSELECTED:
                interpSteps = 0;
                curState = AnimState.NORMALTOSELECTED_ANIM;
            case NORMALTOSELECTED_ANIM:
                if (interpSteps++ <= maxInterpSteps) {
                    float val = Interpolation.exp10Out.apply(interpSteps / maxInterpSteps);
                    blockColor.set(1.0f - val, 1.0f - val, 1.0f - val, 1);
                    charColor.set(val, val, val, 1);
                    blockScale = 1.0f - val * .12f;

                } else
                    curState = AnimState.SELECTED;
                if (found)
                    curState = AnimState.TOFOUND;
                else if (!selected)
                    curState = AnimState.SELECTEDTONORMAL;
                break;
            case SELECTED:
                blockColor = Color.BLACK.cpy();
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
                } else
                    curState = AnimState.NORMAL;
                break;
            case SELECTEDTONORMAL:
                interpSteps = 0;
                curState = AnimState.SELECTEDTONORMAL_ANIM;
            case SELECTEDTONORMAL_ANIM:
                if (interpSteps++ <= maxInterpSteps) {
                    float val = Interpolation.exp10Out.apply(interpSteps / maxInterpSteps);
                    blockColor.set(val, val, val, 1);
                    charColor.set(1.0f - val, 1.0f - val, 1.0f - val, 1);
                    blockScale = .88f + val * .12f;

                } else {
                    blockColor.set(1, 1, 1, 1);
                    charColor.set(0,0,0, 1);
                    curState = AnimState.NORMAL;
                    blockScale = 1f;
                }
                break;
            case TOFOUND:
                break;
            case FOUND:
                break;
        }
    }

    Vector2 tempVec = new Vector2();

    @Override
    void renderShapes(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(blockColor);
        shapeRenderer.identity();
        if (blockScale != 1.0f) {
            tempVec = pos.getCenter(tempVec);
            float width = blockScale * pos.width;
            float outlineWidth = Math.min(width * 1.1f, pos.width);
            com.badlogic.gdx.math.Rectangle r = new com.badlogic.gdx.math.Rectangle(pos);
            com.badlogic.gdx.math.Rectangle outline = new com.badlogic.gdx.math.Rectangle(pos);

            outline.width = outlineWidth;
            outline.height = outlineWidth;
            outline.setCenter(tempVec);
            r.width = width;
            r.height = width;
            r.setCenter(tempVec);
            shapeRenderer.setColor(charColor);
            //shapeRenderer.rect(outline.x, outline.y, outline.width, outline.height);
            roundedRect(shapeRenderer, outline.x, outline.y, outline.width, outline.height, 10);
            shapeRenderer.setColor(blockColor);
            //shapeRenderer.rect(r.x, r.y, r.width, r.height);
            roundedRect(shapeRenderer, r.x, r.y, r.width, r.height, pos.width/35.0f);
        } else
            //shapeRenderer.rect(pos.x, pos.y, pos.width, pos.height);
            roundedRect(shapeRenderer, pos.x, pos.y, pos.width, pos.height, pos.width/35.0f);
    }

    @Override
    void renderSprites(SpriteBatch spriteBatch, BitmapFont font) {
        font.setColor(charColor);
        font.getData().setScale(font.getData().scaleX * blockScale);
        float tempLetterWidth = letterWidth;
        float tempLetterHeight = letterHeight;
        if (blockScale != 1.0f) {
            GlyphLayout glyphLayout = new GlyphLayout();
            String item = letter + "";
            glyphLayout.setText(font, item);
            tempLetterWidth = glyphLayout.width;
            tempLetterHeight = glyphLayout.height;

            font.draw(spriteBatch, letter + "", pos.x + pos.width / 2 - tempLetterWidth / 2, pos.y + pos.height / 2 + tempLetterHeight / 2);
        } else
            font.draw(spriteBatch, letter + "", pos.x + pos.width / 2 - letterWidth / 2, pos.y + pos.height / 2 + letterHeight / 2);
    }

    public void roundedRect(ShapeRenderer shapeRenderer, float x, float y, float width, float height, float radius){
        // Central rectangle
        shapeRenderer.rect(x + radius, y + radius, width - 2 * radius, height - 2 * radius);

        // Four side rectangles, in clockwise order
        shapeRenderer.rect(x + radius, y, width - 2 * radius, radius);
        shapeRenderer.rect(x + width - radius, y + radius, radius, height - 2 * radius);
        shapeRenderer.rect(x + radius, y + height - radius, width - 2 * radius, radius);
        shapeRenderer.rect(x, y + radius, radius, height - 2 * radius);

        // Four arches, clockwise too
        shapeRenderer.arc(x + radius, y + radius, radius, 180f, 90f);
        shapeRenderer.arc(x + width - radius, y + radius, radius, 270f, 90f);
        shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f);
        shapeRenderer.arc(x + radius, y + height - radius, radius, 90f, 90f);
    }
}
