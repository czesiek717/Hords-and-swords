package com.grzesiek.game.castles.buildings;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.castles.units.Zombie;
import com.grzesiek.game.castles.units.ZombieUpg;

/**
 * Created by Grzesiek on 2017-10-22.
 */

public class Graveyard extends UnitRecruiter
{
    public Graveyard(Viewport port, OrthographicCamera cam, SpriteBatch batch)
    {
        super(port, cam, batch);

        buildingImage = new Image(new Texture("castles/castle_1/building_2.png"));

        unit[0] = new Zombie();
        unit[1] = new ZombieUpg();

        requirements = new String[1];
        requirements[0] = "Skeleton's Crypt";
        description = "Graveyard allows its owner to recruit Zombies and Upg.Zombies";
        name = "Graveyard";
        upgName = "Upg.Graveyard";
        currentName = name;


        cost[0] = 15;
        cost[1] = 16;
        cost[2] = 17;
        cost[3] = 18;
        cost[4] = 19;
    }
}
