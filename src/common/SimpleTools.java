
package common;

import java.io.*;
import java.util.*;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Frequently used functions.<br>
 * Project: Java implementation of the AdaBoosting algorithm.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFAdaBoosting.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: July 20, 2020.<br>
 *         Last modified: July 20, 2020.
 * @version 1.0
 */

public class SimpleTools extends Object {
	/**
	 * Maximal length of the parsed array (for int and double).
	 */
	public static final int MAX_PARSE_ARRAY_LENGTH = 100;

	/**
	 * A global random object.
	 */
	public static final Random random = new Random();

	/**
	 * Console output for debugging? If so, output. Otherwise ignore the output
	 * information.
	 */
	static boolean ifConsoleOutput = false;

	/**
	 * Track the process?
	 */
	public static boolean processTracking = false;

	/**
	 * Track the variables?
	 */
	public static boolean variableTracking = false;

	/**
	 * Output to file?
	 */
	public static boolean fileOutput = false;

	/**
	 ****************** 
	 * Output for process tracking.
	 * 
	 * @param paraString
	 *            The string for display.
	 ****************** 
	 */
	public static void processTrackingOutput(String paraString) {
		if (processTracking) {
			System.out.print(paraString);
		} // Of if
	}// Of processTrackingOutput

	/**
	 ****************** 
	 * Output for variable tracking.
	 * 
	 * @param paraString
	 *            The string for display.
	 ****************** 
	 */
	public static void variableTrackingOutput(String paraString) {
		if (variableTracking) {
			System.out.println(paraString);
		} // Of if
	}// Of variableTrackingOutput

	/**
	 ********************************** 
	 * Console output.
	 * 
	 * @param paraString
	 *            the given string.
	 ********************************** 
	 */
	public static void consoleOutput(String paraString) {
		if (ifConsoleOutput) {
			System.out.println(paraString);
		} // Of if
	}// Of consoleOutput

	/**
	 ********************************** 
	 * Console output.
	 * 
	 * @param paraString
	 *            the given string.
	 ********************************** 
	 */
	public static Instances[] splitInTwo(Instances paraOringinalData, double paraFraction) {
		// Step 1. Initialize.
		int tempNumInstances = paraOringinalData.numInstances();
		Instances[] resultInstancesArray = new Instances[2];
		resultInstancesArray[0] = new Instances(paraOringinalData);
		resultInstancesArray[1] = new Instances(paraOringinalData);

		// Step 2. Indices for the two subsets.
		int tempFirstSetSize = (int) (tempNumInstances * paraFraction);
		int[] tempIndices = generateRandomSequence(paraOringinalData.numInstances());

		int[] tempFirstSetIndices = new int[tempFirstSetSize];
		for (int i = 0; i < tempFirstSetIndices.length; i++) {
			tempFirstSetIndices[i] = tempIndices[i];
		} // Of for i
		Arrays.sort(tempFirstSetIndices);
		variableTrackingOutput("The first set indices: " + Arrays.toString(tempFirstSetIndices));

		int[] tempSecondSetIndices = new int[tempNumInstances - tempFirstSetSize];
		for (int i = 0; i < tempSecondSetIndices.length; i++) {
			tempSecondSetIndices[i] = tempIndices[tempFirstSetSize + i];
		} // Of for i
		Arrays.sort(tempSecondSetIndices);
		variableTrackingOutput("The second set indices: " + Arrays.toString(tempSecondSetIndices));

		// Step 3. Remove redundant data.
		for (int i = tempSecondSetIndices.length - 1; i >= 0; i--) {
			resultInstancesArray[0].delete(tempSecondSetIndices[i]);
		} // Of for i

		for (int i = tempFirstSetIndices.length - 1; i >= 0; i--) {
			resultInstancesArray[1].delete(tempFirstSetIndices[i]);
		} // Of for i

		return resultInstancesArray;
	}// Of splitInTwo

	/**
	 *********************************** 
	 * Randomly select some elements from the given array.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @param paraValidLength
	 *            Valid length of the array.
	 * @param paraNumSelection
	 *            The number of selected elements.
	 *********************************** 
	 */
	public static int[] randomSelectFromArray(int[] paraArray, int paraValidLength,
			int paraNumSelection) {

		int[] tempArray = null;
		try {
			tempArray = generateRandomIndices(paraValidLength, paraNumSelection);
		} catch (Exception ee) {
			System.out.println("Internal error occurred in TCR.randomSelectFromArray(): \r\n" + ee);
			System.exit(0);
		} // Of try

		int[] resultArray = new int[paraNumSelection];
		for (int i = 0; i < resultArray.length; i++) {
			resultArray[i] = paraArray[tempArray[i]];
		} // Of for i

		return resultArray;
	}// Of randomSelectFromArray

	/**
	 ************************* 
	 * An instance converted to a double array, where the class label is not
	 * considered.
	 * 
	 * @param paraInstance
	 *            The given instance.
	 * @return A double array.
	 ************************* 
	 */
	public static double[] instanceToDoubleArray(Instance paraInstance) {
		double[] resultArray = new double[paraInstance.numAttributes() - 1];
		for (int i = 0; i < resultArray.length; i++) {
			resultArray[i] = paraInstance.value(i);
		} // Of for i

		return resultArray;
	}// Of instanceToDoubleArray

	/**
	 ********************************** 
	 * Convert a double value into a shorter string.
	 * 
	 * @param paraDouble
	 *            the double value to be converted.
	 * @return A shorter representation of the double value.
	 ********************************** 
	 */
	public static String shorterDouble(double paraDouble) {
		return shorterDouble(paraDouble, 7);
	}// Of shorterDouble

	/**
	 ********************************** 
	 * Convert a double value into a shorter string.
	 * 
	 * @param paraDouble
	 *            the double value to be converted.
	 * @param paraLength
	 *            the length of reserved double.
	 * @return A shorter representation of the double value.
	 ********************************** 
	 */
	public static String shorterDouble(double paraDouble, int paraLength) {
		// double absValue = Math.abs(paraDouble);
		// double least = 0.001;
		// for (int i = 2; i < paraLength; i++)
		// least *= 0.1;
		// if (absValue < least)
		// return "0.0";

		String shorterString = new Double(paraDouble).toString();
		// For uncontrollable data, just output itself
		if ((paraDouble < 0.001) && (-0.001 < paraDouble) || (paraDouble < -9999)
				|| (9999 < paraDouble))
			return shorterString;

		if ((paraDouble > 0) && (shorterString.length() > paraLength))
			shorterString = shorterString.substring(0, paraLength);
		if ((paraDouble < 0) && (shorterString.length() > paraLength + 1))
			shorterString = shorterString.substring(0, paraLength + 1);

		return shorterString;
	}// Of shorterDouble

	/**
	 ********************************** 
	 * Convert a string with commas into a string array, blanks adjacent with
	 * commas are deleted. If the string bewteen two adjacent commas are blank
	 * or contains only space char ' ', then an exception will be thrown.<br>
	 * For example, "a, bc, def, g" will be converted into a string array with 4
	 * elements "a", "bc", "def" and "g".
	 * 
	 * @param paraString
	 *            The source string
	 * @return A string array.
	 * @throws Exception
	 *             Exception for illegal data.
	 * @see #stringArrayToString(java.lang.String[])
	 ********************************** 
	 */
	public static String[] stringToStringArray(String paraString) throws Exception {
		/*
		 * Convert this string into an array such that another method could be
		 * invoked.
		 */
		int tempCounter = 1;
		for (int i = 0; i < paraString.length(); i++) {
			if (paraString.charAt(i) == ',') {
				tempCounter++;
			} // Of if
		} // Of for i

		String[] theStringArray = new String[tempCounter];

		String remainString = new String(paraString) + ",";
		for (int i = 0; i < tempCounter; i++) {
			theStringArray[i] = remainString.substring(0, remainString.indexOf(",")).trim();
			if (theStringArray[i].equals("")) {
				throw new Exception("Error occurred in common.SimpleTool.stringToStringArray()."
						+ "\n\tBlank attribute or data is not allowed as a data. "
						+ "\n\tThe string is:" + paraString);
			} // Of if
				// Common.println(theStringArray[i]);
			remainString = remainString.substring(remainString.indexOf(",") + 1);
			// Common.println("remainString: " + remainString);
		} // Of for i

		return theStringArray;
	}// Of stringToStringArray

	/**
	 ********************************** 
	 * Convert a string array into a string, elements are separated by commas.
	 * 
	 * @param prmStringArray
	 *            The source string array
	 * @return converted string.
	 * @see #stringToStringArray(java.lang.String)
	 ********************************** 
	 */
	public static String stringArrayToString(String[] prmStringArray) {
		String newString = "";
		for (int i = 0; i < prmStringArray.length; i++) {
			newString += new String(prmStringArray[i]) + ",";
		} // Of for

		// Delete the last comma
		newString = newString.substring(0, newString.length() - 1);

		return newString;
	}// Of stringArrayToString

	/**
	 ********************************** 
	 * Add single quotes for a string (may be an array, elements are separated
	 * by commas). This is needed in some SQL statements. For example, "ab,c,d"
	 * will be converted into "ab','c','d".
	 * 
	 * @param prmStringArray
	 *            The source string
	 * @return converted string.
	 ********************************** 
	 */
	public static String addSingleQuotes(String prmStringArray) {
		int tempIndexInString = prmStringArray.indexOf(",");
		while (tempIndexInString > -1) {
			prmStringArray = prmStringArray.substring(0, tempIndexInString) + "\';\'"
					+ prmStringArray.substring(tempIndexInString + 1, prmStringArray.length());
			tempIndexInString = prmStringArray.indexOf(",");
		} // Of while

		prmStringArray = "\'" + prmStringArray.replace(';', ',') + "\'";

		return prmStringArray;
	}// Of addSingleQuotes

	/**
	 ********************************** 
	 * If the string quisi-array (elements are separated by commas) contains
	 * respective attribute.<br>
	 * For exampe "abc, de" contains "de", but it does not contain "d".
	 * 
	 * @param prmStringArray
	 *            The string quisi-array (elements are separated by commas).
	 * @param paraString
	 *            Respect string.
	 * @return If it is contained.
	 * @see #stringToStringArray(java.lang.String)
	 ********************************** 
	 */
	public static boolean stringArrayContainsString(String prmStringArray, String paraString) {
		String[] realStringArray = null;
		try {
			realStringArray = stringToStringArray(prmStringArray);
		} catch (Exception ee) {
			return false;
		} // Of try

		for (int i = 0; i < realStringArray.length; i++) {
			if (realStringArray[i].equals(paraString)) {
				return true;
			} // Of if
		} // Of for
		return false;
	}// Of stringArrayContainsString

	/**
	 ********************************** 
	 * Join two attribute strings, separated by a comma.<br>
	 * 
	 * @param prmFirstString
	 *            The first attribute string
	 * @param prmSecondString
	 *            The second attribute string
	 * @return joined string.
	 ********************************** 
	 */
	public static String joinString(String prmFirstString, String prmSecondString) {
		if (prmFirstString.equals(""))
			return prmSecondString + "";
		if (prmSecondString.equals(""))
			return prmFirstString + "";

		return prmFirstString + "," + prmSecondString;
	}// Of joinString

