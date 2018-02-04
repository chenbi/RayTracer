package objectprimatives;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import datastructures.IntersectionData;


public class Cube extends Shape {
	protected Point3d p0, p1, p2, p3;
	protected Quad[] quads = new Quad[6];
	protected Quad currentIntersectingQuad;
	
	
	public Cube(Point3d p0, Point3d p1, 
			    Point3d p2, Point3d p3){
		super();
		
		this.p0 = new Point3d(p0);
		this.p1 = new Point3d(p1);
		this.p2 = new Point3d(p2);
		this.p3 = new Point3d(p3);
		
		afterInitialize();
	}

	
	private void afterInitialize() {
		currentIntersectingQuad = null;
		
	    //p1 - p0
		Vector3d p1Subp0 = new Vector3d( p1.getX() - p0.getX(),
		                                 p1.getY() - p0.getY(),
		                                 p1.getZ() - p0.getZ() );
		//p3 - p0
		Vector3d p3Subp0 = new Vector3d( p3.getX() - p0.getX(),
                                         p3.getY() - p0.getY(),
                                         p3.getZ() - p0.getZ() );
		Point3d a = new Point3d();
		Point3d b = new Point3d();	
		
		quads[0] = new Quad(p0, p1, p2);  //Front facing quad
		quads[1] = new Quad(p0, p2, p3);  //Left facing quad
		quads[2] = new Quad(p0, p3, p1);  //Bottom facing quad
		
		a = p3;
		a.add(p1Subp0);
		b = p2;
		b.add(p1Subp0);
		quads[3] = new Quad(p1, a, b);  //Right facing quad

		a = p2;
	    a.add(p1Subp0);
	    b = p2;
	    b.add(p3Subp0);
		quads[4] = new Quad(p2, a, b);  //Top facing quad
		
		a = p2;
		a.add(p3Subp0);
		b = p3;
		b.add(p1Subp0);
		quads[5] = new Quad(p3, a, b); //Back facing quad
	}
	
	
	public IntersectionData calcIntersection(Ray ray) {
		// Start off with infinite distance and no intersecting primitive
		double distance = Double.POSITIVE_INFINITY;
		IntersectionData retVal = null;
		
		for (int i = 0; i < quads.length; i++)
		{
			IntersectionData currentIntersection = quads[i].calcIntersection(ray);
			if(currentIntersection != null){
				double t = currentIntersection.distance;
				
				//If we found a closer intersecting quad, keep a reference to and it.
				if (t < distance)
				{
					distance = t;
					retVal = currentIntersection;
				}
			}
		}
		
		return retVal;
	}


	public Quad[] getQuads() {
		return quads;
	}


	public void setQuads(Quad[] quads) {
		this.quads = quads;
	}
}
