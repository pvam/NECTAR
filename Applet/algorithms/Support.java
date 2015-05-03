/**
 * 
 */
package algorithms;

/**
 * @author kalyan
 * 
 */
public class Support {

	private int size;

	private int[] strategies = null;

	public Support(int size) {
		makeSupport(size);
	}

	public Support(int size, int[] strategies) {
		makeSupport(size, strategies);
	}

	/**
	 * initializes the support variables
	 * 
	 * @param size :
	 *            support size
	 * @param strategies :
	 *            list of strategies in the support
	 */
	private void makeSupport(int size, int[] strategies) {
		this.size = size;
		this.strategies = new int[size];

		for (int i = 0; i < size; i++) {
			this.strategies[i] = strategies[i];
		}
		return;
	}

	/**
	 * initializes the support variables
	 * 
	 * @param size :
	 *            support size
	 */
	private void makeSupport(int size) {
		this.size = size;
		this.strategies = new int[size];

		for (int i = 0; i < size; i++) {
			this.strategies[i] = i;
		}

		return;
	}

	/**
	 * checks the given strategy is in support or not
	 * 
	 * @param iStrategy :
	 *            i'th strategy
	 * @return true if i'th strategy in the support else false
	 */
	public boolean isInSupport(int iStrategy) {
		int i;
		for (i = 0; i < this.size; i++) {
			if (iStrategy == this.strategies[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * adds the i'th strategy into specified location
	 * 
	 * @param location :
	 *            location
	 * @param iStrategy :
	 *            i'th strategy
	 */
	public void setStrategy(int location, int iStrategy) {
		this.strategies[location] = iStrategy;
	}

	/**
	 * gets the strategy in the support of given location
	 * 
	 * @param location :
	 *            location
	 * @return : i'th strategy
	 */
	public int getStrategy(int location) {
		return this.strategies[location];
	}

	/**
	 * increase the size of support by 1
	 */
	public void increaseSize() {
		this.size++;
	}

	/**
	 * gets the size of the support
	 * 
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * sets the size of te support
	 * 
	 * @param size :
	 *            size of the support
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * display the support information on the standard output
	 */
	public void display() {
/*
		int i;
		System.out.print("Support Size : " + this.size + "  ");
		for (i = 0; i < this.size; i++) {
			System.out.print("  " + this.strategies[i]);
		}
		System.out.println();
*/
	}

}