	/**
	 ********************************** 
	 * Convert a string with delimiters (such as commas or semi-commas) into a
	 * string array.<br>
	 * This method is more generalized than stringToStringArray because the
	 * latter only permits commas to be delimiters. For more detail please
	 * contact <A href="mailto:qiheliu@uestc.edu.cn">Liu Qihe</A>
	 * 
	 * @param paraString
	 *            The given string
	 * @param paraDelimiter
	 *            The given delimiter
	 * @param paraReturnTokens
	 *            Is the delimiter permitted after convertion.
	 * @return string array separated by commas
	 ********************************** 
	 */
	public static String[] parseString(String paraString, String paraDelimiter,
			boolean paraReturnTokens) {
		String[] returnString = null;
		StringTokenizer token;
		if (paraString != null) {
			token = new StringTokenizer(paraString.trim(), paraDelimiter, paraReturnTokens);
			returnString = new String[token.countTokens()];
			int i = 0;
			while (token.hasMoreTokens()) {
				returnString[i] = (String) token.nextToken();
				i++;
			} // Of while
		} // Of if

		return returnString;
	}// end of parseString

	/**
	 ********************************** 
	 * Remove string from a string array, and return a string. For more detail
	 * please contact <A href="mailto:qiheliu@uestc.edu.cn">Liu Qihe</A>
	 * 
	 * @param paraString
	 *            字符串
	 * @param index
	 *            去掉串的位\uFFFD
	 * @return 字符串，属性值之间用逗号分隔.
	 ***********************************/
	public static String generateString(String[] paraString, int index) {
		String result = "";
		for (int i = 0; i < paraString.length; i++) {
			if (i == index)
				continue;
			result += paraString[i] + ",";
		}
		result = result.substring(0, result.length() - 1);
		return result;

	}// of generateString

	/**
	 ********************************* 
	 * GB2312 to UNICODE. Use this one if Chinese characters is a mess.
	 * 
	 * @param paraString
	 *            a GB2312 string
	 * @return a UNICODE string.
	 * @see #UNICODEToGB2312(java.lang.String)
	 ********************************* 
	 */
	public static String GB2312ToUNICODE(String paraString) {
		char[] tempCharArray = paraString.toCharArray();
		int tempLength = tempCharArray.length;
		byte[] tempByteArray = new byte[tempLength];
		for (int i = 0; i < tempLength; i++) {
			tempByteArray[i] = (byte) tempCharArray[i];
		} // Of for.

		String returnString = new String(tempByteArray);
		return returnString;
	}// Of GB2312ToUNICODE.

	/**
	 ********************************* 
	 * UNICODE to GB2312. Use this one if Chinese characters is a mess.
	 * 
	 * @param paraString
	 *            a UNICODE string.
	 * @return a GB2312 string.
	 * @see #GB2312ToUNICODE(java.lang.String)
	 ********************************* 
	 */
	public static String UNICODEToGB2312(String paraString) {
		// Convert the string into a byte array.
		byte[] byteArray = paraString.getBytes();

		int arrayLength = byteArray.length;

		// Store converted char array.
		char[] charArray = new char[arrayLength];

		// Convert chars one by one.
		for (int i = 0; i < arrayLength; i++) {
			// Add an all 0 byte.
			charArray[i] = (char) byteArray[i];
		} // Of for.

		// Get a new string according to the converted array.
		String convertedString = new String(charArray);
		return convertedString;
	}// Of UNICODEToGB2312.

	/**
	 ********************************** 
	 * One string minus another, essentially corresponding string arrays.<br>
	 * E.g., "ab,cd,efa" minus "ab,efa" gets "cd"
	 * 
	 * @param prmFirstString
	 *            The first string.
	 * @param prmSecondString
	 *            The second string.
	 * @return A result string.
	 * @throws Exception
	 *             if the first string does not contain an element of the second
	 *             string, e.g., "ab, cd" minus "ab, de". #see
	 *             scheme.SymbolicPartition
	 *             .computeOptimalPartitionReduct(java.lang.String,
	 *             java.lang.String, int)
	 ********************************** 
	 */
	public static String stringMinusString(String prmFirstString, String prmSecondString)
			throws Exception {
		String[] firstArray = stringToStringArray(prmFirstString);
		String[] secondArray = stringToStringArray(prmSecondString);

		boolean[] includeArray = new boolean[firstArray.length];
		for (int i = 0; i < includeArray.length; i++) {
			includeArray[i] = true;
		} // Of for i

		// Given an element of secondArray, is there an identical element of
		// firstArray?
		boolean found = false;
		for (int i = 0; i < secondArray.length; i++) {
			found = false;
			for (int j = 0; j < firstArray.length; j++) {
				if (secondArray[i].equals(firstArray[j])) {
					includeArray[j] = false;
					found = true;
					break;
				}
			} // Of for j
			if (!found)
				throw new Exception("Error occured in SimpleTool.stringMinusString(), \n" + "\t"
						+ secondArray[i] + "is not included in " + prmFirstString);
		} // Of for i

		String returnString = "";
		for (int i = 0; i < includeArray.length; i++) {
			if (includeArray[i])
				returnString += firstArray[i] + ",";
		} // Of for i

		if (returnString.length() > 0)
			returnString = returnString.substring(0, returnString.length() - 1);
		return returnString;
	}// Of stringMinusString

	/**
	 ********************************** 
	 * One string union another, essentially corresponding string sets.<br>
	 * E.g., "ab,cd,efa" union "ab,ee" gets "ab,cd,efa, ee"
	 * 
	 * @param prmFirstString
	 *            The first string.
	 * @param prmSecondString
	 *            The second string.
	 * @return A result string. #see
	 *         scheme.Reduction.computeOptimalMReductByEntropy(java.lang.String,
	 *         java.lang.String, java.lang.String)
	 * @throws Exception
	 *             The exception generated by stringToStringArray(String).
	 ********************************** 
	 */
	public static String stringUnionString(String prmFirstString, String prmSecondString)
			throws Exception {
		if (prmFirstString.equals(""))
			return prmSecondString + "";
		if (prmSecondString.equals(""))
			return prmFirstString + "";

		String[] firstArray = stringToStringArray(prmFirstString);
		String[] secondArray = stringToStringArray(prmSecondString);

		String unionString = new String(prmFirstString);

		boolean found = false;
		for (int i = 0; i < secondArray.length; i++) {
			found = false;
			for (int j = 0; j < firstArray.length; j++) {
				if (secondArray[i].equals(firstArray[j])) {
					found = true;
					break;
				}
			} // Of for j
			if (!found)
				if (unionString.equals(""))
					unionString += secondArray[i];
				else
					unionString += "," + secondArray[i];
		} // Of for i

		return unionString;
	}// Of stringUnionString

	/**
	 *************************** 
	 * Judge whether or not the given string is null/empty.
	 * 
	 * @param paraString
	 *            The given string.
	 * @return True if it is null or empty.
	 *************************** 
	 */
	public static boolean isEmptyStr(String paraString) {
		if (paraString == null)
			return true;
		if (paraString.equals(""))
			return true;
		return false;
	}// Of isEmptyStr

	/**
	 *************************** 
	 * Judge whether or not the given string is specified.
	 * 
	 * @param paraString
	 *            The given string.
	 * @return True if the string is empty or "unspecified."
	 *************************** 
	 */
	public static boolean isUnspecifiedStr(String paraString) {
		if (isEmptyStr(paraString))
			return true;
		if (paraString.equals(Common.unspecifiedString))
			return true;
		return false;
	}// Of isEmptyStr

	/**
	 *************************** 
	 * Read an integer array from a given string. Integers are separated by
	 * separators. Author Fan Min.
	 * 
	 * @param paraString
	 *            The given string.
	 * @param paraNumberOfInts
	 *            The number of integer to read from the string. If there are
	 *            more integers, just ignore them.
	 * @param paraSeparator
	 *            The separator of data, blank and commas are most commonly uses
	 *            ones.
	 * @return The constructed array.
	 * @throws Exception
	 *             for format.
	 *************************** 
	 */
	public static int[] parseIntArray(String paraString, int paraNumberOfInts, char paraSeparator)
			throws Exception {
		int[] returnArray = new int[paraNumberOfInts];
		String currentString = null;
		String remainingString = new String(paraString);
		try {
			for (int i = 0; i < paraNumberOfInts - 1; i++) {
				currentString = remainingString.substring(0, remainingString.indexOf(paraSeparator))
						.trim();
				returnArray[i] = Integer.parseInt(currentString);
				remainingString = remainingString
						.substring(remainingString.indexOf(paraSeparator) + 1).trim();
			} // Of for i

			// The last one may have no blank after it.
			if (remainingString.indexOf(paraSeparator) < 0)
				currentString = remainingString;
			else
				currentString = remainingString.substring(0,
						remainingString.indexOf(paraSeparator));

			returnArray[paraNumberOfInts - 1] = Integer.parseInt(currentString);
		} catch (java.lang.StringIndexOutOfBoundsException sie) {
			throw new Exception("Error occurred in common.SimpleTool.parseIntArray.\r\n"
					+ "May caused by the number of int value required exceeds those in the string.\r\n"
					+ sie);
		} catch (java.lang.NumberFormatException nfe) {
			throw new Exception("Error occurred in common.SimpleTool.parseIntArray.\r\n"
					+ "May caused by incorrect separator (e.g., comma, blank).\r\n" + nfe);
		} catch (Exception ee) {
			throw new Exception("Error occurred in common.SimpleTool.parseIntArray.\r\n" + ee);
		} // Of try

		return returnArray;
	}// Of parseIntArray

	/**
	 *************************** 
	 * An int to an attribute subset. Author Fan Min.
	 * 
	 * @param paraInt
	 *            The given integer representing an attribute subset.
	 * @return The attribute subset in a string.
	 *************************** 
	 */
	public static String intToAttributeSetString(int paraInt) {
		String resultString = "";
		if (paraInt < 1) {
			return resultString; // No need to throw an exception.
		}

		int currentIndex = 0;
		int tempInt = paraInt;
		while (tempInt > 0) {
			if (tempInt % 2 == 1) {
				resultString += currentIndex + ",";
			}

			tempInt /= 2;
			currentIndex++;
		} // Of while

		resultString = resultString.substring(0, resultString.length() - 1);
		return resultString;
	}// Of intToAttributeSetString

	/**
	 *************************** 
	 * An int array to an attribute subset. Author Fan Min.
	 * 
	 * @param paraIntArray
	 *            The given integer array an attribute subset.
	 * @param paraLength
	 *            The size of the subset.
	 * @return The attribute subset in a string.
	 *************************** 
	 */
	public static String intArrayToAttributeSetString(int[] paraIntArray, int paraLength) {
		String resultString = "";
		if (paraLength < 1) {
			return resultString; // No need to throw an exception.
		}
		for (int i = 0; i < paraLength; i++) {
			resultString += paraIntArray[i] + ",";

		} // Of for i
		resultString = resultString.substring(0, resultString.length() - 1);
		return resultString;
	}// Of intArrayToAttributeSetString

	/**
	 *************************** 
	 * An int array to an int value. For example, [0, 2] will be binary 101 = 5.
	 * 
	 * @param paraIntArray
	 *            The given integer array indicating which positions is 1.
	 * @return An int value.
	 *************************** 
	 */
	public static int intArrayToInt(int[] paraIntArray) {
		if (paraIntArray == null) {
			return 0;
		}

		int tempValue = 0;
		for (int i = 0; i < paraIntArray.length; i++) {
			tempValue += (int) Math.pow(2, paraIntArray[i]);
		}
		return tempValue;
	}// Of intArrayToInt

