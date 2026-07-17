package ru.samsung.jkgroup.obj;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.jkgroup.GameSettings;

import java.util.Random;

public class ShipsObj extends GameObj {

    private static final int paddingHorizontal = 30;

    private int livesLeft;

    public ShipsObj(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width)),
                GameSettings.SCREEN_HEIGHT + height / 2,
                width, height,
                GameSettings.SHIPS_BIT,
                world
        );

        body.setLinearVelocity(new Vector2(0, -GameSettings.SHIPS_VELOCITY));
        livesLeft = 1;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

    public boolean isInFrame() {
        return getY() + height / 2 > 0;
    }

    @Override
    public void hit() {
        livesLeft -= 1;
    }
}
