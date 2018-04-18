package protocol.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class DraggableActor extends Actor {

	Texture texture = new Texture(Gdx.files.internal("droplet.png"));

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

	public DraggableActor(String textureName) {
		texture = new Texture(Gdx.files.internal(textureName));
		{
			setBounds(actorX, actorY, texture.getWidth(), texture.getHeight());
			addListener(new DragListener() {
				// public boolean touchDown(InputEvent event, float x, float y, int pointer, int
				// button) {
				// Gdx.app.log("Example", "touch started at (" + x + ", " + y + ")");
				// return super.touchDown(event, x, y, pointer, button);
				// }
				//
				// public void touchUp(InputEvent event, float x, float y, int pointer, int
				// button) {
				// Gdx.app.log("Example", "touch done at (" + x + ", " + y + ")");
				// }

				public void drag(InputEvent event, float x, float y, int pointer) {
					// Gdx.app.log("Example", "drag at (" + x + ", " + y + ")");
					// actorX = event.getStageX() ;//getDeltaX();
					// actorY = event.getStageY() ;//getDeltaY();
					actorX -= getDeltaX();
					actorY -= getDeltaY();
				}

				public void dragStop(InputEvent event, float x, float y, int pointer) {
					setBounds(actorX, actorY, texture.getWidth(), texture.getHeight());
				}
			});
		}
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
