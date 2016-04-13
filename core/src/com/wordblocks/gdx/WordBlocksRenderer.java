package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import helpers.MyShapeRenderer;


/**
 * Created by a2558 on 2/21/2016.
 */
public class WordBlocksRenderer {
    private float width;
    private float height;


    private BitmapFont fontBlocks;
    private BitmapFont fontAnswers;
    private BitmapFont fontCurWord;
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
        FreeTypeFontGenerator generatorBlocks = new FreeTypeFontGenerator(Gdx.files.internal("fonts/lucon.ttf"));
        //FreeTypeFontGenerator generatorText = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AGENCYB.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 128;
        fontBlocks = generatorBlocks.generateFont(parameter);
        fontBlocks.setColor(Color.BLACK);

        fontAnswers = generatorBlocks.generateFont(parameter);

        fontCurWord = generatorBlocks.generateFont(parameter);
        fontCurWord.setColor(Color.WHITE);

        fontBlocks.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fontCurWord.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fontAnswers.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        generatorBlocks.dispose(); // don't forget to dispose to avoid memory leaks!
        //generatorText.dispose(); // don't forget to dispose to avoid memory leaks!
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

    float getBlockFontScale() {
        //This is smallest the block font will be
        float origScale = 110.0f / 128.0f;//Based on 7x7, 160 pixel blocks
        //So scale it based on how much larger the current boxDim is than the original 160
        float scaleFactor = game.dims.boxDim / 160.0f;
        return origScale * scaleFactor;
    }

    Game game;
    float origBlockFontScale;

    public void draw(WordBlocksController wbc) {
        game = wbc.game;
        if (game == null)
            return;
        switch (drawState) {
            case INIT:
                float blockFontSize = getBlockFontScale();
                origBlockFontScale = blockFontSize;
                fontBlocks.getData().setScale(blockFontSize, blockFontSize);
                fontCurWord.getData().setScale(100.0f / 128.0f);
                fontAnswers.getData().setScale(65.0f / 128.0f);

                for (int i = 0; i < game.grid.length; ++i) {
                    for (int j = 0; j < game.grid[i].length; ++j) {
                        if (game.grid[i][j].block == null)
                            continue;

                        GlyphLayout glyphLayout = new GlyphLayout();
                        String item = game.grid[i][j].block.letter + "";
                        glyphLayout.setText(fontBlocks, item);
                        game.grid[i][j].block.letterWidth = glyphLayout.width;
                        game.grid[i][j].block.letterHeight = glyphLayout.height;

                        game.grid[i][j].cellPos =
                                new Rectangle(
                                        (int) (game.dims.padding + (game.dims.boxGap + game.dims.boxDim) * i),
                                        height - (int) ((game.dims.boxGap + game.dims.boxDim) * (j + 1)),
                                        game.dims.boxDim,
                                        game.dims.boxDim);
                        game.grid[i][j].block.pos.set(game.grid[i][j].cellPos);
                    }
                }
                drawState = DrawStates.RENDER;
                break;
            case RENDER:
                //Draw blocks
                drawBlocks(game);

                //Draw answers
                drawAnswers(game);

                drawButtons();

                drawFPSCounter();
                break;
        }
    }

    private void drawFPSCounter() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        float x = width - 300;
        float y = camera.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = fontAnswers;
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
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        fontAnswers.setColor(Color.WHITE);
        fontAnswers.draw(spriteBatch, (MyApplication.curLevelIndex + 1) + "", 160.0f, 50.0f);

        spriteBatch.end();
    }

    SpriteBatch spriteBatch;
    GlyphLayout glyphLayout = new GlyphLayout();

    private void drawAnswers(Game game) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        if (game.selectedWord.length() > 0) {
            glyphLayout.setText(fontCurWord, game.selectedWord);
            fontCurWord.draw(spriteBatch, game.selectedWord, width / 2 - glyphLayout.width / 2, height - (width + 40));
        }

        for (int i = 0; i < game.answers.size(); ++i) {
            if (game.answers.get(i).found)
                fontAnswers.setColor(Color.GRAY);
            else
                fontAnswers.setColor(Color.WHITE);
            fontAnswers.draw(spriteBatch, game.answers.get(i).word, 6, height - (game.dims.screenWidth + 125 + 65 * i));
        }
        spriteBatch.end();
    }

    private void drawBlocks(Game game) {
        logger.log();
        //float inset = .13f * game.dims.boxDim;
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                fontBlocks.getData().setScale(origBlockFontScale);
                game.grid[i][j].block.renderShapes(shapeRenderer);
            }
        }
        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                fontBlocks.getData().setScale(origBlockFontScale);
                game.grid[i][j].block.renderSprites(spriteBatch, fontBlocks);
            }
        }
        spriteBatch.end();
    }
}