	/**
	 *************************** 
	 * An int array to an long value. For example, [0, 2] will be binary 101 =
	 * 5.
	 * 
	 * @param paraIntArray
	 *            The given integer representing an attribute subset.
	 * @return An int value.
	 * @see #intArrayToInt(int[])
	 *************************** 
	 */
	public static long intArrayToLong(int[] paraIntArray) {
		if (paraIntArray == null) {
			return 0;
		}

		long tempValue = 0;
		for (int i = 0; i < paraIntArray.length; i++) {
			tempValue += (long) Math.pow(2, paraIntArray[i]);
		}
		return tempValue;
	}// Of intArrayToLong

	/**
	 *************************** 
	 * A long to an attribute subset.
	 * 
	 * @param paraLong
	 *            The given integer representing an attribute subset.
	 * @return The attribute subset in a string.
	 *************************** 
	 */
	public static String longToAttributeSetString(long paraLong) {
		String resultString = "";
		if (paraLong < 1) {
			return resultString; // No need to throw an exception.
		}

		int currentIndex = 0;
		long tempLong = paraLong;
		while (tempLong > 0) {
			if (tempLong % 2 == 1) {
				resultString += currentIndex + ",";
			}

			tempLong /= 2;
			currentIndex++;
		} // Of while

		resultString = resultString.substring(0, resultString.length() - 1);
		return resultString;
	}// Of longToAttributeSetString

	/**
	 *************************** 
	 * Convert an integer array into a string. Integers are separated by
	 * separators. Author Fan Min.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @param paraSeparator
	 *            The separator of data, blank and commas are most commonly uses
	 *            ones.
	 * @return The constructed String.
	 *************************** 
	 */
	public static String intArrayToString(int[] paraArray, char paraSeparator) {
		String returnString = "[]";
		if ((paraArray == null) || (paraArray.length < 1)) {
			return returnString;
		} // Of if

		// throw new Exception(
		// "Error occurred in common.SimpleTool. Cannot convert an empty array
		// into a string.");
		returnString = "";
		for (int i = 0; i < paraArray.length - 1; i++) {
			returnString += "" + paraArray[i] + paraSeparator;
		} // Of for i
		returnString += paraArray[paraArray.length - 1];
		returnString = "[" + returnString + "]";

		return returnString;
	}// Of intArrayToString

	/**
	 *************************** 
	 * Convert an integer matrix into a string. Integers are separated by
	 * separators. Author Xiangju Li.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @param paraSeparator
	 *            The separator of data, blank and commas are most commonly uses
	 *            ones.
	 * @return The constructed String.
	 *************************** 
	 */
	public static String intMatrixToString(int[][] paraArray, char paraSeparator) {
		String returnString = "[]";
		if ((paraArray == null) || (paraArray.length < 1))
			return returnString;
		returnString = "";
		for (int i = 0; i < paraArray.length; i++) {
			for (int j = 0; j < paraArray[i].length - 1; j++) {
				returnString += "" + paraArray[i][j] + paraSeparator;
			} // Of for j
			returnString += paraArray[i][paraArray.length - 1];
			returnString += "\r\n";
		} // Of for i
		return returnString;
	}// Of intMatrixToString

	/**
	 *************************** 
	 * Read a double array from a given string. Double values are separated by
	 * separators. <br>
	 * Author Fan Min.
	 * 
	 * @param paraString
	 *            The given string.
	 * @param paraNumberOfDoubles
	 *            The number of integer to read from the string. If there are
	 *            more integers, just ignore them.
	 * @param paraSeparator
	 *            The separator of data, blank and commas are most commonly uses
	 *            ones.
	 * @return The constructed array.
	 * @throws Exception
	 *             If index out of bounds.
	 *************************** 
	 */
	public static double[] parseDoubleArray(String paraString, int paraNumberOfDoubles,
			char paraSeparator) throws Exception {
		double[] returnArray = new double[paraNumberOfDoubles];
		String currentString = null;
		String remainingString = new String(paraString);

		try {
			for (int i = 0; i < paraNumberOfDoubles - 1; i++) {
				currentString = remainingString.substring(0, remainingString.indexOf(paraSeparator))
						.trim();
				returnArray[i] = Double.parseDouble(currentString);
				remainingString = remainingString
						.substring(remainingString.indexOf(paraSeparator) + 1).trim();
			} // Of for i

			// The last one may have no blank after it.
			if (remainingString.indexOf(paraSeparator) < 0)
				currentString = remainingString;
			else
				currentString = remainingString.substring(0,
						remainingString.indexOf(paraSeparator));

			returnArray[paraNumberOfDoubles - 1] = Double.parseDouble(currentString);
		} catch (java.lang.StringIndexOutOfBoundsException sie) {
			throw new Exception("Error occurred in common.SimpleTool.parseDoubleArray.\r\n"
					+ "May caused by the number of double value required exceeds those in the string,\r\n"
					+ "or invalid separator.\r\n" + sie);
		} catch (java.lang.NumberFormatException nfe) {
			throw new Exception("Error occurred in common.SimpleTool.parseDoubleArray.\r\n"
					+ "May caused by incorrect separator (e.g., comma, blank).\r\n" + nfe);
		} catch (Exception ee) {
			throw new Exception("Error occurred in common.SimpleTool.parseDoubleArray.\r\n" + ee);
		}

		return returnArray;
	}// Of parseDoubleArray

	/**
	 *************************** 
	 * Read a double array from a given string. The number of doubles are not
	 * given, so I will parse as many double values as possible. The seperator
	 * is not indicated, so comma is the first candidate, and blank is the
	 * second. <br>
	 * Author Fan Min.
	 * 
	 * @param paraString
	 *            The given string.
	 * @return The constructed array.
	 * @throws Exception
	 *             if the array length exceeds the bound.
	 *************************** 
	 */
	public static double[] parseDoubleArray(String paraString) throws Exception {
		char separator = ' ';
		if (paraString.indexOf(',') > 0)
			separator = ',';
		// String tempString = new String(paraString);

		double[] tempArray = new double[MAX_PARSE_ARRAY_LENGTH];
		int arrayLength = 0;

		String currentString = null;
		String remainingString = new String(paraString);

		try {
			while (remainingString.indexOf(separator) > 0) {
				currentString = remainingString.substring(0, remainingString.indexOf(separator))
						.trim();
				tempArray[arrayLength] = Double.parseDouble(currentString);
				arrayLength++;
				if (arrayLength >= MAX_PARSE_ARRAY_LENGTH)
					throw new Exception(
							"The array length should not exceed " + MAX_PARSE_ARRAY_LENGTH);
				remainingString = remainingString.substring(remainingString.indexOf(separator) + 1)
						.trim();
			} // Of while

			tempArray[arrayLength] = Double.parseDouble(remainingString);
		} catch (java.lang.StringIndexOutOfBoundsException sie) {
			throw new Exception("Error occurred in common.SimpleTool.parseDoubleArray.\r\n"
					+ "May caused by the number of double value required exceeds those in the string,\r\n"
					+ "or invalid separator.\r\n" + sie);
		} catch (java.lang.NumberFormatException nfe) {
			throw new Exception("Error occurred in common.SimpleTool.parseDoubleArray.\r\n"
					+ "May caused by incorrect separator (e.g., comma, blank).\r\n" + nfe);
		} catch (Exception ee) {
			throw new Exception("Error occurred in common.SimpleTool.parseDoubleArray.\r\n" + ee);
		}

		double[] returnArray = new double[arrayLength + 1];

		// The arrayLength is starting from 0
		for (int i = 0; i <= arrayLength; i++)
			returnArray[i] = tempArray[i];

		return returnArray;
	}// Of parseDoubleArray

	/**
	 *************************** 
	 * Conver a double array into a string. Doubles are separated by blanks.
	 * Author Fan Min.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @return The constructed String.
	 * @throws Exception
	 *             for empty array.
	 *************************** 
	 */
	public static String doubleArrayToString(double[] paraArray) throws Exception {
		return doubleArrayToString(paraArray, ' ');
	}// Of doubleArrayToString

	/**
	 *************************** 
	 * Conver a double array into a string. Doubles are separated by separators.
	 * Author Fan Min.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @param paraSeparator
	 *            The separator of data, blank and commas are most commonly uses
	 *            ones.
	 * @return The constructed String.
	 * @throws Exception
	 *             for null or empty array.
	 *************************** 
	 */
	public static String doubleArrayToString(double[] paraArray, char paraSeparator)
			throws Exception {
		if ((paraArray == null) || (paraArray.length < 1))
			throw new Exception(
					"Error occurred in common.SimpleTool. Cannot convert an empty array into a string.");
		String returnString = "";
		for (int i = 0; i < paraArray.length - 1; i++) {
			returnString += "" + paraArray[i] + paraSeparator;
		} // Of for i
		returnString += paraArray[paraArray.length - 1];

		return returnString;
	}// Of doubleArrayToString

	/**
	 *************************** 
	 * Convert a boolean array into a string. Booleans are separated by
	 * separators.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @param paraSeparator
	 *            The separator of data, blank and commas are most commonly uses
	 *            ones.
	 * @return The constructed String.
	 *************************** 
	 */
	public static String booleanArrayToString(boolean[] paraArray, char paraSeparator) {
		if ((paraArray == null) || (paraArray.length < 1)) {
			return "[]";
		} // Of if

		String returnString = "[";
		for (int i = 0; i < paraArray.length; i++) {
			returnString += "" + paraArray[i] + paraSeparator;
		} // Of for i
		returnString = returnString.substring(0, returnString.length() - 1);
		returnString += "]";

		return returnString;
	}// Of booleanArrayToString

	/**
	 *************************** 
	 * Convert a string array into a boolean array. Booleans are separated by
	 * separators. Only support 0 and 1 in the string. TRUE and FALSE are not
	 * supported.
	 * 
	 * @param paraString
	 *            The given string.
	 * @param paraSeparator
	 *            The separator of data, blank and commas are most commonly uses
	 *            ones.
	 * @return The constructed String.
	 * @throws Exception
	 *             for wrong format.
	 *************************** 
	 */
	public static boolean[] stringToBooleanArray(String paraString, char paraSeparator)
			throws Exception {
		paraString.trim();
		if ((paraString == null) || (paraString.length() < 1)) {
			throw new Exception("Error occurred in SimpleTool.stringToBooleanArray(String, char)."
					+ "\r\nThe given string is null.");
		} // Of if

		// Scan separators to find the size of the array
		int tempLength = 1;
		for (int i = 0; i < paraString.length(); i++) {
			if (paraString.charAt(i) == paraSeparator) {
				tempLength++;
			} // Of if
		} // Of for i

		// parse boolean values
		boolean[] resultArray = new boolean[tempLength];
		for (int i = 0; i < tempLength; i++) {
			if (paraString.charAt(i * 2) == '0') {
				resultArray[i] = false;
			} else if (paraString.charAt(i * 2) == '1') {
				resultArray[i] = true;
			} else {
				throw new Exception(
						"Error occurred in SimpleTool.stringToBooleanArray(String, char)."
								+ "\r\n Unsupported boolean value '" + paraString.charAt(i * 2)
								+ "'. It should be 0 or 1.");
			}
		} // Of for i
		return resultArray;
	}// Of booleanArrayToString

