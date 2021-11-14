package code.mission;

import code.generic.Node;

// Generic class for searching contains all methods to be implemented
public abstract class GeneralQueuing {
	Node initial_node;

	public GeneralQueuing(Node node) {
		this.initial_node = node;
		genQueue();
		enqueue(node);
	}

	public abstract void genQueue();

	public abstract Node dequeue();

	public abstract void enqueue(Node n);

	public abstract boolean isEmpty();

}
