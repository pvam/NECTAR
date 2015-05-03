/**
 * 
 */
package algorithms;

/**
 * @author kalyan
 * 
 */
public class AlgorithmFactory {

	/**
	 * It instanties the needed concrete algorithm with the help of parameters
	 * 
	 * @param algorithmName :
	 *            Algorithm Name
	 * @param additionalInformation :
	 *            Additional Information to the Algorithm
	 * @return speicif concrete object with Algorithm interface
	 * @throws Exception
	 */
	public static Algorithm getAlgorithm(String algorithmName,
			String additionalInformation) throws Exception {
		if (algorithmName.equals("Correlated Equilibrium")) {
			return new CorrelatedEquilibriumAlgorithm();
		} else if (algorithmName.equals("Lemke-Howson")) {
			// return new LemkeHowsonAlgorithm();
			return new LemkeHowsonAlgorithm();
		} else if (algorithmName.equals("Mangasarian Algorithm")) {
			return new MangasarianAlgorithm();
		} else if (algorithmName.equals("Mixed Integer Programming")) {
			return new MIPAlgorithm(additionalInformation);
		} else if (algorithmName.equals("Pure Nash Equilibrium")) {
			return new PureNashEquilibria();
		} else if (algorithmName.equals("Simple Search Method")) {
			return new SimpleSearchAlgorithm(additionalInformation);
		} else if (algorithmName.equals("Two Person Zero-sum Game")) {
			return new TwoPersonConstantSumAlgorithm();
		} else {
			throw new Exception("The Specified algorithm is not exist");
		}
		
	}
}
