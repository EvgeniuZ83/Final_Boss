package ru.samsung.jkgroup.managers;

import com.badlogic.gdx.physics.box2d.*;
import ru.samsung.jkgroup.GameSettings;
import ru.samsung.jkgroup.obj.GameObj;

public class ContactManager {

    World world;

    public ContactManager(World world) {
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDef = fixA.getFilterData().categoryBits;
                int cDef2 = fixB.getFilterData().categoryBits;

                if (cDef == GameSettings.SHIPS_BIT && cDef2 == GameSettings.TORPEDO_BIT
                        || cDef2 == GameSettings.SHIPS_BIT && cDef == GameSettings.TORPEDO_BIT
                        || cDef == GameSettings.SHIPS_BIT && cDef2 == GameSettings.SUB_BIT
                        || cDef2 == GameSettings.SHIPS_BIT && cDef == GameSettings.SUB_BIT) {

                    ((GameObj) fixA.getUserData()).hit();
                    ((GameObj) fixB.getUserData()).hit();

                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

    }

}
