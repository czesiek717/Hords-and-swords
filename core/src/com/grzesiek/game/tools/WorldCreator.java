package com.grzesiek.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.grzesiek.game.Dragon;
import com.grzesiek.game.objects.Crystal;
import com.grzesiek.game.objects.Deboil;
import com.grzesiek.game.objects.Gold;
import com.grzesiek.game.objects.Stone;
import com.grzesiek.game.objects.Wood;
import com.grzesiek.game.screens.PlayScreen;

/**
 * Created by Grzesiek on 2017-08-29.
 */

public class WorldCreator
{
    public WorldCreator(PlayScreen screen)
    {
        World world = screen.getWorld();
        BodyDef bodyDef = new BodyDef();
        TiledMap map = screen.getMap();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //creating obstacles
        for(MapObject object : map.getLayers().get("Structures").getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = Dragon.OBSTACLES_BIT;
            body.createFixture(fixtureDef);
        }
        //creating doors
        for(MapObject object : map.getLayers().get("Doors").getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = Dragon.DOORS_BIT;
            body.createFixture(fixtureDef);
        }
        //creating wood
        for(MapObject object : map.getLayers().get("Wood").getObjects().getByType(RectangleMapObject.class))
        {
            new Wood(screen, object);
        }
        //creating stone
        for(MapObject object : map.getLayers().get("Stone").getObjects().getByType(RectangleMapObject.class))
        {
            new Stone(screen, object);
        }
        //creating crystal
        for(MapObject object : map.getLayers().get("Crystal").getObjects().getByType(RectangleMapObject.class))
        {
            new Crystal(screen, object);
        }
        //creating deboil
        for(MapObject object : map.getLayers().get("Deboil").getObjects().getByType(RectangleMapObject.class))
        {
            new Deboil(screen, object);
        }
        //creating gold
        for(MapObject object : map.getLayers().get("Gold").getObjects().getByType(RectangleMapObject.class))
        {
            new Gold(screen, object);
        }

        //demo for polygonal shapes
        /*for(MapObject object : map.getLayers().get(4).getObjects().getByType(PolygonMapObject.class))
        {
            Polygon polygon = ((PolygonMapObject) object).getPolygon();

            float max = 0;
            float min = 0;

            float[] vertices = polygon.getVertices();

            for(int i = 0; i < vertices.length; i++)
            {
                if(vertices[i] > max)
                {
                    max = vertices[i];
                }
                if(vertices[i] < min)
                {
                    min = vertices[i];
                }
            }

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(polygon.getX(), polygon.getY());

            body = world.createBody(bodyDef);

            shape.set(polygon.getVertices());
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = Dragon.FOREGROUND_BIT;
            body.createFixture(fixtureDef);
        }*/
    }
}
