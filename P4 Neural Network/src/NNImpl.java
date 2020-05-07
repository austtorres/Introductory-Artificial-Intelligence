////////////////////////////////////////////////////////////////////////////////
// Main File: DigitClassifier.java
// This File: NNImpl.java
// Other Files: DigitClassifier.java, Node.java, NodeWeightPair.java
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
 * @author Austin Torres
 *
 */
class NNImpl {
  private double learningRate; // learning rate
  private int maxEpoch; // maximum number of epochs
  private Random random; // shuffles the training set
  private ArrayList<Instance> trainingSet; // the training set
  private ArrayList<Node> inputNodes; // list of input nodes.
  private ArrayList<Node> hiddenNodes; // list of hidden nodes
  private ArrayList<Node> outputNodes; // list of output nodes


  /**
   * This constructor creates the nodes necessary for the neural network Also
   * connects the nodes of different layers After calling the constructor the
   * last node of both inputNodes and hiddenNodes will be bias nodes.
   */
  NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount,
      Double learningRate, int maxEpoch, Random random,
      Double[][] hiddenWeights, Double[][] outputWeights) {
    this.trainingSet = trainingSet;
    this.learningRate = learningRate;
    this.maxEpoch = maxEpoch;
    this.random = random;

    // input layer nodes
    this.inputNodes = new ArrayList<>();
    int inputNodeCount = trainingSet.get(0).attributes.size();
    int outputNodeCount = trainingSet.get(0).classValues.size();
    for (int i = 0; i < inputNodeCount; i++) {
      Node node = new Node(0);
      this.inputNodes.add(node);
    }

    // bias node from input layer to hidden
    Node biasToHidden = new Node(1);
    this.inputNodes.add(biasToHidden);

    // hidden layer nodes
    this.hiddenNodes = new ArrayList<>();
    for (int i = 0; i < hiddenNodeCount; i++) {
      Node node = new Node(2);
      // Connecting hidden layer nodes with input layer nodes
      for (int j = 0; j < this.inputNodes.size(); j++) {
        NodeWeightPair nwp =
            new NodeWeightPair(this.inputNodes.get(j), hiddenWeights[i][j]);
        node.parents.add(nwp);
      }
      this.hiddenNodes.add(node);
    }

    // bias node between hidden layer and output
    Node biasToOutput = new Node(3);
    this.hiddenNodes.add(biasToOutput);

    // Output layer
    this.outputNodes = new ArrayList<>();

    for (int i = 0; i < outputNodeCount; i++) {

      Node node = new Node(4);

      // Connecting output layer nodes with hidden layer nodes
      for (int j = 0; j < this.hiddenNodes.size(); j++) {
        NodeWeightPair nwp =
            new NodeWeightPair(this.hiddenNodes.get(j), outputWeights[i][j]);
        node.parents.add(nwp);
      }
      this.outputNodes.add(node);
    }
  }



  /**
   * trains the neural network using a training set, fixed learning rate, and
   * number of epochs (provided as input to the program). This function also
   * prints the total Cross-Entropy loss on all the training examples after each
   * epoch.
   */
  void train() {
    for (int j = 0; j < this.maxEpoch; j++) {
      double totalLoss = 0;
      Collections.shuffle(this.trainingSet, this.random);

      for (Instance instance : this.trainingSet) {
        runNeuralNet(instance);
        for (int i = 0; i < this.outputNodes.size(); i++) {
          Node outNode = this.outputNodes.get(i);
          outNode.calculateDelta(instance.classValues.get(i), this.outputNodes,
              i);
        }
        for (int i = 0; i < this.hiddenNodes.size(); i++) {
          Node hidNode = this.hiddenNodes.get(i);
          hidNode.calculateDelta(-99999, this.outputNodes, i);
        }
        for (Node node : this.outputNodes) {
          node.updateWeight(this.learningRate);
        }
        for (Node node : this.hiddenNodes) {
          node.updateWeight(this.learningRate);
        }
      }

      for (Instance inst : this.trainingSet) {
        totalLoss += loss(inst);
      }

      totalLoss /= this.trainingSet.size();
      System.out.print("Epoch: " + j + ", Loss: ");
      System.out.format("%.3e", totalLoss);
      System.out.println();
    }
  }

  /**
   * calculates the Cross-Entropy loss from the neural network for a single
   * instance. This function will be used by train()
   */
  private double loss(Instance instance) {
    runNeuralNet(instance);
    double crossEntropy = 0;
    for (int i = 0; i < this.outputNodes.size(); i++) {
      double g = this.outputNodes.get(i).getOutput();
      crossEntropy -= instance.classValues.get(i) * Math.log(g);
    }
    return crossEntropy;
  }

  private void runNeuralNet(Instance instance) {
    // Set input values on the input nodes
    for (int i = 0; i < this.inputNodes.size() - 1; i++) {
      Node node = this.inputNodes.get(i);
      node.setInput(instance.attributes.get(i));
    }


    for (Node node : this.hiddenNodes) {
      node.getWeightedInputValue();
      node.calculateOutput(null);
    }
    for (Node node : this.outputNodes) {
      node.getWeightedInputValue();
    }
    for (Node node : this.outputNodes) {
      node.calculateOutput(this.outputNodes);
    }
  }


  /**
   * calculates the output for an example
   */

  int predict(Instance instance) {

    runNeuralNet(instance);

    // Initialize values
    int predict = 0;
    double max = Double.MIN_VALUE;

    // Loop through outputs
    for (int i = 0; i < this.outputNodes.size(); i++) {
      double out = this.outputNodes.get(i).getOutput();
      if (out > max) {
        max = out;
        predict = i;
      }
    }
    return predict;
  }
}
