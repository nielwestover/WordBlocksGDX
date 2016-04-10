package com.wordblocks.gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import helpers.IntPair;

/**
 * Created by a2558 on 2/21/2016.
 */
public class DrawableObject {
    Rectangle pos = new Rectangle();
    Rectangle targetPos = new Rectangle();
    float velocity;

    void update(){};
    void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, BitmapFont font){};
}
