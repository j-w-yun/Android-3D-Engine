package com.example.game.circle;

import com.example.android3dengine.math.Vector2f;

public class CollidingCircle extends BoundCircle {

	protected double mass;
	protected boolean stationary;
	protected final double SPEED_RESTITUTION_COLLISION = 0.01;

	public CollidingCircle(Vector2f position, Vector2f speed, double radius, Vector2f bound, double mass, boolean stationary) {
		super(position, speed, radius, bound);
		this.mass = mass;
		this.stationary = stationary;
	}

	/**
	 * A method to be called only once for each list of circles that need to be checked for collisions among each other.
	 *
	 * @param elapsedSeconds Seconds elapsed since the last call to this method.
	 * @param collidingCircles The list of circles that can collide among each other.
	 */
	public static void update(double elapsedSeconds, CollidingCircle[] collidingCircles) {
		insertionSort(collidingCircles);

		for(int j = 0; j < collidingCircles.length; j++) {
			if(collidingCircles[j].isStationary())
				continue;

			collidingCircles[j].boundCheck();
			collidingCircles[j].move(elapsedSeconds);

			for(int k = j + 1; k < collidingCircles.length; k++) {
				if ((collidingCircles[j].position.getX() + collidingCircles[j].getRadius()) < (collidingCircles[k].position.getX() - collidingCircles[k].getRadius()))
					break;

				if((collidingCircles[j].position.getY() + collidingCircles[j].getRadius()) < (collidingCircles[k].position.getY() - collidingCircles[k].getRadius()) ||
						(collidingCircles[k].position.getY() + collidingCircles[k].getRadius()) < (collidingCircles[j].position.getY() - collidingCircles[j].getRadius()))
					continue;

				collidingCircles[j].collideCircles(collidingCircles[k]);
			}
		}
	}

	/**
	 * Undo the overlap.
	 *
	 * @param other The other circle to undo the overlap with.
	 */
	protected void collideCircles(CollidingCircle other) {
		if(this.stationary && other.stationary)
			return;

		Vector2f delta = this.position.sub(other.position);
		double r = this.radius + other.radius;
		double squaredDistance = delta.dot(delta);

		/*
		 * No overlap
		 */
		if(squaredDistance > r * r)
			return;

		/*
		 * Find amount overlapping
		 */
		Vector2f amountOverlapping;
		double distance = delta.length();
		if(distance != 0)
			amountOverlapping = delta.mul((float) ((r - distance) / distance));
		else
			amountOverlapping = new Vector2f((float) r, 0).mul((float) ((r - distance - 1) / distance));

		/*
		 * Account for inertia when undoing overlap
		 */
		float invMassThis = (float) (1.0 / this.mass);
		float invMassOther = (float) (1.0 / other.mass);
		if(this.stationary && !other.stationary) {
			other.position = other.position.sub(amountOverlapping.mul(1.0f));
		} else if(other.stationary && !this.stationary) {
			this.position = this.position.add(amountOverlapping.mul(1.0f));
		} else if(!this.stationary && !other.stationary) {
			// This equation is imperative
			this.position = this.position.add(amountOverlapping.mul(invMassThis / (invMassThis + invMassOther)));
			other.position = other.position.sub(amountOverlapping.mul(invMassOther / (invMassThis + invMassOther)));
		}

		/*
		 * Impact speed
		 */
		Vector2f v = (this.speed.sub(other.speed));
		amountOverlapping = amountOverlapping.normalized();
		double vn = v.dot(amountOverlapping);

		/*
		 * Remove futile cycle of collisions
		 */
		if (vn > 0.0f)
			return;

		/*
		 * Collision impulse
		 */
		float i = (float) -((1.0 + SPEED_RESTITUTION_COLLISION) * vn) / (invMassThis + invMassOther);
		Vector2f impulse = amountOverlapping.normalized().mul(i);
		//		Vector2f impulse = amountOverlapping.mul(i);

		/*
		 * Change in momentum
		 */
		if(this.stationary && !other.stationary) {
			other.speed = other.speed.sub(impulse.mul(invMassOther + invMassThis));
		} else if(other.stationary && !this.stationary) {
			this.speed = this.speed.add(impulse.mul(invMassOther + invMassThis));
		} else if(!this.stationary && !other.stationary) {
			// This equation is imperative
			this.speed = this.speed.add(impulse.mul(invMassThis));
			other.speed = other.speed.sub(impulse.mul(invMassOther));
		}
	}

	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	public boolean isStationary() {
		return stationary;
	}
	public void setStationary(boolean stationary) {
		this.stationary = stationary;
	}
}
