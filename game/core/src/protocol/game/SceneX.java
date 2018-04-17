package protocol.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class SceneX implements ApplicationListener {

	private Stage stage;

	@Override
	public void create() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		MyActor myActor1 = new MyActor();
		myActor1.setTouchable(Touchable.enabled);
		MyActor myActor2 = new MyActor();
		myActor2.setTouchable(Touchable.enabled);
		myActor2.setActorX(100);
		myActor2.setActorY(100);

		// MoveToAction moveAction = new MoveToAction();
		// moveAction.setPosition(300f, 0f);
		// moveAction.setDuration(10f);
		// myActor.addAction(moveAction);

		stage.addActor(myActor1);
		stage.addActor(myActor2);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public class MyActor extends Actor {
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

		public MyActor() {
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
					//Gdx.app.log("Example", "drag at (" + x + ", " + y + ")");
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
}