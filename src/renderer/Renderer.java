package renderer;

import illuminiationModels.PhongIllumination;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import objectprimatives.Camera;
import objectprimatives.LightSource;
import objectprimatives.Quad;
import objectprimatives.Sphere;
import objectprimatives.World;
import reflectionmodels.StandardReflection;
import transmissionmodels.StandardTransmission;
import colormodels.CheckerBoardColorModel;

/**
 * Renders an image of a section of the world.
 * 
 * The general flow goes as follows
 * main() -> initialize() -> paintComponent()
 * 
 * @author  
 * @author Chen
 */
public class Renderer extends JComponent {
	private static final long serialVersionUID = 1L;
	private static ArrayList< ArrayList<Color> > sceneColors;
	
	/**
	 * This is the method where the world is set up.
	 * Any positioning of lights, cameras, objects should happen here
	 * Any addition of color models, illumination models, reflection models should also happen here.
	 * 
	 * After the initialize function finishes running, the sceneColors variable 
	 * should contain a two-dimensional array of colors that depict the scene.  This 
	 * array can then later be used to draw colors on a canvas.
	 */
	private static void initialize() {
		
		//Sphere1
		Sphere sphere1 =  new Sphere(new Point3d(-3,-1.5,3), 2);
		sphere1.setColor( new Color(0, 0, 0) );		//black
		sphere1.setIlluminationModel(new PhongIllumination(sphere1, 0.6, 0.2, 0.8, 50) );	
		sphere1.setReflectionModel(new StandardReflection(sphere1, 1, 3));
		
		//Sphere2
		Sphere sphere2 =  new Sphere(new Point3d(0,0,8), 2);
		sphere2.setReflectionModel(new StandardReflection(sphere2, 1, 3));
		sphere2.setTransmissionModel(new StandardTransmission(sphere2, 0.95f, 1));
		sphere2.setIlluminationModel(new PhongIllumination(sphere2, 0.4, 0.3, 0.4, 50) );
		sphere2.setColor( new Color(0, 0, 0) );	//black
		
		//Quad1
		Quad quad1 = new Quad(
		     	     new Point3d(-16,-4,44),	//bottom left 
			         new Point3d(6,-4,44),	//bottom right
			         new Point3d(-16,-4,-30)); //top left
		quad1.setColorModel(new CheckerBoardColorModel());
		quad1.setIlluminationModel(new PhongIllumination(quad1, 0.6, 0.6, 0.4, 70) );
		quad1.setColor( new Color(255, 255, 0) );		//yellow
		
		
		
		//Place objects in world.
		World.getInstance().shapes.add(quad1);
		World.getInstance().shapes.add(sphere2);
		World.getInstance().shapes.add(sphere1);
//		World.getInstance().shapes.add(cube1);
		
		//Create and position the camera.
		Camera camera = new Camera( 
				new Point3d(0, 0, 20), 
				new Point3d(0,0,0), 
				new Vector3d(0,1,0));
		camera.setSuperSamplingValue(1); //Optional, if not set it defaults to 1
		World.getInstance().cameras.add(camera);
		
		//Place lights in world
		LightSource ls1 = new LightSource(Color.white, new Point3d(0 ,20, 30));
		World.getInstance().lightSource.add(ls1);
//		LightSource ls2 = new LightSource(Color.white, new Point3d(-40 ,20, 30));
//		World.getInstance().lightSource.add(ls2);

		//Set background color of world.
		World.getInstance().bgColor = new Color(0, 125, 255); //light blue

		
		//Call camera.render() to take a picture.
		sceneColors = new ArrayList< ArrayList<Color> >();
		for(ArrayList<Color> xColors : camera.render()) {
			ArrayList<Color> sceneXColors = new ArrayList<Color>();
			for(Color color : xColors){
				sceneXColors.add(new Color( color.getRed(), color.getGreen(), color.getBlue() ));
			}
			sceneColors.add(sceneXColors);
		}
		
		//Do Post processing
//		sceneColors = ward(sceneColors);
//		sceneColors = reinHard(sceneColors);
	}
	
	

