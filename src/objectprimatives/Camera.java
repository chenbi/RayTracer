package objectprimatives;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import renderer.Constants;

/**
 * The camera has a single public function called render that snaps 
 * a picture of whatever it is looking at, just as a real camera does.
 * This allows the user to get a 2D array of pixel colors that they can 
 * display on a computer screen in any fashion they see fit.
 * 
 * @author  
 * @author Chen
 *
 */
public class Camera {
	public Point3d position, lookat;
	public Vector3d up;
	public ArrayList< ArrayList<Color> > colors;
	public int screenWidth, screenHeight;
	public double fieldOfView, nearClipping, farClipping, aspect;
	
	private int superSamplingValue = 1;
	private double pixelWidth = 0;

	public Camera() {
		this.position = new Point3d(0.0,0.0,1.0);
		this.lookat = new Point3d(0.0,0.0,0.0);
		this.up = new Vector3d(0, -1, 0);
		this.fieldOfView = 90;
		this.screenWidth = Constants.WINDOW_WIDTH;
		this.screenHeight = Constants.WINDOW_HEIGHT;
		this.aspect = (double)Constants.WINDOW_HEIGHT / (double)Constants.WINDOW_WIDTH ;
		this.nearClipping = 1;
		this.farClipping = 60;
		colors = new ArrayList< ArrayList<Color> >();


		//Always keep these at the bottom to ensure the rest of the parameters have
		//valid values.
		pixelWidth = getPixelWidth();
	}

	/**
	 * Constructor
	 * @param position The cameras position in the world
	 * @param lookat The point in the world the camera is looking at
	 * @param up The direction the camera thinks is up.  
	 * This must not be the same direction the camera is pointing.
	 */
	public Camera(Point3d position, Point3d lookat, Vector3d up) {
		this.position = position;
		this.lookat = lookat;
		this.up = up;
		this.fieldOfView = 90;
		this.screenWidth = Constants.WINDOW_WIDTH;
		this.screenHeight = Constants.WINDOW_HEIGHT;
		this.aspect = (double)Constants.WINDOW_HEIGHT / (double)Constants.WINDOW_WIDTH ;
		this.nearClipping = 1;
		this.farClipping = 60;
		colors = new ArrayList< ArrayList<Color> >();


		//Always keep these at the bottom to ensure the rest of the parameters have
		//valid values.
		pixelWidth = getPixelWidth();
	}
	
	
	/**
	 * This returns an array of pixels containing the rendered
	 * image.
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<Color> > render() {
		return getColorFromPixels();
	}
	
	/**
	 * Gets the width of a pixel by getting the total width
	 * and dividing by the number of pixels 
	 * @return width of pixel
	 */
	private double getPixelWidth(){
		return ((Math.tan(fieldOfView / 2.0) * nearClipping) * 2.0)/Constants.WINDOW_WIDTH;
	}

	
	/**
	 * Gets the direction of the camera in the world coordinate system.
	 * @return direction camera is pointing in the world.
	 */
	private Vector3d getCameraDir() {
		Vector3d retVal = new Vector3d(lookat);
		retVal.sub(position);
		retVal.normalize();
		return retVal;
	}

	/**
	 * This method determines where a pixel is placed in the world. 
	 * It determines this few a few general steps.
	 * 
	 * Get three vectors that describe the cameras angle.
	 * Place the pixel at the camera.
	 * Move the pixel along each vector into its position.
	 * 
	 * @return
	 */
	private ArrayList< ArrayList<Pixel> > transformToWorldCoordinates() {
		
		//return a list of pixels
		ArrayList< ArrayList<Pixel> > retVal = new ArrayList< ArrayList<Pixel> >();
		
		//Get the camera direction vectors
        Vector3d right = new Vector3d();
        Vector3d up = new Vector3d();
        Vector3d dir = getCameraDir();
        right.cross(dir, this.up);
        up.cross(dir, right);

        
		for(int y = 0; y < Constants.WINDOW_HEIGHT; y++) {
			ArrayList<Pixel> xTransformed = new ArrayList<Pixel>();
			double upperLeftY= (aspect * ((double)y)/ Constants.WINDOW_HEIGHT) - (aspect * .5);
			for(int x = 0; x < Constants.WINDOW_WIDTH; x++) {
				double upperLeftX = ((double)x)/ Constants.WINDOW_WIDTH -  .5;
				//sets the pixel the be at the cameras position
				Point3d point = new Point3d(position);
				
				//Moves the pixels along the directional axis' into the
				//correct positions.
				point.x += dir.x * nearClipping;
				point.y += dir.y * nearClipping;
				point.z += dir.z * nearClipping;
				point.x += up.x * upperLeftY;
				point.y += up.y * upperLeftY;
				point.z += up.z * upperLeftY;
				point.x += right.x * upperLeftX;
				point.y += right.y * upperLeftX;
				point.z += right.z * upperLeftX;
				
				xTransformed.add(new Pixel(point, pixelWidth, position, superSamplingValue, up, right));
			}
			retVal.add(xTransformed);
		}

		
		return retVal;
	}
	
	/**
	 * This function gets a list of pixel coordinates, then asks the 
	 * pixels what they see.
	 * @return
	 */
	private ArrayList< ArrayList<Color> >  getColorFromPixels(){
		ArrayList< ArrayList<Color> > retVal = 
			new ArrayList< ArrayList<Color> >(); //array to be filled
		
		ArrayList< ArrayList<Pixel> > pixels = 
			transformToWorldCoordinates(); //get a list of pixel coords
		
		//for each pixel, get its color and put it in a color array.
		for(ArrayList<Pixel> xPixels : pixels){
			ArrayList<Color> xColors = new ArrayList<Color>();
			for(Pixel pixel : xPixels){
				xColors.add(pixel.getColor());
			}
			retVal.add(xColors);
		}
		return retVal;
		
	}

	/**
	 * Get the super sampling value
	 * @return value
	 */
	public double getSuperSamplingValue() {
		return superSamplingValue;
	}

	/**
	 * The super sampling value is the number of times the pixel will 
	 * be split to get a more accurate color.  A value of 2 will split 
	 * the pixel 2^2 times, 3 with split 3^2.
	 * 
	 * @param superSamplingValue
	 */
	public void setSuperSamplingValue(int superSamplingValue) {
		this.superSamplingValue = superSamplingValue;
	}
}