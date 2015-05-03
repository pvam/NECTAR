/**
 * 
 */
package games;

import java.util.ArrayList;

/**
 * @author kalyan
 * 
 */
public class UtilityVector {

	// terminal node number
	private int number;

	// list of utilities of all players
	private ArrayList utilities = null;

	// related terminal node
	private TreeNode terminalNode = null;

	/**
	 * sets the related terminal node to this vector
	 * 
	 * @param terminalNode :
	 *            terminal node
	 */
	public void setTerminalNode(TreeNode terminalNode) {
		this.terminalNode = terminalNode;
	}

	/**
	 * set this utilities list to this vector
	 * 
	 * @param utilities :
	 *            utilities list
	 */
	public void setUtilities(ArrayList utilities) {
		this.utilities = utilities;
	}

	/**
	 * sets the terminal node number
	 * 
	 * @param number :
	 *            terminal node number
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * gets the utilities list from the vector object
	 * 
	 * @return utilities list
	 */
	public ArrayList getUtilities() {
		return this.utilities;
	}

	/**
	 * gets the related terminal node to this vector
	 * 
	 * @return terminal node
	 */
	public TreeNode getTerminalNode() {
		return this.terminalNode;
	}

	/**
	 * gets the related terminal node number
	 * 
	 * @return terminal node number
	 */
	public int getNumber() {
		return this.number;
	}

	/**
	 * gets the utitility of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @return utility of i'th player
	 */
	public double getUtility(int iPlayer) {
		return ((Double) this.utilities.get(iPlayer)).doubleValue();
	}
}
