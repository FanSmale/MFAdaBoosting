package gui.others;

import java.awt.*;
import java.awt.event.*;

import gui.guidialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Ensure the field contains an integer value.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Copied from Hydrosimu.<br>
 * Written time: March 12, 2011. <br>
 * Last modify time: March 12, 2011.
 */
public class IntegerField extends TextField implements FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2462338973265150779L;

	/**
	 *************************** 
	 * Only specify the content.
	 *************************** 
	 */
	public IntegerField() {
		this("513");
	}// Of constructor

	/**
	 *************************** 
	 * Specify the content and the width.
	 * 
	 * @param paraString
	 *            The default value of the content.
	 * @param paraWidth 
	 * The width of the field.
	 *************************** 
	 */
	public IntegerField(String paraString, int paraWidth) {
		super(paraString, paraWidth);
		addFocusListener(this);
	}// Of constructor

	/**
	 *************************** 
	 * Only specify the content.
	 * 
	 * @param paraString
	 *            The given default string.
	 *************************** 
	 */
	public IntegerField(String paraString) {
		super(paraString);
		addFocusListener(this);
	}// Of constructor

	/**
	 *************************** 
	 * Only specify the width.
	 * 
	 * @param paraWidth
	 *            The width of the field.
	 *************************** 
	 */
	public IntegerField(int paraWidth) {
		super(paraWidth);
		setText("513");
		addFocusListener(this);
	}// Of constructor

	/**
	 ********************************** 
	 * Implement FocusListenter.
	 * 
	 * @param paraEvent
	 *            The event is unimportant.
	 ********************************** 
	 */
	public void focusGained(FocusEvent paraEvent) {
	}// Of focusGained

	/**
	 ********************************** 
	 * Implement FocusListenter.
	 * 
	 * @param paraEvent
	 *            The event is unimportant.
	 ********************************** 
	 */
	public void focusLost(FocusEvent paraEvent) {
		try {
			Integer.parseInt(getText());
			// System.out.println(tempInt);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\"Not an integer. Please check.");
			requestFocus();
		}
	}// Of focusLost

	/**
	 ********************************** 
	 * Get the int value.
	 * 
	 * @return the int value.
	 ********************************** 
	 */
	public int getValue() {
		int tempInt = 0;
		try {
			tempInt = Integer.parseInt(getText());
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\" Not an int. Please check.");
			requestFocus();
		}
		return tempInt;
	}// Of getValue

}// Of class IntegerField
