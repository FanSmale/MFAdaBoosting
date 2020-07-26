/*
 * @(#)ErrorDialog.java
 *
 */

package gui.guidialog.common;

import java.awt.*;

import gui.guicommon.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Display the error information.
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
public class ErrorDialog extends Dialog {

	/**
	 * A serial uid
	 */
	private static final long serialVersionUID = 124535235L;

	/**
	 * The ONLY ErrorDialog.
	 */
	public static ErrorDialog errorDialog = new ErrorDialog();

	/**
	 * The label containing the message to display.
	 */
	private TextArea messageTextArea;

	/**
	 *************************** 
	 * Display an error dialog and respective error message. Like other dialogs,
	 * this constructor is private, such that users can use only one dialog,
	 * i.e., ErrorDialog.errorDialog to display message. This is helpful for
	 * saving space (only one dialog) since we may need "many" dialogs.
	 *************************** 
	 */
	private ErrorDialog() {
		// This dialog is module.
		super(GUICommon.mainFrame, "Error", true);

		// Prepare for the dialog.
		messageTextArea = new TextArea();

		Button okButton = new Button("OK");
		okButton.setSize(20, 10);
		okButton.addActionListener(new DialogCloser(this));
		Panel okPanel = new Panel();
		okPanel.setLayout(new FlowLayout());
		okPanel.add(okButton);

		// º”»ÎTextArea”ÎButton
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, messageTextArea);
		add(BorderLayout.SOUTH, okPanel);

		setLocation(200, 200);
		setSize(500, 200);
		addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * set message.
	 * 
	 * @param paramMessage
	 *            the new message
	 *************************** 
	 */
	public void setMessageAndShow(String paramMessage) {
		messageTextArea.setText(paramMessage);

		setVisible(true);
	}// Of setTitleAndMessage

}// Of class ErrorDialog
