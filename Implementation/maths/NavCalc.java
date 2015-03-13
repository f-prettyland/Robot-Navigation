package maths;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import maths.GenericCalc.EqnOfLine;
import renderables.*;
import robot.HitDetails;

public  class NavCalc {

	//DECREASE WITH SENSORS
	private static final double REPULSE_SCALAR=10;
	private static final double ATTRACT_SCALAR=0.0001;
	public static final double INSIDE_SCALAR=Math.pow(10000000,2);

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
	
	public static Point2D pointDownLine(Line2D line, double unit)
	  {
		EqnOfLine lineEqn = GenericCalc.findLine(line);
		Point2D startLoc  = line.getP1();
		// quadratic eqn
		double a = 1 + Math.pow(lineEqn.a, 2);
		double b = (2*lineEqn.b*lineEqn.a)-(2*lineEqn.a*startLoc.getY())-(2*startLoc.getX());
		double c = Math.pow(lineEqn.b, 2) -Math.pow(unit, 2)-(2*lineEqn.b*startLoc.getY())+ Math.pow(startLoc.getY(), 2)+Math.pow(startLoc.getX(), 2) ;
		
		return GenericCalc.closestOfTwoPoints(line.getP2(),GenericCalc.quadEqn(a, b, c, lineEqn));
	  }

	/**
	 * Takes in an bearing and adds an angle to it making sure that the output is still a bearing
	 * @param base
	 * @param addition
	 * @return
	 */
	public static double addToBearing (double base, double addition){
		double a = base;
		a += addition;
		if(a >360){
			a -=360;
		}else if(a < 0){
			a +=360;
		}
		return a;
	}

	public static double attractionAt (Point2D point, RenderablePoint from){		
		return ATTRACT_SCALAR*(Math.pow((point.getX()-from.x),2)+Math.pow((point.getY()-from.y),2));
	}

	public static double repulsionAt (double s,Point2D point, ArrayList<HitDetails> from, Point2D currentLoc, ArrayList<Renderable> map){
		double result = 0;
		for(HitDetails hPoint : from){
			double d= point.distance(hPoint.getHit());
			//if planning to cross object or go into object
//
			if(entersAnotherObs(point, currentLoc,map)){
				result += INSIDE_SCALAR;
			}
			if(s>d){
				result+=(Math.pow(Math.E,((-1)*(1/(s-d))))/d);
			}
		}
		return REPULSE_SCALAR*result;
	}

	private static boolean entersAnotherObs(Point2D point, Point2D currentLoc, ArrayList<Renderable> map){
		for(Renderable obstacle : map){
			if(ObsCalc.pointWithin(point, obstacle)||ObsCalc.doesCross(obstacle, new Line2D.Double(currentLoc, point))){
				return true;
			}
		}
		return false;
	}
	
	public static boolean atGoal(double x, double y, RenderableOval goalCircle) {
		if(((450<x) && (x<550)) && ((450<y)&&(y<550)) ){
			//TODO REM
			System.out.println("hhe");
		}
		return ObsCalc.pointWithin(new Point2D.Double(x , y), goalCircle);
	}
	
	public static float randomDouble(double max)
	{
		Random random = new Random();
		return (float) (random.nextFloat() * max);
	}
}
