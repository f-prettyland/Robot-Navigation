package robot;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import renderables.*;
import robot.maths.NavCalc;
import easyGui.EasyGui;

public class Robot {
	private static int movesPerStep=1;
	private double heading;
	private double xCoord;
	private double yCoord;
	
	private int sensorRad;
	private int sampleRad;
	
	private int noOfSamples;
	private ArrayList<Double> sensorOffsets;
	private int noOfMoves;
	private int noOfTurns;
	private boolean verbose;
	private EasyGui gui;
	private Renderable[] self;
	
	public Robot(float xCoord,float yCoord, EasyGui gui, int noOfSamples, int sensorRad, int sampleRad,boolean verbose){
		noOfMoves=0;
		noOfTurns=0;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.gui = gui;
		this.noOfSamples = noOfSamples;
		this.sensorRad = sensorRad;
		this.sampleRad = sampleRad;
		this.verbose = verbose;
		
		//Calculating sensor angle offsets
		sensorOffsets = new ArrayList<Double>();
		double angleSep = (180/noOfSamples);
		for(int i= 1; i<= noOfSamples; i++){
			//Minus 90 since the heading is at 90 degrees
			//Thus the bearing of any offset can be calc'd by adding it to the heading
			sensorOffsets.add(angleSep*i -90);
		}
		
		createRenderableSelf();
		gui.draw(self);
		gui.update();
	}

	private void reDraw(){
		gui.unDraw(self);
		RenderablePoint oldSelf =  new RenderablePoint((float) xCoord, (float) yCoord);
		oldSelf.setProperties(Color.gray, 5.0f);
		gui.draw(oldSelf);
		createRenderableSelf();
		gui.draw(self);
		gui.update();
		
	}
	
	private void createRenderableSelf(){
			RenderablePolyline oldSelfPath;
			if(self !=null){
				oldSelfPath = (RenderablePolyline) self[1];
			}else{
				oldSelfPath = new RenderablePolyline();
				oldSelfPath.setProperties(Color.gray, 1.0f);
			}
			self = new Renderable[3];
			self[0] = new RenderablePoint((float) xCoord, (float) yCoord);
			((RenderablePoint) self[0]).setProperties(Color.RED, 10.0f);
			

			//adding to the breadcrumb trail
			oldSelfPath.addPoint((int) xCoord, (int) yCoord);
			self[1] = oldSelfPath;
			
			//Creating the heading line
			RenderablePolyline headingLine = new RenderablePolyline();
			headingLine.addPoint((int) xCoord, (int) yCoord);
			Point2D offset = NavCalc.toCartesian(11, heading);
			headingLine.addPoint((int) (xCoord+ offset.getX()), (int)(yCoord+ offset.getY()));
			self[2] = headingLine;
	}

	public void turn(double angle){
		heading = NavCalc.addToBearing(heading, angle);
		output("Turned " + angle + " to new bearing "+ heading);
		noOfTurns++;
	}
	
	public void move(int unitsForward){
		Point2D offset = NavCalc.toCartesian(unitsForward, heading);
		xCoord += offset.getX();
		yCoord += offset.getY();
		output("Moved " + unitsForward + " to ("+xCoord+","+yCoord+")");
		noOfMoves++;
	}
	

	public void go(){
		for(int i= 0; i< movesPerStep; i++){
			move(20);
			reDraw();

			turn(95);
			if(atGoal()){
				break;
			}
		}
	}
	
	
	private void output(String strToOut){
		if(verbose){
			System.out.println(strToOut);
		}
	}

	private boolean atGoal(){
		
		return false;
	}

}
