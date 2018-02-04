package colormodels;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.vecmath.Point3d;

import datastructures.IntersectionData;

public class BitmapColorModel implements ColorModel {
	private BufferedImage image;
	
	public BitmapColorModel(BufferedImage image){
		this.image = image;
	}

	@Override
	public Color getColorAt(IntersectionData intersection) {
		Point3d intersectionPoint = intersection.rayFromCamera.getPointAlongRay(intersection.distance);
		
		//Image image = new ImageIcon(image).getImage();
		
		
		int xPixel = Math.abs((int)Math.round(intersectionPoint.getX() * image.getWidth())) % image.getWidth(); 
		int yPixel = Math.abs((int)Math.round(intersectionPoint.getZ() * image.getHeight())) % image.getHeight();
	   
		Color color = new Color(image.getRGB(xPixel, yPixel));
		
		return color;
	}
}
