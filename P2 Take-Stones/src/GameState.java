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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Austin Torres
 * 
 *         The GameState class defines the state of a game
 *
 */
public class GameState {
  private int size; // The number of stones
  private boolean[] stones; // Game state: true for available stones, false for
                            // taken ones
  private int lastMove; // The last move

  /**
   * Class constructor specifying the number of stones.
   */
  public GameState(int size) {

    this.size = size;

    // For convenience, we use 1-based index, and set 0 to be unavailable
    this.stones = new boolean[this.size + 1];
    this.stones[0] = false;

    // Set default state of stones to available
    for (int i = 1; i <= this.size; ++i) {
      this.stones[i] = true;
    }

    // Set the last move be -1
    this.lastMove = -1;
  }

  /**
   * Copy constructor
   */
  public GameState(GameState other) {
    this.size = other.size;
    this.stones = Arrays.copyOf(other.stones, other.stones.length);
    this.lastMove = other.lastMove;
  }


  /**
   * This method is used to compute a list of legal moves
   *
   * @return This is the list of state's moves
   */
  public List<Integer> getMoves() {

    // Create an array of valid moves
    List<Integer> validMoves = new ArrayList<Integer>();

    // If it is the first move (which is initialized to -1)
    if (lastMove == -1) {

      // For a move to be legal, the stone number must be less than stones/2
      // and be an odd number
      for (int i = 1; i < (size / 2.0); i++) {
        if (i % 2 == 1) {

          validMoves.add(i); // Add i to moves list
        }

      }

    }

    // If it is not the first move
    else {
      for (int i = 1; i <= size; i++) {

        // If stone exists and is a multiple or factor of the last move and is
        // not equal to the last move, then it is a valid stone to take
        if (stones[i]) {
          if ((((double) this.lastMove) / i) % 1 == 0
              || (i % this.lastMove == 0)) {
            validMoves.add(i);
          }
        }
      }
    }
    return validMoves;
  }


  /**
   * This method is used to generate a list of successors using the getMoves()
   * method
   *
   * @return This is the list of state's successors
   */
  public List<GameState> getSuccessors() {
    return this.getMoves().stream().map(move -> {
      var state = new GameState(this);
      state.removeStone(move);
      return state;
    }).collect(Collectors.toList());
  }


  /**
   * This method is used to evaluate a game state based on the given heuristic
   * function
   *
   * @return int This is the static score of given state
   */
  public double evaluate() {

    ///////////////////////////////////////////////////////////////////
    // If stone 1 is not taken, return value of 0 since current state is neutral
    if (stones[1]) {
      return 0;
    }
    // Number of stones taken
    int numStoneTaken = 0;

    // Value to be carried up and down tree denoting future moves
    double value = 0.0;

    // If stone is taken, increment number of stones taken
    for (int i = 1; i < size; i++) {

      if (!stones[i])
        numStoneTaken++;

    }

    // Initialize variables pertinent to continued evaluation

    int multipleCount = 0; // Track number of multiples

    int numValidMoves = getMoves().size();

    int playerTurn = (numStoneTaken % 2 == 0 ? 1 : -1);

    // Values provided in assignment instructions

    // No moves for MAX to take, so MIN wins (value of -1.0)
    if (numValidMoves == 0) {
      value = -1.0;
    }

    // If last move is 1 and number of successors is odd, return 0.5 otherwise
    // return -0.5
    else if (lastMove == 1) {
      value = (numValidMoves % 2 == 0 ? 0.5 : -0.5);
    }

    // If the last move is prime, count multiples of that prime in all possible
    // successors
    else if (Helper.isPrime(lastMove)) {

      for (Integer move : getMoves()) {

        // If a valid stone is a multiple of the last move, increment multiple
        // count
        if (move % lastMove == 0)
          multipleCount++;
      }

      // If count of multiples is odd, return 0.7 otherwise return -0.7
      value = (multipleCount % 2 == 0 ? 0.7 : -0.7);
    }

    // If last move is composite, find largest prime that divides last move and
    // count multiples
    else {

      int largestPrimeFactor = Helper.getLargestPrimeFactor(lastMove);
      for (Integer move : getMoves()) {

        if (move % largestPrimeFactor == 0) {

          multipleCount++;

        }

      }

      // If count of multiples is odd, return 0.6 otherwise return -0.6
      value = (multipleCount % 2 == 0 ? 0.6 : -0.6);
    }

    return value * playerTurn;

  }

  /**
   * This method is used to take a stone out
   *
   * @param idx Index of the taken stone
   */
  public void removeStone(int idx) {
    this.stones[idx] = false;
    this.lastMove = idx;
  }

  /**
   * These are get/set methods for a stone
   *
   * @param idx Index of the taken stone
   */
  public void setStone(int idx) {
    this.stones[idx] = true;
  }

  public boolean getStone(int idx) {
    return this.stones[idx];
  }

  /**
   * These are get/set methods for lastMove variable
   *
   * @param move Index of the taken stone
   */
  public void setLastMove(int move) {
    this.lastMove = move;
  }

  public int getLastMove() {
    return this.lastMove;
  }

  /**
   * This is get method for game size
   *
   * @return int the number of stones
   */
  public int getSize() {
    return this.size;
  }
}
