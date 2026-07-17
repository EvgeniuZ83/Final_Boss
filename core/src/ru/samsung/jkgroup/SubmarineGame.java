package ru.samsung.jkgroup;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.jkgroup.managers.AudioManager;
import ru.samsung.jkgroup.ui.GameScreen;
import ru.samsung.jkgroup.ui.MenuScreen;
import ru.samsung.jkgroup.ui.SettingsScreen;

import static ru.samsung.jkgroup.GameSettings.*;

public class SubmarineGame extends Game {

    public World world;

    public BitmapFont largeWhiteFont;
    public BitmapFont commonWhiteFont;
    public BitmapFont commonBlackFont;
    public BitmapFont commonRedFont;

    public Vector3 touch;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public AudioManager audioManager;

    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    public SettingsScreen settingsScreen;

    float accumulator = 0;

    @Override
    public void create() {

        Box2D.init();
        world = new World(new Vector2(0, 0), true);

        largeWhiteFont = FontBuilder.generate(67, Color.WHITE, GameResources.FONT_PATH);
        commonWhiteFont = FontBuilder.generate(40, Color.WHITE, GameResources.FONT_PATH);
        commonBlackFont = FontBuilder.generate(40, Color.BLACK, GameResources.FONT_PATH);
        commonRedFont = FontBuilder.generate(48, Color.RED, GameResources.FONT_PATH);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        audioManager = new AudioManager();

        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this);
        settingsScreen = new SettingsScreen(this);

        setScreen(menuScreen);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }
}
