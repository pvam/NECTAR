/**
 * 
 */
package games;

import java.util.ArrayList;

/**
 * @author kalyan
 * 
 */
public class InformationSet {

	// information set number
	private int informationSetNumber;

	// all the nodes present in this information set
	private ArrayList treeNodeList = null;

	// no. of nodes present in this information set
	private int degree;

	/**
	 * this constructor initializes the necessary variables
	 */
	public InformationSet() {
		this.treeNodeList = new ArrayList();
		this.degree = 0;
	}

	/**
	 * this constructor also puts the information set number
	 * 
	 * @param informationSetNumber :
	 *            information set number
	 */
	public InformationSet(int informationSetNumber) {
		this.informationSetNumber = informationSetNumber;
		this.treeNodeList = new ArrayList();
	}

	/**
	 * gets the all nodes in this information set
	 * 
	 * @return nodes list
	 */
	public ArrayList getTreeNodeList() {
		return this.treeNodeList;
	}

	/**
	 * gets the size of information set
	 * 
	 * @return no. of nodes
	 */
	public int getDegree() {
		return this.degree;
	}

	/**
	 * gets the particular tree node in this set
	 * 
	 * @param index :
	 *            i'th tree node
	 * @return i'th tree node
	 */
	public TreeNode getTreeNode(int index) {
		if (index < treeNodeList.size()) {
			return (TreeNode) this.treeNodeList.get(index);
		} else
			return null;
	}

	/**
	 * sets the tree nodes list to this information set
	 * 
	 * @param treeNodeList :
	 *            tree nodes list
	 */
	public void setTreeNodeList(ArrayList treeNodeList) {
		this.treeNodeList = treeNodeList;
	}

	/**
	 * sets information set number to this set
	 * 
	 * @param informationSetNumber :
	 *            information set number
	 */
	public void setIndex(int informationSetNumber) {
		this.informationSetNumber = informationSetNumber;
	}

	/**
	 * gets the information set number of this set
	 * 
	 * @return information set number
	 */
	public int getInformationSetNumber() {
		return this.informationSetNumber;
	}

	/**
	 * add a tree node this set
	 * 
	 * @param treeNode :
	 *            tree node
	 */
	public void addTreeNode(TreeNode treeNode) {
		int i = this.treeNodeList.size();
		if (i == 0) {
			this.degree = treeNode.getDegree();
		}
		this.treeNodeList.add(i, treeNode);
	}
}
