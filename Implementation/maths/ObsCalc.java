package maths;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import maths.GenericCalc.EqnOfLine;
import renderables.*;

public class ObsCalc {
	
	public static boolean pointWithin(Point2D point, Renderable shape) {
		if ((numberOfIntersects(shape, new Line2D.Double(point,
				new Point2D.Double(700, 700))) % 2) == 0) {
			return false;
		}
		return true;
	}

	public static Point2D closestIntersection(Point2D loc, Renderable shape,
			Line2D line) {
		Point2D[] intersects = intersectingPoints(shape, line);
		if (intersects == null) {
			return null;
		}
		return GenericCalc.closestOfTwoPoints(loc, intersects);
	}

	public static int numberOfIntersects(Renderable shape, Line2D line) {
		Point2D[] intersects = intersectingPoints(shape, line);
		if (intersects == null) {
			return 0;
		}
		int actualIntersects = 0;
		for (Point2D intersect : intersects) {
			if (line.getBounds2D().contains(intersect)) {
				actualIntersects++;
			}
		}
		return actualIntersects;
	}

	public static boolean doesCross(Renderable shape, Line2D line) {
		RenderableClass rClass = RenderableClass.valueOf(shape.getClass()
				.getSimpleName());
		switch (rClass) {
		case RenderablePoint:
			return doesCross((RenderablePoint) shape, line);
		case RenderableOval:
			return doesCross((RenderableOval) shape, line);
		case RenderablePolygon:
			return doesCross((RenderableOval) shape, line);
		default:
			System.out.println("Shape not configured for");
			return false;
		}
	}
	
	private static Point2D[] intersectingPoints(Renderable shape, Line2D line) {
		RenderableClass rClass = RenderableClass.valueOf(shape.getClass()
				.getSimpleName());
		switch (rClass) {
		case RenderablePoint:
			return intersectingPoints((RenderablePoint) shape, line);
		case RenderableOval:
			return intersectingPoints((RenderableOval) shape, line);
		case RenderablePolygon:
			return intersectingPoints((RenderableOval) shape, line);
		default:
			System.out.println("Shape not configured for");
			return null;
		}
	}

	private static boolean pointWithin(Point2D point, RenderableOval rOval) {
		if ((Math.pow((point.getX() - rOval.centreX), 2) / (Math.pow(
				(rOval.width / 2), 2)))
				+ (Math.pow((point.getY() - rOval.centreY), 2) / (Math.pow(
						(rOval.height / 2), 2))) <= 1) {
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
	private static Point2D[] intersectingPoints(RenderablePoint rPoint,
			Line2D line) {
		if (doesCross(rPoint, line)) {
			EqnOfLine lineEqn = GenericCalc.findLine(line);
			// quadratic eqn
			double a = 1 + Math.pow(lineEqn.a, 2);
			double b = (2 * lineEqn.a * lineEqn.b) - (2 * rPoint.x)
					- (2 * rPoint.y * lineEqn.a);
			double c = Math.pow((rPoint.x), 2)
					- Math.pow((rPoint.penWidth / 2), 2)
					+ Math.pow((lineEqn.b), 2) - (2 * rPoint.y * lineEqn.b)
					+ Math.pow(rPoint.y, 2);

			return GenericCalc.quadEqn(a, b, c, lineEqn);
		}
		return null;
	}

	private static Point2D[] intersectingPoints(RenderableOval rOval,
			Line2D line) {
		if (doesCross(rOval, line)) {
			EqnOfLine lineEqn = GenericCalc.findLine(line);
			//adjust line to be moved down with ellipse to origin
			lineEqn.b -= rOval.centreY-(lineEqn.a*rOval.centreX);
			double wRad = (rOval.width / 2);
			double hRad = (rOval.height / 2);
			double a = Math.pow(lineEqn.a*wRad,2)+Math.pow(hRad,2);
			double b = 2*lineEqn.a*lineEqn.b*Math.pow(wRad,2);
			double c = Math.pow(lineEqn.b*wRad,2) - Math.pow(hRad*wRad,2);

			Point2D[] offsetInter = GenericCalc.quadEqn(a, b, c, lineEqn);
			if(offsetInter==null)
				return null;
			offsetInter[0].setLocation(offsetInter[0].getX()+rOval.centreX, offsetInter[0].getY()+rOval.centreY);
			offsetInter[1].setLocation(offsetInter[1].getX()+rOval.centreX, offsetInter[1].getY()+rOval.centreY);
			return offsetInter;
		}
		return null;
	}

	private static boolean doesCross(RenderablePoint rPoint, Line2D line) {
		if (line.ptSegDist(rPoint.x, rPoint.y) <= (rPoint.penWidth / 2)) {
			return true;
		}
		return false;
	}

	private static boolean doesCross(RenderableOval rOval, Line2D line) {
		if (pointWithin(
				getClosestPointOnLine(line, new Point2D.Double(rOval.centreX,
						rOval.centreY)), rOval)) {
			return true;
		}
		return false;
	}
	

	/**
	 * Projects new line from original gradient
	 * 
	 * @param line
	 * @param point
	 * @return
	 */
	public static Point2D getClosestPointOnLine(Line2D line, Point2D point) {
		// unit vector/gradient
		double deltaX = line.getX2() - line.getX1();
		double deltaY = line.getY2() - line.getY1();

		double productProjectAndLine = (point.getX() - line.getX1()) * deltaX
				+ (point.getY() - line.getY1()) * deltaY;
		double magnitudeOfLine = (Math.pow((deltaX), 2) + Math.pow((deltaY), 2));
		// finding lambda (when multiplied by gradient gives closest point)
		// whilst taking into account start point
		double dist = productProjectAndLine / magnitudeOfLine;
		Point2D result;
		if (dist < 0) {
			result = new Point2D.Double(line.getX1(), line.getY1());
		} else if (dist > magnitudeOfLine) {
			result = new Point2D.Double(line.getX2(), line.getY2());
		} else {
			result = new Point2D.Double((line.getX1() + (dist * deltaX)),
					line.getY1() + (dist * deltaY));
		}
		
		return result;
	}

	/**
	 * Used for individual class distinction for separate method execution
	 */
	enum RenderableClass {
		RenderableImg, RenderableObject, RenderableOval, RenderablePoint, RenderablePolygon, RenderablePolyline, RenderableRectangle, RenderableString;

	}
}
