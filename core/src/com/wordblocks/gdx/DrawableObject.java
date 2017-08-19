package com.wordblocks.gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class DrawableObject {
    float angle;
    Rectangle pos = new Rectangle();
    Rectangle targetPos = new Rectangle();
    float velocity;

    void update() {
    }

    void renderShapes(ShapeRenderer shapeRenderer) {
    }

    void renderSprites(SpriteBatch spriteBatch, BitmapFont font) {
    }
}
