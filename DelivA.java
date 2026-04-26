import java.io.*;
import java.util.*;
import java.util.ArrayList;



public class DelivA {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;

	public DelivA(File in, Graph gr) {
		inputFile = in;
		g = gr;

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


		System.out.println( "DelivA:  To be implemented");
		 output.println( "DelivA:  To be implemented");
		 output.flush();
// first step for this is to run through the val
// check if the graph is empty
		if (g == null || g.getNodeList() == null || g.getNodeList().isEmpty()) {
			String msg = "The graph is empty";
			System.out.println(msg);


		}
		// Not empty, so get nodes
		ArrayList<Node> nodes = g.getNodeList();
		int n = nodes.size();

		// create a new array called reorder of the ordered val and add it

		Node[] reorder = new Node[nodes.size() + 1 ];


   // iterate through the arrayList
		for(Node node : nodes){
     // counter used to be in here
			// int counter = 0;
          // convert into int
			int val = Integer.parseInt(node.getVal());
            // if the val < 1 that is invalid , or if the val > size.
			if(val < 1 || val > n){
				System.out.println("wrong value" + val + "for the Node" + node.getAbbrev());
			}
			// add it into my new array.
			reorder[val] = node;



		}


		 ArrayList<Edge> e = g.getEdgeList();




		int total = 0;

		for(int i = 1; i <= n ; i++){
			System.out.print(i + "->"+ reorder[i].getAbbrev() + " " );


		}

		System.out.println(reorder[1].getAbbrev());


		for(int i = 1 ; i <= n; i++){

			Node from = reorder[i];
			Node to;


			if(i == n){
				to = reorder[1];
			}else{
				to = reorder[i + 1];

			}

			int dist = 0;


			for(Edge edge : from.getOutgoingEdges()){
				if(edge.getHead() == to){
					dist = edge.getDist();
					break;
				}
			}


			total = total + dist;


		}
		System.out.println("Total Dist = "+ total);

































	}

}















