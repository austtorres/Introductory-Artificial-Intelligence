// Project Title: HW3 P3 Decision Tree
//
// Description: Build a decision tree using training data in the form of
// instances containing 9 attributes. Calculate information gain and entropy to
// determine best attribute-threshold pair to select best pair
//
// Files: HW3.java, DecisionTreeImpl.java, DecTreeNode.java
// Course: CS540 Fall 2020
//
// Author: Austin Torres
// Email: artorres3@wisc.edu
// Lecturer's Name: Chuck Dyer
// Lecture: 001
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.util.List;
import java.util.ArrayList;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 */
public class DecisionTreeImpl {
  public DecTreeNode root; // Root node
  public List<List<Integer>> trainData; // Data used to build tree
  public int maxPerLeaf; // Maximum instances per leaf
  public int maxDepth; // Max depth of tree
  public int numAttr; // represents the number of attributes


  /**
   * Private inner class used to determine the best attribute between a pair
   *
   */
  private class bestAttribute {

    private int attribute;
    private int threshold;

    private bestAttribute(int x, int y) {
      this.attribute = x;
      this.threshold = y;
    }
  }

  // Initialize values and build tree using training data set
  DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {

    this.trainData = trainDataSet;

    this.maxPerLeaf = mPerLeaf;

    this.maxDepth = mDepth;

    if (this.trainData.size() > 0) {

      this.numAttr = trainDataSet.get(0).size() - 1;
    }

    this.root = buildTree(this.trainData, 0);
  }

  /**
   * Use data sets to build a decision tree
   * 
   * @param dataset  training data to build tree
   * @param curDepth current depth of tree
   * @return node
   */
  private DecTreeNode buildTree(List<List<Integer>> dataset, int curDepth) {

    DecTreeNode node = null; // Empty tree

    // Counters to track class
    int malignantCounter = 0;

    int benignCounter = 0;

    for (List<Integer> list : dataset) {

      // If last attribute (class) is 0, increment benign counter
      if (list.get(list.size() - 1) == 0) {

        benignCounter += 1;

        // Otherwise increment malignant counter (has to be one or the other
        // since its binary)
      } else {

        malignantCounter += 1;
      }

    }


    // if the number of instances belonging to that node is less than or equal
    // to the “maximum instances per leaf”.....then a leaf node must be created.
    if ((benignCounter + malignantCounter) <= this.maxPerLeaf) {

      if (benignCounter > malignantCounter) {

        node = new DecTreeNode(0, -1, -1);
      } else {
        node = new DecTreeNode(1, -1, -1);
      }
    }

    // if the depth is equal to the “maximum depth” (the root node has depth 0)
    // then a leaf node must be created
    else if (curDepth >= this.maxDepth) {

      // The possible states for classes and their counters
      if (benignCounter > malignantCounter) {

        node = new DecTreeNode(0, -1, -1);

      } else {

        node = new DecTreeNode(1, -1, -1);
      }
    } else if (benignCounter == 0) {

      node = new DecTreeNode(1, -1, -1);

    } else if (malignantCounter == 0) {

      node = new DecTreeNode(0, -1, -1);
    }

    // if the maximum information gain is 0, then a leaf node must be created
    else {
      bestAttribute bestAttributes = optimalATPair(dataset);

      // Determine type of node to create

      // No attributes in group of best attributes
      if (bestAttributes == null) {

        if (benignCounter > malignantCounter) {

          node = new DecTreeNode(0, -1, -1);
        } else {

          node = new DecTreeNode(1, -1, -1);
        }
      }

      // Attributes exist in group of best attributes
      else {

        int attribute = bestAttributes.attribute;

        int threshold = bestAttributes.threshold;

        List<List<Integer>> above = new ArrayList<>();

        List<List<Integer>> below = new ArrayList<>();

        for (List<Integer> dataPoint : dataset) {

          if (dataPoint.get(attribute) <= threshold) {

            below.add(dataPoint);
          } else if (dataPoint.get(attribute) > threshold) {

            above.add(dataPoint);
          }

        }

        DecTreeNode benignNode = buildTree(below, curDepth + 1);
        DecTreeNode malignantNode = buildTree(above, curDepth + 1);

        node = new DecTreeNode(-1, attribute, threshold);
        node.left = benignNode;
        node.right = malignantNode;
      }
    }

    return node;
  }



