package Answers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wordblocks.gdx.Game;
import com.wordblocks.gdx.GameScreen;

import java.util.ArrayList;

public class AnswerListView {
    public AnswerListView(ArrayList<Game.Answer> answers, BitmapFont fontAnswers) {
        view = new Rectangle(30, 200, GameScreen.worldWidth-60, .25f * GameScreen.worldHeight);
        this.fontAnswers = fontAnswers;

        state = AnswerListState.DISPLAY_LIST;

        int count = answers.size();

        drawableAnswers = new ArrayList<DrawableAnswer>();
        for (int i = 0; i < answers.size(); ++i) {
            Vector2 pos = getPosition(answers.get(i).word, i, count);
            drawableAnswers.add(new DrawableAnswer(answers.get(i), pos));
        }
    }

    BitmapFont fontAnswers;
    public Rectangle view;
    GlyphLayout glyphLayout = new GlyphLayout();

    private float getFontScaleForNumWords(int count) {
        switch (count) {
            case 1:
                return 1.3f;
            case 2:
                return 1.1f;
            case 6:
            case 3:
                return .9f;
            case 8:
            case 7:
            case 4:
                return .7f;
            default:
            case 9:
            case 10:
            case 5:
                return .65f;

        }
    }

    private Vector2 getPosition(String word, int index, int total) {
        Vector2 pos = new Vector2();
        float scale = getFontScaleForNumWords(total);

        fontAnswers.getData().setScale(scale, scale);
        //same for all cases
        switch (total) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                float div = view.height/(2.f * total);
                pos.x = view.width / 2.f;
                pos.y = view.y + (2*(total - index) - 1)*div;
                //pos.y = view.y + (float)(total - index) * .33333333f * view.height;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                int newTotal = total/2;
                if (total%2 == 1)
                    newTotal += 1;
                div = view.height/(2.f * newTotal);
                pos.y = view.y + (2*(newTotal - index%newTotal) - 1)*div;
                if (index < newTotal) {
                    pos.x = view.width / 4.f;
                }
                else {
                    pos.x = 3.f * view.width / 4.f;
                }

        }
        glyphLayout.setText(fontAnswers, word);
        pos.x = pos.x - glyphLayout.width / 2;
        pos.y = pos.y + glyphLayout.height / 2;
        return pos;
    }

    public ArrayList<DrawableAnswer> drawableAnswers;

    enum AnswerListState {
        DISPLAY_LIST,
        GAME,
        END_LEVEL
    }

    AnswerListState state = AnswerListState.DISPLAY_LIST;
    int curIndex = 0;
    int curSteps = 0;

    public void update() {

        switch (state) {
            case DISPLAY_LIST:
                //done once we've gone through all the indices
                if (curIndex >= drawableAnswers.size())
                    state = AnswerListState.GAME;
                else {
                    //if cur word is in NONE state, then it needs to start fading in
                    if (drawableAnswers.get(curIndex).state == DrawableAnswer.AnswerState.NONE)
                        drawableAnswers.get(curIndex).state = DrawableAnswer.AnswerState.FADE_IN_WORD;
                    //if cur word is in IDLE state, then it's done fading, and let's move to the next word
                    if (drawableAnswers.get(curIndex).alpha > .03)
                        ++curIndex;
                }
                break;
            case GAME:
                break;
        }

        for (DrawableAnswer da : drawableAnswers) {
            da.update();
        }
    }

    public void render(SpriteBatch spriteBatch, BitmapFont fontAnswers) {
        float scale = getFontScaleForNumWords(drawableAnswers.size());
        fontAnswers.getData().setScale(scale, scale);
        for (DrawableAnswer da : drawableAnswers) {
            da.render(spriteBatch, fontAnswers);
        }
    }
}


