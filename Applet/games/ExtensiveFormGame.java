/**
 * 
 */
package games;

import java.io.BufferedReader;
import java.util.*;

/**
 * @author kalyan
 * 
 */
public class ExtensiveFormGame {
	// number of players
	private int nPlayers;

	// name of the game
	private String gameName = null;

	// player names + chance node
	private String[] names = null;

	// Set of information sets
	private ArrayList[] informationSetList = null;

	// root of the extensive form game
	private TreeNode root = null;

	// a node which represents current working node
	private TreeNode currentNode = null;

	// stack for building extensive game
	private Stack stack = null;

	// utility vectors list
	private ArrayList utilityList = null;

	// no.of sequences of all palyers
	private int sequence[] = null;

	// references to the related tree nodes
	private ArrayList[] sequenceNodesLists = null;

	/**
	 * Constructor which takes number of players of the given game and allocates
	 * memory to data structures
	 * 
	 * @param nPlayers :
	 *            No. of players
	 */
	public ExtensiveFormGame(int nPlayers) {
		int i;

		this.nPlayers = nPlayers;
		// information lists for all players + chance player
		this.informationSetList = new ArrayList[nPlayers + 1];
		// reference to root of the extensive form game
		this.root = new TreeNode();
		// names of all players + chance player
		this.names = new String[nPlayers + 1];
		// utility lists of all terminal nodes
		this.utilityList = new ArrayList();

		this.sequence = new int[nPlayers];

		this.sequenceNodesLists = new ArrayList[nPlayers];

		// initialiaztion nPlayers -> chanceNode's information sets
		// i = { 0, 1, ... , (nPlayers-1)} -> i'th player's information sets
		for (i = 0; i <= nPlayers; i++) {
			this.names[i] = new String("");
			this.informationSetList[i] = new ArrayList();
		}
	}

	/**
	 * gets the move label name to the given sequence number of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @param sequenceNumber :
	 *            sequence number
	 * @return move name
	 */
	public String getMoveName(int iPlayer, int sequenceNumber) {
		TreeNode child = (TreeNode) sequenceNodesLists[iPlayer]
				.get(sequenceNumber - 1);

		return child.getParentEdge();

	}

	/**
	 * returns the name of the extensive form game
	 * 
	 * @return name of the game
	 */
	public String getGameName() {
		return this.gameName;
	}

	/**
	 * sets the name of the extensive form game
	 * 
	 * @param gameName :
	 *            name of the game
	 */
	public void setGameName(String gameName) {
		this.gameName = new String(gameName);
	}

	/**
	 * @param index :
	 *            i'th Player number
	 * @return i'th player name
	 */
	public String getPlayerName(int index) {
		if (index < nPlayers) {
			return this.names[index];
		} else
			return null;
	}

	/**
	 * gets the number of players in the game
	 * 
	 * @return no. of players
	 */
	public int getNPlayers() {
		return this.nPlayers;
	}

	/**
	 * gets the all information sets of i'th player
	 * 
	 * @param player :
	 *            i'th player
	 * @return information sets list
	 */
	public ArrayList getInformationSets(int iPlayer) {
		return informationSetList[iPlayer];
	}

	/**
	 * gets the all utility vectors list
	 * 
	 * @return utility vectors list
	 */
	public ArrayList getUtilityList() {
		return this.utilityList;
	}

	/**
	 * sets the name of the i'th player
	 * 
	 * @param index :
	 *            i'th player number
	 * @param name :
	 *            i'th player name
	 * @return status report
	 */
	public int setPlayerName(int index, String name) {
		if (index < nPlayers) {
			this.names[index] = name;
			return 0;
		} else
			return -1;
	}

