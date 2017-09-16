package com.wordblocks.gdx;

		import android.os.Bundle;

		import com.badlogic.gdx.backends.android.AndroidApplication;
		import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
		import com.wordblocks.gdx.WordBlocksGDX;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.numSamples = 4;
		initialize(new WordBlocksGDX(), config);
	}
}