	/**
	 *************************** 
	 * Conver a boolean array into a string. Booleans are separated by commas.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @return The constructed String.
	 *************************** 
	 */
	public static String booleanArrayToAttributeSetString(boolean[] paraArray) {
		String returnString = "[";
		if ((paraArray == null) || (paraArray.length < 1)) {
			returnString += "]";
			return returnString;
		} // Of if

		for (int i = 0; i < paraArray.length; i++) {
			if (paraArray[i]) {
				returnString += i + ",";
			}
		} // Of for i
		returnString = returnString.substring(0, returnString.length() - 1);
		returnString += "]";

		return returnString;
	}// Of booleanArrayToAttributeSetString

	/**
	 *************************** 
	 * Read a int array from a given string. The number of ints are not given,
	 * so I will parse as many int values as possible. The seperator is not
	 * indicated, so comma is the first candidate, and blank is the second. <br>
	 * Author Fan Min.
	 * 
	 * @param paraString
	 *            The given string.
	 * @return The constructed array.
	 * @throws Exception
	 *             For format error.
	 *************************** 
	 */
	public static int[] parseIntArray(String paraString) throws Exception {
		char separator = ' ';
		String tempString = new String(paraString);
		String remainingString = new String(paraString);
		// System.out.println(tempString);

		if (tempString.lastIndexOf(":") != -1) {
			remainingString = tempString.substring(0, tempString.lastIndexOf(":"));
			// System.out.println(tempString.indexOf(":"));
		}
		if (paraString.indexOf(',') > 0)
			separator = ',';

		int[] tempArray = new int[MAX_PARSE_ARRAY_LENGTH];
		int arrayLength = 0;

		String currentString = null;
		// System.out.println("Test parseIntArray before");
		try {

			while (remainingString.indexOf(separator) > 0) {
				currentString = remainingString.substring(0, remainingString.indexOf(separator))
						.trim();
				tempArray[arrayLength] = Integer.parseInt(currentString);
				arrayLength++;
				if (arrayLength >= MAX_PARSE_ARRAY_LENGTH)
					throw new Exception(
							"The array length should not exceed " + MAX_PARSE_ARRAY_LENGTH);
				remainingString = remainingString.substring(remainingString.indexOf(separator) + 1)
						.trim();
			} // Of while

			tempArray[arrayLength] = Integer.parseInt(remainingString);
		} catch (java.lang.StringIndexOutOfBoundsException sie) {
			throw new Exception("Error occurred in common.SimpleTool.parseIntegerArray.\r\n"
					+ "May caused by the number of int value required exceeds those in the string,\r\n"
					+ "or invalid separator.\r\n" + sie);
		} catch (java.lang.NumberFormatException nfe) {
			throw new Exception("Error occurred in common.SimpleTool.parseIntegerArray.\r\n"
					+ "May caused by incorrect separator (e.g., comma, blank).\r\n" + nfe);
		} catch (Exception ee) {
			throw new Exception("Error occurred in common.SimpleTool.parseIntegerArray.\r\n" + ee);
		}

		int[] returnArray = new int[arrayLength + 1];

		// The arrayLength is starting from 0
		for (int i = 0; i <= arrayLength; i++)
			returnArray[i] = tempArray[i];
		// System.out.println("Test parseIntArray after");
		return returnArray;
	}// Of parseIntArray

	/**
	 *************************** 
	 * Read a double value from a given string after the colon. <br>
	 * 
	 * @param paraString
	 *            The given string.
	 * @return A double value.
	 * @throws Exception
	 *             If cannot be parsed.
	 *************************** 
	 */
	public static double parseDoubleValueAfterColon(String paraString) throws Exception {
		// char separator = ' ';
		double tempValue = 0;
		String tempString = new String(paraString);
		String currentString = null;
		// String remainingString = new String(paraString);
		currentString = tempString.substring(tempString.indexOf(":") + 1).trim();
		tempValue = Double.parseDouble(currentString);
		return tempValue;
	}// Of parseDoubleValueAfterColon

	/**
	 *************************** 
	 * Read an int value from a given string after the colon. <br>
	 * 
	 * @param paraString
	 *            The given string.
	 * @return A double value.
	 *************************** 
	 */
	public static int parseIntValueAfterColon(String paraString) {
		// char separator = ' ';
		int tempValue = 0;
		String tempString = new String(paraString);
		String currentString = null;
		// String remainingString = new String(paraString);
		currentString = tempString.substring(tempString.indexOf(":") + 1).trim();
		tempValue = Integer.parseInt(currentString);
		return tempValue;
	}// Of parseIntValueAfterColon

	/**
	 *************************** 
	 * Compress an int array so that no duplicate elements, no redundant elemnts
	 * exist, and it is in an ascendent order. <br>
	 * 
	 * @param paraIntArray
	 *            The given int array.
	 * @param paraLength
	 *            The effecitive length of the given int array.
	 * @return The constructed array.
	 *************************** 
	 */
	public static int[] compressAndSortIntArray(int[] paraIntArray, int paraLength) {
		int[] noDuplicateArray = new int[paraLength];
		int realLength = 0;
		int currentLeast = 0;
		int currentLeastIndex = 0;
		for (int i = 0; i < paraLength; i++) {
			if (paraIntArray[i] == Integer.MAX_VALUE) {
				continue;
			}

			currentLeast = paraIntArray[i];
			currentLeastIndex = i;

			for (int j = i + 1; j < paraLength; j++) {
				if (paraIntArray[j] < currentLeast) {
					currentLeast = paraIntArray[j];
					currentLeastIndex = j;
				} // Of if
			} // Of for j

			// Swap. The element of [i] should be stored in another place.
			paraIntArray[currentLeastIndex] = paraIntArray[i];

			noDuplicateArray[realLength] = currentLeast;
			realLength++;

			// Don't process this data any more.
			for (int j = i + 1; j < paraLength; j++) {
				if (paraIntArray[j] == currentLeast) {
					paraIntArray[j] = Integer.MAX_VALUE;
				} // Of if
			} // Of for j
		} // Of for i

		int[] compressedArray = new int[realLength];
		for (int i = 0; i < realLength; i++) {
			compressedArray[i] = noDuplicateArray[i];
		} // Of for i

		return compressedArray;
	}// Of compressAndSortIntArray

	/**
	 *************************** 
	 * Compress a long array so that no duplicate elements, no redundant elemnts
	 * exist, and it is in an ascendent order. <br>
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 * @param paraLength
	 *            The effecitive length of the given long array.
	 * @return The constructed array.
	 *************************** 
	 */
	public static long[] compressAndSortLongArray(long[] paraLongArray, int paraLength) {
		long[] noDuplicateArray = new long[paraLength];
		int realLength = 0;
		long currentLeast = 0;
		int currentLeastIndex = 0;
		for (int i = 0; i < paraLength; i++) {
			if (paraLongArray[i] == Long.MAX_VALUE) {
				continue;
			}

			currentLeast = paraLongArray[i];
			currentLeastIndex = i;

			for (int j = i + 1; j < paraLength; j++) {
				if (paraLongArray[j] < currentLeast) {
					currentLeast = paraLongArray[j];
					currentLeastIndex = j;
				} // Of if
			} // Of for j

			// Swap. The element of [i] should be stored in another place.
			paraLongArray[currentLeastIndex] = paraLongArray[i];

			noDuplicateArray[realLength] = currentLeast;
			realLength++;

			// Don't process this data any more.
			for (int j = i + 1; j < paraLength; j++) {
				if (paraLongArray[j] == currentLeast) {
					paraLongArray[j] = Long.MAX_VALUE;
				} // Of if
			} // Of for j
		} // Of for i

		long[] compressedLongArray = new long[realLength];
		for (int i = 0; i < realLength; i++) {
			compressedLongArray[i] = noDuplicateArray[i];
		} // Of for i

		return compressedLongArray;
	}// Of compressAndSortLongArray

	/**
	 ********************************** 
	 * Subreduct sort according to respective measure.
	 * 
	 * @param paraData
	 *            The data, it may represent a subreduct
	 * @param paraMeasuredValues
	 *            The measured values of the data
	 * @param paraLeft
	 *            The left index.
	 * @param paraRight
	 *            The right index.
	 * @throws Exception
	 *             For valueBasedLongArrayPartition().
	 ********************************** 
	 */
	public static void measureBasedQuickSort(long[] paraData, int[] paraMeasuredValues,
			int paraLeft, int paraRight) throws Exception {
		int pivotLoc = 0;
		if (paraLeft < paraRight) {
			pivotLoc = valueBasedLongArrayPartition(paraData, paraMeasuredValues, paraLeft,
					paraRight);
			measureBasedQuickSort(paraData, paraMeasuredValues, paraLeft, pivotLoc - 1);// For
																						// left
			measureBasedQuickSort(paraData, paraMeasuredValues, pivotLoc + 1, paraRight);// For
		} // Of if
	}// Of measureBasedQuickSort

	/**
	 ********************************** 
	 * Invoked only by measureBasedQuickSort.
	 * 
	 * @see #measureBasedQuickSort(long[], double[], int, int)
	 ********************************** 
	 */
	private static int valueBasedLongArrayPartition(long[] paraData, int[] paraMeasuredValues,
			int paraLeft, int paraRight) throws Exception {
		double key = paraMeasuredValues[paraLeft];
		int i = paraLeft;
		int j = paraRight + 1;

		while (true) {
			while (paraMeasuredValues[++i] < key && i < paraRight)
				;
			while (paraMeasuredValues[--j] > key)
				;
			if (i >= j)
				break;
			swap(paraMeasuredValues, i, j);
			swap(paraData, i, j);
		} // Of while

		swap(paraMeasuredValues, paraLeft, j);
		swap(paraData, paraLeft, j);
		return j;
	}// Of valueBasedLongArrayPartition

	/**
	 *************************** 
	 * Adjust a long array length.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 * @param length
	 *            The real length of the given long array.
	 * @return An identical long array.
	 *************************** 
	 */
	public static long[] adjustLongArrayLength(long[] paraLongArray, int length) {
		long[] arrayAim = new long[length];
		for (int i = 0; i < length; i++) {
			arrayAim[i] = paraLongArray[i];
		} // Of for i
		return arrayAim;
	}// Of adjustArrayLength

	/**
	 *************************** 
	 * Copy boolean array
	 * 
	 * @param paramBooleanArray
	 *            The given boolean array.
	 * @return An identical boolean array.
	 *************************** 
	 */
	public static boolean[] copyBooleanArray(boolean[] paramBooleanArray) {
		boolean[] newBooleanArray = new boolean[paramBooleanArray.length];
		for (int i = 0; i < paramBooleanArray.length; i++) {
			newBooleanArray[i] = paramBooleanArray[i];
		} // Of for
		return newBooleanArray;
	}// Of copyBooleanArray

	/**
	 *************************** 
	 * The exponential of an int.
	 * 
	 * @param paraExponential
	 *            the exponential.
	 * @return An identical boolean array.
	 *************************** 
	 */
	public static int exponential(int paraExponential) {
		int results = 1;
		for (int i = 0; i < paraExponential; i++) {
			results *= 2;
		} // Of if
		return results;
	}// Of exponential

