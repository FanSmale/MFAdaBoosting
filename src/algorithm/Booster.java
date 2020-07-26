package algorithm;

import java.io.FileReader;

import weka.core.Instance;
import weka.core.Instances;

import common.SimpleTools;

/**
 * The booster which ensembles base classifiers. <br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Data Created: July 18, 2020.<br>
 *         Last modified: July 19, 2020.
 * @version 1.0
 */

public class Booster {
	/**
	 * The training testing scheme: split in two.
	 */
	public static final int SPLIT_IN_TWO = 0;

	/**
	 * The training testing scheme: use training set.
	 */
	public static final int USE_TRAINING_SET = 1;

	/**
	 * The training testing scheme: specify testing set.
	 */
	public static final int SPECIFY_TESTING_SET = 2;

	/**
	 * The training testing scheme.
	 */
	int trainingTestingScheme;

	/**
	 * Classifiers.
	 */
	StumpClassifier[] classifiers;

	/**
	 * Number of classifiers.
	 */
	int numClassifiers;

	/**
	 * Whether or not stop after converge.
	 */
	boolean stopAfterConverge = false;

	/**
	 * The weights of classifiers.
	 */
	double[] classifierWeights;

	/**
	 * The training data.
	 */
	Instances trainingData;

	/**
	 * The testing data.
	 */
	Instances testingData;

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraTrainingFilename
	 *            The data filename.
	 ****************** 
	 */
	public Booster(String paraTrainingFilename) {
		// Step 1. Read training set.
		try {
			FileReader tempFileReader = new FileReader(paraTrainingFilename);
			trainingData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraTrainingFilename + "\r\n" + ee);
			System.exit(0);
		} // Of try

		// Step 2. Set the last attribute as the class index.
		trainingData.setClassIndex(trainingData.numAttributes() - 1);

