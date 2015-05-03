/**
 * 
 */
package games;

import java.util.ArrayList;

/**
 * @author kalyan
 * 
 */
public class TreeNode {
	// label of the node
	private String label = null;

	// no. of child nodes
	private int degree;

	// owner of the node
	private int player;

	// list of child nodes
	private ArrayList childrenList = null;

	// Names of moves
	private ArrayList edgeNames = null;

	// parent node
	private TreeNode parent = null;

	private String playerName = null;

	private String parentEdge = null;

	/*
	 * nodeType -- "Decision" or "Chance" or "Terminal"
	 */
	private int nodeType;

	// contains utility vector if this node is "Terminal"
	private UtilityVector utilityVector = null;

	// contains probability vector if this node is "Chance"
	private ProbabilityVector probVector = null;

	// sequence number
	private int sequenceNumber;

	// Every node corresponding to one information set excepts "Terminal"
	private InformationSet infoSet = null;

	/**
	 * Default Constructor for initialization
	 */
	public TreeNode() {
		this.label = new String("");
		this.childrenList = new ArrayList();
		this.edgeNames = new ArrayList();
		this.degree = 0;
		this.sequenceNumber = 0;
	}

	/**
	 * Constructor which initializes necessary variables
	 */
	public TreeNode(String label, String nodeType) throws Exception {
		this.label = new String(label);
		this.childrenList = new ArrayList();
		this.edgeNames = new ArrayList();
		this.degree = 0;
		this.setNodeType(nodeType);
	}

	/**
	 * gets the parent tree node of current node
	 * 
	 * @return parent node
	 */
	public TreeNode getParentNode() {
		return this.parent;
	}

	/**
	 * gets the player of current node
	 * 
	 * @return player no.
	 */
	public int getPlayer() {
		return this.player;
	}

	/**
	 * sets the player to the current node
	 * 
	 * @param player :
	 *            player No.
	 */
	public void setPlayer(int player) {
		this.player = player;
	}

	/**
	 * sets the probability vector object to current node (chance node)
	 * 
	 * @param probVector :
	 *            probability vector over the moves
	 */
	public void setProbabilityVector(ProbabilityVector probVector) {
		this.probVector = probVector;
	}

	/**
	 * gets the probability vector object of current node (chance node)
	 * 
	 * @return probability vector over the moves
	 */
	public ProbabilityVector getProbabilityVector() {
		return this.probVector;
	}

	/**
	 * gets the utility vector of this node (terminal node)
	 * 
	 * @return utility vector
	 */
	public UtilityVector getUtilityVector() {
		return this.utilityVector;
	}

	/**
	 * sets the utility vector to this node (terminal node)
	 * 
	 * @param utilityVector :
	 *            utility vector
	 */
	public void setUtilityVector(UtilityVector utilityVector) {
		this.utilityVector = utilityVector;
	}

	/**
	 * gets the sequence number to this node
	 * 
	 * @return sequence numebr
	 */
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	/**
	 * sets the sequence number to this node
	 * 
	 * @param sequenceNumber :
	 *            sequence number
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * gets the label of this node
	 * 
	 * @return label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * sets the label to this node
	 * 
	 * @param label :
	 *            label
	 */
	public void setLabel(String label) {
		this.label = new String(label);
	}

	/**
	 * gets the no.of children to this node
	 * 
	 * @return no.of children
	 */
	public int getDegree() {
		return this.degree;
	}

	/**
	 * sets the player name to this node
	 * 
	 * @param playerName :
	 *            player name
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * gets the player name of this node
	 * 
	 * @return player name
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * gets the corresponding information set of this node
	 * 
	 * @return information set
	 */
	public InformationSet getInformationSet() {
		return this.infoSet;
	}

	/**
	 * sets the corresponding information set to this node
	 * 
	 * @param infoSet :
	 *            information set
	 */
	public void setInformationSet(InformationSet infoSet) {
		this.infoSet = infoSet;
	}

	/**
	 * sets type of node i.e. chance, decision, terminal
	 * 
	 * @param node :
	 *            node type
	 * @throws Exception
	 *             invalid node type
	 */
	public void setNodeType(String node) throws Exception {
		if (node.equals("Chance")) {
			this.nodeType = 0;
		} else if (node.equals("Decision")) {
			this.nodeType = 1;
		} else if (node.equals("Terminal")) {
			this.nodeType = 2;
		} else {
			throw new Exception("Unknown Node Type");
		}

		return;
	}

	/**
	 * gets the node type of this noee
	 * 
	 * @return node type
	 */
	public String getNodeType() {
		if (this.nodeType == 0) {
			return "Chance";
		} else if (this.nodeType == 1) {
			return "Decision";
		} else {
			return "Terminal";
		}
	}

	/**
	 * checks this node, if it is terninal node returns true else false
	 * 
	 * @return
	 */
	public boolean isTerminalNode() {
		if (this.nodeType == 2)
			return true;
		else
			return false;
	}

	/**
	 * checks this node, if it is decision node returns true else false
	 * 
	 * @return
	 */
	public boolean isDecisionNode() {
		if (this.nodeType == 1)
			return true;
		else
			return false;
	}

	/**
	 * checks this node, if it is chance node return true else false
	 * 
	 * @return
	 */
	public boolean isChanceNode() {
		if (this.nodeType == 0)
			return true;
		else
			return false;
	}

	/**
	 * returns the Move from current node to treeNode
	 * 
	 * @param treeNode
	 *            child node
	 * 
	 * @return MoveNumber from current node to given treeNode
	 */
	public int getMove(TreeNode treeNode) throws Exception {
		int i;
		TreeNode child = null;

		for (i = 0; i < this.degree; i++) {
			child = this.getChildAt(i);
			if (child.equals(treeNode)) {
				return (i + 1);
			}
		}
		throw new Exception(
				"Error: The given node is not a children to this node");

	}

	/**
	 * returns the move label to this node
	 * 
	 * @return label of the move
	 */
	public String getParentEdge() {
		return this.parentEdge;
	}

	/**
	 * gets the i'th child node
	 * 
	 * @param index :
	 *            i'th child
	 * @return child node
	 */
	public TreeNode getChildAt(int index) {
		return (TreeNode) this.childrenList.get(index);
	}

	/**
	 * gets child node of specific move
	 * 
	 * @param edgeName :
	 *            move label
	 * @return child node
	 */
	public TreeNode getChild(String edgeName) {
		int i;
		String edge;

		for (i = 0; i < this.childrenList.size(); i++) {
			edge = (String) this.edgeNames.get(i);
			if (edgeName.equals(edge))
				return (TreeNode) this.childrenList.get(i);
		}
		return null;
	}

	/**
	 * adds the child to this node
	 * 
	 * @param child :
	 *            child node
	 * @param edgeName :
	 *            move label
	 * @param player :
	 *            i'th player
	 */
	public void addChild(TreeNode child, String edgeName, int player) {

		this.edgeNames.add(edgeName);
		this.childrenList.add(child);
		child.parent = this;
		child.parentEdge = edgeName;
		this.degree = this.degree + 1;
		// System.out.println("add child " + player);
		// this.player = player;
	}

	/**
	 * removes the i'th child node
	 * 
	 * @param index :
	 *            i'th node
	 * @return status 0 for success -1 for failure
	 */
	public int removeChildAt(int index) {
		if (index < this.childrenList.size()) {
			this.childrenList.remove(index);
			this.degree = this.degree - 1;
			return 0;
		} else
			return -1;
	}

	public ArrayList getMovesList() {
		return this.edgeNames;
	}

}
