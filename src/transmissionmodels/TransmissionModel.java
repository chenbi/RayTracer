package transmissionmodels;

import java.awt.Color;

import objectprimatives.Shape;
import datastructures.IntersectionData;

/**
 * This provides an abstract definition of a transmission model for ray tracing.
 * 
 * @author  
 * @author Chen
 */
public abstract class TransmissionModel {
	/**
	 * The intersection between the ray and the shape.
	 */
	protected IntersectionData shapeIntersection;
	/**
	 * The shape this illumination model belongs to.
	 */
	protected Shape parentShape;
	
	/**
	 * A number between 1 and 0 that is the percent of light transmitted.
	 */
	public double transmissionAmount; //kt
	
	public TransmissionModel (){
		throw new RuntimeException("Transmission models don't " +
				"have default constructors.  A shape is necessary.");
	}
	
	/**
	 * Constructor that sets the parent shape
	 * @param parentShape
	 */
	public TransmissionModel(Shape parentShape){
		transmissionAmount = 1;
		this.parentShape = parentShape;
	}
	
	/**
	 * Implements the illumination model to get the color
	 * @param rayIntersectionPoint This is the point in the world at 
	 * which the a ray has intersected the shape and need to obtain
	 * the shapes color.
	 * @return
	 */
	public abstract Color getColor(IntersectionData rayIntersectionPoint);
}