		// Step 5. The testing data is the same as the training data.
		testingData = trainingData;
		trainingTestingScheme = USE_TRAINING_SET;
	}// Of the first constructor

	/**
	 ****************** 
	 * The second constructor.
	 * 
	 * @param paraTrainingFilename
	 *            The data filename.
	 * @param paraMaxClassifiers
	 *            The maximal number of classifiers.
	 ****************** 
	 */
	public Booster(String paraTrainingFilename, String paraTestingFilename) {
		// Step 1. Read the training set.
		this(paraTrainingFilename);

		// Step 2. Read training set.
		try {
			FileReader tempFileReader = new FileReader(paraTestingFilename);
			testingData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraTestingFilename + "\r\n" + ee);
			System.exit(0);
		} // Of try

		// Step 3. Set the last attribute as the class index.
		testingData.setClassIndex(testingData.numAttributes() - 1);

		// Step 4. Change the scheme.
		trainingTestingScheme = SPECIFY_TESTING_SET;
	}// Of the second constructor

	/**
	 ****************** 
	 * The first constructor.
	 * 
	 * @param paraFilename
	 *            The data filename.
	 * @param paraTrainingFraction
	 *            The fraction of the training set.
	 ****************** 
	 */
	public Booster(String paraFilename, double paraTrainingFraction) {
		// Step 1. Read data.
		Instances tempWholeData = null;
		try {
			FileReader tempFileReader = new FileReader(paraFilename);
			tempWholeData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraFilename + "\r\n" + ee);
			System.exit(0);
		} // Of try

		// Step 2. Split in two
		Instances[] tempDataArray = SimpleTools.splitInTwo(tempWholeData, paraTrainingFraction);

		trainingData = tempDataArray[0];
		testingData = tempDataArray[1];

		// Step 3. Set the last attribute as the class index.
		trainingData.setClassIndex(trainingData.numAttributes() - 1);
		testingData.setClassIndex(testingData.numAttributes() - 1);

		// Step 4. The testing data is the same as the training data.
		trainingTestingScheme = SPLIT_IN_TWO;
	}// Of the third constructor

	/**
	 ****************** 
	 * Set the number of base classifier, and allocate space for them.
	 * 
	 * @param paraNumBaseClassifiers
	 *            The number of base classifier.
	 ****************** 
	 */
	public void setNumBaseClassifiers(int paraNumBaseClassifiers) {
		numClassifiers = paraNumBaseClassifiers;

		// Step 1. Allocate space (only reference) for classifiers
		classifiers = new StumpClassifier[numClassifiers];

		// Step 2. Initialize classifier weights.
		classifierWeights = new double[numClassifiers];
	}// Of setNumBaseClassifiers

	/**
	 ****************** 
	 * Setter.
	 * 
	 * @param paraBoolean
	 *            The given boolean value.
	 ****************** 
	 */
	public void setStopAfterConverge(boolean paraBoolean) {
		stopAfterConverge = paraBoolean;
	}// Of setStopAfterConverge

	/**
	 ****************** 
	 * Train the booster.
	 ****************** 
	 */
	public void train() {
		// Step 1. Build the first classifier.
		WeightedInstances tempWeightedInstances = new WeightedInstances(trainingData);
		classifiers[0] = new StumpClassifier(tempWeightedInstances);
		classifiers[0].train();
		double tempAccuracy = classifiers[0].computeTrainingAccuracy();
		double tempError = classifiers[0].computeWeightedError();
		classifierWeights[0] = 0.5 * Math.log(1 / tempError - 1);
		numClassifiers = 1;
		SimpleTools.variableTrackingOutput(
				"Classifier #" + 0 + " accuray = " + tempAccuracy + ", weighted error = "
						+ tempError + ", weight = " + classifierWeights[0] + "\r\n");

		// Step 2. Build other classifiers.
		for (int i = 1; i < classifiers.length; i++) {
			tempWeightedInstances = new WeightedInstances(trainingData,
					classifiers[i - 1].getWeights(), classifiers[i - 1].computeCorrectnessArray(),
					classifiers[i - 1].computeWeightedError());
			classifiers[i] = new StumpClassifier(tempWeightedInstances);
			classifiers[i].train();
			tempAccuracy = classifiers[i].computeTrainingAccuracy();
			tempError = classifiers[i].computeWeightedError();
			classifierWeights[i] = 0.5 * Math.log(1 / tempError - 1);

			SimpleTools.variableTrackingOutput(
					"Classifier #" + i + " accuray = " + tempAccuracy + ", weighted error = "
							+ tempError + ", weight = " + classifierWeights[i] + "\r\n");

			numClassifiers++;

			double tempTrainingAccuracy = computeTrainingAccuray();
			SimpleTools.variableTrackingOutput(
					"The accuracy of the booster is: " + tempTrainingAccuracy + "\r\n");

			// The accuracy is enough.
			if (stopAfterConverge) {
				if (tempTrainingAccuracy > 0.999999) {
					SimpleTools.processTrackingOutput(
							"Stop at the round: " + i + " due to converge.\r\n");
					break;
				} // Of if
			} // Of if
		} // Of for i
	}// Of train

	/**
	 ****************** 
	 * Classify an instance.
	 * 
	 * @param paraInstance
	 *            The given instance.
	 * @return The predicted label.
	 ****************** 
	 */
	public int classify(Instance paraInstance) {
		double[] tempLabelsCountArray = new double[trainingData.classAttribute().numValues()];
		for (int i = 0; i < numClassifiers; i++) {
			int tempLabel = classifiers[i].classify(paraInstance);
			tempLabelsCountArray[tempLabel] += classifierWeights[i];
		} // Of for i

		int resultLabel = -1;
		double tempMax = -1;
		for (int i = 0; i < tempLabelsCountArray.length; i++) {
			if (tempMax < tempLabelsCountArray[i]) {
				tempMax = tempLabelsCountArray[i];
				resultLabel = i;
			} // Of if
		} // Of for

		return resultLabel;
	}// Of classify

	/**
	 ****************** 
	 * Test the booster on the training data.
	 * 
	 * @return The classification accuracy.
	 ****************** 
	 */
	public double test() {
		SimpleTools.processTrackingOutput(
				"Testing on " + testingData.numInstances() + " instances.\r\n");
		return test(testingData);
	}// Of test

	/**
	 ****************** 
	 * Test with the given data.
	 * 
	 * @param paraFilename
	 *            The testing data filename.
	 * @return The classification accuracy.
	 ****************** 
	 */
	public double test(String paraFilename) {
		// Step 1. Read data.
		try {
			FileReader tempFileReader = new FileReader(paraFilename);
			testingData = new Instances(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraFilename + "\r\n" + ee);
			System.exit(0);
		} // Of try

		// Step 2. Set the last attribute as the class index.
		testingData.setClassIndex(testingData.numAttributes() - 1);
		return test(trainingData);
	}// Of test

	/**
	 ****************** 
	 * Test the booster.
	 * 
	 * @param paraInstances
	 *            The testing set.
	 * @return The classification accuracy.
	 ****************** 
	 */
	public double test(Instances paraInstances) {
		double tempCorrect = 0;
		paraInstances.setClassIndex(paraInstances.numAttributes() - 1);

		for (int i = 0; i < paraInstances.numInstances(); i++) {
			Instance tempInstance = paraInstances.instance(i);
			if (classify(tempInstance) == (double) tempInstance.classValue()) {
				tempCorrect++;
			} // Of if
		} // Of for i

		return tempCorrect / paraInstances.numInstances();
	}// Of test

	/**
	 ****************** 
	 * Compute the training accuracy of the booster.
	 * 
	 * @return The training accuracy.
	 ****************** 
	 */
	public double computeTrainingAccuray() {
		double tempCorrect = 0;

		for (int i = 0; i < trainingData.numInstances(); i++) {
			if (classify(trainingData.instance(i)) == (int) trainingData.instance(i).classValue()) {
				tempCorrect++;
			} // Of if
		} // Of for i

		double tempAccuracy = tempCorrect / trainingData.numInstances();

		return tempAccuracy;
	}// Of computeTrainingAccuray

	/**
	 ****************** 
	 * For integration test.
	 * 
	 * @param args
	 *            Not provided.
	 ****************** 
	 */
	public static void main(String args[]) {
		System.out.println("Starting AdaBoosting...");
		Booster tempBooster = new Booster("src/data/wdbc_norm_ex.arff");
		// Booster tempBooster = new Booster("src/data/iris.arff", 200);

		tempBooster.setNumBaseClassifiers(80);
		tempBooster.train();

		System.out.println("The training accuracy is: " + tempBooster.computeTrainingAccuray());
	}// Of main

}// Of class Booster
