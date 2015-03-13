package maths;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.Double;

public class GenericCalc {
	
	public static EqnOfLine findLine(Line2D line) {
		Point2D point = line.getP1();
		Point2D point2 = line.getP2();
		// gradient of the line
		double m = (point.getY() - point2.getY())
				/ (point.getX() - point2.getX());
		// y intersect
		double b = point.getY() - (m * point.getX());
		
		return new EqnOfLine(m, b);
	}
	
	public static Point2D closestOfTwoPoints(Point2D from, Point2D[] solns){
		if (from.distance(solns[0]) > from.distance(solns[1])) {
			return  solns[1];
		} else {
			return  solns[0];
		}
	}

	public static Point2D[] quadEqn(double a, double b, double c, EqnOfLine lineEqn) {
		// quadratic eqn
		double p1 = (-1) * b / (2 * a);
		double p2 = Math.sqrt(Math.pow(b, 2) - (4 * a * c)) / (2 * a);

		double x1 = p1 + p2;
		double x2 = p1 - p2;

		double y1 = lineEqn.a * x1 + lineEqn.b;
		double y2 = lineEqn.a * x2 + lineEqn.b;
		if (!Double.isNaN(p2)) {
			Point2D[] solns = new Point2D[2];
			solns[0] = new Point2D.Double(x1,y1);
			solns[1] = new Point2D.Double(x2,y2);
			return solns;
		}
		return null;
	}


	public static class EqnOfLine {
		public EqnOfLine(double a, double b) {
			this.a = a;
			this.b = b;
		}

		double a;
		double b;
	}

}
