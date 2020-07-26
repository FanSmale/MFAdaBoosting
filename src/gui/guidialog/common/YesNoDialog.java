package gui.guidialog.common;

import java.awt.*;
import java.awt.event.*;

import gui.guicommon.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Choose yes or no.
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
public class YesNoDialog extends Dialog {
	/**
	 * Serail UID.
	 */
	private static final long serialVersionUID = -1263039638032739476L;

	public static YesNoDialog yesNoDialog = new YesNoDialog("title", "message");

	/**
	 * Text to display.
	 */
	String textToDisplay;

	private boolean choice = false;

	private Dialog currentDialog;

	private Label messageLabel;

	/**
	 *************************** 
	 * Construct the dialog.
	 * 
	 * @param paramTitle
	 *            the title.
	 * @param paramText
	 *            the question.
	 *************************** 
	 */
	private YesNoDialog(String paramTitle, String paramText) {
		// This dialog is not module
		super(GUICommon.mainFrame, paramTitle, false);

		currentDialog = this;

		// Prepare the text and dialog.
		textToDisplay = paramText;
		messageLabel = new Label(textToDisplay);

		Panel southPanel = new Panel();
		southPanel.setLayout(new GridLayout(1, 3));

		Button yesButton = new Button("Yes");
		yesButton.setSize(20, 10);
		yesButton.addActionListener(new ButtonListener(true));

		Button noButton = new Button("No");
		noButton.setSize(20, 10);
		noButton.addActionListener(new ButtonListener(false));

		Panel yesNoPanel = new Panel();
		yesNoPanel.setLayout(new FlowLayout());
		yesNoPanel.add(yesButton);
		yesNoPanel.add(noButton);

		// 加入Label与Button
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, messageLabel);
		add(BorderLayout.SOUTH, yesNoPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(300, 200);
		setSize(300, 100);
		// addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * 获取用户的选择.
	 * 
	 * @return 用户的选择,YES或NO
	 *************************** 
	 */
	public boolean getChoice() {
		return choice;
	}

	/**
	 *************************** 
	 * 设置用户的选择,并关闭本对话框.由于其代码简单,所以作为内嵌类.
	 *************************** 
	 */
	private class ButtonListener implements ActionListener {
		private boolean innerChoose;

		ButtonListener(boolean paramChoose) {
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

		/*
		 * int tempWidth = paramMessage.length(); if (tempWidth < 15){ tempWidth
		 * = 200; }else if (tempWidth > 30){ tempWidth = 500; }else{ tempWidth =
		 * 150 + tempWidth * 10; }//Of if
		 * 
		 * setLocation(400 - tempWidth/2, 200); setSize(tempWidth, 100);
		 */
		setVisible(true);
	}// Of setTitleAndMessageAndShow

}// Of class YesNoDialog
