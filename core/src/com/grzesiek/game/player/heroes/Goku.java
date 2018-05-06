package com.grzesiek.game.player.heroes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.grzesiek.game.castles.UnitSlot;
import com.grzesiek.game.castles.units.Skeleton;
import com.grzesiek.game.castles.units.ZombieUpg;
import com.grzesiek.game.screens.PlayScreen;

/**
 * Created by Grzesiek on 2017-09-17.
 */

public class Goku extends Hero
{
    public Goku(PlayScreen screen)
    {
        super(screen, new Sprite(new Texture("players/player_1/player.png")));

        heroIconTexture = new Texture("players/player_1/player_icon.png");

        running = false;
        pressedA = false;

        startingPosX = 30 * 16;
        startingPosY = 27 * 16;

        playerSpeed = 50.0f;
        playerMovementBooster = 50.0f;

        movementPoints = 1000.0f;
        defaultMovementFactor = 1f;
        movementFactor = defaultMovementFactor;

        name = "Goku";
        level = 1;
        unitSlot[0].addUnit(new Skeleton(), 20);
        unitSlot[1].addUnit(new Skeleton(), 1);
        unitSlot[2].addUnit(new ZombieUpg(), 7);

        defineHero();
        setBounds(0, 0, spriteWidth, spriteHeight);
    }

    public void dispose()
    {
        world.dispose();
    }
}
