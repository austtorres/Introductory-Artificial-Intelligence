////////////////////////////////////////////////////////////////////////////////
// Main File: DigitClassifier.java
// This File: Node.java
// Other Files: NNImpl.java, DigitClassifier.java, NodeWeightPair.java
// Semester: CS 540 Fall 2019
//
// Author: Austin Torres
// Email: artorres3@wisc.edu
// CS Login: atorres
//
/////////////////////////// OTHER SOURCES OF HELP //////////////////////////////
// fully acknowledge and credit all sources of help,
// other than Instructors and TAs.
//
// Persons: Identify persons by name, relationship to you, and email.
// Describe in detail the the ideas and help they provided.
//
// Online sources: avoid web searches to solve your problems, but if you do
// search, be sure to include Web URLs and description of
// of any information you find.
//////////////////////////// 80 columns wide ///////////////////////////////////

import java.util.*;

/**
 * Class for internal organization of a Neural Network. There are 5 types of
 * nodes. Check the type attribute of the node for details. Feel free to modify
 * the provided function signatures to fit your own implementation
 */

/**
 * @author Austin Torres
 *
 */
class Node {
  private int type = 0; // 0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
  // Array List that will contain the parents (including the bias node) with
  // weights if applicable
  ArrayList<NodeWeightPair> parents = null;
  private double inputValue = 0.0;
  private double outputValue = 0.0;
  private double delta = 0.0; // input gradient

  // Create a node with a specific type
  Node(int type) {
    if (type > 4 || type < 0) {
      System.out.println("Incorrect value for node type");
      System.exit(1);

    } else {
      this.type = type;
    }

    if (type == 2 || type == 4) {
      parents = new ArrayList<>();
    }
  }

  // For an input node sets the input value which will be the value of a
  // particular attribute
  void setInput(double inputValue) {
    if (type != 1 && type != 3) {
      this.inputValue = inputValue;
    }
  }

  /**
   * Calculate the output of a node. You can get this value by using getOutput()
   */
  void calculateOutput(ArrayList<Node> outputNodes) {

    // ReLU hidden nodes
    if (this.type == 2) {

      this.outputValue = Math.max(0, this.inputValue);

    }

    // SoftMax output nodes
    else if (this.type == 4) {

      this.outputValue = this.softMax(outputNodes);

    }
  }


  /**
   * Get output value of node
   * 
   * @return output value
   */
  double getOutput() {

    // Input node values do not change
    if (this.type == 0) {

      return this.inputValue;

      // Bias node's value is always 1
    } else if (this.type == 1 || this.type == 3) {

      return 1.00;

    } else {

      return this.outputValue;

    }

  }

  /**
   * Calculate delta value in order to maintain weights
   * 
   * @param targetValue
   * @param outputNodes
   * @param nodeIndex
   */
  void calculateDelta(double targetValue, ArrayList<Node> outputNodes,
      int nodeIndex) {
    if (this.type == 2 || this.type == 4) {
      double delta;
      if (this.type == 2) {
        delta =
            this.getGPrimeRelu() * getWeightedOutput(outputNodes, nodeIndex);
      } else {
        delta = targetValue - this.outputValue;
      }
      this.delta = delta;
    }
  }

  /**
   * Get delta value in order to update weight
   * 
   * @return delta
   */
  private double getDelta() {
    if (this.type == 2 || this.type == 4) {
      return delta;
    }

    return 0;
  }

  /**
   * get output that has been weighted
   * 
   * @param outputNodes to be weighted
   * @param nodeIndex   node
   * @return new value
   */
  private double getWeightedOutput(ArrayList<Node> outputNodes, int nodeIndex) {
    double value = 0;
    for (Node node : outputNodes) {
      value += node.parents.get(nodeIndex).weight * node.getDelta();
    }
    return value;
  }



  /**
   * Update weight using delta
   * 
   * @param learningRate
   */
  void updateWeight(double learningRate) {

    if (this.type == 2 || this.type == 4) {

      for (NodeWeightPair parentPair : this.parents) {

        double deltaW = learningRate * parentPair.node.getOutput() * delta;

        parentPair.weight += deltaW;
      }
    }
  }


  private double getGPrimeRelu() {
    if (this.type == 2) {

      if (this.inputValue <= 0) {

        return 0;

      } else {
        return 1;
      }
    }
    return -1;

  }

  /**
   * Perform softmax function
   * 
   * @param outputNodes to be manipulated
   * @return result
   */
  private double softMax(ArrayList<Node> outputNodes) {
    double sum = 0;
    double z = Math.exp(this.inputValue);
    for (Node node : outputNodes) {
      sum += Math.exp(node.inputValue);
    }
    return z / sum;
  }


  /**
   * Calculate weighted input value
   */
  void getWeightedInputValue() {
    if (this.type == 2 || this.type == 4) {
      double input = 0;
      for (NodeWeightPair pair : parents) {
        input += pair.node.getOutput() * pair.weight;
      }
      this.inputValue = input;
    }
  }
}
