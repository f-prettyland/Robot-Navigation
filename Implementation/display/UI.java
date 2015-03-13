package display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;

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
	private TreeMap<Integer,Integer> fields;
	private final int noOfCirclesID;
	private final int noOfOvalsID;
	private final int nO_OF_CIRCLES = 20;
	private final int nO_OF_OVALS = 20;
	public int bordersize = 700;

	public UI()
	{
		gui = new EasyGui(bordersize, bordersize);
		map= new ArrayList<Renderable>();
		fields = new TreeMap<Integer,Integer>();
		fields.put(gui.addTextField(0, 1, "xStart"),100);
		fields.put(gui.addTextField(0, 2, "yStart"),100);
		fields.put(gui.addTextField(0, 3, "No of sample points"),7);
		fields.put(gui.addTextField(0, 4, "No of sensors"),20);
		fields.put(gui.addTextField(0, 5, "Sensor radius"),40);
		fields.put(gui.addTextField(0, 6, "Step size"),10);
		fields.put(gui.addTextField(0, 7, "Verbosity: 1 or 0"),10);
		
		gui.addButton(1, 0, "Goal n' Robot", this, "makeRobot");
		
		noOfCirclesID = gui.addTextField(3, 0, "No Of Circles");
		gui.addButton(4, 0, "Make Circles", this, "makeCircles");
		
		noOfOvalsID = gui.addTextField(3, 1, "No Of Ovals");
		gui.addButton(4, 1, "Make Ovals", this, "makeOvals");


		gui.addButton(6, 0, "Field Potential", this, "fieldPotentialStart");
		gui.addButton(6, 1, "RRT", this, "rRTStart");
		
	}
	

	public void makeRobot()
	{
		gui.clearGraphicsPanel();
		map= new ArrayList<Renderable>();
		int[] startingValues = new int[7];
		int i =0;
		for(int fieldID : fields.keySet()){
			startingValues[i] = getField(fieldID,fields.get(fieldID));
			i++;
		}
		
		makeGoal(startingValues[0],startingValues[1]);
		boolean verbose;
		if(startingValues[6]==1){
			verbose=true;
		}else{
			verbose=false;
		}
		
		r = new FieldRobot(startingValues, this, verbose);
		rr = new RRTRobot(startingValues[0], startingValues[1], this, startingValues[5], verbose);
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
	
	
	
	public void makeOvals()
	{
		
		int ovalNo = getField(noOfOvalsID,nO_OF_OVALS);
		
		for(int i = 0; i < ovalNo; i++) {
			RenderableOval newOval = new RenderableOval((int)NavCalc.randomDouble(bordersize), (int)NavCalc.randomDouble(bordersize), 10, 20);
			newOval.setProperties(Color.ORANGE, 1.0f,true);
			map.add(newOval);
			gui.draw(newOval);
		}
		gui.update();
	}
	
	public void makeCircles()
	{

		int circleNo = getField(noOfCirclesID,nO_OF_CIRCLES);
		
		for(int i = 0; i < circleNo; i++) {
			RenderablePoint newCircle = new RenderablePoint(NavCalc.randomDouble(bordersize), NavCalc.randomDouble(bordersize));
			newCircle.setProperties(Color.ORANGE, 20.0f);
			map.add(newCircle);
			gui.draw(newCircle);
		}
		gui.update();
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
