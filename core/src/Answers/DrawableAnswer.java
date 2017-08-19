package Answers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.wordblocks.gdx.Game.Answer;
import java.util.Random;

public class DrawableAnswer {
    public float alpha = 0.0f;
    public Answer answer;
    Vector2 center;
    public Color color = Color.WHITE.cpy();
    GlyphLayout glyphLayout;
    Vector2 home;
    Vector2 hoverDir = new Vector2();
    public HoverState hoverState = HoverState.FIND_POSITION;
    private int interpSteps = 0;
    Vector2 lastHover = new Vector2();
    final float maxHoverDist = 18.0f;
    Vector2 offset = new Vector2();
    Random rand = new Random();
    public AnswerState state = AnswerState.NONE;
    Vector2 targetHover = new Vector2();
    Vector2 upperLeft;

    public enum AnswerState {
        NONE,
        FADE_IN_WORD,
        IDLE,
        FADE_OUT_WORD,
        FOUND
    }

    public enum HoverState {
        FIND_POSITION,
        TRAVEL
    }

    public DrawableAnswer(Answer answer, Vector2 pos, Vector2 centerPos) {
        this.answer = answer;
        this.color.a = this.alpha;
        this.upperLeft = pos;
        this.center = centerPos;
        this.offset.x = this.upperLeft.x - centerPos.x;
        this.offset.y = this.upperLeft.y - centerPos.y;
        this.home = this.center.cpy();
        this.lastHover.set(this.home);
        this.glyphLayout = new GlyphLayout();
    }

    public void update() {
        Interpolation interpolation;
        int i;
        switch (this.state) {
            case FADE_IN_WORD:
                if (this.alpha == 1.0f || this.answer.found) {
                    this.state = AnswerState.IDLE;
                } else {
                    interpolation = Interpolation.fade;
                    i = this.interpSteps;
                    this.interpSteps = i + 1;
                    this.alpha = interpolation.apply(((float) i) / 100.0f);
                }
                this.color.a = this.alpha;
                return;
            case IDLE:
                hover();
                if (this.answer.found) {
                    this.state = AnswerState.FADE_OUT_WORD;
                    this.interpSteps = 45;
                    return;
                }
                return;
            case FADE_OUT_WORD:
                if (this.alpha <= 0.0f) {
                    this.state = AnswerState.FOUND;
                } else {
                    interpolation = Interpolation.fade;
                    i = this.interpSteps;
                    this.interpSteps = i - 1;
                    this.alpha = interpolation.apply(((float) i) / 45.0f);
                }
                this.color.a = this.alpha;
                return;
            case FOUND:
                hover();
                return;
            default:
                return;
        }
    }

    private void hover() {
        switch (this.hoverState) {
            case FIND_POSITION:
                this.targetHover.x = (this.home.x + ((this.rand.nextFloat() * 18.0f) * 2.0f)) - 18.0f;
                this.targetHover.y = (this.home.y + ((this.rand.nextFloat() * 18.0f) * 2.0f)) - 18.0f;
                this.hoverState = HoverState.TRAVEL;
                return;
            case TRAVEL:
                this.hoverDir.set(this.targetHover);
                this.hoverDir.sub(this.lastHover);
                this.lastHover.mulAdd(this.hoverDir, 0.1f / this.hoverDir.len());
                this.center.set(this.lastHover);
                if (this.lastHover.dst(this.targetHover) < 0.11f) {
                    this.hoverState = HoverState.FIND_POSITION;
                }
                this.upperLeft.x = this.center.x + this.offset.x;
                this.upperLeft.y = this.center.y + this.offset.y;
                return;
            default:
                return;
        }
    }

    public void render(SpriteBatch spriteBatch, BitmapFont fontAnswers, float scale) {
        if (this.state == AnswerState.FOUND) {
            fontAnswers.setColor(Color.BLACK.cpy());
            AnswerHelpers.drawLetters(spriteBatch, fontAnswers, this.center, scale, this.answer.word);
            return;
        }
        fontAnswers.getData().setScale(scale, scale);
        fontAnswers.setColor(this.color);
        fontAnswers.draw((Batch) spriteBatch, this.answer.word, this.upperLeft.x, this.upperLeft.y);
    }

    public void render(ShapeRenderer shapeRenderer, float scale) {
        if (this.state == AnswerState.FOUND) {
            AnswerHelpers.drawBlocks(shapeRenderer, this.center, scale, this.answer.word);
        }
    }
}
