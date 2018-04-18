package protocol.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class FixedActor extends Actor {
	Texture texture;

	public FixedActor(String textureName) {
		texture = new Texture(Gdx.files.internal(textureName));
		setBounds(actorX, actorY, texture.getWidth(), texture.getHeight());
	}

	public float getActorX() {
		return actorX;
	}

	public void setActorX(float actorX) {
		this.actorX = actorX;
	}

	public float getActorY() {
		return actorY;
	}

	public void setActorY(float actorY) {
		this.actorY = actorY;
	}

	float actorX = 0;
	float actorY = 0;
	public boolean started = false;

	public FixedActor() {
		setBounds(actorX, actorY, texture.getWidth(), texture.getHeight());
	}

	@Override
	public void draw(Batch batch, float alpha) {

		batch.draw(texture, actorX, actorY);
	}

	// @Override
	// public void act(float delta) {
	// if (started) {
	// actorY += 5;
	// }
	// }
}