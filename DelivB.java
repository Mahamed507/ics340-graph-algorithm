import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


// Class DelivB does the work for deliverable DelivB of the Prog340


//*            Note to self
//        What is a Optimal Binary Search Tree? ->
//         A regular BST is Left < Root < Right, But what makes it OPTIMAL is based on the Frequency.
//        ex:
//        Key        Freq
//        A           5
//        B           100
//        C           10
//
//            Since B is searched 100 times, we want the B near the root, also the goal is to have less searching.*/

public class DelivB {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;


	// Nodes in the order they appear in the header (Prog340 already created them that way)
	ArrayList<Node> nodes;

	// DP tables
	int[][] cost;
	int[][] root;
	
	public DelivB( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		
		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring( 0, inputFileName.length()-4 ); // Strip off ".txt"
		String outputFileName = baseFileName.concat( "_out.txt" );
		outputFile = new File( outputFileName );
		if ( outputFile.exists() ) {    // For retests
			outputFile.delete();
		}
		
		try {
			output = new PrintWriter(outputFile);			
		}
		catch (Exception x ) { 
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		//System.out.println( "DelivB:  To be implemented");
		//output.println( "DelivB:  To be implemented");


		nodes = g.getNodeList();

		// call the method that removes every edge from the graph
		clearAllEdges();

		// if there is 1 node atleast, compute the OBST and add edges for it.
		if (nodes.size() > 0) {
			buildOBSTandEdges();
		}

		// Print to console + to output file
		printTable(new PrintWriter(System.out, true));
		printTable(output);

		output.flush();
		output.close();
	}

	// Remove edges from graph + from each node’s lists
	private void clearAllEdges() {
		g.getEdgeList().clear();
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).getOutgoingEdges().clear();
			nodes.get(i).getIncomingEdges().clear();
		}
	}

	private void buildOBSTandEdges() {
		int n = nodes.size();

		// weights from "val" column
		int[] w = new int[n];
		for (int i = 0; i < n; i++) {
			w[i] = Integer.parseInt(nodes.get(i).getVal());
		}

		// prefix sums so sum(i..j) is easy:
		// prefix[k] = w[0] + ... + w[k-1]
		int[] prefix = new int[n + 1];
		prefix[0] = 0;
		for (int i = 0; i < n; i++) {
			prefix[i + 1] = prefix[i] + w[i];
		}

		cost = new int[n][n];
		root = new int[n][n];

		// base case: one key
		for (int i = 0; i < n; i++) {
			cost[i][i] = w[i];
			root[i][i] = i;
		}

		// length from 2 to n
		for (int len = 2; len <= n; len++) {
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;

				int bestCost = Integer.MAX_VALUE;
				int bestRoot = i;

				// sum of weights i..j
				int sumIJ = prefix[j + 1] - prefix[i];

				// try each r as root
				for (int r = i; r <= j; r++) {

					int leftCost = 0;
					int rightCost = 0;

					if (r > i) leftCost = cost[i][r - 1];
					if (r < j) rightCost = cost[r + 1][j];

					int total = leftCost + rightCost + sumIJ;

					if (total < bestCost) {
						bestCost = total;
						bestRoot = r;
					}
				}

				cost[i][j] = bestCost;
				root[i][j] = bestRoot;
			}
		}

		// Now build edges using root[][] (simple recursion)
		addEdgesFromRoot(0, n - 1, -1);
	}

	// Build edges for subtree i..j.
	// parentIndex = -1 means this subtree root is the overall root (no parent edge).
	private void addEdgesFromRoot(int i, int j, int parentIndex) {
		if (i > j) return;

		int r = root[i][j];

		if (parentIndex != -1) {
			Node parent = nodes.get(parentIndex);
			Node child = nodes.get(r);

			Edge e = new Edge(parent, child, 1);
			g.addEdge(e);
			parent.addOutgoingEdge(e);
			child.addIncomingEdge(e);
		}

		// left subtree
		addEdgesFromRoot(i, r - 1, r);

		// right subtree
		addEdgesFromRoot(r + 1, j, r);
	}

	// Print table like the examples: header + each row + ~ where no edge
	private void printTable(PrintWriter out) {
		int n = nodes.size();

		// Build adjacency matrix
		int[][] adj = new int[n][n];
		for (int k = 0; k < g.getEdgeList().size(); k++) {
			Edge e = g.getEdgeList().get(k);

			int from = nodes.indexOf(e.getTail());
			int to = nodes.indexOf(e.getHead());

			if (from >= 0 && to >= 0) {
				adj[from][to] = e.getDist();
			}
		}

		// Header row
		out.printf("%-10s %-4s", "~", "val");
		for (int c = 0; c < n; c++) {
			out.printf(" %-4s", nodes.get(c).getAbbrev());
		}
		out.println();

		// Each node row
		for (int r = 0; r < n; r++) {
			Node rowNode = nodes.get(r);
			out.printf("%-10s %-4s", rowNode.getName(), rowNode.getVal());

			for (int c = 0; c < n; c++) {
				if (adj[r][c] == 0) out.printf(" %-4s", "~");
				else out.printf(" %-4d", adj[r][c]);
			}
			out.println();
		}









	}
}