	/**
	 * it takes the BufferedReader stream from that it extracts the game content
	 * and builds extensive form game
	 * 
	 * @param br :
	 *            file input stream
	 * @throws Exception
	 *             exception for file read operations
	 */
	public void readGame(BufferedReader br) throws Exception {
		String subString = null; // a part of stream in a line
		String buffer = null; // a line in stream
		StringTokenizer st = null; // Tokenizer which splits the line
		// into sub-streams

		/*
		 * with the help of this stack we can build extensive form game without
		 * recursion
		 */
		stack = new Stack();
		// initializing the root of the tree
		root = new TreeNode();
		currentNode = root;

		// pushing the currentNode
		stack.push(currentNode);

		while (true) {
			// read one line of stream
			buffer = br.readLine();
			// System.out.println(buffer);

			if (buffer == null) {
				/*
				 * all lines in the stream are over extensive form has
				 * constructed
				 */
				break;
			}

			st = new StringTokenizer(buffer);
			subString = st.nextToken();

			if (subString.equals("c")) {
				// the node is chance node
				addChanceNode(st);
			} else if (subString.equals("p")) {
				// the node is decision node
				addDecisionNode(st);
			} else if (subString.equals("t")) {
				// the node is terminal node
				addTerminalNode(st);
			} else {
				throw new Exception(
						"The given input file is not in proper format");
			}
		}

		setSequenceNumbers();

		if (!isPerfectRecall()) {
			throw new Exception("Given game is not a perfect recall");
		}

		// System.out.println("Game Tree has constructed successfully");
		// this.displayGame();
	}

	/**
	 * sets the sequence numbers to all the moves in the game tree
	 * 
	 */
	private void setSequenceNumbers() {
		int j, k, l = 0;
		int iPlayer, sequenceNumber;
		InformationSet informationSet = null;
		ArrayList treeNodeList = null;
		TreeNode treeNode = null;
		TreeNode child = null;

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			sequenceNodesLists[iPlayer] = new ArrayList();

			for (j = 0; j < informationSetList[iPlayer].size(); j++) {

				informationSet = (InformationSet) informationSetList[iPlayer]
						.get(j);

				treeNodeList = informationSet.getTreeNodeList();
				boolean flag = true;
				for (k = 0; k < treeNodeList.size(); k++) {
					treeNode = (TreeNode) treeNodeList.get(k);

					for (l = treeNode.getDegree() - 1; l >= 0; l--) {
						child = treeNode.getChildAt(l);
						sequenceNumber = sequence[iPlayer] + l + 1;
						child.setSequenceNumber(sequenceNumber);
						if (flag) {
							sequenceNodesLists[iPlayer].add(child);
						}
					}
					flag = false;
				}
				sequence[iPlayer] = sequence[iPlayer] + treeNode.getDegree();
			}
		}

