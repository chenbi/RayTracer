package transmissionmodels;

import java.awt.Color;

import javax.vecmath.Vector3d;

import objectprimatives.Ray;
import objectprimatives.Shape;
import objectprimatives.World;
import datastructures.IntersectionData;

/**
 * This follows the standard transmission model used in RIT's CG2 course
 * 
 * @author  
 * @author Chen
 *
 */
public class StandardTransmission extends TransmissionModel {

	public float transmissionValue;
	
	/**
	 * Constructor
	 * @param parentShape The shape this model belongs to
	 * @param transmissionValue How transmittive the object is
	 * @param kt
	 */
	public StandardTransmission(Shape parentShape, float transmissionValue, double kt){
		super(parentShape);
		this.transmissionValue = transmissionValue;
		this.transmissionAmount = kt;
	}
	@Override
	public Color getColor(IntersectionData rayIntersectionPoint) {

		if(checkDiscriminant(rayIntersectionPoint)){
			//Get the transmitted ray going into the object
			IntersectionData transmitted = getTransmittedRay(rayIntersectionPoint, World.getInstance().transmissionValue, this.transmissionValue);
			
			//Find the intersection where the transmitted ray leaves the object
			//this is just to get distance.
			IntersectionData transmittedIntersection = 
				parentShape.calcIntersection(transmitted.rayFromCamera);
			
			//reverse the normal, as it is part of the equation
			transmittedIntersection.normal.negate(transmitted.normal);
			transmittedIntersection.rayFromCamera = transmitted.rayFromCamera;
			
			//Now we have a ray who's origin starts inside the sphere and shoots out 
			//of the sphere, when it leaves, it have to be transmitted again.
			IntersectionData outGoingTransmitted = getTransmittedRay(transmittedIntersection, this.transmissionValue, World.getInstance().transmissionValue);
			
			Double distance = Double.POSITIVE_INFINITY;
			Shape closestShape = null;
			IntersectionData possibleIntersection = null;
			IntersectionData closestIntersection = null;
			for(Shape shape : World.getInstance().shapes){
				possibleIntersection = shape.calcIntersection(outGoingTransmitted.rayFromCamera);
				if(possibleIntersection != null){
					if(possibleIntersection.distance < distance){
						distance = possibleIntersection.distance;
						if(possibleIntersection.distance == Double.POSITIVE_INFINITY || possibleIntersection.distance == Double.NEGATIVE_INFINITY){
							System.out.println("This is impossible");
						}
						closestShape = shape;
						closestIntersection = possibleIntersection;
					}
				}
			}
			
			Color retVal = World.getInstance().bgColor;
			if(closestShape != null){
//				retVal = closestShape.getColor(outGoingTransmitted);
				
				Color illuminationColor = closestShape.getIlluminationColor(closestIntersection);
				Color reflectionColor = closestShape.getReflectionColor(closestIntersection);
				int red = illuminationColor.getRed() + reflectionColor.getRed();
				int green = illuminationColor.getGreen() + reflectionColor.getGreen();
				int blue = illuminationColor.getBlue() + reflectionColor.getBlue();
				
				if(red > 255)
					red = 255;
				if(green > 255)
					green = 255;
				if(blue > 255)
					blue = 255;
				if(red < 0 ){
					red = 0;
				}
				if(green < 0 ){
					green = 0;
				}
				if(blue < 0 ){
					blue = 0;
				}
				retVal = new Color(red, green, blue);

			}
			
			
			return retVal;
		} else {
			return parentShape.getReflectionColor(rayIntersectionPoint);
		}
	}
	
	/**
	 * Takes a ray, gives a slightly adjusted ray.
	 * @param rayIntersectionPoint The ray traveling through the medium tv1 and hitting the interface between tv1 and tv2
	 * @param tv1 A value that represents how much light is distorted by the medium
	 * @param tv2 A value that represents how much light is distorted by the medium
	 * @return The ray traveling through tv2 after crossing from tv1 into tv2
	 */
	public IntersectionData getTransmittedRay(IntersectionData rayIntersectionPoint, float tv1, float tv2){
		
		Vector3d shapeToCameraDir = new Vector3d();
		shapeToCameraDir.negate(rayIntersectionPoint.rayFromCamera.direction);
		shapeToCameraDir.normalize();
		
		float dn = (float)shapeToCameraDir.dot(rayIntersectionPoint.normal);
		float nit = tv1 / tv2;
		float discriminant = (float)(1 + (Math.pow(nit, 2)*(( Math.pow(dn, 2) - 1 ))));
		
		
		Vector3d dPrime = new Vector3d();
		dPrime.scale(nit, rayIntersectionPoint.rayFromCamera.direction);
		
		Vector3d nPrime = new Vector3d();
		nPrime.scale(nit*(dn) - Math.sqrt(discriminant), rayIntersectionPoint.normal);
		
		Vector3d transmittedDir = new Vector3d();
		transmittedDir.add(dPrime, nPrime);
		
		IntersectionData retVal = new IntersectionData();
		
		Ray transmittedRay = new Ray(rayIntersectionPoint.rayFromCamera.getPointAlongRay(rayIntersectionPoint.distance),
				transmittedDir);
		
		retVal.rayFromCamera = transmittedRay;
		retVal.distance = Double.POSITIVE_INFINITY;
		if(parentShape.calcIntersection(transmittedRay) != null){
			retVal.normal = parentShape.calcIntersection(transmittedRay).normal;
		} else {
			retVal.normal = new Vector3d(0,0,0);
		}
		
		return retVal;
		
	}
	
	/**
	 * Checks to see if this ray ever leaves the object.  If it doesn't
	 * there is total internal reflection.
	 * @param rayIntersectionPoint
	 * @return
	 */
	private boolean checkDiscriminant(IntersectionData rayIntersectionPoint){
		
		Vector3d shapeToCameraDir = new Vector3d();
		shapeToCameraDir.negate(rayIntersectionPoint.rayFromCamera.direction);
		shapeToCameraDir.normalize();
		float dn = (float)shapeToCameraDir.dot(rayIntersectionPoint.normal);
		float nit = World.getInstance().transmissionValue / this.transmissionValue;
		float discriminant = (float)(1 + (Math.pow(nit, 2)*(( Math.pow(dn, 2) - 1 ))));
		return discriminant >= 0;
	}

}
