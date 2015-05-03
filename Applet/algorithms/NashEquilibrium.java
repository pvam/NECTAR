/**
 * 
 */
package algorithms;

/**
 * @author kalyan
 * 
 */
public class NashEquilibrium {

	private Object[] probDistributionList = null;

	private double[] expectedUtilities = null;

	public NashEquilibrium(int nPlayers, int[] cardinalities) {

		this.probDistributionList = new Object[nPlayers];
		this.expectedUtilities = new double[nPlayers];
	}

	/**
	 * sets the expected utility of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @param expectedUtility :
	 *            expected utility
	 */
	public void setExpectedUtility(int iPlayer, double expectedUtility) {
		this.expectedUtilities[iPlayer] = expectedUtility;
	}

	/**
	 * gets the expected utility of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @return expected utility of i'th player
	 */
	public double getExpectedUtility(int iPlayer) {
		return this.expectedUtilities[iPlayer];
	}

	/**
	 * sets the probability distribution over i'th player strategies
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @param probDistribution :
	 *            probability distribution over the strategies of i'th player
	 */
	public void setProbabilityDistribution(int iPlayer,
			double[] probDistribution) {
		this.probDistributionList[iPlayer] = probDistribution;
	}

	/**
	 * gets the probability distribution of i'th player strategies
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @return probability distribution over the strategies of i'th player
	 */
	public double[] getProbabilityDistribution(int iPlayer) {
		return (double[]) this.probDistributionList[iPlayer];
	}

	/**
	 * gets the no.of players
	 * 
	 * @return no.of players
	 */
	public int getPlayers() {
		return this.expectedUtilities.length;
	}

}
