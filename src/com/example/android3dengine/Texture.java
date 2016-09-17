package com.example.android3dengine;

import android.opengl.GLES20;

public class Texture {

	private int[] textures = new int[1];


	public Texture() {
		GLES20.glGenTextures(1, textures, 0);
	}

	//	public Texture(int id) {
	//		this.id = id;
	//	}

	public void bind() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
	}

	public int getID() {
		return textures[0];
	}
}
