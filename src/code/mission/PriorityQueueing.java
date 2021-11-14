package code.mission;

import java.util.PriorityQueue;
import java.util.Stack;

import code.generic.Node;

public class PriorityQueueing extends GeneralQueuing {
	// A general class for Uniform cost, Greedy, AStar
	// Implemented using a priority queue, The priority of enqueue is assigned in
	// the "Node" class "compareTo" method
	PriorityQueue<Node> queue;

	public PriorityQueueing(Node node) {
		super(node);
		genQueue();
		enqueue(node);

	}

	@Override
	public void genQueue() {
		queue = new PriorityQueue<Node>();

	}

	@Override
	public Node dequeue() {
		if (isEmpty())
			return null;
		Node poped = queue.remove();

		return poped;

	}

	@Override
	public void enqueue(Node n) {

		if (n != null) {
			queue.add(n);
		}

	}

	@Override
	public boolean isEmpty() {

		return queue.isEmpty();
	}

}
