package code.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import code.generic.Node;
import code.generic.Operators;
import code.mission.BreadthFirstSearch;
import code.mission.Cell;
import code.mission.DepthFirstSearch;
import code.mission.GeneralQueuing;
import code.mission.IslandMap;
import code.mission.IslandState;
import code.mission.IterativeDeepining;
import code.mission.MissionImpossible;
import code.mission.PriorityQueueing;

// Abstract General SearchProblem to be inherited
public abstract class SearchProblem {
	ArrayList<Operators> operators;
	public static String queue;
	public ArrayList<Operators> getOperators() {
		return operators;
	}

	public void setOperators(ArrayList<Operators> operators) {
		this.operators = operators;
	}

	public abstract boolean goalTest(State state) throws IOException;

	// a method to get the cost of a node
	public int pathCost(Node node, Operators operator) {
		if (node.getParentNode() == null)
			return 0;
		return node.pathCost + operator.cost;
	}

	// return an arraylist of the nodes that are resulted from applying the
	// operators on a specific node.
	public abstract ArrayList<Node> expand(Node node);

	public abstract State getInitialState();

	// General search to solve search problems that takes string strategy name.
	// In every search we initialize the initial state and call its implemented
	// class.
	// For Uniform cost, Greedy, AStar the all have the same class
	// "PriorityQueueing".
	// The difference is the Node attribute (type) that is passed to the compareTo
	// method to decide how we insert into the priority queue.
	// We implemented three admissible heuristics.
	// We check if the dequeued node is a goal node
	// If not we expand and enqueue, (we add a condition for IterativeDeeping that
	// the node depth is not bigger than the current maximum depth).

	public Node GeneralSearch(String queue) throws IOException {
		GeneralQueuing queueing = null;
		this.queue = queue;
		if (queue.equals("DF")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			queueing = new DepthFirstSearch(initial_node);

		} else if (queue.equals("BF")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			queueing = new BreadthFirstSearch(initial_node);
		} else if (queue.equals("ID")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			queueing = new IterativeDeepining(initial_node);
		} else if (queue.equals("UC")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			initial_node.setType("UC");
			queueing = new PriorityQueueing(initial_node);
		} else if (queue.equals("GR1")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			initial_node.setType("GR1");
			queueing = new PriorityQueueing(initial_node);
		} else if (queue.equals("GR2")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			initial_node.setType("GR2");
			queueing = new PriorityQueueing(initial_node);
		} else if (queue.equals("AS1")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			initial_node.setType("AS1");
			queueing = new PriorityQueueing(initial_node);
		} else if (queue.equals("AS2")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			initial_node.setType("AS2");
			queueing = new PriorityQueueing(initial_node);
		} else if (queue.equals("GR3")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			initial_node.setType("GR3");
			queueing = new PriorityQueueing(initial_node);
		} else if (queue.equals("AS3")) {
			IslandState initial_state = (IslandState) this.getInitialState();
			Node initial_node = new Node(initial_state, null, null);
			initial_node.setType("AS3");
			queueing = new PriorityQueueing(initial_node);
		}
		if (queueing.isEmpty()) {
			// System.out.println("the queue is empty");
			return null;
		}

		while (!queueing.isEmpty()) {
			Node node = queueing.dequeue();
			if (goalTest(node.getNodeState())) {
				// System.out.println("Goal Has Been Found");
				// System.out.println("depth of the last node = " + node.getDepth());
				return node;
			}
			ArrayList<Node> expanded = expand(node);

			// System.out.println("Number of expnaded nodes now: "+ expanded.size());

			for (Node exNode : expanded) {
				if (queue.equals("ID")) {
					if (exNode.getDepth() <= ((IterativeDeepining) queueing).current_depth) {
						queueing.enqueue(exNode);
					}
				} else {
					queueing.enqueue(exNode);
				}
			}
		}
		System.out.println("No goal is found");
		return null;

	}

	public static String getQueue() {
		String s = queue;
		return s;
	}

}
