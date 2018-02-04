package reflectionmodels;

import java.awt.Color;

import objectprimatives.Shape;
import datastructures.IntersectionData;

/**
 * Provides an abstraction for doing reflection in the system.
 * 
 * I'm not really sure if there are any other ways to do reflection in 
 * ray tracing, but just in case, we're ready.
 * @author  
 * @author Chen
 *
 */
public abstract class ReflectionModel {
	
	public ReflectionModel (){
		throw new RuntimeException("Reflection models don't " +
				"have default constructors.  A shape is necessary.");
	}
	
	/**
	 * Constructor that sets the parent shape
	 * @param parentShape
	 */
	public ReflectionModel(Shape parentShape){
		this.parentShape = parentShape;
	}
	
	/**
	 * The shape this illumination model belongs to.
	 */
	protected Shape parentShape;
	
	/**
	 * The intersection between the ray and the shape.
	 */
	protected IntersectionData shapeIntersection;
	
	/**
	 * Implements the illumination model to get the color
	 * @param rayIntersectionPoint This is the point in the world at 
	 * which the a ray has intersected the shape and need to obtain
	 * the shapes color.
	 * @return
	 */
	public abstract Color getColor(IntersectionData rayIntersectionPoint);
	
	/**
	 * This is the recursive call to get the color.  Generally there is no reason 
	 * for anything outside the class to call this.
	 * @param rayIntersectionPoint
	 * @param depth Number of reflections
	 * @return The color of a single recursive reflection.
	 */
	public abstract Color getReflectedColor(IntersectionData rayIntersectionPoint, int depth);

}
