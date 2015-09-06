package io.schwartz.FlockingAgents;

import java.util.ArrayList;

import toxi.geom.*;

public class Boid {
	
	Vec2D location;
	Vec2D velocity;
	Vec2D acceleration;
	float maxForce;
	float maxSpeed;
	float mass;
	
	Boid(float x, float y) {
		acceleration = new Vec2D(0, 0);
		location = new Vec2D(x, y);
		velocity = Vec2D.randomVector();
		maxForce = 0.1f;
		maxSpeed = 0.5f;
		mass = 10f;
	}
	
	void constrainToRect(float x, float y, float width, float height) {
		if (location.x < x) {
			location.setX(location.x + width);
		}
		else if (location.x > x + width) {
			location.setX(location.x - width);
		}
		
		if (location.y < y) {
			location.setY(location.y + height);
		}
		else if (location.y > y + height) {
			location.setY(location.y - height);
		}
	}
	
	void update(ArrayList<Boid> boids) {
		acceleration.scaleSelf(0);
		velocity.jitter(maxSpeed/4);
		applyForce(separate(boids).scaleSelf(2.5f));
		applyForce(align(boids).scaleSelf(0.5f));
		applyForce(cohesion(boids).scaleSelf(1.0f));
		velocity.addSelf(acceleration).limit(maxSpeed);
		location.addSelf(velocity);
	}
	
	void applyForce(Vec2D force) {
		acceleration.addSelf(force.scaleSelf(1/mass));
	}
	
	Vec2D separate(ArrayList<Boid> boids) {
		float desiredSeparation = 5.0f;
		Vec2D separationForce = new Vec2D();
		int relevantBoids = 0;
		
		for (Boid other : boids) {
			float d = location.distanceTo(other.location);
			if (d > 0 && d < desiredSeparation) {
				Vec2D awayFromOther = location.sub(other.location);
				awayFromOther.normalize();
				awayFromOther.scaleSelf(1/d);
				separationForce.addSelf(awayFromOther);
				relevantBoids++;
			}
		}
		
		if (relevantBoids > 0) {
			separationForce.scaleSelf(1.0f/relevantBoids);
		}
		
		if (separationForce.magnitude() > 0) {
			separationForce.normalizeTo(maxSpeed);
			separationForce.subSelf(velocity);
			separationForce.limit(maxForce);
		}
		
		return separationForce;
	}
	
	Vec2D align(ArrayList<Boid> boids) {
		float neighborDistance = 50.0f;
		Vec2D localAverageVelocity = new Vec2D();
		int relevantBoids = 0;
		
		for (Boid other : boids) {
			float d = location.distanceTo(other.location);
			if (d > 0 && d < neighborDistance) {
				localAverageVelocity.addSelf(other.velocity);
				relevantBoids++;
			}
		}
		
		if (relevantBoids > 0) {
			localAverageVelocity.scaleSelf(1.0f/relevantBoids);
			localAverageVelocity.normalizeTo(maxSpeed);
			localAverageVelocity.subSelf(velocity);
			localAverageVelocity.limit(maxForce);
		}
		
		return localAverageVelocity;
	}
	
	Vec2D cohesion(ArrayList<Boid> boids) {
		float neighborDistance = 50.0f;
		Vec2D localAveragePosition = new Vec2D();
		int relevantBoids = 0;
		
		for (Boid other : boids) {
			float d = location.distanceTo(other.location);
			if (d > 0 && d < neighborDistance) {
				localAveragePosition.addSelf(other.location);
				relevantBoids++;
			}
		}
		
		if (relevantBoids > 0) {
			localAveragePosition.scaleSelf(1.0f/relevantBoids);
			localAveragePosition.subSelf(location);
			localAveragePosition.normalizeTo(maxSpeed);
			return localAveragePosition.subSelf(velocity).limit(maxForce);
		}
		else {
			return new Vec2D();
		}
	}

}
