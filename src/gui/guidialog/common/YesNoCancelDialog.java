package gui.guidialog.common;

import java.awt.*;
import java.awt.event.*;

import gui.guicommon.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Choose yes, no, or cancel.
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
public class YesNoCancelDialog extends Dialog {
	/**
	 * Serail UID.
	 */
	private static final long serialVersionUID = 4094155271984291675L;

	public static YesNoCancelDialog yesNoCancelDialog = new YesNoCancelDialog(
			"title", "message");

	/**
	 * YES
	 */
	public static final int YES = 0;

	/**
	 * NO
	 */
	public static final int NO = 1;

	/**
	 * CANCEL
	 */
	public static final int CANCEL = 2;

	/**
	 * Message displayed in the dialog
	 */
	String textToDisplay;

	private int choice = CANCEL;

	private Dialog currentDialog;

	private Label messageLabel;

	/**
	 *************************** 
	 * 显示是/否选择对话框，以及相关的信息
	 * 
	 * @param paramTitle
	 *            对话主题.
	 * @param paramText
	 *            相关的信息
	 *************************** 
	 */
	private YesNoCancelDialog(String paramTitle, String paramText) {
		// This dialog is not module
		super(GUICommon.mainFrame, paramTitle, true);
		currentDialog = this;

		// Prepare the text and dialog.
		textToDisplay = paramText;
		messageLabel = new Label(textToDisplay);

		Panel southPanel = new Panel();
		southPanel.setLayout(new GridLayout(1, 3));

		Button yesButton = new Button("  Yes  ");
		yesButton.setSize(30, 10);
		yesButton.addActionListener(new ButtonListener(YES));

		Button noButton = new Button("  No  ");
		noButton.setSize(30, 10);
		noButton.addActionListener(new ButtonListener(NO));

		Button cancelButton = new Button("Cancel");
		cancelButton.setSize(30, 10);
		cancelButton.addActionListener(new ButtonListener(CANCEL));

		Panel yesNoCancelPanel = new Panel();
		yesNoCancelPanel.setLayout(new FlowLayout());
		yesNoCancelPanel.add(yesButton);
		yesNoCancelPanel.add(noButton);
		yesNoCancelPanel.add(cancelButton);

		// 加入Label与Button
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, messageLabel);
		add(BorderLayout.SOUTH, yesNoCancelPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(300, 200);
		setSize(300, 100);
		addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * 获取用户的选择.
	 * 
	 * @return 用户的选择,YES或NO
	 *************************** 
	 */
	public int getChoice() {
		return choice;
	}

	/**
	 *************************** 
	 * 设置用户的选择,并关闭本对话框.由于其代码简单,所以作为内嵌类.
	 *************************** 
	 */
	private class ButtonListener implements ActionListener {
		private int innerChoose;

		ButtonListener(int paramChoose) {
			innerChoose = paramChoose;
		}

		public void actionPerformed(ActionEvent ee) {
			choice = innerChoose;
			currentDialog.dispose();
		}
	}// Of ButtonListener

	/**
	 *************************** 
	 * set title and message.
	 * 
	 * @param paramTitle
	 *            the new title
	 * @param paramMessage
	 *            the new message
	 *************************** 
	 */
	public void setTitleAndMessageAndShow(String paramTitle, String paramMessage) {
		setTitle(paramTitle);
		messageLabel.setText(paramMessage);

		// Prepare for closing the dialog directly.
		choice = CANCEL;

		setVisible(true);
	}// Of setTitleAndMessageAndShow

}// Of class YesNoCancelDialog
