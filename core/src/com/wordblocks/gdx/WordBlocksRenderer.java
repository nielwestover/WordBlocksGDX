package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
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
    private BitmapFont fontSelected;
    Camera camera;
    MyShapeRenderer shapeRenderer = new MyShapeRenderer();
    FPSLogger logger;
    GameScreen Overlord;

    public WordBlocksRenderer(float width, float height, Camera cam, GameScreen overlord) {
        Overlord = overlord;
        logger = new FPSLogger();
        this.width = width;
        this.height = height;
        camera = cam;

        spriteBatch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ARLRDBD.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 132;
        fontBlocks = generator.generateFont(parameter); // font size 12 pixels
        fontBlocks.setColor(Color.BLACK);

        parameter.size = 36;
        fontAnswers = generator.generateFont(parameter); // font size 12 pixels

        parameter.size = 60;
        fontSelected = generator.generateFont(parameter); // font size 12 pixels
        fontSelected.setColor(Color.WHITE);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        drawState = drawState.INIT;
    }

    enum DrawStates{
        INIT,
        RENDER
    }
    DrawStates drawState = DrawStates.INIT;

    public void init(){

        drawState = DrawStates.INIT;
    }
    Game game;
    public void draw(WordBlocksController wbc) {
        game = wbc.game;
        if (game == null)
            return;
        switch(drawState)
        {
            case INIT:
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

                drawRefresh(game.refresh);
                break;
        }
    }

    private void drawRefresh(Rectangle refresh){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 0, 1);
        shapeRenderer.identity();
        //shapeRenderer.translate(refresh.x, refresh.y, 0);
        shapeRenderer.rect(refresh.x, refresh.y, refresh.width, refresh.height);
        shapeRenderer.end();
    }
    SpriteBatch spriteBatch;
    private void drawAnswers(Game game){
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        fontSelected.draw(spriteBatch, game.selectedWord, width / 2, height - (width + 40));

        for (int i = 0; i < game.answers.size(); ++i){
            if (game.answers.get(i).found)
                fontAnswers.setColor(Color.GRAY);
            else
                fontAnswers.setColor(Color.WHITE);
            fontAnswers.draw(spriteBatch, game.answers.get(i).word, 6, height - (game.dims.screenWidth + 65 + 35 * i));
        }
        spriteBatch.end();

    }
    private void drawBlocks(Game game) {
        logger.log();
        //float inset = .13f * game.dims.boxDim;
        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                game.grid[i][j].block.render(shapeRenderer, spriteBatch, fontBlocks);

                //shapeRenderer.setColor(Color.ORANGE);
                //shapeRenderer.rect(game.grid[i][j].block.pos.x + inset, game.grid[i][j].block.pos.y + inset, game.grid[i][j].block.pos.width - 2*inset, game.grid[i][j].block.pos.height - 2*inset);
            }
        }


    }


}
