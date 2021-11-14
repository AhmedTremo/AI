package code.mission;

import java.util.ArrayList;
import java.util.Collections;

import code.generic.Node;
import code.generic.Operators;

public class DropOperator extends Operators {

	public DropOperator(int cost) {
		super("drop", cost);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node execute(Node node) {
		boolean dead = false;
		IslandState Current_state = (IslandState) node.getNodeState();
		ArrayList<Integer> healths = (ArrayList<Integer>) Current_state.getHealth().clone();
		// decrementing the health of the IMF members before applying the Drop operation
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
		Cell position = Current_state.getPosition();
		// checking if ethan's position is now at the submarine position to be apply to
		// apply the drop function
		if (!(position.equals(Current_state.getSubmarine())) || Current_state.getAcqIMF() == 0) {

			return null;
		}
		// defining the new sate with the new position , IMF members health , and reset
		// the acquired IMFs
		IslandState new_state = new IslandState(Current_state.getPosition(), 0, Current_state.getIMFpositions(),
				Current_state.getMaxCAP(), Current_state.getSubmarine(), healths);
		new_state.setState_healts(Current_state.getState_healts());
		// updating the constant array that saves all the IMF members(dead and still
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
		new_state.setdeaths(Current_state.getdeaths());
		// increment the number of deaths based on who died while this action is taken
		if (dead)
			for (int count = 0; count < counter; count++)
				new_state.incrementdeath();
		// set the cost of applying this operator
		this.setCost(getCost(node, healths, new_state.getdeaths()));
		// creating new node
		Node new_node = new Node(new_state, node, this);
		new_node.setType(node.getType());
		boolean repeated = false;
		if (!repeated)
			return new_node;

		else

			return null;
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
			return delta_death + delta_damage + 15;
		}
		return 0;

	}

}
