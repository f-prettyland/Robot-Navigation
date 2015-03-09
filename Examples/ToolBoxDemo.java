import java.awt.Color;
import java.util.ArrayList;
import renderables.RenderablePoint;
import dataStructures.RRNode;
import dataStructures.RRTree;
import easyGui.EasyGui;
import geometry.IntPoint;

public class ToolBoxDemo
{
	private final EasyGui gui;
	private final RRTree tree;

	private final int fextFieldId;

	public ToolBoxDemo()
	{
		// Create a new EasyGui instance with a 500x500pixel graphics panel.
		gui = new EasyGui(500, 500);

		// Add a label to the cell in row 0 and column 0.
		gui.addLabel(0, 0, "Label (row = 0, col = 0)");

		// Add a text field to the cell in row 1 and column 0. The returned ID is
		// stored in fextFieldId to allow access to the field later on.
		fextFieldId = gui.addTextField(1, 0, "Text field (row = 1, col = 0)");

		// Add a button in row 0 column 1. The button is labeled "ButtonA" and
		// when pressed it will call the method called buttonActionA in "this"
		// instance of the ToolBoxDemo class.
		gui.addButton(0, 1, "ButtonA", this, "buttonActionA");

		// Add a button in row 1 column 1. It calls the method called "buttonActionB".
		gui.addButton(1, 1, "ButtonB", this, "buttonActionB");

		// Create an RRTree to be used later.
		tree = new RRTree(Color.BLACK);

		// Tell the GUI to draw this tree. Because the tree has only just been created
		// there is nothing to draw yet but there will be later on.
		gui.draw(tree);
	}

	public void runDemo()
	{
		// Displays the GUI i.e. makes it visible.
		gui.show();
	}

	public void buttonActionA()
	{
		// This method is called when the button labeled "ButtonA" is pressed.
		System.out.println("You pressed button A");

		// Get the string in the text field and print it out.
		String textFieldString = gui.getTextFieldContent(fextFieldId);
		System.out.println("The text field contains the string: \"" + textFieldString + "\"");

		// Note that you can parse the returned string to another data type. E.g.:
		// int i = Integer.parseInt(textFieldString);
		// float f = Float.parseFloat(textFieldString);
	}

	public void buttonActionB()
	{
		// This method is called when the button labeled "ButtonB" is pressed.
		System.out.println("You pressed button B");

		// Now also call the "treeExample" method to draw an example tree.
		treeExample();
	}

	public void treeExample()
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

	// MAIN
	public static void main(String[] args)
	{
		ToolBoxDemo demo = new ToolBoxDemo();
		demo.runDemo();
	}
}
