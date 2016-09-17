package com.example.android3dengine;

import javax.microedition.khronos.opengles.GL10;

import com.example.android3dengine.math.Transform;
import com.example.game.Game2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	//	private LogoAnim logoAnim;
	private Game2 game;

	@Override
	public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
		// Background color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// Face culling
		//		GLES20.glFrontFace(GLES20.GL_CW);
		//		GLES20.glCullFace(GLES20.GL_BACK); // Usually GL_BACK
		//		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Depth test
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		//		logoAnim = new LogoAnim();
		game = new Game2();
	}

	/*
	 * This method is called by MyGLSurfaceView.requestRender()
	 */
	@Override
	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		//		if(!logoAnim.animFinished)
		//			logoAnim.loopOnce();
		//		else
		game.loopOnce();
	}

	/*
	 * This method is called when screen resolution / rotation is changed
	 */
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		if(width == 0 || height == 0) {
			Log.e("ANDROID 3D ENGINE", "Width or height is zero at MyGLRenderer#onSurfaceChanged");
			System.exit(1);
		}

		Transform.setProjection(70, width, height, 0.01f, 1000.0f);

		GLES20.glViewport(0, 0, width, height);
	}
}
