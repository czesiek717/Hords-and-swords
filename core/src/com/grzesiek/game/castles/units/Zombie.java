package com.grzesiek.game.castles.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Grzesiek on 2017-10-22.
 */

public class Zombie extends Unit
{
    public Zombie()
    {
        imageTexture = new Texture("castles/castle_1/units/zombie.png");
        iconTexture = new Texture("castles/castle_1/units/zombie_icon.png");
        miniTexture = new Texture("castles//castle_1/units/zombie_mini.png");

        image = new Image(imageTexture);
        icon = new Image(iconTexture);
        mini = new Image(miniTexture);

        quantity = 0;
        ID = 3;

        cost = 10;
        attack = 12;
        defence = 8;
        minDamage = 4;
        maxDamage = 5;
        health = 8;
        speed = 5;
        growth = 7;
        ammo = 0;
        unitName = "Zombie";

        ranged = false;
    }
}
