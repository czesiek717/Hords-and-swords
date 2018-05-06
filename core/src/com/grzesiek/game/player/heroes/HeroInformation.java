package com.grzesiek.game.player.heroes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.castles.SplitUnits;
import com.grzesiek.game.castles.UnitInfo;
import com.grzesiek.game.castles.UnitSlot;
import com.grzesiek.game.player.Player;

/**
 * Created by Grzesiek on 2017-10-30.
 */

public class HeroInformation extends DragListener
{
    private Viewport viewport;
    private OrthographicCamera cam;

    private Stage stage;
    private Table table;

    private UnitSlot[] unitSlot;

    private SplitUnits splitUnits;
    private UnitInfo unitInfo;

    private Image backgroundImage;
    private Image heroIconImage;
    private Image heroNameImage;
    private Image acceptImage;

    private Label heroNameLabel;
    private Label heroLevelLabel;

    private boolean visible;
    private boolean unitTouched;

    private int slotNumber;
    private int previousSlotNumber;

    public HeroInformation (SpriteBatch batch, final Player player)
    {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, batch);
        table = new Table();

        splitUnits = new SplitUnits(viewport, batch, this);
        unitInfo = new UnitInfo(viewport, batch, player);

        visible = false;
        unitTouched = false;

        slotNumber = 0;
        previousSlotNumber = 0;

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();
        defineSlots();
        setSlotsPosition();

        Group group = new Group();
        addGroupElements(group);

        table.center().center();
        table.setFillParent(true);
        table.add(group).size(backgroundImage.getWidth(), backgroundImage.getHeight());

        stage.addActor(table);

