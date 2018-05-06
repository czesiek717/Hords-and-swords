package com.grzesiek.game.castles.buildings;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.castles.units.Skeleton;
import com.grzesiek.game.castles.units.SkeletonUpg;

/**
 * Created by Grzesiek on 2017-10-18.
 */

public class SkeletonsCrypt extends UnitRecruiter
{
    public SkeletonsCrypt (Viewport port, OrthographicCamera cam, SpriteBatch batch)
    {
        super(port, cam, batch);

        buildingImage = new Image(new Texture("castles/castle_1/building_1.png"));

        unit[0] = new Skeleton();
        unit[1] = new SkeletonUpg();

        requirements = null;
        description = "Skeleton's crypt allows its owner to recruit Skeletons and Upg.Skeletons";
        name = "Skeleton's Crypt";
        upgName = "Upg.Skeleton's Crypt";
        currentName = name;


        cost[0] = 5;
        cost[1] = 6;
        cost[2] = 7;
        cost[3] = 8;
        cost[4] = 9;
    }
}
