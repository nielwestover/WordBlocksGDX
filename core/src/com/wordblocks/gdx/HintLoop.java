package com.wordblocks.gdx;

import com.badlogic.gdx.math.Interpolation;

public class HintLoop {
    static int hundredPercent = 120;
    static boolean isIncreasing = true;
    static float maxPercent = 0.65f;
    static int maxSteps = ((int) (maxPercent * ((float) hundredPercent)));
    static int minSteps = 0;
    static int steps = 0;
    static float val = 0.0f;

    static void loopHintColors() {
        if (isIncreasing) {
            steps++;
            if (steps > maxSteps) {
                isIncreasing = false;
            }
        } else {
            steps--;
            if (steps < minSteps) {
                isIncreasing = true;
            }
        }
        val = Interpolation.sineOut.apply(((float) steps) / 120.0f);
    }

    static void reset() {
        steps = 0;
        val = 0.0f;
        isIncreasing = true;
    }
}
