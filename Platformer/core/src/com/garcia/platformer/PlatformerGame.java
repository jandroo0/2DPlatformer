package com.garcia.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.garcia.platformer.Screens.GameScreen;
import com.garcia.platformer.Util.Assets;

public class PlatformerGame extends Game {

	public SpriteBatch batch;
	
	@Override
	public void create () {

		// sprite batch
		batch = new SpriteBatch();

		// asset manager
		Assets.instance.init(new AssetManager());

		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
