package display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import renderables.*;
import robot.Robot;
import dataStructures.RRNode;
import dataStructures.RRTree;
import easyGui.EasyGui;

public class UI {

	public final EasyGui gui;
	private  Robot r;
	public  ArrayList<Renderable> map;
	public RenderablePoint goal;
	private final RRTree tree;

	private final int noOfSensorsID;
	private final int noOfCirclesID;
	private final int xCoordID;
	private final int yCoordID;
	private final int nO_OF_SAMPLES = 7;
	private final int nO_OF_CIRCLES = 20;
	private final int xCOORD = 100;
	private final int yCOORD = 100;
	private final int bORDERSIZE = 700;

	public UI()
	{
		gui = new EasyGui(bORDERSIZE, bORDERSIZE);
		map= new ArrayList<Renderable>();
		noOfSensorsID = gui.addTextField(0, 0, "No of sensors");
		xCoordID = gui.addTextField(0, 1, "xStart");
		yCoordID = gui.addTextField(0, 2, "yStart");
		noOfCirclesID = gui.addTextField(0, 3, "No Of Circles");

		gui.addButton(1, 0, "Goal n' Robot", this, "makeRobot");
		gui.addButton(1, 1, "Make Circles", this, "makeCircles");
		gui.addButton(1, 2, "Remake Robot", this, "remakeRobot");


		gui.addButton(4, 0, "Field Potential", this, "fieldPotentialStart");
		gui.addButton(4, 1, "RRT", this, "fieldPotentialStart");
		tree = new RRTree(Color.BLACK);

		gui.draw(tree);
	}

	public void runDemo()
	{
		// Displays the GUI i.e. makes it visible.
		gui.show();
	}
	
	public void makeGoal(float x, float y)
	{
		goal = new RenderablePoint(x+400, y+400);
		RenderableOval goalCircle = new RenderableOval((int)x+400,(int)y+400, 50, 50);
		goalCircle.setProperties(Color.DARK_GRAY, 2.0f, false);
		goal.setProperties(Color.CYAN, 8.0f);
		gui.draw(goal);
		gui.draw(goalCircle);
	}
	
	private float randomDouble(double max)
	{
		Random random = new Random();
		return (float) (random.nextFloat() * max);
	}
	
	public void makeCircles()
	{

		int circleNo = getField(noOfCirclesID,nO_OF_CIRCLES);
		
		for(int i = 0; i < circleNo; i++) {
			RenderablePoint newCircle = new RenderablePoint(randomDouble(bORDERSIZE), randomDouble(bORDERSIZE));
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
		int x = getField(yCoordID,yCOORD);
		int y = getField(xCoordID,xCOORD);
		
		r = new Robot(x, y, this, nO_OF_SAMPLES, 80,40, 10, true,sens);
	}
	
	public void makeRobot()
	{
		gui.clearGraphicsPanel();
		int sens = getField(noOfSensorsID,nO_OF_SAMPLES);
		int x = getField(yCoordID,yCOORD);
		int y = getField(xCoordID,xCOORD);
		
		
		makeGoal(x,y);
		
		r = new Robot(x, y, this, sens, 80,40, 10, true,0);
	}

	public void fieldPotentialStart()
	{
		if(goal ==null){
			System.out.println("No goal in place!");
			return;
		}
		fieldPotential();
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

	public void fieldPotential()
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
