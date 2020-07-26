package gui.guidialog.common;

import java.io.*;

import common.*;

import java.awt.*;
import java.awt.event.*;

import gui.guicommon.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Now that the help message is contained in a text file.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: OK. Copied from Hydrosimu.<br>
 * Written time: March 09, 2011. <br>
 * Last modify time: March 09, 2011.
 */
public class HelpDialog extends Dialog implements ActionListener {
	/**
	 * Serial uid
	 */
	private static final long serialVersionUID = 3869415040299264995L;

	/**
	 * The ONLY help dialog.
	 */
	public static HelpDialog helpDialog = new HelpDialog("Help",
			"src/gui/guidialog/common/generalHelp.txt");

	/**
	 * The ONLY about dialog.
	 */
	public static HelpDialog aboutDialog = new HelpDialog("About",
			"src/gui/guidialog/common/about.txt");

	/**
	 *************************** 
	 * Display the help dialog.
	 * 
	 * @param paramTitle
	 *            the title of the dialog.
	 * @param paramFilename
	 *            the help file.
	 *************************** 
	 */
	public HelpDialog(String paramTitle, String paramFilename) {
		//
		super(GUICommon.mainFrame, paramTitle, true);
		setBackground(GUICommon.MY_COLOR);

		TextArea displayArea = new TextArea("", 10, 10,
				TextArea.SCROLLBARS_VERTICAL_ONLY);
		displayArea.setEditable(false);
		String textToDisplay = "";
		try {
			RandomAccessFile helpFile = new RandomAccessFile(paramFilename, "r");
			String tempLine = helpFile.readLine();
			while (tempLine != null) {
				textToDisplay = textToDisplay + tempLine + "\n";
				tempLine = helpFile.readLine();
			}
			helpFile.close();
		} catch (IOException ee) {
			dispose();
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
		}
		textToDisplay = SimpleTools.GB2312ToUNICODE(textToDisplay);
		displayArea.setText(textToDisplay);
		displayArea.setFont(new Font("Times New Romans", Font.PLAIN, 14));

		Button okButton = new Button("OK");
		okButton.setSize(20, 10);
		okButton.addActionListener(new DialogCloser(this));
		Panel okPanel = new Panel();
		okPanel.setLayout(new FlowLayout());
		okPanel.add(okButton);

		// OK Button
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, displayArea);
		add(BorderLayout.SOUTH, okPanel);

		setLocation(120, 70);
		setSize(500, 400);
		addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	/**
	 ************************* 
	 * Simply set it visible.
	 ************************* 
	 */
	public void actionPerformed(ActionEvent ee) {
		setVisible(true);
	}// Of actionPerformed.
}// Of class HelpDialog
