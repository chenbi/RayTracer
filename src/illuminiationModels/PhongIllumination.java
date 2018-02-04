package illuminiationModels;

import java.awt.Color;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import objectprimatives.LightSource;
import objectprimatives.Ray;
import objectprimatives.Shape;
import objectprimatives.World;
import renderer.Constants;
import datastructures.IntersectionData;

/**
 * 
 * @author  
 * @author Chen
 */
public class PhongIllumination extends IlluminationModel {
	protected double ambientReflection;			//Ka
	protected double diffuseReflection;			//Kd
	protected double specularReflection;			//Ks
	protected double hardness;			//Ks
	
	/**
	 * Constructor 
	 * @param shape The shape this model is applied to.
	 * @param ambient How bright the ambient light is.  Between 0 and 1
	 * @param diffuse How bright the diffuse light is.  Between 0 and 1
	 * @param specular How bright the specular light is.  Between 0 and 1
	 * @param specularEx How sharp the specular highlight is.  Greater than 0
	 */
	public PhongIllumination(Shape shape, double ambient, 
			double diffuse, double specular, double specularEx){
		super(shape);
		
		if(ambient > 1 || diffuse > 1 || specular > 1 ||
				ambient < 0 || diffuse < 0 || specular < 0){
			throw new RuntimeException("PhongIllumination.java: " +
					"ambient, diffuse and specular values must be between 1 and 0. " +
					"Spcular exponent values must be greater than 0");
		}
		
		this.ambientReflection = ambient;
		this.diffuseReflection = diffuse;
		this.specularReflection = specular;
		this.hardness = specularEx;
	}
	
	@Override
	public Color getColor(IntersectionData rayIntersectionPoint) {
		this.distance = rayIntersectionPoint.distance;
		this.ray = rayIntersectionPoint.rayFromCamera;
		this.shapeIntersection = rayIntersectionPoint;
		int red = 0;
		int green = 0;
		int blue = 0;
		
		//Gets adds the ambient, diffuse, and specular colors together to 
		//obtain the apparent color at this specific point.
		Color ambient = getAmbientColor();
		Color diffuse = getDiffuseColor();
		Color specular = getSpecularColor();
		red = ambient.getRed() + diffuse.getRed() + specular.getRed();
		if(red > 255)
			red = 255;
		green = ambient.getGreen() + diffuse.getGreen() + specular.getGreen();
		if(green > 255)
			green = 255;
		blue = ambient.getBlue() + diffuse.getBlue() + specular.getBlue();
		if(blue > 255)
			blue = 255;
		
		return new Color(red, green, blue);
	}
	
	/**
	 * Gets the ambient color of the object by taking the objects natural color 
	 * and multiplying it by the ambient light intensity.
	 * @return
	 */
	protected Color getAmbientColor(){
		//Values for the float constructor need to be between 0.0 and 1.0
		//so dividing by 255 gives a percentage.
		try{
		float red = ((float)parentShape.getColor(shapeIntersection).getRed()/(float)255) * (float)ambientReflection;
		float green = ((float)parentShape.getColor(shapeIntersection).getGreen()/(float)255) * (float)ambientReflection;
		float blue = ((float)parentShape.getColor(shapeIntersection).getBlue()/(float)255) * (float)ambientReflection;
		return new Color(red, green, blue);
		}catch(Exception e){
			System.out.println("blah");
		}
		return null;
	}
	
