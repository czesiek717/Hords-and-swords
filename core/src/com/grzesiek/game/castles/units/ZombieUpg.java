package com.grzesiek.game.castles.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Grzesiek on 2017-10-22.
 */

public class ZombieUpg extends Unit
{
    public ZombieUpg()
    {
        imageTexture = new Texture("castles/castle_1/units/zombieUpg.png");
        iconTexture = new Texture("castles/castle_1/units/zombieUpg_icon.png");
        miniTexture = new Texture("castles//castle_1/units/zombieUpg_mini.png");

        image = new Image(imageTexture);
        icon = new Image(iconTexture);
        mini = new Image(miniTexture);

        quantity = 0;
        ID = 4;

        cost = 15;
        attack = 15;
        defence = 10;
        minDamage = 6;
        maxDamage = 7;
        health = 10;
        speed = 6;
        growth = 7;
        ammo = 0;
        unitName = "Upg.Zombie";

        ranged = false;
    }
}
