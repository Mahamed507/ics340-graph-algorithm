# ics340-graph-algorithm

# ICS 340 — Graph Algorithms (Prog340)

A Java program built across 5 deliverables for ICS 340 at Metropolitan State University. Each deliverable implements a different graph algorithm, all reading from the same adjacency matrix input format.

## How to Run

```bash
javac Prog340.java
java Prog340
```

Then select a test file and a deliverable from the GUI.

## Deliverables

| Deliverable | Algorithm |
|-------------|-----------|
| A | Graph validation and adjacency matrix parsing |
| B | Optimal Binary Search Tree (dynamic programming) |
| C | A* Search (shortest path with heuristic) |
| D | Strongly Connected Components (Kosaraju's Algorithm) |
| E | Maximum Flow (Edmonds-Karp Algorithm) |

## Input Format

All input files use an adjacency matrix format:

```
~          val   C   E   R   S   V   W
Calgary      ~   ~   4  14   ~   ~   ~
Edmonton     ~   ~   ~   ~  12   ~   ~
Vancouver    S  13  16   ~   ~   ~   ~
Winnipeg     T   ~   ~   ~   ~   ~   ~
```

- `~` means no edge
- `val` column: `S` = source, `T` = sink, `~` = regular node
- Edge weights represent capacity (Deliverable E) or distance (other deliverables)

## Deliverable E — Maximum Flow (Edmonds-Karp)

Finds the maximum flow from source node `S` to sink node `T` using BFS to find augmenting paths. Among shortest paths, picks the one with the greatest flow. Ties are broken alphabetically by path name.

**Example output:**
```
Residual graph:
...
Path VESW, augmented flow = 12, total flow = 12.  Residual graph:
...
No more augmenting flows.
```

## Project Structure

```
├── Prog340.java       # Main entry point and GUI
├── Graph.java         # Graph data structure
├── Node.java          # Node with name, abbreviation, val
├── Edge.java          # Weighted directed edge
├── Path.java          # Path helper (used in DelivC)
├── DelivA.java
├── DelivB.java
├── DelivC.java
├── DelivD.java
├── DelivE.java
└── files/             # Test input files
```

## Course Info

Metropolitan State University — ICS 340 (Algorithms)
