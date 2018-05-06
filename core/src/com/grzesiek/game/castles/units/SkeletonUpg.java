package com.grzesiek.game.castles.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Grzesiek on 2017-10-17.
 */

public class SkeletonUpg extends Unit
{
    public SkeletonUpg()
    {
        imageTexture = new Texture("castles/castle_1/units/skeletonUpg.png");
        iconTexture = new Texture("castles/castle_1/units/skeletonUpg_icon.png");
        miniTexture = new Texture("castles//castle_1/units/skeletonUpg_mini.png");

        image = new Image(imageTexture);
        icon = new Image(iconTexture);
        mini = new Image(miniTexture);

        quantity = 0;
        ID = 2;

        cost = 6;
        attack = 7;
        defence = 6;
        minDamage = 3;
        maxDamage = 5;
        health = 6;
        speed = 9;
        growth = 9;
        ammo = 0;
        unitName = "Upg.Skeleton";

        ranged = false;
    }
}