	/**
	 * Fills a canvas on the screen with 1X1 pixel rectangles because java 
	 * doesn't have a way to just color pixels on a screen otherwise.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Paint the window white.  
		//If you see white, the ray tracer isn't working
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		

		for(int y = 0; y<sceneColors.size(); y++) {
			for(int x = 0; x<sceneColors.get(y).size(); x++) {
				g.setColor( sceneColors.get(y).get(x) );
				g.fillRect(x, y, 1, 1);
			}
		}
	}


	public Dimension getPreferredSize() {
		return new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}


	public Dimension getMinimumSize() {
		return getPreferredSize();
	}


	public static void main(String args[]) {
		initialize();
		
		JFrame mainFrame = new JFrame(Constants.WINDOW_TITLE);
		mainFrame.setLocation(100, 100);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		mainFrame.getContentPane().add(new Renderer());
		mainFrame.pack();
		mainFrame.setVisible(true);

		System.out.println("Finished.");
	}
	
	/**
	 * Takes an array of colors for post processing using Ward's method.
	 * @param colors
	 * @return a processed array of colors
	 */
	public static ArrayList< ArrayList<Color> > ward(ArrayList< ArrayList<Color> > colors){
		double Lmax = 100;
		double LdMax = 100;
		
		ArrayList< ArrayList<Double> > reds = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > greens = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > blues = new ArrayList< ArrayList<Double> >();
		
		ArrayList< ArrayList<Double> > approxPixelLum = new ArrayList< ArrayList<Double> >();
		
		
		//Make color Values be 0-1 on RGB channels
		//And multiply them my the quick and dirty approximation.
		for(ArrayList<Color> xColors : colors){
			ArrayList<Double> xRed = new ArrayList<Double>();
			ArrayList<Double> xGreen = new ArrayList<Double>();
			ArrayList<Double> xBlue = new ArrayList<Double>();
			ArrayList<Double> xApproxPixelLum = new ArrayList<Double>();
			for(Color color : xColors){
				xRed.add((color.getRed() / 255f)*Lmax);
				xGreen.add((color.getGreen() / 255f)*Lmax);
				xBlue.add((color.getBlue() / 255f)*Lmax);
				xApproxPixelLum.add(
						(((color.getRed() / 255f)*Lmax)*0.27) +
						(((color.getGreen() / 255f)*Lmax)*0.67) +
						(((color.getBlue() / 255f)*Lmax)*0.06) );
			}
			reds.add(xRed);
			greens.add(xGreen);
			blues.add(xBlue);
			approxPixelLum.add(xApproxPixelLum);
		}
		
		//Get the log-average luminance
		double sum = 0f;
		for(ArrayList<Double> xLuminance : approxPixelLum){
			for(Double luminance : xLuminance){
				sum += Math.log((Constants.EPSILON + luminance));
			}
		}
		double logSumAvg = sum / (Constants.WINDOW_WIDTH * Constants.WINDOW_HEIGHT);
		double averageSceneLuminance = (float)Math.pow(Math.E, logSumAvg);
		
		//Get Ward scale factor
		double scaleFactor = Math.pow(((1.219 + Math.pow((LdMax/2), 0.4)) / 
							 (1.219 + Math.pow(averageSceneLuminance, 0.4))), 2.5);
		
		//Apply ward
		ArrayList< ArrayList<Color> > retVal = new ArrayList< ArrayList<Color> >();
		for(int y = 0; y < Constants.WINDOW_HEIGHT; y++){
			ArrayList<Color> xRetVal = new ArrayList<Color>();
			for(int x = 0; x < Constants.WINDOW_WIDTH; x++){
				float red = (float)((reds.get(y).get(x) * scaleFactor)/LdMax);
				float green = (float)((greens.get(y).get(x) * scaleFactor)/LdMax);
				float blue = (float)((blues.get(y).get(x) * scaleFactor)/LdMax);
				
				if(red > 1)
					red = 1;
				if(green > 1)
					green = 1;
				if(blue > 1)
					blue = 1;
				if(red < 0 ){
					red = 0;
				}
				if(green < 0 ){
					green = 0;
				}
				if(blue < 0 ){
					blue = 0;
				}
				
				xRetVal.add(new Color(red, green, blue));
			}
			retVal.add(xRetVal);
		}
		
		return retVal;
	}
	