		return;
	}

	/**
	 * it builds the chance node type and updates respective data structures
	 * 
	 * @param st
	 *            stream tokenizer which splits the stream
	 * @throws Exception
	 *             for invalid input format
	 */
	private void addChanceNode(StringTokenizer st) throws Exception {
		int i;
		String subString = null;

		String label = null;// label of the chance Node
		double probability; // chance node contains probabilities on choices
		int informationSetNumber; // represents number of a chance node

		// contains all probabilities of choices in a list form
		ArrayList probList = new ArrayList();
		// list of choices names
		ArrayList edgeNames = new ArrayList();

		subString = getNextString(st);

		subString = st.nextToken();
		// getting information set number from the sub-stream
		informationSetNumber = Integer.parseInt(subString);
		// getting label of the node
		label = st.nextToken();

		subString = st.nextToken();
		while (!subString.equals("{")) {
			subString = st.nextToken();
		}

		// format looks like { "choice1" 0.5 "choice" 0.5 }
		// choices and their probabilities
		if (subString.equals("{")) {
			i = 0;
			subString = getNextString(st);
			while (!subString.equals("}")) {
				edgeNames.add(i, subString);
				subString = st.nextToken();

				// probability = Double.parseDouble(subString);
				probability = this.getProbability(subString);

				// checking whether the probalities are valid or not
				if (probability < 0 || probability > 1) {
					throw new Exception(
							"Probability can not be negative or more than 1");
				}
				probList.add(i++, new Double(probability));

				subString = getNextString(st);
			}
		}

		// checking whether total probability equals 1 or not
		double total = 0;
		for (i = 0; i < probList.size(); i++) {
			total = total + ((Double) probList.get(i)).doubleValue();
		}
		if (total != 1) {
			throw new Exception("Total Probability should be equal to 1");
		}

		// getting the current node from the stack
		currentNode = (TreeNode) stack.pop();
		// setting the label to node
		currentNode.setLabel(label);
		// setting the type of node
		currentNode.setNodeType("Chance");
		// setting the owner of the node
		// nPlayers --> chance player -> N
		currentNode.setPlayer(nPlayers);

		// adding children nodes to the current node
		// no. of children equals to no. of choices
		TreeNode treeNode = null;
		String edgeName = null;
		double[] probabilities = new double[probList.size()];
		TreeNode[] children = new TreeNode[probList.size()];
		i = 0;

		while (!edgeNames.isEmpty()) {
			edgeName = (String) edgeNames.remove(0);
			treeNode = new TreeNode();
			probabilities[i] = ((Double) probList.get(i)).doubleValue();
			children[i++] = treeNode;
			currentNode.addChild(treeNode, edgeName, 0);
		}

		ProbabilityVector probVector = new ProbabilityVector(probabilities,
				children);
		currentNode.setProbabilityVector(probVector);

		// for debugging
		// System.out.println("Degree : " + currentNode.getDegree());

		// pushing these newly created nodes for further construction of tree
		for (i = currentNode.getDegree() - 1; i >= 0; i--) {
			stack.push(currentNode.getChildAt(i));
		}

		// for debugging
		// System.out.println("Information Set Number : " +
		// informationSetNumber);

		// adding current node to information set and probabilities of this list
		InformationSet set = new InformationSet(informationSetNumber);
		set.setTreeNodeList(probList);
		informationSetList[nPlayers].add(informationSetNumber - 1, set);

		return;
	}// end of addChanceNode()

	/**
	 * it builds decision node type and updates respective data structures
	 * 
	 * @param st
	 *            stream tokenizer which helps for splitting or parsing
	 */
	private void addDecisionNode(StringTokenizer st) throws Exception {
		int i;
		String subString = null;

		String label;
		int iPlayer;
		int informationSetNumber;
		int index;
		String playerName;

		// for listing of choice names
		ArrayList edgeNames = new ArrayList();

		// name of the player
		playerName = getNextString(st);
		// getting the player number (i'th player)
		iPlayer = Integer.parseInt(st.nextToken()) - 1;
		// getting the information set number
		informationSetNumber = Integer.parseInt(st.nextToken());
		// label of the node
		label = getNextString(st);

		// finding child nodes
		subString = st.nextToken();
		while (!subString.equals("{")) {
			subString = st.nextToken();
		}

		if (subString.equals("{")) {
			i = 0;
			subString = getNextString(st);
			while (!subString.equals("}")) {
				// System.out.println("Edge Name : " + subString);
				edgeNames.add(i, subString);
				subString = getNextString(st);
			}
		}

		// getting the current node from the stack
		currentNode = (TreeNode) stack.pop();

		// setting the player name to the node
		currentNode.setPlayerName(playerName);

		// setting the label to the node
		currentNode.setLabel(label);

		// setting the type of the node
		currentNode.setNodeType("Decision");

		// setting owner of the node
		currentNode.setPlayer(iPlayer);

		// adding children by checking the no of choices
		index = 0;
		TreeNode treeNode = null;

		while (!edgeNames.isEmpty()) {
			label = (String) edgeNames.remove(index);
			treeNode = new TreeNode();
			currentNode.addChild(treeNode, label, iPlayer);
		}

		// pushing these children to the stack
		for (i = currentNode.getDegree() - 1; i >= 0; i--) {
			stack.push(currentNode.getChildAt(i));
		}

		// debug purpose
		// System.out.println("Player : " + player);
		// System.out.println("Information Set Number : "+
		// informationSetNumber);

		// updating the information sets of player
		ArrayList list = this.informationSetList[iPlayer];
		InformationSet informationSet = null;

		int flag = 0;
		for (i = 0; i < list.size(); i++) {
			informationSet = (InformationSet) list.get(i);
			if (informationSet.getInformationSetNumber() == informationSetNumber) {
				informationSet = (InformationSet) list.remove(i);
				flag = 1;
				break;
			}
		}
		if (flag == 1) {
			informationSet.addTreeNode(currentNode);
			currentNode.setInformationSet(informationSet);
			list.add(i, informationSet);
		} else {
			informationSet = new InformationSet(informationSetNumber);
			informationSet.addTreeNode(currentNode);
			currentNode.setInformationSet(informationSet);
			list.add(informationSetNumber - 1, informationSet);
		}

		return;
	}

	/**
	 * building terminal node and updating corresponding data strucutes
	 * 
	 * @param st
	 *            data stream
	 */
	private void addTerminalNode(StringTokenizer st) throws Exception {
		int i;
		String subString = null;

		String label;
		int terminalNumber;

		// utility vector at this terminal node
		ArrayList utilities = new ArrayList(nPlayers);
		
		subString = getNextString(st);
		// getting terminal node number
		terminalNumber = Integer.parseInt(st.nextToken());
		// label of the node
		label = getNextString(st);

		// getting utilities of all players
		// format --> { utility1, utility2, ... , utilityn }
		subString = st.nextToken();
		while (!subString.equals("{")) {
			subString = st.nextToken();
		}

		for (i = 0; i < nPlayers; i++) {
			subString = getNextUtility(st);
			try {
				utilities.add(i, new Double(subString));
			} catch (Exception e) {
				throw new Exception("The given sub stream \"" + subString
						+ "\" is not a valid utility");
			}
		}

		// getting the current node
		currentNode = (TreeNode) stack.pop();
		// setting the label to the node
		currentNode.setLabel(label);
		// setting the type of node
		currentNode.setNodeType("Terminal");

		// updating utility vector of this node
		UtilityVector utilityVector = new UtilityVector();

		utilityVector.setTerminalNode(currentNode);
		utilityVector.setUtilities(utilities);
		utilityVector.setNumber(terminalNumber);
		utilityList.add(utilityVector);

		currentNode.setUtilityVector(utilityVector);

		return;
	}

	/**
	 * writes the whole extensive form representation into stream
	 * 
	 * @return string form of stream
	 */
	public String displayGame() {
		StringBuffer stream = new StringBuffer();

		TreeNode treeNode = root;

		stream.append("Game Name : " + this.getGameName() + "\n\n");
		stream.append(":: Game Tree :: \n\n");
		printTree(treeNode, stream);
		stream.append("\n");
		printInformationSets(stream);
		printPayoffs(stream);

		return new String(stream);
	}

	/**
	 * writes the informations sets details into stream
	 * 
	 * @param stream :
	 *            output stream
	 */
	private void printInformationSets(StringBuffer stream) {
		int i, j;
		int iPlayer;
		InformationSet set = null;
		ArrayList list = null;

		stream.append(":: Information Sets :: \n\n");

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {

			list = informationSetList[iPlayer];

			stream.append("Player " + (iPlayer + 1) + " : \n");

			for (j = 0; j < list.size(); j++) {

				set = (InformationSet) list.get(j);
				stream.append("\tInformatino Set No : "
						+ set.getInformationSetNumber());
				stream.append("\tSize : " + set.getDegree());
				stream.append("\tLabel : " + set.getTreeNode(0).getLabel()
						+ "\n");
			}
		}

		stream.append("\n:: Sequences :: \n");
		for (i = 0; i < sequence.length; i++) {
			stream.append("\tPlayer " + (i + 1) + "  sequences  : "
					+ sequence[i] + "\n");
		}
		stream.append("\n");

		stream.append(":: Chance Nodes :: \n");
		list = informationSetList[nPlayers];
		ArrayList prob = null;

		stream.append("Total Chance Nodes : " + list.size() + "\n");
		for (j = 0; j < list.size(); j++) {

			set = (InformationSet) list.get(j);
			prob = set.getTreeNodeList();

			stream.append("\tChance Node " + (j + 1) + " Degree : "
					+ prob.size() + "\n");

			stream.append("\t\t[  ");
			for (i = 0; i < prob.size(); i++) {
				stream.append(roundValue(((Double) prob.get(i)).doubleValue())
						+ "  ");
			}
			stream.append("]\n");
		}
		stream.append("\n");

		return;
	}

	/**
	 * writes all payoff details into stream
	 * 
	 * @param stream :
	 *            output stream
	 */
	private void printPayoffs(StringBuffer stream) {
		int i;
		UtilityVector vector = null;

		stream.append(":: Utility Vectors :: \n\n");
		stream.append("Total Terminal Nodes: " + utilityList.size() + "\n\n");

		for (i = 0; i < utilityList.size(); i++) {

			vector = (UtilityVector) utilityList.get(i);
			ArrayList list = vector.getUtilities();

			stream.append("\tTerminal Node " + (i + 1) + " : [  ");
			for (int j = 0; j < list.size(); j++) {
				stream.append(roundValue(((Double) list.get(j)).doubleValue())
						+ "  ");
			}
			stream.append("]\n");
		}

		return;
	}

	/**
	 * writes all node details into stream
	 * 
	 * @param treeNode :
	 *            root node
	 * @param stream :
	 *            output stream
	 */
	private void printTree(TreeNode treeNode, StringBuffer stream) {
		int i;

		stream.append("\tNode Type : " + treeNode.getNodeType());
		stream.append("\tChildrens : " + treeNode.getDegree());
		stream.append("\t\tLabel : " + treeNode.getLabel() + "\n");

		for (i = 0; i < treeNode.getDegree(); i++) {
			printTree(treeNode.getChildAt(i), stream);
		}
		return;
	}

	/**
	 * This method checks whether the given game has perfect recall or not. if
	 * it has perfect recall then return true.
	 * 
	 * @throws Exception
	 *             throws exception if the game has not a perfect recall
	 */
	private boolean isPerfectRecall() throws Exception {
		int i, j, k;
		int iPlayer, informationSetNumber;
		InformationSet set = null;
		TreeNode treeNode = null;
		TreeNode parentNode = null;
		ArrayList treeNodeList = null;

		TreeNode xNode = null;
		TreeNode yNode = null;
		TreeNode xPredecessorNode = null;
		TreeNode yPredecessorNode = null;
		String xParentEdge = null;
		String yParentEdge = null;
		boolean xFlag = false;
		boolean yFlag = false;

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			for (i = 0; i < informationSetList[iPlayer].size(); i++) {
				set = (InformationSet) informationSetList[iPlayer].get(i);
				/*
				 * if the information set contains only one node then no need to
				 * check for perfect recall
				 */
				if (set.getDegree() != 1) {
					treeNodeList = set.getTreeNodeList();
					informationSetNumber = set.getInformationSetNumber();

					/*
					 * case 1: if two decision nodes are in the same information
					 * set, then neither is a predecessor of the other
					 */
					for (j = 0; j < treeNodeList.size(); j++) {
						treeNode = (TreeNode) treeNodeList.get(j);
						parentNode = treeNode.getParentNode();

						while (parentNode != null) {
							// getting the information set of same player
							if (parentNode.getPlayer() == iPlayer) {

								if (parentNode.getInformationSet()
										.getInformationSetNumber() == informationSetNumber) {
									throw new Exception(
											"The given extensive form game is not perfect recall");
								}
							}
							parentNode = parentNode.getParentNode();
						}
					}

					/*
					 * case 2: if two nodes x and y are in the same
					 * information set and one of them has a predecessor x',
					 * then the other one has a predecessor y' (possibly x'
					 * itself) in the same information set as x' and the action
					 * taken at x' that leads to x is the same as the action
					 * taken from y' that leads to the y
					 */
					for (j = 0; j < treeNodeList.size(); j++) {
						/*
						 * We have to check nC_2 combinations, where n is the
						 * size of information set
						 */
						xNode = (TreeNode) treeNodeList.get(j);
						xFlag = false;
						// xParentEdge stores the action from x' to x
						xParentEdge = xNode.getParentEdge();
						xPredecessorNode = xNode.getParentNode();

						while (xPredecessorNode != null) {
							// getting the information set of same player
							if (xPredecessorNode.getPlayer() == iPlayer) {
								xFlag = true;
								break;
							}
							xParentEdge = xPredecessorNode.getParentEdge();
							xPredecessorNode = xPredecessorNode.getParentNode();
						}
						if (!xFlag) {
							xPredecessorNode = null;
							xParentEdge = null;
						}

						for (k = j + 1; k < treeNodeList.size(); k++) {

							yNode = (TreeNode) treeNodeList.get(k);
							yFlag = false;
							// yParentEdge store the action from y' to y
							yParentEdge = yNode.getParentEdge();
							yPredecessorNode = yNode.getParentNode();

							while (yPredecessorNode != null) {
								// getting the information set of same player
								if (yPredecessorNode.getPlayer() == iPlayer) {
									yFlag = true;
									break;
								}
								yParentEdge = yPredecessorNode.getParentEdge();
								yPredecessorNode = yPredecessorNode
										.getParentNode();
							}

							if (!yFlag) {
								yPredecessorNode = null;
								yParentEdge = null;
							}

							/*
							 * If they dont have any predecessor nodes then no
							 * need to check
							 */
							if (xPredecessorNode == null
									&& yPredecessorNode == null) {
								continue;
							}

							/*
							 * Here we are checking case 2 of perfect Recall
							 */
							if (xPredecessorNode != null
									&& yPredecessorNode != null) {
								if (xPredecessorNode.getInformationSet()
										.getInformationSetNumber() == yPredecessorNode
										.getInformationSet()
										.getInformationSetNumber()) {
									if (xParentEdge.equals(yParentEdge)) {
										continue;
									}
								}
							}

							/*
							 * if it comes here then definately the given game
							 * has imperfect recall
							 */
							// System.out.println("x Parent Edge Name : " +
							// xParentEdge);
							// System.out.println("y Parent Edge Name : " +
							// yParentEdge);
							throw new Exception(
									"The given extensive form game is not perfect recall");
						}
					}
				}
			}
		}

		/*
		 * the given game has perfect recall
		 */
		return true;
	}

	/**
	 * it Builds NormalFormGame from the extensive form game. it takes each
	 * utility vector, then it traverses to root and finds the information sets
	 * from that it builds normal form game strategies and updates expected
	 * utilities of all the players
	 * 
	 * @return NormalFormGame
	 * @throws Exception
	 *             Invalid Data
	 */
	public NormalFormGame getNormalFormGame() throws Exception {
		int i, j, k, l;

		int iPlayer, jStrategy, count, iterations, updates;

		/*
		 * probability -> for calculating the total probability from terminal
		 * node to root node
		 */
		double probability, utility;

		int[] cardinalities = null;
		int[] profile = null;

		int[] sets = null; // stores the moves at each information set
		int[] totalSets = null; // stores total moves at each information set
		int[] missedSets = null; // stores the information sets which are not
		// in path
		int[] strategySet = null;

		Object[] totalMoveList = null; // moves for all players
		Object[] moveList = null; // moves for all players
		Object[] strategyList = null;

		ArrayList profileList = null;

		int informationSetNumber, iMove;

		NormalFormGame nfg = null;
		// list of utilities of all players of a terminal node
		UtilityVector utilityVector = null;
		TreeNode treeNode = null;
		TreeNode parent = null;
		// information set object
		InformationSet informationSet = null;
		// vector which stores probabilities of all moves at a node
		ProbabilityVector probVector = null;

		/*
		 * initialization
		 */
		cardinalities = new int[nPlayers];
		profile = new int[nPlayers];
		totalMoveList = new Object[nPlayers];
		moveList = new Object[nPlayers];
		strategyList = new Object[nPlayers];
		profileList = new ArrayList();

		/*
		 * computes the no of pure strategies for all players and stores the no.
		 * of moves for each information set
		 */
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			cardinalities[iPlayer] = 1;
			sets = new int[informationSetList[iPlayer].size()];

			// System.out.println("set size : " + sets.length + " Player : " +
			// iPlayer);

			for (j = 0; j < informationSetList[iPlayer].size(); j++) {
				informationSet = (InformationSet) informationSetList[iPlayer]
						.get(j);
				sets[j] = informationSet.getDegree();
				cardinalities[iPlayer] *= sets[j];
			}
			totalMoveList[iPlayer] = sets;
		}

		// getting instance of Normal form game
		nfg = GameFactory.getNormalFormGame(nPlayers, cardinalities);
		nfg.setGameName(this.gameName);

		/*
		 * Traverse all the paths from each terminal node to root node and
		 * computes the total probability to reach that terminal node. Also
		 * stores the moves(actions) present in that path. And finds the
		 * strategies which have to update the utilities; and finds the profiles
		 * which are to be updated
		 */
		for (i = 0; i < utilityList.size(); i++) {

			for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
				sets = new int[informationSetList[iPlayer].size()];
				moveList[iPlayer] = sets;
			}

			// get a utility vector
			utilityVector = (UtilityVector) utilityList.get(i);
			// get the corresponding terminal node from utility vector object
			treeNode = utilityVector.getTerminalNode();
			// get the parent node
			parent = treeNode.getParentNode();

			// testing
			// System.out.println("UtilityVector 1 : " +
			// utilityVector.getUtility(0));
			// System.out.println("UtilityVector 2 : " +
			// utilityVector.getUtility(1));

			probability = 1;
			/*
			 * traversing from terminal node to root node
			 */
			while (parent != null) {
				// if it is decision node
				if (parent.isDecisionNode()) {

					// get the information set from the parent node
					informationSet = parent.getInformationSet();
					// get the player information from the information set
					iPlayer = parent.getPlayer();

					// get the information set number
					informationSetNumber = informationSet
							.getInformationSetNumber();
					// get the move in the path
					iMove = parent.getMove(treeNode);

					// System.out.println("iPlayer : " + iPlayer);

					// store the move in move list
					((int[]) moveList[iPlayer])[informationSetNumber - 1] = iMove;

				} else if (parent.isChanceNode()) {
					// get the probability vector
					probVector = parent.getProbabilityVector();
					// get the corresponding probability of the move
					probability *= probVector.getProbability(treeNode);
				}

				treeNode = parent;
				parent = parent.getParentNode();
			}
			// System.out.println("probablity : " + probability);

			/*
			 * builds the strategies of normal form game for all players
			 */
			for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {

				// initialization
				jStrategy = 0;
				count = 0;
				iterations = 1; // contains no of strategies has to update

				// get moves list of i'th player
				totalSets = (int[]) totalMoveList[iPlayer];
				sets = (int[]) moveList[iPlayer];

				missedSets = new int[sets.length];

				/*
				 * finding the information sets which are not in the path of the
				 * i'th player
				 */
				for (j = 0; j < sets.length; j++) {
					if (sets[j] == 0) {
						missedSets[count++] = j;
						iterations *= totalSets[j];
					}
				}
				// System.out.println("Iterations : " + iterations);

				strategySet = new int[iterations];

				// System.out.println("Count : " + count);

				/*
				 * this block executes when there is miss of information sets of
				 * i'th player in the path. Then it has to update multiple
				 * strategies of normal form game
				 */
				if (iterations > 1) {

					for (j = 0; j < count; j++) {
						sets[missedSets[j]] = 1;
					}

					// it covers all missed information set
					for (j = 0, k = 0; j < iterations; j++) {

						jStrategy = getStrategy(sets, totalSets);

						strategySet[j] = jStrategy;
						// System.out.println("iPlayer ; " + iPlayer + "
						// jStrategy ; " + jStrategy);

						l = missedSets[k];
						sets[l]++;
						while (sets[l] > totalSets[l]) {

							k++;
							if (k == count) {
								break;
							}
							sets[l] = 1;
							l = missedSets[k];
							sets[l]++;
						}
						k = 0;
					}
				} else {
					// compute the j'th strategy from the moves
					jStrategy = getStrategy(sets, totalSets);
					strategySet[0] = jStrategy;
				}

				strategyList[iPlayer] = strategySet;
			}

			updates = 1;
			for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
				updates *= ((int[]) strategyList[iPlayer]).length;
			}

			// System.out.println("Updates : " + updates);

			int[] indices = new int[nPlayers];
			int index = 0;

			while (true) {

				addProfileToList(profileList, indices, strategyList);
				// System.out.println("Indices : [" + indices[0]+ " , " +
				// indices[1]+"]");

				indices[index]++;

				while (indices[index] == ((int[]) strategyList[index]).length) {
					// System.out.println("index :" + index);
					indices[index] = 0;
					index++;
					if (index == nPlayers) {
						break;
					}
					indices[index]++;
				}

				if (index == nPlayers) {
					break;
				} else {
					index = 0;
				}

			}

			/*
			 * updates the utilites of all players corresponding to this path
			 */
			while (!profileList.isEmpty()) {

				profile = (int[]) profileList.remove(0);

				// System.out.println("Profile : [" + profile[0]+ " , " +
				// profile[1]+"]");
				// System.out.println("Old Utility : [" +
				// nfg.getUtility(profile, 0)+ " , " + nfg.getUtility(profile,
				// 1)+"]");
				for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
					utility = nfg.getUtility(profile, iPlayer);
					utility += probability * utilityVector.getUtility(iPlayer);
					nfg.setUtility(profile, iPlayer, utility);
				}
				// System.out.println("New Utility : [" +
				// nfg.getUtility(profile, 0)+ " , " + nfg.getUtility(profile,
				// 1)+"]");
			}
		}
		// returns normal form game
		return nfg;
	}

	/**
	 * computes the strategy of a player from the list moves at each information
	 * set.
	 * 
	 * @param sets
	 *            contains moves of each information set
	 * @param totalSets
	 *            contains total moves of each information set
	 * 
	 * @return the strategy for list moves
	 */
	private int getStrategy(int[] sets, int[] totalSets) {
		int i, jStrategy = 0;

		int product = 1;
		for (i = sets.length - 1; i >= 0; i--) {
			if (i == sets.length - 1) {
				jStrategy = sets[i];
			} else {
				jStrategy += product * (sets[i] - 1);
			}
			product *= totalSets[i];
		}

		return (jStrategy - 1);
	}

	private void addProfileToList(ArrayList profileList, int[] indices,
			Object[] strategyList) {
		int iPlayer, nPlayers;

		nPlayers = strategyList.length;

		int[] profile = new int[nPlayers];

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			profile[iPlayer] = ((int[]) strategyList[iPlayer])[indices[iPlayer]];
		}

		profileList.add(profile);

		return;
	}

	private double getProbability(String stream) throws Exception {
		int index = stream.indexOf('/');

		if (index == -1) {
			return (new Double(stream)).doubleValue();
		} else {
			double numerator = (new Double(stream.substring(0, index)))
					.doubleValue();
			double denominator = (new Double(stream.substring(index + 1)))
					.doubleValue();

			return (numerator / denominator);
		}
	}

	private String getNextString(StringTokenizer st) throws Exception {

		String stream = null;
		String subString = null;

		stream = st.nextToken();

		if (stream.equals("}"))
			return stream;

		while (stream.charAt(stream.length() - 1) != '"'
				|| stream.length() == 1) {
			subString = st.nextToken();
			stream = stream.concat(" ");
			stream = stream.concat(subString);
		}

		return stream;
	}

	private String getNextUtility(StringTokenizer st) throws Exception {

		String string = null;

		string = st.nextToken();

		if (string.equals("}"))
			return string;

		if (string.charAt(string.length() - 1) == ',') {
			string = string.substring(0, (string.length() - 1));
		}

		return string;
	}

	public BehaviourStrategy getBehaviourStrategy(int iPlayer,
			double[] realizationProbabilities) throws Exception {
		int iSequence, jSequence;
		TreeNode childNode, treeNode;
		BehaviourStrategy strategy = new BehaviourStrategy(iPlayer, this);
		double probability;
		int informationSetNumber;
		String moveName;

		for (iSequence = sequence[iPlayer]; iSequence > 0; iSequence--) {
			jSequence = 0;
			childNode = (TreeNode) sequenceNodesLists[iPlayer]
					.get(iSequence - 1);

			treeNode = childNode.getParentNode();

			while (treeNode != null && treeNode.getParentNode() != null
					&& treeNode.getParentNode().getPlayer() != iPlayer) {
				treeNode = treeNode.getParentNode();
			}

			if (treeNode != null) {
				jSequence = treeNode.getSequenceNumber();
			}

			if (realizationProbabilities[jSequence] == 0) {
				probability = 0;
			} else {
				probability = realizationProbabilities[iSequence]
						/ realizationProbabilities[jSequence];
			}
			informationSetNumber = childNode.getParentNode()
					.getInformationSet().getInformationSetNumber();
			moveName = childNode.getParentEdge();

			strategy
					.setProbability(informationSetNumber, probability, moveName);
		}
		return strategy;
	}

	private double roundValue(double x) {
		x = x * 1000;
		x = Math.round(x);
		x = ((double) x) / 1000;
		return x;

	}

}
