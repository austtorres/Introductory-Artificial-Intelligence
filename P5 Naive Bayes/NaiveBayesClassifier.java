import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

/**
 * Your implementation of a naive bayes classifier. Please implement all four
 * methods.
 */

public class NaiveBayesClassifier implements Classifier {

  private Set<String> dict;

  // Private Map instances to classify words in vocab
  private Map<Label, Integer> words; // Words
  private Map<Label, Integer> docs; // Documents

  private Map<String, Integer> posWords; // Good words
  private Map<String, Integer> negWords; // Bad words

  private Map<Label, Double> cache;

  private double posPrev; // Positive prior word
  private double negPrev; // Negative prior word

  // Vocabulary to be maintained
  private int vocab;

  /**
   * Trains the classifier with the provided training data and vocabulary size
   */
  @Override
  public void train(List<Instance> trainData, int v) {

    // Initialize to null to maintain accurate value later
    cache = null;

    // Quantify vocabulary
    vocab = v;

    // Hint: First, calculate the documents and words counts per label and store
    // them.
    docs = getDocumentsCountPerLabel(trainData);
    words = getWordsCountPerLabel(trainData);

    // Then, for all the words in the documents of each label, count the number
    // of occurrences of each word.

    // Create hash set
    dict = new HashSet<String>();

    posWords = new HashMap<String, Integer>();
    negWords = new HashMap<String, Integer>();

    // Iterate through training data
    for (Instance inst : trainData) {

      // For positive words
      if (inst.label == Label.POSITIVE) {
        for (String s : inst.words) {

          // Add word to dict
          dict.add(s);

          // Update positive words
          posWords.put(s, posWords.getOrDefault(s, 0) + 1);
        }
      }

      // Negative words
      else if (inst.label == Label.NEGATIVE) {
        for (String s : inst.words) {

          // Add to dict
          dict.add(s);

          // Update negative word collection
          negWords.put(s, negWords.getOrDefault(s, 0) + 1);
        }
      }
    }

    // Save these information as you will need them to calculate the log
    // probabilities later.
    int N = trainData.size();

    // Empty training set
    if (N == 0) {

      // No prior words to classify
      negPrev = 0.0;
      posPrev = 0.0;
    }

    // Non-empty training set
    else {

      // Count positive and negative words
      posPrev = (double) docs.getOrDefault(Label.POSITIVE, 0) / (double) N;
      negPrev = (double) docs.getOrDefault(Label.NEGATIVE, 0) / (double) N;
    }
    //
    // e.g.
    // Assume m_map is the map that stores the occurrences per word for positive
    // documents
    // m_map.get("catch") should return the number of "catch" es, in the
    // documents labeled positive
    // m_map.get("asdasd") would return null, when the word has not appeared
    // before.
    // Use m_map.put(word,1) to put the first count in.
    // Use m_map.replace(word, count+1) to update the value
  }

  /*
   * Counts the number of words for each label
   */
  @Override
  public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {

    // Create hash set
    Map<Label, Integer> m = new HashMap<>();

    // Counters for positive and negatives in label
    int posCounter = 0;
    int negCounter = 0;

    // Iterate through training data
    for (Instance inst : trainData) {

      // Positive occurrences
      if (inst.label == Label.POSITIVE) {

        for (String s : inst.words) {

          // Maintain count of positives
          posCounter++;
        }
      }

      // Negative occurrences
      else if (inst.label == Label.NEGATIVE) {

        for (String s : inst.words) {

          // Maintain count of negatives
          negCounter++;
        }
      }
    }

    // Update m
    m.put(Label.POSITIVE, posCounter);

    m.put(Label.NEGATIVE, negCounter);

    return m;
  }


  /*
   * Counts the total number of documents for each label
   */
  @Override
  public Map<Label, Integer> getDocumentsCountPerLabel(
      List<Instance> trainData) {

    // Create new Hash Map
    Map<Label, Integer> map = new HashMap<>();

    // Loop through training data
    for (Instance inst : trainData) {

      // Positive occurrences
      if (inst.label == Label.POSITIVE) {

        // Add positive to map
        map.put(Label.POSITIVE, map.getOrDefault(Label.POSITIVE, 0) + 1);
      }

      // Negative occurrences
      else if (inst.label == Label.NEGATIVE) {

        // Add negative to map
        map.put(Label.NEGATIVE, map.getOrDefault(Label.NEGATIVE, 0) + 1);
      }
    }

    return map;
  }


  /**
   * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or
   * P(NEGATIVE)
   */
  private double p_l(Label label) {

    // Positive label
    if (label == Label.POSITIVE) {
      return posPrev;
    }

    // Negative label
    else if (label == Label.NEGATIVE) {
      return negPrev;
    }
    // Neutral
    return 0;
  }

  /**
   * Returns the smoothed conditional probability of the word given the label,
   * i.e. P(word|POSITIVE) or P(word|NEGATIVE)
   */
  private double p_w_given_l(String word, Label label) {

    // Numerator and denominators initialized to zero
    double numerator = 0.0;
    double denominator = 0.0;

    // Instance of map
    Map<String, Integer> map;

    // For positive labels
    if (label == Label.POSITIVE) {
      map = posWords;
    }

    // For negative labels
    else {
      map = negWords;
    }

    // Prepare smoothed numerator of calculation
    numerator = (double) map.getOrDefault(word, 0) + 1.0;

    if (cache != null && cache.containsKey(label)) {

      // Set denominator
      denominator = cache.get(label);
    }


    else {

      // Iterate through dict
      for (String s : dict) {

        // Update denominator
        denominator += (double) map.getOrDefault(s, 0);
      }
      denominator += (double) vocab;

      if (cache == null) {

        cache = new HashMap<Label, Double>();
      }

      cache.put(label, denominator);
    }

    // Smoothed conditional probability
    return denominator == 0.0 ? 0.0 : numerator / denominator;
  }

  /**
   * Classifies an array of words as either POSITIVE or NEGATIVE.
   */
  @Override
  public ClassifyResult classify(List<String> words) {
    // TODO : Implement
    // Sum up the log probabilities for each word in the input data, and the
    // probability of the label
    // Set the label to the class with larger log probability

    // log probabilities to be summed up
    double posLog = posPrev == 0.0 ? 0.0 : Math.log(posPrev);
    double negLog = negPrev == 0.0 ? 0.0 : Math.log(negPrev);

    // Iterate through all words
    for (String s : words) {

      // Classify positive condition as word with positive label
      double posCondition = p_w_given_l(s, Label.POSITIVE);

      // Classify negative condition as word with negative label
      double negCondition = p_w_given_l(s, Label.NEGATIVE);

      // Maintain conditions using log probabilities
      posLog += posCondition == 0 ? 0.0 : Math.log(posCondition);
      negLog += negCondition == 0 ? 0.0 : Math.log(negCondition);
    }

    // Result of classification
    ClassifyResult result = new ClassifyResult();

    // For words that are negative
    if (posLog < negLog) {

      // Give negative label
      result.label = Label.NEGATIVE;
    }

    // For positive words
    else {

      // Give positive label
      result.label = Label.POSITIVE;
    }

    // Create hash map
    Map<Label, Double> logs = new HashMap<Label, Double>();

    // Add label to log probabilities
    logs.put(Label.POSITIVE, posLog);
    logs.put(Label.NEGATIVE, negLog);


    result.logProbPerLabel = logs;

    return result;
  }


}
