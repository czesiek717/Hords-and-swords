package com.grzesiek.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.Dragon;
import com.grzesiek.game.castles.Castle;
import com.grzesiek.game.screens.PlayScreen;
import com.grzesiek.game.player.Player;

/**
 * Created by Grzesiek on 2017-08-27.
 */

public class PlayerController
{
    private Viewport viewport;
    private OrthographicCamera cam;

    private Stage stage;
    private Table[] table;

    private Dragon game;
    private Player player;
    private PlayScreen screen;

    private Image imgUp;
    private Image imgDown;
    private Image imgLeft;
    private Image imgRight;

    private Image imgA;
    private Image imgB;
    private Image imgP;
    private Image imgI;
    private Image imgZoomOut;
    private Image imgM;
    private Image imgEndTurn;

    private boolean isMenuOpen;
    private boolean zoomedOut;

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    private float startX;
    private float startY;

    public PlayerController (Dragon game, final PlayScreen screen, final Player player)
    {
        this.game = game;
        this.screen = screen;
        this.player = player;
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, game.batch);

        isMenuOpen = false;
        zoomedOut = false;

        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;

        defineImages();

        table = new Table[2];
        //creating first table for movement buttons
        table[0] = new Table();
        table[0].left().bottom();

        //creating second table for A, B and the rest of buttons
        table[1] = new Table();
        table[1].right().bottom();
        table[1].setFillParent(true);

        showMoveControls();
        closeMenu();

