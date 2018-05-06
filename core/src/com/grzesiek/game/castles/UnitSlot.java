package com.grzesiek.game.castles;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.grzesiek.game.castles.units.Skeleton;
import com.grzesiek.game.castles.units.SkeletonUpg;
import com.grzesiek.game.castles.units.Unit;
import com.grzesiek.game.castles.units.Zombie;
import com.grzesiek.game.castles.units.ZombieUpg;

/**
 * Created by Grzesiek on 2017-10-30.
 */

public class UnitSlot extends Slot
{
    Unit unit;

    public UnitSlot()
    {
        super();
        unit = null;
    }

    public void addUnit(Unit unit, int numberOfUnits)
    {
        createUnit(unit.getID());
        this.unit.adjustQuantity(numberOfUnits);
        image.setDrawable(new SpriteDrawable(new Sprite(this.unit.getIconTexture())));
        label.setText(String.valueOf(this.unit.getQuantity()));
        label.toFront();
        label.setVisible(true);
    }

    /**
     * in order to use this method properly you need to call <p> {@link #createUnit(int ID)} method
     * @param numberOfUnits - number of units in a slot
     */
    public void addUnit(int numberOfUnits)
    {
        this.unit.adjustQuantity(numberOfUnits);
        image.setDrawable(new SpriteDrawable(new Sprite(this.unit.getIconTexture())));
        label.setText(String.valueOf(this.unit.getQuantity()));
        label.toFront();
        label.setVisible(true);
    }

    public void createUnit(int ID)
    {
        switch(ID)
        {
            case 1:
                this.unit = new Skeleton();
                break;
            case 2:
                this.unit = new SkeletonUpg();
                break;
            case 3:
                this.unit = new Zombie();
                break;
            case 4:
                this.unit = new ZombieUpg();
                break;
        }
    }

    public void removeUnit()
    {
        this.unit = null;
        image.setDrawable(new SpriteDrawable(new Sprite(emptySlot)));
        image.toFront();
        label.setText("");
        label.setVisible(false);
    }

    public void setUnitQuantity(int quantity)
    {
        unit.setQuantity(quantity);
        label.setText(String.valueOf(unit.getQuantity()));
        label.toFront();
    }

    public void adjustUnitQuantity (int numberOfUnits)
    {
        unit.adjustQuantity(numberOfUnits);
        label.setText(String.valueOf(unit.getQuantity()));
        label.toFront();
    }

    public void swapSlots(UnitSlot slot)
    {
        Unit unit = this.getUnit();
        this.removeUnit();
        this.addUnit(slot.getUnit(), slot.getUnit().getQuantity());
        this.getLabel().toFront();
        this.getGroup().setPosition(position.x, position.y);

        slot.removeUnit();
        slot.addUnit(unit, unit.getQuantity());
        slot.getLabel().toFront();

        unit.dispose();
    }

    public Unit getUnit()
    {
        return unit;
    }
}
