package gui.guicommon;

import java.awt.event.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Just for closing the whole project/frame.
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
public class ApplicationShutdown implements WindowListener, ActionListener {
	public static ApplicationShutdown applicationShutdown = new ApplicationShutdown();

	/**
	 *************************** 
	 * Empty constructor
	 *************************** 
	 */
	private ApplicationShutdown() {
	}// Of ApplicationShutdown.

	/**
	 *************************** 
	 * Shutdown the system
	 *************************** 
	 */
	public void windowClosing(WindowEvent comeInWindowEvent) {
		System.exit(0);
	}// Of windowClosing.

	public void windowActivated(WindowEvent comeInWindowEvent) {
	}// Of windowActivated.

	public void windowClosed(WindowEvent comeInWindowEvent) {
	}// Of windowClosed.

	public void windowDeactivated(WindowEvent comeInWindowEvent) {
	}// Of windowDeactivated.

	public void windowDeiconified(WindowEvent comeInWindowEvent) {
	}// Of windowDeiconified.

	public void windowIconified(WindowEvent comeInWindowEvent) {
	}// Of windowIconified.

	public void windowOpened(WindowEvent comeInWindowEvent) {
	}// Of windowOpened.

	/**
    *************************
    *************************
    */
	public void actionPerformed(ActionEvent ee) {
		System.exit(0);
	}// Of actionPerformed.
}// Of class ApplicationShutdown
