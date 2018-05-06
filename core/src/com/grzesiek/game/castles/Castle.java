package com.grzesiek.game.castles;

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
import com.grzesiek.game.Dragon;
import com.grzesiek.game.castles.buildings.Graveyard;
import com.grzesiek.game.castles.units.Unit;
import com.grzesiek.game.castles.buildings.UnitRecruiter;
import com.grzesiek.game.castles.buildings.Hall;
import com.grzesiek.game.castles.buildings.SkeletonsCrypt;
import com.grzesiek.game.player.Player;
import com.grzesiek.game.player.heroes.Hero;

/**
 * Created by Grzesiek on 2017-09-17.
 */

public class Castle extends DragListener
{
    private Viewport viewport;
    private OrthographicCamera cam;

    private Stage stage;
    private Table table;
    private Group group;

    private Hall hall;
    private UnitRecruiter[] unitRecruiter;

    private UnitSlot[] castleUnitSlot;
    private HeroSlot castleHeroSlot;
    private UnitSlot[] heroUnitSlot;
    private HeroSlot heroSlot;

    private SplitUnits splitUnits;
    private UnitInfo unitInfo;

    private Image backgroundImage;
    private Image bottomImage;
    private Image castleIconImage;
    private Image castleNameImage;
    private Image castleEarningsImage;
    private Image castleMiniImage;
    private Image tavernIconImage;
    private Image hallIconImage;
    private Image castlesUpImage;
    private Image castlesDownImage;
    private Image acceptImage;
    private Image[] unitMiniImage;

    private Label castleNameLabel;
    private Label castleEarningsLabel;
    private Label[] unitGrowthLabel;

    private boolean visible;
    private boolean unitTouched;
    private boolean canBuild;
    private boolean heroInCastle;

    private int castleEarnings;
    private int emptyCastleSlotNumber;
    private int slotNumber;
    private int previousSlotNumber;

    public Castle (SpriteBatch batch, final Player player)
    {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, batch);
        table = new Table();

        defineSlots();

        splitUnits = new SplitUnits(viewport, batch, this);
        unitInfo = new UnitInfo(viewport, batch, player);

        visible = false;
        unitTouched = false;
        canBuild = true;
        heroInCastle = false;

        castleEarnings = 1000;
        emptyCastleSlotNumber = 0;
        slotNumber = 0;
        previousSlotNumber = 0;

        unitRecruiter = new UnitRecruiter[2];
        unitRecruiter[0] = new SkeletonsCrypt(viewport, cam, batch);
        unitRecruiter[1] = new Graveyard(viewport, cam, batch);
        hall = new Hall(viewport, batch, this, player);

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();
        setSlotsPosition();

        group = new Group();
        addGroupElements();

