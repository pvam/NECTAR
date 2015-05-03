/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coop;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author arjunsuresh
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static String main(String args[]) throws IOException {
		// TODO code application logic here

		String result = null;
		if (args[1].equals("Core")) {
			FindCore fc = new FindCore();
			fc.setFile(args[0]);
			try {
				result = fc.findCore();
			} catch (Exception ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,
						ex);
			}

		}

		else if (args[1].equals("Shapley Value")) {
			FindShapleyValue fsv = new FindShapleyValue();
			fsv.setFile(args[0]);
			result = fsv.findShapleyValue();
		}

		// System.out.println("\n");

		else if (args[1].equals("Approximate Shapley Value using Sampling")) {
			FindShapleyValueSampling fsvs = new FindShapleyValueSampling();
			fsvs.setFile(args[0]);
			result = fsvs.findShapleyValue();
			// System.out.println("\n");
		}
		
		else if (args[1].equals("Nucleolus")) {
			FindNucleolus fsv = new FindNucleolus();
			fsv.setFile(args[0]);
			try {
				result = fsv.findNucleolus();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*  */

		return result;

	}

}
