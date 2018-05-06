package com.grzesiek.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.Dragon;

/**
 * Created by Grzesiek on 2017-09-13.
 */

public class HUD
{
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera cam;
    private Dragon game;

    //first table components
    private Image[] imgStockWindow;
    private Image[] imgStock;
    private Label[] labelStock;

    //second table components
    private Image[] imgUnit;
    private Image[] imgUnitCount;
    private Label[] labelUnitCount;

    //third table components
    private Image imgMovementInfo;
    private Label labelMovementInfo;

    private Table[] table;

    public HUD(Dragon game)
    {
        this.game = game;
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, game.batch);

        imgStockWindow = new Image[5];
        imgStock = new Image[5];
        labelStock = new Label[5];

        imgUnit = new Image[5];
        imgUnitCount = new Image[5];
        labelUnitCount = new Label[5];


        table = new Table[3];

        //creating stock table
        table[0] = new Table();
        table[0].setFillParent(true);
        table[0].left().bottom();

        Group[] group1 = new Group[5];

        imgStockWindow[4] = new Image(new Texture("HUD/stockWindow2.png"));

        for(int i = 0; i < imgStockWindow.length; i++)
        {
            group1[i] = new Group();

            if(i != imgStockWindow.length - 1)
            {
                imgStockWindow[i] = new Image(new Texture("HUD/stockWindow.png"));
            }
            labelStock[i] = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            imgStock[i] = new Image(new Texture("HUD/stock" +String.valueOf(i + 1) +".png"));

            imgStock[i].setPosition(imgStockWindow[i].getX() + 4, imgStockWindow[i].getHeight() / 2 - imgStock[i].getHeight() / 2);
            labelStock[i].setPosition(imgStockWindow[i].getX() + 6 + imgStock[i].getWidth(), imgStockWindow[i].getHeight() / 2 - labelStock[i].getHeight() / 2);

            group1[i].addActor(imgStockWindow[i]);
            group1[i].addActor(imgStock[i]);
            group1[i].addActor(labelStock[i]);

            if(i == 0)
            {
                table[0].add(group1[i]).size(imgStockWindow[i].getWidth(), imgStockWindow[i].getHeight()).padLeft(175);
            }
            else
            {
                table[0].add(group1[i]).size(imgStockWindow[i].getWidth(), imgStockWindow[i].getHeight());
            }
        }

        //creating unit table
        table[1] = new Table();
        table[1].setFillParent(true);
        table[1].left().top();

        Group[] group2 = new Group[5];

        for(int i = 0; i < imgUnit.length; i++)
        {
            group2[i] = new Group();

            imgUnit[i] = new Image(new Texture("HUD/unit.png"));
            imgUnitCount[i] = new Image(new Texture("HUD/unitCount.png"));
            labelUnitCount[i] = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            labelUnitCount[i].setAlignment(Align.center);

            imgUnitCount[i].setPosition(imgUnit[i].getWidth() / 2 - imgUnitCount[i].getWidth() / 2, -imgUnitCount[i].getHeight() / 2);
            labelUnitCount[i].setPosition(imgUnit[i].getWidth() / 2 - labelUnitCount[i].getWidth() / 2, -labelUnitCount[i].getHeight() / 2);

            group2[i].addActor(imgUnit[i]);
            group2[i].addActor(imgUnitCount[i]);
            group2[i].addActor(labelUnitCount[i]);

            table[1].add(group2[i]).size(imgUnit[i].getWidth(), imgUnit[i].getHeight() + imgUnitCount[i].getHeight() / 2).padBottom(10).padLeft(10);
            table[1].row();
        }

        //creating movement and end turn table
        table[2] = new Table();
        table[2].setFillParent(true);
        table[2].center().top();

        Group group3 = new Group();

        imgMovementInfo = new Image(new Texture("HUD/stockWindow2.png"));
        labelMovementInfo = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        labelMovementInfo.setPosition(imgMovementInfo.getWidth() / 2 - labelMovementInfo.getWidth() / 2, imgMovementInfo.getHeight() / 2 - labelMovementInfo.getHeight() / 2);
        labelMovementInfo.setAlignment(Align.center);

        group3.addActor(imgMovementInfo);
        group3.addActor(labelMovementInfo);

        table[2].add(group3).size(imgMovementInfo.getWidth(), imgMovementInfo.getHeight()).padTop(10);

        stage.addActor(table[0]);
        stage.addActor(table[1]);
        stage.addActor(table[2]);
    }

    public void updateStockLabels(int label1, int label2, int label3, int label4, int label5)
    {
        labelStock[0].setText(String.valueOf(label1));
        labelStock[1].setText(String.valueOf(label2));
        labelStock[2].setText(String.valueOf(label3));
        labelStock[3].setText(String.valueOf(label4));
        labelStock[4].setText(String.valueOf(label5));
    }

    public void updateUnitLabels(int label1, int label2, int label3, int label4, int label5)
    {
        labelUnitCount[0].setText(String.valueOf(label1));
        labelUnitCount[1].setText(String.valueOf(label2));
        labelUnitCount[2].setText(String.valueOf(label3));
        labelUnitCount[3].setText(String.valueOf(label4));
        labelUnitCount[4].setText(String.valueOf(label5));
    }

    public void updateMovementTable(float label1)
    {
        labelMovementInfo.setText("M: " +String.valueOf(label1));
    }

    public void draw ()
    {
        stage.draw();
    }

    public void resize (int width, int height)
    {
        viewport.update(width, height);
    }

    public void dispose ()
    {
        game.dispose();
        stage.dispose();
    }
}