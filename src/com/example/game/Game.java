package com.example.game;

import com.example.android3dengine.Camera;
import com.example.android3dengine.Input;
import com.example.android3dengine.MainActivity;
import com.example.android3dengine.Mesh;
import com.example.android3dengine.R;
import com.example.android3dengine.Shader;
import com.example.android3dengine.Texture;
import com.example.android3dengine.Time;
import com.example.android3dengine.math.Transform;
import com.example.android3dengine.math.Vector2f;
import com.example.game.circle.MassiveCircle;

import android.util.Log;

public class Game {

	private Camera camera;

	private final double MAX_UPDATES_PER_SECOND = 40.0; // Maximum frames to be rendered per second
	private final double MIN_SECONDS_PER_UPDATE = 1.0 / MAX_UPDATES_PER_SECOND;
	private long previousTime;
	private double unprocessedTime;

	private int fps; // Keep track of rendered frames per second
	private double fpsTime; // To observe passing of a second

	private Shader shader;

	private Mesh circleMesh;

	private final int NUM_CIRCLES = 3;
	private Transform[] circleTransform;

	private Transform pointerTransform;

	private MassiveCircle[] massiveCircles;
	private Transform[] massiveCircleTransforms;

	private Texture texture;
	//	private int texID;

	/*
	 * Initialize components
	 */
	public Game() {
		// Initialize camera
		//		camera = new Camera(70, Transform.width / Transform.height, 0.01f, 1000);
		camera = new Camera();
		Transform.setCamera(camera);

		String vertexShaderCode = MainActivity.Utility.loadShaderString(R.raw.phong_vertex);
		String fragmentShaderCode = MainActivity.Utility.loadShaderString(R.raw.phong_fragment);
		shader = new Shader(vertexShaderCode, fragmentShaderCode);

		circleMesh = MainActivity.Utility.loadMesh(R.raw.finetorus);

		circleTransform = new Transform[NUM_CIRCLES];
		for(int j = 0; j < NUM_CIRCLES; j++) {
			circleTransform[j] = new Transform();
		}

		pointerTransform = new Transform();

		massiveCircles = new MassiveCircle[0];
		massiveCircleTransforms = new Transform[0];

		texture = MainActivity.Utility.loadTexture(R.raw.tex2);
		//		texID = MainActivity.Utility.loadTexture(R.raw.tex1);
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
				Log.e("ANDROID 3D ENGINE", "Thread interrupted at Game#update");
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

		MassiveCircle.update(delta, massiveCircles);

		if(lastTouch.getX() != Input.getCurrentTapTouch(0).getX()) {

			lastTouch = Input.getCurrentTapTouch(0);

			addMassiveCircle(new MassiveCircle(
					new Vector2f(Transform.width / 2, Transform.height / 2),
					new Vector2f(0, 0),
					13,
					new Vector2f(Transform.width, Transform.height),
					100,
					false));
		}

		for(int j = 0; j < massiveCircleTransforms.length; j++) {
			massiveCircleTransforms[j].setRotation(rotation += delta * 30, 0, rotation / 2);
		}
	}

	/*
	 * Render game components
	 */
	private void render() {
		Transform.setCamera(camera);

		//		// Aspect ratio for a quick hack to orthographic projection
		//		pointerTransform.setScale(0.01f * (Transform.height / Transform.width), 0.01f, 0.01f);
		//
		//		// Correct positions for touch
		//		Vector2f touch = new Vector2f(
		//				((Input.getCurrentMovingTouch(0).getX() - (Transform.width / 2)) / Transform.width) * 2,
		//				-(Input.getCurrentMovingTouch(0).getY() - (Transform.height / 2)) / Transform.height * 2);
		//
		//		pointerTransform.setTranslation(touch.getX(), touch.getY(), 0.1f);
		//
		//
		//		shader.bind();
		//		// "Orthographic" projection
		//		shader.setUniform("transform", pointerTransform.getTransformation());
		//		//		shader.setUniform("color", new Vector3f(0, 1, 1));
		//
		//		circleMesh.draw();

		////////////////////

		for(int j = 0; j < massiveCircles.length; j++) {

			//			massiveCircleTransforms[j].setScale(0.65f * (Transform.height / Transform.width), 0.65f, 0.65f);
			massiveCircleTransforms[j].setScale(0.05f, 0.05f, 0.05f);
			massiveCircleTransforms[j].setTranslation(
					((massiveCircles[j].getPosition().getX() - (Transform.width / 2)) / Transform.width) * 2,
					-(massiveCircles[j].getPosition().getY() - (Transform.height / 2)) / Transform.height * 2,
					0.1f);

			shader.bind();

			texture.bind();

			//			shader.setUniform("transform", massiveCircleTransforms[j].getTransformation());
			//			shader.setUniform("color", new Vector3f(1, 1, 1));

			shader.setUniform("projection", massiveCircleTransforms[j].getProjectedTransformation());
			shader.setUniform("modelview", massiveCircleTransforms[j].getTransformation());
			//			shader.setUniform("normalMat", massiveCircleTransforms[j].getTransformation().invert());
			shader.setUniformi("mode", 2);

			circleMesh.draw();
		}

		// FPS counter
		if(fpsTime + 1 < (double) System.nanoTime() / Time.NANOSECONDS) {
			Log.i("ANDROID 3D ENGINE", "FPS: " + Integer.toString(fps));
			fpsTime = (double) System.nanoTime() / Time.NANOSECONDS;
			fps = 0;
		}
		fps++;
	}

	public void addMassiveCircle(MassiveCircle circle) {
		MassiveCircle[] newList = new MassiveCircle[massiveCircles.length + 1];
		for(int j = 0; j < massiveCircles.length; j++) {
			newList[j] = massiveCircles[j];
		}
		newList[massiveCircles.length] = circle;
		massiveCircles = newList;

		Transform[] newList2 = new Transform[massiveCircleTransforms.length + 1];
		for(int j = 0; j < massiveCircleTransforms.length; j++) {
			newList2[j] = massiveCircleTransforms[j];
		}
		newList2[massiveCircleTransforms.length] = new Transform();
		massiveCircleTransforms = newList2;
	}
}