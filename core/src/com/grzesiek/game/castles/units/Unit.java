package com.grzesiek.game.castles.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Grzesiek on 2017-09-24.
 */

public abstract class Unit
{
    protected Image image;
    Image icon;
    Image mini;

    Texture imageTexture;
    Texture iconTexture;
    Texture miniTexture;

    int quantity;
    int ID;

    int cost;
    int attack;
    int defence;
    int minDamage;
    int maxDamage;
    int health;
    int speed;
    int growth;
    int ammo;

    String unitName;

    boolean ranged;

    public void dispose()
    {
        imageTexture.dispose();
        iconTexture.dispose();
        miniTexture.dispose();
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void adjustQuantity(int quantity)
    {
        this.quantity += quantity;
    }

    public int getID()
    {
        return ID;
    }

    public Texture getIconTexture()
    {
        return iconTexture;
    }

    public Texture getImageTexture()
    {
        return imageTexture;
    }

    public Texture getMiniTexture()
    {
        return miniTexture;
    }

    public Image getImage()
    {
        return image;
    }

    public Image getMini()
    {
        return mini;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public int getCost ()
    {
        return cost;
    }

    public int getAttack ()
    {
        return attack;
    }

    public int getDefence ()
    {
        return defence;
    }

    public int getMinDamage ()
    {
        return minDamage;
    }

    public int getMaxDamage ()
    {
        return maxDamage;
    }

    public int getHealth ()
    {
        return health;
    }

    public int getSpeed ()
    {
        return speed;
    }

    public int getGrowth ()
    {
        return growth;
    }

    public int getAmmo()
    {
        return ammo;
    }

    public String getName()
    {
        return unitName;
    }

    public boolean isRanged()
    {
        return ranged;
    }
}