	/**
	 *************************** 
	 * The exponential of an int.
	 * 
	 * @param paraExponential
	 *            the exponential.
	 * @return An identical boolean array.
	 *************************** 
	 */
	public static long exponentialLong(int paraExponential) {
		long results = 1;
		for (int i = 0; i < paraExponential; i++) {
			results *= 2;
		} // Of if
		return results;
	}// Of exponentialLong

	/**
	 *************************** 
	 * Who is who's child. Nodes are by integers, and attributes are indicated
	 * by bits.
	 * 
	 * @param paraBits
	 *            How many bits (attributes).
	 * @return An array of children.
	 *************************** 
	 */
	public static boolean[][] children(int paraBits) {
		int sizes = exponential(paraBits);

		boolean[][] children = new boolean[sizes][sizes];
		for (int i = 0; i < sizes; i++) {
			int j = i;
			int m = 1;
			while (j > 0) {
				if (j % 2 == 1) {
					children[i][i - m] = true;
				} // Of if
				j /= 2;
				m *= 2;
			} // Of while
		} // Of for

		return children;
	}// Of children

	/**
	 *************************** 
	 * Convert a long value to a integer array.
	 * 
	 * @param paraLong
	 *            The given long value.
	 * @param paraLength
	 *            The given length of the array.
	 * @return An integer array to indicate which positions (bits) are included.
	 *************************** 
	 */
	public static int[] longToIntArray(long paraLong, int paraLength) {
		int[] result = new int[paraLength];
		/*
		 * if (paraLong < 0) { return null; //No need to throw an exception. }
		 */
		int currentIndex = 0;
		long tempLong = paraLong;
		while (tempLong > 0) {
			if (tempLong % 2 == 1) {
				result[currentIndex] = 1;
			}

			tempLong /= 2;
			currentIndex++;
		} // Of while

		return result;
	}// Of longToIntArray

	/**
	 *************************** 
	 * Convert a long value to a boolean array.
	 * 
	 * @param paraLong
	 *            The given long value.
	 * @param paraLength
	 *            The given length of the array.
	 * @return A boolean array to indicate which positions (bits) are included.
	 *************************** 
	 */
	public static boolean[] longToBooleanArray(long paraLong, int paraLength) {
		long tempLong = paraLong;
		boolean[] returnArray = new boolean[paraLength];
		for (int i = 0; i < paraLength; i++) {
			if (tempLong % 2 == 1) {
				returnArray[i] = true;
			} else {
				returnArray[i] = false;
			} // Of if

			tempLong /= 2;
		} // Of for i

		return returnArray;
	}// Of longToBooleanArray

	/**
	 *************************** 
	 * Convert a boolean array to a long value.
	 * 
	 * @param paraBooleanArray
	 *            The given boolean array.
	 * @return A long to indicate which positions (bits) are included.
	 * @throws Exception
	 *             for array longer than that can be handled.
	 *************************** 
	 */
	public static long booleanArrayToLong(boolean[] paraBooleanArray) throws Exception {
		if (paraBooleanArray.length > 63) {
			throw new Exception("Cannot support the array with length more than 63.");
		} // Of if
		long resultLong = 0;
		long currentPositionValue = 1;
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				resultLong += currentPositionValue;
			} // Of if

