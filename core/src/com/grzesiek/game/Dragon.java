package com.grzesiek.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.grzesiek.game.screens.PlayScreen;

public class Dragon extends Game
{
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 240;

	public static final short HERO_BIT = 1;
	public static final short OBSTACLES_BIT = 2;
	public static final short DOORS_BIT = 4;
	public static final short WOOD_BIT = 8;
	public static final short STONE_BIT = 16;
	public static final short CRYSTAL_BIT = 32;
	public static final short DEBOIL_BIT = 64;
	public static final short GOLD_BIT = 124;
	public static final short REMOVED_STOCK_BIT = 248;

	public static boolean debugMode;

	public SpriteBatch batch;

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		debugMode = false;
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}

	@Override
	public void dispose ()
	{
		super.dispose();
		batch.dispose();
	}
}