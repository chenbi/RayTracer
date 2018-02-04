package objectprimatives;

import java.awt.Color;

import javax.vecmath.Point3d;

/**
 * Basic parameters and methods used to define a light source.
 * 
 * @author  
 * @author Chen
 */
public class LightSource {
    public Color color;       //r,g,b color values of light.
    public Point3d position;  //x,y,z position of light.
    
    
    /*
     * Creates a light source with a color of white light
     * positioned at the origin of the world.
     * 
     */
    public LightSource() {
    	this.color = Color.white;
    	this.position = new Point3d(0.0,0.0,0.0);
    }
    
    /**
     * Constructor
     * @param color
     * @param point
     */
    public LightSource(Color color, Point3d point) {
    	this.color = color;
    	this.position = point;
    }
    
    
    /*
     * Creates a light source with a color of white light
     * positioned at a desired location in the world.
     * 
     * @param position x,y,z position in world of light.
     */
    LightSource(Point3d position) {
        this.position = position;
    }
    
    
    /*
     * Creates a light source with a desired color of light
     * positioned at a desired location in the world.
     * 
     * @param position x,y,z position in world of light.
     */
    LightSource(Point3d position, Color color) {
        this.position = position;
        this.color = color;
    }
}