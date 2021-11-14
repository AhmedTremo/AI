package code.mission;

import java.util.ArrayList;

import code.generic.*;

public class IslandState extends State {

	//A cell that defined ethan's current position on the grid
	Cell ethposition;
	
	//A final integer that indicated the maximum capacity of IMF members that can be carried at once by Ethan
	final int maxCAP;
	
	//An integer that indicated the number of IMF members current withheld in the truck with ethan in this state
	int acqIMF;
	
	//An arrayList that corresponds to the number as well as the position of IMF members still on the map and were not carried.
	ArrayList<Cell> IMFpositions;
	
	//A cell that  corresponds to the submarine position on the grid.
	Cell submarine;
	
	//An ArrayList of integers that change the values throughout the actions taken by Ethan to increment the damage applied on each of the IMF members.
	ArrayList<Integer> health;
	
	//The current number of IMF members who died so far till this state.
	int numberofdeaths = 0;
	
	//A class that holds the position as well as the current health of all IMF members initially generated.
	ArrayList<cellhealthtuple> state_healts_pos; //an array for the IMF members and their corresponding health

	public IslandState(Cell position, int acqIMF, ArrayList<Cell> IMFposition, int maxCap, Cell submarine,
			ArrayList<Integer> health) {
		this.ethposition = position;
		this.maxCAP = maxCap;
		this.acqIMF = acqIMF;
		this.IMFpositions = IMFposition;
		this.submarine = submarine;
		this.health = health;
		state_healts_pos =  new ArrayList<cellhealthtuple>();

	}

	public ArrayList<Integer> getHealth() {
		return health;
	}

	public void setHealth(ArrayList<Integer> health) {
		this.health = health;
	}

	public Cell getPosition() {
		return ethposition;
	}

	public int getMaxCAP() {
		return maxCAP;
	}

	public int getAcqIMF() {
		return acqIMF;
	}

	public ArrayList<Cell> getIMFpositions() {
		return IMFpositions;
	}

	public void setPosition(Cell position) {
		this.ethposition = position;
	}

	public void setAcqIMF(int acqIMF) {
		this.acqIMF = acqIMF;
	}

	public void setIMFpositions(ArrayList<Cell> iMFpositions) {
		IMFpositions = iMFpositions;
	}

	public Cell getSubmarine() {
		return submarine;
	}

	@Override
	public String toString() {
		return "ethposition:  " + ethposition.toString() + " AquiredIMF:  " + acqIMF + " MAX CAP: " + maxCAP
				+ " IMF positions " + IMFpositions.toString() + " Healtsh =" + health + " submarine : "
				+ submarine.toString();
	}
	
	
	//compareTo method was overriden to identify where to put the state in the hashtable to handle repeated state.
	@Override
	public int compareTo(State islState) {
		boolean Flag = false;
		IslandState islstate = ((IslandState) islState);
		//First we compare current ethan position, then the number of IMF members in the truck, then we compare the remaining IMF members to get picked up.
		if (islstate.ethposition.compareTo(this.ethposition) == 0) {
			if (islstate.acqIMF == this.acqIMF) {
				if (this.IMFpositions.size() != islstate.IMFpositions.size()) {
					return 1;
				}
				for (Cell imfpos : IMFpositions) {
					Flag = false;
					for (Cell islimfpos : islstate.IMFpositions) {
						if (islimfpos.equals(imfpos))
							Flag = true;
					}
					if (!Flag)
						return 1;

				}
				return 0;
			}
			return 1;

		}
		return 1;
	}

	//We had to override the hashcode method that identifies the state so that it contains info about everything that might identify a unique state.
	@Override
	public int hashCode() {
		String s="";
		if(SearchProblem.getQueue().equals("UC") || SearchProblem.getQueue().equals("AS1") || SearchProblem.getQueue().equals("AS2") || SearchProblem.getQueue().equals("AS3")) {
			s = ethposition.toString() + IMFpositions.toString() + acqIMF + health.toString();			
		} else {
			s = ethposition.toString() + IMFpositions.toString() + acqIMF;
		}
		return s.hashCode();
	}

	//The .equals methods is used in the same fashion of the compareTo as so we use equals as contains method in the hashtable try to find the required states by comparing the following attributes.
	@Override
	public boolean equals(Object obj) {
		boolean Flag = false;
		IslandState islstate = ((IslandState) obj);
		if (islstate.ethposition.compareTo(this.ethposition) == 0) {
			if (islstate.acqIMF == this.acqIMF) {
				if (this.IMFpositions.size() != islstate.IMFpositions.size()) {
					return false;
				}
				for (Cell imfpos : IMFpositions) {
					Flag = false;
					for (Cell islimfpos : islstate.IMFpositions) {
						if (islimfpos.equals(imfpos))
							Flag = true;
					}
					if (!Flag)
						return false;

				}
				//in this method we added the healths array as this identifies how deep is ethan on our tree as all attributes can be the same except for this one so it's not considered a repeated state.
				if(SearchProblem.getQueue().equals("UC") || SearchProblem.getQueue().equals("AS1") || SearchProblem.getQueue().equals("AS2") || SearchProblem.getQueue().equals("AS3")) {			
					for (int i = 0; i < health.size(); i++) {
						if (health.get(i) != islstate.health.get(i)) {
							return false;
						}
					}
				}
				return true;
			}
			return false;

		}
		return false;
	}

	public void incrementdeath() {
		this.numberofdeaths++;
	}

	public int getdeaths() {
		return this.numberofdeaths;
	}
	public void setdeaths(int deaths) {
		this.numberofdeaths = deaths;
	}

	public ArrayList<cellhealthtuple> getState_healts() {
		return state_healts_pos;
	}

	public void setState_healts(ArrayList<cellhealthtuple> state_healts) {
		this.state_healts_pos = state_healts;
	}
	

}