	/**
	 * Takes an array of colors for post processing using Reinhards method.
	 * @param colors
	 * @return a processed array of colors
	 */
	public static ArrayList< ArrayList<Color> > reinHard(ArrayList< ArrayList<Color> > colors){
		double Lmax = 1;
		double LdMax = 1;
		double alpha = 0.18;
		
		
		ArrayList< ArrayList<Double> > reds = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > greens = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > blues = new ArrayList< ArrayList<Double> >();
		
		ArrayList< ArrayList<Double> > approxPixelLum = new ArrayList< ArrayList<Double> >();
		
		
		//Make color Values be 0-1 on RGB channels
		//And multiply them my the quick and dirty approximation.
		for(ArrayList<Color> xColors : colors){
			ArrayList<Double> xRed = new ArrayList<Double>();
			ArrayList<Double> xGreen = new ArrayList<Double>();
			ArrayList<Double> xBlue = new ArrayList<Double>();
			ArrayList<Double> xApproxPixelLum = new ArrayList<Double>();
			for(Color color : xColors){
				xRed.add((color.getRed() / 255f)*Lmax);
				xGreen.add((color.getGreen() / 255f)*Lmax);
				xBlue.add((color.getBlue() / 255f)*Lmax);
				xApproxPixelLum.add(
						(((color.getRed() / 255f)*Lmax)*0.27) +
						(((color.getGreen() / 255f)*Lmax)*0.67) +
						(((color.getBlue() / 255f)*Lmax)*0.06) );
			}
			reds.add(xRed);
			greens.add(xGreen);
			blues.add(xBlue);
			approxPixelLum.add(xApproxPixelLum);
		}
		
		//Get the log-average luminance
		double sum = 0f;
		for(ArrayList<Double> xLuminance : approxPixelLum){
			for(Double luminance : xLuminance){
				sum += Math.log((Constants.EPSILON + luminance));
			}
		}
		double logSumAvg = sum / (Constants.WINDOW_WIDTH * Constants.WINDOW_HEIGHT);
		double averageSceneLuminance = (float)Math.pow(Math.E, logSumAvg);
		
		
		//Apply Reinhard
		ArrayList< ArrayList<Double> > redsPrime = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > greensPrime = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > bluesPrime = new ArrayList< ArrayList<Double> >();
		
		//Scale each rgb channel by alpha/avgLum
		for(int y = 0; y < Constants.WINDOW_HEIGHT; y++){
			ArrayList<Double> xRed = new ArrayList<Double>();
			ArrayList<Double> xGreen = new ArrayList<Double>();
			ArrayList<Double> xBlue = new ArrayList<Double>();
			for(int x = 0; x < Constants.WINDOW_WIDTH; x++){
				xRed.add(reds.get(y).get(x) * (alpha / averageSceneLuminance));
				xGreen.add(greens.get(y).get(x) * (alpha / averageSceneLuminance));
				xBlue.add(blues.get(y).get(x) * (alpha / averageSceneLuminance));
			}
			redsPrime.add(xRed);
			greensPrime.add(xGreen);
			bluesPrime.add(xBlue);
		}
		
		reds.clear();// we don't need these any more, so free up memory
		greens.clear();
		blues.clear();
		
		
		ArrayList< ArrayList<Double> > redsDoublePrime = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > greensDoublePrime = new ArrayList< ArrayList<Double> >();
		ArrayList< ArrayList<Double> > bluesDoublePrime = new ArrayList< ArrayList<Double> >();
		//Scale each rgb channel by itsSelf / (1+ itsSelf)
		for(int y = 0; y < Constants.WINDOW_HEIGHT; y++){
			ArrayList<Double> xRed = new ArrayList<Double>();
			ArrayList<Double> xGreen = new ArrayList<Double>();
			ArrayList<Double> xBlue = new ArrayList<Double>();
			for(int x = 0; x < Constants.WINDOW_WIDTH; x++){
				xRed.add(redsPrime.get(y).get(x) / (redsPrime.get(y).get(x) + (double)1));
				xGreen.add(greensPrime.get(y).get(x) / (greensPrime.get(y).get(x) + (double)1));
				xBlue.add(bluesPrime.get(y).get(x) / (bluesPrime.get(y).get(x) + (double)1));
			}
			redsDoublePrime.add(xRed);
			greensDoublePrime.add(xGreen);
			bluesDoublePrime.add(xBlue);
		}
		
		redsPrime.clear(); //don't need these anymore so clear and free memory
		greensPrime.clear();
		bluesPrime.clear();
		
		ArrayList< ArrayList<Color> > retVal = new ArrayList< ArrayList<Color> >();
		for(int y = 0; y < Constants.WINDOW_HEIGHT; y++){
			ArrayList<Color> xRetVal = new ArrayList<Color>();
			for(int x = 0; x < Constants.WINDOW_WIDTH; x++){
				float red = (float)(redsDoublePrime.get(y).get(x) * LdMax);
				float green = (float)(greensDoublePrime.get(y).get(x) * LdMax);
				float blue = (float)(bluesDoublePrime.get(y).get(x) * LdMax);
				
				if(red > 1)
					red = 1;
				if(green > 1)
					green = 1;
				if(blue > 1)
					blue = 1;
				if(red < 0 ){
					red = 0;
				}
				if(green < 0 ){
					green = 0;
				}
				if(blue < 0 ){
					blue = 0;
				}
				
				xRetVal.add(new Color(red, green, blue));
			}
			retVal.add(xRetVal);
		}
		
		
		return retVal;
	}
	
}
