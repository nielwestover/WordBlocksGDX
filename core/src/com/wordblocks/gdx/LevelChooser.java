package com.wordblocks.gdx;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelChooser implements Screen {
    private Stage stage;
    private Table container;
    private WordBlocksGDX WBOverlord;
    private int group = 0;

    public void SetCurGroup(int group) {
        this.group = group;
    }

    public LevelChooser(WordBlocksGDX overlord) {
        this.WBOverlord = overlord;
    }


    @Override
    public void show() {

        Gdx.input.setCatchBackKey(true);
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("fe-skin/flat-earth-ui.json"));
        Gdx.input.setInputProcessor(stage);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());
        final ScrollPane scroll = new ScrollPane(table, skin);
        scroll.setFlickScroll(true);
        scroll.setFadeScrollBars(false);
        scroll.setSmoothScrolling(true);
        scroll.setScrollbarsOnTop(false);

        table.row();

        float dp = Gdx.graphics.getDensity();
        Label title = new Label("Select a Puzzle", skin, "title");
        title.setFontScale(.6f * dp);
        container.add(title).padBottom(dp);
        container.row();
        table.pad(dp * 3).defaults().expandX().space(dp * 8);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            table.row();
            TextButton button = new TextButton((group + 1) + "-" + (i + 1) + "", skin, "default");

            button.getLabel().setFontScale(1f * dp);
            table.add(button).width(dp * 200).height(dp * 50);

            button.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    MyApplication.curPackIndex = group;
                    MyApplication.curLevelIndex = index;
                    WBOverlord.setScreen(new GameScreen(WBOverlord));
                }
            });
        }

        final TextButton backButton = new TextButton(" < BACK  ", skin, "default");
        backButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                WBOverlord.setScreen(WBOverlord.groupChooser);
            }
        });

        container.add(scroll).expand().fill().colspan(4);
        container.row().space(dp * 20).padLeft(dp * 20);
        backButton.getLabel().setFontScale(.8f * dp);
        container.add(backButton).left().expandX().height(dp * 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            WBOverlord.setScreen(WBOverlord.groupChooser);
        }
    }

    public void resize(int width, int height) {
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

    public void dispose() {
        stage.dispose();
    }

}