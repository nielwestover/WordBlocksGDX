package com.wordblocks.gdx;

import Answers.AnswerListView;
import Answers.SelectedWordView;
import Answers.SelectedWordView.AnimState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.wordblocks.gdx.Game.Cell;
import helpers.RowColPair;
import particles.ParticleManager;

public class WordBlocksController {
    GameScreen Overlord;
    public float f85X;
    public float f86Y;
    AnswerListView answerView;
    Color clearColor = new Color();
    int clearColorCounter = 0;
    ClearColorState clearColorState = ClearColorState.IDLE;
    public boolean fingerMoving = false;
    public boolean fingerPress = false;
    public BitmapFont fontAnswers;
    public BitmapFont fontBlocks;
    public BitmapFont fontCurWord;
    Game game;
    public GameStates gameState = GameStates.INIT;
    public float height;
    boolean hintAvailable = true;
    public HintSystem hintSystem = null;
    public float origBlockFontScale;
    private RowColPair previousSelectedBlock;
    private RowColPair selectedBlock;
    SelectedWordView selectedWordView;
    public float width;

    public WordBlocksController(float width, float height, GameScreen overlord) {
        this.Overlord = overlord;
        this.width = width;
        this.height = height;
        MyApplication.loadProfile();
    }

    float getBlockFontScale() {
        return 0.859375f * (this.game.dims.boxDim / 160.0f);
    }

    public void init() {
        this.gameState = GameStates.INIT;
    }

