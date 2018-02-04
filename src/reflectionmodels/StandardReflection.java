package reflectionmodels;

import java.awt.Color;

import javax.vecmath.Vector3d;

import objectprimatives.Ray;
import objectprimatives.Shape;
import objectprimatives.World;
import datastructures.IntersectionData;
/**
 * Provides an implemented way to do reflection in the system
 * @author  
 * @author Chen
 *
 */
public class StandardReflection extends ReflectionModel {
	
	
	double falloff;
	int reflectionDepth;
	
	
	/**
	 * Constructor
	 * @param parentShape The shape to which this model belong
	 * @param falloff is how much light fades with each reflection.
	 * @param reflectionDepth is how many more times this ray can bounce before we say enough is enough.
	 */
	public StandardReflection(Shape parentShape, double falloff, int reflectionDepth){
		super(parentShape);
		this.falloff = falloff;
		this.reflectionDepth = reflectionDepth;
	}

	@Override
	public Color getColor(IntersectionData rayIntersectionPoint) {
		return getReflectedColor(rayIntersectionPoint, reflectionDepth);
	}
	
	@Override
	public Color getReflectedColor(IntersectionData rayIntersectionPoint, int depth) {
		Color retVal = null;
		//depth is how many more times this ray can bounce before we say enough is enough.
		if(depth == 0){
			//When enough is enough, just return black this color doesn't
			retVal = new Color(0,0,0);
		} else {
			Vector3d shapeNormal =  rayIntersectionPoint.normal;
			shapeNormal.normalize();
			
			Vector3d shapeToCameraDir = new Vector3d();
			shapeToCameraDir.negate(rayIntersectionPoint.rayFromCamera.direction);
			shapeToCameraDir.normalize();
			
			Vector3d reflectedLightDir = new Vector3d();
			reflectedLightDir = getReflectedVector(
					shapeToCameraDir, shapeNormal);
			reflectedLightDir.normalize();
			
			Ray reflectedRay = new Ray(rayIntersectionPoint.rayFromCamera.getPointAlongRay(
								rayIntersectionPoint.distance), reflectedLightDir);
			
			IntersectionData reflectedIntersection = null;
			double distance = Double.POSITIVE_INFINITY;
			Shape closestShape = null;
			IntersectionData closestIntersection = null;
			for(Shape shape : World.getInstance().shapes){
				reflectedIntersection = shape.calcIntersection(reflectedRay);
				if(reflectedIntersection != null){
					if(reflectedIntersection.distance < distance){
						distance = reflectedIntersection.distance;
						closestShape = shape;
						closestIntersection = reflectedIntersection;
//						IntersectionData ohGodWhy = closestShape.calcIntersection(reflectedRay);
//						if(ohGodWhy == null){
//							System.err.println("Oh god why");
//						}
					}
				}
			}
			
			if(closestShape != null){
				if(closestShape.getReflectionModel() != null){
					reflectedIntersection = closestIntersection;
					Color reflectedColor = closestShape.getReflectionModel().getReflectedColor(reflectedIntersection, (depth - 1));
					
					
					//To get the true illumination color the way this project is constructred
					//the any extra models applied to the object must also be called
					Color illuminationColor = closestShape.getIlluminationColor(closestIntersection);
					Color reflectionColor = closestShape.getReflectionColor(closestIntersection);
					Color transmissionColor = null;
					if(closestShape.getTransmissionModel() != null){
						transmissionColor = closestShape.getTransmissionColor(closestIntersection);
					}else {
						transmissionColor = new Color(0,0,0);
					}
					
					int red1 = illuminationColor.getRed() + reflectionColor.getRed() + transmissionColor.getRed();
					int green1 = illuminationColor.getGreen() + reflectionColor.getGreen() + transmissionColor.getGreen();
					int blue1 = illuminationColor.getBlue() + reflectionColor.getBlue() + transmissionColor.getBlue();
					
					if(red1 > 255)
						red1 = 255;
					if(green1 > 255)
						green1 = 255;
					if(blue1 > 255)
						blue1 = 255;
					if(red1 < 0 ){
						red1 = 0;
					}
					if(green1 < 0 ){
						green1 = 0;
					}
					if(blue1 < 0 ){
						blue1 = 0;
					}
					Color thisReflection = new Color(red1,green1,blue1);
					
					
					
					
					int red = (int)(thisReflection.getRed()*falloff) + (int)(reflectedColor.getRed() * falloff);
					int green = (int)(thisReflection.getGreen()*falloff) + (int)(reflectedColor.getRed() * falloff);
					int blue = (int)(thisReflection.getBlue()*falloff) + (int)(reflectedColor.getRed() * falloff);
					
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
				}else{
					retVal = new Color(0,0,0);
				}
			}else{
				Color worldcolor = World.getInstance().bgColor;
				retVal = new Color (
						(int)(worldcolor.getRed()*falloff),
						(int)(worldcolor.getGreen()*falloff),
						(int)(worldcolor.getBlue()*falloff)
						);
						
			}
		}
		
		//saftey check
		if(retVal == null){
			retVal = new Color(0,0,0);
		}
		return retVal;
		
	}
	
	/**
	 * Takes one vector and reflects it around the second vector.
	 * 
	 * @param rayIn Vector to be reflected
	 * @param normal Vector that gets reflected about.
	 * @return
	 */
	protected Vector3d getReflectedVector(Vector3d rayIn, Vector3d normal){
		double cosTheta = normal.dot(rayIn);
		Vector3d rayOut = new Vector3d();
		rayOut.x = 2 * cosTheta * normal.x;
		rayOut.y = 2 * cosTheta * normal.y;
		rayOut.z = 2 * cosTheta * normal.z;
		rayOut.sub(rayIn);
		
		return rayOut;
	}

}
