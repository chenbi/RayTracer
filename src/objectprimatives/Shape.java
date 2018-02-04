package objectprimatives;

import illuminiationModels.FlatIllumination;
import illuminiationModels.IlluminationModel;

import java.awt.Color;

import javax.vecmath.Point3d;

import reflectionmodels.ReflectionModel;
import reflectionmodels.StandardReflection;
import transmissionmodels.TransmissionModel;
import colormodels.ColorModel;
import datastructures.IntersectionData;

/**
 * Basic parameters and methods used to define a shape.
 * 
 * @author  
 * @author Chen
 */
public abstract class Shape {
	protected Color color;           //r,g,b color values of shape.
	protected IlluminationModel illuminationModel = null;
	protected ReflectionModel reflectionModel = null;
	protected TransmissionModel transmissionModel = null;
	protected ColorModel colorModel = null;

	/**
	 * Creates a shape centered at the origin of the world.
	 */
	public Shape() {
		this.color = new Color(0, 0, 0);
		this.illuminationModel = new FlatIllumination(this);
		this.reflectionModel = new StandardReflection(this, 0, 0);
		
	}
	
	
	public TransmissionModel getTransmissionModel() {
		return transmissionModel;
	}


	public void setTransmissionModel(TransmissionModel transmissionModel) {
		this.transmissionModel = transmissionModel;
	}


	/**
	 * Calculates the x,y,z intersection point of a ray
	 * t distance away from the rays origin.
	 * 
	 * @param ray Ray being projected at shape.
	 * @param distance from rays origin to shape.
	 */
	protected Point3d calcIntersectPoint(Ray ray, double t) {
		Point3d intersectionPoint = new Point3d();
		
		ray.direction.normalize();
		
	    //Set x intersection point: x0 + dx * t
		intersectionPoint.setX( ray.origin.getX() + 
				               (ray.direction.getX() * t) );
	    //Set y intersection point: y0 + dy * t
		intersectionPoint.setY( ray.origin.getY() + 
	                           (ray.direction.getY() * t) );
	    //Set z intersection point: z0 + dz * t
		intersectionPoint.setZ( ray.origin.getZ() + 
	                           (ray.direction.getZ() * t) );
		
		return intersectionPoint;
	}
	
	
	/**
	 * Gets the distance a ray must travel to intersect the object.
	 * @param ray
	 * @return
	 */
	public abstract IntersectionData calcIntersection(Ray ray);


	/**
	 * @deprecated It doesn't make sense to have this. Once procedural or bitmap textures are loaded, coordinates will be needed.  
	 * Gets the color of the shape.
	 */
	public Color getColor() {
		return color;
	}
	
	public Color getColor(IntersectionData id){
		Color retVal = new Color(0,0,0);
		if(colorModel != null){
			retVal = colorModel.getColorAt(id);
		} else {
			retVal = color;
		}
		return retVal;
	}


	/**
	 * Sets the color of the shape.
	 * 
	 * @param color The color to apply to the shape.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Gets the illumination model for this object.
	 * @return
	 */
	public IlluminationModel getIlluminationModel() {
		return illuminationModel;
	}

	/**
	 * Sets the illumination model for this object. The default is flat.
	 * @param illuminationModel
	 */
	public void setIlluminationModel(IlluminationModel illuminationModel) {
		this.illuminationModel = illuminationModel;
	}
	
	
	public ReflectionModel getReflectionModel() {
		return reflectionModel;
	}


	public void setReflectionModel(ReflectionModel reflectionModel) {
		this.reflectionModel = reflectionModel;
	}



	/**
	 * Returns the apparent color of the object using the objects 
	 * illumination model.
	 * 
	 * @param intersectionPoint The point where the ray intersects the object 
	 * and a color needs to be returned.
	 * 
	 * @return
	 */
	public Color getIlluminationColor(IntersectionData intersection){
		return illuminationModel.getColor(intersection);
	}
	
	public Color getReflectionColor(IntersectionData intersection){
		return reflectionModel.getColor(intersection);
	}
	
	public Color getTransmissionColor(IntersectionData intersection){
		return transmissionModel.getColor(intersection);
	}
	
	public ColorModel getColorModel() {
		return colorModel;
	}


	public void setColorModel(ColorModel colorModel) {
		this.colorModel = colorModel;
	}
}