package algorithm;

import java.io.FileReader;
import java.util.Arrays;

import weka.core.Instances;

/**
 * Weighted instances. There is a weight on each instance. <br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: July 18, 2020.<br>
 *         Last modified: July 19, 2020.
 * @version 1.0
 */

public class WeightedInstances extends Instances {
	/**
	 * Just the requirement of some classes, any number is ok.
	 */
	private static final long serialVersionUID = 11087456L;

	/**
	 * Weights.
	 */
	private double[] weights;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraReader
	 *            The given reader to read data from file.
	 ****************** 
	 */
	public WeightedInstances(FileReader paraFileReader) throws Exception {
		super(paraFileReader);
		setClassIndex(numAttributes() - 1);

		// Initialize weights
		weights = new double[numInstances()];
		double tempAverage = 1.0 / numInstances();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = tempAverage;
		} // Of for i
	} // Of the first constructor

	/**
	 ****************** 
	 * The second constructor.
	 * 
	 * @param paraInstances
	 *            The given instance.
	 ****************** 
	 */
	public WeightedInstances(Instances paraInstances) {
		super(paraInstances);
		setClassIndex(numAttributes() - 1);

		// Initialize weights
		weights = new double[numInstances()];
		double tempAverage = 1.0 / numInstances();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = tempAverage;
		} // Of for i
	} // Of the second constructor

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @param paraIndex
	 *            The given index.
	 * @return The weight of the given index.
	 ****************** 
	 */
	public double getWeight(int paraIndex) {
		return weights[paraIndex];
	} // Of getWeight

	/**
	 ****************** 
	 * Adjust the weights.
	 * 
	 * @param paraCorrectArray
	 *            Indicate which instances have been correctly classified.
	 * @param paraAlpha
	 *            The weight of the last classifier.
	 ****************** 
	 */
	public void adjustWeights(boolean[] paraCorrectArray, double paraAlpha) {
		// Step 2. Calculate alpha.
		double tempIncrease = Math.exp(paraAlpha);

		// Step 3. Adjust.
		double tempWeightsSum = 0; // For normalization.
		for (int i = 0; i < weights.length; i++) {
			if (paraCorrectArray[i]) {
				weights[i] /= tempIncrease;
			} else {
				weights[i] *= tempIncrease;
			} // Of if
			tempWeightsSum += weights[i];
		} // Of for i

		// Step 4. Normalize.
		for (int i = 0; i < weights.length; i++) {
			weights[i] /= tempWeightsSum;
		} // Of for i
		
		//SimpleTools.variableTrackingOutput("The current weights are: " + Arrays.toString(weights));
	} // Of adjustWeights

	/**
	 ****************** 
	 * Test the method.
	 ****************** 
	 */
	public void adjustWeightsTest() {
		boolean[] tempCorrectArray = new boolean[numInstances()];
		for (int i = 0; i < tempCorrectArray.length / 2; i++) {
			tempCorrectArray[i] = true;
		} // Of for i

		double tempWeightedError = 0.3;

		adjustWeights(tempCorrectArray, tempWeightedError);

		System.out.println("After adjusting");

		System.out.println(toString());
	} // Of adjustWeightsTest

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a weighted Instances object.\r\n" + "I have " + numInstances()
				+ " instances and " + (numAttributes() - 1) + " conditional attributes.\r\n"
				+ "My weights are: " + Arrays.toString(weights) + "\r\n" + "My data are: \r\n"
				+ super.toString();

		return resultString;
	} // Of toString

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
		} catch (Exception exception1) {
			System.out.println("Cannot read the file: " + tempFilename + "\r\n" + exception1);
			System.exit(0);
		} // Of try

		System.out.println(tempWeightedInstances.toString());

		tempWeightedInstances.adjustWeightsTest();
	} // Of main

} // Of class WeightedInstances
