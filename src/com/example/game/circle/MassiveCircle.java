package com.example.game.circle;

import com.example.android3dengine.math.Vector2f;

public class MassiveCircle extends CollidingCircle {

	public MassiveCircle(Vector2f position, Vector2f speed, double radius, Vector2f bound, double mass, boolean stationary) {
		super(position, speed, radius, bound, mass, stationary);
	}

	/**
	 * A method to be called only once for each list of circles that need to be checked for collisions among each other.
	 *
	 * @param elapsedSeconds Seconds elapsed since the last call to this method.
	 * @param massiveCircles The list of circles that can collide and attract among each other.
	 */
	public static void update(double elapsedSeconds, MassiveCircle[] massiveCircles) {
		for(int j = 0; j < massiveCircles.length; j++) {
			for(int k = j + 1; k < massiveCircles.length; k++) {
				massiveCircles[j].attract(elapsedSeconds, massiveCircles[k]);
			}
		}

		insertionSort(massiveCircles);

		for(int j = 0; j < massiveCircles.length; j++) {
			if(massiveCircles[j].isStationary())
				continue;

			for(int k = j + 1; k < massiveCircles.length; k++) {
				if ((massiveCircles[j].position.getX() + massiveCircles[j].getRadius()) < (massiveCircles[k].position.getX() - massiveCircles[k].getRadius()))
					break;
				if((massiveCircles[j].position.getY() + massiveCircles[j].getRadius()) < (massiveCircles[k].position.getY() - massiveCircles[k].getRadius()) || (massiveCircles[k].position.getY() + massiveCircles[k].getRadius()) < (massiveCircles[j].position.getY() - massiveCircles[j].getRadius()))
					continue;

				massiveCircles[j].collideCircles(massiveCircles[k]);
			}

			massiveCircles[j].boundCheck();
			massiveCircles[j].move(elapsedSeconds);
		}
	}

	public void attract(double elapsedSeconds, CollidingCircle other) {
		Vector2f mag = this.position.sub(other.position);
		float squaredDist = mag.dot(mag);
		if(squaredDist > radius * radius) {
			float g = (float) ((0.667 * this.mass * other.mass) / squaredDist + 0.0001f);
			mag = mag.normalized();
			mag = mag.mul((float) (g * elapsedSeconds));
			other.speed = other.speed.add(mag.mul((float) this.mass));
			this.speed = this.speed.sub(mag.mul((float) other.mass));
		}
	}
}
