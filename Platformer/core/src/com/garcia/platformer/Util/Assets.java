package com.garcia.platformer.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.garcia.platformer.Objects.Player;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    //    public AssetFonts fonts;
//    public AssetSounds sounds;
//    public AssetMusic music;
    public PlayerAsset playerAsset;
    private AssetManager assetManager;

    // singleton: prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);

        // load texture atlas
        assetManager.load("gfx/players/PNG/Adventurer/adventurer_tilesheet.png", Texture.class); // player tilesheet

        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

//        TextureAtlas atlas = assetManager.get("sprites/texture_atlas.atlas");
//        // enable texture filtering for pixel smoothing
//        for (Texture t : atlas.getTextures()) {
//            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        }

        // create game resource objects
//        sounds = new AssetSounds(assetManager);
//        music = new AssetMusic(assetManager);
        playerAsset = new PlayerAsset(assetManager);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }


//    // fonts
//    public class AssetFonts {
//        public final BitmapFont titleFont;
//        public final BitmapFont hudFont;
//        public final BitmapFont buttonFont;
//        public final BitmapFont statFont;
//        public final BitmapFont miniFont;
//
//        public AssetFonts() {
//            //  default font parameters ------
//            FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GalaxyFont.otf"));
//            FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
//
//            fontParameters.size = 72;
//            fontParameters.borderWidth = 3.6f;
//            fontParameters.color = new Color(1, 1, 1, 0.3f);
//            fontParameters.borderColor = new Color(0, 0, 0, 0.3f);
//            // ------
//
//            // font settings ------
//            titleFont = fontGenerator.generateFont(fontParameters);// generate HUD font with paramater
//            hudFont = fontGenerator.generateFont(fontParameters);
//            buttonFont = fontGenerator.generateFont(fontParameters);
//            statFont = fontGenerator.generateFont(fontParameters);
//            miniFont = fontGenerator.generateFont(fontParameters);
//
//            titleFont.getData().setScale(0.11f);
//            hudFont.getData().setScale(0.08f);
//            buttonFont.getData().setScale(0.09f);
//            statFont.getData().setScale(0.07f);
//            miniFont.getData().setScale(0.03f);
//
//            titleFont.setUseIntegerPositions(false);
//            titleFont.getData().markupEnabled = true; // html markup
//            titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//
//            hudFont.setUseIntegerPositions(false);
//            hudFont.getData().markupEnabled = true; // html markup
//            hudFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//
//            buttonFont.setUseIntegerPositions(false);
//            buttonFont.getData().markupEnabled = true; // html markup
//            buttonFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//
//            statFont.setUseIntegerPositions(false);
//            statFont.getData().markupEnabled = true; // html markup
//            statFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//
//            miniFont.setUseIntegerPositions(false);
//            miniFont.getData().markupEnabled = true; // html markup
//            miniFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//
//            // -------------------------------
//        }
//    }

    public class PlayerAsset {
        public final Texture texture;

        public final TextureRegion playerStand;
        public final TextureRegion playerJump;
        public final TextureRegion playerFall;
        public final TextureRegion playerDuck;
//            public final TextureRegion playerHurt;
//            public final TextureRegion playerHold;

        public final Animation<TextureRegion> playerWalk;
//            public final Animation<TextureRegion> playerAttack;

        public PlayerAsset(AssetManager manager) {
            texture = assetManager.get("gfx/players/PNG/Adventurer/adventurer_tilesheet.png");
            Array<TextureRegion> frames = new Array<>();

            // walking animation
            for (int i = 0; i < 2; i++)
                frames.add(new TextureRegion(texture, i * 80, 110, 80, 110));
            playerWalk = new Animation<TextureRegion>(0.1f, frames);
            frames.clear();

            // player standing
            playerStand = new TextureRegion(texture, 400, 220, 80, 110);

            // player jumping
            playerJump = new TextureRegion(texture, 160, 220, 80, 110);

            // player falling
            playerFall = new TextureRegion(texture, 160, 0, 80, 110);

            // player ducking
            playerDuck = new TextureRegion(texture, 240, 0, 80, 110);
        }
    }


//    public class AssetCoin {
//        Texture texture;
//        public final Animation<TextureRegion> coinAnimation;
//        private float coinTimer;
//
//        TextureRegion[][] textureRegion2D;
//        TextureRegion[] textureRegion1D;
//
//        public float x, y;
//        float totalAnimationTime;
//
//        public AssetCoin(AssetManager assetManager) {
//            texture = assetManager.get("sprites/effects/coin_01.png", Texture.class);
//
//            totalAnimationTime = 0.8f;
//
//            // split texture
//            textureRegion2D = TextureRegion.split(texture, 120, 120);
//
//            // convert to 1D array
//            textureRegion1D = new TextureRegion[8];
//
//            int index = 0;
//
//            for (int j = 0; j < 8; j++) { //column
//                textureRegion1D[index] = textureRegion2D[0][j];
//                index++;
//            }
//
//            coinAnimation = new Animation<TextureRegion>(totalAnimationTime / 8, textureRegion1D);
//            coinAnimation.setPlayMode(Animation.PlayMode.LOOP);
//            coinTimer = 0;
//        }
//
//        public void update(float deltaTime) {
//            coinTimer += deltaTime;
//        }
//
//        public void setPos(float x, float y) {
//            this.x = x;
//            this.y = y;
//        }
//
//        public void draw(Batch batch) {
//            batch.draw(coinAnimation.getKeyFrame(coinTimer),
//                    x, y, 5, 6);
//        }
//    }

}





































