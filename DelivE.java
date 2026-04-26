import java.io.File;
import java.io.PrintWriter;
import java.util.*;

// Class DelivE does the work for deliverable DelivE of the Prog340


//*
// Use the Edmonds-Karp algorithm to compute the maximum flow from the source node(with value "S" to
// the sink node with value "T".
//
// Chose the one that augments the flow the most, in case of a tie, choose the path whose name(as a list of Nodes) comes first in the alphabet.*//

public class DelivE {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;


	int[][] residual;
	ArrayList<Node> nodes;
	int n;
	int sourceIdx;
	int sinkIdx;
	
	public DelivE( File in, Graph gr ) {
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
		//System.out.println( "DelivE:  To be implemented");
		//output.println( "DelivE:  To be implemented");
		//output.flush();

		runEdmondsKarp();
		output.flush();
	}
	void runEdmondsKarp() {
		nodes = g.getNodeList();
		n = nodes.size();

		// Find source and sink
		sourceIdx = -1;
		sinkIdx = -1;
		for (int i = 0; i < n; i++) {
			String val = nodes.get(i).getVal();
			if (val != null && val.equals("S")) sourceIdx = i;
			if (val != null && val.equals("T")) sinkIdx = i;
		}

		// Build residual graph
		residual = new int[n][n];
		for (Edge e : g.getEdgeList()) {
			int from = nodes.indexOf(e.getTail());
			int to = nodes.indexOf(e.getHead());
			residual[from][to] = e.getDist();
		}

		// Print initial residual graph
		output.println("Residual graph:");
		System.out.println("Residual graph:");
		printResidual();

		int totalFlow = 0;

		while (true) {
			List<Integer> path = bfs();
			if (path == null) break;

			// Find bottleneck
			int flow = Integer.MAX_VALUE;
			for (int i = 0; i < path.size() - 1; i++) {
				int from = path.get(i);
				int to = path.get(i + 1);
				if (residual[from][to] < flow) {
					flow = residual[from][to];
				}
			}

			// Update residual
			for (int i = 0; i < path.size() - 1; i++) {
				int from = path.get(i);
				int to = path.get(i + 1);
				residual[from][to] -= flow;
				residual[to][from] += flow;
			}

			totalFlow += flow;

			// Build path string without StringBuilder
			String pathStr = "";
			for (int idx : path) {
				pathStr = pathStr + nodes.get(idx).getAbbrev();
			}

			output.println("Path " + pathStr + ", augmented flow = " + flow + ", total flow = " + totalFlow + ".  Residual graph:");
			System.out.println("Path " + pathStr + ", augmented flow = " + flow + ", total flow = " + totalFlow + ".  Residual graph:");
			printResidual();
		}

		output.println("No more augmenting flows.");
		System.out.println("No more augmenting flows.");
	}

	// BFS finds all shortest paths, picks best flow, then alphabetical
	List<Integer> bfs() {
		Queue<List<Integer>> queue = new LinkedList<>();

		List<Integer> startPath = new ArrayList<>();
		startPath.add(sourceIdx);
		queue.add(startPath);

		List<List<Integer>> shortestPaths = new ArrayList<>();
		int shortestLength = -1;

		while (!queue.isEmpty()) {
			List<Integer> currentPath = queue.poll();
			int last = currentPath.get(currentPath.size() - 1);

			// Stop if this path is longer than shortest found
			if (shortestLength != -1 && currentPath.size() > shortestLength) break;

			// If we reached the sink, record it
			if (last == sinkIdx) {
				shortestLength = currentPath.size();
				shortestPaths.add(currentPath);
				continue;
			}

			// Get neighbors sorted alphabetically
			List<Integer> neighbors = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				if (residual[last][i] > 0 && !currentPath.contains(i)) {
					neighbors.add(i);
				}
			}

			// Simple alphabetical sort using selection sort
			for (int i = 0; i < neighbors.size() - 1; i++) {
				for (int j = i + 1; j < neighbors.size(); j++) {
					String abbrevI = nodes.get(neighbors.get(i)).getAbbrev();
					String abbrevJ = nodes.get(neighbors.get(j)).getAbbrev();
					if (abbrevI.compareTo(abbrevJ) > 0) {
						int temp = neighbors.get(i);
						neighbors.set(i, neighbors.get(j));
						neighbors.set(j, temp);
					}
				}
			}

			// Add new paths to queue
			for (int neighbor : neighbors) {
				List<Integer> newPath = new ArrayList<>(currentPath);
				newPath.add(neighbor);
				queue.add(newPath);
			}
		}

		if (shortestPaths.isEmpty()) return null;

		// Pick path with most flow, ties broken alphabetically
		List<Integer> bestPath = null;
		int bestFlow = -1;
		String bestPathStr = "";

		for (List<Integer> path : shortestPaths) {
			// Compute bottleneck
			int flow = Integer.MAX_VALUE;
			for (int i = 0; i < path.size() - 1; i++) {
				int from = path.get(i);
				int to = path.get(i + 1);
				if (residual[from][to] < flow) {
					flow = residual[from][to];
				}
			}

			// Build path string
			String pathStr = "";
			for (int idx : path) {
				pathStr = pathStr + nodes.get(idx).getAbbrev();
			}

			if (bestPath == null) {
				bestPath = path;
				bestFlow = flow;
				bestPathStr = pathStr;
			} else if (flow > bestFlow) {
				bestPath = path;
				bestFlow = flow;
				bestPathStr = pathStr;
			} else if (flow == bestFlow && pathStr.compareTo(bestPathStr) < 0) {
				bestPath = path;
				bestPathStr = pathStr;
			}
		}

		return bestPath;
	}

	void printResidual() {
		// Print header
		String header = String.format("%-15s %-5s", "~", "val");
		for (Node node : nodes) {
			header = header + String.format("%-5s", node.getAbbrev());
		}
		output.println(header);
		System.out.println(header);

		// Print each row
		for (int i = 0; i < n; i++) {
			Node node = nodes.get(i);
			String val = node.getVal() != null ? node.getVal() : "~";
			String row = String.format("%-15s %-5s", node.getName(), val);

			for (int j = 0; j < n; j++) {
				boolean hasEdge = hasOriginalEdge(i, j) || hasOriginalEdge(j, i);
				if (hasEdge) {
					row = row + String.format("%-5s", residual[i][j]);
				} else {
					row = row + String.format("%-5s", "~");
				}
			}
			output.println(row);
			System.out.println(row);
		}
		output.println();
		System.out.println();
	}

	boolean hasOriginalEdge(int from, int to) {
		for (Edge e : g.getEdgeList()) {
			if (nodes.indexOf(e.getTail()) == from && nodes.indexOf(e.getHead()) == to) {
				return true;
			}
		}
		return false;
	}



}


