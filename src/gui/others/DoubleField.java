package gui.others;

import java.awt.*;
import java.awt.event.*;

import gui.guidialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Obtain a double value.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Copied from hydrosimu.<br>
 * Written time: March 12, 2011. <br>
 * Last modify time: March 12, 2011.
 */
public class DoubleField extends TextField implements FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3643575399529932020L;

	protected double doubleValue;

	/**
	 *************************** 
	 * Give it default values.
	 *************************** 
	 */
	public DoubleField() {
		this("5.13", 10);
	}// Of the first constructor

	/**
	 *************************** 
	 * Only specify the content.
	 * 
	 * @param paraString
	 *            The content of the field.
	 *************************** 
	 */
	public DoubleField(String paraString) {
		this(paraString, 10);
	}// Of the second constructor

	/**
	 *************************** 
	 * Only specify the width.
	 * 
	 * @param paraWidth
	 *            The width of the field.
	 *************************** 
	 */
	public DoubleField(int paraWidth) {
		this("5.13", paraWidth);
	}// Of the third constructor

	/**
	 *************************** 
	 * Specify the content and the width.
	 * 
	 * @param paraString
	 *            The content of the field.
	 * @param paraWidth
	 *            The width of the field.
	 *************************** 
	 */
	public DoubleField(String paraString, int paraWidth) {
		super(paraString, paraWidth);
		addFocusListener(this);
	}// Of the fourth constructor

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
			doubleValue = Double.parseDouble(getText());
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText() + "\" Not a double. Please check.");
			requestFocus();
		}
	}// Of focusLost

	/**
	 ********************************** 
	 * Get the double value.
	 * 
	 * @return the double value.
	 ********************************** 
	 */
	public double getValue() {
		try {
			doubleValue = Double.parseDouble(getText());
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText() + "\" Not a double. Please check.");
			requestFocus();
		}
		return doubleValue;
	}// Of getValue
}// Of class DoubleField
