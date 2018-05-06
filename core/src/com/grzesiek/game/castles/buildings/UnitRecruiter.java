package com.grzesiek.game.castles.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.castles.Castle;
import com.grzesiek.game.castles.InformationDialog;
import com.grzesiek.game.castles.units.Unit;
import com.grzesiek.game.player.Player;

/**
 * Created by Grzesiek on 2017-09-19.
 */

public abstract class UnitRecruiter
{
    private Viewport viewport;
    private OrthographicCamera cam;

    private Stage stage;
    private Table table;

    protected Unit[] unit;

    protected InformationDialog informationDialog;

    protected Image buildingImage;
    private Image slider;
    private Image sliderBar;
    private Image accept;
    private Image decline;
    private Image[] unitStatsImage;
    private Image[] unitCost;
    private Image[] sliderMover;
    private Image[] recruitmentWindow;
    private Image[] unitAmount;

    private Label[] unitCostLabel;
    private Label[] unitAmountLabel;
    private Label[] unitStatsLabel;

    private int imageOffset;
    private int unitsAvailable;
    private int currentUnits;
    private int unitPrice;
    private int currentPrice;
    protected  int[] cost;

    private float sliderMove;

    protected boolean created;
    private boolean visible;
    private boolean upgradedUnit;
    private boolean upgradedBuilding;

    protected String description;
    protected String currentName;
    protected String name;
    protected String upgName;
    protected String[] requirements;

    public UnitRecruiter (Viewport port, OrthographicCamera cam, SpriteBatch batch)
    {
        this.viewport = port;
        this.cam = cam;
        viewport.update(550, 405);

        stage = new Stage(viewport, batch);
        table = new Table();

        unit = new Unit[2];

        informationDialog = new InformationDialog(viewport, batch);

        cost = new int[5];
        created = false;
    }

