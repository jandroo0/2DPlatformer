package com.garcia.platformer.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.garcia.platformer.PlatformerGame;
import com.garcia.platformer.Screens.GameScreen;
import com.garcia.platformer.Util.Assets;
import com.garcia.platformer.Util.Constants;

public class Player extends Sprite {
    GameScreen screen;

    public enum State {WALKING, DUCKING, FALLING, HANGING, CLIMBING, STANDING, HOLDING, HURT, IDLE, KICKING, ATTACKING, JUMPING}
    public State currentState, previousState;
    float stateTimer;
    boolean runningRight;

    public World world;
    public Body b2body;

    private float vel = 1f;

    private Assets.PlayerAsset playerAsset;

    public Player(GameScreen screen) {
        super(Assets.instance.playerAsset.texture);
        this.world = screen.getWorld();
        this.screen = screen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        playerAsset = Assets.instance.playerAsset;

        stateTimer = 0;
        runningRight = true;

        definePlayer();
        setBounds(0, 0 , 80 / Constants.PPM, 110 / Constants.PPM);

        setRegion(playerAsset.playerStand);
    }

    // player input
    public void handleInput(float deltaTime) {

        // attacking
        

        // up
        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            b2body.applyLinearImpulse(new Vector2(0, 4), b2body.getWorldCenter(), true);
        // right
        if (Gdx.input.isKeyPressed(Input.Keys.D) && b2body.getLinearVelocity().x <= 1.5f)
            b2body.applyLinearImpulse(new Vector2(getVel(), 0), b2body.getWorldCenter(), true);
        // left
        if (Gdx.input.isKeyPressed(Input.Keys.A) && b2body.getLinearVelocity().x >= -1.5f)
            b2body.applyLinearImpulse(new Vector2(-getVel(), 0), b2body.getWorldCenter(), true);

    }

    public void update(float deltaTime) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() * 0.1f);
        setRegion(getFrame(deltaTime));
    }

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;

        switch (currentState) {
            case JUMPING:
                region = playerAsset.playerJump;
                break;
            case FALLING:
                region = playerAsset.playerFall;
                break;
            case DUCKING:
                region = playerAsset.playerDuck;
                break;
            case WALKING:
                region = playerAsset.playerWalk.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = playerAsset.playerStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(200 / Constants.PPM, 300 / Constants.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / Constants.PPM);

        fdef.filter.categoryBits = Constants.PLAYER_BIT; // collision bit
        fdef.filter.maskBits = Constants.GROUND_BIT; // compatible collision bits

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Constants.PPM, 80 / Constants.PPM), new Vector2(2 / Constants.PPM, 80 / Constants.PPM));
        fdef.shape = head;
        fdef.filter.categoryBits = Constants.PLAYER_HEAD_BIT; // collision bit
        fdef.filter.maskBits = Constants.BORDER_BIT; // compatible collision bits

        b2body.createFixture(fdef).setUserData(this); // for collisions
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) // if going up as well as falling after a jump
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.WALKING;
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
            return State.DUCKING;
        else
            return State.STANDING;
    }

    public float getVel() {
        return vel;
    }
}
