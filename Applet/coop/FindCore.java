/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coop;

import java.io.IOException;
import java.util.HashMap;
import lp_solver.*;

/**
 * 
 * @author arjunsuresh
 */
public class FindCore {

	HashMap<String, Double> util = new HashMap<String, Double>();
	Integer noOfPlayers = new Integer(0);
	float[] shapelyValue = new float[50];
	String[] subSet = new String[2000];
	int subSeti = 0;
	LPVariable[] var = null;
	LPExpression expr = null;
	LPSolver lpSolver = null;
	private String file = null;

	/**
	 * This function computes the elements of the core
	 * 
	 * @return The elements of the Core
	 * @throws IOException
	 * @throws Exception
	 */
	String findCore() throws IOException, Exception {
		ReadInput rd = new ReadInput();
		rd.setInputFile(file);
		lpSolver = LPAdapter.getSolver();
		String result = null;

		noOfPlayers = rd.readFile(util, noOfPlayers);
		var = new LPVariable[noOfPlayers];
		for (int i = 0; i < noOfPlayers; i++) {
			if (i == 0)
				var[i] = lpSolver.variable(0, 5);
			else
				var[i] = lpSolver.variable(0.0, Double.MAX_VALUE);
		}

		expr = lpSolver.linearExpression();
		for (int i = 0; i < noOfPlayers; i++) {
			expr.addTerm(1, var[i]);
		}

		StringBuffer totalN = new StringBuffer();
		for (int i = 1; i <= noOfPlayers; i++) {
			totalN.append((char) (i + 48));
		}
		// System.out.println(totalN.toString());
		if (util.containsKey(totalN.toString())) {
			lpSolver.addEquation(util.get(totalN.toString()), expr);
		} else {
			// lpSolver.addEquation(0, expr);

		}

		findSubSet("", setSet(noOfPlayers));

		for (int i = 0; i < subSeti; i++) {
			if (util.containsKey(subSet[i]) == false
					|| subSet[i].equals(totalN.toString()))
				continue;
			expr = lpSolver.linearExpression();
			for (int j = 0; j < subSet[i].length(); j++) {
				expr.addTerm(1, var[(int) subSet[i].charAt(j) - 49]);

			}

			if (util.containsKey(subSet[i])) {
				lpSolver.addLesserEquation(util.get(subSet[i]), expr);

			} else {

			}

		}
		expr = lpSolver.linearExpression();
		expr.addTerm(1, var[0]);
		lpSolver.addMinimize(expr);
		StringBuffer rs = new StringBuffer();
		rs.append("The core consists of the following elements:\n");
		for (int i = 0; i < noOfPlayers; i++) {
			lpSolver.removeObjective();
			expr = lpSolver.linearExpression();
			expr.addTerm(1, var[i]);
			lpSolver.addMinimize(expr);
			if (lpSolver.solve()) {
				double objValue = lpSolver.getObjectiveValue();

				double[] incx = lpSolver.getValues(var);
				rs.append("\nElement " + (2 * i + 1) + "\n");
				for (int j = 0; j < incx.length; j++) {
					System.out.println("Player " + (j + 1) + ": Value = "
							+ incx[j]);
					rs.append("Player " + (j + 1) + ": Value = " + incx[j]
							+ "\n");

				}
				lpSolver.removeObjective();
				lpSolver.addMaximize(expr);
				if (lpSolver.solve()) {
					objValue = lpSolver.getObjectiveValue();

					incx = lpSolver.getValues(var);
					rs.append("\nElement " + (2 * i + 2) + "\n");
					for (int j = 0; j < incx.length; j++) {
						System.out.println("Player " + (j + 1) + ": Value = "
								+ incx[j]);
						rs.append("Player " + (j + 1) + ": Value = " + incx[j]
								+ "\n");

					}

				}
			}
			result = rs.toString();
		}
		System.out.println(result.length());
		if (result.length() == 45)// 45 is the length of the first statement of
									// output
			result = "The Core is empty";
		return result;

	}

	/**
	 * This function accepts an integer n and returns the string from 1..n
	 * 
	 * @param n
	 * @return string from 1..n
	 */
	String setSet(int n) {
		char[] set = new char[n];
		int i;
		for (i = 0; i < n; i++) {
			set[i] = (char) (i + 49);

		}

		return String.valueOf(set);
	}

	/**
	 * 
	 * @param n
	 * @param r
	 * @return nCr
	 */
	int nCr(int n, int r) {
		if (r >= n || n <= 0) {
			return 0;
		}
		int result = 1;
		for (int i = r + 1; i <= n; i++) {
			result *= i;
		}
		for (int i = 1; i <= n - r; i++) {
			result /= i;
		}
		return result;
	}

	/**
	 * 
	 * @param n
	 * @return n!
	 */
	int fact(int n) {
		if (n < 0) {
			return 0;
		}
		int result = 1;
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		return result;

	}

	/*
	 * This functions inserts a character a to a sorted string, similar to
	 * insertion sort
	 */
	String insertI(String set, char a) {
		int l = set.length();
		int i;
		char[] setS = new char[set.length() + 1];
		for (i = 0; i < set.length(); i++) {
			setS[i] = set.charAt(i);
		}

		for (i = 0; i < set.length(); i++) {
			if (setS[i] > a) {
				for (int j = set.length() - 1; j >= i; j--) {
					setS[j + 1] = setS[j];

				}
				break;
			}
		}
		setS[i] = a;
		return String.valueOf(setS);

	}

	/**
	 * This function finds all the subsets of a set
	 * 
	 * @param prefix
	 *            representing a subset
	 * @param s
	 *            representing the set
	 */
	void findSubSet(String prefix, String s) {
		int N, i;
		N = s.length();
		String t;
		if (N == 0) {

		} else {
			for (i = 0; i < N; i++) {
				String t3, t4;
				t3 = prefix + s.charAt(i);
				t4 = s.substring(0, i) + s.substring(i + 1, s.length());
				if (isCorrect(t3)) {

					subSet[subSeti++] = t3;
				}
				findSubSet(t3, t4);

			}
		}
	}

	/**
	 * This function checks whether the elements of the input string are in
	 * sorted order
	 * 
	 * @param s
	 * @return true if string is sorted and else false
	 */
	boolean isCorrect(String s) {
		for (int i = 0; i < s.length() - 1; i++) {
			for (int j = i + 1; j < s.length(); j++) {
				if (s.charAt(i) > s.charAt(j)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets the input filename
	 * 
	 * @param file
	 */
	void setFile(String file) {
		this.file = file;
	}

	/**
	 * 
	 * @return the input filename
	 */
	String getFile() {
		return file;
	}
}
