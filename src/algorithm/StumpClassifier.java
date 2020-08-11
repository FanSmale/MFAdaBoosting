package algorithm;

import weka.core.Instance;
import java.io.FileReader;
import java.util.*;

/**
 * The stump classifier.<br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: July 18, 2020.<br>
 *         Last modified: July 19, 2020.
 * @version 1.0
 */

public class StumpClassifier {
	/**
	 * A random object.
	 */
	static Random random = new Random();

	/**
	 * Weighted data.
	 */
	WeightedInstances weightedInstances;

	/**
	 * The index of the current attribute.
	 */
	int selectedAttribute;

	/**
	 * The best cut for the current attribute on weightedInstances.
	 */
	double bestCut;

	/**
	 * The class label for attribute value less than bestCut.
	 */
	int leftLeafLabel;

	/**
	 * The class label for attribute value no less than bestCut.
	 */
	int rightLeafLabel;

	/**
	 * The accuracy on the training set.
	 */
	double trainingAccuracy;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraWeightedInstances
	 *            The given instances.
	 ****************** 
	 */
	public StumpClassifier(WeightedInstances paraWeightedInstances) {
		weightedInstances = paraWeightedInstances;
	}// Of the first constructor

	/**
	 ****************** 
	 * Train the classifier.
	 ****************** 
	 */
	public void train() {
		// Step 1. Randomly choose an attribute.
		selectedAttribute = random.nextInt(weightedInstances.numAttributes() - 1);

		// Step 2. Find all attribute values and sort.
		double[] tempValuesArray = new double[weightedInstances.numInstances()];
		for (int i = 0; i < tempValuesArray.length; i++) {
			tempValuesArray[i] = weightedInstances.instance(i).value(selectedAttribute);
		} // Of for i
		Arrays.sort(tempValuesArray);

		// Step 3. Initialize, classify all instances as the same with the
		// original cut.
		int tempNumLabels = weightedInstances.classAttribute().numValues();
		double[] tempLabelCountArray = new double[tempNumLabels];
		int tempCurrentLabel;

		// Step 3.1 Scan all labels to obtain their counts.
		for (int i = 0; i < weightedInstances.numInstances(); i++) {
			// The label of the ith instance
			tempCurrentLabel = (int) weightedInstances.instance(i).classValue();
			tempLabelCountArray[tempCurrentLabel] += weightedInstances.getWeight(i);
		} // Of for i

		// Step 3.2 Find the label with the maximal count.
		double tempMaxCorrect = 0;
		int tempBestLabel = -1;
		for (int i = 0; i < tempLabelCountArray.length; i++) {
			if (tempMaxCorrect < tempLabelCountArray[i]) {
				tempMaxCorrect = tempLabelCountArray[i];
				tempBestLabel = i;
			} // Of if
		} // Of for i

		// Step 3.3 The cut is a little bit smaller than the minimal value.
		bestCut = tempValuesArray[0] - 0.1;
		leftLeafLabel = tempBestLabel;
		rightLeafLabel = tempBestLabel;

		// Step 4. Check candidate cuts one by one.
		// Step 4.1 To handle multi-class data, left and right.
		double tempCut;
		double[][] tempLabelCountMatrix = new double[2][tempNumLabels];

		for (int i = 0; i < tempValuesArray.length - 1; i++) {
			// Step 4.1 Some attribute values are identical, ignore them.
			if (tempValuesArray[i] == tempValuesArray[i + 1]) {
				continue;
			} // Of if
			tempCut = (tempValuesArray[i] + tempValuesArray[i + 1]) / 2;

			// Step 4.2 Scan all labels to obtain their counts wrt. the cut.
			// Initialize again since it is used many times.
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < tempNumLabels; k++) {
					tempLabelCountMatrix[j][k] = 0;
				} // Of for k
			} // Of for j

			for (int j = 0; j < weightedInstances.numInstances(); j++) {
				// The label of the jth instance
				tempCurrentLabel = (int) weightedInstances.instance(j).classValue();
				if (weightedInstances.instance(j).value(selectedAttribute) < tempCut) {
					tempLabelCountMatrix[0][tempCurrentLabel] += weightedInstances.getWeight(j);
				} else {
					tempLabelCountMatrix[1][tempCurrentLabel] += weightedInstances.getWeight(j);
				} // Of if
			} // Of for i

			// Step 4.3 Left leaf.
			double tempLeftMaxCorrect = 0;
			int tempLeftBestLabel = 0;
			for (int j = 0; j < tempLabelCountMatrix[0].length; j++) {
				if (tempLeftMaxCorrect < tempLabelCountMatrix[0][j]) {
					tempLeftMaxCorrect = tempLabelCountMatrix[0][j];
					tempLeftBestLabel = j;
				} // Of if
			} // Of for i

