package com.example.android3dengine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES20;

public class Mesh {

	private final int[] buffers = new int[2];
	private int size;

	public Mesh(float[] vertices, int[] indices) {

		GLES20.glGenBuffers(2, buffers, 0);

		size = indices.length;

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
				vertices.length * 4,
				createFlippedBuffer(vertices),
				GLES20.GL_STATIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
				indices.length * 4,
				createFlippedBuffer(indices),
				GLES20.GL_STATIC_DRAW);
	}

	public void draw() {

		GLES20.glEnableVertexAttribArray(0);
		GLES20.glEnableVertexAttribArray(1);
		GLES20.glEnableVertexAttribArray(2);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 8 * 4, 0);
		GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 8 * 4, 12);
		GLES20.glVertexAttribPointer(2, 3, GLES20.GL_FLOAT, false, 8 * 4, 20);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, size, GLES20.GL_UNSIGNED_INT, 0);

		GLES20.glDisableVertexAttribArray(0);
		GLES20.glDisableVertexAttribArray(1);
		GLES20.glDisableVertexAttribArray(2);
	}

	private static FloatBuffer createFlippedBuffer(float[] values) {
		FloatBuffer buffer = FloatBuffer.allocate(values.length);
		buffer.put(values);
		buffer.flip();

		//		for(int j = 0; j < values.length; j += 5) {
		//			Log.e("ANDROID 3D ENGINE", "Vertex : " + j + " = " +
		//					buffer.get(j + 0) + ", " +
		//					buffer.get(j + 1) + ", " +
		//					buffer.get(j + 2) + ", " +
		//					buffer.get(j + 3) + ", " +
		//					buffer.get(j + 4));
		//		}

		return buffer;
	}

	public static IntBuffer createFlippedBuffer(int[] values) {
		IntBuffer buffer = IntBuffer.allocate(values.length);
		buffer.put(values);
		buffer.flip();

		//		for(int j = 0; j < values.length; j++) {
		//			Log.e("ANDROID 3D ENGINE", "Index : " + j + " = " + buffer.get(j));
		//		}

		return buffer;
	}

	/*
	 * Rendering method #2
	 */
	//	private static final int COORDS_PER_VERTEX = 3; // 3 floats per vertex
	//	private static final int STRIDE_PER_VERTEX = COORDS_PER_VERTEX * 4; // 12 bytes per vertex
	//	private FloatBuffer vb; // Vertex buffer
	//	private IntBuffer ib; // Index buffer
	//	private int numVertices;
	//
	//	public Mesh(float[] vertices, int[] indices) {
	//		numVertices = indices.length;
	//
	//		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
	//		bb.order(ByteOrder.nativeOrder());
	//		vb = bb.asFloatBuffer();
	//		vb.put(vertices);
	//		vb.position(0);
	//
	//		ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 4);
	//		dlb.order(ByteOrder.nativeOrder());
	//		ib = dlb.asIntBuffer();
	//		ib.put(indices);
	//		ib.position(0);
	//	}
	//
	//	// Send vertex and index data to GPU
	//	public void draw() {
	//		GLES20.glEnableVertexAttribArray(0);
	//
	//		GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, STRIDE_PER_VERTEX, vb);
	//
	//		GLES20.glDrawElements(GLES20.GL_TRIANGLES, numVertices, GLES20.GL_UNSIGNED_INT, ib);
	//
	//		GLES20.glDisableVertexAttribArray(0);
	//	}
}