        //listener for stage zoom out
        stage.addListener(new DragListener()
        {
            @Override
            public void dragStart (InputEvent event, float x, float y, int pointer)
            {
                super.dragStart(event, x, y, pointer);
                if(zoomedOut)
                {
                    startX = x;
                    startY = y;
                }
            }

            @Override
            public void drag (InputEvent event, float x, float y, int pointer)
            {
                super.drag(event, x, y, pointer);
                if(zoomedOut)
                {
                    if(isDragging())
                    {
                        screen.getGameCam().position.x += (startX - x);
                        screen.getGameCam().position.y += (startY - y);
                        startX = x;
                        startY = y;
                    }
                }
            }
        });
        //listeners for all images
        imgUp.addListener(new InputListener()
        {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                upPressed = false;
            }
        });
        imgDown.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                downPressed = false;
            }
        });
        imgLeft.addListener(new InputListener()
        {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                leftPressed = false;
            }
        });
        imgRight.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                rightPressed = false;
            }
        });
        imgA.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                player.getHero().setPressedA(true);
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                player.getHero().setPressedA(false);
            }
        });
        imgB.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(! isMenuOpen)
                {
                    openMenu();
                }
                else
                {
                    closeMenu();
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {

            }
        });
        imgP.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                player.getHeroInformation().update(player.getHero());
                player.getHeroInformation().setVisible(true);
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {

            }
        });
        imgI.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                player.getCastle().update(player.getHero());
                player.getCastle().setVisible(true);
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {

            }
        });
        imgZoomOut.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                if(!zoomedOut)
                {
                    zoomedOut = true;
                    screen.getGameCam().zoom += 0.5f;
                    zoomOut();
                }
                else
                {
                    zoomedOut = false;
                    screen.getGameCam().zoom = 1.0f;
                    openMenu();
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        imgM.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        imgEndTurn.addListener(new InputListener()
        {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                player.getHero().setMovementPoints(1000.0f);
                player.getCastle().setCanBuild(true);
                for(int i = 0; i < player.getCastle().getUnitRecruiter().length; i++)
                {
                    player.getCastle().getUnitRecruiter()[i].setUnitsAvailable(player.getCastle().getUnitRecruiter()[i].getUnit()[0].getGrowth());
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {

            }
        });

        stage.addActor(table[0]);
        stage.addActor(table[1]);
    }

    public void defineImages()
    {

        imgUp = new Image(new Texture("buttons/buttonUp.png"));
        imgDown = new Image(new Texture("buttons/buttonDown.png"));
        imgLeft = new Image(new Texture("buttons/buttonLeft.png"));
        imgRight = new Image(new Texture("buttons/buttonRight.png"));
        imgA = new Image(new Texture("buttons/buttonA.png"));
        imgB = new Image(new Texture("buttons/buttonB.png"));
        imgP = new Image(new Texture("buttons/buttonP.png"));
        imgI = new Image(new Texture("buttons/buttonI.png"));
        imgZoomOut = new Image(new Texture("buttons/buttonZoomOut.png"));
        imgM = new Image(new Texture("buttons/buttonM.png"));
        imgEndTurn = new Image(new Texture("buttons/buttonEndTurn.png"));

        imgUp.setSize(50, 50);
        imgDown.setSize(50, 50);
        imgLeft.setSize(50, 50);
        imgRight.setSize(50, 50);
        imgA.setSize(50, 50);
        imgB.setSize(50, 50);
        imgP.setSize(50, 50);
        imgI.setSize(50, 50);
        imgZoomOut.setSize(50, 50);
        imgM.setSize(50, 50);
        imgEndTurn.setSize(50, 50);
    }

    public void checkForInput ()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.W))
        {
            player.getHero().moveUp();
            closeMenu();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            player.getHero().moveDown();
            closeMenu();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            player.getHero().moveLeft();
            closeMenu();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            player.getHero().moveRight();
            closeMenu();
        }
        else if(upPressed)
        {
            if(!zoomedOut)
            {
                player.getHero().moveUp();
                closeMenu();
            }
            else
            {
                screen.getGameCam().position.y += 5;
            }
        }
        else if(downPressed)
        {
            if(!zoomedOut)
            {
                player.getHero().moveDown();
                closeMenu();
            }
            else
            {
                screen.getGameCam().position.y -= 5;
            }
        }
        else if(leftPressed)
        {
            if(!zoomedOut)
            {
                player.getHero().moveLeft();
                closeMenu();
            }
            else
            {
                screen.getGameCam().position.x -= 5;
            }
        }
        else if(rightPressed)
        {
            if(!zoomedOut)
            {
                player.getHero().moveRight();
                closeMenu();
            }
            else
            {
                screen.getGameCam().position.x += 5;
            }
        }
        else
        {
            player.getHero().stopMovement();
        }
        if(player.getCastle().isVisible() || player.getHeroInformation().isVisible())
        {
            table[0].setVisible(false);
            table[1].setVisible(false);
        }
        else
        {
            table[0].setVisible(true);
            table[1].setVisible(true);
        }
    }

    private void openMenu ()
    {
        isMenuOpen = true;

        table[1].clear();

        table[1].right().top();
        table[1].add();
        table[1].add(imgP).size(imgP.getWidth(), imgP.getHeight()).padRight(20).padBottom(10).padTop(120);
        table[1].row();
        table[1].add();
        table[1].add(imgI).size(imgI.getWidth(), imgI.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add(imgZoomOut).size(imgZoomOut.getWidth(), imgZoomOut.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add(imgM).size(imgM.getWidth(), imgM.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add(imgEndTurn).size(imgEndTurn.getWidth(), imgEndTurn.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add(imgB).size(imgB.getWidth(), imgB.getHeight()).padRight(20).padBottom(10);

    }

    private void closeMenu ()
    {
        isMenuOpen = false;

        table[1].clear();

        table[1].right().bottom();
        table[1].add(imgA).size(imgA.getWidth(), imgA.getHeight()).pad(10).padRight(20);
        table[1].row();
        table[1].add(imgB).size(imgB.getWidth(), imgB.getHeight()).padBottom(10).padRight(10);

        screen.getGameCam().zoom = 1;
    }

    private void zoomOut ()
    {
        table[1].clear();

        table[1].right().top();
        table[1].add();
        table[1].add().size(imgP.getWidth(), imgP.getHeight()).padRight(20).padBottom(10).padTop(120);
        table[1].row();
        table[1].add();
        table[1].add().size(imgI.getWidth(), imgI.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add(imgZoomOut).size(imgZoomOut.getWidth(), imgZoomOut.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add().size(imgM.getWidth(), imgM.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add().size(imgEndTurn.getWidth(), imgEndTurn.getHeight()).padRight(20).padBottom(10);
        table[1].row();
        table[1].add();
        table[1].add().size(imgB.getWidth(), imgB.getHeight()).padRight(20).padBottom(10);
    }

    private void showMoveControls ()
    {
        table[0].add();
        table[0].add(imgUp).size(imgUp.getWidth(), imgUp.getHeight());
        table[0].add();
        table[0].row().pad(5, 5, 5, 5);
        table[0].add(imgLeft).size(imgLeft.getWidth(), imgLeft.getHeight());
        table[0].add();
        table[0].add(imgRight).size(imgRight.getWidth(), imgRight.getHeight());
        table[0].row().padBottom(5);
        table[0].add();
        table[0].add(imgDown).size(imgDown.getWidth(), imgDown.getHeight());
        table[0].add();
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

    public boolean getZoomedOut ()
    {
        return zoomedOut;
    }

    public Stage getStage ()
    {
        return stage;
    }
}
