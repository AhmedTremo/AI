package code.mission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import code.generic.Node;
import code.generic.Operators;
import code.generic.SearchProblem;
import code.generic.State;

// Our instance class that inherits the general search problem class.
// It has a map that shows the attributes of our current grid.
// we implemented a hash set to keep track of the visited states and to access them in O(1).
public class MissionImpossible extends SearchProblem {
	IslandMap map;
	ArrayList<Operators> operators;
	public int cumm_expand = 0;
	static HashSet<IslandState> uniquestates;

	// we initialize the map using the method getInstance.
	// This constructor to generate a random map.
	public MissionImpossible() {
		uniquestates = new HashSet<IslandState>();
		map = map.getinstance();
		this.addOperators();

	}

	// we set the map to null at the beginning to clear the previous map (to not use
	// the previous map in the next test).
	// This constructor is to create a map with an input grid.
	public MissionImpossible(String grid) {
		uniquestates = new HashSet<IslandState>();
		map.setInstance(null);
		map = map.getinstance(grid);
		this.addOperators();

	}

	public static String solve(String Grid, String strategy, boolean visualize) throws IOException {
		MissionImpossible ri = new MissionImpossible(Grid);
		Node Goal = ri.GeneralSearch(strategy);
		String solution = ri.printsolution(Goal, visualize);
		return solution;

	}

	private void addOperators() {
		operators = new ArrayList<Operators>();
		operators.add(new IslandMovement("up", 1));
		operators.add(new IslandMovement("down", 1));
		operators.add(new IslandMovement("left", 1));
		operators.add(new IslandMovement("right", 1));
		operators.add(new DropOperator(1));
		operators.add(new CarryOperator(1));
		Operators.initializeArray();

	}

	@Override
	// Our goal test checks if the node' state contains ethan at the submarine
	// position, and there are no more IMF members left in the map, and there are no
	// members still in truck (not dropped).
	public boolean goalTest(State state) throws IOException {
		IslandState s = (IslandState) state;

		if (s.getIMFpositions().size() == 0 && (s.getPosition().equals(s.getSubmarine())) && s.getAcqIMF() == 0) {

			return true;
		}
		return false;
	}

	@Override
	// We expand nodes and check they are not repeated.
	public ArrayList<Node> expand(Node node) {
		ArrayList<Node> expandedNodes = new ArrayList<Node>();
		for (Operators op : this.operators) {

			Node expanded_node = op.execute(node);
			if (expanded_node != null) {
				if (!uniquestates.contains((IslandState) expanded_node.getNodeState())) {
					uniquestates.add((IslandState) expanded_node.getNodeState());
					expandedNodes.add(expanded_node);
				}
			}
		}
		node.setOptionsToTake(expandedNodes);
		cumm_expand += expandedNodes.size();

		return expandedNodes;

	}

	@Override
	// Create an initial state the includes information extracted from the created
	// map
	public State getInitialState() {
		IslandState initial_state = new IslandState(map.ethPosition, 0, map.getIMFpositions(), map.truckMax,
				map.getSubmarinePosition(), map.health);
		ArrayList<cellhealthtuple> pos_health = new ArrayList<cellhealthtuple>();
		for (int i = 0; i < map.getIMFpositions().size(); i++) {
			Cell pos = map.getIMFpositions().get(i);
			int health = map.gethealth().get(i);
			cellhealthtuple newcp = new cellhealthtuple(pos, health);
			pos_health.add(newcp);
		}
		initial_state.setState_healts(pos_health);
		return initial_state;
	}

	public static HashSet<IslandState> getRepeated() {
		return uniquestates;
	}

	// A method to create the output string and to print the map if visualize is set
	// to true.
	public String printsolution(Node Goal, boolean visualize) {
		String solution = "";
		ArrayList<Node> needed_nodes = new ArrayList<Node>();
		needed_nodes.add(Goal);
		Node pre_node = null;
		if (Goal != null)
			pre_node = Goal.getParentNode();
		while (true) {
			if (pre_node != null) {
				needed_nodes.add(pre_node);
				pre_node = pre_node.getParentNode();
			} else {
				break;
			}
		}
		for (int i = needed_nodes.size() - 1; i >= 0; i--) {
			IslandState currentState = null;
			if (needed_nodes.get(i) != null) {
				currentState = (IslandState) needed_nodes.get(i).getNodeState();
			}

			if (visualize)
				System.out.println(currentState.toString());
			for (Node option : needed_nodes.get(i).getOptionsToTake()) {
				if (visualize)
					System.out.print("  options : " + option.getOperator().getName());
			}
			if (needed_nodes.get(i).getOperator() != null) {
				if (visualize)
					System.out.println("operator = " + needed_nodes.get(i).getOperator().getName());
				solution += needed_nodes.get(i).getOperator().getName();
				if (i != 0)
					solution += ",";
			}

			IslandState myState = (IslandState) needed_nodes.get(i).getNodeState();
			if (visualize) {
				System.out.println("");
				System.out.println("cost : " + needed_nodes.get(i).getPathCost());
				System.out.println("number deaths = " + ((IslandState) needed_nodes.get(i).getNodeState()).getdeaths());
			}
			int submarinePosition = myState.getSubmarine().getX() + myState.getSubmarine().getY();
			int myPosition = myState.getPosition().getX() + myState.getPosition().getY();
			int xsqr = myState.getPosition().getX() - myState.getSubmarine().getX();
			int ysqr = myState.getPosition().getY() - myState.getSubmarine().getY();

			if (visualize)
				System.out.println("First Heuristic Cost: " + Math.sqrt(Math.abs((xsqr * xsqr) + (ysqr * ysqr))));

			char[][] grid = new char[IslandMap.getinstance().getGridWidth()][IslandMap.getinstance().getGridHeight()];
			for (int i1 = 0; i1 < grid.length; i1++) {
				Arrays.fill(grid[i1], '.');
			}
			for (Cell pos : currentState.getIMFpositions()) {
				grid[pos.getX()][pos.getY()] = 'I';
			}
			grid[currentState.getSubmarine().getX()][currentState.getSubmarine().getY()] = 'S';
			grid[currentState.getPosition().getX()][currentState.getPosition().getY()] = 'E';
			if (visualize) {
				for (char[] row : grid) {
					for (char c : row) {
						System.out.printf("%1$-10s", c);
					}
					System.out.println();
				}
			}
		}

		solution += ";";

		solution += ((IslandState) Goal.getNodeState()).getdeaths();
		solution += ";";

		for (int m = 0; m < ((IslandState) Goal.getNodeState()).getState_healts().size(); m++) {
			solution += 100 - ((IslandState) Goal.getNodeState()).getState_healts().get(m).getImf_health();
			if (m != ((IslandState) Goal.getNodeState()).getState_healts().size() - 1)
				solution += ",";
		}
		solution += ";";

		solution += this.cumm_expand;
		return solution;

	}

}
