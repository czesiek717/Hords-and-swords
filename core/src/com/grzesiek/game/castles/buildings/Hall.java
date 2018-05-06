package com.grzesiek.game.castles.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.grzesiek.game.castles.Castle;
import com.grzesiek.game.player.Player;

/**
 * Created by Grzesiek on 2017-10-09.
 */

public class Hall
{
    private Viewport viewport;

    private Stage stage;
    private Table table;

    private BuildingPurchase buildingPurchase;

    private Texture greenLabelTexture;
    private Texture redLabelTexture;
    private Texture goldLabelTexture;
    private Texture[] buildingTexture;

    private Image background;
    private Image buildingImage;
    private Image[] building;
    private Image[] buildingBottom;
    private Image accept;

    private Label[] buildingName;

    private boolean visible;

    public Hall(Viewport port, SpriteBatch batch, final Castle castle, final Player player)
    {
        this.viewport = port;
        viewport.update(550, 405);

        stage = new Stage(viewport, batch);
        table = new Table();
        visible = false;

        buildingPurchase = new BuildingPurchase(port, batch, castle, player);

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();

        Group group = new Group();
        addGroupElements(group);

        table.center().center();
        table.setFillParent(true);
        table.add(group).size(background.getWidth(), background.getHeight());

        stage.addActor(table);

        building[0].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(castle.getCanBuild())
                {
                    if(!castle.getUnitRecruiter()[0].isCreated())
                    {
                        buildingPurchase.update(0, castle.getUnitRecruiter()[0].getDescription(), castle.getUnitRecruiter()[0].getCurrentName(),
                                castle.getUnitRecruiter()[0].getRequirements(), castle.getUnitRecruiter()[0].getCost(), buildingTexture[0], false);
                        buildingPurchase.setVisible(true);
                    }
                    else
                    {
                        if(!castle.getUnitRecruiter()[0].isUpgraded())
                        {
                            buildingPurchase.update(0, castle.getUnitRecruiter()[0].getDescription(), castle.getUnitRecruiter()[0].getCurrentName(),
                                    castle.getUnitRecruiter()[0].getRequirements(), castle.getUnitRecruiter()[0].getCost(), buildingTexture[0], true);
                            buildingPurchase.setVisible(true);
                        }
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

        building[1].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(castle.getCanBuild())
                {
                    if(!castle.getUnitRecruiter()[1].isCreated())
                    {
                        buildingPurchase.update(1, castle.getUnitRecruiter()[1].getDescription(), castle.getUnitRecruiter()[1].getCurrentName(),
                                castle.getUnitRecruiter()[1].getRequirements(), castle.getUnitRecruiter()[1].getCost(), buildingTexture[1], false);
                        buildingPurchase.setVisible(true);
                    }
                    else
                    {
                        if(!castle.getUnitRecruiter()[1].isUpgraded())
                        {
                            buildingPurchase.update(1, castle.getUnitRecruiter()[1].getDescription(), castle.getUnitRecruiter()[1].getCurrentName(),
                                    castle.getUnitRecruiter()[1].getRequirements(), castle.getUnitRecruiter()[1].getCost(), buildingTexture[1], true);
                            buildingPurchase.setVisible(true);
                        }
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

        accept.addListener(new InputListener()
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
        buildingTexture = new Texture[2];

        greenLabelTexture = new Texture("castles/label_green.png");
        redLabelTexture = new Texture("castles/label_red.png");
        goldLabelTexture = new Texture("castles/label_gold.png");
        buildingTexture[0] = new Texture("castles/castle_1/building_1_icon.png");
        buildingTexture[1] = new Texture("castles/castle_1/building_2_icon.png");

        building = new Image[2];
        buildingBottom = new Image[2];

        background = new Image(new Texture("castles/hall.png"));
        buildingImage = new Image(new Texture("castles/castle_1/building_4.png"));
        building[0] = new Image(buildingTexture[0]);
        building[1] = new Image(buildingTexture[1]);
        buildingBottom[0] = new Image(greenLabelTexture);
        buildingBottom[1] = new Image(greenLabelTexture);
        accept = new Image(new Texture("castles/accept.png"));
    }

    private void defineLabels()
    {
        buildingName = new Label[2];

        buildingName[0] = new Label("xdd", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        buildingName[1] = new Label("xdd", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        buildingName[0].setWidth(buildingBottom[0].getWidth());
        buildingName[1].setWidth(buildingBottom[1].getWidth());
    }

    private void setImagesPosition()
    {
        background.setSize(550, 405);
        accept.setSize(50, 28);

        building[0].setPosition(background.getX() + 15, background.getY() + background.getHeight() - building[0].getHeight() - 25);
        building[1].setPosition(building[0].getX() + building[0].getWidth() + 15, building[0].getY());
        buildingBottom[0].setPosition(building[0].getX(), building[0].getY() - buildingBottom[0].getHeight());
        buildingBottom[1].setPosition(building[1].getX(), building[1].getY() - buildingBottom[1].getHeight());
        accept.setPosition(background.getWidth() - accept.getWidth() - 6, background.getY() + 3);
    }

    private void setLabelsPosition()
    {
        buildingName[0].setPosition(buildingBottom[0].getX() + buildingBottom[0].getWidth() / 2 - buildingName[0].getWidth() / 2,
                buildingBottom[0].getY() + buildingBottom[0].getHeight() / 2 - buildingName[0].getHeight() / 2);
        buildingName[1].setPosition(buildingBottom[1].getX() + buildingBottom[1].getWidth() / 2 - buildingName[1].getWidth() / 2,
                buildingBottom[1].getY() + buildingBottom[1].getHeight() / 2 - buildingName[1].getHeight() / 2);

        buildingName[0].setAlignment(Align.center);
        buildingName[1].setAlignment(Align.center);
    }

    private void addGroupElements(Group group)
    {
        //adding images to group
        group.addActor(background);
        group.addActor(building[0]);
        group.addActor(building[1]);
        group.addActor(buildingBottom[0]);
        group.addActor(buildingBottom[1]);
        group.addActor(accept);
        //adding labels to group
        group.addActor(buildingName[0]);
        group.addActor(buildingName[1]);
    }

    public void update(Castle castle)
    {
        buildingName[0].setText(castle.getUnitRecruiter()[0].getCurrentName());
        buildingName[1].setText(castle.getUnitRecruiter()[1].getCurrentName());
        for(int i = 0; i < castle.getUnitRecruiter().length; i++)
        {
            if(castle.getUnitRecruiter()[i].isUpgraded())
            {
                buildingBottom[i].setDrawable(new SpriteDrawable(new Sprite(goldLabelTexture)));
                continue;
            }

            if(!castle.getCanBuild())
            {
                buildingBottom[i].setDrawable(new SpriteDrawable(new Sprite(redLabelTexture)));
            }
            else
            {
                buildingBottom[i].setDrawable(new SpriteDrawable(new Sprite(greenLabelTexture)));
            }
        }
    }

    public void draw()
    {
        stage.draw();
        if(buildingPurchase.isVisible())
        {
            buildingPurchase.draw();
        }
        else
        {
            Gdx.input.setInputProcessor(stage);
        }
    }

    public void dispose()
    {
        stage.dispose();
        goldLabelTexture.dispose();
        greenLabelTexture.dispose();
        redLabelTexture.dispose();
        for(int i = 0; i < buildingTexture.length; i++)
        {
            buildingTexture[i].dispose();
        }
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public BuildingPurchase getBuildingPurchase()
    {
        return buildingPurchase;
    }

    public Image getBuildingImage()
    {
        return buildingImage;
    }

    public Stage getStage()
    {
        return stage;
    }
}
