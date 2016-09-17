package com.example.android3dengine;

import java.util.HashMap;

import com.example.android3dengine.math.Matrix4f;
import com.example.android3dengine.math.Vector3f;

import android.opengl.GLES20;
import android.util.Log;

public class Shader {

	public final int program;
	private HashMap<String, Integer> uniforms;

	public Shader(String vertexShaderCode, String fragmentShaderCode) {
		int vertexShader = -1;
		int fragmentShader = -1;
		try {
			vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
			fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

			if(vertexShader == -1 || fragmentShader == -1)
				throw new Exception();
		} catch (Exception e) {
			Log.e("ANDROID 3D ENGINE", "Error compiling shaders at Shader#Shader");
			Log.e("ANDROID 3D ENGINE", "error", e);
			System.exit(1);
		}

		// Create empty OpenGL ES Program
		program = GLES20.glCreateProgram();
		if(program == 0) {
			Log.e("ANDROID 3D ENGINE", "Error creating program at Shader#Shader");
			Log.e("ANDROID 3D ENGINE", "error", new Exception());
			System.exit(1);
		}

		// Add shaders to program
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);

		// Create OpenGL ES program executables
		GLES20.glLinkProgram(program);

		GLES20.glValidateProgram(program);
		Log.e("ANDROID 3D ENGINE", GLES20.glGetShaderInfoLog(program));
		//		System.exit(1);
		//		System.err.println(GLES20.glGetShaderInfoLog(program));
		//		System.exit(1);


		// Keep track of uniform handles
		uniforms = new HashMap<String, Integer>();

		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);
		setAttribLocation("normal", 2);
	}

	// Add program to OpenGL ES environment
	public void bind() {
		GLES20.glUseProgram(program);
	}

	public void setAttribLocation(String attributeName, int location) {
		GLES20.glBindAttribLocation(program, location, attributeName);
	}

	// Create uniform handle and store it for later use
	private int addUniform(String uniform) throws Exception {
		int uniformLocation = GLES20.glGetUniformLocation(program, uniform);

		if(uniformLocation == -1)
			throw new Exception();

		uniforms.put(uniform, uniformLocation);

		return uniformLocation;
	}

	public void setUniformi(String uniformName, int value) {
		Integer location = uniforms.get(uniformName);

		try {
			if(location == null)
				location = addUniform(uniformName);
			GLES20.glUniform1i(location, value);
		} catch (Exception e) {
			Log.e("ANDROID 3D ENGINE", "Error searching for " + uniformName + " at Shader#setUniformi");
			Log.e("ANDROID 3D ENGINE", "error", e);
		}
	}

	public void setUniformf(String uniformName, float value) {
		Integer location = uniforms.get(uniformName);

		try {
			if(location == null)
				location = addUniform(uniformName);
			GLES20.glUniform1f(location, value);
		} catch (Exception e) {
			Log.e("ANDROID 3D ENGINE", "Error searching for " + uniformName + " at Shader#setUniformf");
			Log.e("ANDROID 3D ENGINE", "error", e);
		}
	}

	public void setUniform(String uniformName, Matrix4f value) {
		Integer location = uniforms.get(uniformName);

		try {
			if(location == null)
				location = addUniform(uniformName);
			GLES20.glUniformMatrix4fv(location, 1, true, value.toFloatBuffer()); // True for row-major 4x4 matrix
		} catch (Exception e) {
			Log.e("ANDROID 3D ENGINE", "Error searching for " + uniformName + " at Shader#setUniform");
			Log.e("ANDROID 3D ENGINE", "error", e);
		}
	}

	public void setUniform(String uniformName, Vector3f value) {
		Integer location = uniforms.get(uniformName);

		try {
			if(location == null)
				location = addUniform(uniformName);
			GLES20.glUniform3f(location, value.getX(), value.getY(), value.getZ());
		} catch (Exception e) {
			Log.e("ANDROID 3D ENGINE", "Error searching for " + uniformName + " at Shader#setUniform");
			Log.e("ANDROID 3D ENGINE", "error", e);
		}
	}

	private static int compileShader(int type, String shaderCode) throws Exception {
		// Create a vertex shader type (GLES20.GL_VERTEX_SHADER) or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// Add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}
}
