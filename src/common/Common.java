/*
 * @(#)Common.java
 *
 */

package common;

import java.io.*;
import java.util.*;

/**
 * Provides basic settings along with some other important data for the whole
 * project. <br>
 * Project: Three-way conversational recommendation.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/TCR.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 * @date Created: October 20, 2008.<br>
 *       Last modified: January 30, 2020.
 * @version 1.0
 */

public class Common extends Object {

	/**
	 * The start time of the program/subroutine.
	 */
	public static long startTime;

	/**
	 * The end time of the program/subroutine.
	 */
	public static long endTime;

	/**
	 * The run times the program/subroutine.
	 */
	public static long runtimes;
	
	/**
	 * The number of steps for running.
	 */
	public static long runSteps = 0;

	/**
	 * Help the relative directory. In this way all file paths are correct while
	 * migrating to a new machine.
	 */
	public static String rootDirectory;

	static {
		File pathTestFile = new File("Cenal.java");
		rootDirectory = pathTestFile.getAbsolutePath().substring(0,
				pathTestFile.getAbsolutePath().length() - 10);
	}

	/**
	 * The random object.
	 */
	public static Random random = new Random();

	/**
	 * Anything changed?
	 */
	public static boolean somethingChanged = false;

	/**
	 * Unspecifed value. Used when a string is not available.
	 */
	public static final String unspecifiedString = "unspecified";

	/**
	 * To specify if the system is under debug, and avoid debugging output
	 * appear in formal versions.
	 */
	public static boolean ifDebug = false;

	/**
	 * The project header appears in any files generated.
	 */
	public static final String ProjectHeader = "%The cost-sensitive rough sets project."
			+ "\r\n%Corresponding author: Fan MIN, minfanphd@163.com\r\n";

	/**
	 * The system configuration filename.
	 */
	public static String configurationFilename = "config\\system.properties";

	/**
	 * Properties read from the configuration file.
	 */
	public static Properties property = new Properties();
	/**
	 * The file name of Sub-reduct
	 */
	public static String subReductsFileName = "";

	/**
	 ********************************** 
	 * Initialize. Read the configuration data from the system configuration
	 * file.
	 * 
	 * @throws Exception
	 *             Possible Exception.
	 ********************************** 
	 */
	public static void loadConfiguration() throws Exception {
		try {
			property.load(new FileInputStream(new File(configurationFilename)));
		} catch (Exception ee) {
			throw new Exception(
					"Exception occurred in Common.loadConfiguration()."
							+ "\n\tInvalid filename: " + configurationFilename
							+ ". " + "\n\t The initial Exception is: " + ee);
		}// Of try

		ifDebug = property.getProperty("if_debug").equals("true");
	}// Of loadConfiguration

	/**
	 ********************************** 
	 * Store configuration.
	 * 
	 * @throws Exception
	 *             May be incurred by writing configuration file.
	 ********************************** 
	 */
	public static void storeConfiguration() throws Exception {
		property.setProperty("if_debug", "" + ifDebug);

		try {
			property.store(
					new FileOutputStream(new File(configurationFilename)),
					"Basic properties of UYH. Author email: minfanphd@163.com");
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in common.Common.storeConfiguration()."
							+ "\n\t Invalid configuration filename: "
							+ configurationFilename
							+ "\n\t The initial Exception is: " + ee);
		}// Of try
	}// Of storeConfiguration

	/**
	 ********************************** 
	 * Exit system. No exception is thrown.
	 ********************************** 
	 */
	public static void exitSystem() {
		try {
			System.exit(0);
		} catch (Exception ee) {
			System.out.println(ee);
		}// Of try
	}// Of exitSystem
}// Of class Common