			// Step 4.4 Right leaf.
			double tempRightMaxCorrect = 0;
			int tempRightBestLabel = 0;
			for (int j = 0; j < tempLabelCountMatrix[1].length; j++) {
				if (tempRightMaxCorrect < tempLabelCountMatrix[1][j]) {
					tempRightMaxCorrect = tempLabelCountMatrix[1][j];
					tempRightBestLabel = j;
				} // Of if
			} // Of for i

			// Step 4.5 Compare with the current best.
			if (tempMaxCorrect < tempLeftMaxCorrect + tempRightMaxCorrect) {
				tempMaxCorrect = tempLeftMaxCorrect + tempRightMaxCorrect;
				bestCut = tempCut;
				leftLeafLabel = tempLeftBestLabel;
				rightLeafLabel = tempRightBestLabel;
			} // Of if
		} // Of for i
	}// Of train

	/**
	 ****************** 
	 * Classify an instance.
	 * 
	 * @param paraInstance
	 *            The given instance.
	 * @return Predicted label.
	 ****************** 
	 */
	public int classify(Instance paraInstance) {
		int resultLabel = -1;
		if (paraInstance.value(selectedAttribute) < bestCut) {
			resultLabel = leftLeafLabel;
		} else {
			resultLabel = rightLeafLabel;
		} // Of if
		return resultLabel;
	}// Of classify

	/**
	 ****************** 
	 * Which instances in the training set are correctly classified.
	 * 
	 * @return The correctness array.
	 ****************** 
	 */
	public boolean[] computeCorrectnessArray() {
		boolean[] resultCorrectnessArray = new boolean[weightedInstances.numInstances()];
		for (int i = 0; i < resultCorrectnessArray.length; i++) {
			Instance tempInstance = weightedInstances.instance(i);
			if ((int) (tempInstance.classValue()) == classify(tempInstance)) {
				resultCorrectnessArray[i] = true;
			} // Of if
		} // Of for i
		return resultCorrectnessArray;
	}// Of computeCorrectnessArray

	/**
	 ****************** 
	 * Compute the accuracy on the training set.
	 * 
	 * @return The training accuracy.
	 ****************** 
	 */
	public double computeTrainingAccuracy() {
		double tempCorrect = 0;
		boolean[] tempCorrectnessArray = computeCorrectnessArray();
		for (int i = 0; i < tempCorrectnessArray.length; i++) {
			if (tempCorrectnessArray[i]) {
				tempCorrect++;
			} // Of if
		} // Of for i

		double resultAccuracy = tempCorrect / tempCorrectnessArray.length;

		return resultAccuracy;
	}// Of computeTrainingAccuracy

	/**
	 ****************** 
	 * Compute the weighted error on the training set. It is at least 1e-6 to
	 * avoid NaN.
	 * 
	 * @return The weighted error.
	 ****************** 
	 */
	public double computeWeightedError() {
		double resultError = 0;
		boolean[] tempCorrectnessArray = computeCorrectnessArray();
		for (int i = 0; i < tempCorrectnessArray.length; i++) {
			if (!tempCorrectnessArray[i]) {
				resultError += weightedInstances.getWeight(i);
			} // Of if
		} // Of for i

		if (resultError < 1e-6) {
			resultError = 1e-6;
		} // Of if

		return resultError;
	}// Of computeWeightedError

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a stump classifier.\r\n" + "I choose attribute #"
				+ selectedAttribute + " with cut value " + bestCut + ".\r\n"
				+ "The left and right leaf labels are " + leftLeafLabel + " and " + rightLeafLabel
				+ ", respectively.\r\n" + "My weighted error is: " + computeWeightedError()
				+ ".\r\n" + "My weighted accuracy is : " + computeTrainingAccuracy() + ".";

		return resultString;
	}// Of toString

	/**
	 ****************** 
	 * For unit test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		WeightedInstances tempWeightedInstances = null;
		String tempFilename = "src/data/iris.arff";
		try {
			FileReader tempFileReader = new FileReader(tempFilename);
			tempWeightedInstances = new WeightedInstances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + tempFilename + "\r\n" + ee);
			System.exit(0);
		} // Of try

		StumpClassifier tempClassifier = new StumpClassifier(tempWeightedInstances);
		tempClassifier.train();
		System.out.println(tempClassifier);

		System.out.println(Arrays.toString(tempClassifier.computeCorrectnessArray()));
	}// Of main
}// Of class StumpClassifier
