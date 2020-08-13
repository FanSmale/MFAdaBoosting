package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.JComboBox;

import algorithm.*;
import common.*;
import gui.guicommon.*;
import gui.guidialog.common.HelpDialog;
import gui.others.*;

/**
 * The GUI for experimentation.<br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: July 20, 2020.<br>
 *         Last modified: July 20, 2020.
 * @version 1.0
 */

public class MabGUI implements ActionListener, ItemListener, TextListener {
	
	/**
	 * The properties for setting.
	 */
	private Properties settings = new Properties();

	/**
	 * Select the training data.
	 */
	private FilenameField trainingFilenameField;

	/**
	 * Select the testing data.
	 */
	private FilenameField testingFilenameField;

	/**
	 * Training/testing scheme.
	 */
	private JComboBox<String> trainingTestingSchemeComboBox;

	/**
	 * Training fraction.
	 */
	private DoubleField trainingFractionField;

	/**
	 * Number of base classifiers.
	 */
	private IntegerField numBaseClassifiersField;

	/**
	 * Base classifier type.
	 */
	private JComboBox<String> baseClassifierTypeComboBox;

	/**
	 * Stop when the error reaches 0.
	 */
	private Checkbox stopAfterConvergeCheckbox;

	/**
	 * Checkbox for variable tracking.
	 */
	private Checkbox processTrackingCheckbox;

	/**
	 * Checkbox for variable tracking.
	 */
	private Checkbox variableTrackingCheckbox;

	/**
	 * Result output to file checkbox.
	 */
	private Checkbox fileOutputCheckbox;

	/**
	 * The message area.
	 */
	private TextArea messageTextArea;

