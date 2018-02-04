package illuminiationModels;

import java.awt.Color;

import objectprimatives.Shape;
import datastructures.IntersectionData;

/**
 * This illumination model provides a rendering of the object that 
 * has no shading, or reflections, and will look very 2D, or flat.
 * 
 * @author  
 * @author Chen
 *
 */
public class FlatIllumination extends IlluminationModel {

	public FlatIllumination(Shape parentShape) {
		super(parentShape);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Color getColor(IntersectionData rayIntersectionPoint) {
		return this.parentShape.getColor(shapeIntersection);
	}

}
