package com.wordblocks.gdx;

import com.badlogic.gdx.math.Interpolation;

/**
 * Created by a2558 on 4/30/2016.
 */
public class HintLoop {
    static int steps = 0;
    static int hundredPercent = 120;
    static float maxPercent = .65f;
    static int maxSteps = (int)(maxPercent * hundredPercent);
    static int minSteps = 0;
    static boolean isIncreasing = true;
    static float val = 0;

    static void loopHintColors() {
        if (isIncreasing) {
            steps++;
            if (steps > maxSteps)
                isIncreasing = false;
        } else {
            steps--;
            if (steps < minSteps )
                isIncreasing = true;
        }
        val = Interpolation.sineOut.apply(steps / 120.f);
    }

    static void reset() {
        steps = 0;
        val = 0;
        isIncreasing = true;
    }
}
