package maths;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import renderables.*;

public class ObsCalc {
	public static boolean pointWithin(Point2D point, Renderable shape) {
		RenderableClass rClass = RenderableClass.valueOf(shape.getClass()
				.getSimpleName());
		switch (rClass) {
		case RenderablePoint:
			return pointWithin(point, (RenderablePoint) shape);
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
		default:
			System.out.println("Shape not configured for");
			return null;
		}
	}

	private static boolean pointWithin(Point2D point, RenderablePoint rPoint) {
		if (Math.pow((point.getX() - rPoint.x), 2)
				+ Math.pow((point.getY() - rPoint.x), 2) <= Math.pow(
				(rPoint.penWidth / 2), 2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * After ensuring the line and circle meet the intersection canbe found by
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
		if (line.ptLineDist(rPoint.x, rPoint.y) <= (rPoint.penWidth / 2)) {
			EqnOfLine lineEqn = findLine(line);
			// quadratic eqn
			double a = 1 + Math.pow(lineEqn.a, 2);
			double b = (2 * lineEqn.a * lineEqn.b) - (2 * rPoint.x)
					- (2 * rPoint.y * lineEqn.b);
			double c = Math.pow((rPoint.penWidth / 2), 2)
					- Math.pow((rPoint.x), 2) - Math.pow((lineEqn.b), 2)
					+ (2 * lineEqn.a * lineEqn.b) - Math.pow(rPoint.y, 2);

			double p1 = (-1) * b / (2 * a);
			double p2 = Math.sqrt(Math.pow(b, 2) - (4 * a * c)) / (2 * a);

			double x1 = p1 + p2;
			double x2 = p1 - p2;

			double y1 = lineEqn.a * x1 + lineEqn.b;
			double y2 = lineEqn.a * x2 + lineEqn.b;

			// find which point is closest
			if (loc.distance(x1, y1) > loc.distance(x2, y2)) {
				return new Point2D.Double(x2, y2);
			} else {
				return new Point2D.Double(x1, y1);
			}
		}
		return null;
	}

	private static EqnOfLine findLine(Line2D line) {
		Point2D point = line.getP1();
		Point2D point2 = line.getP2();
		// gradient of the line
		double m = (point.getY() - point2.getY())
				/ (point.getX() - point2.getX());
		// y intersect
		double b = point.getY() - (m * point.getX());

		return new EqnOfLine(m, b);
	}

	public static class EqnOfLine {
		public EqnOfLine(double a, double b) {
			this.a = a;
			this.b = b;
		}

		double a;
		double b;
	}

	enum RenderableClass {
		RenderableImg, RenderableObject, RenderableOval, RenderablePoint, RenderablePolygon, RenderablePolyline, RenderableRectangle, RenderableString;

	}
}
