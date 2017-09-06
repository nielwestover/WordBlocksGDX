package com.wordblocks.gdx;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GroupChooser implements Screen {
    private Stage stage;
    private Table container;
    private WordBlocksGDX WBOverlord;

    public GroupChooser(WordBlocksGDX overlord) {
        this.WBOverlord = overlord;
    }

    public void create () {

    }

    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Gdx.graphics.setVSync(false);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        Table table = new Table();
        // table.debug();

        final ScrollPane scroll = new ScrollPane(table, skin);

        table.pad(10).defaults().expandX().space(4);

        for (int i = 0; i < 25; i++) {
            final int index = i;
            table.row();
            TextButton button = new TextButton(i + 1 + "", skin);
            button.setHeight(100);
            button.setWidth(200);
            table.add(button);
            button.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    MyApplication.curPackIndex = index;
                    MyApplication.curLevelIndex = index;
                    WBOverlord.setScreen(new GameScreen(WBOverlord));
                }
                //this.WBOverlord.setScreen(new GameScreen(this.WBOverlord));
            });
        }
        scroll.setFlickScroll(true);
        scroll.setFadeScrollBars(true);
        scroll.setSmoothScrolling(true);

        final TextButton backButton = new TextButton("< Back", skin);
        backButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //go back
            }
        });

        container.add(scroll).expand().fill().colspan(4);
        container.row().space(10).padBottom(10);
        container.add(backButton).left().expandX();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);

        // Gdx.gl.glViewport(100, 100, width - 200, height - 200);
        // stage.setViewport(800, 600, false, 100, 100, width - 200, height - 200);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose () {
        stage.dispose();
    }

}