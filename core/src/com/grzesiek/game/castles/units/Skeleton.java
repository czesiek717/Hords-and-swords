package com.grzesiek.game.castles.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Grzesiek on 2017-10-17.
 */

public class Skeleton extends Unit
{
    public Skeleton()
    {
        imageTexture = new Texture("castles/castle_1/units/skeleton.png");
        iconTexture = new Texture("castles/castle_1/units/skeleton_icon.png");
        miniTexture = new Texture("castles//castle_1/units/skeleton_mini.png");

        image = new Image(imageTexture);
        icon = new Image(iconTexture);
        mini = new Image(miniTexture);

        quantity = 0;
        ID = 1;

        cost = 3;
        attack = 5;
        defence = 4;
        minDamage = 1;
        maxDamage = 2;
        health = 4;
        speed = 7;
        growth = 9;
        ammo = 0;
        unitName = "Skeleton";

        ranged = false;
    }
}
