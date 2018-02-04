package illuminiationModels;

import java.awt.Color;

import datastructures.IntersectionData;

import objectprimatives.Ray;
import objectprimatives.Shape;

/**
 * This class provides a framework for implementing an illumination model.
 * 
 * @author  
 * @author Chen
 *
 */
public abstract class IlluminationModel {
	
	/**
	 * The shape this illumination model belongs to.
	 */
	protected Shape parentShape;
	
	/**
	 * Distance from ray to intersection, this might be optional.
	 * This is really just to save an extra distance calculation.
	 */
	protected double distance;
	
	/**
	 * Direction of view point for models where such a thing is important
	 */
	protected Ray ray;
	
	/**
	 * The intersection between the ray and the shape.
	 */
	protected IntersectionData shapeIntersection;
	
	public IlluminationModel (){
		throw new RuntimeException("Illumination models don't " +
				"have default constructors.  A shape is necessary.");
	}
	
	/**
	 * Constructor that sets the parent shape
	 * @param parentShape
	 */
	public IlluminationModel(Shape parentShape){
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
