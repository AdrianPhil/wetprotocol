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

		DraggableActor myActor1 = new DraggableActor("bluepipettetransparent.png");
		myActor1.setTouchable(Touchable.enabled);
		DraggableActor myActor2 = new DraggableActor("bluepipettetransparent.png");
		myActor2.setTouchable(Touchable.enabled);
		myActor2.setActorX(100);
		myActor2.setActorY(100);

		// MoveToAction moveAction = new MoveToAction();
		// moveAction.setPosition(300f, 0f);
		// moveAction.setDuration(10f);
		// myActor.addAction(moveAction);

		FixedActor rack = new FixedActor("rack.png");
		rack.setActorX(stage.getWidth() - rack.getWidth());
		rack.setActorY(stage.getHeight() - rack.getHeight());
		stage.addActor(rack);

		stage.addActor(myActor1);
		stage.addActor(myActor2);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);// white backgound (255/255)
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
}