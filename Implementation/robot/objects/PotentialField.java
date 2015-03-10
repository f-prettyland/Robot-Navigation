package robot.objects;

public abstract class PotentialField {
	private int xCoord;
	private int yCoord;

	public abstract int valueAt(int x, int y);

}
