package com.example.game;

import com.example.android3dengine.Camera;
import com.example.android3dengine.MainActivity;
import com.example.android3dengine.Mesh;
import com.example.android3dengine.R;
import com.example.android3dengine.Shader;
import com.example.android3dengine.Time;
import com.example.android3dengine.math.Transform;

import android.opengl.GLES20;
import android.util.Log;

public class LogoAnim {

	private Camera camera;

	private final double MAX_UPDATES_PER_SECOND = 600.0; // Maximum frames to be rendered per second
	private final double MIN_SECONDS_PER_UPDATE = 1.0 / MAX_UPDATES_PER_SECOND;
	private long previousTime;
	private double unprocessedTime;

	private final int NUM_BOXES = 3;
	private Mesh[] boxMesh;
	private Shader[] boxShader;
	private Transform[] boxTransform;

	private Mesh logoMesh;
	private Shader logoShader;
	private Transform logoTransform;

	private float xRotValue = 1.05f;
	private float yTransValue = 1.0f;
	public boolean animFinished;


	/*
	 * Initialize components
	 */
	public LogoAnim() {
		GLES20.glFrontFace(GLES20.GL_CW);
		GLES20.glCullFace(GLES20.GL_FRONT);
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Initialize camera
		//		camera = new Camera();
		// Projection view should conform to camera attributes
		Transform.setCamera(camera);


		String vertexShaderCode = MainActivity.Utility.loadShaderString(R.raw.vertex);
		String fragmentShaderCode = MainActivity.Utility.loadShaderString(R.raw.box_fragment);

		boxMesh = new Mesh[NUM_BOXES];
		boxShader = new Shader[NUM_BOXES];
		boxTransform = new Transform[NUM_BOXES];

		for(int j = 0; j < NUM_BOXES; j++) {
			boxMesh[j] = MainActivity.Utility.loadMesh(R.raw.smallbox);
			boxShader[j] = new Shader(vertexShaderCode, fragmentShaderCode);
			boxTransform[j] = new Transform();
		}
		boxTransform[0].setScale(0.6f, 0.6f, 0.06f);
		boxTransform[1].setScale(0.4f, 0.4f, 0.03f);
		boxTransform[2].setScale(0.15f, 0.15f, 0.01f);

		boxTransform[0].setTranslation(-0.195f, -0.040f, 1.4f);
		boxTransform[1].setTranslation( 0.220f,  0.075f, 1.2f);
		boxTransform[2].setTranslation( 0.091f, -0.125f, 1);


		// Text logo
		fragmentShaderCode = MainActivity.Utility.loadShaderString(R.raw.logo_fragment);

		logoMesh = MainActivity.Utility.loadMesh(R.raw.logo);
		logoShader = new Shader(vertexShaderCode, fragmentShaderCode);
		logoTransform = new Transform();
		logoTransform.setScale(0.13f, 0.13f, 0.13f);
		logoTransform.setTranslation(0, -0.37f, 1);
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

	private void update(float delta) {
		yTransValue /= 1.002;

		camera.rotateX((float) Math.sin(yTransValue) * 0.02f);

		xRotValue /= 1.002;

		if(xRotValue < 0.003f) {
			animFinished = true;
			GLES20.glFrontFace(GLES20.GL_CW);
			GLES20.glCullFace(GLES20.GL_BACK);
			GLES20.glEnable(GLES20.GL_CULL_FACE);
		}
	}

	/*
	 * Render game components
	 */
	private void render() {

		for(int j = 0; j < NUM_BOXES; j++) {
			boxShader[j].bind();

			boxShader[j].setUniform("transform", boxTransform[j].getProjectedTransformation());

			boxMesh[j].draw();
		}
		boxTransform[0].setRotation((float) (Math.sin((1 + xRotValue) * Math.PI * 0.5) * 360.0), (float) (Math.cos((1 + xRotValue) * Math.PI * 0.5) * 180.0), 45);
		boxTransform[1].setRotation((float) (Math.sin((1 + xRotValue) * Math.PI) * 180.0), (float) (Math.cos((1 + xRotValue) * Math.PI) * 180.0), 45);
		boxTransform[2].setRotation((float) (Math.sin((1 + xRotValue) * Math.PI * 1.5) * 180.0), (float) (Math.cos((1 + xRotValue) * Math.PI * 1.0) * 180.0), 45);


		logoShader.bind();
		logoTransform.setRotation((float) (Math.sin((1 + xRotValue) * Math.PI * 0.75) * 90), 180, 0);
		logoShader.setUniform("transform", logoTransform.getProjectedTransformation());
		logoMesh.draw();
	}

	//	backgroundShader.setUniformf("iGlobalTime", (float) (Math.sin(xRotValue / 100.0) * 100.0));
	//	backgroundShader.setUniformf("xPos", 0);
	//	backgroundShader.setUniformf("yPos", 0);
	//	backgroundShader.setUniformi("width", 800);
	//	backgroundShader.setUniformi("height", 800);
}
