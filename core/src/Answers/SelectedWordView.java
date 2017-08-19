package Answers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Interpolation.Pow;
import com.badlogic.gdx.math.Vector2;
import com.wordblocks.gdx.Game;

public class SelectedWordView {
    public AnswerListView answerView;
    Vector2 center;
    Vector2 curPos = new Vector2();
    Game game;
    int interpSteps = 0;
    float percent = 1.0f;
    float scale;
    float startScale = 1.3f;
    public AnimState state = AnimState.DISPLAY_SELECTION;
    Vector2 targetPos = new Vector2();
    float targetScale;
    Vector2 tmpCenter = new Vector2();
    Vector2 tmpTarget = new Vector2();
    String word;

    public enum AnimState {
        DISPLAY_SELECTION,
        MOVE_TO_FOUND,
        MOVE_TO_FOUND_ANIM
    }

    public void update() {
        switch (this.state) {
            case MOVE_TO_FOUND:
                this.interpSteps = 50;
                this.targetScale = AnswerHelpers.getFontScaleForNumWords(this.game.answers.size());
                this.targetPos.set(((DrawableAnswer) this.answerView.drawableAnswers.get(AnswerHelpers.getWordIndex(this.game.selectedWord, this.game.answers))).center);
                this.state = AnimState.MOVE_TO_FOUND_ANIM;
                this.curPos.set(this.center);
                this.word = this.game.selectedWord;
                update();
                return;
            case MOVE_TO_FOUND_ANIM:
                Pow pow = Interpolation.pow3;
                int i = this.interpSteps - 1;
                this.interpSteps = i;
                this.percent = pow.apply(((float) i) / 50.0f);
                if (this.percent < 0.0f) {
                    this.percent = 0.0f;
                }
                this.tmpTarget.set(this.targetPos);
                this.tmpTarget = this.tmpTarget.sub(this.center);
                this.tmpCenter.set(this.center);
                this.curPos = this.tmpCenter.add(this.tmpTarget.scl(1.0f - this.percent));
                this.scale = this.targetScale + (this.percent * (this.startScale - this.targetScale));
                if (this.interpSteps == 0) {
                    this.state = AnimState.DISPLAY_SELECTION;
                    return;
                }
                return;
            default:
                return;
        }
    }

    public SelectedWordView(Game game, AnswerListView answerView) {
        this.answerView = answerView;
        this.game = game;
        this.center = new Vector2(600.0f, 800.0f);
    }

    public void render(SpriteBatch spriteBatch, BitmapFont fontAnswers) {
        fontAnswers.setColor(Color.BLACK.cpy());
        if (this.state == AnimState.DISPLAY_SELECTION && this.game.selectedWord.length() > 0) {
            AnswerHelpers.drawLetters(spriteBatch, fontAnswers, this.center, 1.3f, this.game.selectedWord);
        } else if (this.state == AnimState.MOVE_TO_FOUND_ANIM) {
            AnswerHelpers.drawLetters(spriteBatch, fontAnswers, this.curPos, this.scale, this.word);
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (this.state == AnimState.DISPLAY_SELECTION && this.game.selectedWord.length() > 0) {
            AnswerHelpers.drawBlocks(shapeRenderer, this.center, 1.3f, this.game.selectedWord);
        } else if (this.state == AnimState.MOVE_TO_FOUND_ANIM) {
            AnswerHelpers.drawBlocks(shapeRenderer, this.curPos, this.scale, this.word);
        }
    }
}
