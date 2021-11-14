package code.mission;

import java.util.Queue;
import java.util.Stack;

import code.generic.Node;

// DFS is implemented using a stack (LIFO)
public class DepthFirstSearch extends GeneralQueuing{
    Stack<Node>queue;
	public DepthFirstSearch(Node node) {
		super(node);
		genQueue();
		enqueue(node);
	
	}

	@Override
	public void genQueue() {
		queue = new Stack();
		
	}

	@Override
	public Node dequeue() {
		if (isEmpty()) return null;
		Node poped = queue.pop();
		
		return poped;
		
		
	}

	@Override
	public void enqueue(Node n) {
		if(n !=null) {
			queue.push(n);
		}
		
	}
	
	

	@Override
	public boolean isEmpty() {
	
	    return queue.isEmpty();
	}



	
    

}
