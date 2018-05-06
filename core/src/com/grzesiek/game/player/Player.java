package com.grzesiek.game.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grzesiek.game.castles.Castle;
import com.grzesiek.game.player.heroes.Goku;
import com.grzesiek.game.player.heroes.Hero;
import com.grzesiek.game.player.heroes.HeroInformation;
import com.grzesiek.game.screens.PlayScreen;

/**
 * Created by Grzesiek on 2017-08-27.
 */

public  class Player
{
    private Hero hero;
    private HeroInformation heroInformation;
    private Castle castle;

    private int wood;
    private int stone;
    private int crystal;
    private int deboil;
    private int gold;

    private int unit1;
    private int unit2;
    private int unit3;
    private int unit4;
    private int unit5;

    public Player(PlayScreen screen, SpriteBatch batch)
    {
        hero = new Goku(screen);
        heroInformation = new HeroInformation(batch, this);
        castle = new Castle(batch, this);

        wood = 200;
        stone = 200;
        crystal = 200;
        deboil = 200;
        gold = 200;

        unit1 = 0;
        unit2 = 0;
        unit3 = 0;
        unit4 = 0;
        unit5 = 0;
    }

    public void dispose()
    {
        hero.dispose();
        heroInformation.dispose();
        castle.dispose();
    }

    public void adjustWood (int wood)
    {
        this.wood += wood;
    }

    public void adjustStone (int stone)
    {
        this.stone += stone;
    }

    public void adjustCrystal (int crystal)
    {
        this.crystal += crystal;
    }

    public void adjustDeboil (int deboil)
    {
        this.deboil += deboil;
    }

    public void adjustGold (int gold)
    {
        this.gold += gold;
    }

    public void adjustUnit1 (int unit1)
    {
        this.unit1 += unit1;
    }

    public void adjustUnit2 (int unit2)
    {
        this.unit2 += unit2;
    }

    public void adjustUnit3 (int unit3)
    {
        this.unit3 += unit3;
    }

    public void adjustUnit4 (int unit4)
    {
        this.unit4 += unit4;
    }

    public void adjustUnit5 (int unit5)
    {
        this.unit5 += unit5;
    }

    public void setWood (int wood)
    {
        this.wood = wood;
    }

    public void setStone (int stone)
    {
        this.stone = stone;
    }

    public void setCrystal (int crystal)
    {
        this.crystal = crystal;
    }

    public void setDeboil (int deboil)
    {
        this.deboil = deboil;
    }

    public void setGold (int gold)
    {
        this.gold = gold;
    }

    public void setUnit1 (int unit1)
    {
        this.unit1 = unit1;
    }

    public void setUnit2 (int unit2)
    {
        this.unit2 = unit2;
    }

    public void setUnit3 (int unit3)
    {
        this.unit3 = unit3;
    }

    public void setUnit4 (int unit4)
    {
        this.unit4 = unit4;
    }

    public void setUnit5 (int unit5)
    {
        this.unit5 = unit5;
    }

    public Hero getHero()
    {
        return hero;
    }

    public HeroInformation getHeroInformation()
    {
        return heroInformation;
    }

    public Castle getCastle()
    {
        return castle;
    }

    public int getWood ()
    {
        return wood;
    }

    public int getStone ()
    {
        return stone;
    }

    public int getCrystal ()
    {
        return crystal;
    }

    public int getDeboil ()
    {
        return deboil;
    }

    public int getGold ()
    {
        return gold;
    }

    public int getUnit1 ()
    {
        return unit1;
    }

    public int getUnit2 ()
    {
        return unit2;
    }

    public int getUnit3 ()
    {
        return unit3;
    }

    public int getUnit4 ()
    {
        return unit4;
    }

    public int getUnit5 ()
    {
        return unit5;
    }
}
