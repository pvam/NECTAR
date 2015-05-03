package hello;

import java.io.File;
import java.util.Scanner;

import nectar.Nectar;

public class Helpers {
	/**
	 * This method is called when the user clicks the compute button. It
	 * computes the corresponding algorithm by calling the nectar object and
	 * display the results into output area. It also finds the time taken to
	 * comute and update the time label.
	 */

	/* Start members */
	private File file = null;

	private String algorithmName = null;

	public Nectar nectar;

	public boolean isExtensive;

	// user selects the additional options for simple search and mixed integer
	// algorithms.Either "one" or "all" or formulation.
	public String additionalInformation;

	/* End of members */

	public Helpers(String algorithmName, File file, boolean isExtensive,
			String additionalInformation) {
		this.algorithmName = algorithmName;
		this.file = file;
		nectar = new Nectar();
		nectar.setAlgorithm(algorithmName);
		nectar.setFile(file);

		this.isExtensive = isExtensive;
		this.additionalInformation = additionalInformation;
		System.out.println("--start debugging info--");
		System.out.println(algorithmName);
		System.out.println(file.getAbsolutePath());
		System.out.println(isExtensive);
		System.out.println(additionalInformation);
		System.out.println("--end debugging info--");

	}

	public String computeAlgorithm() {
		// time values in milliseconds
		long startTime, endTime;
		String result = null;

		try {

			if (algorithmName.equalsIgnoreCase("Core")
					|| algorithmName.equalsIgnoreCase("Shapley Value")
					|| algorithmName
					.equalsIgnoreCase("Approximate Shapley Value using Sampling")
					|| algorithmName.equalsIgnoreCase("Nucleolus")) {
				startTime = System.currentTimeMillis();
				String[] args = new String[2];
				args[0] = file.toString();
				args[1] = algorithmName;

				result = coop.Main.main(args);

				endTime = System.currentTimeMillis();
			} else if (algorithmName.equalsIgnoreCase("EPE")
					|| algorithmName.equalsIgnoreCase("Dictatorial")
					|| algorithmName.equalsIgnoreCase("DSIC")
					|| algorithmName.equalsIgnoreCase("BIC")
					|| algorithmName.equalsIgnoreCase("Ex-Post IR")
					|| algorithmName.equalsIgnoreCase("IIR")
					|| algorithmName.equalsIgnoreCase("Ex-Ante IR")) {
				startTime = System.currentTimeMillis();
				String[] args = new String[2];
				args[0] = file.toString();
				args[1] = algorithmName;

				result = mech_design.Main.main(args);

				endTime = System.currentTimeMillis();
			}

			else if (algorithmName.equalsIgnoreCase("Bayesian")) {
				startTime = System.currentTimeMillis();
				String[] args = new String[2];
				args[0] = file.toString();
				args[1] = algorithmName;

				result = bayesian.Main.main(args);

				endTime = System.currentTimeMillis();

			}

			/*
			 * if the algorithm is sequence form type then call the
			 * corresponding algorithm.
			 */
			else if (algorithmName.equalsIgnoreCase("Sequence Form Algorithm")) {
				// get the current time in milli seconds format
				startTime = System.currentTimeMillis();
				result = nectar.computeSequenceFormAlgorithm();
				endTime = System.currentTimeMillis();
			} else {
				/*
				 * if the game is extensive form type and does not converted
				 * into normal form them convert into normal form.
				 */
				if (isExtensive) {
					nectar.convertToNormalForm();
				}

				startTime = System.currentTimeMillis();
				result = nectar.compute(additionalInformation);
				endTime = System.currentTimeMillis();
			}

			return result;

			// System.out.println(result);
			// System.out.println("Time elapsed : " + (endTime - startTime) +
			// "(m Secs)");
		} catch (Exception e) {
			System.out.println("Error : " + e.toString());
			e.printStackTrace();
			return e.toString();
		}
	}

	/**
	 * This method is called when the user selects the show normal form game
	 * button is selcted. It display the normal form game into the output area.
	 */
	public String showGame() {
		String result = "";
		try {
			/*
			 * if the game is extensive form type then converts extensive form
			 * to normal form game.
			 */

			if (isExtensive) {
				result = nectar.convertToNormalForm();
			}

			if (additionalInformation.contains("shapley")) {
				Scanner sc = new Scanner(file);
				while (sc.hasNext()) {
					result += sc.next() + "\n";
				}
			} else {
				result = nectar.showGame();
			}
			return result;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is called when the user selects the show extensive form game
	 * button. It displayes the extensive form in the output area
	 *
	 * @return
	 */
	public String showExtGame() {
		try {
			String output = nectar.showExtensiveGame();
			return output;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is called when the user selects the show sequence form game
	 * button. It converts the extensive form game to sequence form
	 * representation and displays in the output area
	 */
	public String showSeqGame() {
		try {
			String output = nectar.covnertToSequenceForm();
			return output;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

}
