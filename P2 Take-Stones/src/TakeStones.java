// Project Title: HW2 P3 Take-Stones Game Playing
//
// Description: My own data structure with no public or package level fields
// which is to be tested by the TestDS_My class. Creating this helps practice
// testing without knowing the implementation and helps identify things to test
// for.
//
// Files: AlphaBetaPruning.java, GameState.java, TakeStones.java, Helper.java
// Course: CS540 Fall 2020
//
// Author: Austin Torres
// Email: artorres3@wisc.edu
// Lecturer's Name: Chuck Dyer
// Lecture: 001
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

/**
 * @author Austin Torres
 * 
 *         This contains the main function that parses the command line inputs
 *         and runs the search algorithm
 *
 */
public class TakeStones {

  /**
   * This is the main method.
   * 
   * @param args A sequence of integer numbers, including the number of stones,
   *             the number of taken stones, a list of taken stone and search
   *             depth
   * @exception IndexOutOfBoundsException On input error.
   */
  public static void main(String[] args) {
    try {
      // Read input from command line
      int n = Integer.parseInt(args[0]); // the number of stones
      int nTaken = Integer.parseInt(args[1]); // the number of taken stones

      // Initialize the game state
      GameState state = new GameState(n); // game state
      int stone;
      for (int i = 0; i < nTaken; i++) {
        stone = Integer.parseInt(args[i + 2]);
        state.removeStone(stone);
      }

      int depth = Integer.parseInt(args[nTaken + 2]); // search depth
      // Process for depth being -1
      if (0 == depth)
        depth = n + 1;

      // Get next move
      var searcher = new AlphaBetaPruning();
      searcher.run(state, depth);

      // Print search stats
      searcher.printStats();

    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }
}
