package com.wordblocks.gdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wordblocks.gdx.WordBlocksGDX;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;//800;
		config.height = 950;
		new LwjglApplication(new WordBlocksGDX(), config);
	}
}
