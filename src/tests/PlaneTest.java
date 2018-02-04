package tests;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import objectprimatives.Plane;
import objectprimatives.Ray;

public class PlaneTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int numErrors = 0;
//		numErrors += test1();
		numErrors += test2();
		numErrors += test3();
		numErrors += test4();
		numErrors += test5();
		numErrors += test6();
		
		if(numErrors == 0){
			System.out.println("All tests complete successfully");
		} else {
			System.err.println("There were " + numErrors + " errors while running tests.");
		}
	}
	
	
//	/**
//	 * 
//	 * @return
//	 */
//	public static int test1() {
//		Plane plane = new Plane();
//		
//		double intersectionDistance = plane.calcIntersectionDistance( 
//				new Ray( 
//						new Point3d(0.0,0.0,0.0), 
//						new Vector3d(0.0,-5.0,0.0) 
//						) 
//				);
//		if( intersectionDistance != Double.POSITIVE_INFINITY && 
//				intersectionDistance != Double.NEGATIVE_INFINITY ) {
//			System.err.println("PlaneTest.java: Test1 Failure: Intersection point null");
//			return 1;
//		}
//		return 0;
//	}
	
	
	/**
	 * 
	 * @return
	 */
	public static int test2(){
		return 0;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static int test3(){
		return 0;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static int test4() {
		return 0;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static int test5() {
		return 0;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static int test6() {
		return 0;
	}
}