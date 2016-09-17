package com.example.game.circle;

import com.example.android3dengine.math.Vector2f;

public class BoundCircle extends Circle implements Comparable<Circle> {

	protected Vector2f bound;
	protected final double SPEED_RESTITUTION_COLLISION = 1.001;

	public BoundCircle(Vector2f position, Vector2f speed, double radius, Vector2f bound) {
		super(position, speed, radius);
		this.bound = bound;
	}

	public static void update(double elapsedSeconds, BoundCircle[] collidingCircles) {
		insertionSort(collidingCircles);

		for(int j = 0; j < collidingCircles.length; j++) {
			collidingCircles[j].boundCheck();
			collidingCircles[j].move(elapsedSeconds);
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	protected static void insertionSort(Comparable[] a) {
		for(int j = 1; j < a.length; j++ ) {
			Comparable tmp = a[j];
			int k = j;
			for(; k > 0 && tmp.compareTo(a[k - 1]) < 0; k--)
				a[k] = a[k - 1];
			a[k] = tmp;
		}
	}

	@Override
	public int compareTo(Circle circle) {
		if(this.position.getX() - this.getRadius() > circle.position.getX() - circle.getRadius())
			return 1;
		else if(this.position.getX() - this.getRadius() < circle.position.getX() - circle.getRadius())
			return -1;
		else
			return 0;
	}

	/**
	 * Check collision with walls.
	 */
	protected void boundCheck() {
		if(bound == null)
			return;

		if(position.getX() - radius < 0) { // Right or left walls
			position.setX((float) radius);
			speed.setX((float) (-speed.getX() * SPEED_RESTITUTION_COLLISION));
			speed.setY((float) (speed.getY() * SPEED_RESTITUTION_COLLISION));
		} else if(position.getX() + radius > bound.getX()) {
			position.setX((float) (bound.getX() - radius));
			speed.setX((float) (-speed.getX() * SPEED_RESTITUTION_COLLISION));
			speed.setY((float) (speed.getY() * SPEED_RESTITUTION_COLLISION));
		}

		if(position.getY() - radius < 0) { // Top or bottom walls
			position.setY((float) radius);
			speed.setY((float) (-speed.getY() * SPEED_RESTITUTION_COLLISION));
			speed.setX((float) (speed.getX() * SPEED_RESTITUTION_COLLISION));
		} else if(position.getY() + radius > bound.getY()) {
			position.setY((float) (bound.getY() - radius));
			speed.setY((float) (-speed.getY() * SPEED_RESTITUTION_COLLISION));
			speed.setX((float) (speed.getX() * SPEED_RESTITUTION_COLLISION));
		}
	}

	public Vector2f getBound() {
		return bound;
	}
	public void setBound(Vector2f bound) {
		this.bound = bound;
	}
}
