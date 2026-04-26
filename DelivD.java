import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.LinkedHashMap;

// Class DelivD does the work for deliverable DelivD of the Prog340

public class DelivD {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	ArrayList<Node> listOfNodes;
	ArrayList<Edge> listOfEdges;
	int time;
	HashMap<Node, Integer> disc;
	HashMap<Node, Integer> finish;
	HashMap<Node, String> color;
	HashMap<Edge, String> edgeType;




	public DelivD(File in, Graph gr) {
		inputFile = in;
		g = gr;

		listOfNodes = g.getNodeList();
		listOfEdges = g.getEdgeList();


		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring(0, inputFileName.length() - 4); // Strip off ".txt"
		String outputFileName = baseFileName.concat("_out.txt");
		outputFile = new File(outputFileName);
		if (outputFile.exists()) {    // For retests
			outputFile.delete();
		}

		try {
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		/**System.out.println( "DelivD:  To be implemented");
		 output.println( "DelivD:  To be implemented");
		 output.flush();**/

		dfs();
		printResults();
		output.flush();
		output.close();

	}

	// create a method called DFS
	public void dfs() {
		// use a data strcuture "HashMap"
		// inizialize disc map  - will store discovery time for each node
		disc = new HashMap<>();
		// finish map - will store finish time for each node
		finish = new HashMap<>();
		// color map - will store WHITE/GRAY/BLACK status for each node
		color = new HashMap<>();
		//  edgeType map - will store Tree/Back/Forward/Cross for each edge
		edgeType = new HashMap<>();
		time = 0;

// set every Node to white, which means it was unvisited before we begin.
		for (Node n : listOfNodes) {
			color.put(n, "WHITE");
		}

		// find the start node which equals to "s"
		Node startNode = null;
		for (Node n : listOfNodes) {
			if (n.getVal().equals("S")) {
				startNode = n;
				break;
			}
		}

// only call the method dfsVisit if we actaully found a start node
		if (startNode != null) {
			dfsVisit(startNode); // begin DFS from the start node
		}

		// after DFS from start Node,  some nodes may still be unvisted
		//sort remaining nodes in order alphabetically by name
		//***** used AI for this part of the line of code ****
		listOfNodes.sort(Comparator.comparing(Node::getName));

		for (Node n : listOfNodes) {
			// only visit nodes that are still White
			if (color.get(n).equals("WHITE")) {
				dfsVisit(n);  // start a new DFS tree from this unvisited node
			}
		}
	}

	public void dfsVisit(Node u) {
		// mark node as gray meaing we discoverd it
		color.put(u, "GRAY");
		time++;
		// record the discovery time for this node
		disc.put(u, time);

		//  make a copy of outgoing edges so we don't modify the original list on the node
		ArrayList<Edge> edges = new ArrayList<>(u.getOutgoingEdges());
		// sort edges by distance ascending so we visit nearest neighbor first
		// if two edges have the same distance, sort alphabetically by neighbor name
		// AI used on this part******
		edges.sort((e1, e2) -> {
			int distCmp = Integer.compare(e1.getDist(), e2.getDist());
			if (distCmp != 0) return distCmp;
			return e1.getHead().getName().compareTo(e2.getHead().getName());
		});


		// loop through every outgoing edge from this node
		for (Edge e : edges) {
			Node v = e.getHead();  // v is the neighbor at the end of this edge
			String vColor = color.get(v);  // check what color v currently is

			if (vColor.equals("WHITE")) {
				// v is unvisited - this edge is a Tree edge because we use it to discover v
				edgeType.put(e, "Tree");
				dfsVisit(v);
			} else if (vColor.equals("GRAY")) {
				edgeType.put(e, "Back");
			} else {
				// v is BLACK - Forward or Cross
				if (disc.get(u) < disc.get(v)) {
					edgeType.put(e, "Forward");
				} else {
					edgeType.put(e, "Cross");
				}
			}
		}

		// mark as fully done
		color.put(u, "BLACK");
		time++;
		finish.put(u, time);
	}

	public void printResults() {
		// header for the node table
		String nodeHeader = "DFS of graph:\n";
		System.out.println(nodeHeader);
		output.println(nodeHeader);

		String nodeColHeader = String.format("%-10s %-8s %-8s", "Node", "Disc", "Finish");
		System.out.println(nodeColHeader);
		output.println(nodeColHeader);
// print one row per node showing its name, discovery time, and finish time
		for (Node n : listOfNodes) {
			String line = String.format("%-10s %-8d %-8d",
					n.getName(), disc.get(n), finish.get(n));
			System.out.println(line);
			output.println(line);
		}


		System.out.println();
		output.println();

		// print edge classification table
		String edgeHeader = "Edge Classification:\n";
		System.out.println(edgeHeader);
		output.println(edgeHeader);

		String edgeColHeader = String.format("%-15s %-10s", "Edge", "Type");
		System.out.println(edgeColHeader);
		output.println(edgeColHeader);
// print one row per edge showing tail->head and its classification
		for (Edge e : listOfEdges) {
			String label = String.format("%s->%s",
					e.getTail().getName(), e.getHead().getName());
			String type = edgeType.getOrDefault(e, "N/A");
			String line = String.format("%-15s %-10s", label, type);
			System.out.println(line);
			output.println(line);
		}


	}

}










