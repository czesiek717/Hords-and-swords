package com.grzesiek.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.grzesiek.game.Dragon;
import com.grzesiek.game.objects.StockObject;
import com.grzesiek.game.player.Player;

/**
 * Created by Grzesiek on 2017-08-29.
 */

public class WorldContactListener implements ContactListener
{
    private Player player;
    private Array<Body> bodiesToRemove;

    public WorldContactListener (Player player)
    {
        super();
        this.player = player;
        bodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact (final Contact contact)
    {
        final Fixture fixA = contact.getFixtureA();
        final Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch(cDef)
        {
            case Dragon.HERO_BIT | Dragon.DOORS_BIT:
                Gdx.app.log("WorldContactListener", " touched doors");
                Thread castleThread = new Thread("player in castle doors")
                {
                    public void run()
                    {
                        while(contact.isTouching())
                        {
                            player.getHero().setInCastle(true);
                        }
                    }
                };
                castleThread.start();
                break;

            case Dragon.HERO_BIT | Dragon.OBSTACLES_BIT:
                Gdx.app.log("WorldContactListener", " touched obstacle");
                break;
            case Dragon.HERO_BIT | Dragon.WOOD_BIT:
            case Dragon.HERO_BIT | Dragon.STONE_BIT:
            case Dragon.HERO_BIT | Dragon.CRYSTAL_BIT:
            case Dragon.HERO_BIT | Dragon.DEBOIL_BIT:
            case Dragon.HERO_BIT | Dragon.GOLD_BIT:
                Gdx.app.log("WorldContactListener", " touched stock");
                Thread stockThread = new Thread("picking up stock")
                {
                    public void run()
                    {
                        while(contact.isTouching())
                        {
                            if(player.getHero().isPressedA())
                            {
                                Gdx.app.log("WorldContactListener", " picking up stock");
                                if(fixA.getFilterData().categoryBits == Dragon.HERO_BIT)
                                {
                                    ((StockObject) fixB.getUserData()).onActivation(player);
                                    bodiesToRemove.add(fixB.getBody());
                                    break;

                                }
                                else
                                {
                                    ((StockObject) fixA.getUserData()).onActivation(player);
                                    bodiesToRemove.add(fixA.getBody());
                                    break;

                                }
                            }
                        }
                    }
                };
                stockThread.start();
                break;
        }
    }

    @Override
    public void endContact (Contact contact)
    {
    }

    @Override
    public void preSolve (Contact contact, Manifold oldManifold)
    {
    }

    @Override
    public void postSolve (Contact contact, ContactImpulse impulse)
    {
    }

    public Array<Body> getBodiesToRemove ()
    {
        return bodiesToRemove;
    }

    public void dispose()
    {
        player.dispose();
    }
}