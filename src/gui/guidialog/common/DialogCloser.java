/*
 * @(#)DialogCloser.java
 *
 */

package gui.guidialog.common;

import java.awt.*;
import java.awt.event.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Close the dialog. The message sender could be either a button or X
 * at the right top of respective dialog.
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
public class DialogCloser extends WindowAdapter implements ActionListener {

	/**
	 * The dialog under control.
	 */
	private Dialog currentDialog;

	/**
	 *************************** 
	 * As a WindowListener.
	 *************************** 
	 */
	public DialogCloser() {
		super();
	}// Of constructor without parameter

	/**
	 *************************** 
	 * As an ActionListener.
	 * 
	 * @param comeInDialog
	 *            the dialog under control
	 *************************** 
	 */
	public DialogCloser(Dialog comeInDialog) {
		currentDialog = comeInDialog;
	}// Of constructor with one parameter

	/**
	 *************************** 
	 * Close the dialog.
	 * 
	 * @param comeInWindowEvent
	 *            From it we can obtain which window sent the message because X
	 *            was used.
	 *************************** 
	 */
	public void windowClosing(WindowEvent comeInWindowEvent) {
		// Close.
		(comeInWindowEvent.getWindow()).dispose();
	}// Of windowClosing.

	/**
	 *************************** 
	 * 释放对话框.
	 * 
	 * @param comeInEvent
	 *            发生的事件,它不被程序所分析.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent comeInEvent) {
		// Close.
		currentDialog.dispose();
	}// Of actionPerformed.
}// Of class DialogCloser
