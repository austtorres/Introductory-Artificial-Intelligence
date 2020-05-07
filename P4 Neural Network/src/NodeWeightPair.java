////////////////////////////////////////////////////////////////////////////////
// Main File:        DigitClassifier.java
// This File:        NodeWeightPair.java
// Other Files:      NNImpl.java, DigitClassifier.java, Node.java
// Semester:         CS 540 Fall 2019
//
// Author:           Austin Torres
// Email:            artorres3@wisc.edu
// CS Login:         atorres
//
/////////////////////////// OTHER SOURCES OF HELP //////////////////////////////
//                   fully acknowledge and credit all sources of help,
//                   other than Instructors and TAs.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of 
//                   of any information you find.
//////////////////////////// 80 columns wide ///////////////////////////////////

/**
 * Class to identify connections
 * between different layers.
 * Do NOT modify.
 */

public class NodeWeightPair {
    public Node node; //The parent node
    public double weight; //Weight of this connection

    //Create an object with a given parent node
    //and connect weight
    NodeWeightPair(Node node, Double weight) {
        this.node = node;
        this.weight = weight;
    }
}