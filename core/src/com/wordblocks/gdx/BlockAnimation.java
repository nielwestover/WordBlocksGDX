package com.wordblocks.gdx;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class BlockAnimation {
    AnimState animState = AnimState.INIT;
    BlockAnimType animType;
    Block block;
    int col;
    int dim;
    int interpCapped;
    float interpRange = 0.0f;
    float interpStart = 0.0f;
    int interpSteps = 0;
    float interpVal = 0.0f;
    Interpolation interpolation = null;
    boolean isEven;
    float maxSteps = 0.0f;
    Vector2 moveDir = new Vector2();
    Random rand = new Random();
    int row;
    Vector2 startPos = new Vector2();
    Vector2 temp = new Vector2();
    int terminationCondition = 0;
    Vector2 tmp = new Vector2();
    Vector2 tmp2 = new Vector2();

    public enum AnimState {
        INIT,
        ANIMATE,
        DONE
    }

    public enum BlockAnimType {
        RANDOM_START,
        ZIG_ZAG_SLIDE,
        FALL_RANDOM,
        FALL,
        SLIDE_IN_FROM_SIDES,
        SLIDE_IN_FROM_SIDES_BOUNCE,
        SHRINK_COLLAPSE
    }

    public BlockAnimation(Block block, BlockAnimType animType, int dim) {
        boolean z = false;
        this.animType = animType;
        this.dim = dim;
        this.block = block;
        this.row = block.id / dim;
        this.col = block.id % dim;
        if (block.id % 2 == 0) {
            z = true;
        }
        this.isEven = z;
    }

    public AnimState update() {
        switch (this.animState) {
            case INIT:
                init();
                this.animState = AnimState.ANIMATE;
                break;
            case ANIMATE:
                if (!animate()) {
                    this.animState = AnimState.DONE;
                    break;
                }
                break;
        }
        return this.animState;
    }

    private void init() {
        switch (this.animType) {
            case RANDOM_START:
                this.maxSteps = 50.0f;
                this.interpolation = Interpolation.pow3Out;
                this.terminationCondition = (int) this.maxSteps;
                this.interpSteps = 0;
                this.interpStart = 0.0f;
                this.interpRange = 1.0f;
                if (this.isEven) {
                    this.block.pos.x = (this.rand.nextFloat() * 1200.0f) - 1560.0f;
                } else {
                    this.block.pos.x = (this.rand.nextFloat() * 1200.0f) + 1560.0f;
                }
                this.block.pos.y = ((this.rand.nextFloat() * 2133.0f) * 2.0f) + 500.0f;
                break;
            case FALL:
                this.maxSteps = 60.0f;
                this.interpolation = Interpolation.pow3In;
                this.terminationCondition = (int) this.maxSteps;
                this.interpSteps = this.row * -8;
                this.interpStart = 0.2f;
                this.interpRange = 0.8f;
                this.block.pos.x = this.block.targetPos.x;
                this.block.pos.y = 2772.9f + (this.block.game.dims.boxDim * ((float) this.row));
                break;
            case FALL_RANDOM:
                this.maxSteps = 60.0f;
                this.interpolation = Interpolation.pow3In;
                this.terminationCondition = (int) this.maxSteps;
                this.interpSteps = -((int) ((this.rand.nextFloat() * 7.0f) + ((float) (this.row * 7))));
                this.interpStart = 0.5f;
                this.interpRange = 0.5f;
                this.block.pos.x = this.block.targetPos.x;
                this.block.pos.y = this.block.targetPos.y + 1500.0f;
                break;
            case SHRINK_COLLAPSE:
                int centerRowCol = this.dim / 2;
                this.maxSteps = 60.0f;
                this.interpolation = Interpolation.pow3Out;
                this.terminationCondition = (int) this.maxSteps;
                this.interpSteps = 0;
                this.interpStart = 0.4f;
                this.interpRange = 0.6f;
                Rectangle target = this.block.game.grid[centerRowCol][centerRowCol].block.targetPos;
                if (this.dim % 2 != 0) {
                    this.tmp2.set(target.x + (target.width / 2.0f), target.y + (target.height / 2.0f));
                } else {
                    this.tmp2.set(target.x, target.y);
                }
                this.tmp.set(this.block.pos.x + (this.block.pos.width / 2.0f), this.block.pos.y + (this.block.pos.height / 2.0f));
                this.tmp.sub(this.tmp2);
                this.tmp.scl(10.0f);
                this.block.pos.setCenter(this.tmp.x + this.block.pos.x, this.tmp.y + this.block.pos.y);
                break;
            case ZIG_ZAG_SLIDE:
                this.maxSteps = 40.0f;
                this.interpolation = Interpolation.pow2Out;
                this.terminationCondition = (int) this.maxSteps;
                this.interpSteps = ((this.dim - this.row) - 1) * -5;
                this.interpStart = 0.0f;
                this.interpRange = 1.0f;
                if (this.row % 2 != 0) {
                    this.block.pos.x = (this.block.game.dims.boxDim * ((float) this.col)) + 1200.0f;
                } else {
                    this.block.pos.x = -(this.block.game.dims.boxDim * ((float) (this.dim - this.col)));
                }
                this.block.pos.y = this.block.targetPos.y;
                break;
            case SLIDE_IN_FROM_SIDES:
            case SLIDE_IN_FROM_SIDES_BOUNCE:
                boolean hasMiddleCol = this.dim % 2 != 0;
                int middleCol = this.dim / 2;
                this.maxSteps = 50.0f;
                if (this.animType == BlockAnimType.SLIDE_IN_FROM_SIDES) {
                    this.interpolation = Interpolation.pow4Out;
                } else {
                    this.interpolation = Interpolation.bounce;
                }
                this.terminationCondition = (int) this.maxSteps;
                this.interpStart = 0.0f;
                this.interpRange = 1.0f;
                if (!hasMiddleCol || this.col != middleCol) {
                    int dist = Math.abs(middleCol - this.col);
                    if (!hasMiddleCol && this.col >= middleCol) {
                        dist++;
                    }
                    this.interpSteps = ((-dist) * 5) - (this.row * 6);
                    this.block.pos.y = this.block.targetPos.y;
                    if (this.col >= middleCol) {
                        this.block.pos.x = this.block.targetPos.x + ((this.block.game.dims.boxDim * ((float) dist)) * 5.0f);
                        break;
                    } else {
                        this.block.pos.x = this.block.targetPos.x - ((this.block.game.dims.boxDim * ((float) dist)) * 5.0f);
                        break;
                    }
                }
                this.interpSteps = (-(this.row + 2)) * 5;
                this.block.pos.x = this.block.targetPos.x;
                this.block.pos.y = 2772.9f + (this.block.game.dims.boxDim * ((float) (this.row + 1)));
                break;
        }
        this.startPos.set(this.block.pos.x, this.block.pos.y);
        this.moveDir.set(this.block.targetPos.x, this.block.targetPos.y);
        this.moveDir.sub(this.startPos);
    }

    private boolean animate() {
        if (this.interpSteps == this.terminationCondition) {
            this.block.pos.set(this.block.targetPos);
            return false;
        }
        this.interpCapped = this.interpSteps;
        if (this.interpCapped < 0) {
            this.interpCapped = 0;
        }
        this.interpVal = this.interpolation.apply(this.interpStart + ((((float) this.interpCapped) / this.maxSteps) * this.interpRange));
        this.interpSteps++;
        this.temp.set(this.startPos);
        this.temp.mulAdd(this.moveDir, this.interpVal);
        this.block.pos.setX(this.temp.x);
        this.block.pos.setY(this.temp.y);
        return true;
    }
}
