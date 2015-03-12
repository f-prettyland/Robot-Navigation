package robot;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeMap;

import dataStructures.RRNode;
import dataStructures.RRTree;
import display.UI;
import maths.NavCalc;
import maths.ObsCalc;
import renderables.*;
import easyGui.EasyGui;
import geometry.IntPoint;

public class RRTRobot implements IRobot{
	private static int movesPerStep = 50;
	private double stepSize;

	private boolean verbose;
	private final RRTree tree;
	private UI ui;
	private int noOfMoves;
	private Renderable[] self;

	public RRTRobot(float xCoord, float yCoord, UI ui, double stepSize, boolean verbose) {
		this.ui = ui;  
		this.stepSize = stepSize;  
		this.verbose = verbose;
		
		tree = new RRTree(Color.gray);
		
		tree.setStartAndGoal(new IntPoint((int)xCoord, (int)yCoord), new IntPoint((int)ui.goal.x, (int)ui.goal.y), ((int) ui.goalCircle.width/2));
		
		ui.gui.draw(tree);
	}


	private RRNode findNewNode() {
		
		double ranX = NavCalc.randomDouble(ui.bordersize);
		double ranY = NavCalc.randomDouble(ui.bordersize);		
		RRNode nearest = tree.getNearestNeighbour(new IntPoint((int)ranX, (int)ranY));
		
		Line2D vectorToRanPoint = new Line2D.Double(nearest.x,nearest.y , ranX, ranY);
		
		
		Point2D pointAlongLine = NavCalc.pointDownLine(vectorToRanPoint, stepSize);
		output("new point at " + pointAlongLine.getX() +", "+ pointAlongLine.getY());
		
		tree.addNode(nearest, new IntPoint((int)pointAlongLine.getX(), (int)pointAlongLine.getY()));
		
		return tree.getNearestNeighbour(new IntPoint((int)pointAlongLine.getX(), (int)pointAlongLine.getY()));

		
	}

	public void go() {
		for (int i = 0; i < movesPerStep; i++) {
			RRNode newNode = findNewNode();
			if (atGoal(newNode)) {
				ui.gui.update();
				System.out.println("AT GOAL");
				break;
			}
		}
		ui.gui.update();
	}

	private void output(String strToOut) {
		if (verbose) {
			System.out.println(strToOut);
		}
	}

	private boolean atGoal(RRNode newPoint) {
		if(NavCalc.atGoal(newPoint.x,newPoint.y,ui.goalCircle)){
			ArrayList<IntPoint> path = tree.getPathFromRootTo(newPoint);
			RenderablePolyline successfulRoute = new RenderablePolyline();
			successfulRoute.setProperties(Color.green, 2.0f);
			for(IntPoint p : path){
				successfulRoute.addPoint(new RenderablePoint(p.x,p.y));
				noOfMoves++;
			}
			ui.gui.draw(successfulRoute);
			return true;
		}
		return false;
	}

	public int getNoOfMoves() {
		return noOfMoves;
	}


}
