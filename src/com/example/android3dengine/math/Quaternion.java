package com.example.android3dengine.math;

public class Quaternion {

	private float x;
	private float y;
	private float z;
	private float w;


	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalize() {
		float length = length();

		x /= length;
		y /= length;
		z /= length;
		w /= length;

		return this;
	}

	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion mul(Quaternion r) {
		float w = this.w * r.w - this.x * r.x - this.y * r.y - this.z * r.z;
		float x = this.x * r.w + this.w * r.x + this.y * r.z - this.z * r.y;
		float y = this.y * r.w + this.w * r.y + this.z * r.x - this.x * r.z;
		float z = this.z * r.w + this.w * r.z + this.x * r.y - this.y * r.x;

		return new  Quaternion(x, y, z, w);
	}

	public Quaternion mul(Vector3f r) {
		float w = -this.x * r.getX() - this.y * r.getY() - this.z * r.getZ();
		float x = this.w * r.getX() + this.y * r.getZ() - this.z * r.getY();
		float y = this.w * r.getY() + this.z * r.getX() - this.x * r.getZ();
		float z = this.w * r.getZ() + this.x * r.getY() - this.y * r.getX();

		return new Quaternion(x, y, z, w);
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

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
}
