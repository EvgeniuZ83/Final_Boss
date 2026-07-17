package ru.samsung.jkgroup.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.jkgroup.*;
import ru.samsung.jkgroup.components.*;
import ru.samsung.jkgroup.managers.ContactManager;
import ru.samsung.jkgroup.managers.MemoryManager;
import ru.samsung.jkgroup.obj.TorpedoObj;
import ru.samsung.jkgroup.obj.SubmarineObj;
import ru.samsung.jkgroup.obj.ShipsObj;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {

    SubmarineGame submarineGame;
    GameSession gameSession;
    SubmarineObj submarineObj;

    ArrayList<ShipsObj> shipArray;
    ArrayList<TorpedoObj> bulletArray;

    ContactManager contactManager;

    // PLAY state UI
    SeaMovingView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    TextView scoreTextView;
    ButtonView pauseButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;

    public GameScreen(SubmarineGame submarineGame) {
        this.submarineGame = submarineGame;
        gameSession = new GameSession();

        contactManager = new ContactManager(submarineGame.world);

        shipArray = new ArrayList<>();
        bulletArray = new ArrayList<>();

        submarineObj = new SubmarineObj(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SUB_WIDTH, GameSettings.SUB_HEIGHT,
                GameResources.SUB_IMG_PATH,
                submarineGame.world
        );

        backgroundView = new SeaMovingView(GameResources.BACKGROUND_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(305, 1215);
        scoreTextView = new TextView(submarineGame.commonRedFont, 50, 1215);
        pauseButton = new ButtonView(
                605, 1200,
                46, 54,
                GameResources.PAUSE_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        pauseTextView = new TextView(submarineGame.largeWhiteFont, 282, 842, "Пауза");
        homeButton = new ButtonView(
                138, 695,
                200, 70,
                submarineGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Выход"
        );
        continueButton = new ButtonView(
                393, 695,
                200, 70,
                submarineGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "В бой!"
        );

        recordsListView = new RecordsListView(submarineGame.commonWhiteFont, 690);
        recordsTextView = new TextView(submarineGame.largeWhiteFont, 140, 842, "Рекордсмены");
        homeButton2 = new ButtonView(
                280, 365,
                160, 70,
                submarineGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Назад"
        );

    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnShip()) {
                ShipsObj torpedoObject = new ShipsObj(
                        GameSettings.SHIPS_WIDTH, GameSettings.SHIPS_HEIGHT,
                        GameResources.SHIP_IMG_PATH,
                        submarineGame.world
                );
                shipArray.add(torpedoObject);
            }

            if (submarineObj.needToShoot()) {
                TorpedoObj torpedo = new TorpedoObj(
                        submarineObj.getX(), submarineObj.getY() + submarineObj.height / 2,
                        GameSettings.TORPEDO_WIDTH, GameSettings.TORPEDO_HEIGHT,
                        GameResources.TORPEDO_IMG_PATH,
                        submarineGame.world
                );
                bulletArray.add(torpedo);
                if (submarineGame.audioManager.isSoundOn) submarineGame.audioManager.shootSound.play();
            }

            if (!submarineObj.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            updateShip();
            updateTorpedo();
            backgroundView.move();
            gameSession.updateScore();
            scoreTextView.setText("Очки : " + gameSession.getScore());
            liveView.setLeftLives(submarineObj.getLiveLeft());

            submarineGame.stepWorld();
        }

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            submarineGame.touch = submarineGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    submarineObj.move(submarineGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                        submarineGame.setScreen(submarineGame.menuScreen);
                    }
                    break;

                case ENDED:

                    if (homeButton2.isHit(submarineGame.touch.x, submarineGame.touch.y)) {
                        submarineGame.setScreen(submarineGame.menuScreen);
                    }
                    break;
            }

        }
    }

    private void draw() {

        submarineGame.camera.update();
        submarineGame.batch.setProjectionMatrix(submarineGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        submarineGame.batch.begin();
        backgroundView.draw(submarineGame.batch);
        for (ShipsObj ship : shipArray) ship.draw(submarineGame.batch);
        submarineObj.draw(submarineGame.batch);
        for (TorpedoObj bullet : bulletArray) bullet.draw(submarineGame.batch);
        topBlackoutView.draw(submarineGame.batch);
        scoreTextView.draw(submarineGame.batch);
        liveView.draw(submarineGame.batch);
        pauseButton.draw(submarineGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(submarineGame.batch);
            pauseTextView.draw(submarineGame.batch);
            homeButton.draw(submarineGame.batch);
            continueButton.draw(submarineGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(submarineGame.batch);
            recordsTextView.draw(submarineGame.batch);
            recordsListView.draw(submarineGame.batch);
            homeButton2.draw(submarineGame.batch);
        }

        submarineGame.batch.end();

    }

    private void updateShip() {
        for (int i = 0; i < shipArray.size(); i++) {

            boolean hasToBeDestroyed = !shipArray.get(i).isAlive() || !shipArray.get(i).isInFrame();

            if (!shipArray.get(i).isAlive()) {
                gameSession.destructionRegistration();
                if (submarineGame.audioManager.isSoundOn) submarineGame.audioManager.explosionSound.play(0.2f);
            }

            if (hasToBeDestroyed) {
                submarineGame.world.destroyBody(shipArray.get(i).body);
                shipArray.remove(i--);
            }
        }
    }

    private void updateTorpedo() {
        for (int i = 0; i < bulletArray.size(); i++) {
            if (bulletArray.get(i).hasToBeDestroyed()) {
                submarineGame.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i--);
            }
        }
    }

    private void restartGame() {

        for (int i = 0; i < shipArray.size(); i++) {
            submarineGame.world.destroyBody(shipArray.get(i).body);
            shipArray.remove(i--);
        }

        if (submarineObj != null) {
            submarineGame.world.destroyBody(submarineObj.body);
        }

        submarineObj = new SubmarineObj(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SUB_WIDTH, GameSettings.SUB_HEIGHT,
                GameResources.SUB_IMG_PATH,
                submarineGame.world
        );

        bulletArray.clear();
        gameSession.startGame();
    }

}
