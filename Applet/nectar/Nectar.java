/**
 * 
 */
package nectar;

import java.io.*;

import algorithms.*;
import games.*;

/**
 * @author kalyan
 * 
 */
//Nectar class
public class Nectar {

	private NormalFormGame nfg = null;

	private SequenceFormGame sfg = null;

	private ExtensiveFormGame efg = null;

	private File file = null;

	private String algorithmName = null;

	public Nectar() {
		super();
	}

	/**
	 * sets the algorithm name
	 * 
	 * @param algorithmName :
	 *            algorithm name
	 */
	public void setAlgorithm(String algorithmName) {
		this.algorithmName = algorithmName;
		return;
	}

	/**
	 * sets the file object
	 * 
	 * @param file :
	 *            file object
	 */
	public void setFile(File file) {
		this.file = file;
		return;
	}

	/**
	 * resets the file information
	 */
	public void resetFile() {
		this.clear();
		return;
	}

	/**
	 * it calls the the algorithm object for computing the nash equilibrium
	 * point
	 * 
	 * @param additionalInformation :
	 *            additional information
	 * @return results in string format
	 * @throws Exception
	 */
	public String compute(String additionalInformation) throws Exception {
		Algorithm algorithm = AlgorithmFactory.getAlgorithm(algorithmName,
				additionalInformation);

		if (nfg == null) {
			Input input = InputFactory.getInput("normal", file
					.getAbsolutePath());
			nfg = (NormalFormGame) input.readGame();
		}
		ResultObject resultObject = algorithm.computeEquilibria(nfg);

		String a= resultObject.getResults();
		return a ;
	}

	/**
	 * it calls the sequence form algorithm object for computing equilibrium
	 * point
	 * 
	 * @return results in string format
	 */
	public String computeSequenceFormAlgorithm() throws Exception {
		String output = null;

		if (sfg == null) {
			if (efg == null) {
				this.showExtensiveGame();
			}
			sfg = new SequenceFormGame(efg);
			sfg.updateMatrices();
		}

		SequenceFormAlgorithm algorithm = new SequenceFormAlgorithm(sfg);
		SequenceFormResultObject result = algorithm.computeEquilibrium();

		output = result.getResult();

		return output;
	}

	/**
	 * It calls the required object for dispalying the normal form game
	 * 
	 * @return normal form game in string format
	 * @throws Exception
	 */
	public String showGame() throws Exception {
		String output = null;

		if (nfg == null) {
			Input input = InputFactory.getInput("normal", file
					.getAbsolutePath());
			nfg = (NormalFormGame) input.readGame();
		}

		output = nfg.displayGame();

		return output;
	}

	/**
	 * It calls the required object for displaying the extensive form game
	 * 
	 * @return extensive form game in string format
	 * @throws Exception
	 */
	public String showExtensiveGame() throws Exception {
		String output = null;

		if (efg == null) {
			String fileName = file.getAbsolutePath();
			Input input = InputFactory.getInput("extensive", fileName);
			efg = (ExtensiveFormGame) input.readGame();
		}

		output = efg.displayGame();
		return output;
	}

	/**
	 * It calls the corresponding object for converting to normal form game
	 * 
	 * @return normal form game in string format for display
	 * @throws Exception
	 */
	public String convertToNormalForm() throws Exception {
		String output = null;

		if (nfg == null) {
			if (efg == null) {
				String fileName = file.getAbsolutePath();
				Input input = InputFactory.getInput("extensive", fileName);
				efg = (ExtensiveFormGame) input.readGame();
			}
			nfg = efg.getNormalFormGame();
		}
		output = nfg.displayGame();
		return output;
	}

	/**
	 * it call the corresponding object for converting extensive form to
	 * sequence form game
	 * 
	 * @return sequence form game in string format for display
	 * @throws Exception
	 */
	public String covnertToSequenceForm() throws Exception {
		String output = null;

		if (sfg == null) {
			if (efg == null) {
				this.showExtensiveGame();
			}
			sfg = new SequenceFormGame(efg);
			sfg.updateMatrices();
		}
		output = sfg.printSequenceFormGame();

		return output;
	}

	/**
	 * clears the object
	 */
	public void clear() {
		this.nfg = null;
		this.efg = null;
		this.sfg = null;
		this.file = null;
		this.algorithmName = null;
		return;
	}
}
