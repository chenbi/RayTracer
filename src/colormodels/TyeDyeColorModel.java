package colormodels;

import java.awt.Color;
import java.util.Random;

import javax.vecmath.Vector3d;

import objectprimatives.Shape;
import datastructures.IntersectionData;

public class TyeDyeColorModel implements ColorModel {

	private int red = 0;
	private int green = 0;
	private int blue = 0;
	private Random rand;
	private int CHANGE_AMOUNT = 30; //2 or greater
	private Shape shape;
	private double lastAngle = 0.0;
	
	public TyeDyeColorModel(){
		super();
		rand = new Random();
		red = rand.nextInt(256);
		green = rand.nextInt(256);
		blue = rand.nextInt(256);
	}
	
	public TyeDyeColorModel(Shape shape){
		this();
		this.shape = shape;
	}
	
	
	@Override
	public Color getColorAt(IntersectionData intersection) {	
		Vector3d rayToCamera = new Vector3d();
		rayToCamera.sub(intersection.rayFromCamera.origin,
				intersection.rayFromCamera.getPointAlongRay(intersection.distance));
		
		double angle = Math.toDegrees(intersection.normal.dot(rayToCamera));
		red = (int)(255 * Math.cos(angle * 3000));
		green = (int)(255 % lastAngle * Math.sin(angle * 3000));
		blue = (int)((255 * angle)/Math.sin(lastAngle));
		
		lastAngle = angle;

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

}
