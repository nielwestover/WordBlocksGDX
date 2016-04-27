package com.wordblocks.gdx;

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
    Game game;

    public enum GameStates {
        INIT,
        WAIT_FOR_PRESS,
        FINGER_DOWN,
        MOVING,
        FINGER_UP,
        ANIMATE_DROP_BLOCKS,
        CHECK_LEVEL_FINISHED,
        GAME_OVER
    }
    public float width, height;
    GameScreen Overlord;

    public WordBlocksController(float width, float height, GameScreen overlord){
        Overlord = overlord;
        this.width = width;
        this.height = height;
    }
    public GameStates gameState = GameStates.INIT;

    public void init(){
        gameState = GameStates.INIT;
    }

    public void update() {
        switch (gameState) {
            case INIT:
                game = new Game(MyApplication.getCurLevel());
                game.refresh = new Rectangle(width - 150, 0, 150, 150);
                game.skipNext = new Rectangle(0, 0, 150, 150);
                initGameDimensions();

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
                    gameState = GameStates.ANIMATE_DROP_BLOCKS;
                    game.removeWordFound();
                    game.dropBlocks();
                    setBlocksToFall();
                } else {
                    gameState = GameStates.WAIT_FOR_PRESS;
                }
                game.deselectAll();
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
                    }
                    else
                        gameState = GameStates.GAME_OVER;
                }
                else
                    gameState = GameStates.WAIT_FOR_PRESS;
            case GAME_OVER:
                break;
        }

        updateBlocks();
    }

    private void setBlocksToFall(){
        for (int i = 0; i < game.grid.length; ++i) {
            for (int j = 0; j < game.grid[i].length; ++j) {
                if (game.grid[i][j].block == null)
                    continue;
                game.grid[i][j].block.setCurState(Block.AnimState.FALLING);
                game.grid[i][j].block.targetPos = game.grid[i][j].cellPos;
            }
        }
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

    public void initGameDimensions() {
        game.dims.screenWidth = width;
        game.dims.scalar = 1;//game.dims.screenWidth / 600;


        game.dims.boxGap = (25 - 2 * game.grid.length) * game.dims.scalar;
        game.dims.padding = 60 * game.dims.scalar;
        game.dims.boxRounding = 10 * game.dims.scalar;
        game.dims.boxDim = (game.dims.screenWidth - (game.dims.padding * 2 + game.dims.boxGap * (game.grid.length - 1))) / (game.grid.length * 1.0f);
        game.dims.inset = .13f * game.dims.boxDim;

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
}
