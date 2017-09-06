package com.wordblocks.gdx;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends WordBlocksInputProcessor implements Screen {
    public static final int worldHeight = 2133;
    public static final int worldWidth = 1200;
    final int FRAMES_PER_SECOND = 60;
    final int SKIP_TICKS = 16;
    WordBlocksGDX WBOverlord;
    OrthographicCamera camera;
    boolean first = true;
    private float height;
    long nextGameTick = 0;
    public boolean paused = false;
    ShapeRenderer shapeRenderer;
    Skin skin;
    Stage stage;
    long curMillis;
    Viewport viewport;
    private float width;
    public WordBlocksController wordBlocksController;
    public WordBlocksRenderer wordBlocksRenderer;

    public GameScreen(WordBlocksGDX overlord) {
        this.WBOverlord = overlord;
    }

    public void hide() {
    }

    public void show() {
        this.camera = new OrthographicCamera((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        this.viewport = new FitViewport(1200.0f, 2133.0f, this.camera);
        this.viewport.apply();
        this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f, 0.0f);
        Gdx.input.setInputProcessor(this);
        this.shapeRenderer = new ShapeRenderer();
        this.wordBlocksController = new WordBlocksController(1200.0f, 2133.0f, this);
        this.wordBlocksRenderer = new WordBlocksRenderer(1200.0f, 2133.0f, this);
        this.wordBlocksRenderer.setCamera(this.camera);
        this.wordBlocksController.update();
    }

    public void pause() {
        if (Gdx.app.getType() != ApplicationType.Desktop) {
            this.paused = true;
        }
    }

    public void resume() {
        this.paused = false;
    }

    public void render(float delta) {
        this.camera.update();
        Color c = this.wordBlocksController.getClearColor();
        Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        this.curMillis = TimeUtils.millis();
        if (this.curMillis >= this.nextGameTick && !this.paused) {
            this.wordBlocksController.update();
            this.nextGameTick = this.curMillis + 16;
        }
        if (!this.paused) {
            this.wordBlocksRenderer.draw(this.wordBlocksController);
        }
    }

    public void resize(int width, int height) {
        this.viewport.update(width, height);
        this.camera.position.set(this.camera.viewportWidth / 2.0f, this.camera.viewportHeight / 2.0f, 0.0f);
        this.width = (float) width;
        this.height = (float) height;
        this.wordBlocksRenderer.setCamera(this.camera);
    }

    public void dispose() {
        this.shapeRenderer.dispose();
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        Vector3 screen = this.camera.unproject(new Vector3((float) x, (float) y, 0.0f), (float) this.viewport.getScreenX(), (float) this.viewport.getScreenY(), (float) this.viewport.getScreenWidth(), (float) this.viewport.getScreenHeight());
        this.wordBlocksController.fingerPress = true;
        this.wordBlocksController.f85X = screen.x;
        this.wordBlocksController.f86Y = screen.y;
        if (this.wordBlocksController.game.refresh.contains(screen.x, screen.y)) {
            this.wordBlocksController.init();
        }
        if (this.wordBlocksController.game.giveHint.contains(screen.x, screen.y)) {
            this.wordBlocksController.giveHint();
        }
        return true;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        this.wordBlocksController.fingerPress = false;
        return true;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        this.wordBlocksController.fingerMoving = true;
        Vector3 screen = this.camera.unproject(new Vector3((float) x, (float) y, 0.0f), (float) this.viewport.getScreenX(), (float) this.viewport.getScreenY(), (float) this.viewport.getScreenWidth(), (float) this.viewport.getScreenHeight());
        this.wordBlocksController.fingerPress = true;
        this.wordBlocksController.f85X = screen.x;
        this.wordBlocksController.f86Y = screen.y;
        return true;
    }
}
