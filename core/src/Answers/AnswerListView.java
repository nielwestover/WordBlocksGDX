package Answers;

import Answers.DrawableAnswer.AnswerState;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.wordblocks.gdx.Game.Answer;
import java.util.ArrayList;
import java.util.Iterator;

public class AnswerListView {
    int curIndex = 0;
    int curSteps = 0;
    public ArrayList<DrawableAnswer> drawableAnswers;
    BitmapFont fontAnswers;
    AnswerListState state = AnswerListState.DISPLAY_LIST;
    public Rectangle view = new Rectangle(30.0f, 200.0f, 1140.0f, 533.25f);

    enum AnswerListState {
        DISPLAY_LIST,
        GAME,
        END_LEVEL
    }

    public AnswerListView(ArrayList<Answer> answers, BitmapFont fontAnswers) {
        this.fontAnswers = fontAnswers;
        this.state = AnswerListState.DISPLAY_LIST;
        int count = answers.size();
        this.drawableAnswers = new ArrayList();
        for (int i = 0; i < answers.size(); i++) {
            this.drawableAnswers.add(new DrawableAnswer((Answer) answers.get(i), AnswerHelpers.getPosition(((Answer) answers.get(i)).word, i, count, fontAnswers, this.view), AnswerHelpers.getCenterPosition(((Answer) answers.get(i)).word, i, count, this.view)));
        }
    }

    public void update() {
        switch (this.state) {
            case DISPLAY_LIST:
                if (this.curIndex < this.drawableAnswers.size()) {
                    if (((DrawableAnswer) this.drawableAnswers.get(this.curIndex)).state == AnswerState.NONE) {
                        ((DrawableAnswer) this.drawableAnswers.get(this.curIndex)).state = AnswerState.FADE_IN_WORD;
                    }
                    if (((double) ((DrawableAnswer) this.drawableAnswers.get(this.curIndex)).alpha) > 0.03d) {
                        this.curIndex++;
                        break;
                    }
                }
                else
                    this.state = AnswerListState.GAME;
                break;
        }
        Iterator it = this.drawableAnswers.iterator();
        while (it.hasNext()) {
            ((DrawableAnswer) it.next()).update();
        }
    }

    public void render(SpriteBatch spriteBatch, BitmapFont fontAnswers) {
        float scale = AnswerHelpers.getFontScaleForNumWords(this.drawableAnswers.size());
        Iterator it = this.drawableAnswers.iterator();
        while (it.hasNext()) {
            ((DrawableAnswer) it.next()).render(spriteBatch, fontAnswers, AnswerHelpers.getFontScaleForNumWords(this.drawableAnswers.size()));
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        Iterator it = this.drawableAnswers.iterator();
        while (it.hasNext()) {
            ((DrawableAnswer) it.next()).render(shapeRenderer, AnswerHelpers.getFontScaleForNumWords(this.drawableAnswers.size()));
        }
    }
}
