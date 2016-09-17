package com.example.android3dengine.math;

public class Vector3f {

	private float x;
	private float y;
	private float z;


	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public float dot(Vector3f r) {
		return this.x * r.x + this.y * r.y + this.z * r.z;
	}

	public Vector3f cross(Vector3f r) {
		float x = this.y * r.z - this.z * r.y;
		float y = this.z * r.x - this.x * r.z;
		float z = this.x * r.y - this.y * r.x;

		return new Vector3f(x, y, z);
	}

	public Vector3f normalized() {
		float length = length();

		return new Vector3f(x / length, y / length, z / length);
	}

	public Vector3f rotate(float angle, Vector3f axis) {
		float sinHalfAngle = (float) Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float) Math.cos(Math.toRadians(angle / 2));

		// Quaternion components
		float rX = axis.getX() * sinHalfAngle;
		float rY = axis.getY() * sinHalfAngle;
		float rZ = axis.getZ() * sinHalfAngle;
		float rW = cosHalfAngle;

		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();

		Quaternion w = rotation.mul(this).mul(conjugate);

		x = w.getX();
		y = w.getY();
		z = w.getZ();

		return this;
	}

	public Vector3f add(Vector3f r) {
		return new Vector3f(this.x + r.x, this.y + r.y, this.z + r.z);
	}

	public Vector3f add(float r) {
		return new Vector3f(this.x + r, this.y + r, this.z + r);
	}

	public Vector3f sub(Vector3f r) {
		return new Vector3f(this.x - r.x, this.y - r.y, this.z - r.z);
	}

	public Vector3f sub(float r) {
		return new Vector3f(this.x - r, this.y - r, this.z - r);
	}

	public Vector3f mul(Vector3f r) {
		return new Vector3f(this.x * r.x, this.y * r.y, this.z * r.z);
	}

	public Vector3f mul(float r) {
		return new Vector3f(this.x * r, this.y * r, this.z * r);
	}

	public Vector3f div(Vector3f r) {
		return new Vector3f(this.x / r.x, this.y / r.y, this.z / r.z);
	}

	public Vector3f div(float r) {
		return new Vector3f(this.x / r, this.y / r, this.z / r);
	}

	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}