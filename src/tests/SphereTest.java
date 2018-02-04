package tests;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import objectprimatives.Ray;
import objectprimatives.Sphere;
import datastructures.IntersectionData;

public class SphereTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int numErrors = 0;
		numErrors += test1();
//		numErrors += test2();
//		numErrors += test3();
//		numErrors += test4();
//		numErrors += test5();
//		numErrors += test6();
		
		if(numErrors == 0){
			System.out.println("All tests complete successfully");
		} else {
			System.err.println("There were " + numErrors + " errors while running tests.");
		}
	}
	
	
	/**
	 * Hit the sphere dead on with ray direction magnitude not
	 * enough to reach the sphere
	 * @return
	 */
	public static int test1() {
		Sphere sphere = new Sphere(new Point3d(0,0,8), 2);
		
		Point3d origin = new Point3d(-0.13979287356424797, 1.1896812129322716, 6.398401871881016);
		//Vector3d bla = new Vector3d(-0.01058492024800414, 0.157918784381037, -0.9873953701547112);
		Vector3d dir = new Vector3d(-0.09327524669920571, 0.7938016137758825, 0.600981469198516);
//		Vector3d smallDir = new Vector3d();
//		smallDir.scale(0.00000001, dir);
//		origin.add(smallDir);
		
		IntersectionData intersectionData = sphere.calcIntersection( 
				new Ray( origin, dir ) 
				);
		
		if(intersectionData != null) {
			double intersectionDistance = intersectionData.distance;
			if( intersectionDistance == Double.POSITIVE_INFINITY || 
					intersectionDistance == Double.NEGATIVE_INFINITY ) {
				System.err.println("SphereTest.java: Test1 Failure: Intersection distance infinity");
				return 1;
			} else {
				//
			}
		} else {
			System.err.println("SphereTest.java: Test1 Failure: Intersection point null");
			return 1;
		}
		return 0;
	}
//	
//	
//	/**
//	 * Hit the sphere dead on with ray direction magnitude 
//	 * large enough to reach the sphere
//	 * @return
//	 */
//	public static int test2() {
//		Sphere sphere = new Sphere(2.0);
//		
//		double intersectionDistance = sphere.calcIntersectionDistance(
//				new Ray( 
//						new Point3d(0.0,-5.0,0.0), 
//						new Vector3d(0.0,5.0,0.0) 
//						) 
//				);
//		if( intersectionDistance != Double.POSITIVE_INFINITY && 
//				intersectionDistance != Double.NEGATIVE_INFINITY ) {
//			System.err.println("SphereTest.java: Test2 Failure: Intersection point null");
//			return 1;
//		}
//		return 0;
//	}
//	
//	
//	/**
//	 * Miss the sphere entirely
//	 * @return
//	 */
//	public static int test3() {
//		Sphere sphere = new Sphere(2.0);
//		
//		double intersectionDistance = sphere.calcIntersectionDistance(
//				new Ray( 
//						new Point3d(0.0,-5.0,0.0), 
//						new Vector3d(5.0,0.0,0.0) 
//						) 
//				);
//		if( intersectionDistance != Double.POSITIVE_INFINITY && 
//				intersectionDistance != Double.NEGATIVE_INFINITY ) {
//			System.err.println("SphereTest.java: Test3 Failure: Intersection point not null");
//			return 1;
//		}
//		return 0;
//	}
//	
//	
//	/**
//	 * Hit - Glance the very edge of the sphere
//	 * @return
//	 */
//	public static int test4() {
//		Sphere sphere = new Sphere(2.0);
//		
//		double intersectionDistance = sphere.calcIntersectionDistance( 
//				new Ray( 
//						new Point3d(1.0,-5.0,0.0), 
//						new Vector3d(0.0,5.0,0.0) 
//						) 
//				);
//		if( intersectionDistance != Double.POSITIVE_INFINITY && 
//				intersectionDistance != Double.NEGATIVE_INFINITY ) {
//			System.err.println("SphereTest.java: Test4 Failure: Intersection point null");
//			return 1;
//		}
//		return 0;
//	}
//	
//	
//	/**
//	 * Hit the sphere - Glancing blow.
//	 * @return
//	 */
//	public static int test5() {
//		Sphere sphere = new Sphere(2.0);
//		
//		double intersectionDistance = sphere.calcIntersectionDistance(
//				new Ray( 
//						new Point3d(2.0,-5.0,0.0), 
//						new Vector3d(0.0,5.0,0.0) 
//						) 
//				);
//		if( intersectionDistance != Double.POSITIVE_INFINITY && 
//				intersectionDistance != Double.NEGATIVE_INFINITY ) {
//			System.err.println("SphereTest.java: Test5 Failure: Intersection point not null");
//			return 1;
//		}
//		return 0;
//	}
//	
//	
//	/**
//	 * Miss - Ray starts within the sphere and fires in any direction
//	 * @return
//	 */
//	public static int test6() {
//		Sphere sphere = new Sphere(2.0);
//		
//		double intersectionDistance = sphere.calcIntersectionDistance(
//				new Ray( 
//						new Point3d(0.0,0.0,0.0), 
//						new Vector3d(0.0,5.0,0.0) 
//						) 
//				);
//		if( intersectionDistance != Double.POSITIVE_INFINITY && 
//				intersectionDistance != Double.NEGATIVE_INFINITY ) {
//			System.err.println("SphereTest.java: Test6 Failure: Intersection point null");
//			return 1;
//		}
//		return 0;
//	}
}