package colormodels;

import java.awt.Color;

import javax.vecmath.Point3d;
import datastructures.IntersectionData;

public class CheckerBoardColorModel implements ColorModel {
	double checkersSize = 1;
	Color checkersDiffuse1 = Color.red;
	Color checkersDiffuse2 = Color.yellow;
	//Quad quad;
	double width;
	double height;
	
	
	//public CheckerBoardColorModel(Shape shape) {
	    //this.quad = (Quad)shape;
	//}
	
	public CheckerBoardColorModel() {
	}
	
	@Override
	public Color getColorAt(IntersectionData intersection) {
		Point3d intersectionPoint = intersection.rayFromCamera.getPointAlongRay(intersection.distance);
		
		//double dx = Math.abs( intersectionPoint.getX() - quad.getP2().getX() );
		//double dz = Math.abs( intersectionPoint.getZ() - quad.getP2().getZ() );

		//int checkersX = (int)(dx /checkersSize);
		//int checkersZ = (int)(dz /checkersSize);
		
		//checkersX = checkersX % 2;
		//checkersZ = checkersZ % 2;
		
		double checkersX = Math.abs(Math.floor(intersectionPoint.getX() / checkersSize) % 2);
		double checkersZ = Math.abs(Math.floor(intersectionPoint.getZ() / checkersSize) % 2);
		 
		if (checkersX == 0 && checkersZ == 0) return checkersDiffuse2;
		if (checkersX == 0 && checkersZ == 1) return checkersDiffuse1;
		if (checkersX == 1 && checkersZ == 0) return checkersDiffuse1;
		if (checkersX == 1 && checkersZ == 1) return checkersDiffuse2;
		
		return Color.BLACK;
	}
}
