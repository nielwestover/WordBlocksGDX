package com.wordblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by a2558 on 3/21/2016.
 */
public class GameScreen extends WordBlocksInputProcessor implements Screen {
    final int worldWidth = 1200;
    final int worldHeight = 1600;
    long t;
    long nextGameTick = 0;
    final int FRAMES_PER_SECOND = 60;
    final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

    public WordBlocksRenderer wordBlocksRenderer;
    public WordBlocksController wordBlocksController;
    private float width, height;
    ShapeRenderer shapeRenderer;
    Viewport viewport;
    OrthographicCamera camera;
    Skin skin;
    Stage stage;
    WordBlocksGDX WBOverlord;
    public GameScreen(WordBlocksGDX overlord){
        WBOverlord = overlord;
    }

    @Override
    public void hide () {
    }

    @Override
    public void show () {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        Gdx.input.setInputProcessor(this);
        shapeRenderer  = new ShapeRenderer();

        //skin = new Skin(Gdx.files.internal("uiskin.json"));
        //stage = new Stage(viewport);
        //stage.addActor(new TextButton("Refresh", skin, "default"));
    }

    @Override
    public void resume () {

    }

    @Override
    public void render (float delta) {
        camera.update();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        t = TimeUtils.millis();
        if (t >= nextGameTick) {
            wordBlocksController.update();
            //Log.d("Update: ", String.valueOf(t));
            nextGameTick = t + SKIP_TICKS;
        }

        //Log("Draw: ", String.valueOf(t));
        wordBlocksRenderer.draw(wordBlocksController);
    }

    boolean first = true;
    @Override
    public void resize (int width, int height) {
        if (!first)
            return;
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        this.width = width;
        this.height = height;
        wordBlocksController = new WordBlocksController(worldWidth, worldHeight, this);
        wordBlocksRenderer = new WordBlocksRenderer(worldWidth, worldHeight, camera, this);
        wordBlocksController.update();//do initial update to set up board, so game is not null for the next call
        first = false;
    }

    @Override
    public void pause () {

    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        Vector3 screen = camera.unproject(new Vector3(x, y, 0), viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        wordBlocksController.fingerPress = true;
        wordBlocksController.X = screen.x;
        wordBlocksController.Y = screen.y;

        if (wordBlocksController.game.refresh.contains(screen.x, screen.y)) {
            //automatically calls init on the renderer
            wordBlocksController.init();
        }


        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        wordBlocksController.fingerPress = false;
        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        wordBlocksController.fingerMoving = true;
        Vector3 screen = camera.unproject(new Vector3(x, y, 0), viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        wordBlocksController.fingerPress = true;
        wordBlocksController.X = screen.x;
        wordBlocksController.Y = screen.y;
        return true;
    }
}
