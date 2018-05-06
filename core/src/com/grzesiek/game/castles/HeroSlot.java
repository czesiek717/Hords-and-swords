package com.grzesiek.game.castles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by Grzesiek on 2017-10-30.
 */

public class HeroSlot extends Slot
{
    private String heroName;

    private Texture emptyHeroSlot;
    private Texture heroIconTexture;

    public HeroSlot()
    {
        super();
        emptyHeroSlot = new Texture("castles/emptyHeroSlot.png");
    }

    public void addHero(String heroName, Texture heroIconTexture)
    {
        this.heroName = heroName;
        this.heroIconTexture = heroIconTexture;
        image.setDrawable(new SpriteDrawable(new Sprite(heroIconTexture)));
        label.setText(this.heroName);
        label.toFront();
        label.setVisible(true);
    }

    void removeHero(boolean inCastle)
    {
        heroName = null;
        heroIconTexture = null;
        if(inCastle)
        {
            image.setDrawable(new SpriteDrawable(new Sprite(emptyHeroSlot)));
        }
        else
        {
            image.setDrawable(new SpriteDrawable(new Sprite(emptySlot)));
        }
        image.toFront();
        label.setText("");
        label.setVisible(false);
    }

    void swapSlots(HeroSlot slot, boolean inCastle)
    {
        slot.addHero(this.heroName, this.heroIconTexture);
        this.removeHero(inCastle);
        this.getLabel().setVisible(false);
        this.getGroup().setPosition(position.x, position.y);

        slot.getLabel().toFront();
    }

    public void setImageToEmptyCastleSlot()
    {
        image.setDrawable(new SpriteDrawable(new SpriteDrawable(new Sprite(emptyHeroSlot))));
    }

    public String getHeroName()
    {
        return heroName;
    }
}