	/**
	 * How many times to repeat.
	 */
	private IntegerField repeatTimesField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	public MabGUI() {
		// A simple frame to contain dialogs.
		Frame mainFrame = new Frame();
		mainFrame.setTitle("AdaBoosting. minfan@swpu.edu.cn, minfanphd@163.com");

		// Step 1. The top part: select training/testing data files.
		// Step 1.1 Training part
		Panel trainingFilePanel = new Panel();
		trainingFilenameField = new FilenameField(30);
		trainingFilenameField.setText("src/data/iris.arff");
		trainingFilenameField.addTextListener(this);

		Button trainingBrowseButton = new Button(" Browse ");
		trainingBrowseButton.setSize(20, 10);
		Panel trainingBrowsePanel = new Panel();
		trainingBrowsePanel.add(trainingBrowseButton);
		trainingBrowseButton.addActionListener(trainingFilenameField);

		trainingFilePanel.add(new Label("Training data:"));
		trainingFilePanel.add(trainingFilenameField);
		trainingFilePanel.add(trainingBrowseButton);

		// Step 1.2 Testing part
		Panel testingFilePanel = new Panel();
		testingFilenameField = new FilenameField(30);
		testingFilenameField.setText("src/data/iris.arff");
		testingFilenameField.addTextListener(this);

		Button testingBrowseButton = new Button(" Browse ");
		testingBrowseButton.setSize(20, 10);
		Panel testingBrowsePanel = new Panel();
		testingBrowsePanel.add(testingBrowseButton);
		testingBrowseButton.addActionListener(testingFilenameField);

		testingFilePanel.add(new Label("Testing data:"));
		testingFilePanel.add(testingFilenameField);
		testingFilePanel.add(testingBrowseButton);

		// Step 1.3 Scheme part
		Panel tempSchemePanel = new Panel();
		String[] tempTrainingTestingSchemes = { "Split in two", "Use training set",
				"Specify testing data" };
		trainingTestingSchemeComboBox = new JComboBox<String>(tempTrainingTestingSchemes);
		trainingFractionField = new DoubleField("0.6");
		tempSchemePanel.add(new Label("Training/testing scheme:"));
		tempSchemePanel.add(trainingTestingSchemeComboBox);
		tempSchemePanel.add(new Label("Training fraction"));
		tempSchemePanel.add(trainingFractionField);

		Panel dataPanel = new Panel();
		dataPanel.setLayout(new GridLayout(3, 1));
		dataPanel.add(trainingFilePanel);
		dataPanel.add(testingFilePanel);
		dataPanel.add(tempSchemePanel);

		// Step 2. Settings.
		numBaseClassifiersField = new IntegerField("100");
		String[] tempClassifierTypes = { "Stump", "Bayes" };
		baseClassifierTypeComboBox = new JComboBox<String>(tempClassifierTypes);
		stopAfterConvergeCheckbox = new Checkbox("Stop after converge");
		Panel settingPanel = new Panel();
		settingPanel.add(new Label("Number of base classifiers:"));
		settingPanel.add(numBaseClassifiersField);
		settingPanel.add(numBaseClassifiersField);
		settingPanel.add(new Label("Base classifier type:"));
		settingPanel.add(baseClassifierTypeComboBox);
		settingPanel.add(stopAfterConvergeCheckbox);

		processTrackingCheckbox = new Checkbox(" Process tracking ", false);
		variableTrackingCheckbox = new Checkbox(" Variable tracking ", false);
		fileOutputCheckbox = new Checkbox(" Output to file ", false);
		Panel trackingPanel = new Panel();
		trackingPanel.add(processTrackingCheckbox);
		trackingPanel.add(variableTrackingCheckbox);
		trackingPanel.add(fileOutputCheckbox);

		Panel topPanel = new Panel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add("North", dataPanel);
		topPanel.add("Center", settingPanel);
		topPanel.add("South", trackingPanel);

		Panel centralPanel = new Panel();
		messageTextArea = new TextArea(30, 80);
		centralPanel.add(messageTextArea);

		// The bottom part: ok and exit
		repeatTimesField = new IntegerField("5");
		Panel repeatTimesPanel = new Panel();
		repeatTimesPanel.add(new Label(" Repeat times: "));
		repeatTimesPanel.add(repeatTimesField);

		Button okButton = new Button(" OK ");
		okButton.addActionListener(this);
		// DialogCloser dialogCloser = new DialogCloser(this);
		Button exitButton = new Button(" Exit ");
		// cancelButton.addActionListener(dialogCloser);
		exitButton.addActionListener(ApplicationShutdown.applicationShutdown);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("AdaBoosting", "src/gui/MabHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(exitButton);
		okPanel.add(helpButton);

		Panel southPanel = new Panel();
		southPanel.setLayout(new GridLayout(2, 1));
		southPanel.add(repeatTimesPanel);
		southPanel.add(okPanel);

		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(BorderLayout.NORTH, topPanel);
		mainFrame.add(BorderLayout.CENTER, centralPanel);
		mainFrame.add(BorderLayout.SOUTH, southPanel);

		mainFrame.setSize(700, 500);
		mainFrame.setLocation(10, 10);
		mainFrame.addWindowListener(ApplicationShutdown.applicationShutdown);
		mainFrame.setBackground(GUICommon.MY_COLOR);
		mainFrame.setVisible(true);
	}// Of the constructor

	/**
	 *************************** 
	 * Read the arff file.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		Common.startTime = new Date().getTime();
		messageTextArea.setText("Processing ... Please wait.\r\n");

		// Parameters to be transferred to respective objects.
		String tempTrainingFilename = trainingFilenameField.getText().trim();
		String tempTestingFilename = testingFilenameField.getText().trim();

		int tempNumBaseClassifiers = numBaseClassifiersField.getValue();
		int tempBaseClassifierType = baseClassifierTypeComboBox.getSelectedIndex();
		boolean tempStopAfterConverge = stopAfterConvergeCheckbox.getState();
		double tempTrainingFraction = trainingFractionField.getValue();

		int tempRepeatTimes = repeatTimesField.getValue();

		SimpleTools.processTracking = processTrackingCheckbox.getState();
		SimpleTools.variableTracking = variableTrackingCheckbox.getState();
		SimpleTools.fileOutput = fileOutputCheckbox.getState();

		String tempParametersInformation = "Dataset information: training set filename: "
				+ tempTrainingFilename + "\r\n  " + "Number of base classifiers = "
				+ tempNumBaseClassifiers;
		messageTextArea.append(tempParametersInformation);

		double tempMinAccuracy = 1;
		double tempMaxAccuracy = 0;
		double tempAccuracySum = 0;

		for (int i = 0; i < tempRepeatTimes; i++) {
			// Read the data here.
			Booster tempBooster = null;
			int tempScheme = trainingTestingSchemeComboBox.getSelectedIndex();
			switch (tempScheme) {
			case Booster.SPLIT_IN_TWO:
				tempBooster = new Booster(tempTrainingFilename, tempTrainingFraction);
				break;
			case Booster.USE_TRAINING_SET:
				tempBooster = new Booster(tempTrainingFilename);
				break;
			case Booster.SPECIFY_TESTING_SET:
				tempBooster = new Booster(tempTrainingFilename, tempTestingFilename);
				break;
			default:
				System.out.println("Unsupported training-testing scheme: " + tempScheme);
				System.exit(0);
			}// Of switch
			tempBooster.setNumBaseClassifiers(tempNumBaseClassifiers);
			tempBooster.setBaseClassifierType(tempBaseClassifierType);
			tempBooster.setStopAfterConverge(tempStopAfterConverge);

			tempBooster.train();
			double tempAccuracy = tempBooster.test();
			if (tempMinAccuracy > tempAccuracy) {
				tempMinAccuracy = tempAccuracy;
			} // Of if

			if (tempMaxAccuracy < tempAccuracy) {
				tempMaxAccuracy = tempAccuracy;
			} // Of if

			tempAccuracySum += tempAccuracy;
		} // Of for i

		messageTextArea.append("\r\nSummary:\r\n");
		messageTextArea.append("Min accuracy: " + tempMinAccuracy + "\r\n");
		messageTextArea.append("Max accuracy: " + tempMaxAccuracy + "\r\n");
		messageTextArea.append("Average accuracy: " + (tempAccuracySum / tempRepeatTimes) + "\r\n");

		Common.endTime = new Date().getTime();
		long tempTimeUsed = Common.endTime - Common.startTime;
		messageTextArea.append("Runtime: " + tempTimeUsed + "\r\n");

		messageTextArea.append("\r\nEnd.");
	} // Of actionPerformed

	/**
	 *************************** 
	 * When the checkbox is selected or deselected.
	 *************************** 
	 */
	public void itemStateChanged(ItemEvent paraEvent) {
	} // Of itemStateChanged

	/**
	 *************************** 
	 * Read properties to settings.
	 *************************** 
	 */
	public void textValueChanged(TextEvent paraEvent) {
		String tempPropertyFilename = "";
		String tempFilename = trainingFilenameField.getText().trim();
		if (tempFilename.indexOf("iris") > 0) {
			tempPropertyFilename = "src/properties/iris.properties";
		} else if (tempFilename.toLowerCase().indexOf("wdbc") > 0) {
			tempPropertyFilename = "src/properties/wdbc.properties";
		} else {
			System.out.println("Unknown dataset.");
			return;
		} // Of if

		try {
			InputStream tempInputStream = new BufferedInputStream(
					new FileInputStream(tempPropertyFilename));
			settings.load(tempInputStream);

			numBaseClassifiersField.setText(settings.getProperty("classifiers"));
			String tempString = settings.getProperty("stopafterconverge");
			stopAfterConvergeCheckbox.setState(Boolean.parseBoolean(tempString));
		} catch (Exception ee) {
			System.out.println("Error occurred while reading properties: " + ee);
		} // Of try
	} // Of textValueChanged

	/**
	 *************************** 
	 * The entrance method.
	 * 
	 * @param args
	 *            The parameters.
	 *************************** 
	 */
	public static void main(String args[]) {
		new MabGUI();
	} // Of main
} // Of class MabGUI
