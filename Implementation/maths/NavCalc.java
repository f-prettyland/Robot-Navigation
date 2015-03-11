package maths;

import java.awt.geom.Line2D;
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
	
	//TODO DEL
	public static Line2D findLine (Point2D point, Point2D point2){		
		return new Line2D.Double(point, point2);
	}
	
	public static double attractionAt (Point2D point, RenderablePoint from){
		return 0;
	}
	
	public static double repulsionAt (Point2D point, ArrayList<Point2D> from){
		return 0;
	}
}