  /**
   * Determine class using tree built with training data
   * 
   * @param instance being classified
   * @return class of instance
   */
  public int classify(List<Integer> instance) {
    // Note that the last element of the array is the label.
    int predictedClass = traverse(this.root, instance);
    return predictedClass;
  }


  /**
   * Traverse tree built with training data to find class of instance
   * 
   * @param node      being traversed
   * @param dataPoint being compared
   * @return class found at leaf
   */
  private int traverse(DecTreeNode node, List<Integer> dataPoint) {

    int predictedClass = -1;

    if (node.isLeaf()) { // Found label (class)

      return node.classLabel;

    } else {

      // Traverse left
      if (dataPoint.get(node.attribute) <= node.threshold) {

        return traverse(node.left, dataPoint);
      }

      // Traverse right
      else if (dataPoint.get(node.attribute) > node.threshold) {

        return traverse(node.right, dataPoint);
      }
    }
    return predictedClass;
  }


  // Returns information gain of given parameters
  private double informationGain(double initialEntropy, int leftZero,
      int leftOne, int rightZero, int rightOne, double entropyLeft,
      double entropyRight) {

    // Sum of info gain
    int totalGain = leftZero + leftOne + rightZero + rightOne;

    // Find final entropy using formula
    double finalEntropy =
        ((double) ((double) leftZero + (double) leftOne) / ((double) totalGain))
            * (entropyLeft)
            + ((double) ((double) rightZero + (double) rightOne)
                / ((double) totalGain)) * (entropyRight);

    // How to determine information gain
    double informationGain = initialEntropy - finalEntropy;

    return informationGain;
  }

  /**
   * Determines info gain for attribute
   * 
   * @param dataset       being tested
   * @param initialEn     starting entropy
   * @param attributePair attribute threshold pair being analyzed
   * @return info gain of specified attribute
   */
  private double attributeInfoGain(List<List<Integer>> dataset,
      double initialEn, bestAttribute attributePair) {

    // Zero is benign, one is malignant

    // Counters for labels less than or equal to or greater than threshold
    int rightZero = 0;

    int rightOne = 0;

    int leftZero = 0;

    int leftOne = 0;

    int attribute = attributePair.attribute;

    int threshold = attributePair.threshold;

    for (List<Integer> dataPoint : dataset) {

      // If attribute is less than or equal to threshold at that location
      if (dataPoint.get(attribute) <= threshold) {

        // Update leftZero counter if label is 0
        if (dataPoint.get(dataPoint.size() - 1) == 0) {

          leftZero++;
        }

        // Update leftOne counter if label is 1
        else if (dataPoint.get(dataPoint.size() - 1) == 1) {

          leftOne++;
        }
      }

      // If attribute is greater than the threshold at that location
      // Same implementation as above
      else if (dataPoint.get(attribute) > threshold) {

        if (dataPoint.get(dataPoint.size() - 1) == 0) {

          rightZero++;
        } else if (dataPoint.get(dataPoint.size() - 1) == 1) {

          rightOne++;
        }
      }
    }

    // Calculate the entropy of left and right subtrees
    double leftTreeEntropy = calculateEntropy(leftZero, leftOne);

    double rightTreeEntropy = calculateEntropy(rightZero, rightOne);

    double infoGain = informationGain(initialEn, leftZero, leftOne, rightZero,
        rightOne, leftTreeEntropy, rightTreeEntropy);

    return infoGain;

  }


