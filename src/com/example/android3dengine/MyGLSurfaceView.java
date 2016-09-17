package com.example.android3dengine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

	private final MyGLRenderer mRenderer;

	public MyGLSurfaceView(Context context) {
		super(context);

		// Create an OpenGL ES 3.0 context
		setEGLContextClientVersion(3);

		mRenderer = new MyGLRenderer();

		// Graphic settings
		setEGLConfigChooser(new MyGLConfigChooser());

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(mRenderer);

		new Thread(new Runnable() {
			@Override
			public void run() {
				requestRender();
			}
		}).start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		Input.update(e);
		return true;
	}
}
