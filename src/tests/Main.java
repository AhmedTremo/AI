package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import code.mission.MissionImpossible;
import code.mission.*;
import code.generic.Node;
import code.generic.SearchProblem;
import code.generic.State;

public class Main {
	static String grid5 = "5,5;2,1;1,0;1,3,4,2,4,1,3,1;54,31,39,98;2";
	static String grid6 = "6,6;1,1;3,3;3,5,0,1,2,4,4,3,1,5;4,43,94,40,92;3";
	static String grid7 = "7,7;1,6;5,4;2,2,1,4,0,3,2,3,0,1,4,5;6,44,82,49,24,54;4";
	static String grid8 = "8,8;4,2;7,4;5,1,7,7,4,0,6,7;93,85,72,78;1";
	static String grid9 = "9,9;8,7;5,0;0,8,2,6,5,6,1,7,5,5,8,3,2,2,2,5,0,7;11,13,75,50,56,44,26,77,18;2";
	static String grid10 = "10,10;6,3;4,8;9,1,2,4,4,0,3,9,6,4,3,4,0,5,1,6,1,9;97,49,25,17,94,3,96,35,98;3";
	static String grid11 = "11,11;7,7;8,8;9,7,7,4,7,6,9,6,9,5,9,1,4,5,3,10,5,10;14,3,96,89,61,22,17,70,83;5";
	static String grid12 = "12,12;7,7;10,6;0,4,2,2,1,3,8,2,4,2,9,3;95,4,68,2,94,91;5";
	static String grid13 = "13,13;7,4;4,0;9,3,3,9,12,7,7,9,3,12,11,8,4,2,12,6;22,62,74,56,43,70,17,14;4";
	static String grid14 = "14,14;13,9;1,13;5,3,9,7,11,10,8,3,10,7,13,6,11,1,5,2;76,30,2,49,63,43,72,1;6";
	static String grid15 = "15,15;5,10;14,14;0,0,0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8;81,13,40,38,52,63,66,36,13;1";
	static String test_grid = "5,5;1,2;4,0;0,3,2,1,3,0,3,2,3,4,4,3;20,30,90,80,70,60;3";

	public static void main(String[] args) throws IOException {
        MissionImpossible ms = new MissionImpossible();
		//Node node =ms.GeneralSearch("AS2");
		//System.out.println(ms.printsolution(node, true));
        String solution = ms.solve(grid9, "ID", false);
        System.out.println(solution);
	}



}