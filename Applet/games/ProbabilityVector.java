/**
 * 
 */
package games;

/**
 * @author kalyan
 * 
 */
public class ProbabilityVector {

	private double[] probabilityDistribution = null;;

	private TreeNode[] treeNode = null;

	/**
	 * this constructor puts probabilities over the moves and the list of
	 * children nodes
	 * 
	 * @param probabilityDistribition
	 * @param treeNode
	 */
	public ProbabilityVector(double[] probabilityDistribution,
			TreeNode[] treeNode) {
		this.probabilityDistribution = probabilityDistribution;
		this.treeNode = treeNode;
	}

	/**
	 * returns probability placed on the child node
	 * 
	 * @param Node :
	 *            child node
	 * @return probability placed on child node
	 */
	public double getProbability(TreeNode Node) {
		int iNode;
		for (iNode = 0; iNode < treeNode.length; iNode++) {
			if (treeNode[iNode].equals(Node)) {
				return probabilityDistribution[iNode];
			}
		}
		// it happens if the given node is not a child to this node
		return 0.0;
	}
}
