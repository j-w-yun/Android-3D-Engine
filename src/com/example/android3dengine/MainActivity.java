package com.example.android3dengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

	private static MainActivity mainActivity;
	private GLSurfaceView mGLSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mGLSurfaceView = new MyGLSurfaceView(this);

		setContentView(mGLSurfaceView);

		mainActivity = this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	public void onPause() {
		mGLSurfaceView.setVisibility(View.GONE);
		super.onPause();
		mGLSurfaceView.onPause();
	}

	public static class Utility {

		// Loads shader code
		public static String loadShaderString(int resourceID) {
			StringBuilder sb = new StringBuilder();
			try {
				InputStream inputStream = mainActivity.getResources().openRawResource(resourceID);
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				while((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
			} catch (IOException e) {
				Log.e("ANDROID 3D ENGINE", "Error opening shader data at MainActivity.Utility#loadShaderString");
				Log.e("ANDROID 3D ENGINE", "error", e);
				System.exit(1);
			}
			return sb.toString();
		}

		// Loads .obj files
		public static Mesh loadMesh(int resourceID) {
			ArrayList<Float> vertices = new ArrayList<Float>();
			ArrayList<Float> texCoords = new ArrayList<Float>();
			ArrayList<Float> normals = new ArrayList<Float>();
			ArrayList<Integer> faceData = new ArrayList<Integer>();

			ArrayList<Float> interleaved = new ArrayList<Float>();
			ArrayList<Integer> indices = new ArrayList<Integer>();

			try {
				InputStream inputStream = mainActivity.getResources().openRawResource(resourceID);
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				String line;
				while((line = br.readLine()) != null) {
					String[] tokens = line.split(" ");
					tokens = removeEmptyStrings(tokens);

					if(tokens.length == 0 || tokens[0].equals("#")) {
						continue;
					} else if(tokens[0].equals("v")) { // Vertex for vbo
						vertices.add(Float.valueOf(tokens[1]));
						vertices.add(Float.valueOf(tokens[2]));
						vertices.add(Float.valueOf(tokens[3]));

					} else if(tokens[0].equals("vt")) {
						texCoords.add(Float.valueOf(tokens[1]));
						texCoords.add(Float.valueOf(tokens[2]));

					} else if(tokens[0].equals("vn")) {
						normals.add(Float.valueOf(tokens[1]));
						normals.add(Float.valueOf(tokens[2]));
						normals.add(Float.valueOf(tokens[3]));

					} else if(tokens[0].equals("f")) { // Index for ibo
						faceData.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
						faceData.add(Integer.parseInt(tokens[1].split("/")[1]) - 1);
						faceData.add(Integer.parseInt(tokens[1].split("/")[2]) - 1);

						faceData.add(Integer.parseInt(tokens[2].split("/")[0]) - 1);
						faceData.add(Integer.parseInt(tokens[2].split("/")[1]) - 1);
						faceData.add(Integer.parseInt(tokens[2].split("/")[2]) - 1);

						faceData.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
						faceData.add(Integer.parseInt(tokens[3].split("/")[1]) - 1);
						faceData.add(Integer.parseInt(tokens[3].split("/")[2]) - 1);

						//						if(tokens.length > 4) {
						//							faceData.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
						//							faceData.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
						//							faceData.add(Integer.parseInt(tokens[4].split("/")[0]) - 1);
						//						}
					}
				}
				br.close();
			} catch (IOException e) {
				Log.e("ANDROID 3D ENGINE", "Error opening mesh data at MainActivity.Utility#loadMesh");
				Log.e("ANDROID 3D ENGINE", "error", e);
				System.exit(1);
			}

			int index = 0;

			for(int j = 0; j < faceData.size(); j += 3) {
				int ve = faceData.get(j + 0); // Vertex index
				int vt = faceData.get(j + 1); // Texture coordinate index
				int vn = faceData.get(j + 2); // Normal index

				indices.add(index++);

				interleaved.add(vertices.get(ve * 3 + 0));
				interleaved.add(vertices.get(ve * 3 + 1));
				interleaved.add(vertices.get(ve * 3 + 2));

				interleaved.add(texCoords.get(vt * 2 + 0));
				interleaved.add(texCoords.get(vt * 2 + 1));

				interleaved.add(normals.get(vn * 3 + 0));
				interleaved.add(normals.get(vn * 3 + 1));
				interleaved.add(normals.get(vn * 3 + 2));
			}

			/*
			 * ArrayList to primitive[] direct conversion is not supported by Java
			 * Therefore we do the below steps to obtain float[] and int[]
			 */

			// Convert from ArrayList to Object[]
			Float[] interleavedDataObject = new Float[interleaved.size()];
			interleaved.toArray(interleavedDataObject);
			Integer[] indexDataObject = new Integer[indices.size()];
			indices.toArray(indexDataObject);

			// Convert from Object[] to primitive[]
			float[] interleavedData = new float[interleavedDataObject.length];
			for(int j = 0; j < interleavedDataObject.length; j++) {
				interleavedData[j] = interleavedDataObject[j];
			}
			int[] indexData = new int[indexDataObject.length];
			for(int j = 0; j < indexDataObject.length; j++) {
				indexData[j] = indexDataObject[j];
			}

			return new Mesh(interleavedData, indexData);
		}

		//		// Loads .obj files
		//		public static Mesh loadMesh(int resourceID) {
		//			ArrayList<Float> vertices = new ArrayList<Float>();
		//			ArrayList<Integer> indices = new ArrayList<Integer>();
		//
		//			try {
		//				InputStream inputStream = mainActivity.getResources().openRawResource(resourceID);
		//				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		//				String line;
		//				while((line = br.readLine()) != null) {
		//					String[] tokens = line.split(" ");
		//					tokens = removeEmptyStrings(tokens);
		//
		//					if(tokens.length == 0 || tokens[0].equals("#")) {
		//						continue;
		//					} else if(tokens[0].equals("v")) { // Vertex for vbo
		//						vertices.add(Float.valueOf(tokens[1]));
		//						vertices.add(Float.valueOf(tokens[2]));
		//						vertices.add(Float.valueOf(tokens[3]));
		//					} else if(tokens[0].equals("f")) { // Index for ibo
		//						indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
		//						indices.add(Integer.parseInt(tokens[2].split("/")[0]) - 1);
		//						indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
		//
		//						if(tokens.length > 4) {
		//							indices.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
		//							indices.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
		//							indices.add(Integer.parseInt(tokens[4].split("/")[0]) - 1);
		//						}
		//					}
		//				}
		//				br.close();
		//			} catch (IOException e) {
		//				Log.e("ANDROID 3D ENGINE", "Error opening mesh data at MainActivity.Utility#loadMesh");
		//				Log.e("ANDROID 3D ENGINE", "error", e);
		//				System.exit(1);
		//			}
		//
		//			/*
		//			 * ArrayList to primitive[] direct conversion is not supported by Java
		//			 * Therefore we do the below steps to obtain float[] and int[]
		//			 */
		//
		//			// Convert from ArrayList to Object[]
		//			Float[] interleavedDataObject = new Float[vertices.size()];
		//			vertices.toArray(interleavedDataObject);
		//			Integer[] indexDataObject = new Integer[indices.size()];
		//			indices.toArray(indexDataObject);
		//
		//			// Convert from Object[] to primitive[]
		//			float[] interleavedData = new float[interleavedDataObject.length];
		//			for(int j = 0; j < interleavedDataObject.length; j++) {
		//				interleavedData[j] = interleavedDataObject[j];
		//			}
		//			int[] indexData = new int[indexDataObject.length];
		//			for(int j = 0; j < indexDataObject.length; j++) {
		//				indexData[j] = indexDataObject[j];
		//			}
		//
		//			return new Mesh(interleavedData, indexData);
		//		}

		private static String[] removeEmptyStrings(String[] data) {
			ArrayList<String> result = new ArrayList<String>();

			for(int j = 0; j < data.length; j++) {
				if(!data[j].equals(""))
					result.add(data[j]);
			}

			String[] res = new String[result.size()];
			result.toArray(res);

			return res;
		}

		//		public static int loadTexture(final int resourceId)
		//		{
		//			final int[] textureHandle = new int[1];
		//
		//			GLES20.glGenTextures(1, textureHandle, 0);
		//
		//			if (textureHandle[0] != 0)
		//			{
		//				final BitmapFactory.Options options = new BitmapFactory.Options();
		//				options.inScaled = false;   // No pre-scaling
		//
		//				// Read in the resource
		//				final Bitmap bitmap = BitmapFactory.decodeResource(mainActivity.getResources(), resourceId, options);
		//
		//				// Bind to the texture in OpenGL
		//				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		//
		//				// Set filtering
		//				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		//				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		//
		//				// Load the bitmap into the bound texture.
		//				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		//
		//				// Recycle the bitmap, since its data has been loaded into OpenGL.
		//				bitmap.recycle();
		//			}
		//
		//			if (textureHandle[0] == 0)
		//			{
		//				throw new RuntimeException("Error loading texture.");
		//			}
		//
		//			return textureHandle[0];
		//		}

		public static Texture loadTexture(int id)
		{
			try
			{
				Bitmap image = BitmapFactory.decodeResource(mainActivity.getResources(), id);

				int[] pixels = new int[image.getWidth() * image.getHeight()];
				image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

				ByteBuffer buffer = ByteBuffer.allocate(image.getHeight() * image.getWidth() * 4);
				boolean hasAlpha = image.hasAlpha();

				//				BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
				//				int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

				//				ByteBuffer buffer = Util.CreateByteBuffer(image.getHeight() * image.getWidth() * 4);
				//				boolean hasAlpha = image.getColorModel().hasAlpha();

				for(int y = 0; y < image.getHeight(); y++)
				{
					for(int x = 0; x < image.getWidth(); x++)
					{
						int pixel = pixels[y * image.getWidth() + x];

						buffer.put((byte)((pixel >> 16) & 0xFF));
						buffer.put((byte)((pixel >> 8) & 0xFF));
						buffer.put((byte)((pixel) & 0xFF));
						if(hasAlpha)
							buffer.put((byte)((pixel >> 24) & 0xFF));
						else
							buffer.put((byte)(0xFF));
					}
				}

				buffer.flip();

				Texture resource = new Texture();
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, resource.getID());

				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

				GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, image.getWidth(), image.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);

				return resource;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}

			return null;
		}
	}
}