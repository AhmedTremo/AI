package code.mission;

import java.util.Stack;

import code.generic.Node;

// Iterative deepening is implemented using a Queue
// When dequeuing if the queue becomes empty, we increase the depth by one, and clear the repeated states hash set, then we enqueue the rootNode.
public class IterativeDeepining extends GeneralQueuing {
	Stack<Node> queue;
	Node parentNode;
	public int current_depth;

	public IterativeDeepining(Node node) {
		super(node);
		current_depth = 0;
		parentNode = node;
		genQueue();
		enqueue(node);

	}

	@Override
	public void genQueue() {
		queue = new Stack();

	}

	@Override
	public Node dequeue() {
		Node poped;
		if (queue.size() <= 0) {
			current_depth++;
			MissionImpossible.getRepeated().clear();
			poped = parentNode;
		} else {
			poped = queue.pop();
		}

		return poped;

	}

	@Override
	public void enqueue(Node n) {
		if (n != null) {
			queue.push(n);
		}

	}

	@Override
	public boolean isEmpty() {

		return false;
	}

}
