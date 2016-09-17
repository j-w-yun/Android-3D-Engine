package com.example.android3dengine.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Matrix4f {

	private float[][] m;


	public Matrix4f() {
		m = new float[4][4];
	}

	public Matrix4f initIdentity() {
		m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
		m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = 0;
		m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = 0;
		m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

		return this;
	}

	public Matrix4f initTranslation(float x, float y, float z) {
		m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = x;
		m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = y;
		m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = z;
		m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

		return this;
	}

	public Matrix4f initRotation(float x, float y, float z) {
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();

		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);

		rz.m[0][0] = (float) Math.cos(z);	rz.m[0][1] = (float) -Math.sin(z);	rz.m[0][2] = 0; rz.m[0][3] = 0;
		rz.m[1][0] = (float) Math.sin(z);	rz.m[1][1] = (float) Math.cos(z);	rz.m[1][2] = 0; rz.m[1][3] = 0;
		rz.m[2][0] = 0;						rz.m[2][1] = 0;						rz.m[2][2] = 1; rz.m[2][3] = 0;
		rz.m[3][0] = 0;						rz.m[3][1] = 0;						rz.m[3][2] = 0; rz.m[3][3] = 1;

		rx.m[0][0] = 1; rx.m[0][1] = 0;						rx.m[0][2] = 0;						rx.m[0][3] = 0;
		rx.m[1][0] = 0; rx.m[1][1] = (float) Math.cos(x);	rx.m[1][2] = (float) -Math.sin(x);	rx.m[1][3] = 0;
		rx.m[2][0] = 0; rx.m[2][1] = (float) Math.sin(x);	rx.m[2][2] = (float) Math.cos(x);	rx.m[2][3] = 0;
		rx.m[3][0] = 0; rx.m[3][1] = 0;						rx.m[3][2] = 0;						rx.m[3][3] = 1;

		ry.m[0][0] = (float) Math.cos(y);	ry.m[0][1] = 0; ry.m[0][2] = (float) -Math.sin(y);	ry.m[0][3] = 0;
		ry.m[1][0] = 0;						ry.m[1][1] = 1; ry.m[1][2] = 0;						ry.m[1][3] = 0;
		ry.m[2][0] = (float) Math.sin(y);	ry.m[2][1] = 0; ry.m[2][2] = (float) Math.cos(y);	ry.m[2][3] = 0;
		ry.m[3][0] = 0;						ry.m[3][1] = 0; ry.m[3][2] = 0;						ry.m[3][3] = 1;

		m = rz.mul(ry.mul(rx)).getM();

		return this;
	}

	public Matrix4f initScale(float x, float y, float z) {
		m[0][0] = x; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
		m[1][0] = 0; m[1][1] = y; m[1][2] = 0; m[1][3] = 0;
		m[2][0] = 0; m[2][1] = 0; m[2][2] = z; m[2][3] = 0;
		m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

		return this;
	}

	public Matrix4f initProjection(float fov, float width, float height, float zNear, float zFar) {
		float ar = width / height; // Aspect ratio
		float tanHalfFOV = (float) Math.tan(Math.toRadians(fov / 2.0));
		float zRange = zNear - zFar;

		m[0][0] = 1.0f / (tanHalfFOV * ar);	m[0][1] = 0;					m[0][2] = 0;						m[0][3] = 0;
		m[1][0] = 0;						m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;						m[1][3] = 0;
		m[2][0] = 0;						m[2][1] = 0;					m[2][2] = (-zNear - zFar) / zRange;	m[2][3] = 2.0f * zFar * zNear / zRange;
		m[3][0] = 0;						m[3][1] = 0;					m[3][2] = 1;						m[3][3] = 0;

		return this;
	}

	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float tanHalfFOV = (float) Math.tan(fov / 2.0);
		float zRange = zNear - zFar;

		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	m[0][1] = 0;					m[0][2] = 0;						m[0][3] = 0;
		m[1][0] = 0;									m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;						m[1][3] = 0;
		m[2][0] = 0;									m[2][1] = 0;					m[2][2] = (-zNear - zFar) / zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;									m[3][1] = 0;					m[3][2] = 1;						m[3][3] = 0;

		return this;
	}

	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far) {
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		m[0][0] = 2 / width;	m[0][1] = 0;			m[0][2] = 0;			m[0][3] = -(right + left) / width;
		m[1][0] = 0;			m[1][1] = 2 / height;	m[1][2] = 0;			m[1][3] = -(top + bottom) / height;
		m[2][0] = 0;			m[2][1] = 0;			m[2][2] = -2 / depth;	m[2][3] = -(far + near) / depth;
		m[3][0] = 0;			m[3][1] = 0;			m[3][2] = 0;			m[3][3] = 1;

		return this;
	}

	public Matrix4f initRotation(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalized();

		// Horizontal vector
		Vector3f r = up.normalized();

		r = r.cross(f);

		// Recalculate vertical vector
		Vector3f u = f.cross(r);

		m[0][0] = r.getX();	m[0][1] = r.getY();	m[0][2] = r.getZ();	m[0][3] = 0;
		m[1][0] = u.getX();	m[1][1] = u.getY();	m[1][2] = u.getZ();	m[1][3] = 0;
		m[2][0] = f.getX();	m[2][1] = f.getY();	m[2][2] = f.getZ();	m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}

	public Matrix4f mul(Matrix4f r) {
		Matrix4f result = new Matrix4f();

		for(int j = 0; j < 4; j++) {
			for(int k = 0; k < 4; k++) {
				result.set(j, k,
						this.m[j][0] * r.m[0][k] +
						this.m[j][1] * r.m[1][k] +
						this.m[j][2] * r.m[2][k] +
						this.m[j][3] * r.m[3][k]);
			}
		}

		return result;
	}

	/**
	 * @return A deep copy of the 4x4 matrix in 2D float array in row major format.
	 */
	public float[][] getM() {
		float[][] res = new float[4][4];

		for(int j = 0; j < 4; j++) {
			for(int k = 0; k < 4; k++) {
				res[j][k] = m[j][k];
			}
		}

		return m;
	}

	public float get(int x, int y) {
		return m[x][y];
	}

	public void set(int x, int y, float value) { m[x][y] = value;
	}

	public void setM(float[][] m) {
		this.m = m;
	}

	public FloatBuffer toFloatBuffer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(16 * 4);
		bb.order(ByteOrder.nativeOrder());

		FloatBuffer fb = bb.asFloatBuffer();

		for(int j = 0; j < 4; j++) {
			for(int k = 0; k < 4; k++) {
				fb.put(get(j, k));
			}
		}
		fb.position(0);

		return fb;
	}

	public float determinant() {
		float f =
				m[0][0]
						* ((m[1][1] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][1] + m[1][3] * m[2][1] * m[3][2])
								- m[1][3] * m[2][2] * m[3][1]
										- m[1][1] * m[2][3] * m[3][2]
												- m[1][2] * m[2][1] * m[3][3]);
		f -= m[0][1]
				* ((m[1][0] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][2])
						- m[1][3] * m[2][2] * m[3][0]
								- m[1][0] * m[2][3] * m[3][2]
										- m[1][2] * m[2][0] * m[3][3]);
		f += m[0][2]
				* ((m[1][0] * m[2][1] * m[3][3] + m[1][1] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][1])
						- m[1][3] * m[2][1] * m[3][0]
								- m[1][0] * m[2][3] * m[3][1]
										- m[1][1] * m[2][0] * m[3][3]);
		f -= m[0][3]
				* ((m[1][0] * m[2][1] * m[3][2] + m[1][1] * m[2][2] * m[3][0] + m[1][2] * m[2][0] * m[3][1])
						- m[1][2] * m[2][1] * m[3][0]
								- m[1][0] * m[2][2] * m[3][1]
										- m[1][1] * m[2][0] * m[3][2]);
		return f;
	}

	private static float determinant3x3 (float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22) {
		return   t00 * (t11 * t22 - t12 * t21)
				+ t01 * (t12 * t20 - t10 * t22)
				+ t02 * (t10 * t21 - t11 * t20);
	}

	public Matrix4f invert() {
		return invert(this, this);
	}

	public static Matrix4f invert(Matrix4f src, Matrix4f dest) {
		float determinant = src.determinant();

		if (determinant != 0) {
			/*
			 * m00 m01 m02 m03
			 * m10 m11 m12 m13
			 * m20 m21 m22 m23
			 * m30 m31 m32 m33
			 */
			if (dest == null)
				dest = new Matrix4f();
			float determinant_inv = 1f/determinant;

			// first row
			float t00 =  determinant3x3(src.m[1][1], src.m[1][2], src.m[1][3], src.m[2][1], src.m[2][2], src.m[2][3], src.m[3][1], src.m[3][2], src.m[3][3]);
			float t01 = -determinant3x3(src.m[1][0], src.m[1][2], src.m[1][3], src.m[2][0], src.m[2][2], src.m[2][3], src.m[3][0], src.m[3][2], src.m[3][3]);
			float t02 =  determinant3x3(src.m[1][0], src.m[1][1], src.m[1][3], src.m[2][0], src.m[2][1], src.m[2][3], src.m[3][0], src.m[3][1], src.m[3][3]);
			float t03 = -determinant3x3(src.m[1][0], src.m[1][1], src.m[1][2], src.m[2][0], src.m[2][1], src.m[2][2], src.m[3][0], src.m[3][1], src.m[3][2]);
			// second row
			float t10 = -determinant3x3(src.m[0][1], src.m[0][2], src.m[0][3], src.m[2][1], src.m[2][2], src.m[2][3], src.m[3][1], src.m[3][2], src.m[3][3]);
			float t11 =  determinant3x3(src.m[0][0], src.m[0][2], src.m[0][3], src.m[2][0], src.m[2][2], src.m[2][3], src.m[3][0], src.m[3][2], src.m[3][3]);
			float t12 = -determinant3x3(src.m[0][0], src.m[0][1], src.m[0][3], src.m[2][0], src.m[2][1], src.m[2][3], src.m[3][0], src.m[3][1], src.m[3][3]);
			float t13 =  determinant3x3(src.m[0][0], src.m[0][1], src.m[0][2], src.m[2][0], src.m[2][1], src.m[2][2], src.m[3][0], src.m[3][1], src.m[3][2]);
			// third row
			float t20 =  determinant3x3(src.m[0][1], src.m[0][2], src.m[0][3], src.m[1][1], src.m[1][2], src.m[1][3], src.m[3][1], src.m[3][2], src.m[3][3]);
			float t21 = -determinant3x3(src.m[0][0], src.m[0][2], src.m[0][3], src.m[1][0], src.m[1][2], src.m[1][3], src.m[3][0], src.m[3][2], src.m[3][3]);
			float t22 =  determinant3x3(src.m[0][0], src.m[0][1], src.m[0][3], src.m[1][0], src.m[1][1], src.m[1][3], src.m[3][0], src.m[3][1], src.m[3][3]);
			float t23 = -determinant3x3(src.m[0][0], src.m[0][1], src.m[0][2], src.m[1][0], src.m[1][1], src.m[1][2], src.m[3][0], src.m[3][1], src.m[3][2]);
			// fourth row
			float t30 = -determinant3x3(src.m[0][1], src.m[0][2], src.m[0][3], src.m[1][1], src.m[1][2], src.m[1][3], src.m[2][1], src.m[2][2], src.m[2][3]);
			float t31 =  determinant3x3(src.m[0][0], src.m[0][2], src.m[0][3], src.m[1][0], src.m[1][2], src.m[1][3], src.m[2][0], src.m[2][2], src.m[2][3]);
			float t32 = -determinant3x3(src.m[0][0], src.m[0][1], src.m[0][3], src.m[1][0], src.m[1][1], src.m[1][3], src.m[2][0], src.m[2][1], src.m[2][3]);
			float t33 =  determinant3x3(src.m[0][0], src.m[0][1], src.m[0][2], src.m[1][0], src.m[1][1], src.m[1][2], src.m[2][0], src.m[2][1], src.m[2][2]);

			// transpose and divide by the determinant
			dest.m[0][0] = t00*determinant_inv;
			dest.m[1][1] = t11*determinant_inv;
			dest.m[2][2] = t22*determinant_inv;
			dest.m[3][3] = t33*determinant_inv;
			dest.m[0][1] = t10*determinant_inv;
			dest.m[1][0] = t01*determinant_inv;
			dest.m[2][0] = t02*determinant_inv;
			dest.m[0][2] = t20*determinant_inv;
			dest.m[1][2] = t21*determinant_inv;
			dest.m[2][1] = t12*determinant_inv;
			dest.m[0][3] = t30*determinant_inv;
			dest.m[3][0] = t03*determinant_inv;
			dest.m[1][3] = t31*determinant_inv;
			dest.m[3][1] = t13*determinant_inv;
			dest.m[3][2] = t23*determinant_inv;
			dest.m[2][3] = t32*determinant_inv;

			return dest;
		} else
			return null;
	}

	public Matrix4f transpose() {
		return transpose(this);
	}

	public Matrix4f transpose(Matrix4f dest) {
		return transpose(this, dest);
	}

	public static Matrix4f transpose(Matrix4f src, Matrix4f dest) {
		if (dest == null)
			dest = new Matrix4f();

		float m00 = src.m[0][0];
		float m01 = src.m[1][0];
		float m02 = src.m[2][0];
		float m03 = src.m[3][0];
		float m10 = src.m[0][1];
		float m11 = src.m[1][1];
		float m12 = src.m[2][1];
		float m13 = src.m[3][1];
		float m20 = src.m[0][2];
		float m21 = src.m[1][2];
		float m22 = src.m[2][2];
		float m23 = src.m[3][2];
		float m30 = src.m[0][3];
		float m31 = src.m[1][3];
		float m32 = src.m[2][3];
		float m33 = src.m[3][3];

		dest.m[0][0] = m00;
		dest.m[0][1] = m01;
		dest.m[0][2] = m02;
		dest.m[0][3] = m03;
		dest.m[1][0] = m10;
		dest.m[1][1] = m11;
		dest.m[1][2] = m12;
		dest.m[1][3] = m13;
		dest.m[2][0] = m20;
		dest.m[2][1] = m21;
		dest.m[2][2] = m22;
		dest.m[2][3] = m23;
		dest.m[3][0] = m30;
		dest.m[3][1] = m31;
		dest.m[3][2] = m32;
		dest.m[3][3] = m33;

		return dest;
	}
}