package datastructures;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import objectprimatives.Ray;

/**
 * 
 * @author  
 *
 */
public class IntersectionData {
	public Double distance;
	public Vector3d normal;
	public Ray rayFromCamera;
	
	/**
	 * 
	 */
	public IntersectionData(){
		distance = Double.POSITIVE_INFINITY;
		normal = null;
	}
	
//	/**
//	 * 
//	 * @param distance
//	 * @param normal
//	 * @param point
//	 */
//	public IntersectionData(double distance, Vector3d normal){
//		this.distance = distance;
//		this.normal = normal;
//	}
	
	/**
	 * 
	 * @param distance
	 * @param normal
	 * @param point
	 */
	public IntersectionData(double distance, Vector3d normal, Ray rayFromCamera){
		this.distance = distance;
		this.normal = normal;
		this.rayFromCamera = rayFromCamera;
	}
}
