package com.grzesiek.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.grzesiek.game.Dragon;
import com.grzesiek.game.controllers.PlayerController;
import com.grzesiek.game.HUD.HUD;
import com.grzesiek.game.player.Player;
import com.grzesiek.game.tools.WorldContactListener;
import com.grzesiek.game.tools.WorldCreator;

/**
 * Created by Grzesiek on 2017-08-23.
 */

public class PlayScreen implements Screen
{
    private Dragon game;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;

    private PlayerController controller;
    private HUD hud;

    private WorldCreator worldCreator;
    private WorldContactListener contactListener;

    private float counter;

    public PlayScreen (Dragon game)
    {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(game.V_WIDTH, game.V_HEIGHT, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level_1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gameCam.position.set(0, 0, 0);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        worldCreator = new WorldCreator(this);

        player = new Player(this, game.batch);

        controller = new PlayerController(game, this, player);
        hud = new HUD(game);
        contactListener = new WorldContactListener(player);

        world.setContactListener(contactListener);

        counter = 0;
    }

    public void update (float dt)
    {
        world.step(1 / 60f, 6, 2);

        sweepDeadBodies();

        player.getHero().update(dt);
        controller.checkForInput();

        if(! controller.getZoomedOut())
        {
            gameCam.position.x = player.getHero().b2body.getPosition().x;
            gameCam.position.y = player.getHero().b2body.getPosition().y;
        }

        gameCam.update();
        renderer.setView(gameCam);

        hud.updateStockLabels(player.getWood(), player.getStone(), player.getCrystal(), player.getDeboil(), player.getGold());
        hud.updateUnitLabels(player.getUnit1(), player.getUnit2(), player.getUnit3(), player.getUnit4(), player.getUnit5());
        hud.updateMovementTable(player.getHero().getMovementPoints());


        counter += dt;
        if(counter >= 5)
        {
            for(int i = 0; i < player.getHero().getUnitSlot().length; i++)
            {
                if(player.getHero().getUnitSlot()[i].getUnit() != null)
                {
                    Gdx.app.log(String.valueOf(i), String.valueOf(player.getHero().getUnitSlot()[i].getUnit().getName()));
                    Gdx.app.log(String.valueOf(i), String.valueOf(player.getHero().getUnitSlot()[i].getUnit().getQuantity()));
                }
            }
            counter = 0;
            Gdx.app.log("-------------", "-------------");
            Gdx.app.log("isHeroInCastle" , String.valueOf(player.getHero().isInCastle()));
        }
    }

    private void sweepDeadBodies ()
    {
        Array<Body> bodies = contactListener.getBodiesToRemove();
        for(int i = 0; i < bodies.size; i++)
        {
            Body body = bodies.get(i);
            world.destroyBody(body);
        }
        bodies.clear();
    }

    @Override
    public void show ()
    {

    }

    @Override
    public void render (float delta)
    {
        update(delta);

        //render map
        renderer.render();

        //render Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        //render player
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.getHero().draw(game.batch);
        game.batch.end();
        controller.draw();
        hud.draw();
        if(player.getCastle().isVisible())
        {
            player.getCastle().draw();
        }
        else if(player.getHeroInformation().isVisible())
        {
            player.getHeroInformation().draw();
        }
        else
        {
            Gdx.input.setInputProcessor(controller.getStage());
        }
    }

    @Override
    public void resize (int width, int height)
    {
        gamePort.update(width, height);
        controller.resize(width, height);
        hud.resize(width, height);
        player.getCastle().resize(width, height);
    }

    @Override
    public void pause ()
    {

    }

    @Override
    public void resume ()
    {

    }

    @Override
    public void hide ()
    {

    }

    @Override
    public void dispose ()
    {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        player.dispose();
        game.dispose();
        controller.dispose();
        hud.dispose();
        contactListener.dispose();
    }

    public World getWorld ()
    {
        return world;
    }

    public TiledMap getMap ()
    {
        return map;
    }

    public OrthographicCamera getGameCam ()
    {
        return gameCam;
    }
}
