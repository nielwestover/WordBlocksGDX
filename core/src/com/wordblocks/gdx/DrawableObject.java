package com.wordblocks.gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by a2558 on 2/21/2016.
 */
public class DrawableObject {
    Rectangle pos = new Rectangle();
    Rectangle targetPos = new Rectangle();
    float velocity;
    float angle;

    void update(){};
    void renderShapes(ShapeRenderer shapeRenderer){};
    void renderSprites(SpriteBatch spriteBatch, BitmapFont font){};
}