        splitUnits.getSplitIcon().addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(unitTouched)
                {
                    if(unitSlot[previousSlotNumber].getUnit().getQuantity() > 1)
                    {
                        splitUnits.setClicked(true);
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

        acceptImage.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                visible = false;
                for(int i = 0; i < unitSlot.length; i++)
                {
                    if(unitSlot[i].getUnit() != null)
                    {
                        if(player.getHero().getUnitSlot()[i].getUnit() == null)
                        {
                            player.getHero().getUnitSlot()[i].addUnit(unitSlot[i].getUnit(), unitSlot[i].getUnit().getQuantity());
                        }
                        else if(unitSlot[i].getUnit().getName() == player.getHero().getUnitSlot()[i].getUnit().getName())
                        {
                            player.getHero().getUnitSlot()[i].setUnitQuantity(unitSlot[i].getUnit().getQuantity());
                        }
                        else
                        {
                            player.getHero().getUnitSlot()[i].removeUnit();
                            player.getHero().getUnitSlot()[i].addUnit(unitSlot[i].getUnit(), unitSlot[i].getUnit().getQuantity());
                        }
                    }
                    else
                    {
                        if(player.getHero().getUnitSlot()[i].getUnit() != null)
                        {
                            player.getHero().getUnitSlot()[i].removeUnit();
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
    }

    private void defineImages ()
    {
        backgroundImage = new Image(new Texture("playerInformation/background.png"));
        heroIconImage = new Image(new Texture("players/player_1/player_icon.png"));
        heroNameImage = new Image(new Texture("playerInformation/heroName.png"));
        acceptImage = new Image(new Texture("castles/accept.png"));
    }

    private void defineLabels ()
    {
        heroNameLabel = new Label("xdd", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        heroLevelLabel = new Label("xdd", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        heroNameLabel.setWidth(heroNameImage.getWidth());
        heroLevelLabel.setWidth(heroNameImage.getWidth());

        heroNameLabel.setAlignment(Align.center);
        heroLevelLabel.setAlignment(Align.center);
    }

    private void setImagesPosition ()
    {
        backgroundImage.setSize(600, 440);
        heroIconImage.setSize(heroIconImage.getWidth(), heroIconImage.getHeight() - 5);
        heroNameImage.setSize(heroNameImage.getWidth(), heroNameImage.getHeight() - 5);
        acceptImage.setSize(acceptImage.getWidth() - 12, acceptImage.getHeight());
        splitUnits.getSplitIcon().setSize(splitUnits.getSplitIcon().getWidth() + 4, splitUnits.getSplitIcon().getHeight() - 3);

        heroIconImage.setPosition(backgroundImage.getX() + 14, backgroundImage.getY() + backgroundImage.getHeight() - heroIconImage.getHeight() - 12);
        heroNameImage.setPosition(heroIconImage.getX() + heroIconImage.getWidth() + 1, heroIconImage.getY());
        acceptImage.setPosition(backgroundImage.getX() + backgroundImage.getWidth() - acceptImage.getWidth() - 7, backgroundImage.getY() + 25);
        splitUnits.getSplitIcon().setPosition(acceptImage.getX() - splitUnits.getSplitIcon().getWidth() - 13, acceptImage.getY());
    }

    private void setLabelsPosition ()
    {
        heroNameLabel.setPosition(heroNameImage.getX() + heroNameImage.getWidth() / 2 - heroNameLabel.getWidth() / 2, heroNameImage.getY() + heroNameImage.getHeight() - heroNameLabel.getHeight());
        heroLevelLabel.setPosition(heroNameImage.getX() + heroNameImage.getWidth() / 2 - heroLevelLabel.getWidth() / 2, heroNameLabel.getY() - heroLevelLabel.getHeight());
    }

    private void defineSlots()
    {
        unitSlot = new UnitSlot[7];
        for(int i = 0; i < unitSlot.length; i++)
        {

            unitSlot[i] = new UnitSlot();
            unitSlot[i].getGroup().addListener(this);
        }
    }

    private void setSlotsPosition()
    {
        for(int i = 0; i < unitSlot.length; i++)
        {
            unitSlot[i].getImage().setSize(unitSlot[i].getGroup().getWidth() - 2, unitSlot[i].getGroup().getHeight() - 10);
            unitSlot[i].getGroup().setSize(unitSlot[i].getImage().getWidth(), unitSlot[i].getImage().getHeight());
            if(i == 0)
            {
                unitSlot[0].getGroup().setPosition(backgroundImage.getX() + 10, backgroundImage.getY() + 25);
            }
            if(i > 0)
            {
                unitSlot[i].getGroup().setPosition(unitSlot[i - 1].getGroup().getX() + unitSlot[i - 1].getGroup().getWidth() + 4, unitSlot[i - 1].getGroup().getY());
            }
            unitSlot[i].setBounds();
            unitSlot[i].setLabelsPosition();
            unitSlot[i].getGroup().setName(String.valueOf(i));
            unitSlot[i].setPositionCoordinates(unitSlot[i].getGroup().getX(), unitSlot[i].getGroup().getY());
        }
    }

    private void addGroupElements (Group group)
    {
        //adding images to group
        group.addActor(backgroundImage);
        group.addActor(heroIconImage);
        group.addActor(heroNameImage);
        group.addActor(acceptImage);
        group.addActor(splitUnits.getSplitIcon());
        //adding labels to group
        group.addActor(heroNameLabel);
        group.addActor(heroLevelLabel);
        //adding slots to group
        group.addActor(unitSlot[0].getGroup());
        group.addActor(unitSlot[1].getGroup());
        group.addActor(unitSlot[2].getGroup());
        group.addActor(unitSlot[3].getGroup());
        group.addActor(unitSlot[4].getGroup());
        group.addActor(unitSlot[5].getGroup());
        group.addActor(unitSlot[6].getGroup());
    }

    public void update(Hero hero)
    {
        heroIconImage.setDrawable(new SpriteDrawable(new Sprite(hero.getHeroIconTexture())));
        heroNameLabel.setText(hero.getName());
        heroLevelLabel.setText("Level " +String.valueOf(hero.getLevel()) +": idiot knight");
        for(int i = 0; i < unitSlot.length; i++)
        {
            unitSlot[i].removeUnit();
            if(hero.getUnitSlot()[i].getUnit() != null)
            {
                unitSlot[i].addUnit(hero.getUnitSlot()[i].getUnit(), hero.getUnitSlot()[i].getUnit().getQuantity());
            }
        }
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
    {
        slotNumber = manageSlotName(event.getTarget().getParent().getName());
        if(unitSlot[slotNumber].getUnit() != null)
        {
            if(! splitUnits.isClicked())
            {
                if(unitTouched)
                {
                    if(slotNumber == previousSlotNumber)
                    {
                        unitTouched = false;
                        if(unitSlot[slotNumber].getUnit().getID() % 2 == 1)
                        {
                            unitInfo.update(unitSlot[slotNumber], false);
                        }
                        else
                        {
                            unitInfo.update(unitSlot[slotNumber], false);
                        }
                        unitInfo.setVisible(true);
                    }
                }
                else
                {
                    unitTouched = true;
                }
            }
            else
            {
                if(slotNumber != previousSlotNumber)
                {
                    if(unitSlot[previousSlotNumber].getUnit().getName() == unitSlot[slotNumber].getUnit().getName())
                    {
                        splitUnits.setVisible(true);
                        splitUnits.update(this, unitSlot[previousSlotNumber].getUnit(), previousSlotNumber, slotNumber, true);
                    }
                }
            }
            splitUnits.setClicked(false);
            splitUnits.setJustClosed(false);
            unitInfo.setJustClosed(false);
        }
        else
        {
            if(splitUnits.isClicked())
            {
                splitUnits.setVisible(true);
                splitUnits.update(this, unitSlot[previousSlotNumber].getUnit(), previousSlotNumber, slotNumber, false);
            }
            unitTouched = false;
            splitUnits.setClicked(false);
            splitUnits.setJustClosed(false);
            unitInfo.setJustClosed(false);
        }
        previousSlotNumber = slotNumber;
        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void drag (InputEvent event, float x, float y, int pointer)
    {
        slotNumber = manageSlotName(event.getTarget().getParent().getName());
        if(!splitUnits.isJustClosed() && ! unitInfo.isJustClosed())
        {
            if(unitSlot[slotNumber].getUnit() != null)
            {
                unitTouched = false;
                unitSlot[slotNumber].getGroup().moveBy(x - unitSlot[slotNumber].getGroup().getWidth() / 2, y - unitSlot[slotNumber].getGroup().getHeight() / 2);
                unitSlot[slotNumber].getGroup().toFront();
            }
        }
        super.drag(event, x, y, pointer);
    }

    @Override
    public void dragStop (InputEvent event, float x, float y, int pointer)
    {
        int newSlot = -1;
        for(int i = 0; i < unitSlot.length; i++)
        {
            if(unitSlot[i].getBounds().contains(unitSlot[slotNumber].getGroup().getX() + unitSlot[slotNumber].getGroup().getWidth() / 2,
                    unitSlot[slotNumber].getGroup().getY() + unitSlot[slotNumber].getGroup().getHeight() / 2))
            {
                newSlot = i;
                if(slotNumber != newSlot)
                {
                    if(unitSlot[newSlot].getUnit() == null)
                    {
                        unitSlot[newSlot].addUnit(unitSlot[slotNumber].getUnit(), unitSlot[slotNumber].getUnit().getQuantity());
                        unitSlot[slotNumber].removeUnit();
                        unitSlot[slotNumber].restorePosition();
                        break;
                    }
                    else
                    {
                        if(unitSlot[newSlot].getUnit().getName().equals(unitSlot[slotNumber].getUnit().getName()))
                        {
                            unitSlot[newSlot].adjustUnitQuantity(unitSlot[slotNumber].getUnit().getQuantity());
                            unitSlot[slotNumber].removeUnit();
                            unitSlot[slotNumber].restorePosition();
                            break;
                        }
                        else
                        {
                            unitSlot[slotNumber].swapSlots(unitSlot[newSlot]);
                            break;

                        }
                    }
                }
                else
                {
                    unitSlot[slotNumber].restorePosition();
                }
            }
        }
        if(newSlot == -1)
        {
            unitSlot[slotNumber].restorePosition();
        }
        super.dragStop(event, x, y, pointer);
    }

    private int manageSlotName(String targetName)
    {
        int slotNumber = 0;
        for(int i = 0; i < unitSlot.length; i++)
        {
            if(targetName.contains(String.valueOf(i)))
            {
                slotNumber = i;
            }
        }
        return slotNumber;
    }

    public void draw()
    {
        stage.draw();
        if(unitInfo.isVisible())
        {
            unitInfo.draw();
        }
        else if(splitUnits.isVisible())
        {
            splitUnits.draw();
        }
        else
        {
            Gdx.input.setInputProcessor(stage);
            drawGoldLine(slotNumber);
        }
    }

    private void drawGoldLine(int slotNumber)
    {
        if(unitTouched)
        {
            if(unitSlot[slotNumber].getUnit() != null)
            {
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(cam.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GOLD);
                if(splitUnits.isClicked())
                {
                    for(int i = 0; i < unitSlot.length; i++)
                    {
                        if(unitSlot[i].getUnit() == null)
                        {
                            shapeRenderer.rect(unitSlot[i].getGroup().getX() + 100, unitSlot[i].getGroup().getY() + 20,
                                    unitSlot[i].getGroup().getWidth(), unitSlot[i].getGroup().getHeight());
                        }
                        else
                        {
                            if(i != previousSlotNumber)
                            {
                                if(unitSlot[i].getUnit().getName() == unitSlot[previousSlotNumber].getUnit().getName())
                                {
                                    shapeRenderer.rect(unitSlot[i].getGroup().getX() + 100, unitSlot[i].getGroup().getY() + 20,
                                            unitSlot[i].getGroup().getWidth(), unitSlot[i].getGroup().getHeight());
                                }
                            }
                        }
                    }
                }
                else
                {
                    shapeRenderer.rect(unitSlot[slotNumber].getGroup().getX() + 100, unitSlot[slotNumber].getGroup().getY() + 20,
                            unitSlot[slotNumber].getGroup().getWidth(), unitSlot[slotNumber].getGroup().getHeight());
                    if(unitSlot[slotNumber].getUnit().getQuantity() > 1)
                    {
                        splitUnits.getSplitIcon().setDrawable(new SpriteDrawable(new Sprite(splitUnits.getSplitTextureActive())));
                    }
                    else
                    {
                        splitUnits.getSplitIcon().setDrawable(new SpriteDrawable(new Sprite(splitUnits.getSplitTexture())));
                    }
                }
                shapeRenderer.end();
            }
        }
        else
        {
            splitUnits.getSplitIcon().setDrawable(new SpriteDrawable(new Sprite(splitUnits.getSplitTexture())));
        }

    }

    public void dispose()
    {
        for(int i = 0; i < unitSlot.length; i++)
        {
            unitSlot[i].dispose();
        }
        splitUnits.dispose();
        unitInfo.dispose();
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public void setUnitTouched(boolean unitTouched)
    {
        this.unitTouched = unitTouched;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public Stage getStage()
    {
        return stage;
    }

    public UnitSlot[] getUnitSlot()
    {
        return unitSlot;
    }
}