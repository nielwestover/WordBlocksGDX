package com.wordblocks.gdx;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(1);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.numSamples = 4;
        initialize(new WordBlocksGDX(), config);
    }
}
