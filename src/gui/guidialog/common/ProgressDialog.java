/*
 * @(#)ProgressDialog.java
 *
 */

package gui.guidialog.common;

import java.awt.*;

import gui.guicommon.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Display the progress.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: OK. Copied from Hydrosimu.<br>
 * Written time: March 10, 2011. <br>
 * Last modify time: March 10, 2011.
 */
public class ProgressDialog extends Dialog {

	/**
	 * Serail UID.
	 */
	private static final long serialVersionUID = -2243735738817185629L;

	/**
	 * The ONLY ProgressDialog.
	 */
	public static ProgressDialog progressDialog = new ProgressDialog();

	/**
	 * The label containing the message to display.
	 */
	private TextArea messageTextArea;

	/**
	 *************************** 
	 * Display an error dialog and respective error message. Like other dialogs,
	 * this constructor is private, such that users can use only one dialog,
	 * i.e., ProgressDialog.progressDialog to display message. This is helpful
	 * for saving space (only one dialog) since we may need "many" dialogs.
	 *************************** 
	 */
	private ProgressDialog() {
		// This dialog is not module.
		super(GUICommon.mainFrame, "Processing", false);

		// Prepare for the dialog.
		messageTextArea = new TextArea();

		// Add TextArea and Button
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, messageTextArea);

		setLocation(200, 200);
		setSize(500, 200);
		addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * Set message.
	 * 
	 * @param paramMessage
	 *            the new message
	 *************************** 
	 */
	public void setMessageAndShow(String paramMessage) {
		messageTextArea.setText(paramMessage);

		setVisible(true);
	}// Of setMessageAndShow

	/**
	 *************************** 
	 * Append message.
	 * 
	 * @param paramMessage
	 *            the new message
	 *************************** 
	 */
	public void appendMessage(String paramMessage) {
		messageTextArea.append(paramMessage);
	}// Of appendMessage

	/**
	 *************************** 
	 * To display more progress has been made. May be replaced by other
	 * approaches with better visualization.
	 *************************** 
	 */
	public void moreProgress() {
		messageTextArea.append("...");
	}// Of appendMessage

}// Of class ProgressDialog
