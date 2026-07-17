package ru.samsung.jkgroup.obj;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.samsung.jkgroup.GameSettings;

public class TorpedoObj extends GameObj {

    public boolean wasHit;

    public TorpedoObj(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.TORPEDO_BIT, world);
        body.setLinearVelocity(new Vector2(0, GameSettings.TORPEDO_VELOCITY));
        body.setBullet(true);
        wasHit = false;
    }

    public boolean hasToBeDestroyed() {
        return wasHit || (getY() - height / 2 > GameSettings.SCREEN_HEIGHT);
    }

    @Override
    public void hit() {
        wasHit = true;
    }
}
