package code.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class IslandMap {
	
	//Dimensions of the grid.
	int gridWidth;
	int gridHeight;

	//a variable grid that just contains the visualization of the map in case of visualize is equal true.
	char[][] grid;
	
	//this reference is to keep a static reference to the unchanged map throughout the search and it has a setter than initializes it in case of a null value.
	private static IslandMap Instance = null;
	
	/*An arrayList that corresponds to the generated IMFmembers on the grid as well as their position.
	As well as an array that contains the initial healths of the imF members at the beginning of the problem*/
	ArrayList<Cell> IMFpositions;
	ArrayList<Integer> health;

	//A variable that is unchanged through the series of actions that indicates the initial number of generated IMFmembers.
	int initial_IMF_Count;

	//Initial positions of Ethan and the submarine
	Cell ethPosition;
	Cell submarinePosition;

	//number indicating the maximum number of IMF members that can be carried at once before returning to drop at the truck.
	int truckMax;

	//Random variable to generate the grid.
	private Random random;

	
	//Visual representation of our the elements of the problem on the char grid.
	final char ethain = 'E';
	final char submarine = 'S';
	final char imf = 'I';
	final char empty = '.';

	//First a constructor that doesn't have any data on the grid so it generates the grid randomly
	public IslandMap() {
		random = new Random();
		IMFpositions = new ArrayList<Cell>();
		this.genGrid();

	}

	//Secondly the constructor the takes a string of grid data to generate this grid.
	public IslandMap(String grid_info) {
		random = new Random();
		IMFpositions = new ArrayList<Cell>();
		this.genGrid(grid_info);

	}

	//Data generated grid.
	public void genGrid(String grid_info) {
		//First we split the string with respect to the ';' to access the data within
		String []grid_need_info = grid_info.split(";");
		String [] w_h = grid_need_info[0].split(",");
		
		//setting the grid width and height respectively according to the index of the input grid.
		this.setGridWidth(Integer.parseInt(w_h[0]));
		this.setGridHeight(Integer.parseInt(w_h[1]));
		
		//Setting the position of the components of our problem to data entry
		String [] ethposxy = grid_need_info[1].split(",");
		this.setEthPosition(new Cell(Integer.parseInt(ethposxy[1]), Integer.parseInt(ethposxy[0])));
		String [] submarinexy = grid_need_info[2].split(",");
		this.setSubmarinePosition(new Cell(Integer.parseInt(submarinexy[1]), Integer.parseInt(submarinexy[0])));
		String [] imf_positionsxy = grid_need_info[3].split(",");
		ArrayList<Cell> imf_poses = new ArrayList<Cell>();
		
		//A loop to get the imf positions in the map.
		for(int i =0 ;i <imf_positionsxy.length;i+=2) {
			int im_posy = Integer.parseInt(imf_positionsxy[i]);
			int im_posx = Integer.parseInt(imf_positionsxy[i+1]);
			imf_poses.add(new Cell(im_posx, im_posy));
		}
		this.setIMFpositions(imf_poses);
		this.setInitial_IMF_Count(imf_poses.size());
		
		//Initial health of IMF positions.
		String [] imf_healts = grid_need_info[4].split(",");
		ArrayList<Integer> imf_health = new ArrayList<Integer>();
		for(int i =0 ;i <imf_healts.length;i++) {
			int im_health = 100-Integer.parseInt(imf_healts[i]);
			imf_health.add(im_health);
		}
		this.setHealth(imf_health);
		
		this.setTruckMax(Integer.parseInt(grid_need_info[5]));

	}

	//In case of no data entry the grid is generated randomly
	public void genGrid() {
		//Randomizing the dimensions of the grid.
		gridWidth = random.nextInt(11) + 5;
		gridHeight = random.nextInt(11) + 5;
		
		health = new ArrayList<Integer>();
		
		//filling the representation grid with the char corresponding to an empty cell initially.
		grid = new char[gridWidth][gridHeight];
		for (int i = 0; i < gridWidth; i++) {
			Arrays.fill(this.grid[i], empty);
		}
		System.out.println(grid[0].length + " col :" + grid.length);
		
		//Random number of IMF members between 5 and 10
		int imfCount = random.nextInt(6) + 5;
		initial_IMF_Count = imfCount;
		
		//Random max capacity to the truck between 1 and the generated number of IMF members.
		truckMax = random.nextInt(imfCount) + 1;
		
		//Using an implemented method that returns an array of integers each integer is unique for IMF member + ethan position + the submarine position.
		ArrayList<Integer> ranGen = genPositions(imfCount + 2);
		
		int imfCounter = imfCount;
		
		for (int pos : ranGen) {
			/*We loop over the generated unique integers that indicates which cell the position is at
			 * first we divide the integer by the width of the grid to get which row the position is at
			 * then we get the remainder to indicate which column the position is at, and by that all the positions are unique
			 */
			int positionY = pos / gridWidth;
			int positionX = pos % gridWidth;
			
			//First we set the positions of the IMF members according to the count generated at the top
			if (imfCounter > 0) {
				grid[positionX][positionY] = imf;
				IMFpositions.add(new Cell(positionX, positionY));
				// System.out.println(IMFpositions.toString());
				int randHeath = random.nextInt(99) + 1;
				health.add(randHeath);
				imfCounter--;

			} else if (imfCounter == 0) {
				//if we're done generating positions for IMF members we take the position of the submarine.
				grid[positionX][positionY] = submarine;
				submarinePosition = new Cell(positionX, positionY);
				imfCounter--;

			} else {
				//lastly ethan position in case that we generated every other position on the map and since all of them is random it doesn't matter.
				grid[positionX][positionY] = ethain;
				ethPosition = new Cell(positionX, positionY);
			}

		}

	}

	public void printGrid() {
		printGrid(this.grid);
	}

	static void printGrid(char[][] grid) {
		for (char[] row : grid) {
			for (char e : row) {
				System.out.printf("%1$-10s", e);
			}
			System.out.println();
		}
	}

	/*A method that generates count number of unique random integers 
	 *that are within the range of 0 and width*height which is the number of cells in the current grid
	 */
	public ArrayList<Integer> genPositions(int count) {
		ArrayList<Integer> ranPositions = new ArrayList<Integer>();
		while (count > 0) {
			//setting an upper bound of the number of cells in the map
			int pos = random.nextInt(this.gridHeight * this.gridWidth);
			
			//conditions for unique values
			if (!(ranPositions.contains(pos))) {
				ranPositions.add(pos);
				count--;
			}

		}

		return ranPositions;

	}

	public ArrayList<Cell> getIMFpositions() {
		return IMFpositions;
	}

	public Cell getEthPosition() {
		return ethPosition;
	}

	public Cell getSubmarinePosition() {
		return submarinePosition;
	}

	//returns the shared unchanged instance of the map and initializes it in case of a null
	public static IslandMap getinstance() {
		if (Instance == null) {
			Instance = new IslandMap();
			return Instance;
		}
		return Instance;

	}

	//Setters and getters for the variables within the class
	public static IslandMap getinstance(String grid_info) {
		if (Instance == null) {
			Instance = new IslandMap(grid_info);
			return Instance;
		}
		return Instance;

	}

	public int getNumberofIMF() {
		return initial_IMF_Count;

	}

	public ArrayList<Integer> gethealth() {
		return this.health;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGrid(char[][] grid) {
		this.grid = grid;
	}

	public static void setInstance(IslandMap instance) {
		Instance = instance;
	}

	public void setIMFpositions(ArrayList<Cell> iMFpositions) {
		IMFpositions = iMFpositions;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public void setInitial_IMF_Count(int initial_IMF_Count) {
		this.initial_IMF_Count = initial_IMF_Count;
	}

	public void setEthPosition(Cell ethPosition) {
		this.ethPosition = ethPosition;
	}

	public void setSubmarinePosition(Cell submarinePosition) {
		this.submarinePosition = submarinePosition;
	}

	public void setHealth(ArrayList<Integer> health) {
		this.health = health;
	}

	public void setTruckMax(int truckMax) {
		this.truckMax = truckMax;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

}