	/**
	 * Shoots a ray from this specific point on the object towards the light. 
	 * If another object in the world is between this point and the light then 
	 * this point has no diffuse color and is therefore in shadow.  If this 
	 * point can see the light, then fancy math happens to figure out how 
	 * intense it is based on the angle the light hits at.
	 * @return
	 */
	protected Color getDiffuseColor(){
		int red = 0;
		int green = 0;
		int blue = 0;
		World world = World.getInstance();
		Point3d intersectionPoint = ray.getPointAlongRay(distance);
		//For each light in the world
		for(LightSource source : world.lightSource){
			Ray rayToLight = new Ray(intersectionPoint);
			Vector3d direction = new Vector3d();
			//Get a vector than points from here, to the light.
			direction.sub(source.position, intersectionPoint);
			direction.normalize();
			rayToLight.direction = direction;
			boolean intersection = false;
			
			//make sure there is a clear path to the light source
			for(Shape shape : world.shapes){
				IntersectionData onWayToLight = shape.calcIntersection(rayToLight);
				if(onWayToLight != null){
					double distance =  onWayToLight.distance;
					//if the distance is not infinity it hit something.
					//if the distance is not greater than epsilon, it 
					//has hit its self, and should't count.  Rounding error.
					if(distance != Double.POSITIVE_INFINITY  && 
							distance != Double.NEGATIVE_INFINITY 
							&& (distance > Constants.EPSILON)){
						intersection = true;
						break;
					}
				}
			}
			
			//If there is a clear path, figure out the lighting amount
			if(!intersection){
				Vector3d shapeNormal =  shapeIntersection.normal;
				shapeNormal.normalize();
				double lightScaler = shapeNormal.dot(direction);
				
				//This happens if the vector angle is greater than 90degrees
				if(lightScaler < 0){
					lightScaler = 0;
				}
				
				//I don't know if this ever happens, but it can't hurt, right?
				if(lightScaler > 1){
					lightScaler = 1;
				}
				
				//lightScaler is a value of intensity at this point based on 
				//the angle of light
				red += (int)(source.color.getRed() * lightScaler);
				green += (int)(source.color.getGreen() * lightScaler);
				blue += (int)(source.color.getBlue() * lightScaler);
			}
			intersection = false;
		}
		
		red = (int)((red) * diffuseReflection);
		green = (int)((green)* diffuseReflection);
		blue = (int)((blue)* diffuseReflection);
		
		//Do a final check that the color values won't throw and exception.
		Color retVal = null;
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
		return retVal;
	}
	
	/**
	 * See comments for Diffuse.
	 * 
	 * This function has all the same features as diffuse except there is a 
	 * pow() call to lightScaler.
	 * @return
	 */
	protected Color getSpecularColor(){
		int red = 0;
		int green = 0;
		int blue = 0;
		World world = World.getInstance();
		Point3d intersectionPoint = ray.getPointAlongRay(distance);
		for(LightSource light : world.lightSource){
			Ray rayToLight = new Ray(intersectionPoint);
			Vector3d direction = new Vector3d();
			direction.sub(light.position, intersectionPoint);
			direction.normalize();
			rayToLight.direction = direction;
			boolean intersection = false;
			
			//make sure there is a clear path to the light source
			for(Shape shape : world.shapes){
				IntersectionData onWayToLight = shape.calcIntersection(rayToLight);
				if(onWayToLight != null){
					double distance =  onWayToLight.distance;
					//I don't know why epsilon has to be negative here, 
					//But it makes it work right. I'm sure there are demons 
					//somewhere in this code.  It's rounding error again for
					//sure though.
					if(distance != Double.POSITIVE_INFINITY  && 
							distance != Double.NEGATIVE_INFINITY 
							&& (distance > -Constants.EPSILON)){
						intersection = true;
						break;
					}
				}
			}
			
			//If there is a clear path, figure out the lighting amount
			if(!intersection){
				Vector3d shapeNormal =  shapeIntersection.normal;
				shapeNormal.normalize();
				
				Vector3d shapeToCameraDir = new Vector3d();
				shapeToCameraDir.negate(ray.direction);
				shapeToCameraDir.normalize();
				
				Vector3d reflectedLightDir = new Vector3d();
				reflectedLightDir = getReflectedVector(
						rayToLight.direction, shapeNormal);
				reflectedLightDir.normalize();
				
				double lightScaler = reflectedLightDir.dot(shapeToCameraDir);
				lightScaler = Math.pow(lightScaler, hardness);
				
				//This happens if the vector angle is greater than 90degrees
				if(lightScaler < 0){
					lightScaler = 0;
				}
				
				red += (int)(light.color.getRed() * lightScaler);
				green += (int)(light.color.getGreen() * lightScaler);
				blue += (int)(light.color.getBlue() * lightScaler);
			}
			intersection = false;
		}
		
		red = (int)((red) * specularReflection);
		green = (int)((green)* specularReflection);
		blue = (int)((blue)* specularReflection);
		
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
		
		return new Color(red, green, blue);
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
