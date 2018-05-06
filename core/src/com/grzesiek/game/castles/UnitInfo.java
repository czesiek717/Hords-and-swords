package com.grzesiek.game.castles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.castles.units.Unit;
import com.grzesiek.game.player.Player;

/**
 * Created by Grzesiek on 2017-10-28.
 */

public class UnitInfo
{
    private Viewport viewport;

    private Stage stage;
    private Table table;

    private UnitSlot slot;

    private InformationDialog informationDialog;

    private Image backgroundImage;
    private Image unitImage;
    private Image upgradeImage;
    private Image disbandImage;
    private Image acceptImage;
    private Image moraleImage;
    private Image luckImage;
    private Image unitNameImage;
    private Image[] statsImage;

    private Label unitNameLabel;
    private Label unitQuantityLabel;
    private Label[] statsLabel;

    private boolean visible;
    private boolean justClosed;

    private int operationCost;

    public UnitInfo (Viewport port, final SpriteBatch batch, final Player player)
    {
        this.viewport = port;
        viewport.update(269, 281);

        stage = new Stage(viewport, batch);
        table = new Table();

        slot = null;

        informationDialog = new InformationDialog(viewport, batch);

        visible = false;
        justClosed = false;

        operationCost = 0;

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();

        Group group = new Group();
        addGroupElements(group);

        table.center().center();
        table.setFillParent(true);
        table.add(group).size(backgroundImage.getWidth(), backgroundImage.getHeight());

        stage.addActor(table);

        upgradeImage.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                Action action = new Action()
                {
                    @Override
                    public boolean act (float delta)
                    {
                        if(player.getGold() >= operationCost)
                        {
                            player.adjustGold(- operationCost);
                            upgrade();
                        }
                        slot = null;
                        informationDialog.setVisible(false);
                        return false;
                    }
                };
                informationDialog.setVisible(true);
                if(player.getGold() >= operationCost)
                {
                    informationDialog.update("Are you sure?",
                            "In order to upgrade these units you have to properly train them. This operation will cost you: " + operationCost + " gold", action);
                }
                else
                {
                    informationDialog.update("Sorry",
                        "You do not have enough gold to upgrade these units", action);
                    informationDialog.getDeclineImage().setVisible(false);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        disbandImage.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                disband();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        acceptImage.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                visible = false;
                justClosed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void defineImages()
    {
        statsImage = new Image[7];

        backgroundImage = new Image(new Texture("castles/unitUpgrade.png"));
        unitImage = new Image(new Texture("castles/castle_1/units/skeleton.png"));
        upgradeImage = new Image(new Texture("castles/upgrade.png"));
        disbandImage = new Image(new Texture("castles/disband.png"));
        acceptImage = new Image(new Texture("castles/accept.png"));
        moraleImage = new Image(new Texture("castles/morale.png"));
        luckImage = new Image(new Texture("castles/luck.png"));
        unitNameImage = new Image(new Texture("castles/unitStats.png"));
        for(int i = 0; i < statsImage.length; i++)
        {
            statsImage[i] = new Image(new Texture("castles/unitStats.png"));
        }
    }

    private void defineLabels()
    {
        statsLabel = new Label[14];

        unitNameLabel = new Label("skeleton", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitQuantityLabel = new Label("10", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[0] = new Label("attack: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[1] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[2] = new Label("defence: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[3] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[4] = new Label("damage: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[5] = new Label("0 - 0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[6] = new Label("ammo: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[7] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[8] = new Label("health: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[9] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[10] = new Label("speed: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[11] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[12] = new Label("growth: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        statsLabel[13] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        unitQuantityLabel.setWidth(unitImage.getWidth() - 5);
    }

    private void setImagesPosition()
    {
        for(int i = 0; i < statsImage.length; i++)
        {
            statsImage[i].setSize(125, statsImage[i].getHeight());
        }
        unitNameImage.setSize(unitNameImage.getWidth() - 20, unitNameImage.getHeight());

        unitNameImage.setPosition(backgroundImage.getX() + backgroundImage.getWidth() / 2 - unitNameImage.getWidth() / 2, backgroundImage.getY() + backgroundImage.getHeight() - unitNameImage.getHeight() - 15);
        unitImage.setPosition(unitNameImage.getX(), unitNameImage.getY() - unitImage.getHeight() - 7);
        statsImage[0].setPosition(unitImage.getX() + unitImage.getWidth() + 15, unitImage.getY() + unitImage.getHeight() - statsImage[0].getHeight());
        statsImage[1].setPosition(statsImage[0].getX(), statsImage[0].getY() - statsImage[1].getHeight());
        statsImage[2].setPosition(statsImage[1].getX(), statsImage[1].getY() - statsImage[2].getHeight());
        statsImage[3].setPosition(statsImage[2].getX(), statsImage[2].getY() - statsImage[3].getHeight());
        statsImage[4].setPosition(statsImage[3].getX(), statsImage[3].getY() - statsImage[4].getHeight());
        statsImage[5].setPosition(statsImage[4].getX(), statsImage[4].getY() - statsImage[5].getHeight());
        statsImage[6].setPosition(statsImage[5].getX(), statsImage[5].getY() - statsImage[6].getHeight());
        moraleImage.setPosition(unitImage.getX(), unitImage.getY() - moraleImage.getHeight() - 7);
        luckImage.setPosition(moraleImage.getX() + moraleImage.getWidth() + 7, moraleImage.getY());
        disbandImage.setPosition(moraleImage.getX(), moraleImage.getY() - disbandImage.getHeight() - 7);
        upgradeImage.setPosition(luckImage.getX(), disbandImage.getY());
        acceptImage.setPosition(statsImage[6].getX() + statsImage[6].getWidth() - acceptImage.getWidth(), disbandImage.getY());
    }

    private void setLabelsPosition()
    {
        unitNameLabel.setPosition(unitNameImage.getX() + unitNameImage.getWidth() / 2 - unitNameLabel.getWidth() / 2, unitNameImage.getY() + unitNameImage.getHeight() / 2 - unitNameLabel.getHeight() / 2);
        unitQuantityLabel.setPosition(unitImage.getX(), unitImage.getY());
        statsLabel[0].setPosition(statsImage[0].getX() + 10, statsImage[0].getY() + statsImage[0].getHeight() / 2 - statsLabel[0].getHeight() / 2);
        statsLabel[1].setPosition(statsImage[0].getX() + statsImage[0].getWidth() - statsLabel[1].getWidth() - 10,
                statsImage[0].getY() + statsImage[0].getHeight() / 2 - statsLabel[0].getHeight() / 2);
        statsLabel[2].setPosition(statsImage[1].getX() + 10, statsImage[1].getY() + statsImage[1].getHeight() / 2 - statsLabel[2].getHeight() / 2);
        statsLabel[3].setPosition(statsImage[1].getX() + statsImage[1].getWidth() - statsLabel[3].getWidth() - 10,
                statsImage[1].getY() + statsImage[1].getHeight() / 2 - statsLabel[3].getHeight() / 2);
        statsLabel[4].setPosition(statsImage[2].getX() + 10, statsImage[2].getY() + statsImage[2].getHeight() / 2 - statsLabel[4].getHeight() / 2);
        statsLabel[5].setPosition(statsImage[2].getX() + statsImage[2].getWidth() - statsLabel[5].getWidth() - 10,
                statsImage[2].getY() + statsImage[2].getHeight() / 2 - statsLabel[5].getHeight() / 2);
        statsLabel[6].setPosition(statsImage[3].getX() + 10, statsImage[3].getY() + statsImage[3].getHeight() / 2 - statsLabel[6].getHeight() / 2);
        statsLabel[7].setPosition(statsImage[3].getX() + statsImage[3].getWidth() - statsLabel[7].getWidth() - 10,
                statsImage[3].getY() + statsImage[3].getHeight() / 2 - statsLabel[7].getHeight() / 2);
        statsLabel[8].setPosition(statsImage[4].getX() + 10, statsImage[4].getY() + statsImage[4].getHeight() / 2 - statsLabel[8].getHeight() / 2);
        statsLabel[9].setPosition(statsImage[4].getX() + statsImage[4].getWidth() - statsLabel[9].getWidth() - 10,
                statsImage[4].getY() + statsImage[4].getHeight() / 2 - statsLabel[9].getHeight() / 2);
        statsLabel[10].setPosition(statsImage[5].getX() + 10, statsImage[5].getY() + statsImage[5].getHeight() / 2 - statsLabel[10].getHeight() / 2);
        statsLabel[11].setPosition(statsImage[5].getX() + statsImage[5].getWidth() - statsLabel[11].getWidth() - 10,
                statsImage[5].getY() + statsImage[5].getHeight() / 2 - statsLabel[11].getHeight() / 2);
        statsLabel[12].setPosition(statsImage[6].getX() + 10, statsImage[6].getY() + statsImage[6].getHeight() / 2 - statsLabel[12].getHeight() / 2);
        statsLabel[13].setPosition(statsImage[6].getX() + statsImage[6].getWidth() - statsLabel[13].getWidth() - 10,
                statsImage[6].getY() + statsImage[6].getHeight() / 2 - statsLabel[13].getHeight() / 2);

        unitNameLabel.setAlignment(Align.center);
        unitQuantityLabel.setAlignment(Align.right);
        statsLabel[1].setAlignment(Align.right);
        statsLabel[3].setAlignment(Align.right);
        statsLabel[5].setAlignment(Align.right);
        statsLabel[7].setAlignment(Align.right);
        statsLabel[9].setAlignment(Align.right);
        statsLabel[11].setAlignment(Align.right);
        statsLabel[12].setAlignment(Align.right);
        statsLabel[13].setAlignment(Align.right);
    }

    private void addGroupElements(Group group)
    {
        //adding images to group
        group.addActor(backgroundImage);
        group.addActor(unitImage);
        group.addActor(upgradeImage);
        group.addActor(disbandImage);
        group.addActor(moraleImage);
        group.addActor(luckImage);
        group.addActor(acceptImage);
        group.addActor(unitNameImage);
        group.addActor(statsImage[0]);
        group.addActor(statsImage[1]);
        group.addActor(statsImage[2]);
        group.addActor(statsImage[3]);
        group.addActor(statsImage[4]);
        group.addActor(statsImage[5]);
        group.addActor(statsImage[6]);
        //adding labels to group
        group.addActor(unitNameLabel);
        group.addActor(unitQuantityLabel);
        group.addActor(statsLabel[0]);
        group.addActor(statsLabel[1]);
        group.addActor(statsLabel[2]);
        group.addActor(statsLabel[3]);
        group.addActor(statsLabel[4]);
        group.addActor(statsLabel[5]);
        group.addActor(statsLabel[6]);
        group.addActor(statsLabel[7]);
        group.addActor(statsLabel[8]);
        group.addActor(statsLabel[9]);
        group.addActor(statsLabel[10]);
        group.addActor(statsLabel[11]);
        group.addActor(statsLabel[12]);
        group.addActor(statsLabel[13]);
    }

    private void upgrade()
    {
        int ID = slot.getUnit().getID();
        int quantity = slot.getUnit().getQuantity();
        slot.removeUnit();
        slot.createUnit(ID + 1);
        slot.addUnit(quantity);
        update(slot, false);
        operationCost = 0;
    }

    private void disband()
    {
        slot.removeUnit();
        visible = false;
    }

    public void update(UnitSlot slot, boolean upgrade)
    {
        this.slot = slot;

        if(upgrade)
        {
            int ID = slot.getUnit().getID();
            UnitSlot tempSlot = new UnitSlot();
            tempSlot.createUnit(ID + 1);
            operationCost = (tempSlot.getUnit().getCost() - slot.getUnit().getCost()) * slot.getUnit().getQuantity();
            tempSlot.dispose();
            upgradeImage.setVisible(true);
        }
        else
        {
            upgradeImage.setVisible(false);
        }

        Unit unit = slot.getUnit();

        unitImage.setDrawable(new SpriteDrawable(new SpriteDrawable(new Sprite(unit.getImageTexture()))));

        unitNameLabel.setText(unit.getName());
        unitQuantityLabel.setText(String.valueOf(unit.getQuantity()));
        statsLabel[1].setText(String.valueOf(unit.getAttack()));
        statsLabel[3].setText(String.valueOf(unit.getDefence()));
        statsLabel[5].setText(String.valueOf(unit.getMinDamage()) + "-" +String.valueOf(unit.getMaxDamage()));
        if(unit.getAmmo() > 0)
        {
            statsLabel[7].setText(String.valueOf(unit.getAmmo()));
        }
        else
        {
            statsLabel[7].setText("-");
        }
        statsLabel[9].setText(String.valueOf(unit.getHealth()));
        statsLabel[11].setText(String.valueOf(unit.getSpeed()));
        statsLabel[13].setText(String.valueOf(unit.getGrowth()));
    }

    public void draw()
    {
        stage.draw();
        if(informationDialog.isVisible())
        {
            informationDialog.draw();
        }
        else
        {
            Gdx.input.setInputProcessor(stage);
        }
    }

    public void dispose()
    {
        stage.dispose();
        slot.dispose();
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public void setJustClosed(boolean justClosed)
    {
        this.justClosed = justClosed;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public boolean isJustClosed()
    {
        return justClosed;
    }

    public Stage getStage()
    {
        return stage;
    }

    public InformationDialog getInformationDialog()
    {
        return informationDialog;
    }
}
