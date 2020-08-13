package algorithm;

import weka.core.Instance;
import java.io.FileReader;
import java.util.*;

import common.Common;

/**
 * The Gaussian classifier consider only one attribute.<br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: July 18, 2020.<br>
 *         Last modified: July 19, 2020.
 * @version 1.0
 */

public class GaussianClassifier extends SimpleClassifier {

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
	public GaussianClassifier(WeightedInstances paraWeightedInstances) {
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
		calculateGaussianParameters();
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
			double tempAttributeValue = paraInstance.value(selectedAttribute);
			double tempSigma = gaussianParameters[i].sigma;
			double tempMu = gaussianParameters[i].mu;

			double tempPseudoProbability = Math.log(classDistributionLaplacian[i]) 
					- Math.log(tempSigma) 
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

		//System.out.println("Class distribution: " +
		// Arrays.toString(classDistribution));
		//System.out.println(
		//"Class distribution Laplacian: " + Arrays.toString(classDistributionLaplacian));
	}// Of calculateClassDistribution

	/**
	 ********************
	 * Calculate the conditional probabilities with Laplacian smooth.
	 ********************
	 */
	public void calculateGaussianParameters() {
		gaussianParameters = new GaussianParamters[numClasses];
		
		double[] tempMuArray = new double[numClasses];
		double[] tempSigmaSumArray = new double[numClasses];
		double[] tempSigmaArray = new double[numClasses];
		
		double[] tempValueSumArray = new double[numClasses];
		double[] tempWeightSumArray = new double[numClasses];
		
		//Step 1. Calculate mean attribute value for each class.
		for (int i = 0; i < numInstances; i++) {
			int tempClass = (int) weightedInstances.instance(i).classValue();
			
			tempValueSumArray[tempClass] += weightedInstances.instance(i)
					.value(selectedAttribute) * weightedInstances.getWeight(i);
			tempWeightSumArray[tempClass] += weightedInstances.getWeight(i);
		}//Of for i
		
		for (int i = 0; i < numClasses; i++) {
			tempMuArray[i] = tempValueSumArray[i] / tempWeightSumArray[i];
		} //Of for i
		
		//Step 2. Calculate sigma for each class.
		for (int i = 0; i < numInstances; i++) {
			int tempClass = (int) weightedInstances.instance(i).classValue();
			
			double tempDifference = weightedInstances.instance(i)
					.value(selectedAttribute) - tempMuArray[tempClass];
			tempSigmaSumArray[tempClass] += tempDifference * tempDifference;
		}//Of for i
		
		//Step 3. Calculate
		for (int i = 0; i < numClasses; i++) {
			tempSigmaArray[i] = Math.sqrt(tempSigmaSumArray[i] / tempWeightSumArray[i] / numInstances);
			gaussianParameters[i] = new GaussianParamters(tempMuArray[i], tempSigmaArray[i]);
		}//Of for i
		
		//System.out.println("Gaussian parameters: " +
		//Arrays.deepToString(gaussianParameters));
	}// Of calculateGaussianParameters

	/**
	 ****************** 
	 * For display.
	 ****************** 
	 */
	public String toString() {
		String resultString = "I am a Gaussian classifier.\r\n" + "I choose attribute #"
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

		GaussianClassifier tempClassifier = new GaussianClassifier(tempWeightedInstances);
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
			//sigma = 0.1;
		}// Of the constructor

		public String toString() {
			return "(" + mu + ", " + sigma + ")";
		}// Of toString
	}// Of GaussianParamters
} // Of class GaussianClassifier
