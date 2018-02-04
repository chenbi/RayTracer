package objectprimatives;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Defines the world.
 * 
 * @author  
 * @author Chen
 */
public class World {
	public ArrayList<Shape> shapes;    //All shapes in the world.
	public ArrayList<Camera> cameras;  //All cameras in the world.
	public ArrayList<LightSource> lightSource;   //All lights in the world.
	public Color bgColor;                     //The r,g,b background color of the world.
	public float transmissionValue;		//Ni for transmission stuff.

	private static World world;
	
	
	public World() {
	   this.shapes = new ArrayList<Shape>();
	   this.lightSource = new ArrayList<LightSource>();
	   this.cameras = new ArrayList<Camera>();
	   this.bgColor = Color.white;
	   this.transmissionValue = 1.0f;
	}
	
	
	public static World getInstance() {
		if( world== null )
		   world = new World();
		
		return world;
	}
}