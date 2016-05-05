package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;

import helpers.RowColPair;

/**
 * Created by a2558 on 2/21/2016.
 */
public class WordBlocksController {

    public boolean fingerPress = false;
    public boolean fingerMoving = false;
    public float X;
    public float Y;
    private RowColPair selectedBlock;
    private RowColPair previousSelectedBlock;
    public BitmapFont fontBlocks;
    public BitmapFont fontAnswers;
    public BitmapFont fontCurWord;

    Game game;

    public enum GameStates {
        INIT,
        WAIT_FOR_PRESS,
        FINGER_DOWN,
        MOVING,
        FINGER_UP,
        ANIMATE_DISSOLVE,
        ANIMATE_DROP_BLOCKS,
        CHECK_LEVEL_FINISHED,
        GAME_OVER
    }

    public float width, height;
    GameScreen Overlord;


    public WordBlocksController(float width, float height, GameScreen overlord) {
        Overlord = overlord;
        this.width = width;
        this.height = height;
    }

    public float origBlockFontScale;
    float getBlockFontScale() {
        //This is smallest the block font will be
        float origScale = 110.0f / 128.0f;//Based on 7x7, 160 pixel blocks
        //So scale it based on how much larger the current boxDim is than the original 160
        float scaleFactor = game.dims.boxDim / 160.0f;
        return origScale * scaleFactor;
    }


    public GameStates gameState = GameStates.INIT;

    public void init() {
        gameState = GameStates.INIT;
    }

    private void initFonts(){
        //FreeTypeFontGenerator generatorAnswers = new FreeTypeFontGenerator(Gdx.files.internal("fonts/lucon.ttf"));
        FreeTypeFontGenerator generatorBlocks = new FreeTypeFontGenerator(Gdx.files.internal("fonts/calibrib.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 115;
        fontBlocks = generatorBlocks.generateFont(parameter);
        fontBlocks.setColor(Color.BLACK);

        fontCurWord = generatorBlocks.generateFont(parameter);
        fontCurWord.setColor(Color.WHITE);

        fontAnswers = generatorBlocks.generateFont(parameter);

        fontBlocks.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fontCurWord.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fontAnswers.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        float blockFontSize = getBlockFontScale();
        origBlockFontScale = blockFontSize;
        fontBlocks.getData().setScale(blockFontSize, blockFontSize);
        fontCurWord.getData().setScale(100.0f / 128.0f);
        fontAnswers.getData().setScale(65.0f / 128.0f);

        generatorBlocks.dispose(); // don't forget to dispose to avoid memory leaks!
        //generatorAnswers.dispose(); // don't forget to dispose to avoid memory leaks!
    }

    public void update() {
        switch (gameState) {
            case INIT:
                game = new Game(MyApplication.getCurLevel());
                initGameDimensions();

                initFonts();
                answerView = new Answers.AnswerListView(game.answers, fontAnswers);
                game.refresh = new Rectangle(width - 150, 0, 150, 150);
                game.skipNext = new Rectangle(0, 0, 150, 150);
                game.giveHint = new Rectangle(width / 2 - 75, 0, 150, 150);
                hintSystem = null;
                Block.hintLoop.reset();


                //always reinitialize the renderer when starting over
                Overlord.wordBlocksRenderer.init();
                gameState = GameStates.WAIT_FOR_PRESS;
                break;
            case WAIT_FOR_PRESS:
                if (fingerPress) {
                    selectedBlock = new RowColPair(-1, -1);
                    previousSelectedBlock = new RowColPair(-1, -1);
                    gameState = GameStates.FINGER_DOWN;
                    update();
                }
                break;
            case FINGER_DOWN:
                doBlockLogic();
                if (fingerMoving || !fingerPress) {
                    gameState = GameStates.MOVING;
                    update();
                }
                break;
            case MOVING:
                if (!fingerPress) {
                    gameState = GameStates.FINGER_UP;
                }
                doBlockLogic();
                break;
            case FINGER_UP:
                if (game.getSelectedWord(game.selectedWord) != null) {
                    gameState = GameStates.ANIMATE_DISSOLVE;
                    setBlocksToDissolve();
                } else {
                    game.deselectAll();
                    gameState = GameStates.WAIT_FOR_PRESS;
                }
                break;
            case ANIMATE_DISSOLVE:
                if (!blocksStillDissolving()) {
                    gameState = GameStates.GAME_OVER.ANIMATE_DROP_BLOCKS;
                    game.removeWordFound();
                    game.deselectAll();
                    hintSystem = null;
                    removeHints();
                    game.dropBlocks();
                    setBlocksToFall();
                }
                break;
            case ANIMATE_DROP_BLOCKS:
                //runs until finished
                if (!blocksStillFalling())
                    gameState = GameStates.CHECK_LEVEL_FINISHED;
                break;
            case CHECK_LEVEL_FINISHED:
                if (game.blocksLeft() == 0) {
                    if (MyApplication.incrementCurLevel()) {
                        gameState = GameStates.INIT;
                    } else
                        gameState = GameStates.GAME_OVER;
                } else
                    gameState = GameStates.WAIT_FOR_PRESS;
            case GAME_OVER:
                break;
        }

        answerView.update();
        updateBlocks();
    }
    Answers.AnswerListView answerView;

    private void setBlocksToFall() {
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                game.grid[i][j].block.setCurState(Block.AnimState.FALLING);
                game.grid[i][j].block.targetPos = game.grid[i][j].cellPos;
            }
        }
    }

