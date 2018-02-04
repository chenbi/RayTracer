package objectprimatives;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import datastructures.IntersectionData;

/**
 * A pixel is a representation of a single pixel on the computer screen.
 * By representing a pixel as an object in the program it becomes easier 
 * to separate things a pixel would care about from things that a camera or ray 
 * generally wouldn't.  Super sampling is an example of such a thing.
 * 
 * 
 * @author  <code> <mailto:   at rit dot edu></code>
 * @author Chen
 *
 */
public class Pixel {
	Point3d center;
	Point3d cameraPos;
	double pixelwidth;
	int superSamplingValue = 1;
	Vector3d up;
	Vector3d right;
	
	/**
	 * Constructor
	 * @param center The pixels starting position in world coordinates.
	 * @param pixelwidth The width of the pixel in world coordinates.
	 * @param cameraPos The position of the camera this pixel belongs to.
	 * @param samplingValue Super-sampling value will make a pixel be 1 X 1, 2 X 2, etc.  Defaults to 1
	 * @param up The up vector of the camera this pixel belongs to.
	 * @param right The right vector of the camera this pixel belongs to.
	 */
	public Pixel(
			Point3d center,
			double pixelwidth,
			Point3d cameraPos,
			int samplingValue, 
			Vector3d up, 
			Vector3d right){
		this.center = center;
		this.pixelwidth = pixelwidth;
		this.cameraPos = cameraPos;
		this.superSamplingValue = samplingValue;
		this.up = up;
		this.right = right;
	}
	
	/**
	 * The pixel averages together all colors obtained from rays during 
	 * super-sampling and returns a single color.
	 * @return
	 */
	public Color getColor(){
		int red = 0;
		int green = 0;
		int blue = 0;
		int counter = 0;
		ArrayList< ArrayList<Color> > colors = getColorsFromVectors();
		//Add the colors of all the pixels together and divide by
		//the number of pixels to get an average.
		for(ArrayList<Color> xColors : colors){
			for(Color color : xColors){
				red += color.getRed();
				blue += color.getBlue();
				green += color.getGreen();
				counter += 1;
			}
		}
		Color retVal = new Color((red/counter), (green/counter), (blue/counter));
		return retVal;
	}
	
	/**
	 * Takes the initial camera point and splits it into sub sections
	 * depending on how much super sampling is done.
	 * @return An array of points that are all within the bounds of 
	 * the original pixel.
	 */
	private ArrayList< ArrayList<Point3d> > getSuperSampledPoints(){
		ArrayList< ArrayList<Point3d> > retVal = new ArrayList< ArrayList<Point3d> >();
		for(int y = 0; y < superSamplingValue; y++) {
			ArrayList<Point3d> xTransformed = new ArrayList<Point3d>();
			//This is complicated and hard to explain.  Mail me or call me 
			//if you can't figure it out.
			double initialOffsetY = (2.0/(Math.pow(2, superSamplingValue))) / 2.0;
			double upperLeftY = -.5 + initialOffsetY + (y * (initialOffsetY * 2)); 
			for(int x = 0; x < superSamplingValue; x++) {
				double initialOffsetX = (2.0/(Math.pow(2, superSamplingValue))) / 2.0;
				double upperLeftX = -.5 + initialOffsetX + (x * (initialOffsetX * 2));  
				Point3d point = new Point3d(center);
				point.x += up.x * (upperLeftY) * pixelwidth;
				point.y += up.y * (upperLeftY) * pixelwidth;
				point.z += up.z * (upperLeftY) * pixelwidth;
				point.x += right.x * (upperLeftX) * pixelwidth;
				point.y += right.y * (upperLeftX) * pixelwidth;
				point.z += right.z * (upperLeftX) * pixelwidth;
				xTransformed.add(point);
			}
			retVal.add(xTransformed);
		}
		return retVal;
	}
	
	/**
	 * This takes a 2 dimensional array of points and converts them to 
	 * rays that originate from the camera.
	 * @param points
	 * @return An array of rays all eminating out from the bounds of
	 * the original pixel, in slightly different directions.
	 */
	private ArrayList< ArrayList<Ray> > getCameraRays() {

		ArrayList< ArrayList<Point3d> > points = getSuperSampledPoints();
		
		ArrayList< ArrayList<Ray> > retVal = 
			new ArrayList< ArrayList<Ray> >();

		//For each point, get the vector that goes from the camera
		//to that point, then throw them into a ray.
		for(ArrayList<Point3d> xPoints : points){
			ArrayList<Ray> xRays = new ArrayList<Ray>();
			for(Point3d xPoint : xPoints){
				Vector3d vec = new Vector3d();
				vec.x = xPoint.x - cameraPos.x;
				vec.y = xPoint.y - cameraPos.y;
				vec.z = xPoint.z - cameraPos.z;
				vec.normalize();
				Ray ray = new Ray(cameraPos, vec);
				xRays.add(ray);
			}
			retVal.add(xRays);
		}
		return retVal;
	}
	
	/**
	 * Takes a 2 dimensional list of rays and fires them into the world 
	 * if a ray hits an object, it returns a color;
	 * @param rays
	 * @return An array of colors that can be averaged together to 
	 * get a more accurate color for this pixel.
	 */
	private ArrayList< ArrayList<Color> > getColorsFromVectors() {

		ArrayList< ArrayList<Ray> > rays = getCameraRays();
		
		ArrayList<ArrayList<Color> > retVal = 
			new ArrayList<ArrayList<Color> >();

		ArrayList<Shape> shapes = World.getInstance().shapes;
		//For each ray, see if it intersects a shape in the world.
		//if it does, grab its color.
		for(ArrayList<Ray> xRays : rays ){
			ArrayList<Color> xColors = new ArrayList<Color>();
			for(Ray ray : xRays){
				Color color = World.getInstance().bgColor;
				Shape closestShape = null;
				double closestDistance = Double.POSITIVE_INFINITY;
				IntersectionData closestIntersection = null;
				
				for(Shape shape : shapes){
					IntersectionData current = shape.calcIntersection(ray);
					if( current != null ) {
						double intersectionDistance = current.distance;
						if(intersectionDistance < closestDistance){
							closestShape = shape;
							closestDistance = intersectionDistance;
							closestIntersection = shape.calcIntersection(ray);
						}
					}
				}
				if(closestShape != null && closestIntersection != null){
					Color iluminationColor = closestShape.getIlluminationColor(closestIntersection);
					Color reflectionColor = closestShape.getReflectionColor(closestIntersection);
					Color transmissionColor = new Color(0,0,0);
					if(closestShape.getTransmissionModel() != null){
						transmissionColor = closestShape.getTransmissionColor(closestIntersection);
						reflectionColor = new Color((int)(reflectionColor.getRed()*(1-closestShape.getTransmissionModel().transmissionAmount)),
								(int)(reflectionColor.getGreen()*(1-closestShape.getTransmissionModel().transmissionAmount)),
								(int)(reflectionColor.getBlue()*(1-closestShape.getTransmissionModel().transmissionAmount)));
					}
					
					
					int red = iluminationColor.getRed() + reflectionColor.getRed() + transmissionColor.getRed();
					int green = iluminationColor.getGreen() + reflectionColor.getGreen() + transmissionColor.getGreen();
					int blue = iluminationColor.getBlue() + reflectionColor.getBlue() + transmissionColor.getBlue();
					
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
					
					color = new Color(red, green, blue);
				}
				xColors.add(color);
			}
			retVal.add(xColors);
		}

		return retVal;
	}
}
