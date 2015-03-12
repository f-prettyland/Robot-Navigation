package display;

import java.awt.Color;
import java.util.ArrayList;

import maths.NavCalc;
import renderables.*;
import robot.FieldRobot;
import robot.IRobot;
import robot.RRTRobot;
import easyGui.EasyGui;

public class UI {

	public final EasyGui gui;
	private  IRobot r;
	private  IRobot rr;
	public  ArrayList<Renderable> map;
	public RenderablePoint goal;
	public RenderableOval goalCircle;

	private final int noOfSensorsID;
	private final int noOfCirclesID;
	private final int xCoordID;
	private final int yCoordID;
	private final int nO_OF_SAMPLES = 7;
	private final int nO_OF_CIRCLES = 20;
	private int stepSize = 40;
	private int xCoord = 100;
	private int yCoord = 100;
	public int bordersize = 700;

	public UI()
	{
		gui = new EasyGui(bordersize, bordersize);
		map= new ArrayList<Renderable>();
		noOfSensorsID = gui.addTextField(0, 0, "No of sensors");
		xCoordID = gui.addTextField(0, 1, "xStart");
		yCoordID = gui.addTextField(0, 2, "yStart");
		noOfCirclesID = gui.addTextField(0, 3, "No Of Circles");

		gui.addButton(1, 0, "Goal n' Robot", this, "makeRobot");
		gui.addButton(1, 1, "Make Circles", this, "makeCircles");
		gui.addButton(1, 2, "Remake Robot", this, "remakeRobot");


		gui.addButton(4, 0, "Field Potential", this, "fieldPotentialStart");
		gui.addButton(4, 1, "RRT", this, "rRTStart");
		
	}

	public void runDemo()
	{
		// Displays the GUI i.e. makes it visible.
		gui.show();
	}
	
	public void makeGoal(float x, float y)
	{
		goal = new RenderablePoint(x+400, y+400);
		goalCircle = new RenderableOval((int)x+400,(int)y+400, 50, 50);
		goalCircle.setProperties(Color.DARK_GRAY, 2.0f, false);
		goal.setProperties(Color.CYAN, 8.0f);
		gui.draw(goal);
		gui.draw(goalCircle);
	}
	
	
	
	public void makeCircles()
	{

		int circleNo = getField(noOfCirclesID,nO_OF_CIRCLES);
		
		for(int i = 0; i < circleNo; i++) {
			RenderablePoint newCircle = new RenderablePoint(NavCalc.randomDouble(bordersize), NavCalc.randomDouble(bordersize));
			newCircle.setProperties(Color.ORANGE, 20.0f);
			System.out.printf("new circ at %f,%f",newCircle.x,newCircle.y);
			map.add(newCircle);
			gui.draw(newCircle);
		}
		gui.update();
	}
	
	public void remakeRobot()
	{
		
		int sens = getField(noOfSensorsID,nO_OF_SAMPLES);
		xCoord = getField(xCoordID,xCoord);
		yCoord = getField(yCoordID,yCoord);
		
		r = new FieldRobot(xCoord, yCoord, this, nO_OF_SAMPLES, 80,40, stepSize, true,sens);
	}
	
	public void makeRobot()
	{
		gui.clearGraphicsPanel();
		map= new ArrayList<Renderable>();
		int sens = getField(noOfSensorsID,nO_OF_SAMPLES);
		yCoord= getField(yCoordID,yCoord);
		xCoord = getField(xCoordID,xCoord);
		
		
		makeGoal(xCoord,yCoord);
		
		r = new FieldRobot(xCoord, yCoord, this, sens, 80,40, stepSize, true,0);
		rr = new RRTRobot(xCoord, yCoord, this, stepSize, true);
	}

	public void fieldPotentialStart()
	{
		if(goal ==null){
			System.out.println("No goal in place!");
			return;
		}
		runRobot(r);
	}

	private int getField(int iD, int defaultValue) {
		String textFieldString = gui.getTextFieldContent(iD);
		int i = defaultValue;
		try{
			i = Integer.parseInt(textFieldString);
		}catch(NumberFormatException e){
			System.out.println("Not a number, going with default value of "+ i);
		}
		System.out.println(i);
		return i;
	}
	
	public void rRTStart()
	{
		if(goal ==null){
			System.out.println("No goal in place!");
			return;
		}
		runRobot(rr);
	}

	public void runRobot(IRobot r)
	{
		r.go();
		gui.update();
	}


	public static void main(String[] args)
	{
		UI demo = new UI();
		demo.runDemo();
	}

}
