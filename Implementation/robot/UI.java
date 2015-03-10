package robot;

import java.awt.Color;
import java.util.ArrayList;
import renderables.RenderablePoint;
import dataStructures.RRNode;
import dataStructures.RRTree;
import easyGui.EasyGui;
import geometry.IntPoint;

public class UI {

	private final EasyGui gui;
	private  Robot r;
	private final RRTree tree;

	private final int noOfSensorsID;
	private final int xCoordID;
	private final int yCoordID;
	private final int nO_OF_SAMPLES = 8;
	private final int xCOORD = 100;
	private final int yCOORD = 100;

	public UI()
	{
		gui = new EasyGui(500, 500);

		noOfSensorsID = gui.addTextField(0, 0, "No of sensors");
		xCoordID = gui.addTextField(0, 1, "xStart");
		yCoordID = gui.addTextField(0, 2, "yStart");

		gui.addButton(1, 1, "ButtonA", this, "buttonActionA");
		gui.addButton(1, 0, "Make Robot", this, "makeRobot");


		tree = new RRTree(Color.BLACK);

		gui.draw(tree);
	}

	public void runDemo()
	{
		// Displays the GUI i.e. makes it visible.
		gui.show();
	}
	
	public void makeRobot()
	{

		int sens = getField(noOfSensorsID,nO_OF_SAMPLES);
		int x = getField(yCoordID,yCOORD);
		int y = getField(xCoordID,xCOORD);

		r = new Robot(x, y, gui, sens, 5, 5, true);
	}

	public void buttonActionA()
	{
		// Now also call the "treeExample" method to draw an example tree.
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
