package com.example.android3dengine;

import com.example.android3dengine.math.Vector2f;

import android.util.Log;
import android.view.MotionEvent;

/*
 * Touch information fed to Input by MyGLSurfaceView#onTouchEvent
 */
public class Input {

	private static final int MAX_POINTERS = 2;

	private static Vector2f pivotVector;
	private static float relativeAngle;

	// Updates only when tapped without drag
	private static Vector2f[] lastTapTouch = new Vector2f[MAX_POINTERS];
	private static Vector2f[] currentTapTouch = new Vector2f[MAX_POINTERS];

	// Updates only when dragged
	private static Vector2f[] lastMovingTouch = new Vector2f[MAX_POINTERS];
	private static Vector2f[] currentMovingTouch = new Vector2f[MAX_POINTERS];

	static {
		pivotVector = new Vector2f(0, 0);

		for(int j = 0; j < MAX_POINTERS; j++) {
			lastMovingTouch[j] = new Vector2f(0, 0);
			currentMovingTouch[j] = new Vector2f(0, 0);
			lastTapTouch[j] = new Vector2f(0, 0);
			currentTapTouch[j] = new Vector2f(0, 0);
		}
	}

	/*
	 * Update inputs
	 */
	public static void update(MotionEvent e) {
		int pointerCount = e.getPointerCount();

		for(int j = 0; j < Math.min(pointerCount, MAX_POINTERS); j++) {
			if(e.getActionMasked() == MotionEvent.ACTION_DOWN) {
				updateTap(e.getX(j), e.getY(j), j);
				//				resetMoving(j);
			}
		}

		if(pointerCount != 2)
			pivotVector = new Vector2f(0, 0); // Reset pivot


		if(pointerCount == 1) {
			if(e.getActionMasked() == MotionEvent.ACTION_MOVE)
				updateMoving(e.getX(), e.getY(), 0);

		} else if(pointerCount == 2) {
			if(pivotVector.getX() == 0) { // Set pivot point
				Vector2f touchOne = new Vector2f(e.getX(0), e.getY(0));
				Vector2f touchTwo = new Vector2f(e.getX(1), e.getY(1));

				pivotVector = touchOne.sub(touchTwo);
			}

			// TODO : FIX Relative angle calculation
			if(e.getActionMasked() == MotionEvent.ACTION_MOVE) {
				updateMoving(e.getX(0), e.getY(0), 0);
				updateMoving(e.getX(1), e.getY(1), 1);

				Vector2f touchOne = new Vector2f(e.getX(0), e.getY(0));
				Vector2f touchTwo = new Vector2f(e.getX(1), e.getY(1));
				Vector2f absoluteFromPivot = touchOne.sub(touchTwo);
				float absoluteAngle = (float) (Math.atan(absoluteFromPivot.getY() / (float) absoluteFromPivot.getX()) * (180 / Math.PI));
				float pivotAngle = (float) (Math.atan(pivotVector.getY() / (float) pivotVector.getX()) * (180 / Math.PI));
				relativeAngle = (float) (pivotAngle - absoluteAngle);

				Log.e("ANDROID 3D ENGINE", Float.toString(relativeAngle));
			}
		}
	}

	private static void updateTap(float x, float y, int index) {
		lastTapTouch[index].setX(currentTapTouch[index].getX());
		lastTapTouch[index].setY(currentTapTouch[index].getY());

		currentTapTouch[index].setX(x);
		currentTapTouch[index].setY(y);
	}

	private static void updateMoving(float x, float y, int index) {
		lastMovingTouch[index].setX(currentMovingTouch[index].getX());
		lastMovingTouch[index].setY(currentMovingTouch[index].getY());

		currentMovingTouch[index].setX(x);
		currentMovingTouch[index].setY(y);
	}

	private static void resetMoving(int index) {
		lastMovingTouch[index].setX(0);
		lastMovingTouch[index].setY(0);

		currentMovingTouch[index].setX(0);
		currentMovingTouch[index].setY(0);
	}

	public static Vector2f getDeltaTouch(int index) {
		Vector2f deltaTouch = new Vector2f(0, 0);

		if(lastMovingTouch[index].getX() == 0 && lastMovingTouch[index].getY() == 0) {
			deltaTouch.setX(0);
			deltaTouch.setY(0);
		} else {
			deltaTouch.setX(lastMovingTouch[index].getX() - currentMovingTouch[index].getX());
			deltaTouch.setY(lastMovingTouch[index].getY() - currentMovingTouch[index].getY());
		}

		return deltaTouch;
	}

	public static Vector2f getLastMovingTouch(int index) {
		return new Vector2f( lastMovingTouch[index].getX(), lastMovingTouch[index].getY() );
	}

	public static Vector2f getLastTapTouch(int index) {
		return new Vector2f( lastTapTouch[index].getX() , lastTapTouch[index].getY() );
	}

	public static Vector2f getCurrentMovingTouch(int index) {
		return new Vector2f( currentMovingTouch[index].getX(), currentMovingTouch[index].getY() );
	}

	public static Vector2f getCurrentTapTouch(int index) {
		return new Vector2f( currentTapTouch[index].getX(), currentTapTouch[index].getY() );
	}

	public static Vector2f getPivotVector() {
		return new Vector2f( pivotVector.getX(), pivotVector.getY() );
	}

	public static float getRelativeAngle() {
		return relativeAngle;
	}
}