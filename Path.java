import java.util.ArrayList;

public class Path implements Comparable<Path> {

    private ArrayList<Node> cities;  // cities visited so far
    private double d;  // distance so far
    private double h;  // heuristic of last city to goal
    private double f;  // f =  d + h

   // create a constructor
    public Path(Node start, double heuristic) {
        cities = new ArrayList<Node>();
        cities.add(start);
        d = 0;
        h = heuristic;
        f = d + h;
    }


    public Path(Path previous, Node next, double newDist, double newH) {
        cities = new ArrayList<Node>(previous.cities); // copy existing path
        cities.add(next);
        d = newDist;
        h = newH;
        f = d + h;
    }

    public Node getLastNode() {
        return cities.get(cities.size() - 1);
    }

    public double getD() { return d; }
    public double getH() { return h; }
    public double getF() { return f; }

    public boolean contains(Node n) {
        return cities.contains(n);
    }

    // Priority queue uses this to sort by f-value (lowest first)

    public int compareTo(Path other) {
        return Double.compare(this.f, other.f);
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cities.size(); i++) {
            if (i > 0) sb.append("-");
            sb.append(cities.get(i).getAbbrev());
        }
        return sb.toString();
    }
}
