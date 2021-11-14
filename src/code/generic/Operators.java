package code.generic;

import java.util.ArrayList;

import code.mission.IslandMap;

public abstract class Operators {
String name;
int cost;
//An arrayList that corresponds to the IMFmembers generated that will indicate which member is carried and which is not>
protected static ArrayList<Boolean> carried ;
public Operators(String name , int cost) {
	this.name = name ;
	this.cost =cost;
	
	
}

//An abstract method that has to be defined in the children classed to define how to execute the operator on a node in other classes.
public abstract Node execute(Node node);

public String getName() {
	return name;
}
public int getCost() {
	return cost;
}
public void setCost(int cost) {
	this.cost=cost;
}

public String toString() {
	return "operator Name :" +this.name + "Cost :" +this.cost;
}

//A method that was used to eliminate repeated states from ancestors but was proven not that useful.
//public static boolean repeatedstates(Node node) {
//	ArrayList<Node> needed_nodes = new ArrayList<Node>();
//	Node pre_node =node.getParentNode();
//	while(true) {
//		if(pre_node!=null) {
//			needed_nodes.add(pre_node);
//			pre_node=pre_node.getParentNode();
//		}
//		else {
//			break;
//		}
//	}
//	for(Node pn:needed_nodes) {
//		if(pn.getNodeState().compareTo(node.getNodeState())==0)
//			return true;
//	}
//	return false;
//
//	
//}

//A method to initialize the array that all IMFmembers are initially on the grid and not carried by Ethan.
public static void initializeArray() {
	carried = new ArrayList<Boolean>();
	for(int i =0;i<IslandMap.getinstance().gethealth().size();i++) {
		carried.add(false);
	}
	
}


}

