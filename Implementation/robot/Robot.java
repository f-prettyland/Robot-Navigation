package robot;

import java.util.ArrayList;

import renderables.Renderable;

import easyGui.EasyGui;

public class Robot {
	private double heading;
	private int xCoord;
	private int yCoord;
	private int senorRad;
	private int sampleRad;
	private int noOfSensors;
	private ArrayList<Integer> sensorOffsets;
	private int noOfMoves;
	private int noOfTurns;
	private boolean verbose;
	private EasyGui gui;
	private Renderable[] self;

	public void update(){


	}

	public void turn(double angle){

		output("Turned " + angle);
		noOfTurns++;
	}

	public void move(int unitsForward){

		output("Moved " + unitsForward + " to ("+xCoord+","+yCoord+")");
		noOfMoves++;
	}

	private void output(String strToOut){
		if(verbose){
			System.out.println(strToOut);
		}
	}

}
