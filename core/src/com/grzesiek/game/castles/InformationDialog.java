package com.grzesiek.game.castles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

/**
 * Created by Grzesiek on 2017-10-28.
 */

public class InformationDialog
{
    private Viewport viewport;

    private Stage stage;
    private Table table;

    private Action action;

    private Image backgroundImage;
    private Image acceptImage;
    private Image declineImage;
    private Image[] infoImage;

    private Label[] infoLabel;

    private boolean visible;

    public InformationDialog(Viewport port, final SpriteBatch batch)
    {
        this.viewport = port;
        viewport.update(269, 281);

        stage = new Stage(viewport, batch);
        table = new Table();

        action = null;

        visible = false;

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

        acceptImage.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                action.act(Gdx.graphics.getDeltaTime());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        declineImage.addListener(new InputListener()
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
        infoImage = new Image[2];

        backgroundImage = new Image(new Texture("castles/unitUpgrade.png"));
        acceptImage = new Image(new Texture("castles/accept.png"));
        declineImage = new Image(new Texture("castles/decline.png"));
        infoImage[0] = new Image(new Texture("castles/unitStats.png"));
        infoImage[1] = new Image(new Texture("castles/splitInfo.png"));
    }

    private void defineLabels()
    {
        infoLabel = new Label[2];

        infoLabel[0] = new Label("dialog name: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        infoLabel[1] = new Label("dialog content: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    }

    private void setImagesPosition()
    {
        backgroundImage.setSize(200, 200);
        infoImage[0].setSize(backgroundImage.getWidth() - 25, infoImage[0].getHeight());
        infoImage[1].setSize(backgroundImage.getWidth() - 25, infoImage[1].getHeight() * 5 / 2);

        acceptImage.setPosition(backgroundImage.getX() + backgroundImage.getWidth() / 4 - acceptImage.getWidth() / 2, backgroundImage.getY() + acceptImage.getHeight() / 2 + 15);
        declineImage.setPosition(backgroundImage.getX() + backgroundImage.getWidth() / 4 * 3 - acceptImage.getWidth() / 2, acceptImage.getY());
        infoImage[0].setPosition(backgroundImage.getX() + backgroundImage.getWidth() / 2 - infoImage[0].getWidth() / 2,
                backgroundImage.getY() + backgroundImage.getHeight() - infoImage[0].getHeight() - 15);
        infoImage[1].setPosition(backgroundImage.getX() + backgroundImage.getWidth() / 2 - infoImage[1].getWidth() / 2,
                infoImage[0].getY() - infoImage[1].getHeight() - 5);
    }

    private void setLabelsPosition()
    {
        infoLabel[0].setWidth(infoImage[0].getWidth());
        infoLabel[1].setWidth(infoImage[1].getWidth());

        infoLabel[0].setPosition(infoImage[0].getX() + infoImage[0].getWidth() / 2 - infoLabel[0].getWidth() / 2,
                infoImage[0].getY() + infoImage[0].getHeight() / 2 - infoLabel[0].getHeight() / 2);
        infoLabel[1].setPosition(infoImage[1].getX() + infoImage[1].getWidth() / 2 - infoLabel[1].getWidth() / 2,
                infoImage[1].getY() + infoImage[1].getHeight() / 2 - infoLabel[1].getHeight() / 2);

        infoLabel[0].setAlignment(Align.center);
        infoLabel[1].setAlignment(Align.center);
        infoLabel[1].setWrap(true);
    }

    private void addGroupElements(Group group)
    {
        //adding images to group
        group.addActor(backgroundImage);
        group.addActor(infoImage[0]);
        group.addActor(infoImage[1]);
        group.addActor(acceptImage);
        group.addActor(declineImage);
        //adding labels to group
        group.addActor(infoLabel[0]);
        group.addActor(infoLabel[1]);
    }

    public void update(String dialogName, String dialogContent, Action action)
    {
        infoLabel[0].setText(dialogName);
        infoLabel[1].setText(dialogContent);

        this.action = action;

        declineImage.setVisible(true);
    }

    public void draw()
    {
        Gdx.input.setInputProcessor(stage);
        stage. draw();
    }

    public void dispose()
    {
        stage.dispose();
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public Stage getStage()
    {
        return stage;
    }

    public Image getAcceptImage()
    {
        return acceptImage;
    }

    public Image getDeclineImage()
    {
        return declineImage;
    }
}
