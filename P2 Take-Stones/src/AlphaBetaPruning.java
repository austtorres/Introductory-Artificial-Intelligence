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
import java.util.List;

/**
 * @author Austin Torres
 * 
 *         This is where the Alpha-Beta pruning algorithm is implemented
 *
 */
public class AlphaBetaPruning {

  private int dLim; // Depth limit
  private int move; // Possible moves
  private int numVisited; // Number of nodes visited
  private int numEvaluated; // Number of nodes evaluated
  private int maxDepth; // Maximum depth of search

  private double value;
  private double bFactor; // Branching factor

  private ArrayList<Double> moveVal = new ArrayList<Double>();
  private ArrayList<Integer> bFactors = new ArrayList<Integer>();

  public AlphaBetaPruning() {
  }

  /**
   * This function will print out the information to the terminal, as specified
   * in the homework description.
   */
  public void printStats() {

    System.out.println("Move: " + this.move);

    // Formatted to one decimal place
    System.out.println("Value: " + String.format("%.1f", this.value));
    System.out.println("Number of Nodes Visited: " + this.numVisited);
    System.out.println("Number of Nodes Evaluated: " + this.numEvaluated);
    System.out.println("Max Depth Reached: " + this.maxDepth);

    // Do not use println to prevent an extra line from causing grading problems
    System.out.print("Avg Effective Branching Factor: "
        + String.format("%.1f", this.bFactor));

  }

  /**
   * This function will start the alpha-beta search
   * 
   * @param state This is the current game state
   * @param depth This is the specified search depth
   */
  public void run(GameState state, int depth) {

    dLim = depth; // Set depth limit using input
    int numStoneTaken = 0;
    for (int i = 1; i < state.getSize(); i++) {
      if (!state.getStone(i))
        numStoneTaken++;
    }

    // Recursively call alphabeta to continue search
    value = alphabeta(state, depth, Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY, (numStoneTaken % 2 == 0));
    int idx = moveVal.indexOf(value);

    // Maintain possible moves
    move = state.getMoves().get(idx);
    for (Integer i : bFactors) {
      bFactor += i;
    }

    // Update branching factor keeping it a double
    bFactor /= (bFactors.size() * 1.0);
  }

  /**
   * This method is used to implement alpha-beta pruning for both 2 players
   * 
   * @param state     This is the current game state
   * @param depth     Current depth of search
   * @param alpha     Current Alpha value
   * @param beta      Current Beta value
   * @param maxPlayer True if player is Max Player; Otherwise, false
   * @return int This is the number indicating score of the best next move
   */
  private double alphabeta(GameState state, int depth, double alpha,
      double beta, boolean maxPlayer) {

    // Maintain number of nodes visited and max depth reached

    maxDepth = Math.max(maxDepth, dLim - depth);
    numVisited++;
    List<GameState> successors = state.getSuccessors();
    double value;
    int numBranches = 0;

    // Written by following the lecture slides from Professor Dyer (they make
    // much more sense than the book


    // If a terminal state is reached
    if (depth == 0 || successors.size() == 0) {
      numEvaluated++;
      value = state.evaluate();
      if (depth == dLim - 1)
        moveVal.add(value);
      return value;
    }

    // MAX
    if (maxPlayer) {

      // Initialized to negative infinity
      value = Double.NEGATIVE_INFINITY;
      for (GameState successor : successors) {
        numBranches++;

        // Depth must be depth - 1 to prevent looping through same depth
        value = Math.max(value, alphabeta(successor, depth - 1, alpha, beta, false));
        if (value >= beta) {
          break;
        }

        // Update alpha for max
        alpha = Math.max(alpha, value);
      }
    }

    // MIN
    else {
      value = Double.POSITIVE_INFINITY;
      for (GameState successor : successors) {
        numBranches++;
        value = Math.min(value, alphabeta(successor, depth - 1, alpha, beta, true));
        if (value <= alpha) {
          break;
        }

        // Update beta for min
        beta = Math.min(value, beta);
      }
    }
    bFactors.add(numBranches);
    if (depth == dLim - 1) {
      moveVal.add(value);
    }
    return value;
  } // Steps described in detail in professor's lecture slides


  private double maxValue(GameState state, int depth, double alpha, double beta,
      boolean maxPlayer) {
    numVisited++;
    this.maxDepth = Math.max(depth, this.maxDepth);


    List<Integer> validMoves = state.getMoves();
    if (validMoves.isEmpty() || depth == dLim) {
      numEvaluated++;
      return state.evaluate();
    }
    List<GameState> validStates = state.getSuccessors();
    double v = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < validStates.size(); i++) {
      v = Math.max(v,
          minValue(validStates.get(i), depth + 1, alpha, beta, false));
      if (v >= beta) { // Prune remaining children
        return v;
      }
      alpha = Math.max(alpha, v);
    }
    return v; // Return value of best child
  }

  /**
   * Finds the value produced when looking ahead at what MAX will choose
   * 
   * @param state
   * @param depth
   * @param alpha
   * @param beta
   * @param maxPlayer
   * @return
   */
  private double minValue(GameState state, int depth, double alpha, double beta,
      boolean maxPlayer) {
    numVisited++;
    this.maxDepth = Math.max(depth, this.maxDepth);
    List<Integer> validMoves = state.getMoves();
    System.out.println(validMoves);
    if (validMoves.isEmpty() || depth == dLim) {
      numEvaluated++;
      return state.evaluate();
    }
    List<GameState> validStates = state.getSuccessors();
    double v = Double.POSITIVE_INFINITY;
    for (int i = 0; i < validStates.size(); i++) {
      v = Math.min(v,
          maxValue(validStates.get(i), depth + 1, alpha, beta, true));
      if (v <= alpha) {
        return v;
      }
      beta = Math.min(beta, v);
    }
    return v;
  }
}
