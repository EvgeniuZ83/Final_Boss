package ru.samsung.jkgroup.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.jkgroup.GameResources;
import ru.samsung.jkgroup.SubmarineGame;
import ru.samsung.jkgroup.components.ButtonView;
import ru.samsung.jkgroup.components.SeaMovingView;
import ru.samsung.jkgroup.components.TextView;

public class MenuScreen extends ScreenAdapter {

    SubmarineGame submarineGame;

    SeaMovingView backgroundView;
    TextView titleView;
    ButtonView startButtonView;
    ButtonView settingsButtonView;
    ButtonView exitButtonView;

    public MenuScreen(SubmarineGame submarineGame) {
        this.submarineGame = submarineGame;

        backgroundView = new SeaMovingView(GameResources.BACKGROUND_IMG_PATH);
        titleView = new TextView(submarineGame.largeWhiteFont, 100, 960, "Тихий Охотник");
        startButtonView = new ButtonView(140, 646, 440, 70, submarineGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "Начать");
        settingsButtonView = new ButtonView(140, 551, 440, 70, submarineGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "Настройки");
        exitButtonView = new ButtonView(140, 456, 440, 70, submarineGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "Выход");
    }

    @Override
    public void render(float delta) {

        handleInput();

        submarineGame.camera.update();
        submarineGame.batch.setProjectionMatrix(submarineGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        submarineGame.batch.begin();

        backgroundView.draw(submarineGame.batch);
        titleView.draw(submarineGame.batch);
        exitButtonView.draw(submarineGame.batch);
        settingsButtonView.draw(submarineGame.batch);
        startButtonView.draw(submarineGame.batch);

        submarineGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            submarineGame.touch = submarineGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (startButtonView.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                submarineGame.setScreen(submarineGame.gameScreen);
            }
            if (exitButtonView.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                Gdx.app.exit();
            }
            if (settingsButtonView.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                submarineGame.setScreen(submarineGame.settingsScreen);
            }
        }
    }
}
