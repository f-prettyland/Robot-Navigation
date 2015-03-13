package robot;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.TreeMap;

import display.UI;
import maths.NavCalc;
import maths.ObsCalc;
import renderables.*;
import easyGui.EasyGui;

public class FieldRobot implements IRobot{
	private static int movesPerStep = 10;
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

	public FieldRobot(int[] startV, UI ui, boolean verbose){
		this(startV[0],startV[1], ui, startV[2],startV[3],startV[4],startV[5], verbose, startV[6]);
	}
	
	public FieldRobot(int xCoord, int yCoord, UI ui, int noOfSamples,
			int noOfSensors, int sensorRad, int sampleRad, boolean verbose, int heading) {
		noOfMoves = 0;
		noOfTurns = 0;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.ui = ui;
		this.sensorRad = sensorRad;
		this.sampleRad = sampleRad;
		this.verbose = verbose;
		this.heading = heading;

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

	private HitDetails checkHit(Point2D hitpoint) {;
		for (Renderable obs : ui.map) {
			
			Point2D closestPoint = ObsCalc.closestIntersection(new Point2D.Double(xCoord,yCoord), obs, new Line2D.Double(xCoord, yCoord,hitpoint.getX(),hitpoint.getY()));
			if(closestPoint!= null && closestPoint.distance(xCoord, yCoord)<=sensorRad){
				RenderablePolyline obs1 = new RenderablePolyline();
				obs1.addPoint((int)xCoord, (int)yCoord);
				obs1.addPoint((int)closestPoint.getX(),(int) closestPoint.getY());
				obs1.setProperties(Color.decode("0xFF6868"), 1.0f);
				ui.gui.draw(obs1);
				ui.gui.update();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				ui.gui.update();
				return new HitDetails (obs,closestPoint);
			}else if (ObsCalc.pointWithin(hitpoint, obs)) {
				System.out.println("HHHHHHHHHHHHHHHHHHHHEEEEEEEEEEEEEEEYYYYYYY");
				return new HitDetails (obs,hitpoint);				
			}

		}
		return null;
	}

	private void decideDir() {
		ArrayList<HitDetails> hittingHitpoints = new ArrayList<HitDetails>();
		for (Double sensor : sensorOffsets) {
			Point2D potentialHitOffset = NavCalc.toCartesian(sensorRad, NavCalc.addToBearing(heading, sensor));
			Point2D potentialHit =  new Point2D.Double(xCoord+potentialHitOffset.getX(), yCoord+potentialHitOffset.getY());
			HitDetails hitDet =  checkHit(potentialHit);
			if ( hitDet!= null) {
				hittingHitpoints.add(hitDet);
			}
		}
		
		int insideDir=0;
		TreeMap<Double, Double> directionPotentials = new TreeMap<Double, Double>();
		for (Double possAngle : sampleOffsets) {
			Point2D possOffset =  NavCalc.toCartesian(sampleRad, NavCalc.addToBearing(heading, possAngle));
			Point2D possDir =  new Point2D.Double(xCoord+possOffset.getX(), yCoord+possOffset.getY());
			double potential = NavCalc.repulsionAt(sensorRad,possDir, hittingHitpoints, new Point2D.Double(xCoord, yCoord), ui.map);
			if(potential>0)
				output(String.format("Repulse poten: %.2f\n",potential));
			if(potential>NavCalc.INSIDE_SCALAR)
				insideDir++;
			potential += NavCalc.attractionAt(possDir, ui.goal);
			output(String.format("Angle: %.1f Poten: %.2f\n", possAngle, potential));
			directionPotentials.put(potential, possAngle);
		}
		//if all directions all inside a circle
		if(insideDir >= directionPotentials.size() ){
			turn(180);
		}else{
			// if all has same potential
			if (directionPotentials.size() == 1) {
				//Go for close to center movement
				turn(sampleOffsets.get(sampleOffsets.size() / 2));
			} else{
				turn(directionPotentials.firstEntry().getValue());
			}
			//paths lead inside
			
			move(sampleRad);
		}
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
		self = new Renderable[6];
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
		
		RenderableOval sensCircle = new RenderableOval((int)xCoord,(int) yCoord,(int) (2*sensorRad), (int) (2*sensorRad));
		sensCircle.setProperties(Color.DARK_GRAY, 1.0f, false);
		self[3] = sensCircle;
		
		RenderableOval samsCircle = new RenderableOval((int)xCoord,(int) yCoord,(int) (2*sampleRad), (int) (2*sampleRad));
		samsCircle.setProperties(Color.LIGHT_GRAY, 1.0f, false);
		self[4] = samsCircle;
		
		self[5] = new RenderablePolyline();
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
			if (NavCalc.atGoal(xCoord,yCoord,ui.goalCircle)) {
				NavCalc.atGoal(xCoord,yCoord,ui.goalCircle);
				((RenderablePolyline)self[1]).setProperties(Color.green,1.0f);
				ui.gui.draw(self);
				System.out.println("AT GOAL");
				output("In "+ noOfMoves +" moves and " + noOfTurns +" turns");
				break;
			}
			decideDir();
		}
	}

	private void output(String strToOut) {
		if (verbose) {
			System.out.println(strToOut);
		}
	}

	

	public int getNoOfMoves() {
		return noOfMoves;
	}

	public int getNoOfTurns() {
		return noOfTurns;
	}
	
	

}
