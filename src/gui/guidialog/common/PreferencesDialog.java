package gui.guidialog.common;

import java.awt.*;
import java.awt.event.*;
import common.*;
import gui.guicommon.*;

/**
 * Title: Upper Yangtze hydrology and reservoir systems experiment benchmark.
 * For settings. Version: 1.0 Copyright: The source code and all documents are
 * open and free. PLEASE keep this header while revising the program. Author:
 * Fan Min minfan@uestc.edu.cn, minfanphd@163.com Organization: Upper Yangtze
 * catchment reservoir systems research group. University of Electronics Science
 * and Technology of China (http://www.uestc.edu.cn), Sichuan University
 * (www.scu.edu.cn). Progress: Currently not used. Written time: Dec. 12, 2008.
 * Last modify time: Dec. 12, 2008.
 */

public class PreferencesDialog extends Dialog implements ActionListener {
	/**
	 * Serial uid
	 */
	private static final long serialVersionUID = -5250526705817437808L;

	public static PreferencesDialog preferencesDialog = new PreferencesDialog();

	private PreferencesDialog currentDialog;

	private Checkbox ifDebugCheckbox = null;

	/**
	 *************************** 
	 * The constructor
	 *************************** 
	 */
	private PreferencesDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Preferences", true);

		currentDialog = this;

		// Prepare all options.
		ifDebugCheckbox = new Checkbox("Debugging");

		Panel preferencesPanel = new Panel();
		preferencesPanel.setLayout(new GridLayout(7, 2));
		preferencesPanel.add(ifDebugCheckbox);

		loadPreferences();

		Button saveButton = new Button(" Save ");
		saveButton.setSize(20, 10);
		saveButton.addActionListener(this);

		Button cancelButton = new Button("Cancel");
		cancelButton.setSize(20, 10);
		DialogCloser dialogCloser = new DialogCloser(this);
		cancelButton.addActionListener(dialogCloser);

		Panel southPanel = new Panel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(saveButton);
		southPanel.add(cancelButton);

		// º”»ÎLabel”ÎButton
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, preferencesPanel);
		add(BorderLayout.SOUTH, southPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(300, 200);
		setSize(400, 250);
		addWindowListener(dialogCloser);
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * Load the user preferences.
	 *************************** 
	 */
	public void loadPreferences() {
		ifDebugCheckbox.setState(Common.ifDebug);
	}// Of loadPreferences

	/**
	 *************************** 
	 * Save the user preference.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		Common.ifDebug = ifDebugCheckbox.getState();
		currentDialog.dispose();
	}// Of actionPerformed

}// Of class PreferencesDialog
