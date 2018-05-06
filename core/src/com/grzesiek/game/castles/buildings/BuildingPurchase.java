package com.grzesiek.game.castles.buildings;

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
import com.grzesiek.game.castles.Castle;
import com.grzesiek.game.castles.InformationDialog;
import com.grzesiek.game.player.Player;

import java.util.Arrays;

/**
 * Created by Grzesiek on 2017-10-09.
 */

public class BuildingPurchase
{
    private Viewport viewport;

    private Stage stage;
    private Table table;
    private Group group;

    private InformationDialog informationDialog;

    private Image background;
    private Image buildingIcon;
    private Image costInfo;
    private Image accept;
    private Image decline;
    private Image[] buildingSpecifics;
    private Image[] stock;

    private Label buildingNameLabel;
    private Label[] infoLabel;
    private Label[] costLabel;

    private boolean visible;
    private boolean upgrade;

    private int buildingIndex;
    private int[] cost;

    public BuildingPurchase(Viewport port, final SpriteBatch batch, final Castle castle, final Player player)
    {
        this.viewport = port;
        viewport.update(350, 405);

        stage = new Stage(viewport, batch);
        table = new Table();

        informationDialog = new InformationDialog(viewport, batch);

        visible = false;
        upgrade = false;

        buildingIndex = 0;
        cost = new int[5];
        cost[0] = 0;
        cost[1] = 0;
        cost[2] = 0;
        cost[3] = 0;
        cost[4] = 0;

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();

        group = new Group();
        addGroupElements();

        table.center().center();
        table.setFillParent(true);
        table.add(group).size(background.getWidth(), background.getHeight());

        stage.addActor(table);

        accept.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(player.getWood() >= cost[0] && player.getStone() >= cost[1] && player.getCrystal() >= cost[2] && player.getDeboil() >= cost[3] && player.getGold() >= cost[4])
                {
                    if(castle.getUnitRecruiter()[buildingIndex].getRequirements() != null)
                    {
                        for(int i = 0; i < 2; i++)
                        {
                            for(int j = 0; j < castle.getUnitRecruiter()[buildingIndex].getRequirements().length; j++)
                            {
                                if(castle.getUnitRecruiter()[i].getCurrentName().equals(castle.getUnitRecruiter()[buildingIndex].getRequirements()[j]))
                                {
                                    if(! castle.getUnitRecruiter()[i].isCreated())
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
                                        informationDialog.update("Sorry", "You haven't built all required buildings yet", action);
                                        informationDialog.setVisible(true);
                                        informationDialog.getDeclineImage().setVisible(false);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    if(!upgrade)
                    {
                        castle.createUnitRecruiter(player, buildingIndex);
                    }
                    else
                    {
                        castle.getUnitRecruiter()[buildingIndex].upgrade();
                        castle.getMiniImage()[buildingIndex].setDrawable(new SpriteDrawable(new Sprite(castle.getUnitRecruiter()[buildingIndex].getUnit()[1].getMiniTexture())));
                        castle.getHall().setVisible(false);
                    }
                    player.getCastle().setCanBuild(false);
                    visible = false;
                    player.adjustWood(-cost[0]);
                    player.adjustStone(-cost[1]);
                    player.adjustCrystal(-cost[2]);
                    player.adjustDeboil(-cost[3]);
                    player.adjustGold(- cost[4]);
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
        buildingSpecifics = new Image[2];
        stock = new Image[5];

        background = new Image(new Texture("castles/buildingPurchase.png"));
        buildingIcon = new Image(new Texture("castles/castle_1/building_1_icon.png"));
        buildingSpecifics[0] = new Image(new Texture("castles/buildingSpecifics.png"));
        buildingSpecifics[1] = new Image(new Texture("castles/buildingSpecifics.png"));
        costInfo = new Image(new Texture("castles/costInfo.png"));
        stock[0] = new Image(new Texture("HUD/stock1.png"));
        stock[1] = new Image(new Texture("HUD/stock2.png"));
        stock[2] = new Image(new Texture("HUD/stock3.png"));
        stock[3] = new Image(new Texture("HUD/stock4.png"));
        stock[4] = new Image(new Texture("HUD/stock5.png"));
        accept = new Image(new Texture("castles/buy.png"));
        decline = new Image(new Texture("castles/decline.png"));
    }

    private void defineLabels()
    {
        infoLabel = new Label[2];
        costLabel = new Label[5];

        buildingNameLabel = new Label("Build: Skeleton's Crypt", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        infoLabel[0] = new Label("Building allows it's owner to buy skeletons", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        infoLabel[1] = new Label("All conditions to buy this building have been met.", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        costLabel[0] = new Label(String.valueOf(this.cost[0]), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        costLabel[1] = new Label(String.valueOf(this.cost[1]), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        costLabel[2] = new Label(String.valueOf(this.cost[2]), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        costLabel[3] = new Label(String.valueOf(this.cost[3]), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        costLabel[4] = new Label(String.valueOf(this.cost[4]), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        buildingNameLabel.setWidth(buildingIcon.getWidth());
        infoLabel[0].setWidth(buildingSpecifics[0].getWidth());
        infoLabel[1].setWidth(buildingSpecifics[1].getWidth());

        buildingNameLabel.setAlignment(Align.center);
        infoLabel[0].setAlignment(Align.center);
        infoLabel[1].setAlignment(Align.center);
    }

    private void setImagesPosition()
    {
        background.setSize(350, 405);
        costInfo.setSize(costInfo.getWidth(), costInfo.getHeight() / 2);

        float stockWidth = (costInfo.getWidth() - 20) / 5;

        buildingIcon.setPosition(background.getX() + background.getWidth() / 2 - buildingIcon.getWidth() / 2, background.getY() + background.getHeight() - buildingIcon.getHeight() * 3 / 2);
        buildingSpecifics[0].setPosition(background.getX() + background.getWidth() / 2 - buildingSpecifics[0].getWidth() / 2, buildingIcon.getY() - buildingSpecifics[0].getHeight() - 7);
        buildingSpecifics[1].setPosition(background.getX() + background.getWidth() / 2 - buildingSpecifics[1].getWidth() / 2, buildingSpecifics[0].getY() - buildingSpecifics[1].getHeight() - 7);
        costInfo.setPosition(background.getX() + background.getWidth() / 2 - costInfo.getWidth() / 2, buildingSpecifics[1].getY() - costInfo.getHeight() - 7);
        stock[0].setPosition(costInfo.getX() + stockWidth / 2, costInfo.getY() + costInfo.getHeight() / 2 - stock[0].getHeight() / 2 + 10);
        stock[1].setPosition(costInfo.getX() + stockWidth * 3 / 2, stock[0].getY());
        stock[2].setPosition(costInfo.getX() + stockWidth * 5 / 2, stock[0].getY());
        stock[3].setPosition(costInfo.getX() + stockWidth * 7 / 2, stock[0].getY());
        stock[4].setPosition(costInfo.getX() + stockWidth * 9 / 2, stock[0].getY());
        accept.setPosition(background.getX() + background.getWidth() / 2 - accept.getWidth() * 2, costInfo.getY() - accept.getHeight() - 7);
        decline.setPosition(background.getX() + background.getWidth() / 2 + decline.getWidth(), costInfo.getY() - decline.getHeight() - 7);
    }

    private void setLabelsPosition()
    {
        buildingNameLabel.setPosition(buildingIcon.getX() + buildingIcon.getWidth() / 2 - buildingNameLabel.getWidth() / 2, buildingIcon.getY() + buildingIcon.getHeight());
        infoLabel[0].setPosition(buildingSpecifics[0].getX() + buildingSpecifics[0].getWidth() / 2 - infoLabel[0].getWidth() / 2,
                buildingSpecifics[0].getY() + buildingSpecifics[0].getHeight() / 2 - infoLabel[0].getHeight() / 2);
        infoLabel[1].setPosition(buildingSpecifics[1].getX() + buildingSpecifics[1].getWidth() / 2 - infoLabel[1].getWidth() / 2,
                buildingSpecifics[1].getY() + buildingSpecifics[1].getHeight() / 2 - infoLabel[1].getHeight() / 2);
        costLabel[0].setPosition(stock[0].getX() + stock[0].getWidth() / 2 - costLabel[0].getWidth() / 2, stock[0].getY() - costLabel[0].getHeight());
        costLabel[1].setPosition(stock[1].getX() + stock[1].getWidth() / 2 - costLabel[1].getWidth() / 2, stock[1].getY() - costLabel[1].getHeight());
        costLabel[2].setPosition(stock[2].getX() + stock[2].getWidth() / 2 - costLabel[2].getWidth() / 2, stock[2].getY() - costLabel[2].getHeight());
        costLabel[3].setPosition(stock[3].getX() + stock[3].getWidth() / 2 - costLabel[3].getWidth() / 2, stock[3].getY() - costLabel[3].getHeight());
        costLabel[4].setPosition(stock[4].getX() + stock[4].getWidth() / 2 - costLabel[4].getWidth() / 2, stock[4].getY() - costLabel[4].getHeight());

        infoLabel[0].setWrap(true);
        infoLabel[1].setWrap(true);
        buildingNameLabel.setAlignment(Align.center);
        infoLabel[0].setAlignment(Align.center);
        infoLabel[1].setAlignment(Align.center);
        costLabel[0].setAlignment(Align.center);
        costLabel[1].setAlignment(Align.center);
        costLabel[2].setAlignment(Align.center);
        costLabel[3].setAlignment(Align.center);
        costLabel[4].setAlignment(Align.center);
    }

    private void addGroupElements()
    {
        //adding images to group
        group.addActor(background);
        group.addActor(buildingIcon);
        group.addActor(buildingSpecifics[0]);
        group.addActor(buildingSpecifics[1]);
        group.addActor(costInfo);
        group.addActor(stock[0]);
        group.addActor(stock[1]);
        group.addActor(stock[2]);
        group.addActor(stock[3]);
        group.addActor(stock[4]);
        group.addActor(accept);
        group.addActor(decline);
        //adding labels to group
        group.addActor(buildingNameLabel);
        group.addActor(infoLabel[0]);
        group.addActor(infoLabel[1]);
        group.addActor(costLabel[0]);
        group.addActor(costLabel[1]);
        group.addActor(costLabel[2]);
        group.addActor(costLabel[3]);
        group.addActor(costLabel[4]);
    }

    public void update(int buildingIndex, String description, String name, String[] requirements, int[] cost1, Texture buildingIconTexture, boolean upgrade)
    {
        this.buildingIndex = buildingIndex;

        cost[0] = cost1[0];
        cost[1] = cost1[1];
        cost[2] = cost1[2];
        cost[3] = cost1[3];
        cost[4] = cost1[4];

        this.upgrade = upgrade;

        buildingIcon.setDrawable(new SpriteDrawable(new Sprite(buildingIconTexture)));

        buildingNameLabel.setText("Build " +name);
        infoLabel[0].setText(description);
        if(requirements != null)
        {
            infoLabel[1].setText("To build " + name + " first you have to buy: " + Arrays.deepToString(requirements));
        }
        else
        {
            infoLabel[1].setText("All conditions to build this building have been met");
        }
        costLabel[0].setText(String.valueOf(cost[0]));
        costLabel[1].setText(String.valueOf(cost[1]));
        costLabel[2].setText(String.valueOf(cost[2]));
        costLabel[3].setText(String.valueOf(cost[3]));
        costLabel[4].setText(String.valueOf(cost[4]));
    }

    void draw()
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
        informationDialog.dispose();
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
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
