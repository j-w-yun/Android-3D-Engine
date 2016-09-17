package com.example.game.circle;

import com.example.android3dengine.math.Vector2f;
import com.example.android3dengine.math.Vector3f;

/**
 * A very basic circle with information about its position, speed, and radius.
 *
 * @author Jaewan Yun
 */
public class Circle {

	protected Vector2f position;
	protected Vector2f speed;
	protected double radius;
	protected final double MINIMUM_SQUARED_SPEED = 0;
	protected final double GRAVITY = -9.8;
	protected final double KINETIC_FRICTION_COEFFICIENT = 0;

	/**
	 * Prevent default constructor call.
	 */
	@SuppressWarnings("unused") private Circle() {throw new UnsupportedOperationException();}

	/**
	 * The only constructor available.
	 *
	 * @param position The starting position of a circle.
	 * @param speed The starting speed of a circle.
	 * @param radius The radius of a circle.
	 */
	public Circle(Vector2f position, Vector2f speed, double radius) {
		this.position = position;
		this.speed = speed;
		this.radius = radius;
	}

	/**
	 * Update the position based on speed and the time passed.
	 *
	 * @param elapsedSeconds Seconds elapsed since the last call to this method.
	 */
	protected void move(double elapsedSeconds) {
		if(speed.dot(speed) < MINIMUM_SQUARED_SPEED) {
			speed.setX(0);
			speed.setY(0);
		}

		speed = speed.add(new Vector2f(
				(float) (speed.getX() * (KINETIC_FRICTION_COEFFICIENT * GRAVITY * elapsedSeconds)),
				(float) (speed.getY() * (KINETIC_FRICTION_COEFFICIENT * GRAVITY * elapsedSeconds))));
		position = position.add(speed.mul((float) elapsedSeconds));
	}

	/**
	 * Check if this circle overlaps with another circle.
	 *
	 * @param other The other circle to check overlaps with.
	 * @return True if the two overlap. False if otherwise.
	 */
	protected boolean overlapping(Circle other) {
		Vector2f delta = this.position.sub(other.position);
		if(delta.dot(delta) < Math.pow(this.radius + other.radius, 2))
			return true;
		return false;
	}

	/**
	 * Check overlap between a point and this circle.
	 *
	 * @param point A vector to check if it touches this circle.
	 * @return True if the point touches this circle, false if otherwise.
	 */
	public boolean overlapping(Vector2f point) {
		Vector2f delta = this.position.sub(point);
		if(delta.dot(delta) < Math.pow(this.radius, 2))
			return true;
		return false;
	}

	public Vector2f getPosition() {
		return position;
	}
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	public Vector2f getSpeed() {
		return speed;
	}
	public void setSpeed(Vector2f speed) {
		this.speed = speed;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Vector3f getColor() {
		float g = 0.5f;
		float b = 0.6f;
		float r = (float) speed.dot(speed) / 300000;
		if(r >= 1.0f) {
			b = 0.3f;
			g = 0.5f / r;
			r /= r;
		}
		//		System.out.println(r + " " + g + " " + b);
		return new Vector3f(g, r, b);
	}
}
