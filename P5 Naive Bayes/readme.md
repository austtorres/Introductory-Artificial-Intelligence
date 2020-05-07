One application of Naïve Bayes classifiers is sentiment analysis, which is a sub-field of AI that
extracts affective states and subjective information from text. One common use of sentiment
analysis is to determine if a text document expresses negative or positive feelings. In this
project I implement a Naïve Bayes classifier for categorizing movie reviews as
either POSITIVE or NEGATIVE. The dataset provided consists of online movie reviews derived
from an IMDb dataset: https://ai.stanford.edu/~amaas/data/sentiment/ that have been
labeled based on the review scores. A negative review has a score ≤ 4 out of 10, and a positive
review has a score ≥ 7 out of 10. Preprocessing has been done on the original dataset to
remove some noisy features. Each row in the training set and test set files contains one review,
where the first word in each line is the class label (1 represents POSITIVE and 0 represents
NEGATIVE) and the remainder of the line is the review text. 

Four sample test cases:
- `java SentimentAnalysis 0 train.txt test.txt` -> mode0.txt

- `java SentimentAnalysis 1 train.txt test.txt` -> mode1.txt

- `java SentimentAnalysis 2 train.txt test.txt` -> mode2.txt

- `java SentimentAnalysis 3 train.txt 5` -> mode3.txt
