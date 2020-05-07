In this problem you are to implement a program that builds a binary decision tree for numerical attributes,
and binary classification tasks. By binary tree, we mean that every non-leaf node of your tree will have
exactly two children. Each node will have a selected attribute and an associated threshold value.
Instances (aka examples) that have an attribute value less than or equal to the threshold belong to the
left subtree of a node, and instances with an attribute value greater than the threshold belong to the right
subtree of a node. The programming part requires building a tree from a training set and classifying
instances of both the training set and the testing set with the learned decision tree.

1. All attributes will be (numerical) integer valued and have values from 1 to 10. There will be no
categorical attributes or non-integer real-valued attributes in the set of training or test instances.
2. Splits on integer-valued attributes are binary (i.e., each split creates a <= set and a > set).
3. An attribute can be split on multiple times in the decision tree.
4. All attributes will appear in the same order for every instance (a row in the data file) and will always be
separated by commas only.
5. The first column of every row in the input data file contains the ID and will be discarded. The last column
contains the class label (2 or 4) for that specific instance. The skeleton code provided removes the first
column and converts the class labels to 0 or 1 (2 to 0 and 4 to 1) when reading the file. 

Aspects of the Tree
Your decision tree must have certain properties so that the program output based on your decision tree
implementation matches our own. The constraints are:
1. The attribute values of your instances are integers, but the information gain calculation must be done
using doubles. Remember to use the convention that 0 log2 0 = 0 when computing the information gain.
2. At any non-leaf node in the tree, the left child of a node represents instances that have an attribute
value less than or equal to (<=) the threshold specified at the node. The right child of a node represents
instances that have an attribute value greater than (>) the threshold specified at the node.
3. When selecting the attribute at a decision tree node, first find the best (i.e., highest information gain)
threshold for each attribute by evaluating multiple candidate split thresholds. The candidate splits are
integers in {1, 2, …, 9} for each attribute for this homework. Once the best candidate split is found for each
attribute, choose the attribute that has the highest information gain (among the ones strictly larger than
0). If there are multiple attributes with the same information gain, split on the attribute that appears
earliest in the list of attribute labels. If an attribute has multiple split thresholds producing the same best
information gain, select the threshold with lowest integer value.
4. When creating a decision tree node, if the number of instances belonging to that node is less than or
equal to the “maximum instances per leaf”, or if the depth is equal to the “maximum depth” (the root
node has depth 0), or if the maximum information gain is 0, then a leaf node must be created. The label
assigned to the node is the majority label of the instances belonging to that node. If there are an equal
number of instances labeled 0 and 1, then the label 1 is assigned to the leaf.

Description of Program Outputs
Command line format:

java HW3 <train file> <test file> <maximum instances per leaf> <maximum depth>

where <train file> and <test file> are the names of the training and testing datasets,
formatted to the specifications described later in this document, and <maximum instances per
leaf> and <maximum depth> are strictly positive integers. The output prints a decision tree built
from the training set followed by the classification for each example in the testing set (either 0 or 1), and
the accuracy (rounded to 2 decimal in percentage, a simple String.format(“%.2f”) should be fine to use)
on the testing set.

Data
The dataset we’ve provided comes from the Wisconsin Breast Cancer dataset and can be found at the UCI
machine learning repository:
https://archive.ics.uci.edu/ml/datasets/Breast+Cancer+Wisconsin+%28Original%29
The first column is an ID, not an attribute, and therefore is not used to construct the decision tree. The
next nine columns are attributes, named X0, X1, …, X8, that have integer values from 1 to 10. The last
column contains the class label (Y = 2 for benign, Y = 4 for malignant). Each row corresponds to one sample
from a patient. Rows with missing data (rows that contain “?”) are removed by the provided skeleton
code.

This means, for the purpose of this homework, you may use the special property that attributes X1 to X9
are integers 1 to 10, and the label Y is converted to 0 or 1 correctly when reading the input file.
We have provided input and output for the following three test cases:
java HW3 train_1.data test_1.data 10 10 -> output_1.txt
java HW3 train_2.data test_2.data 10 1 -> output_2.txt
java HW3 train_3.data test_3.data 1 10 -> output_3.txt
You can download the complete data set from the UCI machine learning repository for additional training
and test data if you desire.
