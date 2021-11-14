package code.mission;

import java.util.ArrayList;
import java.util.Collections;

import code.generic.Node;
import code.generic.Operators;

public class CarryOperator extends Operators {

	public CarryOperator(int cost) {
		super("carry", cost);

		// TODO Auto-generated constructor stub
	}

	@Override
	public Node execute(Node node) {
		boolean found = false;
		boolean increment = false;
		IslandState Current_state = (IslandState) node.getNodeState();
		// checking if there is still enough room in the car for another IMF
		if (Current_state.getAcqIMF() < Current_state.getMaxCAP()) {
			ArrayList<Integer> healths;

			healths = (ArrayList<Integer>) Current_state.getHealth().clone();
			ArrayList<Boolean> deads = new ArrayList<Boolean>();
			// saving the dead IMFs index for incrementing the number of dead IMFs
			for (int i = 0; i < healths.size(); i++) {
				deads.add(false);
			}
			// decrementing the health of the IMF members before applying the Carry
			// operation
			for (int i = 0; i < healths.size(); i++) {
				if (healths.get(i) > 1) {
					int h = (healths.remove(i)) - 2;
					healths.add(i, h);
					if (h == 0)
						deads.set(i, true);
				} else if (healths.get(i) == 1) {
					int h = (healths.remove(i)) - 1;
					healths.add(i, h);
					if (h == 0)
						deads.set(i, true);
				}
			}
			// checking if Ethan is at the position of an IMF member so can apply carry
			// operation
			Cell position = Current_state.getPosition();
			ArrayList<Cell> IMF_poses = (ArrayList<Cell>) Current_state.getIMFpositions().clone();
			int removed_imf_index = -1;
			for (int i = 0; i < IMF_poses.size(); i++) {
				if (position.equals(IMF_poses.get(i)) && Current_state.getAcqIMF() < Current_state.getMaxCAP()) {
					found = true;
					IMF_poses.remove(IMF_poses.get(i));
					healths.remove(i);
					removed_imf_index = i;
				}
			}
			// defining the new sate with the new position , IMF members health , set the
			// acquired IMFs
			IslandState new_state = new IslandState(Current_state.getPosition(), Current_state.getAcqIMF() + 1,
					IMF_poses, Current_state.getMaxCAP(), Current_state.getSubmarine(), healths);

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
			// calculate all the deaths
			for (int dp = 0; dp < Collections.frequency(deads, true); dp++) {
				new_state.incrementdeath();
			}

			// setting the operator cost
			this.setCost(getCost(node, healths, removed_imf_index, new_state.getdeaths()));
			Node new_node = new Node(new_state, node, this);
			new_node.setType(node.getType());
			boolean repeated = false;
			// checking if there was an IMF in this position
			if (found && !repeated) {

				return new_node;
			}

			else {

				return null;
			}

		} else {

			return null;
		}
	}

	// setting the cost of the operator as the delta between the deaths and the
	// damage between the new node and the current node.
	// here we are avoiding the change in the array size.
	public int getCost(Node node, ArrayList<Integer> healths, int removed_imf_index, int deaths) {

		IslandMap map = IslandMap.getinstance();
		boolean flag = false;

		if (node != null) {
			int delta_death1 = deaths;
			int delta_death2 = ((IslandState) node.getNodeState()).getdeaths();
			int delta_death = (delta_death1 - delta_death2) * 100
					* ((((IslandState) (node.getNodeState())).health.size()) + delta_death1);
			int delta_damage = 0;
			for (int i = 0; i < healths.size(); i++) {
				// System.out.println("the removed IMF : " + removed_imf_index);
				if ((removed_imf_index == -1 || i != removed_imf_index) && flag == false)

					delta_damage += (100 - healths.get(i)) - (100 - ((IslandState) node.getNodeState()).health.get(i));

				else {

					delta_damage += (100 - healths.get(i))
							- (100 - ((IslandState) node.getNodeState()).health.get(i + 1));
					flag = true;
				}
			}
			return delta_death + delta_damage + 15;
		}
		return 0;

	}

}
