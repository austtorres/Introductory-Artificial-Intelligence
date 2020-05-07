import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

  /**
   * Calls the parent class constructor.
   * 
   * @see Searcher
   * @param maze initial maze.
   */
  public BreadthFirstSearcher(Maze maze) {
    super(maze);
  }

  /**
   * Main breadth first search algorithm.
   * 
   * @return true if the search finds a solution, false otherwise.
   */
  public boolean search() {

    // explored list is a 2D Boolean array that indicates if a state associated
    // with a given position in the maze has already been explored.
    boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

    // Queue implementing the Frontier list
    LinkedList<State> queue = new LinkedList<State>();

    // Add starting state before loop in the top left corner of the maze
    State start = new State(this.maze.getPlayerSquare(), null, 0, 0);

    // In case search starts with goal
    while (start.isGoal(maze)) {
      return true;
    }
    // Add 1 to nodes expanded (Starting state)
    queue.add(start);


    while (!queue.isEmpty()) {

      // maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
      // maxDepthSearched, maxSizeOfFrontier during
      // the search
      // update the maze if a solution found

      // use queue.pop() to pop the queue.
      // use queue.add(...) to add elements to queue
      // Pop off of queue and add to explored

      // Expand (make sure not in frontier twice) then do goal test

      // Identify the current state by popping the next state off of the queue
      State current = queue.pop();
      cost = current.getGValue(); // cost of current state in search

      // Update maxDepthSearched if the current depth is > maxDepthSearched
      maxDepthSearched = Math.max(maxDepthSearched, current.getDepth());

      // Declare that state has been explored
      explored[current.getX()][current.getY()] = true;

      // Maintain noOfNodesExpanded
      noOfNodesExpanded++;

      if (current.isGoal(maze)) {

        cost = current.getGValue();

        maxDepthSearched = Math.max(maxDepthSearched, current.getDepth());

        current = current.getParent();
        while (!current.getSquare().equals(maze.getPlayerSquare())) {
          // uses '.' character to signify path
          maze.setOneSquare(current.getSquare(), '.');
          current = current.getParent();
        }
        return true; // return true if a solution is found
      }
      // Loop through successors (not in explored)
      for (State successor : current.getSuccessors(explored, maze)) {
        if (!explored[successor.getX()][successor.getY()]) {

          // Add successor to frontier and declare it explored, updating
          // frontier size
          queue.add(successor);
          explored[successor.getX()][successor.getY()] = true;
          if (queue.size() > maxSizeOfFrontier) {
            maxSizeOfFrontier = queue.size();
          }
        }
      }
    }
    return false;
  }
}
