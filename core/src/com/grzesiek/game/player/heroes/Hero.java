package com.grzesiek.game.player.heroes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.grzesiek.game.Dragon;
import com.grzesiek.game.castles.UnitSlot;
import com.grzesiek.game.screens.PlayScreen;

/**
 * Created by Grzesiek on 2017-10-30.
 */

public abstract class Hero extends Sprite
{
    private enum State
    {
        STANDING_DOWN, STANDING_RIGHT, STANDING_LEFT, STANDING_UP, RUNNING_DOWN, RUNNING_RIGHT, RUNNING_LEFT, RUNNING_UP
    }

    private State previousState;
    private State currentState;

    protected World world;
    public Body b2body;

    private TextureRegion playerStandingDown;
    private TextureRegion playerStandingUp;
    private TextureRegion playerStandingRight;
    private TextureRegion playerStandingLeft;
    private Animation<TextureRegion> playerRunningRight;
    private Animation<TextureRegion> playerRunningLeft;
    private Animation<TextureRegion> playerRunningUp;
    private Animation<TextureRegion> playerRunningDown;

    protected Texture heroIconTexture;

    private float stateTimer;

    protected float playerSpeed;
    protected float playerMovementBooster;

    protected float movementPoints;
    protected float movementFactor;
    protected float defaultMovementFactor;

    protected float startingX;
    protected float startingY;
    protected float currentX;
    protected float currentY;

    private int boxWidth;
    private int boxHeight;
    private int boxOffsetX;
    private int boxOffsetY;

    protected int spriteWidth;
    protected int spriteHeight;

    protected int startingPosX;
    protected int startingPosY;

    protected boolean running;
    protected boolean pressedA;

    protected UnitSlot[] unitSlot;

    protected String name;

    protected int level;

    protected boolean inCastle;

    public Hero (PlayScreen screen, Sprite sprite)
    {
        super(sprite);
        this.world = screen.getWorld();




        unitSlot = new UnitSlot[7];
        for(int i = 0; i < unitSlot.length; i++)
        {
            unitSlot[i] = new UnitSlot();
        }
        inCastle = true;





        currentState = State.STANDING_DOWN;
        previousState = State.STANDING_DOWN;

        stateTimer = 0;

        boxWidth = 8;
        boxHeight = 3;
        boxOffsetX = 0;
        boxOffsetY = - 13;

        spriteWidth = 16;
        spriteHeight = 32;

        createAnimations();
        setRegion(playerStandingDown);
    }

