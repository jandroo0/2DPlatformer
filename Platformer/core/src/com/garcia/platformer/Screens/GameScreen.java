package com.garcia.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.garcia.platformer.Objects.Player;
import com.garcia.platformer.PlatformerGame;
import com.garcia.platformer.Util.B2WorldCreator;
import com.garcia.platformer.Util.Constants;
import com.garcia.platformer.Util.WorldContactListener;

public class GameScreen implements Screen {

    private PlatformerGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    // box2d vars
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public int mapWidth, mapHeight, tilePixelWidth, tilePixelHeight, mapPixelWidth, mapPixelHeight;

    // player
    private Player player;

    public GameScreen(PlatformerGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.V_WIDTH / Constants.PPM, Constants.v_HEIGHT /  Constants.PPM, camera);

        loadMap();

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); // center camera

        world = new World(new Vector2(0, -8), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Player(this);

        world.setContactListener(new WorldContactListener());
    }

    public void loadMap() {
        mapLoader = new TmxMapLoader();
        map =  mapLoader.load("tilemap/levels/level01.tmx"); // change for future levels
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);

        MapProperties prop = map.getProperties();

        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        tilePixelWidth = prop.get("tilewidth", Integer.class);
        tilePixelHeight = prop.get("tileheight", Integer.class);

        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;
    }

    public void update(float deltaTime) {
        //player input
        player.handleInput(deltaTime);

        // takes 1 step in the physics simulation (60 times per second)
        world.step(1 / 60f, 6, 2);

        // update player
        player.update(deltaTime);

        camera.position.x = player.b2body.getPosition().x;

        if(camera.position.x <= viewport.getWorldWidth() / 2) // if distance between the start of map and camera.x is < half the screen width
            camera.position.x = viewport.getWorldWidth() / 2;
        else if (camera.position.x >= mapWidth - (viewport.getWorldWidth() + 12)){ // if distance between end of map and camera.x < half the screen width
            camera.position.x = mapWidth - (viewport.getWorldWidth() + 12);
        }

        camera.update();
        renderer.setView(camera);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(); // render game map

        b2dr.render(world, camera.combined); // box2d lines

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin(); // begin batch

        player.draw(game.batch); // draw player

        game.batch.end(); // end batch


    }

    public World getWorld() {
        return world;
    }
    public TiledMap getMap() {return map;}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
