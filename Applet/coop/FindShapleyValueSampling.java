/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author arjunsuresh
 */
public class FindShapleyValueSampling {

	HashMap<String, Double> util = new HashMap<String, Double>();
    Integer noOfPlayers = new Integer(0);
    float[] shapelyValue = new float[50];
    String[] subSet = new String[2000];
    int subSeti = 0, m = 100, samflag = 0;
    private String file = null;
    Random generator = new Random();

    /**
     * This function finds the approximate Shapely Value based on Sampling
     * @return The string containing the Approximate Shapely Value of the input TU game
     * @throws IOException
     */
    String findShapleyValue() throws IOException {
        ReadInput rd = new ReadInput();
        rd.setInputFile(file);

        for (int i = 0; i < noOfPlayers; i++) {
            shapelyValue[i] = 0.0f;
        }

        noOfPlayers = rd.readFile(util, noOfPlayers);
       
        findSubSet("", setSet(noOfPlayers));
        if (subSeti < m)
            m = subSeti;
        else
            samflag = 1;

        util.put("", 0.0);
        int[] flag = new int[subSeti];
        for (int i = 0; i < noOfPlayers; i++) {
            flag = new int[subSeti];
            for (int l = 0; l < m; l++) {
                int j = l;
                if(samflag == 1)
                    j = generator.nextInt(subSeti);
                System.out.println("j = "+j);
                int index = subSet[j].indexOf(i + 49);
                if (index == -1 || flag[j] == 1)
                {
                   
                    if( samflag == 1)
                        l--;
                    continue;
                }
               
                flag[j] = 1;
                int k = index;
                {
                   
                    String sub = subSet[j].substring(0, k);
                    char[] subc = sub.toCharArray();
                    java.util.Arrays.sort(subc);
                    sub = new String(subc);
                    if (util.containsKey(sub)
                            && util.containsKey(insertI(sub, (char) (i + 49)))) {
                           shapelyValue[i] += (float) (util.get(insertI(sub,
                                (char) (i + 49))) - util.get(sub))
                                / fact(noOfPlayers);
                    }
                }
            }
        }
       
        StringBuffer rs = new StringBuffer();
        rs.append("The Shapely value using sampling is: \n\n");
        for (int i = 0; i < noOfPlayers; i++) {
            rs.append("Player "+(i+1)+": "+shapelyValue[i]+"\n");
            rs.append("\n");
            System.out.println(shapelyValue[i]);
        }
        return rs.toString();

    }

    /**
     * This function accepts an integer n and returns the string from 1..n
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
	 * This functions inserts a character a to a sorted string, similar to insertion sort 
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
	 * @param prefix prefix representing a subset
	 * @param s representing the set
	 */
    void findSubSet(String prefix, String s) {
        int N, i;
        N = s.length();
        String t;
        if (N == 0) {
            subSet[subSeti++] = prefix;
            
        } else {
            for (i = 0; i < N; i++) {
                String t3, t4;
                t3 = prefix + s.charAt(i);
                t4 = s.substring(0, i) + s.substring(i + 1, s.length());
                
                findSubSet(t3, t4);

            }
        }
    }

    /**
	 * This function checks whether the elements of the input string are in sorted order
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


