// from neil: barnes hutt algorithm

package io.schwartz.FlockingAgents;
import processing.core.*;
import java.util.ArrayList;


public class FlockingAgentsApplet extends PApplet {

	private static final long serialVersionUID = 1L;
	ArrayList<Boid> boids;
	
	public static void main(String args[]) {
	    PApplet.main(new String[] {"--present", "FlockingAgentsApplet"});
	}
	
	public void setup() {
		size(1280,720);
		background(50);
		boids = new ArrayList<Boid>();
		for (int i=0; i<500; i++) {
			boids.add(new Boid(random(width), random(height)));
		}
	}
	
	public void draw() {
		
		background(50);
		
		ArrayList<Boid> oldState = boids;
		for (Boid b : boids) {
			b.update(oldState);
			b.constrainToRect(0, 0, width, height);
			fill(155, 0, 255);
			stroke(155, 0, 255);
			ellipse(b.location.x, b.location.y, 2, 2);
		}
	}
}