    public void create(final Player player, final Castle castle)
    {
        unitPrice = unit[0].getCost();
        unitsAvailable = unit[0].getGrowth();
        imageOffset = 10;
        currentUnits = 0;
        currentPrice = 0;

        created = true;
        visible = false;
        upgradedUnit = false;
        upgradedBuilding = false;

        currentName = upgName;

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();

        Group group = new Group();
        addGroupElements(group);

        updateSlidersMove();

        table.center().center();
        table.setFillParent(true);
        table.add(group).size(recruitmentWindow[0].getWidth() * 3, recruitmentWindow[0].getHeight() * 3);

        stage.addActor(table);

        sliderMover[0].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(slider.getX() > sliderBar.getX())
                {
                    if(unitsAvailable > 0)
                    {
                        currentUnits--;
                        update();
                        slider.setPosition(slider.getX() - sliderMove, slider.getY());
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        sliderMover[1].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(slider.getX() < sliderBar.getX() + sliderBar.getWidth() - slider.getWidth())
                {
                    if(player.getGold() >= currentPrice + unitPrice && unitsAvailable > 0)
                    {
                        currentUnits++;
                        update();
                        slider.setPosition(slider.getX() + sliderMove, slider.getY());
                    }
                    else
                    {
                        noEnoughGold();
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        sliderMover[2].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(player.getGold() > 0 && unitsAvailable > 0)
                {
                    currentUnits = 0;
                    update();
                    slider.setPosition(sliderBar.getX(), slider.getY());
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        sliderMover[3].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(player.getGold() >= unitPrice * unitsAvailable && player.getGold() > 0 && unitsAvailable > 0)
                {
                    currentUnits = unitsAvailable;
                    update();
                    slider.setPosition(sliderBar.getX() + sliderBar.getWidth() - slider.getWidth(), slider.getY());
                }
                else
                {
                    if(player.getGold() >= currentPrice + unitPrice && player.getGold() > 0 && unitsAvailable > 0)
                    {
                        int counter;
                        counter = player.getGold() / unitPrice;
                        currentUnits = 1 * counter;
                        currentPrice = unitPrice * counter;
                        update();
                        slider.setPosition(sliderBar.getX() + sliderMove * counter, slider.getY());
                    }
                    else
                    {
                        noEnoughGold();
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        unit[0].getImage().addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(upgradedBuilding)
                {
                    upgradedUnit = false;
                    unitPrice = unit[0].getCost();
                    unitCostLabel[1].setText(String.valueOf(unitPrice));
                    update();
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        unit[1].getImage().addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(upgradedBuilding)
                {
                    upgradedUnit = true;
                    unitPrice = unit[1].getCost();
                    unitCostLabel[1].setText(String.valueOf(unitPrice));
                    update();
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        accept.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(player.getGold() >= currentPrice && currentUnits > 0)
                {
                    if(upgradedUnit)
                    {
                        if(castle.updateCastleSlot(unit[1], currentUnits) >= 0)
                        {
                            Gdx.app.log("purchased", String.valueOf(currentUnits) + " " + unit[1].getName() + "('s)  for: " + String.valueOf(currentPrice) + " gold");
                        }
                        else
                        {
                            noEmptyCastleSlot();
                            return false;
                        }
                    }
                    else
                    {
                        if(castle.updateCastleSlot(unit[0], currentUnits) >= 0)
                        {
                            Gdx.app.log("purchased", String.valueOf(currentUnits) + " " + unit[0].getName() + "('s)  for: " + String.valueOf(currentPrice) + " gold");
                        }
                        else
                        {
                            noEmptyCastleSlot();
                            return false;
                        }
                    }
                    player.adjustGold(-currentPrice);
                    unitsAvailable -= currentUnits;
                    currentUnits = 0;
                    currentPrice = 0;
                    slider.setPosition(sliderBar.getX(), slider.getY());
                    update();
                }
                else
                {
                    noEnoughGold();
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        decline.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                visible = false;
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
        recruitmentWindow = new Image[3];
        unitCost = new Image[2];
        unitAmount = new Image[2];
        sliderMover = new Image[4];
        unitStatsImage = new Image[7];

        recruitmentWindow[0] = new Image(new Texture("castles/unitRecruitmentWindow.png"));
        recruitmentWindow[1] = new Image(new Texture("castles/unitRecruitmentWindow.png"));
        recruitmentWindow[2] = new Image(new Texture("castles/unitRecruitmentWindow.png"));
        unitStatsImage[0] = new Image(new Texture("castles/unitStats.png"));
        unitStatsImage[1] = new Image(new Texture("castles/unitStats.png"));
        unitStatsImage[2] = new Image(new Texture("castles/unitStats.png"));
        unitStatsImage[3] = new Image(new Texture("castles/unitStats.png"));
        unitStatsImage[4] = new Image(new Texture("castles/unitStats.png"));
        unitStatsImage[5] = new Image(new Texture("castles/unitStats.png"));
        unitStatsImage[6] = new Image(new Texture("castles/unitStats.png"));
        unitCost[0] = new Image(new Texture("castles/unitCost.png"));
        unitCost[1] = new Image(new Texture("castles/unitCost.png"));
        unitAmount[0] = new Image(new Texture("castles/unitAmount.png"));
        unitAmount[1] = new Image(new Texture("castles/unitAmount.png"));
        slider = new Image(new Texture("castles/slider.png"));
        sliderBar = new Image(new Texture("castles/sliderBar.png"));
        sliderMover[0] = new Image(new Texture("castles/sliderMover_1.png"));
        sliderMover[1] = new Image(new Texture("castles/sliderMover_2.png"));
        sliderMover[2] = new Image(new Texture("castles/sliderMover_3.png"));
        sliderMover[3] = new Image(new Texture("castles/sliderMover_4.png"));
        accept = new Image(new Texture("castles/buy.png"));
        decline = new Image(new Texture("castles/decline.png"));
    }

    private void defineLabels()
    {
        unitCostLabel = new Label[4];
        unitAmountLabel = new Label[4];
        unitStatsLabel = new Label[14];

        unitCostLabel[0] = new Label("Cost per unit:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitCostLabel[1] = new Label(String.valueOf(unitPrice), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitAmountLabel[0] = new Label("available:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitAmountLabel[1] = new Label("to recruit:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitAmountLabel[2] = new Label(String.valueOf(unitsAvailable), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitAmountLabel[3] = new Label(String.valueOf(currentUnits), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitCostLabel[2] = new Label("Total cost:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitCostLabel[3] = new Label(String.valueOf(currentPrice), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[0] = new Label("attack: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[1] = new Label(String.valueOf(unit[0].getAttack()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[2] = new Label("defence: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[3] = new Label(String.valueOf(unit[0].getDefence()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[4] = new Label("damage: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[5] = new Label(String.valueOf(unit[0].getMinDamage()) +"-" +String.valueOf(unit[0].getMaxDamage()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[6] = new Label("ammo: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        if(unit[0].getAmmo() > 0)
        {
            unitStatsLabel[7] = new Label(String.valueOf(unit[0].getAmmo()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        }
        else
        {
            unitStatsLabel[7] = new Label("-", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        }
        unitStatsLabel[8] = new Label("health: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[9] = new Label(String.valueOf(unit[0].getHealth()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[10] = new Label("speed: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[11] = new Label(String.valueOf(unit[0].getSpeed()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[12] = new Label("growth: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        unitStatsLabel[13] = new Label(String.valueOf(unit[0].getGrowth()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    }

    private void setImagesPosition()
    {
        accept.setSize(100, 50);
        decline.setSize(100, 50);
        sliderMover[0].setSize(50, 50);
        sliderMover[1].setSize(50, 50);
        sliderMover[2].setSize(50, 50);
        sliderMover[3].setSize(50, 50);

        unit[1].getImage().setVisible(false);

        recruitmentWindow[0].setPosition(stage.getWidth() - recruitmentWindow[0].getWidth() / 2 + sliderMover[0].getWidth() / 2, recruitmentWindow[1].getHeight() * 2);
        recruitmentWindow[1].setPosition(stage.getWidth() - recruitmentWindow[0].getWidth() / 2 + sliderMover[0].getWidth() / 2, recruitmentWindow[1].getHeight());
        recruitmentWindow[2].setPosition(stage.getWidth() - recruitmentWindow[0].getWidth() / 2 + sliderMover[0].getWidth() / 2, 0);
        unit[0].getImage().setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() / 4 - unit[0].getImage().getWidth() / 2,
                recruitmentWindow[0].getY() + recruitmentWindow[0].getHeight() / 2 - unit[0].getImage().getHeight() / 2);
        unitStatsImage[0].setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() - unitStatsImage[0].getWidth(),
                recruitmentWindow[0].getY() + recruitmentWindow[0].getHeight() - unitStatsImage[0].getHeight());
        unitStatsImage[1].setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() - unitStatsImage[1].getWidth(),
                unitStatsImage[0].getY() - unitStatsImage[1].getHeight());
        unitStatsImage[2].setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() - unitStatsImage[2].getWidth(),
                unitStatsImage[1].getY() - unitStatsImage[2].getHeight());
        unitStatsImage[3].setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() - unitStatsImage[3].getWidth(),
                unitStatsImage[2].getY() - unitStatsImage[3].getHeight());
        unitStatsImage[4].setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() - unitStatsImage[4].getWidth(),
                unitStatsImage[3].getY() - unitStatsImage[4].getHeight());
        unitStatsImage[5].setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() - unitStatsImage[5].getWidth(),
                unitStatsImage[4].getY() - unitStatsImage[5].getHeight());
        unitStatsImage[6].setPosition(recruitmentWindow[0].getX() + recruitmentWindow[0].getWidth() - unitStatsImage[6].getWidth(),
                unitStatsImage[5].getY() - unitStatsImage[6].getHeight());
        unitCost[0].setPosition(recruitmentWindow[1].getX() + unitCost[0].getWidth() / 2, recruitmentWindow[1].getY() + unitCost[0].getHeight() / 2 - unitStatsImage[0].getHeight() / 2);
        unitAmount[0].setPosition(unitCost[0].getX() + unitCost[0].getWidth() + imageOffset, recruitmentWindow[1].getY() + unitAmount[0].getHeight() / 2 - unitStatsImage[0].getHeight() / 2);
        unitAmount[1].setPosition(unitAmount[0].getX() + unitAmount[0].getWidth() + imageOffset, recruitmentWindow[1].getY() + unitAmount[1].getHeight() / 2 - unitStatsImage[0].getHeight() / 2);
        unitCost[1].setPosition(unitAmount[1].getX() + unitAmount[1].getWidth() + imageOffset, recruitmentWindow[1].getY() + unitCost[1].getHeight() / 2 - unitStatsImage[0].getHeight() / 2);
        sliderBar.setPosition(recruitmentWindow[1].getX() + (recruitmentWindow[1].getWidth() / 2 - sliderBar.getWidth() / 2), recruitmentWindow[1].getY() - sliderBar.getHeight());
        sliderMover[0].setPosition(sliderBar.getX() - sliderMover[0].getWidth(), sliderBar.getY());
        sliderMover[1].setPosition(sliderBar.getX() + sliderBar.getWidth(), sliderBar.getY());
        sliderMover[2].setPosition(sliderBar.getX() - sliderMover[2].getWidth() - sliderMover[2].getWidth(), sliderBar.getY());
        sliderMover[3].setPosition(sliderBar.getX() + sliderBar.getWidth() + sliderMover[3].getWidth(), sliderBar.getY());
        slider.setPosition(sliderBar.getX(), sliderBar.getY());
        accept.setPosition(recruitmentWindow[2].getX() + recruitmentWindow[2].getWidth() / 2 - accept.getWidth() - (imageOffset * 5), recruitmentWindow[2].getY());
        decline.setPosition(recruitmentWindow[2].getX() + recruitmentWindow[2].getWidth() / 2 + (imageOffset * 5), recruitmentWindow[2].getY());
    }

    private void setLabelsPosition()
    {
        unitCostLabel[0].setPosition(unitCost[0].getX() + unitCost[0].getWidth() / 2 - unitCostLabel[0].getWidth() / 2,
                unitCost[0].getY() + unitCost[0].getHeight() - unitCostLabel[0].getHeight() - imageOffset / 2);
        unitCostLabel[1].setPosition(unitCost[0].getX() + unitCost[0].getWidth() / 2 - unitCostLabel[1].getWidth() / 2,
                unitCost[0].getY() + unitCost[0].getHeight() - unitCostLabel[0].getHeight() * 3 - imageOffset * 3 / 2);
        unitAmountLabel[0].setPosition(unitAmount[0].getX() + unitAmount[0].getWidth() / 2 - unitAmountLabel[0].getWidth() / 2,
                unitAmount[0].getY() + unitAmount[0].getHeight() - unitAmountLabel[0].getHeight() - imageOffset / 2);
        unitAmountLabel[1].setPosition(unitAmount[1].getX() + unitAmount[1].getWidth() / 2 - unitAmountLabel[1].getWidth() / 2,
                unitAmount[1].getY() + unitAmount[1].getHeight() - unitAmountLabel[1].getHeight() - imageOffset / 2);
        unitAmountLabel[2].setPosition(unitAmount[0].getX() + unitAmount[0].getWidth() / 2 - unitAmountLabel[2].getWidth() / 2,
                unitAmount[0].getY() + unitAmountLabel[2].getHeight());
        unitAmountLabel[3].setPosition(unitAmount[1].getX() + unitAmount[1].getWidth() / 2 - unitAmountLabel[3].getWidth() / 2,
                unitAmount[1].getY() + unitAmountLabel[3].getHeight());
        unitCostLabel[2].setPosition(unitCost[1].getX() + unitCost[1].getWidth() / 2 - unitCostLabel[2].getWidth() / 2,
                unitCost[1].getY() + unitCost[1].getHeight() - unitCostLabel[0].getHeight() - imageOffset / 2);
        unitCostLabel[3].setPosition(unitCost[1].getX() + unitCost[1].getWidth() / 2 - unitCostLabel[3].getWidth() / 2,
                unitCost[1].getY() + unitCost[1].getHeight() - unitCostLabel[0].getHeight() * 3 - imageOffset * 3 / 2);
        unitStatsLabel[0].setPosition(unitStatsImage[0].getX() + imageOffset, unitStatsImage[0].getY() + unitStatsImage[0].getHeight() / 2 - unitStatsLabel[0].getHeight() / 2);
        unitStatsLabel[1].setPosition(unitStatsImage[0].getX() + unitStatsImage[0].getWidth() - unitStatsLabel[1].getWidth() - imageOffset,
                unitStatsImage[0].getY() + unitStatsImage[0].getHeight() / 2 - unitStatsLabel[0].getHeight() / 2);
        unitStatsLabel[2].setPosition(unitStatsImage[1].getX() + imageOffset, unitStatsImage[1].getY() + unitStatsImage[1].getHeight() / 2 - unitStatsLabel[2].getHeight() / 2);
        unitStatsLabel[3].setPosition(unitStatsImage[1].getX() + unitStatsImage[1].getWidth() - unitStatsLabel[3].getWidth() - imageOffset,
                unitStatsImage[1].getY() + unitStatsImage[1].getHeight() / 2 - unitStatsLabel[3].getHeight() / 2);
        unitStatsLabel[4].setPosition(unitStatsImage[2].getX() + imageOffset, unitStatsImage[2].getY() + unitStatsImage[2].getHeight() / 2 - unitStatsLabel[4].getHeight() / 2);
        unitStatsLabel[5].setPosition(unitStatsImage[2].getX() + unitStatsImage[2].getWidth() - unitStatsLabel[5].getWidth() - imageOffset,
                unitStatsImage[2].getY() + unitStatsImage[2].getHeight() / 2 - unitStatsLabel[5].getHeight() / 2);
        unitStatsLabel[6].setPosition(unitStatsImage[3].getX() + imageOffset, unitStatsImage[3].getY() + unitStatsImage[3].getHeight() / 2 - unitStatsLabel[6].getHeight() / 2);
        unitStatsLabel[7].setPosition(unitStatsImage[3].getX() + unitStatsImage[3].getWidth() - unitStatsLabel[7].getWidth() - imageOffset,
                unitStatsImage[3].getY() + unitStatsImage[3].getHeight() / 2 - unitStatsLabel[7].getHeight() / 2);
        unitStatsLabel[8].setPosition(unitStatsImage[4].getX() + imageOffset, unitStatsImage[4].getY() + unitStatsImage[4].getHeight() / 2 - unitStatsLabel[8].getHeight() / 2);
        unitStatsLabel[9].setPosition(unitStatsImage[4].getX() + unitStatsImage[4].getWidth() - unitStatsLabel[9].getWidth() - imageOffset,
                unitStatsImage[4].getY() + unitStatsImage[4].getHeight() / 2 - unitStatsLabel[9].getHeight() / 2);
        unitStatsLabel[10].setPosition(unitStatsImage[5].getX() + imageOffset, unitStatsImage[5].getY() + unitStatsImage[5].getHeight() / 2 - unitStatsLabel[10].getHeight() / 2);
        unitStatsLabel[11].setPosition(unitStatsImage[5].getX() + unitStatsImage[5].getWidth() - unitStatsLabel[11].getWidth() - imageOffset,
                unitStatsImage[5].getY() + unitStatsImage[5].getHeight() / 2 - unitStatsLabel[11].getHeight() / 2);
        unitStatsLabel[12].setPosition(unitStatsImage[6].getX() + imageOffset, unitStatsImage[6].getY() + unitStatsImage[6].getHeight() / 2 - unitStatsLabel[12].getHeight() / 2);
        unitStatsLabel[13].setPosition(unitStatsImage[6].getX() + unitStatsImage[6].getWidth() - unitStatsLabel[13].getWidth() - imageOffset,
                unitStatsImage[6].getY() + unitStatsImage[6].getHeight() / 2 - unitStatsLabel[13].getHeight() / 2);

        unitCostLabel[0].setAlignment(Align.center);
        unitCostLabel[1].setAlignment(Align.center);
        unitAmountLabel[0].setAlignment(Align.center);
        unitAmountLabel[1].setAlignment(Align.center);
        unitAmountLabel[2].setAlignment(Align.center);
        unitAmountLabel[3].setAlignment(Align.center);
        unitCostLabel[2].setAlignment(Align.center);
        unitCostLabel[3].setAlignment(Align.center);
        unitStatsLabel[1].setAlignment(Align.right);
        unitStatsLabel[3].setAlignment(Align.right);
        unitStatsLabel[5].setAlignment(Align.right);
        unitStatsLabel[7].setAlignment(Align.right);
        unitStatsLabel[9].setAlignment(Align.right);
        unitStatsLabel[11].setAlignment(Align.right);
        unitStatsLabel[12].setAlignment(Align.right);
        unitStatsLabel[13].setAlignment(Align.right);

        unitAmountLabel[2].setFontScale(2);
        unitAmountLabel[3].setFontScale(2);
    }

    private void addGroupElements(Group group)
    {
        //adding images to group
        group.addActor(recruitmentWindow[0]);
        group.addActor(recruitmentWindow[1]);
        group.addActor(recruitmentWindow[2]);
        group.addActor(unit[0].getImage());
        group.addActor(unit[1].getImage());
        group.addActor(unitStatsImage[0]);
        group.addActor(unitStatsImage[1]);
        group.addActor(unitStatsImage[2]);
        group.addActor(unitStatsImage[3]);
        group.addActor(unitStatsImage[4]);
        group.addActor(unitStatsImage[5]);
        group.addActor(unitStatsImage[6]);
        group.addActor(unitCost[0]);
        group.addActor(unitCost[1]);
        group.addActor(unitAmount[0]);
        group.addActor(unitAmount[1]);
        group.addActor(sliderBar);
        group.addActor(sliderMover[0]);
        group.addActor(sliderMover[1]);
        group.addActor(sliderMover[2]);
        group.addActor(sliderMover[3]);
        group.addActor(slider);
        group.addActor(accept);
        group.addActor(decline);
        //adding labels to group
        group.addActor(unitCostLabel[0]);
        group.addActor(unitCostLabel[1]);
        group.addActor(unitAmountLabel[0]);
        group.addActor(unitAmountLabel[1]);
        group.addActor(unitAmountLabel[2]);
        group.addActor(unitAmountLabel[3]);
        group.addActor(unitCostLabel[2]);
        group.addActor(unitCostLabel[3]);
        group.addActor(unitStatsLabel[0]);
        group.addActor(unitStatsLabel[1]);
        group.addActor(unitStatsLabel[2]);
        group.addActor(unitStatsLabel[3]);
        group.addActor(unitStatsLabel[4]);
        group.addActor(unitStatsLabel[5]);
        group.addActor(unitStatsLabel[6]);
        group.addActor(unitStatsLabel[7]);
        group.addActor(unitStatsLabel[8]);
        group.addActor(unitStatsLabel[9]);
        group.addActor(unitStatsLabel[10]);
        group.addActor(unitStatsLabel[11]);
        group.addActor(unitStatsLabel[12]);
        group.addActor(unitStatsLabel[13]);
    }

    private void updateSlidersMove()
    {
        if(unitsAvailable > 0)
        {
            sliderMove = (sliderBar.getWidth() - slider.getWidth()) / unitsAvailable;
        }
        else
        {
            sliderMove = 0;
        }
    }

    private void noEmptyCastleSlot()
    {
        Action action = new Action()
        {
            @Override
            public boolean act (float delta)
            {
                informationDialog.setVisible(false);
                return false;
            }
        };
        informationDialog.update("Sorry", "There is no empty slot in castle to recruit this unit.", action);
        informationDialog.getDeclineImage().setVisible(false);
        informationDialog.setVisible(true);
    }

    private void noEnoughGold()
    {
        Action action = new Action()
        {
            @Override
            public boolean act (float delta)
            {
                informationDialog.setVisible(false);
                return false;
            }
        };
        informationDialog.update("Sorry", "You don't have enough gold to buy these units.", action);
        informationDialog.getDeclineImage().setVisible(false);
        informationDialog.setVisible(true);
    }

    public void upgrade()
    {
        upgradedBuilding = true;
        unit[0].getImage().setPosition(recruitmentWindow[0].getX() + unit[0].getImage().getWidth() / 2 + imageOffset,
                recruitmentWindow[0].getY() + recruitmentWindow[0].getHeight() / 2 - unit[0].getImage().getHeight() / 2);
        unit[1].getImage().setPosition(recruitmentWindow[0].getX() + unit[1].getImage().getWidth() * 2 + imageOffset,
                recruitmentWindow[0].getY() + recruitmentWindow[0].getHeight() / 2 - unit[1].getImage().getHeight() / 2);

        unit[1].getImage().setVisible(true);

        currentName = name;

        update();
    }

    public void update()
    {
        if(upgradedUnit)
        {
            unitStatsLabel[1].setText(String.valueOf(unit[1].getAttack()));
            unitStatsLabel[3].setText(String.valueOf(unit[1].getDefence()));
            unitStatsLabel[5].setText(String.valueOf(unit[1].getMinDamage()) + "-" +String.valueOf(unit[1].getMaxDamage()));
            if(unit[1].getAmmo() > 0)
            {
                unitStatsLabel[7].setText(String.valueOf(unit[1].getAmmo()));
            }
            else
            {
                unitStatsLabel[7].setText("-");
            }
            unitStatsLabel[9].setText(String.valueOf(unit[1].getHealth()));
            unitStatsLabel[11].setText(String.valueOf(unit[1].getSpeed()));
            unitStatsLabel[13].setText(String.valueOf(unit[1].getGrowth()));
            unitPrice = unit[1].getCost();
        }
        else
        {
            unitStatsLabel[1].setText(String.valueOf(unit[0].getAttack()));
            unitStatsLabel[3].setText(String.valueOf(unit[0].getDefence()));
            unitStatsLabel[5].setText(String.valueOf(unit[0].getMinDamage()) + "-" +String.valueOf(unit[0].getMaxDamage()));
            if(unit[1].getAmmo() > 0)
            {
                unitStatsLabel[7].setText(String.valueOf(unit[1].getAmmo()));
            }
            else
            {
                unitStatsLabel[7].setText("-");
            }
            unitStatsLabel[9].setText(String.valueOf(unit[0].getHealth()));
            unitStatsLabel[11].setText(String.valueOf(unit[0].getSpeed()));
            unitStatsLabel[13].setText(String.valueOf(unit[0].getGrowth()));
            unitPrice = unit[0].getCost();
        }

        currentPrice = unitPrice * currentUnits;

        unitCostLabel[3].setText(String.valueOf(currentPrice));
        unitAmountLabel[2].setText(String.valueOf(unitsAvailable));
        unitAmountLabel[3].setText(String.valueOf(currentUnits));

        updateSlidersMove();
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
            drawGoldLine();
        }
    }

    private void drawGoldLine()
    {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        if(upgradedUnit)
        {
            shapeRenderer.rect(unit[1].getImage().getX() - unit[1].getImage().getWidth() * 9 / 2 - 16, unit[1].getImage().getY() + 38, unit[1].getImage().getWidth(), unit[1].getImage().getHeight());
        }
        else
        {
            shapeRenderer.rect(unit[0].getImage().getX() - unit[0].getImage().getWidth() * 9 / 2 - 16, unit[0].getImage().getY() + 38, unit[0].getImage().getWidth(), unit[0].getImage().getHeight());
        }
        shapeRenderer.end();
    }

    public void dispose()
    {
        unit[0].dispose();
        unit[1].dispose();
        stage.dispose();
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;

        slider.setPosition(sliderBar.getX(), sliderBar.getY());
        currentPrice = 0;
        currentUnits = 0;
        unitAmountLabel[3].setText("0");
        unitCostLabel[3].setText("0");
    }

    public void setUnitsAvailable (int unitsAvailable)
    {
        this.unitsAvailable = unitsAvailable;
    }

    public boolean isCreated()
    {
        return created;
    }

    public boolean isVisible()
    {
        return  visible;
    }

    public boolean isUpgraded()
    {
        return upgradedBuilding;
    }

    public Image getBuildingImage()
    {
        return buildingImage;
    }

    public Stage getStage()
    {
        return stage;
    }

    public Unit[] getUnit()
    {
        return unit;
    }

    public InformationDialog getInformationDialog()
    {
        return informationDialog;
    }

    public String getDescription()
    {
        return description;
    }

    String getCurrentName()
    {
        return currentName;
    }

    String[] getRequirements()
    {
        return requirements;
    }

    public int[] getCost()
    {
        return cost;
    }
}