/**
 * 
 */
package algorithms;

import java.util.*;

/**
 * @author kalyan
 * 
 */
public class NashResultObject implements ResultObject {

	private String algorithmName = null;

	private String gameName = null;

	private ArrayList equilibriaList = null;

	public NashResultObject() {
		this.equilibriaList = new ArrayList();
	}

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
	 * sets the game name
	 * 
	 * @param gameName :
	 *            game name
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * gets the game name
	 * 
	 * @return game name
	 */
	public String getGameName() {
		return this.gameName;
	}

	/**
	 * adds the equilibrium point
	 * 
	 * @param equilibrium :
	 *            Nash equilibrium point
	 */
	public void addEqulibrium(NashEquilibrium equilibrium) {
		equilibriaList.add(equilibrium);
	}

	/**
	 * adds the equilibrium point at the specified index
	 * 
	 * @param index :
	 *            position in the list
	 * @param equilibrium :
	 *            Nash equilibrium point object
	 */
	public void addEquilibrium(int index, NashEquilibrium equilibrium) {
		equilibriaList.add(index, equilibrium);
	}

	/**
	 * It translates the equilibrium details into stream form and returns the
	 * stream
	 */
	public String getResults() {

		StringBuffer stream = new StringBuffer();
		int nPlayers, iPlayer, iStrategy;
		double[] probabilityDistribution = null;
		double value;

		NashEquilibrium equilibrium = null;

		stream.append("Game Name       : " + this.getGameName() + "\n");
		stream.append("Algorithm Name : " + this.getAlgorithmName() + "\n\n");

		if (equilibriaList.size() == 0) {
			stream.append("\t\t No Pure Nash Equilibria");
		}

		for (int i = 0; i < equilibriaList.size(); i++) {

			equilibrium = (NashEquilibrium) equilibriaList.get(i);
			nPlayers = equilibrium.getPlayers();

			stream.append("Nash Equlibrium ( " + (i + 1) + " ) \n\n");
			for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {

				probabilityDistribution = equilibrium
						.getProbabilityDistribution(iPlayer);

				stream.append("\t Player " + (iPlayer + 1) + " :\n");
				for (iStrategy = 0; iStrategy < probabilityDistribution.length; iStrategy++) {
					value = roundValue(probabilityDistribution[iStrategy]);
					if (value > 0) {
						stream.append("\t\tStrategy " + (iStrategy + 1)
								+ "  -->  " + roundValue(value) + "\n");
					}
				}
				stream.append("\n");
			}

			for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
				stream.append("\t Player " + (iPlayer + 1)
						+ " Expected Utility: "
						+ roundValue(equilibrium.getExpectedUtility(iPlayer))
						+ "\n");
			}

			stream.append("\n\n");
		}
		return stream.toString();
	}

	/**
	 * Gives the Rounded value of a given double value
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
