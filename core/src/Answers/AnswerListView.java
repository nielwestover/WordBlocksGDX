package Answers;

import com.badlogic.gdx.math.Interpolation;
import com.wordblocks.gdx.Game;
import java.util.ArrayList;

public class AnswerListView {
    public AnswerListView(ArrayList<Game.Answer> answers){
        state = AnswerListState.DISPLAY_LIST;

        int count = answers.size();

        drawableAnswers = new ArrayList<DrawableAnswer>();
        for (int i = 0; i < answers.size(); ++i){
            drawableAnswers.add(new DrawableAnswer(answers.get(i), i, count ));
        }
    }

    public ArrayList<DrawableAnswer> drawableAnswers;

    enum AnswerListState{
        DISPLAY_LIST,
        GAME,
        END_LEVEL
    }
    AnswerListState state = AnswerListState.DISPLAY_LIST;
    int curIndex = 0;
    int curSteps = 0;
    public void update(){

        switch(state){
            case DISPLAY_LIST:
                //done once we've gone through all the indices
                if (curIndex >= drawableAnswers.size())
                    state = AnswerListState.GAME;
                else {
                    //if cur word is in NONE state, then it needs to start fading in
                    if (drawableAnswers.get(curIndex).state == DrawableAnswer.AnswerState.NONE)
                        drawableAnswers.get(curIndex).state = DrawableAnswer.AnswerState.FADE_IN_WORD;
                    //if cur word is in IDLE state, then it's done fading, and let's move to the next word
                    if (drawableAnswers.get(curIndex).alpha > .8)
                        ++curIndex;
                }
            break;
            case GAME:
                break;
        }

        for ( DrawableAnswer da : drawableAnswers) {
            da.update();
        }
    }

    public void render(){
        for ( DrawableAnswer da : drawableAnswers) {
            da.render();
        }
    }
}


