package robot;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeMap;

import display.UI;
import maths.NavCalc;
import maths.ObsCalc;
import renderables.*;
import easyGui.EasyGui;

public class Robot {
	private static int movesPerStep = 1;
	private double heading;
	private double xCoord;
	private double yCoord;

	private double sensorRad;
	private double sampleRad;

	private ArrayList<Double> sensorOffsets;
	private ArrayList<Double> sampleOffsets;
	private int noOfMoves;
	private int noOfTurns;
	private boolean verbose;
	private UI ui;
	private Renderable[] self;

	public Robot(float xCoord, float yCoord, UI ui, int noOfSamples,
			int noOfSensors, double sensorRad, double sampleRad, boolean verbose) {
		noOfMoves = 0;
		noOfTurns = 0;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.ui = ui;
		this.sensorRad = sensorRad;
		this.sampleRad = sampleRad;
		this.verbose = verbose;

		// Calculating sensor angle offsets
		sensorOffsets = new ArrayList<Double>();
		double angleSep = (180 / (noOfSensors + 1));
		for (int i = 1; i <= noOfSensors; i++) {
			// Minus 90 since the heading is at 90 degrees
			// Thus the bearing of any offset can be calc'd by adding it to the
			// heading
			sensorOffsets.add(angleSep * i - 90);
		}

		// Calculating sensor angle offsets
		sampleOffsets = new ArrayList<Double>();
		angleSep = (180 / (noOfSamples + 1));
		for (int i = 1; i <= noOfSamples; i++) {
			// Minus 90 since the heading is at 90 degrees
			// Thus the bearing of any offset can be calc'd by adding it to the
			// heading
			sampleOffsets.add(angleSep * i - 90);
		}

		createRenderableSelf();
		ui.gui.draw(self);
		ui.gui.update();
	}

	private boolean checkHit(Point2D hitpoint) {
		for (Renderable obs : ui.map) {
			if (ObsCalc.pointWithin(hitpoint, obs)) {
				return true;
			}
		}
		return false;
	}

	private void decideDir() {
		ArrayList<Point2D> hittingHitpoints = new ArrayList<Point2D>();
		for (Double sensor : sensorOffsets) {
			Point2D potentialHit = NavCalc.toCartesian(sensorRad, sensor);
			if (checkHit(potentialHit)) {
				hittingHitpoints.add(potentialHit);
			}
		}

		TreeMap<Double, Double> directionPotentials = new TreeMap<Double, Double>();
		for (Double possAngle : sampleOffsets) {
			Point2D possDir = NavCalc.toCartesian(sampleRad, possAngle);
			double potential = NavCalc.repulsionAt(possDir, hittingHitpoints);
			potential += NavCalc.attractionAt(possDir, ui.goal);
			directionPotentials.put(potential, possAngle);
		}
		;
		// if all has same potential
		if (directionPotentials.size() == 1) {
			turn(sampleOffsets.get(sampleOffsets.size() / 2));
		} else {
			turn(directionPotentials.firstEntry().getValue());
		}
		move(sampleRad);
		reDraw();
	}

	private void reDraw() {
		ui.gui.unDraw(self);
		RenderablePoint oldSelf = new RenderablePoint((float) xCoord,
				(float) yCoord);
		oldSelf.setProperties(Color.gray, 5.0f);
		ui.gui.draw(oldSelf);
		createRenderableSelf();
		ui.gui.draw(self);
		ui.gui.update();

	}

	private void createRenderableSelf() {
		RenderablePolyline oldSelfPath;
		if (self != null) {
			oldSelfPath = (RenderablePolyline) self[1];
		} else {
			oldSelfPath = new RenderablePolyline();
			oldSelfPath.setProperties(Color.gray, 1.0f);
		}
		self = new Renderable[3];
		self[0] = new RenderablePoint((float) xCoord, (float) yCoord);
		((RenderablePoint) self[0]).setProperties(Color.RED, 10.0f);

		// adding to the breadcrumb trail
		oldSelfPath.addPoint((int) xCoord, (int) yCoord);
		self[1] = oldSelfPath;

		// Creating the heading line
		RenderablePolyline headingLine = new RenderablePolyline();
		headingLine.addPoint((int) xCoord, (int) yCoord);
		Point2D offset = NavCalc.toCartesian(11, heading);
		headingLine.addPoint((int) (xCoord + offset.getX()),
				(int) (yCoord + offset.getY()));
		self[2] = headingLine;
	}

	public void turn(double angle) {
		heading = NavCalc.addToBearing(heading, angle);
		output("Turned " + angle + " to new bearing " + heading);
		noOfTurns += 1;
	}

	public void move(double unitsForward) {
		Point2D offset = NavCalc.toCartesian(unitsForward, heading);
		xCoord += offset.getX();
		yCoord += offset.getY();
		output("Moved " + unitsForward + " to (" + xCoord + "," + yCoord + ")");
		noOfMoves += 1;
	}

	public void go() {
		for (int i = 0; i < movesPerStep; i++) {
			decideDir();
			if (atGoal()) {
				break;
			}
		}
	}

	private void output(String strToOut) {
		if (verbose) {
			System.out.println(strToOut);
		}
	}

	private boolean atGoal() {

		return false;
	}

	public int getNoOfMoves() {
		return noOfMoves;
	}

	public int getNoOfTurns() {
		return noOfTurns;
	}

}
