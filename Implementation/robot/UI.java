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
	private final RRTree tree;

	private final int fextFieldId;
	private final int NO_OF_SENSORS = 8;

	public UI()
	{
		gui = new EasyGui(500, 500);

		fextFieldId = gui.addTextField(0, 0, "No of sensors");

		gui.addButton(0, 1, "ButtonA", this, "buttonActionA");


		tree = new RRTree(Color.BLACK);

		gui.draw(tree);
	}

	public void runDemo()
	{
		// Displays the GUI i.e. makes it visible.
		gui.show();
	}

	public void buttonActionA()
	{
		// This method is called when the button labeled "ButtonB" is pressed.
		System.out.println("You pressed button A");

		String textFieldString = gui.getTextFieldContent(fextFieldId);
		int i = NO_OF_SENSORS;
		try{
			i = Integer.parseInt(textFieldString);
		}catch(NumberFormatException e){
			System.out.println("Not a number, going with default value of "+ NO_OF_SENSORS);
		}
		System.out.println(i);

		// Now also call the "treeExample" method to draw an example tree.
		//fieldPotential();
	}

	public void fieldPotential()
	{
		// Please note that the coordinates in the example below are arbitrary.

		// Set the start position to (100, 100) and the goal position to (300, 300).
		// The goal radius is 40. A path that ends in the circle of this radius around
		// the goal is considered to have attained the goal position.
		tree.setStartAndGoal(new IntPoint(100, 100), new IntPoint(400, 300), 40);

		// Returns the nearest node to the point (200, 300). Currently the start position is
		// the only node in the tree, so it will be returned no matter what coordinates you
		// choose. Once you have added nodes the coordinates are important.
		RRNode nearest = tree.getNearestNeighbour(new IntPoint(200, 300));

		// Adds a new node representing the point (150, 200) to the tree.
		// The parent node is the nearest node obtained using the getNearestNeighbour method.
		tree.addNode(nearest, new IntPoint(150, 200));

		// Draws a thick red point at location (200, 300).
		RenderablePoint point = new RenderablePoint(200, 300);
		point.setProperties(Color.RED, 10.0f);
		gui.draw(point);

		// This prints the path from the start position to the position of the given node.
		// In this example we only have a reference to the "nearest" node we got above.
		// Since this happens to be the root node (representing the start position) only the
		// start position is printed.
		ArrayList<IntPoint> path = tree.getPathFromRootTo(nearest);
		System.out.println("Path:");
		for(IntPoint p : path)
			System.out.println(p);

		// Call the update method to refresh the GUI. Otherwise the newly added node will
		// not be displayed. You only need to call this method once, no matter how many nodes
		// were added.
		gui.update();
	}


	public static void main(String[] args)
	{
		UI demo = new UI();
		demo.runDemo();
	}

}
