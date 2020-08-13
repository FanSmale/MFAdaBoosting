package algorithm;

import weka.core.Instance;
import java.io.FileReader;
import java.util.*;

import common.Common;

/**
 * The Bayes classifier consider only one attribute.<br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: July 18, 2020.<br>
 *         Last modified: July 19, 2020.
 * @version 1.0
 */

public class BayesClassifier extends SimpleClassifier {

	/**
	 * Class distribution.
	 */
	double[] classDistribution;

	/**
	 * Class distribution with Laplacian smooth.
	 */
	double[] classDistributionLaplacian;

	/**
	 * The Guassian parameters.
	 */
	GaussianParamters[] gaussianParameters;

	/**
	 ****************** 
	 * The only constructor.
	 * 
	 * @param paraWeightedInstances
	 *            The given instances.
	 ****************** 
	 */
	public BayesClassifier(WeightedInstances paraWeightedInstances) {
		super(paraWeightedInstances);
	}// Of the only constructor

	/**
	 ****************** 
	 * Train the classifier.
	 ****************** 
	 */
	public void train() {
		// Step 1. Randomly choose an attribute.
		selectedAttribute = Common.random.nextInt(weightedInstances.numAttributes() - 1);

		// Step 2. Calculate class distribution.
		calculateClassDistribution();

		// Step 3. Calculate Gausssian parameters.
		calculateGausssianParameters();
	} // Of train

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
		// Find the biggest one
		double tempBiggest = -10000;
		int resultBestIndex = 0;
		//double tempSqrt2Pi = Math.log(2 * Math.PI) / 2;
		for (int i = 0; i < numClasses; i++) {
			double tempPseudoProbability = Math.log(classDistributionLaplacian[i]);

			double tempAttributeValue = paraInstance.value(selectedAttribute);
			double tempSigma = gaussianParameters[i].sigma;
			double tempMu = gaussianParameters[i].mu;

			tempPseudoProbability += - Math.log(tempSigma)
					- (tempAttributeValue - tempMu) * (tempAttributeValue - tempMu)
							/ (2 * tempSigma * tempSigma);

			if (tempBiggest < tempPseudoProbability) {
				tempBiggest = tempPseudoProbability;
				resultBestIndex = i;
			} // Of if
		} // Of for i

		return resultBestIndex;
	}// Of classify

	/**
	 ********************
	 * Calculate the class distribution with Laplacian smooth.
	 ********************
	 */
	public void calculateClassDistribution() {
		classDistribution = new double[numClasses];
		classDistributionLaplacian = new double[numClasses];

		double[] tempCounts = new double[numClasses];
		for (int i = 0; i < numInstances; i++) {
			int tempClassValue = (int) weightedInstances.instance(i).classValue();
			tempCounts[tempClassValue] += weightedInstances.getWeight(i) * numInstances;
		} // Of for i

		for (int i = 0; i < numClasses; i++) {
			classDistribution[i] = tempCounts[i] / numInstances;
			classDistributionLaplacian[i] = (tempCounts[i] + 1) / (numInstances + numClasses);
		} // Of for i

		// System.out.println("Class distribution: " +
		// Arrays.toString(classDistribution));
		//System.out.println(
		//		"Class distribution Laplacian: " + Arrays.toString(classDistributionLaplacian));
	}// Of calculateClassDistribution

	/**
	 ********************
	 * Calculate the conditional probabilities with Laplacian smooth.
	 ********************
	 */
	public void calculateGausssianParameters() {
		gaussianParameters = new GaussianParamters[numClasses];

		double[] tempValuesArray = new double[numInstances];
		int tempNumValues = 0;
		double tempSum = 0;
		double tempValueSum = 0;

		for (int i = 0; i < numClasses; i++) {
			tempSum = 0;
			tempValueSum = 0;

			// Obtain values for this class.
			tempNumValues = 0;
			for (int k = 0; k < numInstances; k++) {
				if ((int) weightedInstances.instance(k).classValue() != i) {
					continue;
				} // Of if

				// Changed
				tempValuesArray[tempNumValues] = weightedInstances.instance(k)
						.value(selectedAttribute) * weightedInstances.getWeight(k);
				tempSum += tempValuesArray[tempNumValues];
				tempNumValues++;
				tempValueSum += weightedInstances.getWeight(k);
			} // Of for k

			// Obtain parameters.
			double tempMu = tempSum / tempValueSum;

			double tempSigma = 0;
			for (int k = 0; k < tempNumValues; k++) {
				tempSigma += (tempValuesArray[k] - tempMu) * (tempValuesArray[k] - tempMu);
			} // Of for k
			tempSigma /= tempNumValues;
			tempSigma = Math.sqrt(tempSigma);

			gaussianParameters[i] = new GaussianParamters(tempMu, tempSigma);
		} // Of for i

		// System.out.println("Gaussian parameters: " +
		// Arrays.deepToString(gaussianParameters));
	}// Of calculateGausssianParameters

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a Bayes classifier.\r\n" + "I choose attribute #"
				+ selectedAttribute + ".\r\n" + "My weighted error is: " + computeWeightedError()
				+ ".\r\n" + "My accuracy is : " + computeTrainingAccuracy() + ".";

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

		BayesClassifier tempClassifier = new BayesClassifier(tempWeightedInstances);
		tempClassifier.train();
		System.out.println(tempClassifier);

		System.out.println(Arrays.toString(tempClassifier.computeCorrectnessArray()));
	}// Of main

	/**
	 ************************* 
	 * An inner class to store parameters.
	 ************************* 
	 */
	private class GaussianParamters {
		double mu;
		double sigma;

		public GaussianParamters(double paraMu, double paraSigma) {
			mu = paraMu;
			sigma = paraSigma;
		}// Of the constructor

		public String toString() {
			return "(" + mu + ", " + sigma + ")";
		}// Of toString
	}// Of GaussianParamters
} // Of class BayesClassifier
