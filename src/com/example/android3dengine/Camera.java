package com.example.android3dengine;

import com.example.android3dengine.math.Vector3f;

//public class Camera {
//
//	public static final Vector3f yAxis = new Vector3f(0, 1, 0);
//	private Vector3f pos;
//	private Vector3f forward;
//	private Vector3f up;
//	private Matrix4f projection;
//
//
//	public Camera(float fov, float aspectRatio, float zNear, float zFar) {
//		this.pos = new Vector3f(0, 0, 0);
//		this.forward = new Vector3f(0, 0, 1).normalized();
//		this.up = new Vector3f(0, 1, 0).normalized();
//		this.projection = new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar);
//	}
//
//	public Matrix4f getViewProjection() {
//		Matrix4f cameraRotation = new Matrix4f().initRotation(forward, up);
//		Matrix4f cameraTranslation = new Matrix4f().initTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
//
//		return projection.mul(cameraRotation.mul(cameraTranslation));
//	}
//
//	public void input(float delta) {
//		//		float moveAmount = (float) (10 * delta);
//		//		float rotationAmount = (float) (100 * delta);
//
//		//		if(Input.getKey(Input.KEY_W))
//		//			move(getForward(), moveAmount);
//		//		if(Input.getKey(Input.KEY_S))
//		//			move(getForward(), -moveAmount);
//		//		if(Input.getKey(Input.KEY_A))
//		//			move(getLeft(), moveAmount);
//		//		if(Input.getKey(Input.KEY_D))
//		//			move(getRight(), moveAmount);
//
//		if(Input.getDeltaTouch(0).getY() != 0)
//			rotateX(Input.getDeltaTouch(0).getY() / 100);
//		if(Input.getDeltaTouch(0).getX() != 0)
//			rotateY(Input.getDeltaTouch(0).getX() / 100);
//	}
//
//	public void move(Vector3f dir, float amount) {
//		pos = pos.add(dir.mul(amount));
//	}
//
//	// Tilting head left and right
//	public void rotateY(float angle) {
//		Vector3f xAxis = yAxis.cross(forward).normalized();
//
//		forward = forward.rotate(angle, yAxis).normalized();
//
//		up = forward.cross(xAxis).normalized();
//	}
//
//	// Tilting head up and down
//	public void rotateX(float angle) {
//		Vector3f xAxis = yAxis.cross(forward).normalized();
//
//		forward = forward.rotate(angle, xAxis).normalized();
//
//		up = forward.cross(xAxis).normalized();
//	}
//
//	public Vector3f getLeft() {
//		return forward.cross(up).normalized();
//	}
//
//	public Vector3f getRight() {
//		return up.cross(forward).normalized();
//	}
//
//	public Vector3f getPos() {
//		return pos;
//	}
//
//	public void setPos(Vector3f pos) {
//		this.pos = pos;
//	}
//
//	public Vector3f getForward() {
//		return forward;
//	}
//
//	public void setForward(Vector3f forward) {
//		this.forward = forward;
//	}
//
//	public Vector3f getUp() {
//		return up;
//	}
//
//	public void setUp(Vector3f up) {
//		this.up = up;
//	}
//}


public class Camera {

	public static final Vector3f yAxis = new Vector3f(0, 1, 0);
	private Vector3f pos;
	private Vector3f forward;
	private Vector3f up;

	public Camera() {
		this(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));
	}

	public Camera(Vector3f pos, Vector3f forward, Vector3f up) {
		this.pos = pos;
		this.forward = forward.normalized();
		this.up = up.normalized();
	}

	public void input(float delta) {
		//		float moveAmount = (float) (10 * delta);
		//		float rotationAmount = (float) (100 * delta);

		//		if(Input.getKey(Input.KEY_W))
		//			move(getForward(), moveAmount);
		//		if(Input.getKey(Input.KEY_S))
		//			move(getForward(), -moveAmount);
		//		if(Input.getKey(Input.KEY_A))
		//			move(getLeft(), moveAmount);
		//		if(Input.getKey(Input.KEY_D))
		//			move(getRight(), moveAmount);

		if(Input.getDeltaTouch(0).getY() != 0)
			rotateX(Input.getDeltaTouch(0).getY() / 100);
		if(Input.getDeltaTouch(0).getX() != 0)
			rotateY(Input.getDeltaTouch(0).getX() / 100);
	}

	public void move(Vector3f dir, float amount) {
		pos = pos.add(dir.mul(amount));
	}

	// Tilting head left and right
	public void rotateY(float angle) {
		Vector3f xAxis = yAxis.cross(forward).normalized();

		forward = forward.rotate(angle, yAxis).normalized();

		up = forward.cross(xAxis).normalized();
	}

	// Tilting head up and down
	public void rotateX(float angle) {
		Vector3f xAxis = yAxis.cross(forward).normalized();

		forward = forward.rotate(angle, xAxis).normalized();

		up = forward.cross(xAxis).normalized();
	}

	public Vector3f getLeft() {
		return forward.cross(up).normalized();
	}

	public Vector3f getRight() {
		return up.cross(forward).normalized();
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getForward() {
		return forward;
	}

	public void setForward(Vector3f forward) {
		this.forward = forward;
	}

	public Vector3f getUp() {
		return up;
	}

	public void setUp(Vector3f up) {
		this.up = up;
	}
}