        createHall(this);
        if(Dragon.debugMode)
        {
            for(int i = 0; i < unitRecruiter.length; i++)
            {
                this.createUnitRecruiter(player, i);
                unitRecruiter[i].upgrade();
                unitMiniImage[i].setDrawable(new SpriteDrawable(new Sprite(unitRecruiter[i].getUnit()[1].getMiniTexture())));
            }
        }

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
                    if(castleUnitSlot[previousSlotNumber].getUnit().getQuantity() > 1)
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
                hall.setVisible(false);
                hall.getBuildingPurchase().setVisible(false);
                for(int i = 0; i < unitRecruiter.length; i++)
                {
                    if(unitRecruiter[i].isCreated())
                    {
                        unitRecruiter[i].setVisible(false);
                    }
                }
                unitTouched = false;
                if(heroInCastle)
                {
                    for(int i = 0; i < player.getHero().getUnitSlot().length; i++)
                    {
                        if(castleUnitSlot[i + 7].getUnit() != null)
                        {
                            if(player.getHero().getUnitSlot()[i].getUnit() == null)
                            {
                                player.getHero().getUnitSlot()[i].addUnit(castleUnitSlot[i + 7].getUnit(), castleUnitSlot[i + 7].getUnit().getQuantity());
                                castleUnitSlot[i + 7].removeUnit();
                            }
                            else if(castleUnitSlot[i + 7].getUnit().getName() == player.getHero().getUnitSlot()[i].getUnit().getName())
                            {
                                player.getHero().getUnitSlot()[i].setUnitQuantity(castleUnitSlot[i + 7].getUnit().getQuantity());
                                castleUnitSlot[i + 7].removeUnit();
                            }
                            else
                            {
                                player.getHero().getUnitSlot()[i].addUnit(castleUnitSlot[i + 7].getUnit(), castleUnitSlot[i + 7].getUnit().getQuantity());
                                castleUnitSlot[i + 7].removeUnit();
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
                    //heroSlot.removeHero(false);
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

    private void defineImages()
    {
        unitMiniImage = new Image[8];

        backgroundImage = new Image(new Texture("castles/castle_1/castleBackground.png"));
        bottomImage = new Image(new Texture("castles/castleBottom.png"));
        castleIconImage = new Image(new Texture("castles/castle_1/castle_icon.png"));
        castleNameImage = new Image(new Texture("castles/castleName.png"));
        castleEarningsImage = new Image(new Texture("castles/castleEarnings.png"));
        castleMiniImage = new Image(new Texture("castles/castle_1/castle_mini.png"));
        tavernIconImage = new Image(new Texture("castles/tavern_icon.png"));
        hallIconImage = new Image(new Texture("castles/hall_icon.png"));
        castlesUpImage = new Image(new Texture("castles/castlesUp2.png"));
        castlesDownImage = new Image(new Texture("castles/castlesDown2.png"));
        acceptImage = new Image(new Texture("castles/accept.png"));

        for(int i = 0; i < unitMiniImage.length; i++)
        {
            unitMiniImage[i] = new Image(new Texture("castles/emptyMini.png"));
            unitMiniImage[i].setSize(unitMiniImage[i].getWidth() + 2, unitMiniImage[i].getHeight());
        }
    }

    private void defineLabels()
    {
        unitGrowthLabel = new Label[7];

        castleNameLabel = new Label("Citadel of death", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        castleEarningsLabel = new Label(String.valueOf(castleEarnings), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        for(int i = 0; i < unitGrowthLabel.length; i++)
        {
            unitGrowthLabel[i] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
            unitGrowthLabel[i].setFontScale(0.8f);
            unitGrowthLabel[i].setWidth(unitMiniImage[i].getWidth());
            unitGrowthLabel[i].setAlignment(Align.center);
            unitGrowthLabel[i].setVisible(false);
        }
    }

    private void setImagesPosition()
    {
        this.backgroundImage.setSize(760, 440);
        bottomImage.setSize(backgroundImage.getWidth(), bottomImage.getHeight() / 2);
        castleIconImage.setSize(castleIconImage.getWidth() + 2, castleIconImage.getHeight());
        tavernIconImage.setSize(tavernIconImage.getWidth() + 2, tavernIconImage.getHeight() + 2);
        castleNameImage.setSize(castleNameImage.getWidth() + 5, castleNameImage.getHeight());
        castleEarningsImage.setSize(castleEarningsImage.getWidth() + 2, castleEarningsImage.getHeight());
        hallIconImage.setSize(hallIconImage.getWidth() + 2, hallIconImage.getHeight() + 2);
        splitUnits.getSplitIcon().setSize(splitUnits.getSplitIcon().getWidth(), splitUnits.getSplitIcon().getHeight() - 2);
        acceptImage.setSize(46, 28);

        bottomImage.setPosition(backgroundImage.getX(), backgroundImage.getY());
        castleIconImage.setPosition(bottomImage.getX() + 13, bottomImage.getY() + bottomImage.getHeight() - castleIconImage.getHeight() - 10);
        castleMiniImage.setPosition(bottomImage.getX() + bottomImage.getWidth() - castleMiniImage.getWidth() - 7, bottomImage.getY() + bottomImage.getHeight() - castleMiniImage.getHeight() - castleMiniImage.getHeight() * 3 / 2 - 5);
        tavernIconImage.setPosition(backgroundImage.getX() + tavernIconImage.getWidth() * 2 - 3, backgroundImage.getY() + tavernIconImage.getHeight() * 3 - 8);
        hallIconImage.setPosition(tavernIconImage.getX() + tavernIconImage.getWidth() + 1, tavernIconImage.getY());
        castleNameImage.setPosition(castleIconImage.getX() + castleIconImage.getWidth() + 5, castleIconImage.getY() + castleIconImage.getHeight() - castleNameImage.getHeight());
        castleEarningsImage.setPosition(hallIconImage.getX() + hallIconImage.getWidth() + 1, hallIconImage.getY() + 3);
        unitMiniImage[0].setPosition(castleIconImage.getX(), castleIconImage.getY() - unitRecruiter[0].getUnit()[0].getMini().getHeight() - 4);
        unitMiniImage[1].setPosition(unitMiniImage[0].getX() + unitMiniImage[1].getWidth() + 5, unitMiniImage[0].getY());
        unitMiniImage[2].setPosition(unitMiniImage[1].getX() + unitMiniImage[2].getWidth() + 5, unitMiniImage[0].getY());
        unitMiniImage[3].setPosition(unitMiniImage[2].getX() + unitMiniImage[3].getWidth() + 5, unitMiniImage[0].getY());
        unitMiniImage[4].setPosition(unitMiniImage[0].getX(), unitMiniImage[0].getY() - unitMiniImage[4].getHeight() - 13);
        unitMiniImage[5].setPosition(unitMiniImage[1].getX(), unitMiniImage[1].getY() - unitMiniImage[5].getHeight() - 13);
        unitMiniImage[6].setPosition(unitMiniImage[2].getX(), unitMiniImage[2].getY() - unitMiniImage[6].getHeight() - 13);
        unitMiniImage[7].setPosition(unitMiniImage[3].getX(), unitMiniImage[3].getY() - unitMiniImage[7].getHeight() - 13);
        castlesUpImage.setPosition(castleMiniImage.getX(), castleMiniImage.getY() + castleMiniImage.getHeight() + 2);
        castlesDownImage.setPosition(castlesUpImage.getX(), bottomImage.getY() + castlesDownImage.getHeight() * 4 / 2 - 2);
        splitUnits.getSplitIcon().setPosition(castlesUpImage.getX(), castlesUpImage.getY() + castlesUpImage.getHeight() + 2);
        acceptImage.setPosition(backgroundImage.getWidth() - acceptImage.getWidth() - 6, backgroundImage.getY() + 1);
    }

    private void setLabelsPosition()
    {
        castleNameLabel.setPosition(castleNameImage.getX() + castleNameImage.getWidth() / 2 - castleNameLabel.getWidth() / 2, castleNameImage.getY() + castleNameImage.getHeight() / 2 - castleNameLabel.getHeight() / 2);
        castleEarningsLabel.setPosition(castleEarningsImage.getX() + castleEarningsImage.getWidth() / 2 - castleEarningsLabel.getWidth() / 2,
                castleEarningsImage.getY() + castleEarningsImage.getHeight() / 2 - castleEarningsLabel.getHeight() / 2);
        for(int i = 0; i < unitGrowthLabel.length; i++)
        {
            unitGrowthLabel[i].setPosition(unitMiniImage[i].getX() + unitMiniImage[i].getWidth() / 2 - unitGrowthLabel[i].getWidth() / 2, unitMiniImage[i].getY() - unitGrowthLabel[i].getHeight() + 2);
        }
    }

    private void defineSlots()
    {
        castleUnitSlot = new UnitSlot[14];
        //heroUnitSlot = new UnitSlot[7];
        for(int i = 0; i < castleUnitSlot.length; i++)
        {
            castleUnitSlot[i] = new UnitSlot();
            castleUnitSlot[i].getGroup().addListener(this);
            /*heroUnitSlot[i] = new UnitSlot();
            heroUnitSlot[i].getGroup().addListener(this);*/
        }
        /*castleHeroSlot = new HeroSlot();
        castleHeroSlot.getGroup().addListener(this);
        castleHeroSlot.setImageToEmptyCastleSlot();
        heroSlot = new HeroSlot();
        heroSlot.getGroup().addListener(this);*/
    }

    private void setSlotsPosition()
    {
        for(int i = 0; i < castleUnitSlot.length; i++)
        {
            if(i == 0)
            {
                castleUnitSlot[0].getGroup().setPosition(hallIconImage.getX() + hallIconImage.getWidth() * 9 / 2 - 2, hallIconImage.getY() + 1);
            }
            else if(i > 0 && i < 7)
            {
                castleUnitSlot[i].getGroup().setPosition(castleUnitSlot[i - 1].getGroup().getX() + castleUnitSlot[i - 1].getGroup().getWidth() + 2, castleUnitSlot[i - 1].getGroup().getY());
            }
            else
            {
                castleUnitSlot[i].getGroup().setPosition(castleUnitSlot[i - 7].getGroup().getX(), castleUnitSlot[i - 7].getGroup().getY() - castleUnitSlot[i].getGroup().getHeight() - 25);
            }
            castleUnitSlot[i].setBounds();
            castleUnitSlot[i].setLabelsPosition();
            castleUnitSlot[i].getGroup().setName(String.valueOf(i));
            castleUnitSlot[i].setPositionCoordinates(castleUnitSlot[i].getGroup().getX(), castleUnitSlot[i].getGroup().getY());

            /*heroUnitSlot[i].getGroup().setPosition(castleUnitSlot[i].getGroup().getX(), castleUnitSlot[i].getGroup().getY() - heroUnitSlot[i].getGroup().getHeight() - 25);
            heroUnitSlot[i].setBounds();
            heroUnitSlot[i].setLabelsPosition();
            heroUnitSlot[i].getGroup().setName("heroUnitSlot_" +String.valueOf(i));
            heroUnitSlot[i].setPositionCoordinates(heroUnitSlot[i].getGroup().getX(), heroUnitSlot[i].getGroup().getY());*/
        }

        /*castleHeroSlot.getGroup().setPosition(castleUnitSlot[0].getPosition().x - castleHeroSlot.getGroup().getWidth() - 3, castleUnitSlot[0].getPosition().y);
        castleHeroSlot.setBounds();
        castleHeroSlot.setLabelsPosition();
        castleHeroSlot.getGroup().setName("castleHeroSlot_" +String.valueOf(7));
        castleHeroSlot.setPositionCoordinates(castleHeroSlot.getGroup().getX(), castleHeroSlot.getGroup().getY());

        heroSlot.getGroup().setPosition(castleHeroSlot.getGroup().getX(), castleHeroSlot.getGroup().getY() - heroSlot.getGroup().getHeight() - 25);
        heroSlot.setBounds();
        heroSlot.setLabelsPosition();
        heroSlot.getGroup().setName("heroSlot_" +String.valueOf(7));
        heroSlot.setPositionCoordinates(heroSlot.getGroup().getX(), heroSlot.getGroup().getY());*/
    }

    private void addGroupElements()
    {
        //adding images to group
        group.addActor(backgroundImage);
        group.addActor(bottomImage);
        group.addActor(castleIconImage);
        group.addActor(castleMiniImage);
        group.addActor(tavernIconImage);
        group.addActor(hallIconImage);
        group.addActor(castleNameImage);
        group.addActor(castleEarningsImage);
        group.addActor(unitMiniImage[0]);
        group.addActor(unitMiniImage[1]);
        group.addActor(unitMiniImage[2]);
        group.addActor(unitMiniImage[3]);
        group.addActor(unitMiniImage[4]);
        group.addActor(unitMiniImage[5]);
        group.addActor(unitMiniImage[6]);
        group.addActor(unitMiniImage[7]);
        group.addActor(castlesUpImage);
        group.addActor(castlesDownImage);
        group.addActor(splitUnits.getSplitIcon());
        group.addActor(acceptImage);
        //adding labels to group
        group.addActor(castleNameLabel);
        group.addActor(castleEarningsLabel);
        group.addActor(unitGrowthLabel[0]);
        group.addActor(unitGrowthLabel[1]);
        group.addActor(unitGrowthLabel[2]);
        group.addActor(unitGrowthLabel[3]);
        group.addActor(unitGrowthLabel[4]);
        group.addActor(unitGrowthLabel[5]);
        group.addActor(unitGrowthLabel[6]);
        //adding slot groups to group
        group.addActor(castleUnitSlot[0].getGroup());
        group.addActor(castleUnitSlot[1].getGroup());
        group.addActor(castleUnitSlot[2].getGroup());
        group.addActor(castleUnitSlot[3].getGroup());
        group.addActor(castleUnitSlot[4].getGroup());
        group.addActor(castleUnitSlot[5].getGroup());
        group.addActor(castleUnitSlot[6].getGroup());
        group.addActor(castleUnitSlot[7].getGroup());
        group.addActor(castleUnitSlot[8].getGroup());
        group.addActor(castleUnitSlot[9].getGroup());
        group.addActor(castleUnitSlot[10].getGroup());
        group.addActor(castleUnitSlot[11].getGroup());
        group.addActor(castleUnitSlot[12].getGroup());
        group.addActor(castleUnitSlot[13].getGroup());
        /*group.addActor(castleHeroSlot.getGroup());
        group.addActor(heroUnitSlot[0].getGroup());
        group.addActor(heroUnitSlot[1].getGroup());
        group.addActor(heroUnitSlot[2].getGroup());
        group.addActor(heroUnitSlot[3].getGroup());
        group.addActor(heroUnitSlot[4].getGroup());
        group.addActor(heroUnitSlot[5].getGroup());
        group.addActor(heroUnitSlot[6].getGroup());
        group.addActor(heroSlot.getGroup());*/
    }

    private void createHall(final Castle castle)
    {
        hall.getBuildingImage().setPosition(backgroundImage.getX() + backgroundImage.getWidth() - hall.getBuildingImage().getWidth() * 2,
                backgroundImage.getY() + backgroundImage.getHeight() - hall.getBuildingImage().getHeight() * 2);

        group.addActor(hall.getBuildingImage());

        hall.getBuildingImage().addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                hall.setVisible(true);
                unitTouched = false;
                hall.update(castle);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    public void createUnitRecruiter(Player player, final int index)
    {
        unitRecruiter[index].create(player, this);

        unitGrowthLabel[index].setText(String.valueOf(unitRecruiter[index].getUnit()[index].getGrowth()));
        unitGrowthLabel[index].setVisible(true);
        unitMiniImage[index].setDrawable(new SpriteDrawable(new Sprite(unitRecruiter[index].getUnit()[0].getMiniTexture())));

        if(index == 0)
        {
            unitRecruiter[index].getBuildingImage().setPosition(backgroundImage.getX() + unitRecruiter[index].getBuildingImage().getWidth() / 2,
                    bottomImage.getHeight() + unitRecruiter[index].getBuildingImage().getHeight() / 2);
        }
        else if(index == 1)
        {
            unitRecruiter[index].getBuildingImage().setPosition(backgroundImage.getX() + unitRecruiter[index].getBuildingImage().getWidth() / 2 + 200,
                    bottomImage.getHeight() + unitRecruiter[index].getBuildingImage().getHeight() / 2 + 50);
        }

        group.addActor(unitRecruiter[index].getBuildingImage());

        unitRecruiter[index].getBuildingImage().addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                unitRecruiter[index].setVisible(true);
                unitTouched = false;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        hall.setVisible(false);
    }

    public int updateCastleSlot(Unit unit, int numberOfUnits)
    {
        emptyCastleSlotNumber = -1;
        for(int i = 0; i < castleUnitSlot.length; i++)
        {
            if(castleUnitSlot[i].getUnit() != null)
            {
                if(castleUnitSlot[i].getUnit().getName().equals(unit.getName()))
                {
                    castleUnitSlot[i].adjustUnitQuantity(numberOfUnits);
                    emptyCastleSlotNumber = i;
                    break;
                }
            }
        }
        if(emptyCastleSlotNumber == -1)
        {
            for(int i = 0; i < castleUnitSlot.length; i++)
            {
                if(castleUnitSlot[i].getUnit() == null)
                {
                    castleUnitSlot[i].addUnit(unit, numberOfUnits);
                    emptyCastleSlotNumber = i;
                    break;
                }
            }
        }
        return emptyCastleSlotNumber;
    }

    public void update(Hero hero)
    {
        heroInCastle = hero.isInCastle();
        if(heroInCastle)
        {
            for(int i = 0; i < hero.getUnitSlot().length; i++)
            {
                castleUnitSlot[i + 7].removeUnit();
                if(hero.getUnitSlot()[i].getUnit() != null)
                {
                    castleUnitSlot[i + 7].addUnit(hero.getUnitSlot()[i].getUnit(), hero.getUnitSlot()[i].getUnit().getQuantity());
                }
            }
            //heroSlot.addHero(hero.getName(), hero.getHeroIconTexture());
        }
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
    {
        slotNumber = manageSlotNumber(event.getTarget().getParent().getName());
        if(heroInCastle)
        {
            manageSlotTouches();
        }
        else
        {
            if(slotNumber < (castleUnitSlot.length / 2))
            {
                manageSlotTouches();
            }
        }
        previousSlotNumber = slotNumber;
        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void drag (InputEvent event, float x, float y, int pointer)
    {
        slotNumber = manageSlotNumber(event.getTarget().getParent().getName());
        if(!splitUnits.isJustClosed() && ! unitInfo.isJustClosed())
        {
            if(castleUnitSlot[slotNumber].getUnit() != null)
            {
                unitTouched = false;
                castleUnitSlot[slotNumber].getGroup().moveBy(x - castleUnitSlot[slotNumber].getGroup().getWidth() / 2, y - castleUnitSlot[slotNumber].getGroup().getHeight() / 2);
                castleUnitSlot[slotNumber].getGroup().toFront();
            }
        }
        super.drag(event, x, y, pointer);
    }

    @Override
    public void dragStop (InputEvent event, float x, float y, int pointer)
    {
        if(heroInCastle)
        {
            manageSlotDrops(castleUnitSlot.length);
        }
        else
        {
            manageSlotDrops(castleUnitSlot.length / 2);
        }
        super.dragStop(event, x, y, pointer);
    }

    private int manageSlotNumber(String targetName)
    {
        int slotNumber = 0;
        for(int i = 0; i < (castleUnitSlot.length + 1); i++)
        {
            if(targetName.contains(String.valueOf(i)))
            {
                slotNumber = i;
            }
        }
        return slotNumber;
    }

    private void manageSlotTouches()
    {
        if(castleUnitSlot[slotNumber].getUnit() != null)
        {
            if(! splitUnits.isClicked())
            {
                if(unitTouched)
                {
                    if(slotNumber == previousSlotNumber)
                    {
                        unitTouched = false;
                        if(castleUnitSlot[slotNumber].getUnit().getID() % 2 == 1)
                        {
                            for(int i = 0; i < unitRecruiter.length; i++)
                            {
                                if(unitRecruiter[i].getUnit()[0].getName().equals(castleUnitSlot[slotNumber].getUnit().getName()))
                                {
                                    if(unitRecruiter[i].isUpgraded())
                                    {
                                        unitInfo.update(castleUnitSlot[slotNumber], true);
                                    }
                                    else
                                    {
                                        unitInfo.update(castleUnitSlot[slotNumber], false);
                                    }
                                }
                            }
                        }
                        else
                        {
                            unitInfo.update(castleUnitSlot[slotNumber], false);
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
                    if(castleUnitSlot[previousSlotNumber].getUnit() != null)
                    {
                        if(castleUnitSlot[previousSlotNumber].getUnit().getName() == castleUnitSlot[slotNumber].getUnit().getName())
                        {
                            splitUnits.setVisible(true);
                            splitUnits.update(this, castleUnitSlot[previousSlotNumber].getUnit(), previousSlotNumber, slotNumber, true);
                        }
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
                splitUnits.update(this, castleUnitSlot[previousSlotNumber].getUnit(), previousSlotNumber, slotNumber, false);
            }
            unitTouched = false;
            splitUnits.setClicked(false);
            splitUnits.setJustClosed(false);
            unitInfo.setJustClosed(false);
        }
    }

    private void manageSlotDrops(int length)
    {
        int newSlot = -1;
        for(int i = 0; i < length; i++)
        {
            if(castleUnitSlot[i].getBounds().contains(castleUnitSlot[slotNumber].getGroup().getX() + castleUnitSlot[slotNumber].getGroup().getWidth() / 2,
                    castleUnitSlot[slotNumber].getGroup().getY() + castleUnitSlot[slotNumber].getGroup().getHeight() / 2))
            {
                newSlot = i;
                if(slotNumber != newSlot)
                {
                    if(castleUnitSlot[newSlot].getUnit() == null)
                    {
                        castleUnitSlot[newSlot].addUnit(castleUnitSlot[slotNumber].getUnit(), castleUnitSlot[slotNumber].getUnit().getQuantity());
                        castleUnitSlot[slotNumber].removeUnit();
                        castleUnitSlot[slotNumber].restorePosition();
                        break;
                    }
                    else
                    {
                        if(castleUnitSlot[newSlot].getUnit().getName().equals(castleUnitSlot[slotNumber].getUnit().getName()))
                        {
                            castleUnitSlot[newSlot].adjustUnitQuantity(castleUnitSlot[slotNumber].getUnit().getQuantity());
                            castleUnitSlot[slotNumber].removeUnit();
                            castleUnitSlot[slotNumber].restorePosition();
                            break;
                        }
                        else
                        {
                            castleUnitSlot[slotNumber].swapSlots(castleUnitSlot[newSlot]);
                            break;

                        }
                    }
                }
                else
                {
                    castleUnitSlot[slotNumber].restorePosition();
                }
            }
        }
        if(newSlot == -1)
        {
            castleUnitSlot[slotNumber].restorePosition();
        }
    }

    public void draw()
    {
        Gdx.input.setInputProcessor(stage);
        stage.draw();
        if(hall.isVisible())
        {
            hall.draw();
        }
        else if(unitRecruiter[0].isVisible())
        {
            unitRecruiter[0].draw();
        }
        else if(unitRecruiter[1].isVisible())
        {
            unitRecruiter[1].draw();
        }
        else if(splitUnits.isVisible())
        {
            splitUnits.draw();
        }
        else if(unitInfo.isVisible())
        {
            unitInfo.draw();
        }
        else
        {
            updateGoldenFrame(slotNumber);
        }
    }

    private void updateGoldenFrame(int slotNumber)
    {
        if(unitTouched)
        {
            if(castleUnitSlot[slotNumber].getUnit() != null)
            {
                ShapeRenderer shapeRenderer = new ShapeRenderer();
                shapeRenderer.setProjectionMatrix(cam.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GOLD);
                if(splitUnits.isClicked())
                {
                    for(int i = 0; i < castleUnitSlot.length; i++)
                    {
                        if(!heroInCastle)
                        {
                            if(i > ((castleUnitSlot.length / 2) - 1))
                            {
                                break;
                            }
                        }
                        if(castleUnitSlot[i].getUnit() == null)
                        {
                            shapeRenderer.rect(castleUnitSlot[i].getGroup().getX() + 20, castleUnitSlot[i].getGroup().getY() + 20,
                                    castleUnitSlot[i].getGroup().getWidth(), castleUnitSlot[i].getGroup().getHeight());
                        }
                        else
                        {
                            if(i != previousSlotNumber)
                            {
                                if(castleUnitSlot[i].getUnit().getName() == castleUnitSlot[previousSlotNumber].getUnit().getName())
                                {
                                    shapeRenderer.rect(castleUnitSlot[i].getGroup().getX() + 20, castleUnitSlot[i].getGroup().getY() + 20,
                                            castleUnitSlot[i].getGroup().getWidth(), castleUnitSlot[i].getGroup().getHeight());
                                }
                            }
                        }
                    }
                }
                else
                {
                    shapeRenderer.rect(castleUnitSlot[slotNumber].getGroup().getX() + 20, castleUnitSlot[slotNumber].getGroup().getY() + 20,
                            castleUnitSlot[slotNumber].getGroup().getWidth(), castleUnitSlot[slotNumber].getGroup().getHeight());
                    if(castleUnitSlot[slotNumber].getUnit().getQuantity() > 1)
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
        unitRecruiter[0].dispose();
        unitRecruiter[1].dispose();
        splitUnits.dispose();
        hall.dispose();
        for(int i = 0; i < castleUnitSlot.length; i++)
        {
            castleUnitSlot[i].dispose();
        }
        unitInfo.dispose();
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public void setCanBuild(boolean canBuild)
    {
        this.canBuild = canBuild;
    }

    public void setUnitTouched (boolean unitTouched)
    {
        this.unitTouched = unitTouched;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public boolean getCanBuild()
    {
        return canBuild;
    }

    public Image[] getMiniImage()
    {
        return unitMiniImage;
    }

    public Stage getStage()
    {
        return stage;
    }

    public Hall getHall()
    {
        return hall;
    }

    public UnitRecruiter[] getUnitRecruiter ()
    {
        return unitRecruiter;
    }

    public SplitUnits getSplitUnits()
    {
        return splitUnits;
    }

    public UnitInfo getUnitInfo ()
    {
        return unitInfo;
    }

    public UnitSlot[] getCastleUnitSlot ()
    {
        return castleUnitSlot;
    }
}