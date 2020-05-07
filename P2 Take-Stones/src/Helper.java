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

/**
 * @author Austin Torres
 * 
 *         Helper functions to check if numbers are prime and that get their
 *         greatest prime factor
 *
 */
public class Helper {

  /**
   * Class constructor.
   */
  private Helper() {
  }

  /**
   * This method is used to check if a number is prime or not
   * 
   * @param x A positive integer number
   * @return boolean True if x is prime; Otherwise, false
   */
  public static boolean isPrime(int x) {

    // Since 1, 2, and 3 are all prime numbers, no further check is needed
    if (x < 4)
      return true;

    for (int i = 3; i < Math.sqrt(x); i += 2) {

      // If i is a factor of x, then that means x is not prime
      if (x % i == 0)
        return false;
    }

    return true; // x is prime
  }

  /**
   * This method is used to get the largest prime factor
   * 
   * @param x A positive integer number
   * @return int The largest prime factor of x
   */
  public static int getLargestPrimeFactor(int x) {
    int i = 0;

    // Loop through stones starting at 2 since 1 has no other factors
    for (i = 2; i <= x; i++) {

      // If x is divisible by i, i is a factor of x; update x and decrement i
      if (x % i == 0) {
        x /= i;
        i--;
      }
    }
    return i; // Largest prime factor
  }

  /**
   * This method is used to count the multiples of x
   *
   * @param x is the last stone taken in the game
   * @return number of multiples for x
   */
  public static int countMultiples(int x, ArrayList<Integer> moves) {
    int multiples = 0;
    for (int i = 0; i < moves.size(); i++) {
      int current = moves.get(i);
      // If i is a multiple of x
      if (current > x && (((double) current) / x) % 1 == 0) {
        multiples++;
      }
    }
    return multiples;
  }
}