  /**
   * Find the initial entropy of the given data set
   * 
   * @param dataset being tested
   * @return initial entropy
   */
  private double initialEtropy(List<List<Integer>> dataset) {

    // Initialize entropy and label counters
    double initEntropy = 0;

    int zeroLabels = 0;

    int oneLabels = 0;

    for (List<Integer> list : dataset) {

      // Determine labels (class) by looking at the last digit of the instance
      if (list.get(list.size() - 1) == 0) {

        zeroLabels += 1;
      } else if (list.get(list.size() - 1) == 1) {

        oneLabels += 1;
      }
    }

    initEntropy = calculateEntropy(zeroLabels, oneLabels);

    return initEntropy;
  }

  /**
   * Calculate entropy using formula given in lecture
   * 
   * @param zeroCount benign labels
   * @param oneCount  malignant label
   * @return entropy
   */
  private double calculateEntropy(int zeroCount, int oneCount) {

    // Initialize values for calculation
    // Condensed parts of entropy formula such as log and the fractions into
    // their own variables to clean up the code
    double entropy = 0;

    double zeroValue = 0;

    double oneValue = 0;

    double loggedZero = 0;

    double loggedOne = 0;

    // Formula found in slides
    if (zeroCount != 0) {

      zeroValue =
          (double) ((double) (zeroCount)) / ((double) (zeroCount + oneCount));

      loggedZero = Math.log(zeroValue) / Math.log((double) 2);
    }

    if (oneCount != 0) {

      oneValue =
          (double) ((double) (oneCount)) / ((double) (zeroCount + oneCount));

      loggedOne = Math.log(oneValue) / Math.log((double) 2);
    }

    entropy = -(zeroValue * loggedZero + oneValue * loggedOne);

    return entropy;
  }



  /**
   * Uses information gain to determine the best attribute-threshold pair to use
   * 
   * @param dataset to test
   * @return best AT pair
   */
  private bestAttribute optimalATPair(List<List<Integer>> dataset) {

    // best attribute threshold pair
    bestAttribute bestATPair = null;

    double initEntropy = initialEtropy(dataset);

    double maxInfoGain = -1;

    int attribute = 0;

    // Test attribute threshold pairs to find the best one

    // Loop through attributes
    for (attribute = 0; attribute < 9; attribute++) {

      // Loop through thresholds
      for (int threshold = 1; threshold <= 10; threshold++) {

        bestAttribute ATPair = new bestAttribute(attribute, threshold);

        // Use information gain to determine best pair (higher is better)
        double informationGain = 0;

        informationGain = attributeInfoGain(dataset, initEntropy, ATPair);

        // Maintain maxInfoGain by updating it with each higher info gain
        if (informationGain > maxInfoGain) {

          maxInfoGain = informationGain;

          // Update best attribute threshold pair
          bestATPair = ATPair;
        }
      }
    }

    // If info gain is 0 (attribute already tested, or any other reason)
    if (maxInfoGain == 0) {

      return null;
    }

    // Best possible pair according to max info gain
    return bestATPair;
  }

  // Print the decision tree in the specified format
  public void printTree() {
    printTreeNode("", this.root);
  }

  public void printTreeNode(String prefixStr, DecTreeNode node) {
    String printStr = prefixStr + "X_" + node.attribute;
    System.out.print(printStr + " <= " + String.format("%d", node.threshold));
    if (node.left.isLeaf()) {
      System.out.println(" : " + String.valueOf(node.left.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.left);
    }
    System.out.print(printStr + " > " + String.format("%d", node.threshold));
    if (node.right.isLeaf()) {
      System.out.println(" : " + String.valueOf(node.right.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.right);
    }
  }

  public double printTest(List<List<Integer>> testDataSet) {
    int numEqual = 0;
    int numTotal = 0;
    for (int i = 0; i < testDataSet.size(); i++) {
      int prediction = classify(testDataSet.get(i));
      int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
      System.out.println(prediction);
      if (groundTruth == prediction) {
        numEqual++;
      }
      numTotal++;
    }
    double accuracy = numEqual * 100.0 / (double) numTotal;
    System.out.println(String.format("%.2f", accuracy) + "%");
    return accuracy;
  }
}

