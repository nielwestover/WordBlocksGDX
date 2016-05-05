package Answers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.wordblocks.gdx.Game;

public class DrawableAnswer {
    enum AnswerState {
        NONE,
        FADE_IN_WORD,
        IDLE,
        FADE_IN_BLOCK
    }

    public Game.Answer answer;
    private int interpSteps = 0;
    private final int maxInterpSteps = 60;
    public float alpha = 0.f;
    public Color color = Color.WHITE.cpy();
    Vector2 center;

    public DrawableAnswer(Game.Answer answer, Vector2 pos) {
        this.answer = answer;
        color.a = alpha;
        this.center = pos;
    }

    AnswerState state = AnswerState.NONE;

    public void update() {
        switch (state) {
            case NONE:
                break;
            case FADE_IN_WORD:
                if (alpha == 1.f)
                    state = AnswerState.IDLE;
                else {
                    alpha = Interpolation.fade.apply(interpSteps++ / 100.f);
                }
                color.a = alpha;

                break;
            case IDLE:
                break;
        }
    }

    public void render(SpriteBatch spriteBatch, BitmapFont fontAnswers) {
            fontAnswers.setColor(color);
            fontAnswers.draw(spriteBatch, answer.word, center.x, center.y);

    }
}