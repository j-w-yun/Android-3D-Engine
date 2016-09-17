package com.example.game.rope;

import com.example.android3dengine.math.Vector2f;

public class Rope {

	private Vector2f headNodePosition;
	private float length;
	private int numNodes;
	private float attraction;
	private RopeNode[] nodes;

	public Rope(Vector2f headNodePosition, float length, int numNodes, float attraction) {
		this.headNodePosition = headNodePosition;
		this.length = length;
		this.numNodes = numNodes;
		this.attraction = attraction;

		float preferredDistance = length / numNodes;

		nodes = new RopeNode[numNodes];
		for(int j = 0; j < numNodes; j++) {
			// Head is always on node[0]
			nodes[j] = new RopeNode(
					new Vector2f(headNodePosition.getX(), headNodePosition.getY()),
					preferredDistance,
					attraction);
		}
	}

	public void update(float delta) {
		for(int j = 1; j < numNodes; j++) {
			nodes[j].updateIndependent(delta);
		}
		nodes[0].updateIndependent(delta, 4000f);

		// Update according to upper node's position
		for(int j = 1; j < numNodes; j++) {
			nodes[j].updateDependent(delta, nodes[j - 1]);
		}
		//		nodes[1].updateDependent(delta, nodes[0], 1f);

		// Update according to lower node's position
		for(int j = 0; j < numNodes - 1; j++) {
			nodes[j].updateDependent(delta, nodes[j + 1]);
		}
		//		nodes[0].updateDependent(delta, nodes[1], 0.8f); // Head node's influence by snap-back force
	}

	public int getNumNodes() {
		return numNodes;
	}

	public RopeNode[] getNodes() {
		return nodes;
	}

	public class RopeNode {

		private float preferredDistance;
		private float attraction;
		private Vector2f velocity;
		private Vector2f position;

		public boolean immobile;

		private RopeNode(Vector2f position, float preferredDistance, float attraction) {
			this.position = position;
			this.preferredDistance = preferredDistance;
			this.attraction = attraction;

			this.velocity = new Vector2f(0, 0);
		}

		private void updateIndependent(float delta) {
			updateIndependent(delta, 0);
		}

		private void updateIndependent(float delta, float gravity) {
			if(immobile)
				return;

			// Update this node's velocity according to gravity
			this.velocity.setY(velocity.getY() + gravity * delta);

			// Update this node's position
			this.position.setX(this.position.getX() + velocity.getX() * delta);
			this.position.setY(this.position.getY() + velocity.getY() * delta);
		}

		private void updateDependent(float delta, RopeNode previousNode) {
			updateDependent(delta, previousNode, 0.97f);
		}

		private void updateDependent(float delta, RopeNode previousNode, float efficiency) {
			if(immobile)
				return;

			// Update this node's velocity according to its location away from the previous node
			Vector2f relativeDistance = this.position.sub(previousNode.position);
			float stretchedDistance = relativeDistance.length() - preferredDistance;


			Vector2f attractionVector = relativeDistance.normalized().mul(stretchedDistance);
			attractionVector = attractionVector.mul(attraction);
			this.velocity.setX(this.velocity.getX() - attractionVector.getX() * delta);
			this.velocity.setY(this.velocity.getY() - attractionVector.getY() * delta);


			this.velocity = velocity.mul(efficiency);

			// Update this node's position
			Vector2f newPos = new Vector2f(this.position.getX() + velocity.getX() * delta, this.position.getY() + velocity.getY() * delta);
			this.position = newPos;
		}

		public Vector2f getPosition() {
			return position;
		}

		public Vector2f getVelocity() {
			return velocity;
		}
	}
}