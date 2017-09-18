package com.wordblocks.gdx;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GroupChooser implements Screen {
    private Stage stage;
    private Table container;
    private WordBlocksGDX WBOverlord;

    public GroupChooser(WordBlocksGDX overlord) {
        this.WBOverlord = overlord;
    }

    @Override
    public void show() {

        stage = new Stage(new ScreenViewport());
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
        Label title = new Label("Select a Level", skin, "title");
        title.setFontScale(.6f * dp);
        container.add(title).padBottom(dp);
        container.row();
        table.pad(dp * 3).defaults().expandX().space(dp * 8);

        for (int i = 0; i < 25; i++) {
            final int index = i;
            table.row();
            TextButton button = new TextButton(i + 1 + "", skin, "default");

            button.getLabel().setFontScale(1f * dp);
            table.add(button).width(dp * 200).height(dp * 50);

            button.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    WBOverlord.levelChooser.SetCurGroup(index);
                    WBOverlord.setScreen(WBOverlord.levelChooser);
                }
            });
        }

        final TextButton backButton = new TextButton(" < Back", skin);
        /*backButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //go back
            }
        });
*/
        container.add(scroll).expand().fill();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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