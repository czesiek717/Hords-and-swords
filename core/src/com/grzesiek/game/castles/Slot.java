package com.grzesiek.game.castles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.grzesiek.game.castles.units.Skeleton;
import com.grzesiek.game.castles.units.SkeletonUpg;
import com.grzesiek.game.castles.units.Unit;
import com.grzesiek.game.castles.units.Zombie;
import com.grzesiek.game.castles.units.ZombieUpg;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Grzesiek on 2017-10-17.
 */

public abstract class Slot
{
    protected Group group;
    protected Texture emptySlot;
    protected Image image;
    protected Label label;
    protected Rectangle bounds;
    protected Vector2 position;

    public Slot()
    {
        group = new Group();
        defineImages();
        defineLabels();
        addGroupElements();
    }

    private void defineImages()
    {
        emptySlot = new Texture("castles/emptySlot.png");
        image = new Image(emptySlot);
        image.setSize(image.getWidth() + 2, image.getHeight() + 1);
    }

    private void defineLabels()
    {
        label = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        label.setWidth(image.getWidth());
        label.setAlignment(Align.right);
        label.setVisible(false);
    }

    public void setLabelsPosition()
    {
        label.setPosition(group.getWidth() - label.getWidth() - 5, 0);
    }

    private void addGroupElements()
    {
        group.setSize(image.getWidth(), image.getHeight());
        group.addActor(image);
        group.addActor(label);
    }

    public void restorePosition()
    {
        group.setPosition(position.x, position.y);
        label.toFront();
    }

    public void dispose()
    {
        emptySlot.dispose();
    }

    public void setBounds()
    {
        bounds = new Rectangle(group.getX(), group.getY(), group.getWidth(), group.getHeight());
    }

    public void setPositionCoordinates(float x, float y)
    {
        position = new Vector2(x, y);
    }

    public Group getGroup()
    {
        return group;
    }

    public Image getImage()
    {
        return image;
    }

    public Label getLabel()
    {
        return label;
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public Vector2 getPosition()
    {
        return position;
    }
}