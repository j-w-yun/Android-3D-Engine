package com.example.game;

import com.example.android3dengine.Camera;
import com.example.android3dengine.Input;
import com.example.android3dengine.MainActivity;
import com.example.android3dengine.Mesh;
import com.example.android3dengine.R;
import com.example.android3dengine.Shader;
import com.example.android3dengine.Time;
import com.example.android3dengine.math.Transform;
import com.example.android3dengine.math.Vector2f;
import com.example.android3dengine.math.Vector3f;
import com.example.game.rope.Rope;

import android.util.Log;

public class Game2 {

	private Camera camera;

	private final double MAX_UPDATES_PER_SECOND = 60.0; // Maximum frames to be rendered per second
	private final double MIN_SECONDS_PER_UPDATE = 1.0 / MAX_UPDATES_PER_SECOND;
	private long previousTime;
	private double unprocessedTime;

	private int fps; // Keep track of rendered frames per second
	private double fpsTime; // To observe passing of a second

	private Transform circleTransform;
	private Transform playerTransform;
	private Mesh circleMesh;
	private Shader shader;

	//	private Texture texture;

	private Rope rope;

	/*
	 * Initialize components
	 */
	public Game2() {
		// Initialize camera
		//		camera = new Camera(70, Transform.width / Transform.height, 0.01f, 1000);
		camera = new Camera();
		Transform.setCamera(camera);

		String vertexShaderCode = MainActivity.Utility.loadShaderString(R.raw.basic_vertex);
		String fragmentShaderCode = MainActivity.Utility.loadShaderString(R.raw.basic_fragment);
		shader = new Shader(vertexShaderCode, fragmentShaderCode);

		circleMesh = MainActivity.Utility.loadMesh(R.raw.filledrect);

		circleTransform = new Transform();
		playerTransform = new Transform();

		//		texture = MainActivity.Utility.loadTexture(R.raw.tex2);

		rope = new Rope(new Vector2f(0, 0), 100f, 10, 400f);
	}

	/*
	 * Update components with a set amount of updates per second via game loop
	 */
	public void loopOnce() {
		if(previousTime == 0)
			previousTime = System.nanoTime();

		long startTime = System.nanoTime();
		long passedTime = startTime - previousTime;
		previousTime = startTime;

		unprocessedTime += (double) passedTime / Time.NANOSECONDS;

		// Update to relative game timer
		while(unprocessedTime > MIN_SECONDS_PER_UPDATE) {
			unprocessedTime -= MIN_SECONDS_PER_UPDATE;

			// Update components and process input
			update((float) MIN_SECONDS_PER_UPDATE);
		}

		// Reduce CPU load
		if(unprocessedTime <= MIN_SECONDS_PER_UPDATE) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Log.e("ANDROID 3D ENGINE", "Thread interrupted at Game2#update");
				Log.e("ANDROID 3D ENGINE", "error", e);
			}
		}

		render();
	}

	Vector2f lastTouch = new Vector2f(0, 0);
	float rotation = 0;

	private void update(float delta) {
		// Update camera direction and coordinates
		camera.input(delta);

		//		if(lastTouch.getX() != Input.getCurrentTapTouch(0).getX()) {

		Vector2f bottomNodePos = rope.getNodes()[rope.getNumNodes() - 1].getPosition();
		rope.getNodes()[rope.getNumNodes() - 1].immobile = true;
		bottomNodePos.setX(Input.getCurrentMovingTouch(0).getX());
		bottomNodePos.setY(Input.getCurrentMovingTouch(0).getY());

		rope.update(0.01f);

		//		lastTouch = Input.getCurrentTapTouch(0);
	}

	/*
	 * Render game components
	 */
	private void render() {
		Transform.setCamera(camera);



		shader.bind();
		//		texture.bind();

		circleTransform.setScale(0.03f * (Transform.height / Transform.width), 0.03f, 0.03f);
		playerTransform.setScale(0.09f * (Transform.height / Transform.width), 0.09f, 0.09f);

		for(int j = 0; j < rope.getNumNodes(); j++) {
			//			if(j != 0) {
			//				Vector2f thisPosition = rope.getNodes()[j].getPosition();
			//				Vector2f lastPosition = rope.getNodes()[j - 1].getPosition();
			//				Vector2f deltaPosition = lastPosition.sub(thisPosition);
			//
			//				int numFillers = 5;
			//
			//				Vector2f temp = deltaPosition.div(numFillers);
			//				Vector2f drawPosition = thisPosition;
			//
			//				for(int k = 0; k < numFillers; k++) {
			//					Vector2f touch = new Vector2f(
			//							((drawPosition.getX() - (Transform.width / 2)) / Transform.width * 2),
			//							-(drawPosition.getY() - (Transform.height / 2)) / Transform.height * 2);
			//					circleTransform.setTranslation(touch.getX(), touch.getY(), 0.1f);
			//
			//					shader.setUniform("transform", circleTransform.getTransformation());
			//					shader.setUniform("color", new Vector3f(1, 1, 1));
			//
			//					circleMesh.draw();
			//
			//
			//					drawPosition = drawPosition.add(temp);
			//				}
			//		}

			if(j == 0) {
				Vector2f touch = new Vector2f(
						((rope.getNodes()[j].getPosition().getX() - (Transform.width / 2)) / Transform.width * 2),
						-(rope.getNodes()[j].getPosition().getY() - (Transform.height / 2)) / Transform.height * 2);
				playerTransform.setTranslation(touch.getX(), touch.getY(), 0.1f);

				shader.setUniform("transform", playerTransform.getTransformation());
				shader.setUniform("color", new Vector3f(1, 0, 0));

				circleMesh.draw();

			} else {
				Vector2f touch = new Vector2f(
						((rope.getNodes()[j].getPosition().getX() - (Transform.width / 2)) / Transform.width * 2),
						-(rope.getNodes()[j].getPosition().getY() - (Transform.height / 2)) / Transform.height * 2);
				circleTransform.setTranslation(touch.getX(), touch.getY(), 0.1f);

				shader.setUniform("transform", circleTransform.getTransformation());
				shader.setUniform("color", new Vector3f(1, 1, 1));

				circleMesh.draw();
			}
		}



		// FPS counter
		if(fpsTime + 1 < (double) System.nanoTime() / Time.NANOSECONDS) {
			Log.i("ANDROID 3D ENGINE", "FPS: " + Integer.toString(fps));
			fpsTime = (double) System.nanoTime() / Time.NANOSECONDS;
			fps = 0;
		}
		fps++;
	}
}