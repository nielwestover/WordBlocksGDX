package com.wordblocks.gdx;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelChooser implements Screen {
    private Stage stage;
    private Table container;
    private WordBlocksGDX WBOverlord;
    private BitmapFont titleFont;
    private BitmapFont groupFont;
    private int group = 0;

    public void SetCurGroup(int group) {
        this.group = group;
    }

    public LevelChooser(WordBlocksGDX overlord) {
        this.WBOverlord = overlord;
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void initFonts() {
        float dp = Gdx.graphics.getDensity();
        FreeTypeFontGenerator generatorBlocks = new FreeTypeFontGenerator(Gdx.files.internal("fonts/calibrib.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(dp * 40);
        titleFont = generatorBlocks.generateFont(parameter);
        titleFont.setColor(Color.BLACK);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        parameter.size = (int)(dp * 40);
        groupFont = generatorBlocks.generateFont(parameter);
        groupFont.setColor(Color.BLACK);
        groupFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //titleFont.getData().setScale(0.78125f);
        generatorBlocks.dispose();
    }

    @Override
    public void show() {
        initFonts();

        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("myskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Gdx.graphics.setVSync(false);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        Table table = new Table();
        final ScrollPane scroll = new ScrollPane(table, skin);

        //table.debug();
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        TextButtonStyle groupStyle = new TextButtonStyle();
        groupStyle.up = skin.getDrawable("default-round-large");
        groupStyle.down = skin.getDrawable("default-round-down");
        groupStyle.font = groupFont;
        groupStyle.fontColor = Color.WHITE;
        groupStyle.downFontColor = Color.BLACK;

        float dp = Gdx.graphics.getDensity();
        table.row();
        Label header = new Label("GROUP " + (group + 1), titleStyle);
        table.add(header).height(60);

        table.row();
        Label title = new Label("Select a Level", titleStyle);
        table.add(title).height(dp * 40);
        table.pad(10).defaults().expandX().space(dp * 8);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            table.row();
            TextButton button = new TextButton(i + 1 + "", groupStyle);

            table.add(button).height(dp * 60).width(dp * 200);
            button.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    MyApplication.curPackIndex = group;
                    MyApplication.curLevelIndex = index;
                    WBOverlord.setScreen(new GameScreen(WBOverlord));
                }
            });
        }
        scroll.setFlickScroll(true);
        scroll.setFadeScrollBars(true);
        scroll.setSmoothScrolling(true);
        scroll.setScrollbarsOnTop(false);
        final TextButton backButton = new TextButton(" < Back", groupStyle);
        backButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                WBOverlord.setScreen(WBOverlord.groupChooser);
            }
        });

        container.add(scroll).expand().fill().colspan(4);
        container.row();
        container.add(backButton).left().expandX().height(dp * 50);
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