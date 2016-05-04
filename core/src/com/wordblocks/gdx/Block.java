package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;

import particles.ParticleManager;

public class Block extends DrawableObject {
    char letter;
    int hintIndex;
    int id;
    float letterWidth;
    float letterHeight;
    private boolean selected;
    private boolean found;
    Color blockColor = Color.WHITE.cpy();
    Color charColor = Color.BLACK.cpy();
    float blockScale;
    Vector2 center = new Vector2();
    boolean showParticle = false;

    private boolean isHint;

    public Block() {
        curState = AnimState.ANIM_START;
        blockScale = 1;
    }

    public boolean getIsHint() {
        return isHint;
    }

    private boolean isFirstHint = false;

    public void setIsHint(boolean isHint) {
        this.isHint = isHint;
        if (curState == AnimState.NORMAL) {
            curState = AnimState.NORMALTOHINT;
        }
    }

    public static enum AnimState {
        ANIM_START,
        NORMAL,
        NORMALTOHINT,
        NORMALTOHINT_ANIM,
        NORMALTOSELECTED,
        NORMALTOSELECTED_ANIM,
        SELECTED,
        DISSOLVING,
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

    static HintLoop hintLoop;
    float maxInterpSteps = 20;
    int interpSteps = 0;
    int hintInterpSteps = 5;
    boolean hintAnimIncreasing = true;
    int hintAnimRotateCount = 0;
    float dissolveInterpSteps = 40;
    float dissolveSteps = 0;

    @Override
    void update() {
        center = pos.getCenter(center);

        switch (curState) {
            case ANIM_START:
                curState = AnimState.NORMAL;
                hintLoop = new HintLoop();
                break;
            case NORMAL:
                if (isHint) {
                    blockColor.set(1.0f - hintLoop.val, 1.0f - hintLoop.val, 1.0f - hintLoop.val, 1);
                    charColor.set(hintLoop.val, hintLoop.val, hintLoop.val, 1);
                } else {
                    blockColor = Color.WHITE.cpy();
                    charColor = Color.BLACK.cpy();
                }
                if (selected) {
                    curState = AnimState.NORMALTOSELECTED;
                    update();
                }
                break;
            case NORMALTOHINT:
                interpSteps = (int) (.3333f * hintInterpSteps);
                curState = AnimState.NORMALTOHINT_ANIM;
                hintAnimIncreasing = true;
                hintAnimRotateCount = 0;
            case NORMALTOHINT_ANIM:
                if (hintAnimRotateCount < 4) {
                    if (hintAnimIncreasing) {
                        if (interpSteps++ >= hintInterpSteps) {
                            hintAnimIncreasing = false;
                            hintAnimRotateCount++;
                        }
                    } else {
                        if (interpSteps-- <= 0) {
                            hintAnimIncreasing = true;
                        }
                    }
                    float angleVal = Interpolation.sineOut.apply(interpSteps / (float) hintInterpSteps);
                    angle = 350 + angleVal * 20;
                    //blockColor.set(1.0f - hintLoop.val, 1.0f - hintLoop.val, 1.0f - hintLoop.val, 1);
                    //charColor.set(hintLoop.val, hintLoop.val, hintLoop.val, 1);
                } else {
                    angle = 0;
                    blockScale = 1.0f;
                    curState = AnimState.NORMAL;
                }
                break;
            case NORMALTOSELECTED:
                interpSteps = 0;
                curState = AnimState.NORMALTOSELECTED_ANIM;
            case NORMALTOSELECTED_ANIM:
                if (interpSteps++ <= maxInterpSteps) {
                    float val = Interpolation.exp5Out.apply(interpSteps / maxInterpSteps);
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
            case DISSOLVING:
                if (dissolveSteps++ <= dissolveInterpSteps) {
                    float val = 1.f - Interpolation.exp5Out.apply(dissolveSteps / 60.f);
                    blockColor.a = val;
                    charColor.a = val;
                } else
                    curState = AnimState.FOUND;
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
                    charColor.set(0, 0, 0, 1);
                    curState = AnimState.NORMAL;
                    blockScale = 1f;
                }
                break;
        }
    }

    @Override
    void renderShapes(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(blockColor);
        shapeRenderer.identity();

        if (blockScale != 1.0f) {

            float width = blockScale * pos.width;
            float outlineWidth = Math.min(width * 1.1f, pos.width);
            com.badlogic.gdx.math.Rectangle r = new com.badlogic.gdx.math.Rectangle(pos);
            com.badlogic.gdx.math.Rectangle outline = new com.badlogic.gdx.math.Rectangle(pos);

            outline.width = outlineWidth;
            outline.height = outlineWidth;
            outline.setCenter(center);
            r.width = width;
            r.height = width;
            r.setCenter(center);
            shapeRenderer.setColor(charColor);
            //shapeRenderer.rect(outline.x, outline.y, outline.width, outline.height);
            roundedRect(shapeRenderer, outline.x, outline.y, outline.width, outline.height, 10);
            shapeRenderer.setColor(blockColor);
            //shapeRenderer.rect(r.x, r.y, r.width, r.height);
            roundedRect(shapeRenderer, r.x, r.y, r.width, r.height, pos.width / 35.0f);
        } else {

            if (angle == 0)
                roundedRect(shapeRenderer, pos.x, pos.y, pos.width, pos.height, pos.width / 35.0f);
            else {
                shapeRenderer.rect(pos.x, pos.y, pos.width/2, pos.height/2, pos.width, pos.height, 1.f, 1.f, angle);
            }
        }
        //Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    void renderSprites(SpriteBatch spriteBatch, BitmapFont font) {

        font.setColor(charColor);
        font.getData().setScale(font.getData().scaleX * blockScale);
        if (blockScale != 1.0f) {
            GlyphLayout glyphLayout = new GlyphLayout();
            String item = letter + "";
            glyphLayout.setText(font, item);
            float tempLetterWidth = glyphLayout.width;
            float tempLetterHeight = glyphLayout.height;

            font.draw(spriteBatch, letter + "", pos.x + pos.width / 2 - tempLetterWidth / 2, pos.y + pos.height / 2 + tempLetterHeight / 2);
        } else
            font.draw(spriteBatch, letter + "", pos.x + pos.width / 2 - letterWidth / 2, pos.y + pos.height / 2 + letterHeight / 2);
    }

    public void roundedRect(ShapeRenderer shapeRenderer, float x, float y, float width, float height, float radius) {
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
