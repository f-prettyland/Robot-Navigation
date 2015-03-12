package maths;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import maths.GenericCalc.EqnOfLine;
import renderables.*;

public class ObsCalc {
	public static boolean pointWithin(Point2D point, Renderable shape) {
		RenderableClass rClass = RenderableClass.valueOf(shape.getClass()
				.getSimpleName());
		switch (rClass) {
		case RenderablePoint:
			return pointWithin(point, (RenderablePoint) shape);
		case RenderableOval:
			return pointWithin(point, (RenderableOval) shape);
		default:
			System.out.println("Shape not configured for");
			return false;
		}
	}

	public static Point2D closestCrossing(Point2D loc, Renderable shape,
			Line2D line) {
		RenderableClass rClass = RenderableClass.valueOf(shape.getClass()
				.getSimpleName());
		switch (rClass) {
		case RenderablePoint:
			return closestCrossing(loc, (RenderablePoint) shape, line);
		case RenderableOval:
			return closestCrossing(loc, (RenderableOval) shape, line);
		default:
			System.out.println("Shape not configured for");
			return null;
		}
	}
	public static boolean doesCross(Renderable shape,
			Line2D line) {
		RenderableClass rClass = RenderableClass.valueOf(shape.getClass()
				.getSimpleName());
		switch (rClass) {
		case RenderablePoint:
			return doesCross( (RenderablePoint) shape, line);
		case RenderableOval:
			return doesCross( (RenderableOval) shape, line);
		default:
			System.out.println("Shape not configured for");
			return false;
		}
	}
	
	private static boolean pointWithin(Point2D point, RenderablePoint rPoint) {
		if ((Math.pow((point.getX() - rPoint.x), 2)
				+ Math.pow((point.getY() - rPoint.y), 2)) <= Math.pow(
						(rPoint.penWidth / 2), 2)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean pointWithin(Point2D point, RenderableOval rOval) {
		if ((Math.pow((point.getX() - rOval.centreX), 2)/(Math.pow((rOval.width/2), 2)))
				+ (Math.pow((point.getY() - rOval.centreY), 2)/(Math.pow((rOval.height/2), 2))) <= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * After ensuring the line and circle meet the intersection can be found by
	 * finding the line equation, this can be combined with the equation for a
	 * circle and can be solved overall using the quadratic equation. Once two
	 * values are returned from this, they are compared to the location given to
	 * return the closest intersection
	 * 
	 * @param loc
	 * @param rPoint
	 * @param line
	 * @return
	 */
	private static Point2D closestCrossing(Point2D loc, RenderablePoint rPoint,
			Line2D line) {
		if (line.ptSegDist(rPoint.x, rPoint.y) <= (rPoint.penWidth / 2)) {
			EqnOfLine lineEqn = GenericCalc.findLine(line);
			// quadratic eqn
			double a = 1 + Math.pow(lineEqn.a, 2);
			double b = (2 * lineEqn.a * lineEqn.b) - (2 * rPoint.x)
					- (2 * rPoint.y * lineEqn.a);
			double c = Math.pow((rPoint.x), 2)
					- Math.pow((rPoint.penWidth / 2), 2) + Math.pow((lineEqn.b), 2)
					- (2 * rPoint.y* lineEqn.b) + Math.pow(rPoint.y, 2);

			return GenericCalc.quadEqnClosest(a, b, c, lineEqn, loc);
		}
		return null;
	}
	
	private static Point2D closestCrossing(Point2D loc, RenderableOval rOval,
			Line2D line) {
		if (pointWithin(getClosestLinePoint(line, new Point2D.Double(rOval.centreX,rOval.centreY)), rOval)) {
			EqnOfLine lineEqn = GenericCalc.findLine(line);
			double hRad= rOval.height/2; 
			double wRad= rOval.width/2; 
			// quadratic eqn
			double a = Math.pow(wRad, 2) + Math.pow(hRad, 2);
			double b = (2 * lineEqn.a * lineEqn.b) - (2 *Math.pow(hRad, 2)* rOval.centreX)
					- (2 * lineEqn.a*Math.pow(wRad, 2)* rOval.centreY);
			double c = (Math.pow(hRad, 2)*Math.pow(rOval.centreX, 2))
					+(Math.pow(wRad, 2)*
							(Math.pow(lineEqn.b, 2))+(2*rOval.centreY*lineEqn.b)+Math.pow(rOval.centreY, 2))
					-Math.pow(wRad, 2) + Math.pow(hRad, 2);;

			return GenericCalc.quadEqnClosest(a, b, c, lineEqn, loc);
		}
		return null;
	}
	
	private static boolean doesCross(RenderablePoint rPoint,
			Line2D line) {
		if (line.ptSegDist(rPoint.x, rPoint.y) <= (rPoint.penWidth / 2)) {
			return true;
		}
		return false;
	}
	
	private static boolean doesCross(RenderableOval rOval,
			Line2D line) {
		if (pointWithin(getClosestLinePoint(line, new Point2D.Double(rOval.centreX,rOval.centreY)), rOval)) {
						return true;
		}
		return false;
	}

	  /**
	   * Projects new line from original gradient 
	 * @param line
	 * @param point
	 * @return
	 */
	public static Point2D getClosestLinePoint(Line2D line, Point2D point)
	  {
		//unit vector/gradient
		double deltaX = line.getX2() - line.getX1();
	    double deltaY = line.getY2() - line.getY1();

	    //finding lambda (when multiplied by gradient gives closest point) whilst taking into account start point
	    double u = ((point.getX() - line.getX1()) * deltaX + (point.getY() - line.getY1()) * deltaY) / (Math.pow((deltaX), 2)+ Math.pow((deltaY), 2));

	    //multiply projected vector
	    return new Point2D.Double((line.getX1() + u * deltaX), line.getY1() + u * deltaY);
	  }
	 
	
	

	enum RenderableClass {
		RenderableImg, RenderableObject, RenderableOval, RenderablePoint, RenderablePolygon, RenderablePolyline, RenderableRectangle, RenderableString;

	}
}
