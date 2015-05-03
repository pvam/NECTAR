/**
 * 
 */
package algorithms;

import games.NormalFormGame;

/**
 * @author kalyan
 * 
 */
public class CorrelatedResultObject implements ResultObject {

	private String algorithmName = null;

	private CorrelatedEquilibrium equilibrium = null;

	private NormalFormGame nfg = null;

	/**
	 * sets the algorithm name
	 * 
	 * @param algorithmName :
	 *            algorithm name
	 */
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	/**
	 * gets the algorithm name
	 * 
	 * @return algorithm name
	 */
	public String getAlgorithmName() {
		return this.algorithmName;
	}

	/**
	 * add correlated equilibrium object to result object
	 * 
	 * @param equilibrium :
	 *            correlated equilibrium
	 */
	public void addEquilibrium(CorrelatedEquilibrium equilibrium) {
		this.equilibrium = equilibrium;
	}

	/**
	 * sets the normal form game
	 * 
	 * @param nfg :
	 *            Norm form game
	 */
	public void setNormalFormGame(NormalFormGame nfg) {
		this.nfg = nfg;
	}

	/**
	 * It writes all the information about the equilibrium into a stream and
	 * returns.
	 * 
	 * @return returns the result in the form of string
	 */
	public String getResults() {

		int iPlayer, nPlayers, iVariable;

		int[] profile = null;
		int[] cardinalities = null;
		double[] eUtilities = null;

		StringBuffer stream = new StringBuffer();

		double[] probDistribution = equilibrium.getProbabilityDistribution();

		cardinalities = nfg.getAllCardinalities();
		nPlayers = nfg.getPlayers();
		profile = new int[nPlayers];
		eUtilities = new double[nPlayers];

		stream.append("Algorithm Name : " + this.algorithmName + "\n");
		stream.append("Game Name : " + nfg.getGameName() + "\n\n");

		while (profile[nPlayers - 1] < cardinalities[nPlayers - 1]) {

			for (profile[0] = 0; profile[0] < cardinalities[0]; profile[0]++) {

				iVariable = nfg.getOffset(profile);

				if (probDistribution[iVariable] != 0) {

					stream.append("\tProfile  [");
					for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
						stream.append("  " + (profile[iPlayer] + 1));
					}
					stream.append("  ]  -->  "
							+ roundValue(probDistribution[iVariable]) + "\n");

					for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
						eUtilities[iPlayer] += probDistribution[iVariable]
								* nfg.getUtility(profile, iPlayer);
					}
				}
			}

			for (iPlayer = 1; iPlayer < nPlayers; iPlayer++) {

				profile[iPlayer]++;
				if (profile[iPlayer] == cardinalities[iPlayer]
						&& iPlayer != nPlayers - 1) {
					profile[iPlayer] = 0;
					continue;
				}
				break;
			}
		}

		stream.append("\n\n Expected Utilities of Players : \n");
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			stream.append("\n\tPlayer " + (iPlayer + 1) + "  -->  "
					+ roundValue(eUtilities[iPlayer]));
		}

		return stream.toString();
	}

	/**
	 * Rounding the double value upto 3 decimal digits for easy readability
	 * 
	 * @param x :
	 *            double value
	 * @return rounded value
	 */
	private double roundValue(double x) {
		x = x * 1000;
		x = Math.round(x);
		x = ((double) x) / 1000;
		return x;
	}

}
