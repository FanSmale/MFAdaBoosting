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
 * Date Created: July 18, 2020.<br>
 *       Last modified: July 19, 2020.
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
	double[] weights;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraInstances
	 *            The given instance.
	 ****************** 
	 */
	public WeightedInstances(Instances paraInstances) {
		super(paraInstances);

		// Initialize weights
		weights = new double[numInstances()];
		double tempAverage = 1.0 / numInstances();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = tempAverage;
		} // Of for i
	}// Of the first constructor

	/**
	 ****************** 
	 * The second constructor.
	 * 
	 * @param paraInstances
	 *            The given instance.
	 * @param paraWeights
	 *            Weights determined by the last classifier.
	 * @param paraCorrectnessArray
	 *            The correctness array determined by the last classifier.
	 * @param paraWeightedError
	 *            The weighted error determined by the last classifier.
	 ****************** 
	 */
	public WeightedInstances(Instances paraInstances, double[] paraWeights,
			boolean[] paraCorrectnessArray, double paraWeightedError) {
		super(paraInstances);

		// The weights are copied one by one.
		weights = new double[numInstances()];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = paraWeights[i];
		} // Of for i

		// Adjust weights for new training.
		adjustWeights(paraCorrectnessArray, paraWeightedError);
	}// Of the second constructor

	/**
	 ****************** 
	 * Setter.
	 * 
	 * @param paraWeights
	 *            The given weights.
	 ****************** 
	 */
	public void setWeights(double[] paraWeights) {
		weights = paraWeights;
	}// Of setWeights

	/**
	 ****************** 
	 * Getter.
	 * 
	 * @return The weights.
	 ****************** 
	 */
	public double[] getWeights() {
		return weights;
	}// Of getWeights

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
	}// Of getWeight

	/**
	 ****************** 
	 * Adjust the weights.
	 * 
	 * @param paraCorrectArray
	 *            Indicate which instances have been correctly classified.
	 ****************** 
	 */
	public void adjustWeights(boolean[] paraCorrectArray, 
			double paraWeightedError) {
		//Step 1. Avoid overflow.
		if (paraWeightedError < 1e-6) {
			paraWeightedError = 1e-6;
		}//Of if
		
		// Step 2. Calculate alpha.
		double tempAlpha = 0.5 * Math.log((1 - paraWeightedError) / paraWeightedError);
		double tempIncrease = Math.exp(tempAlpha);
		
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
	}// Of adjustWeights

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a weighted instances object.\r\n" + "I have "
				+ numInstances() + " instances and " + (numAttributes() - 1) 
				+ " conditional attributes.\r\n" + "My weights is: " 
				+ Arrays.toString(weights) + "\r\n" + "My data is: \r\n"
				+ super.toString();

		return resultString;
	}// Of toString

	/**
	 ****************** 
	 * For unit test.
	 * @param args Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		Instances tempInstances = null;
		String tempFilename = "src/data/iris.arff";
		try {
			FileReader tempFileReader = new FileReader(tempFilename);
			tempInstances = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + tempFilename + "\r\n" + ee);
			System.exit(0);
		} // Of try

		WeightedInstances tempWeightedInstances = new WeightedInstances(tempInstances);
		System.out.println(tempWeightedInstances);
	}// Of main

}// Of class WeightedInstances
