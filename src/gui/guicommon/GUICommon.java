package gui.guicommon;

import java.awt.*;
import javax.swing.*;

/**
 * Active learning through clustering algorithm selection.
 * <p>
 * See Min F. et al. Active learning through clustering algorithm selection,
 * International Journal of Machine Learning and Cybernetics, 2020.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com, minfan@swpu.edu.cn <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://www.fansmale.com/>Lab of Machine Learning</a>,
 * Southwest Petroleum University, Chengdu 610500, China.<br>
 * Project: The cost-sensitive active learning project.
 * <p>
 * Progress: Almost finished, further revision is possible.<br>
 * Written time: March 09, 2011. <br>
 * Last modify time: August 28, 2019.
 */
public class GUICommon extends Object {
	public static Frame mainFrame = null;

	public static JTabbedPane mainPane = null;

	/**
	 * For default project number.
	 */
	public static int currentProjectNumber = 0;

	/**
	 * Defaut font.
	 */
	public static final Font MY_FONT = new Font("Times New Romans", Font.PLAIN, 12);

	/**
	 * Default color
	 */
	public static final Color MY_COLOR = Color.lightGray;

	/**
	 *************************** 
	 * Set the main frame. This can be done only once at the initialzing stage.
	 * 
	 * @param paramFrame
	 *            the main frame of the GUI.
	 * @throws Exception
	 *             If the main frame is set more than once.
	 *************************** 
	 */
	public static void setFrame(Frame paramFrame) throws Exception {
		if (mainFrame == null) {
			mainFrame = paramFrame;
		} else {
			throw new Exception("The main frame can be set only ONCE!");
		} // Of if
	}// Of setFrame

	/**
	 *************************** 
	 * Set the main pane. This can be done only once at the initialzing stage.
	 * 
	 * @param paramPane
	 *            the main pane of the GUI.
	 * @throws Exception
	 *             If the main panel is set more than once.
	 *************************** 
	 */
	public static void setPane(JTabbedPane paramPane) throws Exception {
		if (mainPane == null) {
			mainPane = paramPane;
		} else {
			throw new Exception("The main panel can be set only ONCE!");
		} // Of if
	}// Of setPAne

}// Of class GUICommon
