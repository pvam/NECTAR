/**
 * 
 */
package games;

/**
 * @author kalyan
 * 
 */
public class DominatedStrategy {

	private int iPlayer;

	private int iStrtegy;

	private int dominatedStrategy;

	private boolean stronglyDominated;

	/**
	 * sets the player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 */
	public void setPlayer(int iPlayer) {
		this.iPlayer = iPlayer;
	}

	/**
	 * gets the player
	 * 
	 * @return i'th player
	 */
	public int getPlayer() {
		return this.iPlayer;
	}

	/**
	 * sets the i'th strategey
	 * 
	 * @param iStrategy :
	 *            i'th strategy
	 */
	public void setStrategy(int iStrategy) {
		this.iStrtegy = iStrategy;
	}

	/**
	 * gets the i'th strategy
	 * 
	 * @return i'th strategy
	 */
	public int getStrategy() {
		return this.iStrtegy;
	}

	/**
	 * sets the dominated strategy
	 * 
	 * @param dominatedStrategy :
	 *            dominated strategy
	 */
	public void setDominatedStrategy(int dominatedStrategy) {
		this.dominatedStrategy = dominatedStrategy;
	}

	/**
	 * gets the dominated strategy
	 * 
	 * @return dominated strategy
	 */
	public int getDominatedStrategy() {
		return this.dominatedStrategy;
	}

	/**
	 * sets the strongly dominated strategy
	 * 
	 * @param strongDominated :
	 *            strongly dominated strategy
	 */
	public void setStronglyDominated(boolean strongDominated) {
		this.stronglyDominated = strongDominated;
	}

	/**
	 * checks the given strategy is dominated
	 * 
	 * @return true if it is dominated or false
	 */
	public boolean isDominated() {
		return this.stronglyDominated;
	}
}
