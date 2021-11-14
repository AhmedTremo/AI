package code.mission;

import java.util.ArrayList;
import java.util.Collections;

import code.generic.Node;
import code.generic.Operators;

public class IslandMovement extends Operators {
	int x = 0;
	int y = 0;

	// defining the up,down,left right movement effect on the position
	public IslandMovement(String name, int cost) {

		super(name, cost);
		if (name.equals("up")) {
			y = -1;

		} else if (name.equals("down")) {
			y = 1;

		} else if (name.equals("left"))
			x = -1;
		else if (name.equals("right"))
			x = 1;

	}

	@Override
	public Node execute(Node node) {
		boolean dead = false;
		IslandState current_state = (IslandState) node.getNodeState();
		Cell new_position = new Cell(current_state.getPosition().getX() + x, current_state.getPosition().getY() + y);
		IslandMap map = IslandMap.getinstance();
		// checking the validity of this movement if out of the grid
		if (!new_position.isValid(map.gridWidth, map.gridHeight)) {
			return null;
		}
		// decrementing the health of the imf members before applying the movement
		// operation
		ArrayList<Integer> healths = (ArrayList<Integer>) current_state.getHealth().clone();
		int counter = 0;
		for (int i = 0; i < healths.size(); i++) {
			if (healths.get(i) > 1) {
				int h = (healths.remove(i)) - 2;
				if (h == 0) {
					dead = true;
					counter++;
				}
				healths.add(i, h);
			} else if (healths.get(i) == 1) {
				int h = (healths.remove(i)) - 1;
				if (h == 0) {
					dead = true;
					counter++;
				}
				healths.add(i, h);

			}

		}
		// defining the new sate with the new position , imf members health , eth
		// position , and acquired imfs
		IslandState new_state = new IslandState(new_position, current_state.acqIMF, current_state.getIMFpositions(),
				current_state.getMaxCAP(), current_state.getSubmarine(), healths);

		new_state.setState_healts(current_state.getState_healts());
		// updating the constant array that saves all the imf members(dead and still
		// alive) positions and health
		ArrayList<cellhealthtuple> new_state_healts = new ArrayList<cellhealthtuple>();
		for (int k = 0; k < new_state.getState_healts().size(); k++) {
			cellhealthtuple oldhealth_pos = new_state.getState_healts().get(k);
			int h_i = oldhealth_pos.getImf_health();
			Cell p_i = oldhealth_pos.getImf_position();
			if (h_i > 1 && new_state.getIMFpositions().contains(p_i)) {
				h_i -= 2;
			} else if (h_i == 1 && new_state.getIMFpositions().contains(p_i)) {
				h_i -= 1;
			}
			new_state_healts.add(new cellhealthtuple(p_i, h_i));
		}
		new_state.setState_healts(new_state_healts);
		new_state.setdeaths(current_state.getdeaths());
		// set the number of deaths after this movement
		if (dead)
			for (int count = 0; count < counter; count++)
				new_state.incrementdeath();
		// setting the cost of applying the movement operator to reach this state
		int cost = getCost(node, healths, new_state.getdeaths());
		this.setCost(cost);
		// creating new node
		Node new_node = new Node(new_state, node, this);
		new_node.setType(node.getType());
		boolean repeated = false;
		if (!repeated)
			return new_node;
		else {

			return null;
		}

	}
	// setting the cost of the operator as the delta between the deaths and the
	// damage between the new node and the current node.

	public int getCost(Node node, ArrayList<Integer> healths, int deaths) {
		IslandMap map = IslandMap.getinstance();
		if (node != null) {
			int delta_death1 = deaths;
			int delta_death2 = ((IslandState) node.getNodeState()).getdeaths();
			int delta_death = (delta_death1 - delta_death2) * 100
					* ((((IslandState) (node.getNodeState())).health.size()) + delta_death1);
			int delta_damage = 0;
			for (int i = 0; i < healths.size(); i++) {
				delta_damage += (100 - healths.get(i)) - (100 - ((IslandState) node.getNodeState()).health.get(i));
			}
			return delta_death + delta_damage + 16;
		}
		return 0;

	}

}
