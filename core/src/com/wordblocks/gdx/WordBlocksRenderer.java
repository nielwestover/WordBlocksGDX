package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Answers.DrawableAnswer;
import helpers.MyShapeRenderer;
import particles.ParticleManager;


/**
 * Created by a2558 on 2/21/2016.
 */
public class WordBlocksRenderer {
    private float width;
    private float height;


    Camera camera;
    MyShapeRenderer shapeRenderer = new MyShapeRenderer();
    FPSLogger logger;
    GameScreen Overlord;

    public void setCamera(Camera cam) {
        camera = cam;
    }

    public WordBlocksRenderer(float width, float height, GameScreen overlord) {
        Overlord = overlord;
        logger = new FPSLogger();
        this.width = width;
        this.height = height;

        spriteBatch = new SpriteBatch();

        drawState = drawState.INIT;
    }

    enum DrawStates {
        INIT,
        RENDER
    }

    DrawStates drawState = DrawStates.INIT;

    public void init() {

        drawState = DrawStates.INIT;
    }



    Game game;
    WordBlocksController wbc;

    public void draw(WordBlocksController wbc) {
        this.wbc = wbc;
        game = wbc.game;
        if (game == null)
            return;
        switch (drawState) {
            case INIT:
                for (int i = 0; i < game.grid.length; ++i) {
                    for (int j = 0; j < game.grid[i].length; ++j) {
                        if (game.grid[i][j].block == null)
                            continue;

                        GlyphLayout glyphLayout = new GlyphLayout();
                        String item = game.grid[i][j].block.letter + "";
                        glyphLayout.setText(wbc.fontBlocks, item);
                        game.grid[i][j].block.letterWidth = glyphLayout.width;
                        game.grid[i][j].block.letterHeight = glyphLayout.height;

                        game.grid[i][j].cellPos =
                                new Rectangle(
                                        (int) (game.dims.padding + (game.dims.boxGap + game.dims.boxDim) * i),
                                        height - game.dims.topPadding - (int) ((game.dims.boxGap + game.dims.boxDim) * (j + 1)),
                                        game.dims.boxDim,
                                        game.dims.boxDim);
                        game.grid[i][j].block.pos.set(game.grid[i][j].cellPos);
                    }
                }
                drawState = DrawStates.RENDER;
                break;
            case RENDER:
                //draw particles
                drawParticles();

                //Draw blocks
                drawBlocks(game);

                //Draw answers
                drawAnswers(game);

                drawButtons();

                drawFPSCounter();

                break;
        }
    }

    private void drawParticles() {
        spriteBatch.begin();
        ParticleManager.Inst().update();
        ParticleManager.Inst().render(spriteBatch);
        spriteBatch.end();
    }

    private void drawFPSCounter() {
        wbc.fontAnswers.getData().setScale(65.0f / 128.0f);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        float x = width - 300;
        float y = camera.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = wbc.fontAnswers;
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(spriteBatch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
        spriteBatch.end();
    }

    private void drawButtons() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 0, 1);
        shapeRenderer.identity();
        //shapeRenderer.translate(refresh.x, refresh.y, 0);
        shapeRenderer.rect(game.refresh.x, game.refresh.y, game.refresh.width, game.refresh.height);

        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(game.skipNext.x, game.skipNext.y, game.skipNext.width, game.skipNext.height);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(game.giveHint.x, game.giveHint.y, game.giveHint.width, game.giveHint.height);
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        wbc.fontAnswers.getData().setScale(65.0f / 128.0f);
        wbc.fontAnswers.setColor(Color.WHITE);
        wbc.fontAnswers.draw(spriteBatch, (MyApplication.curPackIndex + 1) + " : " + (MyApplication.curLevelIndex + 1) + "", 160.0f, 50.0f);

        spriteBatch.end();
    }

    SpriteBatch spriteBatch;
    GlyphLayout glyphLayout = new GlyphLayout();

    private void drawAnswers(Game game) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        //DRAW SELECTED WORD
        if (game.selectedWord.length() > 0) {
            glyphLayout.setText(wbc.fontCurWord, game.selectedWord);
            wbc.fontCurWord.draw(spriteBatch, game.selectedWord, width / 2 - glyphLayout.width / 2, height - (width + 40));
        }

        //DRAW answers
        wbc.answerView.render(spriteBatch, wbc.fontAnswers);

        spriteBatch.end();
    }

    private void drawBlocks(Game game) {
        logger.log();
        if (wbc.hintSystem != null && wbc.hintSystem.getHintIndex() > 0)
            Block.hintLoop.loopHintColors();
        //float inset = .13f * game.dims.boxDim;
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Gdx.gl.glEnable(GL20.GL_BLEND);

        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                wbc.fontBlocks.getData().setScale(wbc.origBlockFontScale);
                game.grid[i][j].block.renderShapes(shapeRenderer);

                //UNCOMMENT TO DISPLAY YELLOW ANSWER BOX!
//                shapeRenderer.setColor(1, 1, 0, 1);
//                shapeRenderer.identity();
//                shapeRenderer.rect(wbc.answerView.view.x, wbc.answerView.view.y, wbc.answerView.view.width, wbc.answerView.view.height);

            }
        }
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                wbc.fontBlocks.getData().setScale(wbc.origBlockFontScale);
                game.grid[i][j].block.renderSprites(spriteBatch, wbc.fontBlocks);
            }
        }
        spriteBatch.end();
    }
}
