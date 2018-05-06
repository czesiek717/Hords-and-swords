package com.grzesiek.game.castles;

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
import com.grzesiek.game.castles.units.Unit;
import com.grzesiek.game.player.heroes.HeroInformation;

/**
 * Created by Grzesiek on 2017-10-22.
 */

public class SplitUnits
{
    private Viewport viewport;

    private Stage stage;
    private Table table;

    private Texture splitTexture;
    private Texture splitTextureActive;

    private Image splitIcon;
    private Image splitBackground;
    private Image unitImage;
    private Image slider;
    private Image sliderBar;
    private Image accept;
    private Image decline;
    private Image[] sliderMover;
    private Image[] splitInfoImage;

    private Label splitHeader;
    private Label[] splitInfoLabel;

    private boolean visible;
    private boolean clicked;
    private boolean justClosed;

    private int numberOfUnits;
    private int newNumberOfUnits;
    private int currentSlotNumber;
    private int previousSlotNumber;

    private float sliderMove;

    public SplitUnits(Viewport port, SpriteBatch batch, final Castle castle)
    {
        this.viewport = port;
        viewport.update(350, 380);

        stage = new Stage(viewport, batch);
        table = new Table();

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();

        Group group = new Group();
        addGroupElements(group);

        visible = false;
        clicked = false;
        justClosed = false;

        numberOfUnits = 0;
        newNumberOfUnits = 1;
        currentSlotNumber = 0;
        previousSlotNumber = 0;

        table.center().center();
        table.setFillParent(true);
        table.add(group).size(splitBackground.getWidth(), splitBackground.getHeight());

        stage.addActor(table);

        sliderMover[0].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(slider.getX() > sliderBar.getX() && sliderMove > 0)
                {
                    numberOfUnits++;
                    newNumberOfUnits--;
                    slider.setPosition(slider.getX() - sliderMove, slider.getY());
                    updateLabels();

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
                if(slider.getX() < sliderBar.getX() + sliderBar.getWidth() - slider.getWidth() && sliderMove > 0)
                {
                    numberOfUnits--;
                    newNumberOfUnits++;
                    slider.setPosition(slider.getX() + sliderMove, slider.getY());
                    updateLabels();
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
                justClosed = true;
                castle.setUnitTouched(false);
                split(castle);
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

    public SplitUnits(Viewport port, SpriteBatch batch, final HeroInformation heroInformation)
    {
        this.viewport = port;
        viewport.update(350, 380);

        stage = new Stage(viewport, batch);
        table = new Table();

        defineImages();
        defineLabels();
        setImagesPosition();
        setLabelsPosition();

        Group group = new Group();
        addGroupElements(group);

        visible = false;
        clicked = false;
        justClosed = false;

        numberOfUnits = 0;
        newNumberOfUnits = 1;
        currentSlotNumber = 0;
        previousSlotNumber = 0;

        table.center().center();
        table.setFillParent(true);
        table.add(group).size(splitBackground.getWidth(), splitBackground.getHeight());

        stage.addActor(table);

        sliderMover[0].addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(slider.getX() > sliderBar.getX() && sliderMove > 0)
                {
                    numberOfUnits++;
                    newNumberOfUnits--;
                    slider.setPosition(slider.getX() - sliderMove, slider.getY());
                    updateLabels();

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
                if(slider.getX() < sliderBar.getX() + sliderBar.getWidth() - slider.getWidth() && sliderMove > 0)
                {
                    if(numberOfUnits > 1)
                    {
                        numberOfUnits--;
                        newNumberOfUnits++;
                        slider.setPosition(slider.getX() + sliderMove, slider.getY());
                    }
                    updateLabels();
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
                justClosed = true;
                heroInformation.setUnitTouched(false);
                split(heroInformation);
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
        splitTexture = new Texture("castles/split_icon2.png");
        splitTextureActive = new Texture("castles/split_icon.png");

        sliderMover = new Image[2];
        splitInfoImage = new Image[2];

        splitIcon = new Image(splitTexture);
        splitBackground = new Image(new Texture("castles/splitBackground.png"));
        unitImage = new Image(new Texture("castles/castle_1/units/skeleton.png"));
        slider = new Image(new Texture("castles/slider.png"));
        sliderBar = new Image(new Texture("castles/sliderBar.png"));
        sliderMover[0] = new Image(new Texture("castles/sliderMover_1.png"));
        sliderMover[1] = new Image(new Texture("castles/sliderMover_2.png"));
        splitInfoImage[0] = new Image(new Texture("castles/splitInfo.png"));
        splitInfoImage[1] = new Image(new Texture("castles/splitInfo.png"));
        accept = new Image(new Texture("castles/accept.png"));
        decline = new Image(new Texture("castles/decline.png"));
    }

    private void defineLabels()
    {
        splitInfoLabel = new Label[2];

        splitHeader = new Label("Split: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        splitInfoLabel[0] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        splitInfoLabel[1] = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        splitHeader.setWidth(splitBackground.getWidth());
        splitInfoLabel[0].setWidth(splitInfoImage[0].getWidth());
        splitInfoLabel[1].setWidth(splitInfoImage[1].getWidth());

        splitHeader.setAlignment(Align.center);
        splitInfoLabel[0].setAlignment(Align.center);
        splitInfoLabel[1].setAlignment(Align.center);
    }

    private void setImagesPosition()
    {
        splitBackground.setSize(350, 380);
        unitImage.setSize(unitImage.getWidth() + 25, unitImage.getHeight() + 25);

        unitImage.setPosition(splitBackground.getX() + splitBackground.getWidth() / 2 - unitImage.getWidth() / 2, splitBackground.getY() + splitBackground.getHeight() - unitImage.getHeight() - 50);
        sliderBar.setPosition(splitBackground.getX() + splitBackground.getWidth() / 2 - sliderBar.getWidth() / 2, unitImage.getY() - sliderBar.getHeight() - 10);
        slider.setPosition(sliderBar.getX(), sliderBar.getY());
        sliderMover[0].setPosition(sliderBar.getX() - sliderMover[0].getWidth(), sliderBar.getY());
        sliderMover[1].setPosition(sliderBar.getX() + sliderBar.getWidth(), sliderBar.getY());
        splitInfoImage[0].setPosition(splitBackground.getX() + 20, sliderBar.getY() - splitInfoImage[0].getHeight() - 10);
        splitInfoImage[1].setPosition(splitBackground.getX() +splitBackground.getWidth() - splitInfoImage[1].getWidth() - 20, sliderBar.getY() - splitInfoImage[0].getHeight() - 10);
        accept.setPosition(splitInfoImage[0].getX(), splitInfoImage[0].getY() - accept.getHeight() - 10);
        decline.setPosition(splitBackground.getX() +splitBackground.getWidth() - decline.getWidth() - 20, splitInfoImage[1].getY() - decline.getHeight() - 10);
    }

    private void setLabelsPosition()
    {
        splitHeader.setPosition(unitImage.getX() + unitImage.getWidth() / 2 - splitHeader.getWidth() / 2, unitImage.getY() + unitImage.getHeight() + 10);
        splitInfoLabel[0].setPosition(splitInfoImage[0].getX() + splitInfoImage[0].getWidth() / 2 - splitInfoLabel[0].getWidth() / 2,
                splitInfoImage[0].getY() + splitInfoImage[0].getHeight() / 2 - splitInfoLabel[0].getHeight() / 2);
        splitInfoLabel[1].setPosition(splitInfoImage[1].getX() + splitInfoImage[1].getWidth() / 2 - splitInfoLabel[1].getWidth() / 2,
                splitInfoImage[1].getY() + splitInfoImage[1].getHeight() / 2 - splitInfoLabel[1].getHeight() / 2);
    }

    private void addGroupElements(Group group)
    {
        //adding images to group
        group.addActor(splitBackground);
        group.addActor(unitImage);
        group.addActor(sliderBar);
        group.addActor(slider);
        group.addActor(sliderMover[0]);
        group.addActor(sliderMover[1]);
        group.addActor(splitInfoImage[0]);
        group.addActor(splitInfoImage[1]);
        group.addActor(accept);
        group.addActor(decline);
        //adding labels to group
        group.addActor(splitHeader);
        group.addActor(splitInfoLabel[0]);
        group.addActor(splitInfoLabel[1]);
    }

    private void updateSlidersMove(boolean merge)
    {
        if(!merge)
        {
            if(numberOfUnits - newNumberOfUnits > 0)
            {
                sliderMove = (sliderBar.getWidth() - slider.getWidth()) / (numberOfUnits - newNumberOfUnits);
                slider.setPosition(sliderBar.getX(), sliderBar.getY());
            }
            else
            {
                sliderMove = 0;
                slider.setPosition(sliderBar.getX() + sliderBar.getWidth() / 2 - slider.getWidth() / 2, sliderBar.getY());
            }
        }
        else
        {
            if(numberOfUnits == newNumberOfUnits)
            {
                sliderMove = (sliderBar.getWidth() - slider.getWidth()) / ((numberOfUnits + newNumberOfUnits) - 2);
                slider.setPosition(sliderBar.getX() + (sliderMove * (numberOfUnits - 1)), sliderBar.getY());
            }
            else
            {
                sliderMove = (sliderBar.getWidth() - slider.getWidth()) / ((numberOfUnits + newNumberOfUnits) - 2);
                slider.setPosition(sliderBar.getX() + (sliderMove * (newNumberOfUnits - 1)), sliderBar.getY());
            }
        }
    }

    private void updateLabels()
    {
        splitInfoLabel[0].setText(String.valueOf(numberOfUnits));
        splitInfoLabel[1].setText(String.valueOf(newNumberOfUnits));
    }

    public void update(Castle castle, Unit unit, int previousSlotNumber, int currentSlotNumber, boolean merge)
    {
        unitImage.setDrawable(new SpriteDrawable(new Sprite(unit.getImageTexture())));

        splitHeader.setText("Split: " + unit.getName() + "'s");
        splitInfoLabel[0].setText("");
        splitInfoLabel[1].setText("");

        this.previousSlotNumber = previousSlotNumber;
        this.currentSlotNumber = currentSlotNumber;

        numberOfUnits = unit.getQuantity();
        if(merge)
        {
            newNumberOfUnits = castle.getCastleUnitSlot()[currentSlotNumber].getUnit().getQuantity();
            updateSlidersMove(true);
        }
        else
        {
            newNumberOfUnits = 1;
            numberOfUnits -= newNumberOfUnits;
            updateSlidersMove(false);
        }

        updateLabels();
    }
    public void update(HeroInformation heroInformation, Unit unit, int previousSlotNumber, int currentSlotNumber, boolean merge)
    {
        unitImage.setDrawable(new SpriteDrawable(new Sprite(unit.getImageTexture())));

        splitHeader.setText("Split: " + unit.getName() + "'s");
        splitInfoLabel[0].setText("");
        splitInfoLabel[1].setText("");

        this.previousSlotNumber = previousSlotNumber;
        this.currentSlotNumber = currentSlotNumber;

        numberOfUnits = unit.getQuantity();
        if(merge)
        {
            newNumberOfUnits = heroInformation.getUnitSlot()[currentSlotNumber].getUnit().getQuantity();
            updateSlidersMove(true);
        }
        else
        {
            newNumberOfUnits = 1;
            numberOfUnits -= newNumberOfUnits;
            updateSlidersMove(false);
        }

        updateLabels();
    }

    private void split(Castle castle)
    {
        if(castle.getCastleUnitSlot()[currentSlotNumber].getUnit() == null)
        {
            castle.getCastleUnitSlot()[currentSlotNumber].addUnit(castle.getCastleUnitSlot()[previousSlotNumber].getUnit(), castle.getCastleUnitSlot()[previousSlotNumber].getUnit().getQuantity());
        }
        castle.getCastleUnitSlot()[previousSlotNumber].setUnitQuantity(numberOfUnits);
        castle.getCastleUnitSlot()[currentSlotNumber].setUnitQuantity(newNumberOfUnits);
    }

    private void split(HeroInformation heroInformation)
    {
        if(heroInformation.getUnitSlot()[currentSlotNumber].getUnit() == null)
        {
            heroInformation.getUnitSlot()[currentSlotNumber].addUnit(heroInformation.getUnitSlot()[previousSlotNumber].getUnit(), heroInformation.getUnitSlot()[previousSlotNumber].getUnit().getQuantity());
        }
        heroInformation.getUnitSlot()[previousSlotNumber].setUnitQuantity(numberOfUnits);
        heroInformation.getUnitSlot()[currentSlotNumber].setUnitQuantity(newNumberOfUnits);
    }

    public void draw()
    {
        Gdx.input.setInputProcessor(stage);
        stage.draw();
    }

    public void dispose()
    {
        splitTexture.dispose();
        splitTextureActive.dispose();
        stage.dispose();
    }

    public void setVisible(boolean change)
    {
        visible = change;
    }

    public void setClicked(boolean change)
    {
        clicked = change;
    }

    public void setJustClosed(boolean change)
    {
        justClosed = change;
    }

    public Stage getStage()
    {
        return stage;
    }

    public Texture getSplitTexture()
    {
        return splitTexture;
    }

    public Texture getSplitTextureActive()
    {
        return splitTextureActive;
    }

    public Image getSplitIcon()
    {
        return splitIcon;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public boolean isClicked()
    {
        return clicked;
    }

    public boolean isJustClosed()
    {
        return justClosed;
    }
}
