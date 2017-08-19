/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Math
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.wordblocks.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wordblocks.gdx.BlockAnimation;
import com.wordblocks.gdx.DrawableObject;
import com.wordblocks.gdx.Game;
import com.wordblocks.gdx.HintLoop;
import particles.ParticleManager;

public class Block
extends DrawableObject {
    static HintLoop hintLoop;
    Color alpha = new Color();
    BlockAnimation blockAnimation;
    Color blockColor = Color.WHITE.cpy();
    float blockScale;
    Vector2 center = new Vector2();
    Color charColor = Color.BLACK.cpy();
    private AnimState curState;
    float dissolveInterpSteps = 15.0f;
    float dissolveSteps = 0.0f;
    private boolean found;
    Game game;
    boolean hintAnimIncreasing = true;
    int hintAnimRotateCount = 0;
    int hintIndex;
    int hintInterpSteps = 5;
    int id;
    public int interpSteps = 0;
    private boolean isFirstHint = false;
    private boolean isHint;
    char letter;
    float letterHeight;
    float letterWidth;
    float maxInterpSteps = 20.0f;
    private boolean selected;
    float targetAlphaFill = 1.0f;
    int wordPosition;

    public Block(int n, Game game) {
        this.game = game;
        this.id = n;
        this.curState = AnimState.ANIM_START;
        this.blockScale = 1.0f;
        this.blockAnimation = new BlockAnimation((Block)this, game.blockAnimType, game.grid.length);
    }

    public AnimState getCurState() {
        return this.curState;
    }

    public boolean getIsHint() {
        return this.isHint;
    }

    boolean getSelected() {
        return this.selected;
    }

    boolean isTouched(float f, float f2, float f3) {
        if (f3 + this.pos.x < f && this.pos.x + this.pos.width - f3 > f && f3 + this.pos.y < f2 && this.pos.y + this.pos.height - f3 > f2) {
            return true;
        }
        return false;
    }

    @Override
    void renderShapes(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(this.blockColor);
        shapeRenderer.identity();
        if (this.blockScale != 1.0f) {
            float f = this.blockScale * this.pos.width;
            float f2 = Math.min((float)(1.1f * f), (float)this.pos.width);
            Rectangle rectangle = new Rectangle(this.pos);
            Rectangle rectangle2 = new Rectangle(this.pos);
            rectangle2.width = f2;
            rectangle2.height = f2;
            rectangle2.setCenter(this.center);
            rectangle.width = f;
            rectangle.height = f;
            rectangle.setCenter(this.center);
            this.alpha.set(this.blockColor);
            this.alpha.a = this.targetAlphaFill;
            shapeRenderer.setColor(this.alpha);
            shapeRenderer.rect(rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
            shapeRenderer.setColor(this.charColor);
            this.roundedRect(shapeRenderer, rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height, 10.0f, false);
            shapeRenderer.setColor(this.blockColor);
            this.roundedRect(shapeRenderer, rectangle.x, rectangle.y, rectangle.width, rectangle.height, this.pos.width / 35.0f, false);
            return;
        }
        if (this.angle == 0.0f) {
            this.roundedRect(shapeRenderer, this.pos.x, this.pos.y, this.pos.width, this.pos.height, this.pos.width / 35.0f, true);
            return;
        }
        shapeRenderer.rect(this.pos.x, this.pos.y, this.pos.width / 2.0f, this.pos.height / 2.0f, this.pos.width, this.pos.height, 1.0f, 1.0f, this.angle);
    }

    @Override
    void renderSprites(SpriteBatch spriteBatch, BitmapFont bitmapFont) {
        bitmapFont.setColor(this.charColor);
        bitmapFont.getData().setScale(bitmapFont.getData().scaleX * this.blockScale);
        if (this.blockScale != 1.0f) {
            GlyphLayout glyphLayout = new GlyphLayout();
            glyphLayout.setText(bitmapFont, "" + this.letter + "");
            float f = glyphLayout.width;
            float f2 = glyphLayout.height;
            bitmapFont.draw((Batch)spriteBatch, "" + this.letter + "", this.pos.x + this.pos.width / 2.0f - f / 2.0f, this.pos.y + this.pos.height / 2.0f + f2 / 2.0f);
            return;
        }
        bitmapFont.draw((Batch)spriteBatch, "" + this.letter + "", this.pos.x + this.pos.width / 2.0f - this.letterWidth / 2.0f, this.pos.y + this.pos.height / 2.0f + this.letterHeight / 2.0f);
    }

    public void roundedRect(ShapeRenderer shapeRenderer, float f, float f2, float f3, float f4, float f5, boolean bl) {
        if (bl) {
            shapeRenderer.rect(f + f5, f2 + f5, f3 - 2.0f * f5, f4 - 2.0f * f5);
        }
        shapeRenderer.rect(f + f5, f2, f3 - 2.0f * f5, f5);
        shapeRenderer.rect(f + f3 - f5, f2 + f5, f5, f4 - 2.0f * f5);
        shapeRenderer.rect(f + f5, f2 + f4 - f5, f3 - 2.0f * f5, f5);
        shapeRenderer.rect(f, f2 + f5, f5, f4 - 2.0f * f5);
        shapeRenderer.arc(f + f5, f2 + f5, f5, 180.0f, 90.0f);
        shapeRenderer.arc(f + f3 - f5, f2 + f5, f5, 270.0f, 90.0f);
        shapeRenderer.arc(f + f3 - f5, f2 + f4 - f5, f5, 0.0f, 90.0f);
        shapeRenderer.arc(f + f5, f2 + f4 - f5, f5, 90.0f, 90.0f);
    }

    public void setCurState(AnimState animState) {
        this.curState = animState;
    }

    public void setIsHint(boolean bl) {
        this.isHint = bl;
        if (this.curState == AnimState.NORMAL) {
            this.curState = AnimState.NORMALTOHINT;
        }
    }

    void setSelected(boolean bl) {
        this.selected = bl;
    }

    public void setWordPosition(int n) {
        this.wordPosition = n;
        this.targetAlphaFill = 0.2f + 0.03f * (float)n;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    void update() {
        this.center = this.pos.getCenter(this.center);
        switch (this.curState) {
            case ANIM_START: {
                if (this.blockAnimation.update() != BlockAnimation.AnimState.DONE) return;
                this.curState = AnimState.ANIM_START_DONE;
                return;
            }
            case ANIM_START_DONE: {
                this.curState = AnimState.NORMAL;
                hintLoop = new HintLoop();
            }
            case NORMAL: {
                if (this.isHint) {
                    Color color = this.blockColor;
                    float f = 1.0f - HintLoop.val;
                    float f2 = 1.0f - HintLoop.val;
                    color.set(f, f2, 1.0f - HintLoop.val, 1.0f);
                    Color color2 = this.charColor;
                    float f3 = HintLoop.val;
                    float f4 = HintLoop.val;
                    color2.set(f3, f4, HintLoop.val, 1.0f);
                } else {
                    this.blockColor = Color.WHITE.cpy();
                    this.charColor = Color.BLACK.cpy();
                }
                if (!this.selected) return;
                this.curState = AnimState.NORMALTOSELECTED;
                this.update();
                break;
            }
            case NORMALTOHINT: {
                this.interpSteps = (int)(0.3333f * (float)this.hintInterpSteps);
                this.curState = AnimState.NORMALTOHINT_ANIM;
                ParticleManager.Inst().createEffect("hint.txt", this.center, false);
                this.hintAnimIncreasing = true;
                this.hintAnimRotateCount = 0;
            }
            case NORMALTOHINT_ANIM: {
                if (this.hintAnimRotateCount >= 4) {
                    this.angle = 0.0f;
                    this.blockScale = 1.0f;
                    this.curState = AnimState.NORMAL;
                    return;
                }
                if (this.hintAnimIncreasing) {
                    int n = this.interpSteps;
                    this.interpSteps = n + 1;
                    if (n >= this.hintInterpSteps) {
                        this.hintAnimIncreasing = false;
                        this.hintAnimRotateCount = 1 + this.hintAnimRotateCount;
                    }
                } else {
                    int n = this.interpSteps;
                    this.interpSteps = n - 1;
                    if (n <= 0) {
                        this.hintAnimIncreasing = true;
                    }
                }
                this.angle = 350.0f + 20.0f * Interpolation.sineOut.apply((float)this.interpSteps / (float)this.hintInterpSteps);
                break;
            }
            case NORMALTOSELECTED: {
                this.interpSteps = 0;
                this.curState = AnimState.NORMALTOSELECTED_ANIM;
            }
            case NORMALTOSELECTED_ANIM: {
                int n = this.interpSteps;
                this.interpSteps = n + 1;
                if ((float)n <= this.maxInterpSteps) {
                    float f = Interpolation.exp5Out.apply((float)this.interpSteps / this.maxInterpSteps);
                    this.blockColor.set(1.0f - f, 1.0f - f, 1.0f - f, 1.0f);
                    this.charColor.set(f, f, f, 1.0f);
                    this.blockScale = 1.0f - f * 0.12f;
                } else {
                    this.curState = AnimState.SELECTED;
                }
                if (this.found) {
                    this.curState = AnimState.TOFOUND;
                    return;
                }
                if (this.selected) return;
                this.curState = AnimState.SELECTEDTONORMAL;
                break;
            }
            case SELECTED: {
                this.blockColor = Color.BLACK.cpy();
                if (this.selected) break;
                this.curState = AnimState.SELECTEDTONORMAL;
                break;
            }
            case DISSOLVING: {
                float f = this.dissolveSteps;
                this.dissolveSteps = f + 1.0f;
                if (f <= this.dissolveInterpSteps) {
                    float f5;
                    this.blockColor.a = f5 = 1.0f - Interpolation.exp5Out.apply(this.dissolveSteps / 20.0f);
                    this.charColor.a = f5;
                    return;
                }
                this.curState = AnimState.FOUND;
                break;
            }
            case FALLING: {
                if (this.pos.y > this.targetPos.y) {
                    this.velocity = 4.0f + this.velocity;
                    this.pos.setY(this.pos.y - this.velocity);
                    if (this.pos.y > this.targetPos.y) return;
                    this.pos.set(this.targetPos);
                    this.velocity = 0.0f;
                    return;
                }
                this.curState = AnimState.NORMAL;
                this.update();
                break;
            }
            case SELECTEDTONORMAL: {
                this.interpSteps = 0;
                this.curState = AnimState.SELECTEDTONORMAL_ANIM;
            }
            case SELECTEDTONORMAL_ANIM: {
                int n = this.interpSteps;
                this.interpSteps = n + 1;
                if ((float)n < this.maxInterpSteps) {
                    float f = Interpolation.linear.apply((float)this.interpSteps / this.maxInterpSteps);
                    this.blockColor.set(f, f, f, 1.0f);
                    this.charColor.set(1.0f - f, 1.0f - f, 1.0f - f, 1.0f);
                    this.blockScale = 0.88f + f * 0.12f;
                    return;
                }
                this.blockColor.set(1.0f, 1.0f, 1.0f, 1.0f);
                this.charColor.set(0.0f, 0.0f, 0.0f, 1.0f);
                this.curState = AnimState.NORMAL;
                this.blockScale = 1.0f;
                break;
            }
        }

    }

    public enum AnimState {
        ANIM_START,
        ANIM_START_DONE,
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

}