    private void initFonts() {
        FreeTypeFontGenerator generatorBlocks = new FreeTypeFontGenerator(Gdx.files.internal("fonts/calibrib.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 115;
        this.fontBlocks = generatorBlocks.generateFont(parameter);
        this.fontBlocks.setColor(Color.BLACK);
        this.fontCurWord = generatorBlocks.generateFont(parameter);
        this.fontCurWord.setColor(Color.WHITE);
        this.fontAnswers = generatorBlocks.generateFont(parameter);
        this.fontBlocks.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.fontCurWord.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.fontAnswers.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        float blockFontSize = getBlockFontScale();
        this.origBlockFontScale = blockFontSize;
        this.fontBlocks.getData().setScale(blockFontSize, blockFontSize);
        this.fontCurWord.getData().setScale(0.78125f);
        this.fontAnswers.getData().setScale(0.5078125f);
        generatorBlocks.dispose();
    }

    public void update() {
        switch (this.gameState) {
            case INIT:
                ParticleManager.Inst().startRandomBackgroundEffect();
                this.clearColor.set(Color.BLACK);
                this.game = new Game(MyApplication.getCurLevel());
                initGameDimensions();
                this.answerView = new AnswerListView(this.game.answers, this.fontAnswers);
                this.selectedWordView = new SelectedWordView(this.game, this.answerView);
                this.game.refresh = new Rectangle(this.width - 350.0f, 0.0f, 350.0f, 150.0f);
                this.game.giveHint = new Rectangle(0.0f, 0.0f, 350.0f, 150.0f);
                this.hintSystem = null;
                HintLoop hintLoop = Block.hintLoop;
                HintLoop.reset();
                this.Overlord.wordBlocksRenderer.init();
                this.gameState = GameStates.ENTER_ANIM;
                for (int i = 0; i < this.game.grid.length; i++) {
                    for (int j = 0; j < this.game.grid[i].length; j++) {
                        this.game.grid[i][j].block.targetPos.set(this.game.grid[i][j].cellPos);
                    }
                }
                break;
            case ENTER_ANIM:
                if (!stillEntering()) {
                    this.gameState = GameStates.WAIT_FOR_PRESS;
                    break;
                }
                break;
            case WAIT_FOR_PRESS:
                if (this.fingerPress) {
                    this.selectedBlock = new RowColPair(-1, -1);
                    this.previousSelectedBlock = new RowColPair(-1, -1);
                    this.gameState = GameStates.FINGER_DOWN;
                    update();
                    break;
                }
                break;
            case FINGER_DOWN:
                doBlockLogic();
                if (this.fingerMoving || !this.fingerPress) {
                    this.gameState = GameStates.MOVING;
                    update();
                    break;
                }
            case MOVING:
                if (!this.fingerPress) {
                    this.gameState = GameStates.FINGER_UP;
                }
                doBlockLogic();
                break;
            case FINGER_UP:
                if (this.game.getSelectedWord(this.game.selectedWord) == null) {
                    this.game.deselectAll();
                    this.gameState = GameStates.WAIT_FOR_PRESS;
                    break;
                }
                this.gameState = GameStates.ANIMATE_DISSOLVE;
                this.selectedWordView.state = AnimState.MOVE_TO_FOUND;
                setBlocksToDissolve();
                this.game.getSelectedWord(this.game.selectedWord).found = true;
                if (this.game.remainingWords().size() != 0) {
                    Gdx.input.vibrate(100);
                    break;
                } else {
                    Gdx.input.vibrate(1000);
                    break;
                }
            case ANIMATE_DISSOLVE:
                if (!blocksStillDissolving()) {
                    this.gameState = GameStates.ANIMATE_DROP_BLOCKS;
                    this.game.removeWordFound();
                    this.game.deselectAll();
                    this.hintSystem = null;
                    removeHints();
                    this.game.dropBlocks();
                    setBlocksToFall();
                    update();
                    break;
                }
                break;
            case ANIMATE_DROP_BLOCKS:
                if (!blocksStillFalling()) {
                    this.gameState = GameStates.CHECK_LEVEL_FINISHED;
                    update();
                    break;
                }
                break;
            case CHECK_LEVEL_FINISHED:
                if (this.game.blocksLeft() == 0) {
                    if (this.selectedWordView.state != AnimState.MOVE_TO_FOUND_ANIM && MyApplication.incrementCurLevel()) {
                        MyApplication.getPreferences().putInteger("pack", MyApplication.curPackIndex);
                        MyApplication.getPreferences().putInteger("level", MyApplication.curLevelIndex);
                        MyApplication.getPreferences().flush();
                        this.gameState = GameStates.INIT;
                        break;
                    }
                }
                else
                    this.gameState = GameStates.WAIT_FOR_PRESS;
                break;
        }
        this.answerView.update();
        this.selectedWordView.update();
        updateClearColor();
        updateBlocks();
    }

    private void setBlocksToFall() {
        for (int i = 0; i < this.game.grid.length; i++) {
            for (int j = 0; j < this.game.grid[i].length; j++) {
                if (this.game.grid[i][j].block != null) {
                    this.game.grid[i][j].block.setCurState(Block.AnimState.FALLING);
                    this.game.grid[i][j].block.targetPos = this.game.grid[i][j].cellPos;
                }
            }
        }
    }

    private void setBlocksToDissolve() {
        for (RowColPair rc : this.game.selectedChain) {
            this.game.grid[rc.Row][rc.Col].block.setCurState(Block.AnimState.DISSOLVING);
        }
    }

    private void updateBlocks() {
        for (int i = 0; i < this.game.grid.length; i++) {
            for (int j = 0; j < this.game.grid[i].length; j++) {
                if (this.game.grid[i][j].block != null) {
                    this.game.grid[i][j].block.update();
                }
            }
        }
    }

    private boolean stillEntering() {
        for (int i = 0; i < this.game.grid.length; i++) {
            for (Cell cell : this.game.grid[i]) {
                if (cell.block.getCurState() == Block.AnimState.ANIM_START) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean blocksStillFalling() {
        int i = 0;
        while (i < this.game.grid.length) {
            int j = 0;
            while (j < this.game.grid[i].length) {
                if (this.game.grid[i][j].block != null && this.game.grid[i][j].block.getCurState() == Block.AnimState.FALLING) {
                    return true;
                }
                j++;
            }
            i++;
        }
        return false;
    }

    private boolean blocksStillDissolving() {
        int i = 0;
        while (i < this.game.grid.length) {
            int j = 0;
            while (j < this.game.grid[i].length) {
                if (this.game.grid[i][j].block != null && this.game.grid[i][j].block.getCurState() == Block.AnimState.DISSOLVING) {
                    return true;
                }
                j++;
            }
            i++;
        }
        return false;
    }

    public void initGameDimensions() {
        this.game.dims.screenWidth = this.width;
        this.game.dims.boxGap = (float) (25 - (this.game.grid.length * 2));
        this.game.dims.padding = 40.0f;
        this.game.dims.boxDim = (this.game.dims.screenWidth - ((this.game.dims.padding * 2.0f) + (this.game.dims.boxGap * ((float) (this.game.grid.length - 1))))) / (((float) this.game.grid.length) * 1.0f);
        this.game.dims.inset = 0.13f * this.game.dims.boxDim;
        this.game.dims.topPadding = 100.0f;
        initFonts();
        for (int i = 0; i < this.game.grid.length; i++) {
            for (int j = 0; j < this.game.grid[i].length; j++) {
                if (this.game.grid[i][j].block != null) {
                    GlyphLayout glyphLayout = new GlyphLayout();
                    glyphLayout.setText(this.fontBlocks, this.game.grid[i][j].block.letter + BuildConfig.FLAVOR);
                    this.game.grid[i][j].block.letterWidth = glyphLayout.width;
                    this.game.grid[i][j].block.letterHeight = glyphLayout.height;
                    this.game.grid[i][j].cellPos = new Rectangle((float) ((int) (this.game.dims.padding + ((this.game.dims.boxGap + this.game.dims.boxDim) * ((float) i)))), (this.height - this.game.dims.topPadding) - ((float) ((int) ((this.game.dims.boxGap + this.game.dims.boxDim) * ((float) (j + 1))))), this.game.dims.boxDim, this.game.dims.boxDim);
                    this.game.grid[i][j].block.pos.set(this.game.grid[i][j].cellPos);
                }
            }
        }
    }

    private void doBlockLogic() {
        findSelectedBlock();
        if (isSelectedBlockValid()) {
            setSelectedBlock();
        }
    }

    private boolean isSelectedBlockValid() {
        if (this.previousSelectedBlock.Row == -1) {
            this.previousSelectedBlock = this.selectedBlock;
            return true;
        } else if (this.game.selectedChain.size() >= 10) {
            return false;
        } else {
            if (this.selectedBlock == this.previousSelectedBlock) {
                return false;
            }
            if (this.game.grid[this.selectedBlock.Row][this.selectedBlock.Col].block.getSelected()) {
                return false;
            }
            int xDist = Math.abs(this.selectedBlock.Row - this.previousSelectedBlock.Row);
            int yDist = Math.abs(this.selectedBlock.Col - this.previousSelectedBlock.Col);
            if (xDist > 1 || yDist > 1) {
                return false;
            }
            this.previousSelectedBlock = this.selectedBlock;
            return true;
        }
    }

    private void findSelectedBlock() {
        int i = 0;
        while (i < this.game.grid.length) {
            int j = 0;
            while (j < this.game.grid[i].length) {
                if (this.game.grid[i][j].block != null && this.game.grid[i][j].block.isTouched(this.f85X, this.f86Y, this.game.dims.inset)) {
                    this.selectedBlock = new RowColPair(i, j);
                }
                j++;
            }
            i++;
        }
    }

    private void setSelectedBlock() {
        int i = this.selectedBlock.Row;
        int j = this.selectedBlock.Col;
        if (i != -1 && j != -1) {
            if (!(this.game.grid[i][j].block == null || this.game.grid[i][j].block.getSelected())) {
                this.game.grid[i][j].block.setSelected(true);
                this.game.selectedWord = this.game.selectedWord.concat(this.game.grid[i][j].block.letter + BuildConfig.FLAVOR);
                this.game.grid[i][j].block.setWordPosition(this.game.selectedChain.size());
                this.game.selectedChain.add(new RowColPair(i, j));
            }
            Gdx.input.vibrate(5);
        }
    }

    public void giveHint() {
        if (this.hintSystem == null) {
            this.hintSystem = new HintSystem(this.game);
        }
        int id = this.hintSystem.getHint();
        if (id >= 0) {
            Block b = this.game.getBlockByID(id);
            if (b != null) {
                b.setIsHint(true);
                return;
            }
            return;
        }
        this.clearColorState = ClearColorState.TO_RED;
    }

    public void removeHints() {
        for (int i = 0; i < this.game.grid.length; i++) {
            for (int j = 0; j < this.game.grid[i].length; j++) {
                if (this.game.grid[i][j].block != null) {
                    this.game.grid[i][j].block.setIsHint(false);
                }
            }
        }
    }

    public Color getClearColor() {
        return this.clearColor;
    }

    private void updateClearColor() {
        Color color;
        switch (this.clearColorState) {
            case TO_RED:
                color = this.clearColor;
                color.r += 0.2f;
                if (this.clearColor.r >= 1.0f) {
                    this.clearColorState = ClearColorState.TO_BLACK;
                    return;
                }
                return;
            case TO_BLACK:
                color = this.clearColor;
                color.r -= 0.05f;
                if (this.clearColor.r <= 0.0f) {
                    this.clearColor.set(Color.BLACK);
                    this.clearColorState = ClearColorState.IDLE;
                    return;
                }
                return;
            default:
                return;
        }
    }
}