    private void setBlocksToDissolve() {
        for (RowColPair rc : game.selectedChain)
            game.grid[rc.Row][rc.Col].block.setCurState(Block.AnimState.DISSOLVING);

    }

    private void updateBlocks() {
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                game.grid[i][j].block.update();
            }
        }
    }

    private boolean blocksStillFalling() {
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                if (game.grid[i][j].block.getCurState() == Block.AnimState.FALLING)
                    return true;
            }
        }
        return false;
    }

    private boolean blocksStillDissolving() {
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                if (game.grid[i][j].block.getCurState() == Block.AnimState.DISSOLVING)
                    return true;
            }
        }
        return false;
    }

    public void initGameDimensions() {
        game.dims.screenWidth = width;

        game.dims.boxGap = (25 - 2 * game.grid.length);
        game.dims.padding = 40;
        game.dims.boxDim = (game.dims.screenWidth - (game.dims.padding * 2 + game.dims.boxGap * (game.grid.length - 1))) / (game.grid.length * 1.0f);
        game.dims.inset = .13f * game.dims.boxDim;
        game.dims.topPadding = 100;
    }

    private void doBlockLogic() {
        findSelectedBlock();
        if (isSelectedBlockValid())
            setSelectedBlock();
    }

    private boolean isSelectedBlockValid() {
        //initial case
        if (previousSelectedBlock.Row == -1) {
            previousSelectedBlock = selectedBlock;
            return true;
        }
        if (selectedBlock == previousSelectedBlock)
            return false;
        if (game.grid[selectedBlock.Row][selectedBlock.Col].block.getSelected())
            return false;
        int xDist = Math.abs(selectedBlock.Row - previousSelectedBlock.Row);
        int yDist = Math.abs(selectedBlock.Col - previousSelectedBlock.Col);
        if (xDist > 1 || yDist > 1)
            return false;
        previousSelectedBlock = selectedBlock;
        return true;
    }

    private void findSelectedBlock() {
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                if (game.grid[i][j].block.isTouched(X, Y, game.dims.inset))
                    selectedBlock = new RowColPair(i, j);

            }
        }
    }

    private void setSelectedBlock() {
        int i = selectedBlock.Row;
        int j = selectedBlock.Col;
        if (i == -1 || j == -1)
            return;
        if (game.grid[i][j].block != null && !game.grid[i][j].block.getSelected()) {
            game.grid[i][j].block.setSelected(true);
            game.selectedWord = game.selectedWord.concat(game.grid[i][j].block.letter + "");
            game.selectedChain.add(new RowColPair(i, j));
        }
    }

    public void giveHint() {
        if (hintSystem == null)
            hintSystem = new HintSystem(game);
        int id = hintSystem.getHint();
        //check for error codes -1 and -2
        if (id >= 0) {
            Block b = game.getBlockByID(id);
            if (b != null)
                b.setIsHint(true);
        }
    }

    public HintSystem hintSystem = null;

    public void removeHints() {
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                game.grid[i][j].block.setIsHint(false);
            }
        }
    }

}