    protected void createAnimations ()
    {
        //create frames and add them to animation for running in all directions
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 2; i < 6; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * spriteWidth, 0, spriteWidth, spriteHeight));
        }
        playerRunningDown = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 8; i < 12; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * spriteWidth, 0, spriteWidth, spriteHeight));
        }
        playerRunningRight = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 13; i < 17; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * spriteWidth, 0, spriteWidth, spriteHeight));
        }
        playerRunningUp = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 20; i < 24; i++)
        {
            frames.add(new TextureRegion(getTexture(), i * spriteWidth, 0, spriteWidth, spriteHeight));
        }
        playerRunningLeft = new Animation(0.2f, frames);
        frames.clear();

        //create frames and add them to animation for standing in all directions
        playerStandingDown = new TextureRegion(getTexture(), 0, 0, spriteWidth, spriteHeight);
        playerStandingRight = new TextureRegion(getTexture(), 6 * spriteWidth, 0, spriteWidth, spriteHeight);
        playerStandingLeft = new TextureRegion(getTexture(), 18 * spriteWidth, 0, spriteWidth, spriteHeight);
        playerStandingUp = new TextureRegion(getTexture(), 12 * spriteWidth, 0, spriteWidth, spriteHeight);
    }

    protected void defineHero ()
    {
        //creating body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(startingPosX, startingPosY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boxWidth, boxHeight, new Vector2(boxOffsetX, boxOffsetY), 0);
        fixtureDef.filter.categoryBits = Dragon.HERO_BIT;
        fixtureDef.filter.maskBits = Dragon.OBSTACLES_BIT | Dragon.DOORS_BIT | Dragon.WOOD_BIT | Dragon.STONE_BIT | Dragon.CRYSTAL_BIT | Dragon.DEBOIL_BIT | Dragon.GOLD_BIT;
        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef).setUserData(this);

        startingX = b2body.getPosition().x;
        startingY = b2body.getPosition().y;
    }

    public void update (float dt)
    {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        currentX = b2body.getPosition().x;
        currentY = b2body.getPosition().y;

        if(currentX - startingX >= 1 || currentX - startingX <= - 1 || currentY - startingY >= 1 || currentY - startingY <= - 1)
        {
            movementPoints -= movementFactor;
            startingX = currentX;
            startingY = currentY;
        }
    }

    public TextureRegion getFrame (float dt)
    {
        currentState = getState();

        TextureRegion textureRegion;
        switch(currentState)
        {
            case STANDING_DOWN:
            default:
                textureRegion = playerStandingDown;
                break;
            case STANDING_RIGHT:
                textureRegion = playerStandingRight;
                break;
            case STANDING_LEFT:
                textureRegion = playerStandingLeft;
                break;
            case STANDING_UP:
                textureRegion = playerStandingUp;
                break;
            case RUNNING_DOWN:
                textureRegion = playerRunningDown.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_RIGHT:
                textureRegion = playerRunningRight.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_LEFT:
                textureRegion = playerRunningLeft.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_UP:
                textureRegion = playerRunningUp.getKeyFrame(stateTimer, true);
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return textureRegion;
    }

    public State getState ()
    {
        if(b2body.getLinearVelocity().x > 0)
        {
            return State.RUNNING_RIGHT;
        }
        else if(b2body.getLinearVelocity().x < 0)
        {
            return State.RUNNING_LEFT;
        }
        else if(b2body.getLinearVelocity().y > 0)
        {
            return State.RUNNING_UP;
        }
        else if(b2body.getLinearVelocity().y < 0)
        {
            return State.RUNNING_DOWN;
        }
        else if(b2body.getLinearVelocity().x == 0 && previousState.equals(State.RUNNING_RIGHT))
        {
            return State.STANDING_RIGHT;
        }
        else if(b2body.getLinearVelocity().x == 0 && previousState.equals(State.RUNNING_LEFT))
        {
            return State.STANDING_LEFT;
        }
        else if(b2body.getLinearVelocity().x == 0 && previousState.equals(State.RUNNING_UP))
        {
            return State.STANDING_UP;
        }
        else if(b2body.getLinearVelocity().x == 0 && previousState.equals(State.RUNNING_DOWN))
        {
            return State.STANDING_DOWN;
        }
        else
        { return previousState; }
    }

    public void moveUp ()
    {
        if(movementPoints >= movementFactor)
        {
            if(b2body.getLinearVelocity().x < playerSpeed && ! running)
            {
                b2body.applyLinearImpulse(new Vector2(0, playerSpeed), b2body.getWorldCenter(), true);
                b2body.applyLinearImpulse(new Vector2(0, playerMovementBooster), b2body.getWorldCenter(), true);
                inCastle = false;
            }
        }
        else
        {
            stopMovement();
        }
    }

    public void moveDown ()
    {
        if(movementPoints >= movementFactor)
        {
            if(b2body.getLinearVelocity().x < playerSpeed && ! running)
            {
                b2body.applyLinearImpulse(new Vector2(0, - playerSpeed), b2body.getWorldCenter(), true);
                b2body.applyLinearImpulse(new Vector2(0, - playerMovementBooster), b2body.getWorldCenter(), true);
                inCastle = false;
            }
        }
        else
        {
            stopMovement();
        }
    }

    public void moveLeft ()
    {
        if(movementPoints >= movementFactor)
        {
            if(b2body.getLinearVelocity().x < playerSpeed && ! running)
            {
                b2body.applyLinearImpulse(new Vector2(- playerSpeed, 0), b2body.getWorldCenter(), true);
                b2body.applyLinearImpulse(new Vector2(- playerMovementBooster, 0), b2body.getWorldCenter(), true);
                inCastle = false;
            }
        }
        else
        {
            stopMovement();
        }
    }

    public void moveRight ()
    {
        if(movementPoints >= movementFactor)
        {
            if(b2body.getLinearVelocity().x < playerSpeed && ! running)
            {
                b2body.applyLinearImpulse(new Vector2(playerSpeed, 0), b2body.getWorldCenter(), true);
                b2body.applyLinearImpulse(new Vector2(playerMovementBooster, 0), b2body.getWorldCenter(), true);
                inCastle = false;
            }
        }
        else
        {
            stopMovement();
        }
    }

    public void stopMovement ()
    {
        b2body.setLinearVelocity(0, 0);
        startingX = currentX;
        startingY = currentY;
    }

    public void dispose ()
    {
        world.dispose();
    }

    public void adjustMovementPoints(float movementPoints)
    {
        this.movementPoints +=movementPoints;
    }

    public void setHeroSpeed (float speed)
    {
        this.playerSpeed = speed;
    }

    public void setMovementPoints (float movementPoints)
    {
        this.movementPoints = movementPoints;
    }

    public void setPressedA (boolean pressedA)
    {
        this.pressedA = pressedA;
    }

    public boolean isPressedA ()
    {
        return pressedA;
    }

    public Texture getHeroIconTexture ()
    {
        return heroIconTexture;
    }

    public float getHeroSpeed ()
    {
        return playerSpeed;
    }

    public float getMovementPoints ()
    {
        return movementPoints;
    }

    public UnitSlot[] getUnitSlot()
    {
        return unitSlot;
    }

    public String getName()
    {
        return name;
    }

    public int getLevel()
    {
        return level;
    }

    public boolean isInCastle()
    {
        return inCastle;
    }

    public void setInCastle(boolean inCastle)
    {
        this.inCastle = inCastle;
    }
}
