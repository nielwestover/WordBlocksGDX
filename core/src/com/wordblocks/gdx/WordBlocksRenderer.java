package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import helpers.MyShapeRenderer;
import particles.ParticleManager;

public class WordBlocksRenderer {
    GameScreen Overlord;
    Camera camera;
    DrawStates drawState = DrawStates.INIT;
    Game game;
    GlyphLayout glyphLayout = new GlyphLayout();
    private float height;
    String levelString = BuildConfig.FLAVOR;
    FPSLogger logger;
    MyShapeRenderer shapeRenderer = new MyShapeRenderer();
    SpriteBatch spriteBatch;
    WordBlocksController wbc;
    private float width;

    enum DrawStates {
        INIT,
        RENDER
    }

    public void setCamera(Camera cam) {
        this.camera = cam;
    }

    public WordBlocksRenderer(float width, float height, GameScreen overlord) {
        this.Overlord = overlord;
        this.logger = new FPSLogger();
        this.width = width;
        this.height = height;
        this.spriteBatch = new SpriteBatch();
        DrawStates drawStates = this.drawState;
        this.drawState = DrawStates.INIT;
    }

    public void init() {
        this.drawState = DrawStates.INIT;
    }

    public void draw(WordBlocksController wbc) {
        this.wbc = wbc;
        this.game = wbc.game;
        if (this.game != null) {
            switch (this.drawState) {
                case INIT:
                    this.drawState = DrawStates.RENDER;
                    return;
                case RENDER:
                    drawParticlesBackground();
                    drawBlocks(this.game);
                    drawAnswers(this.game);
                    drawButtons();
                    drawParticlesForeground();
                    drawFPSCounter();
                    return;
                default:
                    return;
            }
        }
    }

    private void drawParticlesBackground() {
        this.spriteBatch.begin();
        ParticleManager.Inst().update();
        ParticleManager.Inst().renderBackground(this.spriteBatch);
        this.spriteBatch.end();
    }

    private void drawParticlesForeground() {
        this.spriteBatch.begin();
        ParticleManager.Inst().update();
        ParticleManager.Inst().renderForeground(this.spriteBatch);
        this.spriteBatch.end();
    }

    private void drawFPSCounter() {
        this.wbc.fontAnswers.getData().setScale(0.5078125f);
        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.spriteBatch.begin();
        float x = this.width - 300.0f;
        float y = this.camera.viewportHeight - 15.0f;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = this.wbc.fontAnswers;
        if (fps >= 45) {
            fpsFont.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        } else if (fps >= 30) {
            fpsFont.setColor(1.0f, 1.0f, 0.0f, 1.0f);
        } else {
            fpsFont.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        }
        //fpsFont.draw(this.spriteBatch, "FPS: " + fps, x, y);
        this.wbc.fontCurWord.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.levelString = (MyApplication.curPackIndex + 1) + " - " + (MyApplication.curLevelIndex + 1) + BuildConfig.FLAVOR;
        this.wbc.fontCurWord.getData().setScale(0.8f);
        this.glyphLayout.setText(this.wbc.fontCurWord, this.levelString);
        this.wbc.fontCurWord.draw(this.spriteBatch, this.levelString, (this.width / 2.0f) - (this.glyphLayout.width / 2.0f), this.height - 25.0f);
        this.spriteBatch.end();
    }

    private void drawButtons() {
        this.shapeRenderer.begin(ShapeType.Line);
        this.shapeRenderer.setColor(1.0f, 1.0f, 0.0f, 1.0f);
        this.shapeRenderer.identity();
        this.shapeRenderer.rect(this.game.refresh.x, this.game.refresh.y, this.game.refresh.width, this.game.refresh.height);
        this.shapeRenderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        this.shapeRenderer.rect(this.game.giveHint.x, this.game.giveHint.y, this.game.giveHint.width, this.game.giveHint.height);
        this.shapeRenderer.end();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.spriteBatch.begin();
        this.wbc.fontCurWord.getData().setScale(0.7f);
        this.wbc.fontCurWord.setColor(Color.WHITE);
        this.glyphLayout.setText(this.wbc.fontCurWord, "HINT");
        this.wbc.fontCurWord.draw(this.spriteBatch, (CharSequence) "HINT", 175.0f - (this.glyphLayout.width / 2.0f), (this.glyphLayout.height / 2.0f) + 75.0f);
        this.glyphLayout.setText(this.wbc.fontCurWord, "RETRY");
        this.wbc.fontCurWord.draw(this.spriteBatch, (CharSequence) "RETRY", (this.width - 175.0f) - (this.glyphLayout.width / 2.0f), (this.glyphLayout.height / 2.0f) + 75.0f);
        this.spriteBatch.end();
    }

    private void drawAnswers(Game game) {
        this.shapeRenderer.begin(ShapeType.Filled);
        this.shapeRenderer.identity();
        this.shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.wbc.selectedWordView.render(this.shapeRenderer);
        this.wbc.answerView.render(this.shapeRenderer);
        this.shapeRenderer.end();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.spriteBatch.begin();
        this.wbc.selectedWordView.render(this.spriteBatch, this.wbc.fontAnswers);
        this.wbc.answerView.render(this.spriteBatch, this.wbc.fontAnswers);
        this.spriteBatch.end();
    }

    private void drawBlocks(Game game) {
        int i;
        int j;
        //this.logger.log();
        if (this.wbc.hintSystem != null && this.wbc.hintSystem.getHintIndex() > 0) {
            HintLoop hintLoop = Block.hintLoop;
            HintLoop.loopHintColors();
        }
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.begin(ShapeType.Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        for (i = 0; i < game.grid.length; i++) {
            for (j = 0; j < game.grid[i].length; j++) {
                if (game.grid[i][j].block != null) {
                    this.wbc.fontBlocks.getData().setScale(this.wbc.origBlockFontScale);
                    game.grid[i][j].block.renderShapes(this.shapeRenderer);
                }
            }
        }
        this.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.spriteBatch.begin();
        for (i = 0; i < game.grid.length; i++) {
            for (j = 0; j < game.grid[i].length; j++) {
                if (game.grid[i][j].block != null) {
                    this.wbc.fontBlocks.getData().setScale(this.wbc.origBlockFontScale);
                    game.grid[i][j].block.renderSprites(this.spriteBatch, this.wbc.fontBlocks);
                }
            }
        }
        this.spriteBatch.end();
    }
}
