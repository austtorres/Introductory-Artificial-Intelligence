import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
  /*
   * Returns the k-fold cross validation score of classifier clf on training
   * data.
   */
  public static double kFoldScore(Classifier clf, List<Instance> trainData,
      int k, int v) {


    double[] acc = new double[k]; // Accuracy

    int instances = trainData.size() / k; // Number of instances per set

    // ArrayList of sets
    ArrayList<ArrayList<Instance>> sets = new ArrayList<ArrayList<Instance>>();

    // Index to be maintained
    int index = 0;

    // Calculate subsets of the training data
    for (int i = 0; i < k; i++) {

      // Declare current set
      ArrayList<Instance> currentSet = new ArrayList<Instance>();

      for (int j = index; j < index + instances; j++) {

        currentSet.add(trainData.get(j));

      }

      // Add set to sets
      sets.add(currentSet);

      // Maintain index
      index += instances;

    }

    // Cross validate
    for (int i = 0; i < k; i++) {
      ArrayList<Instance> testSet = sets.get(i);

      // List of training set of instances
      ArrayList<Instance> trainSet = new ArrayList<Instance>();

      // Iterate through sets
      for (int j = 0; j < k; j++) {

        if (i != j) {

          // Add sets to training set
          trainSet.addAll(sets.get(j));
        }

      }

      // Use training set to train
      clf.train(trainSet, v);

      // Number of correct classifications
      int correctClassifications = 0;

      // Calculate accuracy
      for (Instance inst : testSet) {

        ClassifyResult result = clf.classify(inst.words);

        // Maintain correct classifications
        if (result.label.ordinal() == inst.label.ordinal()) {
          correctClassifications++;
        }

      }

      // Calculate accuracy at index
      acc[i] = ((double) correctClassifications) / testSet.size();

    }

    // Final accuracy as calculated
    double finalAccuracy = 0.0;

    for (int i = 0; i < k; i++) {

      // Maintain accuracy
      finalAccuracy += acc[i];
    }

    return finalAccuracy / k;
  }
}
