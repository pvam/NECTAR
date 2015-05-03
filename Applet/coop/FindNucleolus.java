package coop;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import lp_solver.*;

/**
 *
 * @author arjunsuresh
 */
public class FindNucleolus {

    HashMap<String, Double> util = new HashMap<String, Double>();
    Integer noOfPlayers = new Integer(0);
    float[] shapelyValue = new float[50];
    HashMap<String, Double> excessG = new HashMap<String, Double>();
    String[] subSet = new String[2000];
    int subSeti = 0;
    LPVariable[] var = null;
    LPExpression expr = null;
    LPSolver lpSolver = null;
    int flag[] = null;
    private String file = null;

    /**
     * This function finds the Nucleolus of the input TU game
     *
     * @return The string containing the Nucleolus of the input TU game
     * @throws IOException
     * @throws Exception
     */
    String findNucleolus() throws IOException, Exception {
        ReadInput rd = new ReadInput();
        rd.setInputFile(file);
        lpSolver = LPAdapter.getSolver();

        noOfPlayers = rd.readFile(util, noOfPlayers);
        var = new LPVariable[noOfPlayers];
        for (int i = 0; i < noOfPlayers; i++) {
            var[i] = lpSolver.variable(0.0, Double.MAX_VALUE);
        }

        expr = lpSolver.linearExpression();
        for (int i = 0; i < noOfPlayers; i++) {
            expr.addTerm(1.0, var[i]);
        }

        StringBuffer totalN = new StringBuffer();
        for (int i = 1; i <= noOfPlayers; i++) {
            totalN.append((char) (i + 48));
        }

        if (util.containsKey(totalN.toString())) {
            // lpSolver.addGreaterEquation(util.get(totalN.toString()), expr);
            // lpSolver.addLesserEquation(util.get(totalN.toString()), expr);
            lpSolver.addEquation(util.get(totalN.toString()), expr);
        } else {

        }

        findSubSet("", setSet(noOfPlayers));

        flag = new int[subSeti];
        for (int i = 0; i < subSeti; i++) {
            if (util.containsKey(subSet[i]) == false
                    || subSet[i].equals(totalN.toString())
                    || subSet[i].length() > 1)
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

        double[] xval = null;
        if (lpSolver.solve()) {

            xval = lpSolver.getValues(var);
            System.out.println();
            for (int i = 0; i < xval.length; i++) {
                System.out.print("\t" + xval[i]);
            }
        }
        Double excessMax = Double.MAX_VALUE;
        for (int ii = 0; ii < subSeti; ii++)

        {
            if (xval == null) {
                break;
            }
            HashMap<String, Double> excess = new HashMap<String, Double>();
            for (int i = 0; i < subSeti; i++) {
                if (!util.containsKey(subSet[i]) || flag[i] == 1) {
                    continue;
                }
                Double value = util.get(subSet[i]);
                for (int j = 0; j < subSet[i].length(); j++) {
                    value -= xval[subSet[i].charAt(j) - 49];
                }
                // System.out.println("\nSet = " + subSet[i] + " " + value);
                excess.put(subSet[i], value);
                excessG.put(subSet[i], value);
            }

            Collection c = excess.keySet();

            Iterator itr = c.iterator();
            Double max = 0.0;
            String maxc = null, temp;
            if (itr.hasNext() == false)
                break;
            if (itr.hasNext()) {
                maxc = itr.next().toString();
                max = excess.get(maxc);
            }
        while (itr.hasNext()) {
                temp = itr.next().toString();

                if (excess.get(temp) > max) {
                    max = excess.get(temp);
                    maxc = temp;
                }
            }
        if(max < excessMax) {
            excessMax = max;
            for(int y = 0; y < subSeti; y++) {
                flag[y] = 0;
               
            }
            ii = -1;
            System.out.println("\nCurrent excess = "+ (max));
        }
           

            System.out.println("Maximum excess for " + maxc);
            expr = lpSolver.linearExpression();
            String total = totalN.toString();
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < total.length(); i++) {
                if (!contains(maxc, total.charAt(i))) {
                    s.append(total.charAt(i));
                    expr.addTerm(1.0, var[i]);
                } else {
                    // expr.addTerm(-1.0, var[i]);}
                }
            }

            System.out.println("Lesser cond for " + s.toString());
            // if (maxc.length() != noOfPlayers &&
            // util.containsKey(s.toString()))
            // lpSolver.addLesserEquation(util.get(s.toString()) - max, expr);
            if (maxc.length() != noOfPlayers && util.containsKey(s.toString())) {
                lpSolver.addLesserEquation(util.get(s.toString()) - max, expr);
                System.out.println("s > " + max);
            }

            for (int ik = 0; ik < subSeti; ik++) { //This loops ensures that the excess of the coalitions never exceeds max
                //if (flag[ik] == 0)
                {
                    if (maxc.length() != noOfPlayers
                            && util.containsKey(subSet[ik])) {
                        expr = lpSolver.linearExpression();
                        for (int i = 0; i < subSet[ik].length(); i++) {
                            expr.addTerm(1.0, var[subSet[ik].charAt(i) - 49]);
                        }

                        lpSolver.addLesserEquation(util.get(subSet[ik]) - max,
                                expr);
                        // System.out.println("s > " + max);
                    }
                }
            }
            expr = lpSolver.linearExpression();
            for (int i = 0; i < maxc.length(); i++) {
                expr.addTerm(1.0, var[maxc.charAt(i) - 49]);
            }
            lpSolver.removeObjective();
            lpSolver.addMaximize(expr);

            if (lpSolver.solve()) {

                xval = lpSolver.getValues(var);
                System.out.println();
                for (int i = 0; i < xval.length; i++) {
                    System.out.print("\t\t" + xval[i]);
                }
            }
            for (int i = 0; i < subSeti; i++) {
                if (subSet[i].equals(maxc)) {
                    flag[i] = 1;
                    break;
                }
            }

        }

        StringBuffer sb = new StringBuffer();

        sb.append("The Nucleolus is: \n\n");
        for (int i = 0; i < xval.length; i++) {
            // System.out.print("\t\t" + xval[i]);
            sb.append("Player " + (i + 1) + ": " + xval[i] + "\n\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     *
     * @param s
     * @param a
     * @return true if String s contains character a and else return false
     */
    boolean contains(String s, char a) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == a)
                return true;
        }
        return false;
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

    /**
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
     *            prefix representing a subset
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

