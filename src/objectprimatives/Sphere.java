package objectprimatives;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import renderer.Constants;
import datastructures.IntersectionData;

/**
 * Basic parameters and methods used to define a sphere.
 * 
 * @author  
 * @author Chen
 */
public class Sphere extends Shape {
	protected Point3d centerPoint;  //Center point of sphere in world. 
	protected double radius;    //Size of the radius of sphere object.
	
	
	/*
	 * Creates a sphere object with a radius of 1 centered
	 * at the origin of the world.
	 */
	public Sphere(){
		super();
		
		centerPoint = new Point3d(0.0, 0.0, 0.0);
		radius = 1;
	}
	
	
	/*
	 * Creates a sphere object centered at the origin of the world 
	 * with a desired radius length.
	 * 
	 * @param radius length of radius of the sphere.
	 */
	public Sphere(double radius){
		super();
		
		centerPoint = new Point3d(0.0, 0.0, 0.0);
		this.radius = radius;
	}
	
	
	/*
	 * Creates a sphere object with a predefined radius length and 
	 * center location.
	 * 
	 * @param centerPoint point in the world sphere is centered around.
	 * @param radius length of radius of the sphere.
	 */
	public Sphere(Point3d centerPoint, double radius){
		super();
		
		this.centerPoint = centerPoint;
		this.radius = radius;
	}
	
	
	/*
	 * 
	 */
	public IntersectionData calcIntersection(Ray ray) {
		IntersectionData retVal = null;
		
		//The distance from the origin of the ray to the point of intersection.
		double distance = Double.POSITIVE_INFINITY;
		double b, c, x, y, z;
		
		//ray.direction.sub(ray.origin);
		
		//Normalize direction of ray.
		ray.direction.normalize();

		//dx * (x0 - xc)
		x = ray.direction.getX() * (ray.origin.getX() - centerPoint.getX());
		//dy * (y0 - yc)
		y = ray.direction.getY() * (ray.origin.getY() - centerPoint.getY());
		//dz * (z0 - zc)
		z = ray.direction.getZ() * (ray.origin.getZ() - centerPoint.getZ());
		//b = 2 * (dx * (x0 - xc) + dy * (y0 - yc) + dz * (z0 - zc))
		b = 2 * (x + y + z);
		
		//(x0 - xc)^2
		x = Math.pow((ray.origin.getX() - centerPoint.getX()),2);
		//(y0 - yc)^2
		y = Math.pow((ray.origin.getY() - centerPoint.getY()),2);
		//(z0 - zc)^2
		z = Math.pow((ray.origin.getZ() - centerPoint.getZ()),2);
		//c = (x0 - xc)^2 + (y0 - yc)^2 + (z0 - zc)^2 - r^2
		c = x + y + z - Math.pow(this.radius,2);
		
		//System.out.println("a: " + a + " b: " + b + " c: " + c);
		
        //b^2 - 4c from quadratic equation without 'a' because it was normalized to 1.
		double quadraticRoot = Math.pow(b,2) - (4 * c);
		//System.out.println("quadraticRoot: " + quadraticRoot);
		
		//If quadraticRoot less then zero, no real root, therefore no intersection.
		//If quadraticRoot equal to zero, one root, ray intersects at sphere’s surface.
		//If quadraticRoot greater then zero, two roots, ray goes through sphere.
		//Use least positive root 't'.
		if( quadraticRoot >= 0.0 ) {
		    //t = (-b - sqrt(b^2 - 4c)) / 2
			//No need to solve for t = (-b +/- sqrt(b^2 - 4c)) / 2 because
			//If quadraticRoot is less then zero, no real root, therefore no intersection.
			//This means quadraticRoot always >= 0 if the ray intersects with the sphere.
			//-b - any positive number would always be less then -b + any positive number.
			//The less of the 2 roots is the first intersection point and where we want to draw.
			double root0 = (-b - Math.sqrt(quadraticRoot)) / 2;
			double root1 = (-b + Math.sqrt(quadraticRoot)) / 2;
				
			if(root0 > Constants.EPSILON && root1 > Constants.EPSILON)
			{
				//Use smallest root.
				distance = Math.min(root0, root1);
			}
			else if (root0 <= Constants.EPSILON && root1 <= Constants.EPSILON)
			{
				distance = Double.POSITIVE_INFINITY;
			}
			else
			{
				distance = Math.max(root0, root1);
			}
		}
		
		//distance must be >=0, otherwise the intersection is behind the origin of the ray.
		if(distance >= -Constants.EPSILON && distance != Double.POSITIVE_INFINITY){
			Vector3d normal = new Vector3d();
			
			normal.sub(ray.getPointAlongRay(distance), centerPoint);
			normal.normalize();
			retVal = new IntersectionData(distance, normal, ray);
		}
		return retVal;
	}
	
	
	/*
	 * Gets the x,y,z coordinates of sphere center in world.
	 */
	public Point3d getcenterPoint() {
		return centerPoint;
	}


	/*
	 * Sets the x,y,z coordinates of sphere center in world.
	 *
	 * @param centerPoint x,y,z position to center sphere at.
	 */
	public void setWorldCoords(Point3d centerPoint) {
		this.centerPoint = centerPoint;
	}
	
	
	/*
	 * Used to get the radius of the sphere.
	 */
	public double getRadius() {
	    return radius;
	}


	/*
	 * Used to set the radius of the sphere.
	 * 
	 * @param radius The radius of the sphere.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}
}