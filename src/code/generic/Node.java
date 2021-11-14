package code.generic;

import java.util.ArrayList;
import java.util.Comparator;

import code.mission.Cell;
import code.mission.IslandMap;
import code.mission.IslandMovement;
import code.mission.IslandState;
import code.mission.cellhealthtuple;

public class Node implements Comparable<Node> {
	State nodeState; // the state of this node
	Node parentNode;
	Operators operator;
	int depth = 0;
	int pathCost = 0;
	ArrayList<Node> optionsToTake;// the options that was valid for this node 
	String type = "";

	public Node(State nodeState, Node parentNode, Operators operator) {
		this.nodeState = nodeState;
		this.parentNode = parentNode;
		this.operator = operator;
		
		if (parentNode != null) {
			this.depth = parentNode.depth + 1;
			this.pathCost = parentNode.pathCost + operator.cost;
			optionsToTake = new ArrayList<Node>();
		}
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public State getNodeState() {
		return nodeState;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public Operators getOperator() {
		return operator;
	}

	public int getDepth() {
		return depth;
	}

	public int getPathCost() {
		return pathCost;
	}

	public String toString() {
		return "parentNode :" + this.parentNode.toString() + "State :" + this.nodeState.toString() + "Operator :"
				+ this.operator.toString() + "Pathcost :" + this.pathCost + "Depth :" + this.depth;

	}

	@Override

	public int compareTo(Node o) {
		// Heuristic one comparing two nodes based on which one has smaller  euclidean to the submarine 
		// Heuristic one
		int diff = 0;
		IslandState myState = ((IslandState) this.getNodeState());
		IslandState otherState = ((IslandState) o.getNodeState());

		int my_xsqr = myState.getPosition().getX() - myState.getSubmarine().getX();
		int my_ysqr = myState.getPosition().getY() - myState.getSubmarine().getY();

		int other_xsqr = otherState.getPosition().getX() - otherState.getSubmarine().getX();
		int other_ysqr = otherState.getPosition().getY() - otherState.getSubmarine().getY();

		double my_eqdist = Math.sqrt(Math.abs((my_xsqr * my_xsqr) + (my_ysqr * my_ysqr)));
		double other_eqdist = Math.sqrt(Math.abs((other_xsqr * other_xsqr) + (other_ysqr * other_ysqr)));
		double d = my_eqdist - other_eqdist;
        // Heuristic two is preferring the node that will lead to less deaths
		// Heuristic two
		int delta_death1 = ((IslandState) this.getNodeState()).getdeaths();
		int delta_death2 = ((IslandState) o.getNodeState()).getdeaths();
		int delta_death = (delta_death1 - delta_death2) * 100
				* ((((IslandState) (this.getNodeState())).getHealth().size()) + delta_death1);
        // Heuristic three prefer the node that will lead to less damage and deaths. 
		// Heuristic three
		int minSoFar = 101;
		int myxdist = 0;
		int myydist = 0;
		int damagef = 0;
		for (int i =0; i<myState.getHealth().size();i++) {
			int xDif = Math.abs(myState.getPosition().getX() - myState.getIMFpositions().get(i).getX());
			int yDif = Math.abs(myState.getPosition().getY() - myState.getIMFpositions().get(i).getY());
			int total_damage = (xDif + yDif) * 2;
			if (myState.getHealth().get(i) > total_damage + 2) {
				if (myState.getHealth().get(i)  < minSoFar) {
					minSoFar = myState.getHealth().get(i) ;
					myxdist = xDif;
					myydist = yDif;
				}
			}
			else {
				damagef+=100;
			}

		}
		damagef += myState.getdeaths()*100;

		int minSoFarOther = 101;
		int otherxdist = 0;
		int otherydist = 0;
		int damagef1 =0;
		for (int i = 0;i< otherState.getHealth().size();i++) {
			int xDif = Math.abs(otherState.getPosition().getX() - otherState.getIMFpositions().get(i).getX());
			int yDif = Math.abs(otherState.getPosition().getY() - otherState.getIMFpositions().get(i).getY());
			int total_damage = (xDif + yDif) * 2;
			if (otherState.getHealth().get(i) > total_damage + 2) {
				if (otherState.getHealth().get(i)< minSoFarOther) {
					minSoFarOther = otherState.getHealth().get(i);
					otherxdist = xDif;
					otherydist = yDif;
				}

			}
			else {
				damagef1+=100;
			}
		}

		damagef1 += myState.getdeaths()*100;
		if (type.equals("UC")) {
			diff = this.getPathCost() - o.getPathCost();
		} else if (type.equals("GR1")) {

			if (d > 0) {
				return 1;
			} else {
				if (d == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		} else if (type.equals("AS1")) {
			double diffe = this.getPathCost() - o.getPathCost() + d;
			if (diffe > 0) {
				return 1;
			} else {
				if (diffe == 0) {
					return 0;
				} else {
					return -1;
				}
			}

		} else if (type.equals("GR2")) {
			return delta_death;
		} else if (type.equals("AS2")) {
			double diff2 = this.getPathCost() - o.getPathCost() + delta_death;
			if (diff2 > 0) {
				return 1;
			} else {
				if (diff2 == 0) {
					return 0;
				} else {
					return -1;
				}
			}

		} else {
			if (type.equals("GR3")) {
				if (myState.getIMFpositions().size() != 0 && (myState.getAcqIMF() < myState.getMaxCAP())) {
					double myeq = Math.sqrt((myxdist) * (myxdist) + (myydist) * (myydist)) + damagef;
					double othereq = Math.sqrt((otherxdist) * (otherxdist) + (otherydist) * (otherydist)) + damagef1;
					if (myeq > othereq) {
						return 1;
					} else if (myeq == othereq) {
						return 0;
					} else if (myeq < othereq) {
						return -1;
					}
				} else {

					if (d > 0) {
						return 1;
					} else {
						if (d == 0) {
							return 0;
						} else {
							return -1;
						}
					}

				}
			} else {
				if (type.equals("AS3")) {
					if (myState.getIMFpositions().size() != 0 && (myState.getAcqIMF() < myState.getMaxCAP())) {
						double myeq = Math.sqrt((myxdist) * (myxdist) + (myydist) * (myydist)) + damagef;
						double othereq = Math.sqrt((otherxdist) * (otherxdist) + (otherydist) * (otherydist)) + damagef1;
						double difference = myeq - othereq + this.getPathCost() - o.getPathCost();
						if (difference > 0) {
							return 1;
						} else if (difference == 0) {
							return 0;
						} else if (difference < 0) {
							return -1;
						}
					} else {

						double diffe = this.getPathCost() - o.getPathCost() + d;
						if (diffe > 0) {
							return 1;
						} else {
							if (diffe == 0) {
								return 0;
							} else {
								return -1;
							}
						}

					}

				}
			}
		}
		return diff;
	}

	public ArrayList<Node> getOptionsToTake() {
		return optionsToTake;
	}

	public void setOptionsToTake(ArrayList<Node> optionsToTake) {
		this.optionsToTake = optionsToTake;
	}

}
