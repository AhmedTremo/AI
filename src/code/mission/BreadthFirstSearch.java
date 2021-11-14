package code.mission;

import java.util.ArrayDeque;


import code.generic.Node;


// BFS is implemented using a Queue (FIFO)
public class BreadthFirstSearch extends GeneralQueuing {

	 ArrayDeque<Node> queue;
		public BreadthFirstSearch(Node node) {
			super(node);
			genQueue();
			enqueue(node);
		
		}

		@Override
		public void genQueue() {
			queue = new ArrayDeque<Node>();
			
		}

		@Override
		public Node dequeue() {
			if (isEmpty()) return null;
			Node poped = queue.removeFirst();
			//System.out.println("dequeue");
			return poped;
			
			
		}

		@Override
		public void enqueue(Node n) {
			if(n !=null) {
				queue.addLast(n);
			}
			
		}

		@Override
		public boolean isEmpty() {
		
		    return queue.isEmpty();
		}


}
