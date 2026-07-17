package ru.samsung.jkgroup.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.jkgroup.GameResources;
import ru.samsung.jkgroup.managers.MemoryManager;
import ru.samsung.jkgroup.SubmarineGame;
import ru.samsung.jkgroup.components.ButtonView;
import ru.samsung.jkgroup.components.ImageView;
import ru.samsung.jkgroup.components.SeaMovingView;
import ru.samsung.jkgroup.components.TextView;

import java.util.ArrayList;

public class SettingsScreen extends ScreenAdapter {

    SubmarineGame submarineGame;

    SeaMovingView backgroundView;
    TextView titleTextView;
    ImageView blackoutImageView;
    ButtonView returnButton;
    TextView musicSettingView;
    TextView soundSettingView;
    TextView clearSettingView;

    public SettingsScreen(SubmarineGame submarineGame) {
        this.submarineGame = submarineGame;

        backgroundView = new SeaMovingView(GameResources.BACKGROUND_IMG_PATH);
        titleTextView = new TextView(submarineGame.largeWhiteFont, 180, 956, "Настройки");
        blackoutImageView = new ImageView(85, 365, GameResources.BLACKOUT_MIDDLE_IMG_PATH);
        clearSettingView = new TextView(submarineGame.commonWhiteFont, 173, 599, "Очистить рекорды");

        musicSettingView = new TextView(
                submarineGame.commonWhiteFont,
                173, 717,
                "Музыка: " + translateStateToText(MemoryManager.loadIsMusicOn())
        );

        soundSettingView = new TextView(
                submarineGame.commonWhiteFont,
                173, 658,
                "Звуки: " + translateStateToText(MemoryManager.loadIsSoundOn())
        );

        returnButton = new ButtonView(
                280, 447,
                160, 70,
                submarineGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Назад"
        );

    }

    @Override
    public void render(float delta) {

        handleInput();

        submarineGame.camera.update();
        submarineGame.batch.setProjectionMatrix(submarineGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        submarineGame.batch.begin();

        backgroundView.draw(submarineGame.batch);
        titleTextView.draw(submarineGame.batch);
        blackoutImageView.draw(submarineGame.batch);
        returnButton.draw(submarineGame.batch);
        musicSettingView.draw(submarineGame.batch);
        soundSettingView.draw(submarineGame.batch);
        clearSettingView.draw(submarineGame.batch);

        submarineGame.batch.end();
    }

    void handleInput() {
        if (Gdx.input.justTouched()) {
            submarineGame.touch = submarineGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (returnButton.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                submarineGame.setScreen(submarineGame.menuScreen);
            }
            if (clearSettingView.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                MemoryManager.saveTableOfRecords(new ArrayList<>());
                clearSettingView.setText("Очистить рекорды");
            }
            if (musicSettingView.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                MemoryManager.saveMusicSettings(!MemoryManager.loadIsMusicOn());
                musicSettingView.setText("Музыка: " + translateStateToText(MemoryManager.loadIsMusicOn()));
                submarineGame.audioManager.updateMusicFlag();
            }
            if (soundSettingView.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                MemoryManager.saveSoundSettings(!MemoryManager.loadIsSoundOn());
                soundSettingView.setText("Звуки: " + translateStateToText(MemoryManager.loadIsSoundOn()));
                submarineGame.audioManager.updateSoundFlag();
            }
        }
    }

    private String translateStateToText(boolean state) {
        return state ? "Вкл" : "Выкл";
    }
}
