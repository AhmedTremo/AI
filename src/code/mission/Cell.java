package code.mission;

//this is a class that was implemented to identify the positions on the grid.
public class Cell implements Comparable<Cell> {
	//x & y coordinates on the map
	int x;
	int y;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int compareTo(Cell c) {
		if (c.x == this.x) {
			return c.y - this.y;
		}
		return 1;

	}

	//this method is to check if this cell is valid within the boundaries of our current grid.
	public boolean isValid(int w, int h) {
		if (x >= 0 && x < w && y >= 0 && y < h) {
			return true;
		}
		return false;
	}

	public boolean equals(Cell c) {
		if (c.x == this.x) {
			return c.y == this.y;
		}
		return false;

	}
	@Override
	public String toString() {
		
		return "( "+this.x +" , "+ this.y+" ) ";
	}
}
