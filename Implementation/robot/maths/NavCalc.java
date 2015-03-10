package robot.maths;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import renderables.*;

public  class NavCalc {

	/**
	 * Returns the cartesian offset given a vector
	 * @param magnitude
	 * @param angle
	 * @return
	 */
	public static Point2D toCartesian (double magnitude, double angle){
		//covert to radians
		double rAngle = angle*(Math.PI/180);
		if(angle < 0){
			//TODO REMOVE
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHH");
			System.exit(1);
		} else if(angle >360){
			//TODO REMOVE
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHH");
			System.exit(1);
		}
		
		
		double x=magnitude*Math.sin(rAngle);
		double y=magnitude*Math.cos(rAngle);
		
		return new Point2D.Double(x,y);
	}
	
	/**
	 * Takes in an bearing and adds an angle to it making sure that the output is still a bearing
	 * @param base
	 * @param addition
	 * @return
	 */
	public static double addToBearing (double base, double addition){
		base += addition;
		if(base >360){
			base -=360;
		}else if(base < 0){
			base +=360;
		}
		return base;
	}
	
	public static Point2D findLine (Point2D point, Point2D point2){
		//gradient of the line
		double m = (point.getY() - point2.getY())/(point.getX() - point2.getX());
		//y intersect
		double b = point.getY() - (m*point.getX());
		
		return new Point2D.Double(m,b);
	}
	
	public static boolean pointWithin (Point2D point, Renderable shape){
		return true;
	}
	
	public static Point2D closestCrossing (Point2D loc, Renderable shape, Point2D lineEqn){
		return null;
	}
	
	public static boolean attractionAt (Point2D point, Point2D from){
		return true;
	}
	
	public static boolean repulsionAt (Point2D point, ArrayList<Point2D> from){
		return true;
	}
}
