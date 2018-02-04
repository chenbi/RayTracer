package objectprimatives;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Basic parameters and methods used to define a ray.
 * 
 * @author  
 * @author Chen
 */
public class Ray {
	public Point3d origin;      //Starting x,y,z position of the ray.
	public Vector3d direction;  //x,y,z direction ray is going.
	
	/*
	 * Creates a ray with a specified origin.
	 * 
	 * @param origin Point in the world the ray starts at.
	 */
	public Ray(Point3d origin){
		this.origin = origin;
	}
	
	
	/*
	 * Creates a ray with a specified origin.
	 * 
	 * @param x x position in world.
	 * @param y y position in world.
	 * @param z z position in world.
	 */
	public Ray(double x, double y, double z){
		this.origin = new Point3d();
		this.origin.x = x;
		this.origin.y = y;
		this.origin.z = z;
	}
	
	/*
     *
	 */
	public Ray(Point3d origin, Vector3d direction) {
		this.origin = origin;
		this.direction = direction;
	}


	public Point3d getOrigin() {
		return origin;
	}


	public void setOrigin(Point3d origin) {
		this.origin = origin;
	}


	public Vector3d getDirection() {
		return direction;
	}


	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}
	
	public Point3d getPointAlongRay(double distance){
		Point3d retVal = new Point3d();
		retVal.x = origin.x + (direction.x * distance);
		retVal.y = origin.y + (direction.y * distance);
		retVal.z = origin.z + (direction.z * distance);
		
		return retVal;
	}
}