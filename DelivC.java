import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;

// Class DelivC does the work for deliverable DelivC of the Prog340


/*
* The goal is to use the A* search algorithm, which finds the shortest path to the goal.
*
* */

public class DelivC {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	Graph gh;

	public DelivC(File in, Graph gr, Graph heuristic) {
		inputFile = in;
		g = gr;
		gh = heuristic;


		// Get output file name.
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

		aStarSearch();

	}

	// call search algo


	/**
	 * System.out.println( "DelivC:  To be implemented");
	 * output.println( "DelivC:  To be implemented");
	 * output.flush();
	 **/


	// crate  a method
	public void aStarSearch() {
  // get citiy from the graph
		ArrayList<Node> nodes = g.getNodeList();

		// Step 1: Find start and goal nodes, they are empty placeholder
		Node startNode = null;
		Node goalNode = null;

		// loops through until it finds the value "S" , and "G".

		for (Node n : nodes) {
			if ("S".equals(n.getVal())) {
				startNode = n;
			}
			if ("G".equals(n.getVal())) {
				goalNode = n;
			}
		}

		// if you can't find it then just print it. Can't run A* search Algo
		if (startNode == null || goalNode == null) {
			System.out.println("No start or goal node found!");
			return;
		}

		//
		double startH = getHeuristic(startNode, goalNode);

		// prints in format
		String header = String.format("%-30s %6s %6s %8s", "PATH", "DIST", "HEUR", "F-VALUE");
		System.out.println(header);
		output.println(header);

		// create a priority Queue, which in defualt it's the lowest first(like a min-heap)
		PriorityQueue<Path> queue = new PriorityQueue<>();
		queue.add(new Path(startNode, startH));

		// create a map, using K=String , V=Double to keep track, and set the start  to 0.0
		HashMap<String, Double> bestDist = new HashMap<>();
		bestDist.put(startNode.getName(), 0.0);

		//checks if the queue is empty
		while (!queue.isEmpty()) {

			// Dequeue path with lowest f-value ** Ai used***
			Path current = queue.poll();

			// Get the last node in current path
			Node lastNode = current.getLastNode();

			// Check if we reached the goal
			if (lastNode.equals(goalNode)) {
				// Goal line - only path and distance
				String goalLine = String.format("%-30s %6.0f",
						current.toString(), current.getD());
				System.out.println(goalLine);
				output.println(goalLine);
				output.flush();
				return;
			}

			// Print current path
			String line = String.format("%-30s %6.0f %6.0f %8.0f",
					current.toString(),
					current.getD(),
					current.getH(),
					current.getF());
			System.out.println(line);
			output.println(line);

			// Expand neighbors
			for (Edge e : lastNode.getOutgoingEdges()) {
				Node neighbor = e.getHead();
				double newDist = current.getD() + e.getDist();
				double newH = getHeuristic(neighbor, goalNode);

				// Only add if shorter path to this neighbor
				if (!bestDist.containsKey(neighbor.getName()) ||
						newDist < bestDist.get(neighbor.getName())) {
					bestDist.put(neighbor.getName(), newDist);
					queue.add(new Path(current, neighbor, newDist, newH));
				}
			}
		}

		System.out.println("No path found!");
		output.flush();
	}

	public double getHeuristic(Node from, Node to) {
		ArrayList<Node> hNodes = gh.getNodeList();
		for (Node n : hNodes) {
			if (n.getName().equals(from.getName())) {
				for (Edge e : n.getOutgoingEdges()) {
					if (e.getHead().getName().equals(to.getName())) {
						return e.getDist();
					}
				}
			}
		}
		return 0;
	}







	}



















