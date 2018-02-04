package objectprimatives;

import javax.vecmath.Vector3d;

import datastructures.IntersectionData;

/**
 * Basic parameters and methods used to define a plane.
 * 
 * @author  
 * @author Chen
 */
public class Plane extends Shape {
	//Distance from origin of world that plane is located at.
	protected double distanceFromOrigin;
	//Normal x,y,z values for a given point on the plane.
	protected Vector3d normal;
	
	
	/**
	 * Creates a plane on the x-axis with a normal of positive y.
	 */
	public Plane() {
		super();
		
		distanceFromOrigin = 0.0;
		normal = new Vector3d(0.0,1.0,0.0);
	
		normal.normalize();
	}
	
	
	/**
	 * Creates a plane a specific distance from the origin of the world
	 * with a normal of positive y.
	 * 
	 * @param distanceFromOrigin distance from origin of world to place plane.
	 */
	public Plane(double distanceFromOrigin) {
		super();
		
		this.distanceFromOrigin = distanceFromOrigin;
		normal = new Vector3d(0.0,1.0,0.0);
		
		normal.normalize();
	}

	
	/**
	 * Creates a plane a specific distance from the origin of the world with 
	 * a normal for any point on the plane.
	 * 
	 * @param distanceFromOrigin distance from origin of world to place plane.
	 * @param normal
	 */
	public Plane(double distanceFromOrigin, Vector3d normal) {
		super();
		
		this.normal = normal;
		this.distanceFromOrigin = distanceFromOrigin;
		
		this.normal.normalize();
	}
	
	
	/**
	 * 
	 */
	public IntersectionData calcIntersection(Ray ray2) {
		IntersectionData retVal = null;
		Ray ray = new Ray(ray2.origin, ray2.direction);
		ray.direction.normalize();
		
		//Normalize the normal.
		normal.normalize();
		
		//Distance from origin of ray to object surface.
		double distance = Double.POSITIVE_INFINITY;
		
		//distanceDenominator = (a * dx) + (b * dy) + (c * dz)
        //same as distanceDenominator = normalPoint.dotProduct(rayDirection)
		double distanceDenominator = (normal.getX() * ray.direction.getX()) + 
		                             (normal.getY() * ray.direction.getY()) + 
		                             (normal.getZ() * ray.direction.getZ());

		//If distanceDenominator is 0 then ray is parallel to plane, therefore no intersection.
		//Only calculate if not parallel.
		if( distanceDenominator != 0 ) {
			//distance = -( (a * x0) + (b * y0) + (c * z0) + f )
			//same as distance = -(normalPoint.dotProduct(rayOrigin) + distanceFromOrigin)
			distance = -( (normal.getX() * ray.origin.getX()) + 
		                  (normal.getY() * ray.origin.getY()) + 
		                  (normal.getZ() * ray.origin.getZ()) + distanceFromOrigin );
			distance = distance / distanceDenominator;
		}
		
		//If distance < 0: then the ray intersects behind the origin of the ray, ignore.
		if(distance >= 0){
			retVal = new IntersectionData(distance, normal, ray);
		}
		
	    return retVal;
	}

}