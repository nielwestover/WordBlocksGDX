package Answers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.wordblocks.gdx.Game;

public class DrawableAnswer {
    enum AnswerState{
        NONE,
        FADE_IN_WORD,
        IDLE,
        FADE_IN_BLOCK
    };
    public Game.Answer answer;
    private int interpSteps = 0;
    private final int maxInterpSteps = 60;
    public float alpha = 0.f;
    public Color color = Color.WHITE.cpy();

    public DrawableAnswer(Game.Answer answer, int index, int total){
        this.answer = answer;
        color.a = alpha;
    }

    AnswerState state = AnswerState.NONE;
    public void update(){
        switch (state){
            case NONE:
                break;
            case FADE_IN_WORD:
                if (alpha == 1.f)
                    state = AnswerState.IDLE;
                else {
                    alpha = Interpolation.circleOut.apply(interpSteps++ / 30.f);
                }
                color.a = alpha;

                break;
            case IDLE:
                break;
        }
    }
    public void render(){

    }
}