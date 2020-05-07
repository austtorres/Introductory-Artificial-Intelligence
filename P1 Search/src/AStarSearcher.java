import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

  /**
   * Calls the parent class constructor.
   * 
   * @see Searcher
   * @param maze initial maze.
   */
  public AStarSearcher(Maze maze) {
    super(maze);
  }

  /**
   * Main a-star search algorithm.
   * 
   * @return true if the search finds a solution, false otherwise.
   */
  public boolean search() {

    // explored list is a Boolean array that indicates if a state
    // associated with a given position in the maze has already been explored.
    boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

    PriorityQueue<StateFValuePair> frontier =
        new PriorityQueue<StateFValuePair>();

    // This creates a 2D array representing all of the rows and columns in the
    // maze. This enables the ability to calculate the heuristic for every
    // square in the maze using the euclidian distance formula
    // sqrt((u-p)^2 + (v-q)^2) where the current (row#, column#) position is
    // (u, v) and the goal position is (p, q)
    double[][] mazeArray = new double[maze.getNoOfRows()][maze.getNoOfCols()];
    for (int p = 0; p < mazeArray.length; p++) {
      for (int q = 0; q < mazeArray[p].length; q++) {
        Square goal = maze.getGoalSquare();
        mazeArray[p][q] = (double) Math.sqrt(
            (double) (Math.pow((goal.X - p), 2) + Math.pow((goal.Y - q), 2)));
      }
    }
    // initialize the root state and add to the frontier list
    Square player = maze.getPlayerSquare();
    State playerState = new State(player, null, 0, 0);
    StateFValuePair pSP = new StateFValuePair(playerState, 0);

    // use frontier.add(...) to add stateFValue pairs
    frontier.add(pSP);

    while (!frontier.isEmpty()) {

      // use frontier.poll() to extract the minimum stateFValuePair.
      StateFValuePair parents = frontier.poll();
      State parentState = parents.getState();
      Square parentSquare = parentState.getSquare();
      noOfNodesExpanded++;

      // maintain explored
      explored[parentSquare.X][parentSquare.Y] = true;

      ArrayList<State> succs = parentState.getSuccessors(explored, maze);
      for (State succ : succs) {
        explored[succ.getSquare().X][succ.getSquare().Y] = true;

        // use frontier.add(...) to add stateFValue pairs
        frontier.add(new StateFValuePair(succ,
            succ.getGValue() + mazeArray[succ.getX()][succ.getY()]));

        // maintain maxDepthSearched
        if (maxDepthSearched < succ.getDepth())
          maxDepthSearched = succ.getDepth();
      }
      // maintain maxSizeOfFrontier
      if (frontier.size() > maxSizeOfFrontier) {
        maxSizeOfFrontier = frontier.size();
      }
      // update the maze if a solution found
      if (parentState.isGoal(maze)) {
        cost = parentState.getGValue();
        State temp = parentState;
        while (temp != null) {
          if (maze.getMazeMatrix()[temp.getSquare().X][temp
              .getSquare().Y] == ' ')
            maze.setOneSquare(temp.getSquare(), '.');
          temp = temp.getParent();
        }
        // return true if a solution has been found
        return true;
      }
    }
    // return false if there is no solution
    return false;
  }
}