			currentPositionValue *= 2;
		} // Of for i

		return resultLong;
	}// Of booleanArrayToLong

	/**
	 *************************** 
	 * Compute the attribute subset size.
	 * 
	 * @param paraLong
	 *            The given long value.
	 * @param paraLength
	 *            The size of all attributes.
	 * @return the size of the attribute subset.
	 *************************** 
	 */
	public static int attributeSubsetSize(long paraLong, int paraLength) {
		long tempLong = paraLong;
		int returnSize = 0;
		for (int i = 0; i < paraLength; i++) {
			if (tempLong % 2 == 1) {
				returnSize++;
			} // Of if

			tempLong /= 2;
		} // Of for i

		return returnSize;
	}// Of attributeSubsetSize

	/**
	 *************************** 
	 * Compute the attribute subset size.
	 * 
	 * @param paraBooleanArray
	 *            The given boolean array.
	 * @return the size of the attribute subset.
	 *************************** 
	 */
	public static int attributeSubsetSize(boolean[] paraBooleanArray) {
		int returnSize = 0;
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				returnSize++;
			} // Of if
		} // Of for i

		return returnSize;
	}// Of attributeSubsetSize

	/**
	 *************************** 
	 * Convert an integer value to a boolean array.
	 * 
	 * @param paraInt
	 *            The given long value.
	 * @param paraLength
	 *            The given length of the array.
	 * @return A boolean array to indicate which positions (bits) are included.
	 *************************** 
	 */
	public static boolean[] intToBooleanArray(int paraInt, int paraLength) {
		long tempInt = paraInt;
		boolean[] returnArray = new boolean[paraLength];
		for (int i = 0; i < paraLength; i++) {
			if (tempInt % 2 == 1) {
				returnArray[i] = true;
			} else {
				returnArray[i] = false;
			} // Of if

			tempInt /= 2;
		} // Of for i

		return returnArray;
	}// Of intToBooleanArray

	/**
	 *************************** 
	 * Convert a boolean array to an int value.
	 * 
	 * @param paraBooleanArray
	 *            The given boolean array.
	 * @return An integer to indicate which positions (bits) are included.
	 * @throws Exception
	 *             for long array.
	 *************************** 
	 */
	public static int booleanArrayToInt(boolean[] paraBooleanArray) throws Exception {
		if (paraBooleanArray.length > 31) {
			throw new Exception("Cannot support the array with length more than 31.");
		} // Of if

		int resultInt = 0;
		int currentPositionValue = 1;
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				resultInt += currentPositionValue;
			} // Of if

			currentPositionValue *= 2;
		} // Of for i

		return resultInt;
	}// Of booleanArrayToInt

	/**
	 *************************** 
	 * Print an int array, simply for test.
	 * 
	 * @param paraIntArray
	 *            The given int array.
	 *************************** 
	 */
	public static void printIntArray(int[] paraIntArray) {
		if (paraIntArray.length == 0) {
			System.out.println("This is an empty int array.");
			return;
		} else {
			System.out.print("This is an int array: ");
		}
		for (int i = 0; i < paraIntArray.length; i++) {
			System.out.print("" + paraIntArray[i] + "\t");
		} // Of for i
		System.out.println();
	}// Of paraIntArray

	/**
	 *************************** 
	 * Print a long array, simply for test.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 *************************** 
	 */
	public static void printLongArray(long[] paraLongArray) {
		for (int i = 0; i < paraLongArray.length; i++) {
			System.out.print("" + paraLongArray[i] + "\t");
		} // Of for i
	}// Of printLongArray

	/**
	 *************************** 
	 * Print a long array, zero is not printed. Simply for test.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 *************************** 
	 */
	public static void printLongArrayNoZero(long[] paraLongArray) {
		for (int i = 0; i < paraLongArray.length; i++) {
			if (paraLongArray[i] == 0)
				continue;
			System.out.print("" + paraLongArray[i] + "\t");
		} // Of for i
	}// Of printLongArrayNoZero

	/**
	 ************************* 
	 * Print all reducts.
	 * 
	 * @param paraAllReducts
	 *            The given array to print.
	 ************************* 
	 */
	public static void printAllReducts(boolean[][] paraAllReducts) {
		for (int i = 0; i < paraAllReducts.length; i++) {
			System.out.println();
			for (int j = 0; j < paraAllReducts[0].length; j++) {
				if (paraAllReducts[i][j]) {
					System.out.print("" + 1 + ",");
				} else {
					System.out.print("" + 0 + ",");
				}
			} // Of for j
		} // Of for i
	}// Of printAllReducts

	/**
	 ************************* 
	 * Print a boolean array.
	 * 
	 * @param paraBooleanArray
	 *            The boolean array.
	 ************************* 
	 */
	public static void printBooleanArray(boolean[] paraBooleanArray) {
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				System.out.print("" + 1 + ",");
			} else {
				System.out.print("" + 0 + ",");
			}

		} // Of for i
		System.out.println();
	}// Of printBooleanArray

	/**
	 ************************* 
	 * Print a boolean matrix.
	 * 
	 * @param paraBooleanMatrix
	 *            The boolean matrix.
	 ************************* 
	 */
	public static void printBooleanMatrix(boolean[][] paraBooleanMatrix) {
		for (int i = 0; i < paraBooleanMatrix.length; i++) {
			for (int j = 0; j < paraBooleanMatrix[i].length; j++) {
				if (paraBooleanMatrix[i][j]) {
					System.out.print("1,");
				} else {
					System.out.print("0,");
				} // Of if
			} // Of for j
			System.out.println();
		} // Of for i
		System.out.println();
	}// Of printBooleanMatrix

	/**
	 ************************* 
	 * Print a double array.
	 * 
	 * @param paraDoubleArray
	 *            The given array.
	 ************************* 
	 */
	public static void printDoubleArray(double[] paraDoubleArray) {
		for (int i = 0; i < paraDoubleArray.length; i++) {
			System.out.print(paraDoubleArray[i] + " ");
		} // Of for i
		System.out.println();
	}// Of printDoubleArray

	/**
	 ********************************** 
	 * Swap two value in a double array.
	 * 
	 * @param paraDoubleArray
	 *            The given double array.
	 * @param src
	 *            The first index of the double array.
	 * @param dest
	 *            The second index of the double array.
	 * @throws Exception
	 *             for index out of bounds.
	 ********************************** 
	 */
	public static void swap(double[] paraDoubleArray, int src, int dest) throws Exception {
		double tempDoubleArray = 0;
		tempDoubleArray = paraDoubleArray[src];
		paraDoubleArray[src] = paraDoubleArray[dest];
		paraDoubleArray[dest] = tempDoubleArray;
	}// Of swap

	/**
	 ********************************** 
	 * Swap two value in a long array.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 * @param src
	 *            The first index of the long array.
	 * @param dest
	 *            The second index of the long array.
	 * @throws Exception
	 *             if index out of bounds.
	 ********************************** 
	 */
	public static void swap(long[] paraLongArray, int src, int dest) throws Exception {
		long tempIntArray = 0;
		tempIntArray = paraLongArray[src];
		paraLongArray[src] = paraLongArray[dest];
		paraLongArray[dest] = tempIntArray;
	}// Of swap

	/**
	 ********************************** 
	 * Swap two value in a long array.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 * @param src
	 *            The first index of the long array.
	 * @param dest
	 *            The second index of the long array.
	 * @throws Exception
	 *             For index out of bounds.
	 ********************************** 
	 */
	public static void swap(int[] paraLongArray, int src, int dest) throws Exception {
		int tempIntArray = 0;
		tempIntArray = paraLongArray[src];
		paraLongArray[src] = paraLongArray[dest];
		paraLongArray[dest] = tempIntArray;
	}// Of swap

	/**
	 ********************************** 
	 * Long array to boolean matrix.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 * @param paraLength
	 *            The length of each long.
	 * @return the boolean matrix
	 ********************************** 
	 */
	public static boolean[][] longArrayToBooleanMatrix(long[] paraLongArray, int paraLength) {
		boolean[] availableAttribute = new boolean[paraLength];
		boolean[][] paraAllSubreducts = new boolean[paraLongArray.length][paraLength];

		for (int i = 0; i < paraLongArray.length; i++) {
			availableAttribute = longToBooleanArray(paraLongArray[i], paraLength);
			for (int j = 0; j < paraLength; j++) {
				if (availableAttribute[j]) {
					paraAllSubreducts[i][j] = true;
				} // Of if
			} // Of for j
		} // of for i
		return paraAllSubreducts;
	}// Of swap

	/**
	 ***********************************
	 * Write a message to a new file.
	 * 
	 * @paraFilename The given filename.
	 * @paraMessage The givean message string.
	 ***********************************
	 */
	public static void writeFile(String paraFilename, String paraMessage) throws Exception {
		File resultFile = new File(paraFilename);
		if (resultFile.exists()) {
			resultFile.delete();
		}
		resultFile.createNewFile();
		PrintWriter writer = new PrintWriter(new FileOutputStream(resultFile));
		writer.print(paraMessage);
		writer.flush();
		writer.close();
	}// Of writeFile

	/**
	 ********************************** 
	 * Is the first set a subset of the second one.
	 * 
	 * @param paraFirstSet
	 *            The first set in long.
	 * @param paraSecondSet
	 *            The second set in long.
	 * @param paraAttributes
	 *            Number of attributes.
	 * @return is the first set a subset of the second one?
	 * @throws Exception
	 *             That is generated by longToBooleanArray() or isSubset().
	 ********************************** 
	 */
	public static boolean isSubset(long paraFirstSet, long paraSecondSet, int paraAttributes)
			throws Exception {
		boolean[] firstSetBooleanArray = longToBooleanArray(paraFirstSet, paraAttributes);
		boolean[] secondSetBooleanArray = longToBooleanArray(paraSecondSet, paraAttributes);
		return isSubset(firstSetBooleanArray, secondSetBooleanArray);
	}// Of isSubset

	/**
	 ********************************** 
	 * Is the first set a subset of the second one.
	 * 
	 * @param paraFirstSet
	 *            The first set in int array.
	 * @param paraSecondSet
	 *            The second set in int array.
	 * @return is the first set a subset of the second one?
	 * @throws Exception
	 *             for emptyset.
	 ********************************** 
	 */
	public static boolean isSubset(int[] paraFirstSet, int[] paraSecondSet) throws Exception {
		Common.runSteps++;
		if ((paraFirstSet.length > paraSecondSet.length)
				|| (paraFirstSet[paraFirstSet.length - 1] > paraSecondSet[paraSecondSet.length - 1])
				|| paraSecondSet[paraSecondSet.length - 1] > 100) {
			return false;
		} // Of if

		int indexInTheFirstSet = 0;
		int indexInTheSecondSet = 0;
		while (indexInTheFirstSet < paraFirstSet.length) {
			Common.runSteps++;
			if (paraFirstSet[indexInTheFirstSet] > paraSecondSet[indexInTheSecondSet]) {
				indexInTheSecondSet++;
			} else if (paraFirstSet[indexInTheFirstSet] < paraSecondSet[indexInTheSecondSet]) {
				return false;
			} else {
				indexInTheFirstSet++;
				indexInTheSecondSet++;
			} // Of if
		} // Of while

		return true;
	}// Of isSubset

	/**
	 ********************************** 
	 * Is the first set a subset of the second one.
	 * 
	 * @param paraFirstSet
	 *            The first set in boolean array.
	 * @param paraSecondSet
	 *            The second set in boolean array.
	 * @return is the first set a subset of the second one?
	 * @throws Exception
	 *             For empty sets.
	 ********************************** 
	 */
	public static boolean isSubset(boolean[] paraFirstSet, boolean[] paraSecondSet)
			throws Exception {
		if (paraFirstSet.length != paraFirstSet.length) {
			throw new Exception("Error occurred in SimpleTool.isSubset(). Boolean arrays should"
					+ " have the same length");
		} // Of if

		for (int i = 0; i < paraFirstSet.length; i++) {
			if (paraFirstSet[i] && !paraSecondSet[i]) {
				return false;
			} // Of if
		} // Of for i
		return true;
	}// Of isSubset

	/**
	 ********************************** 
	 * Convert a long value to a bit string.
	 * 
	 * @param paraLong
	 *            The given long value.
	 * @return the bit string.
	 ********************************** 
	 */
	public static String longToBitString(long paraLong) {
		String returnString = "";
		while (paraLong > 0) {
			if (paraLong % 2 == 1) {
				returnString = "1" + returnString;
			} else {
				returnString = "0" + returnString;
			} // Of if
			paraLong /= 2;
		} // Of while
		return returnString;
	}// Of longToBitString

	/**
	 ********************************** 
	 * Return the size of the subset. It is the true values in the array.
	 * 
	 * @param paraSubset
	 *            A subset with the form of a boolean array.
	 * @return The size.
	 ********************************** 
	 */
	public static int getSubsetSize(boolean[] paraSubset) {
		int tempCounter = 0;
		for (int i = 0; i < paraSubset.length; i++) {
			if (paraSubset[i]) {
				tempCounter++;
			}
		} // Of if
		return tempCounter;
	}// Of getSubsetSize

	/**
	 ********************************** 
	 * Return the size of the subset. It is the true values in the array.
	 * 
	 * @param paraSubset
	 *            A subset with the form of a long.
	 * @return The size.
	 ********************************** 
	 */
	public static int getSubsetSize(long paraSubset) {
		long tempLong = paraSubset;
		int tempCounter = 0;
		while (tempLong > 0) {
			if (tempLong % 2 == 1) {
				tempCounter++;
			} // Of if

			tempLong /= 2;
		} // Of for i

		return tempCounter;
	}// Of getSubsetSize

	/**
	 *************************** 
	 * Check Who is who's subset.
	 * 
	 * @param paraFirstSet
	 *            the first set in long.
	 * @param paraSecondSet
	 *            the second set in long.
	 * @param paraNumberOfConditions
	 *            the number of conditions.
	 * @return '0' means no relationship, '1' means the second is the subset,
	 *         '2' means the first is the subset, '3' means equal.
	 *************************** 
	 */
	public static char subSetCheck(long paraFirstSet, long paraSecondSet,
			int paraNumberOfConditions) {
		boolean supportPositive = true;
		boolean supportNegative = true;

		int[] firstSubset = SimpleTools.longToIntArray(paraFirstSet, paraNumberOfConditions);
		int[] secondSubset = SimpleTools.longToIntArray(paraSecondSet, paraNumberOfConditions);

		for (int i = 0; i < paraNumberOfConditions; i++) {
			if (firstSubset[i] - secondSubset[i] > 0) {
				supportPositive = false;
			} else if (firstSubset[i] - secondSubset[i] < 0) {
				supportNegative = false;
			} // Of if
		} // Of for i

		if (!supportPositive && !supportNegative) {
			return '0'; // The two sets with not inclusion raletionship
		} // Of if
		if (!supportPositive) {
			return '1'; // The second set is the child of the first set.
		} // Of if
		if (!supportNegative) {
			return '2'; // The first set is the child of the second set.
		} // Of if

		return '3'; // The two sets is equal to each other.
	}// Of subSetCheck

	/**
	 ********************************** 
	 * Convert a boolean matrix to string
	 * 
	 * @param paraMatrix
	 *            The boolean matrix.
	 * @return The string.
	 ********************************** 
	 */
	public static String booleanMatrixToString(boolean[][] paraMatrix) {
		String tempString = "";
		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[0].length; j++) {
				if (paraMatrix[i][j]) {
					tempString += " 1";
				} else {
					tempString += " 0";
				} // Of if
			} // Of for j
			tempString += "\r\n";
		} // Of for i
		return tempString;
	}// Of booleanMatrixToString

	/**
	 ********************************** 
	 * Generate a boolean array to divide a dataset in two
	 * 
	 * @param paraDatasetSize
	 *            the dataset size
	 * @param paraPercentage
	 *            the percentage of the first subset.
	 * @return The result boolean array.
	 * @throws Exception
	 *             If the percentage exceeds lower/upper bounds.
	 ********************************** 
	 */
	public static boolean[] generateBooleanArrayForDivision(int paraDatasetSize,
			double paraPercentage) throws Exception {
		double percentageLowerBound = 1.0 / paraDatasetSize;
		double percentageUpperBound = 1 - percentageLowerBound;
		if ((paraPercentage < percentageLowerBound) || (paraPercentage > percentageUpperBound)) {
			throw new Exception("Error occurred in SimpleTool.generateForPartitionInTwo().\r\n"
					+ "The valid bound of the percentage should be in [" + percentageLowerBound
					+ ", " + percentageUpperBound + "].");
		} // Of if

		boolean[] tempBooleanArray = new boolean[paraDatasetSize];
		int firstSetSize = (int) (paraDatasetSize * paraPercentage);
		int secondSetSize = paraDatasetSize - firstSetSize;

		int firstSetCurrentSize = 0;
		int secondSetCurrentSize = 0;

		int tempInt;

		for (int i = 0; i < paraDatasetSize; i++) {
			tempInt = random.nextInt(paraDatasetSize);
			if (tempInt < firstSetSize) {
				tempBooleanArray[i] = true;
				firstSetCurrentSize++;
			} else {
				tempBooleanArray[i] = false;
				secondSetCurrentSize++;
			} // Of if

			// Keep the remaining part to false (the default value)
			if (firstSetCurrentSize >= firstSetSize) {
				break;
			} // Of if

			// Set the remaining part to true
			if (secondSetCurrentSize >= secondSetSize) {
				for (i++; i < paraDatasetSize; i++) {
					tempBooleanArray[i] = true;
				} // Of for i
				break;
			} // Of if
		} // Of for i
			// for (int i = 0; i < firstSetSize; i++) {
			// tempBooleanArray[i] = true;
			// }// Of for i

		return tempBooleanArray;
	}// Of generateBooleanArrayForDivision

	/**
	 ********************************** 
	 * Revert a boolean array.
	 * 
	 * @param paraArray
	 *            the given array
	 * @return reverted array.
	 ********************************** 
	 */
	public static boolean[] revertBooleanArray(boolean[] paraArray) {
		boolean[] tempArray = new boolean[paraArray.length];
		for (int i = 0; i < paraArray.length; i++) {
			tempArray[i] = !paraArray[i];
		} // Of for i
		return tempArray;
	}// Of revertBooleanArray

	/**
	 ********************************** 
	 * Remove supersets, used to obtain reducts.
	 * 
	 * @param paraSets
	 *            the subsets with each one represented by a long value.
	 * @param paraNumberOfConditions
	 *            the number of condition of the dataset
	 * @return a set of reducts without superflous attributes.
	 ********************************** 
	 */
	public static long[] removeSupersets(long[] paraSets, int paraNumberOfConditions) {
		char check = 0;

		// Check who is who's child.
		for (int i = 0; i < paraSets.length - 1; i++) {
			if (paraSets[i] == -1) {
				continue;
			} // Of if
			for (int j = i + 1; j < paraSets.length; j++) {
				if (paraSets[j] == -1) {
					continue;
				} // Of if

				check = SimpleTools.subSetCheck(paraSets[i], paraSets[j], paraNumberOfConditions);
				if (check == '1') {
					paraSets[i] = -1;
					break;
				} // Of if

				if (check == '2') {
					paraSets[j] = -1;
				} // Of if
			} // Of for j
		} // Of for i

		int numberRemainingSubsets = 0;
		for (int i = 0; i < paraSets.length; i++) {
			if (paraSets[i] > 0) {
				numberRemainingSubsets++;
			} // Of if
		} // Of for i

		long[] returnArray = new long[numberRemainingSubsets];
		int m = 0;
		for (int i = 0; i < paraSets.length; i++) {
			if (paraSets[i] > 0) {
				returnArray[m] = paraSets[i];
				m++;
			} // Of if
		} // Of for i

		return returnArray;
	}// Of removeSupersets

	/**
	 ********************************** 
	 * Set intersection. The sets should be sorted.
	 * 
	 * @param paraFirstSet
	 *            The first set indicated by numbers for indices.
	 * @param paraSecondSet
	 *            The second set indicated by numbers for indices.
	 * @return a set for the intersection.
	 * @throws Exception
	 *             For unordered data.
	 ********************************** 
	 */
	public static int[] setIntersection(int[] paraFirstSet, int[] paraSecondSet) throws Exception {
		int[] emptyArray = new int[0];
		if ((paraFirstSet.length == 0) || (paraSecondSet.length == 0)) {
			return emptyArray;
		}

		if (!isAscendingOrder(paraFirstSet)) {
			throw new Exception("The first array is not in an ascending order: "
					+ intArrayToString(paraFirstSet, ','));
		}
		if (!isAscendingOrder(paraSecondSet)) {
			throw new Exception("The second array is not in an ascending order: "
					+ intArrayToString(paraSecondSet, ','));
		}

		int[] tempSet = new int[paraFirstSet.length + paraSecondSet.length];
		int tempLength = 0;
		int firstIndex = 0;
		int secondIndex = 0;
		while ((firstIndex < paraFirstSet.length) && (secondIndex < paraSecondSet.length)) {
			Common.runSteps++;
			if (paraFirstSet[firstIndex] < paraSecondSet[secondIndex]) {
				firstIndex++;
			} else if (paraFirstSet[firstIndex] > paraSecondSet[secondIndex]) {
				secondIndex++;
			} else {
				tempSet[tempLength] = paraFirstSet[firstIndex];
				firstIndex++;
				secondIndex++;
				tempLength++;
			} // Of if
		} // Of while

		int[] resultSet = new int[tempLength];
		for (int i = 0; i < tempLength; i++) {
			resultSet[i] = tempSet[i];
		} // Of for

		return resultSet;
	}// Of setIntersection

	/**
	 ********************************** 
	 * Is the given array in an ascending order?
	 * 
	 * @param paraArray
	 *            The int array.
	 * @return true if it is ascending (equal values permitted).
	 ********************************** 
	 */
	public static boolean isAscendingOrder(int[] paraArray) {
		for (int i = 0; i < paraArray.length - 1; i++) {
			if (paraArray[i] > paraArray[i + 1]) {
				return false;
			} // Of if
		} // Of for
		return true;
	}// Of isAscendingOrder

	/**
	 *************************** 
	 * Judge whether or not the given boolean matrix is upper triangle one.
	 * 
	 * @param paraMatrix
	 *            The given boolean matrix.
	 * @return The constructed String.
	 *************************** 
	 */
	public static boolean isUpperTriangleBooleanMatrix(boolean[][] paraMatrix) {
		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j <= i; j++) {
				if (paraMatrix[i][j]) {
					return false;
				} // Of if
			} // Of for j
		} // Of for i

		return true;
	}// Of isUpperTriangleBooleanMatrix

	/**
	 ************************* 
	 * Print a boolean array as IDs.
	 * 
	 * @param paraBooleanArray
	 *            The given array.
	 ************************* 
	 */
	public static void printBooleanArrayAsID(boolean[] paraBooleanArray) {
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				System.out.print(i + ", ");
			} // Of if
		} // Of for i
		System.out.println();
	}// Of printBooleanArrayAsID

	/**
	 ************************* 
	 * Compute the size of the intersection of two sets. These sets should be
	 * ordered from small to big.
	 * 
	 * @param paraFirstSet
	 *            The indices of the first set.
	 * @param paraSecondSet
	 *            The indices of the second set.
	 * @return the intersection size
	 ************************* 
	 */
	public static int intersectionSize(int[] paraFirstSet, int[] paraSecondSet) {
		int resultSize = 0;
		int tempFirstIndex = 0;
		int tempSecondIndex = 0;

		while ((tempFirstIndex < paraFirstSet.length) && (tempSecondIndex < paraSecondSet.length)) {
			if (paraFirstSet[tempFirstIndex] == paraSecondSet[tempSecondIndex]) {
				resultSize++;
				tempFirstIndex++;
				tempSecondIndex++;
			} else if (paraFirstSet[tempFirstIndex] < paraSecondSet[tempSecondIndex]) {
				tempFirstIndex++;
			} else {
				tempSecondIndex++;
			} // Of if
		} // Of while

		return resultSize;
	}// Of intersectionSize

	/**
	 ************************* 
	 * Compute the intersection of two sets. These sets should be ordered from
	 * small to big.
	 * 
	 * @param paraFirstSet
	 *            The indices of the first set.
	 * @param paraSecondSet
	 *            The indices of the second set.
	 * @return the intersection. May be an empty array.
	 ************************* 
	 */
	public static int[] intersection(int[] paraFirstSet, int[] paraSecondSet) {
		int tempSize = intersectionSize(paraFirstSet, paraSecondSet);
		int[] resultArray = new int[tempSize];
		int tempIndex = 0;

		int tempFirstIndex = 0;
		int tempSecondIndex = 0;

		while ((tempFirstIndex < paraFirstSet.length) && (tempSecondIndex < paraSecondSet.length)) {
			if (paraFirstSet[tempFirstIndex] == paraSecondSet[tempSecondIndex]) {
				resultArray[tempIndex] = paraFirstSet[tempFirstIndex];
				tempIndex++;
				tempFirstIndex++;
				tempSecondIndex++;
			} else if (paraFirstSet[tempFirstIndex] < paraSecondSet[tempSecondIndex]) {
				tempFirstIndex++;
			} else {
				tempSecondIndex++;
			} // Of if
		} // Of while

		return resultArray;
	}// Of intersection

	/**
	 ************************* 
	 * Compute the maximal intersections of two set families. These sets
	 * families should be composed by exactly two sets.
	 * 
	 * @param paraFirstSetFamily
	 *            The first set family, e.g., [[1, 3, 5, 7], [0, 2, 4, 6]].
	 * @param paraSecondSetFamily
	 *            The second set family, e.g., [[1, 2, 3], [0, 4, 5, 6, 7]].
	 * @return the maximal intersection family.
	 ************************* 
	 */
	public static int[][] binarySetFamilyMaximalIntersection(int[][] paraFirstSetFamily,
			int[][] paraSecondSetFamily) {
		int[][] resultSetFamily = new int[2][];
		int tempIntersectionSize = intersectionSize(paraFirstSetFamily[0], paraSecondSetFamily[0]);

		if (tempIntersectionSize > paraFirstSetFamily[0].length / 2) {
			// A1 cap B1, A2 cap B2
			resultSetFamily[0] = intersection(paraFirstSetFamily[0], paraSecondSetFamily[0]);
			resultSetFamily[1] = intersection(paraFirstSetFamily[1], paraSecondSetFamily[1]);
		} else {
			// A1 cap B2, A2 cap B1
			resultSetFamily[0] = intersection(paraFirstSetFamily[0], paraSecondSetFamily[1]);
			resultSetFamily[1] = intersection(paraFirstSetFamily[1], paraSecondSetFamily[0]);
		} // Of if

		return resultSetFamily;
	}// Of binarySetFamilyMaximalIntersection

	/**
	 ************************* 
	 * Are double matrices equal?
	 * 
	 * @param paraMatrix1
	 *            The first matrix.
	 * @param paraMatrix2
	 *            The second matrix.
	 * @return True if equal.
	 ************************* 
	 */
	public static boolean doubleMatricesEqual(double[][] paraMatrix1, double[][] paraMatrix2) {
		if (paraMatrix1.length != paraMatrix2.length) {
			return false;
		} // Of if
		if (paraMatrix1[0].length != paraMatrix2[0].length) {
			return false;
		} // Of if

		for (int i = 0; i < paraMatrix1.length; i++) {
			for (int j = 0; j < paraMatrix1[0].length; j++) {
				if (Math.abs(paraMatrix1[i][j] - paraMatrix2[i][j]) > 1e-6) {
					return false;
				} // Of if
			} // Of if
		} // Of for i

		return true;
	}// Of doubleMatricesEqual

	/**
	 ************************* 
	 * Test the method doubleMatricesEqual
	 ************************* 
	 */
	public static void testDoubleMatricesEqual() {
		double[][] tempMatrix1 = { { 1.2, 2.3, 3.33298999 }, { 2.1, 2.2, 2.4 } };
		double[][] tempMatrix2 = { { 1.2, 2.3, 3.333 }, { 2.1, 2.2, 2.4 } };

		boolean tempEqual = doubleMatricesEqual(tempMatrix1, tempMatrix2);
		System.out.println("Are the matrices equal? " + tempEqual);
	}// Of testDoubleMatricesEqual

	/**
	 ************************* 
	 * Normalize a decision system. Attention: the given data will be changed!
	 * 
	 * @param paraData
	 *            The given data.
	 ************************* 
	 */
	public static void normalizeDecisionSystem(Instances paraData) {
		double tempMin, tempMax;
		double tempValue, tempNewValue;
		// Normalize each attribute except the last one (decision)
		for (int i = 0; i < paraData.numAttributes() - 1; i++) {
			tempMin = Double.MAX_VALUE;
			tempMax = Double.MIN_VALUE;
			// The first scan for minimal/maximal values.
			for (int j = 0; j < paraData.numInstances(); j++) {
				tempValue = paraData.instance(j).value(i);
				if (tempValue < tempMin) {
					tempMin = tempValue;
				} // Of if

				if (tempValue > tempMax) {
					tempMax = tempValue;
				} // Of if
			} // Of for j

			if (tempMax == tempMin) {
				System.out.println("Fatal error occurred in SimpleTools.normalize()! Attribute #"
						+ i + " has only one value.");
				System.exit(0);
			} // Of if

			// The second scan for change the values.
			for (int j = 0; j < paraData.numInstances(); j++) {
				tempNewValue = (paraData.instance(j).value(i) - tempMin) / (tempMax - tempMin);
				paraData.instance(j).setValue(i, tempNewValue);
			} // Of for j
		} // Of for i

		consoleOutput("The normalized data is: \r\n" + paraData);
	}// Of normalizeDecisionSystem

	/**
	 ************************* 
	 * Get the index of the maximal value.
	 * 
	 * @param paraArray
	 *            The given array.
	 * @return The index.
	 ************************* 
	 */
	public static int getMaxIndex(int[] paraArray) {
		int tempMaxValue = -1;
		int tempMaxValueIndex = -1;
		for (int i = 0; i < paraArray.length; i++) {
			if (tempMaxValue < paraArray[i]) {
				tempMaxValue = paraArray[i];
				tempMaxValueIndex = i;
			} // Of if
		} // Of for i
		return tempMaxValueIndex;
	}// Of getMaxIndex

	/**
	 ************************* 
	 * Is the given index in the block?
	 * 
	 * @param paraBlock
	 *            The given block which is sorted in ascending order. In this
	 *            way we can use binary searching.
	 * @param paraInt
	 *            The given index.
	 * @return In block or not.
	 ************************* 
	 */
	public static boolean inBlock(int[] paraBlock, int paraInt) {
		boolean resultInBlock = false;
		int tempLeft = 0;
		int tempRight = paraBlock.length - 1;
		int tempMiddle;

		while (tempLeft <= tempRight) {
			tempMiddle = (tempLeft + tempRight) / 2;
			if (paraBlock[tempMiddle] == paraInt) {
				resultInBlock = true;
				break;
			} else if (paraBlock[tempMiddle] < paraInt) {
				tempLeft = tempMiddle + 1;
			} else {
				tempRight = tempMiddle - 1;
			} // Of if
		} // Of while

		return resultInBlock;
	}// Of inBlock

	/**
	 ********************************** 
	 * Get a random order index array.
	 * 
	 * @param paraLength
	 *            The length of the array.
	 * @return A random order.
	 ********************************** 
	 */
	public static int[] getRandomOrder(int paraLength) {
		// Step 1. Initialize
		int[] resultArray = new int[paraLength];
		for (int i = 0; i < paraLength; i++) {
			resultArray[i] = i;
		} // Of for i

		// Step 2. Swap many times
		int tempFirst, tempSecond;
		int tempValue;
		for (int i = 0; i < paraLength * 10; i++) {
			tempFirst = random.nextInt(paraLength);
			tempSecond = random.nextInt(paraLength);

			tempValue = resultArray[tempFirst];
			resultArray[tempFirst] = resultArray[tempSecond];
			resultArray[tempSecond] = tempValue;
		} // Of for i

		return resultArray;
	}// Of getRandomOrder

	/**
	 ********************************** 
	 * Disorder a dataset, so that the order does not influence the results.
	 * 
	 * @param paraFilename
	 *            The given filename.
	 ********************************** 
	 */
	public static void disorderData(String paraFilename) {
		// Step 1. Read the data.
		Instances tempData = null;
		try {
			FileReader fileReader = new FileReader(paraFilename);
			tempData = new Instances(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			System.out.println("Cannot read the file: " + paraFilename + "\r\n" + ee);
			System.exit(0);
		} // Of try

		// Step 2. Disorder.
		int[] tempNewOrders = getRandomOrder(tempData.numInstances());
		// Copy.
		Instances tempNewData = new Instances(tempData);
		tempNewData.delete();
		System.out.println("The empty data is: " + tempNewData);
		for (int i = 0; i < tempNewOrders.length; i++) {
			tempNewData.add(tempData.instance(tempNewOrders[i]));
		} // Of for i

		System.out.println("Writing to a new file:");
		// Step 3. Write to a new file.
		int tempLength = paraFilename.length();
		String tempNewFilename = paraFilename.substring(0, tempLength - 5) + "_disorder.arff";
		System.out.println(tempNewFilename);
		try {
			RandomAccessFile tempNewFile = new RandomAccessFile(tempNewFilename, "rw");
			tempNewFile.writeBytes(tempNewData.toString());
			tempNewFile.close();
		} catch (IOException ee) {
			System.out.println(ee);
		} // Of try
	}// Of disorderData

	/**
	 ********************************** 
	 * Disorder a dataset, so that the order does not influence the results.
	 * 
	 * @param paraData
	 *            The given dataset.
	 ********************************** 
	 */
	public static void disorderData(Instances paraData) {
		// Step 1. New orders.
		int[] tempNewOrders = getRandomOrder(paraData.numInstances());

		// Step 2. Copy.
		Instances tempData = new Instances(paraData);
		paraData.delete();

		for (int i = 0; i < tempNewOrders.length; i++) {
			paraData.add(tempData.instance(tempNewOrders[i]));
		} // Of for i
	}// Of disorderData

	/**
	 *************************** 
	 * Recommending times array to distribution array. For example, given the
	 * times each movie has been rated, obtain the number of movies that has
	 * been rated respective times.
	 * 
	 * @param paraTimesArray
	 *            the array indicating how many times each item has been rated
	 * @return a distribution array, the length is determined by the maximal
	 *         time rated
	 *************************** 
	 */
	public static int[] recommendTimesToDistribution(int[] paraTimesArray) throws Exception {
		// Scan one time to obtain the maximal value
		int tempMaxTimes = 0;
		for (int i = 0; i < paraTimesArray.length; i++) {
			if (tempMaxTimes < paraTimesArray[i]) {
				tempMaxTimes = paraTimesArray[i];
			} // Of if
		} // Of for i

		// Scan the second time for statistics
		int[] returnArray = new int[tempMaxTimes + 1];
		for (int i = 0; i < paraTimesArray.length; i++) {
			returnArray[paraTimesArray[i]]++;
		} // Of for i

		return returnArray;
	}// Of recommendTimesToDistribution

	/**
	 ********************************** 
	 * Generate a random sequence of [0, n - 1].
	 * 
	 * @author Hengru Zhang, Revised by Fan Min 2013/12/24
	 * 
	 * @param paraLength
	 *            the length of the sequence
	 * @return an array of non-repeat random numbers in [0, paraLength - 1].
	 ********************************** 
	 */
	public static int[] generateRandomSequence(int paraLength) {
		// Initialize
		int[] tempResultArray = new int[paraLength];
		for (int i = 0; i < paraLength; i++) {
			tempResultArray[i] = i;
		} // Of for i

		// Swap some elements
		int tempFirstIndex, tempSecondIndex, tempValue;
		for (int i = 0; i < paraLength / 2; i++) {
			tempFirstIndex = random.nextInt(paraLength);
			tempSecondIndex = random.nextInt(paraLength);

			// Really swap elements in these two indices
			tempValue = tempResultArray[tempFirstIndex];
			tempResultArray[tempFirstIndex] = tempResultArray[tempSecondIndex];
			tempResultArray[tempSecondIndex] = tempValue;
		} // Of for i

		return tempResultArray;
	}// Of generateRandomSequence

	/**
	 ********************************** 
	 * Generating random indices.
	 * 
	 * @author Fan Min 2013/12/24
	 * 
	 * @param paraSequenceLength
	 *            the length of the sequence
	 * @param paraSelectionLength
	 *            the length of the selection
	 * @return a sorted array of non-repeat random numbers in [0,
	 *         paraSequenceLength - 1].
	 ********************************** 
	 */
	public static int[] generateRandomIndices(int paraSequenceLength, int paraSelectionLength)
			throws Exception {
		if (paraSequenceLength < paraSequenceLength) {
			throw new Exception("Error occurred in SimpleTool.generateRandomIndices()\r\n"
					+ "The selection length is greater than the sequence length.");
		} // Of if

		// Generate a random sequence
		int[] tempSequence = generateRandomSequence(paraSequenceLength);

		// Initialize
		int[] tempResultArray = new int[paraSelectionLength];

		// Sort and pickup the first paraSelectionLength elements
		boolean[] tempSelectedArray = new boolean[paraSelectionLength];
		int tempMinimalIndex = 0;
		int tempMinimalValue;
		for (int i = 0; i < paraSelectionLength; i++) {
			tempMinimalValue = Integer.MAX_VALUE;
			for (int j = 0; j < paraSelectionLength; j++) {
				if (!tempSelectedArray[j] && (tempSequence[j] < tempMinimalValue)) {
					tempMinimalValue = tempSequence[j];
					tempMinimalIndex = j;
				} // Of if
			} // Of for j

			tempResultArray[i] = tempMinimalValue;
			tempSelectedArray[tempMinimalIndex] = true;
		} // Of for i

		return tempResultArray;
	}// Of generateRandomIndices

	/**
	 ************************* 
	 * Test the method.
	 ************************* 
	 */
	public static void testInBlock() {
		int[] tempBlock = { 1, 3, 5, 8, 149 };
		int tempInt = 3;
		boolean tempInBlock = inBlock(tempBlock, tempInt);
		System.out.println(
				"" + tempInt + " in the block " + Arrays.toString(tempBlock) + "? " + tempInBlock);

		tempInt = 6;
		tempInBlock = inBlock(tempBlock, tempInt);
		System.out.println(
				"" + tempInt + " in the block " + Arrays.toString(tempBlock) + "? " + tempInBlock);

		tempInt = 9;
		tempInBlock = inBlock(tempBlock, tempInt);
		System.out.println(
				"" + tempInt + " in the block " + Arrays.toString(tempBlock) + "? " + tempInBlock);

		tempInt = 0;
		tempInBlock = inBlock(tempBlock, tempInt);
		System.out.println(
				"" + tempInt + " in the block " + Arrays.toString(tempBlock) + "? " + tempInBlock);

		tempInt = 149;
		tempInBlock = inBlock(tempBlock, tempInt);
		System.out.println(
				"" + tempInt + " in the block " + Arrays.toString(tempBlock) + "? " + tempInBlock);
	}// Of testInBlock

	/**
	 ************************* 
	 * Test the method.
	 ************************* 
	 */
	public static void testBinarySetFamilyMaximalIntersection() {
		int[][] tempFirstSetFamily = { { 1, 3, 5, 7 }, { 0, 2, 4, 6 } };
		int[][] tempSecondSetFamily = { { 1, 2, 3 }, { 0, 4, 5, 6, 7 } };
		int[][] resultFamily = binarySetFamilyMaximalIntersection(tempFirstSetFamily,
				tempSecondSetFamily);
		System.out.println("The result set family is: " + Arrays.deepToString(resultFamily));
	}// Of testBinarySetFamilyMaximalIntersection

	/**
	 ************************* 
	 * Test the method.
	 ************************* 
	 */
	public static void testIntersectionSize() {
		int[] tempFirstSet = { 1, 3, 5, 7, 9 };
		int[] tempSecondSet = { 3, 4, 5, 9 };
		int resultSize = intersectionSize(tempFirstSet, tempSecondSet);
		System.out.println("The size is: " + resultSize);
	}// Of testIntersectionSize

	/**
	 ************************* 
	 * Test the method.
	 ************************* 
	 */
	public static void testIntersection() {
		int[] tempFirstSet = { 1, 3, 5, 7, 9 };
		int[] tempSecondSet = { 4 };
		int[] resultArray = intersection(tempFirstSet, tempSecondSet);
		System.out.println("The size is: " + Arrays.toString(resultArray));
	}// Of testIntersection

	/**
	 ************************** 
	 * Testing method.
	 * 
	 * @param args
	 *            The parameters.
	 ************************** 
	 */
	public static void main(String args[]) {
		// int[] firstSet = {1, 2, 6};
		// int[] firstSet = {2};
		// int[] firstSet = {};
		// int[] firstSet = {1};
		/*
		 * int[] firstSet = { 2, 3, 6 }; int[] secondSet = { 2, 3, 6 }; int[]
		 * thirdSet = {}; try { thirdSet = setIntersection(firstSet, secondSet);
		 * } catch (Exception ee) { System.out.println(ee); return; }
		 * printIntArray(thirdSet);
		 */
		// testIntersectionSize();
		// testIntersection();
		// testBinarySetFamilyMaximalIntersection();
		// testInBlock();
		disorderData("src/data/spiral.arff");
	}// Of main
}// Of class SimpleTool
