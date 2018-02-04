package objectprimatives;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import datastructures.IntersectionData;


/**
 * Basic parameters and methods used to define a quad.
 * 
 * @author  
 * @author Chen
 */
public class Quad extends Plane {
	protected Point3d p0,p1,p2,p3;
	
	
	/*
	 * Creates a quad.
	 */
	public Quad(Point3d p0, Point3d p1, Point3d p2) {
		super();
		
		this.p0 = new Point3d(p0); //bottom left
		this.p1 = new Point3d(p1); //bottom right
		this.p2 = new Point3d(p2); //top left;
		
		afterInitialize();
	}

	
	private void afterInitialize() {
		//Calculate the forth point.
		p3 = new Point3d( calcFourthPoint(p0, p1, p2) );
		
		//p1 - p0
		Vector3d p1Subp0 = new Vector3d( p1.getX() - p0.getX(),
				                         p1.getY() - p0.getY(),
				                         p1.getZ() - p0.getZ() );
		//p2 - p0
		Vector3d p2Subp0 = new Vector3d( p2.getX() - p0.getX(),
                                         p2.getY() - p0.getY(),
                                         p2.getZ() - p0.getZ() );
		
		//Set the normal: (p1 - p0) cross (p2 - p0)
		super.normal.cross(p1Subp0, p2Subp0);

		//Normalize normal.
		super.normal.normalize();
		
		//Set distance from origin that plane is: (normal) dot p0
		//Find the plane coefficient by substituting x,y,z with a point which is on the plane. 
		//for example p0 of the rectangle;
		super.distanceFromOrigin = -(super.normal.dot(p0));
	}
	
	
	private Point3d calcFourthPoint(Point3d p0, Point3d p1, Point3d p2) {
		Point3d p3 = new Point3d( p1.getX() + (p2.getX() - p0.getX()), 
				                  p1.getY() + (p2.getY() - p0.getY()),
				                  p1.getZ() + (p2.getZ() - p0.getZ()) );
		
		return p3;
	}
	
	
	/*
	 * Given a ray, determines if that ray will hit the cone.
	 * 
	 * @param ray Ray to be tested for intersection with cone.
	 */
	public IntersectionData calcIntersection(Ray ray2) {		
		Ray ray = new Ray(ray2.origin, ray2.direction);
		ray.direction.normalize();
		IntersectionData superIntersection = super.calcIntersection(ray);
		IntersectionData retVal = null;
		boolean pointInQuad = false;
		
		if(superIntersection != null) {
			Point3d intersectionPoint = new Point3d();
			double distance = superIntersection.distance;
			intersectionPoint = calcIntersectPoint(ray, distance);
			
			if( intersectionPoint != null ) {
				double dot00, dot01, dot02, dot11, dot12;
				double denominator, u, v;			
				
				//p2 - p0
				Vector3d v0 = new Vector3d( p2.getX() - p0.getX(), 
						                    p2.getY() - p0.getY(), 
						                    p2.getZ() - p0.getZ() );
				//p1 - p0
				Vector3d v1 = new Vector3d( p1.getX() - p0.getX(), 
										    p1.getY() - p0.getY(), 
										    p1.getZ() - p0.getZ() );
				//(intersection point) - p0
				Vector3d v2 = new Vector3d( intersectionPoint.getX() - p0.getX(), 
						                    intersectionPoint.getY() - p0.getY(), 
						                    intersectionPoint.getZ() - p0.getZ() );
				
				//Compute dot products.
				dot00 = v0.dot(v0);
				dot01 = v0.dot(v1);
				dot02 = v0.dot(v2);
				dot11 = v1.dot(v1);
				dot12 = v1.dot(v2);
				
				//Compute barycentric coordinates.
				denominator = 1 / (dot00 * dot11 - dot01 * dot01);
				u = (dot11 * dot02 - dot01 * dot12) * denominator;
				v = (dot00 * dot12 - dot01 * dot02) * denominator;
				
				//Check if point is in the quad.
				if ((u > 0) && (v > 0) && (u < 1) && (v < 1)) {
					pointInQuad = true;
				}
			}
		}
		
		if(pointInQuad){
			retVal = superIntersection;
		}
		
		return retVal;
	}


	/**
	 * @return the p0
	 */
	public Point3d getP0() {
		return p0;
	}


	/**
	 * @param p0 the p0 to set
	 */
	public void setP0(Point3d p0) {
		this.p0 = p0;
	}


	/**
	 * @return the p1
	 */
	public Point3d getP1() {
		return p1;
	}


	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(Point3d p1) {
		this.p1 = p1;
	}


	/**
	 * @return the p2
	 */
	public Point3d getP2() {
		return p2;
	}


	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(Point3d p2) {
		this.p2 = p2;
	}


	/**
	 * @return the p3
	 */
	public Point3d getP3() {
		return p3;
	}


	/**
	 * @param p3 the p3 to set
	 */
	public void setP3(Point3d p3) {
		this.p3 = p3;
	}
}