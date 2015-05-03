/**
 * 
 */
package algorithms;

/**
 * @author kalyan
 *
 */
public class CorrelatedEquilibrium {

	private double[] probabilityDistribution = null;
	
	/**
	 * sets the probability distribution
	 * @param probDistribution : probability distribution over the strategy space
	 */
	public void setProbabilityDistribution(double[] probDistribution) {
		this.probabilityDistribution = probDistribution;
	}
	
	/**
	 * gets the probability distribution
	 * @return probability distribution over the strategy space
	 */
	public double[] getProbabilityDistribution() {
		return this.probabilityDistribution;
	}
}
