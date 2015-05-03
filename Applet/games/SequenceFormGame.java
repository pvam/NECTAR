/**
 * 
 */
package games;

import java.util.ArrayList;

/**
 * @author kalyan
 * 
 */
public class SequenceFormGame {

	final private byte PLAYER1 = 0;

	final private byte PLAYER2 = 1;

	// extensive form game
	private ExtensiveFormGame efg = null;

	// no. of players
	private int nPlayers;

	// no. of sequences of each player
	private int[] sequences = null;

	// no. of information sets of each player
	private int[] informationSetSize = null;

	// constraint Matrices of each player
	private ArrayList constraintMatrices = null;

	// utility matrices of each player
	private ArrayList utilityMatrices = null;

	// 
	private double[][] chanceMatrix = null;

	public SequenceFormGame(ExtensiveFormGame efg) {
		this.efg = efg;
		initialize();
	}

	/**
	 * This method initializes all the variable
	 */
	private void initialize() {
		int i;
		int iPlayer;
		int totalSequences;

		ArrayList informationSets = null;
		InformationSet informationSet = null;
		double[][] constraintMatrix = null;
		double[][] utilityMatrix = null;

		nPlayers = efg.getNPlayers();

		informationSetSize = new int[nPlayers];
		sequences = new int[nPlayers];

		/*
		 * This block finds the no.of sequences for each player.
		 */
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {

			informationSets = efg.getInformationSets(iPlayer);
			// information set size of player i
			informationSetSize[iPlayer] = informationSets.size();
			// counting the null sequence
			totalSequences = 1;
			for (i = 0; i < informationSetSize[iPlayer]; i++) {
				informationSet = (InformationSet) informationSets.get(i);
				totalSequences = totalSequences + informationSet.getDegree();
			}
			// total sequences of player i
			sequences[iPlayer] = totalSequences;
		}

		/*
		 * Initializing the constraint matrices, and utility matrices for each
		 * player
		 */
		constraintMatrices = new ArrayList(nPlayers);
		utilityMatrices = new ArrayList(nPlayers);

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			constraintMatrix = new double[informationSetSize[iPlayer] + 1][sequences[iPlayer]];
			constraintMatrices.add(iPlayer, constraintMatrix);
			utilityMatrix = new double[sequences[0]][sequences[1]];
			// System.out.println("matrix size ; " + utilityMatrix.length);
			utilityMatrices.add(iPlayer, utilityMatrix);
		}

		// this matrix represents probabilities of chance player
		chanceMatrix = new double[sequences[0]][sequences[1]];
		// initilizing all elements to 1
		for (i = 0; i < sequences[0]; i++) {
			for (int j = 0; j < sequences[1]; j++) {
				chanceMatrix[i][j] = 1;
			}
		}
		// System.out.println("Player 1 sequences : " + sequences[0]);
		// System.out.println("Player 2 sequences : " + sequences[1]);

		return;
	}

	/**
	 * This method makes constraint matrices and utility matrices of each player
	 * from the extensive form game.
	 */
	public void updateMatrices() {
		generateConstraintMatrices();
		generateUtilityMatrices();

		return;
	}

	/**
	 * get the no. of sequences of each player
	 * 
	 * @return no. of sequences of each player
	 */
	public int[] getSequences() {
		return this.sequences;
	}

	/**
	 * get the no.of information sets of each player
	 * 
	 * @return no. of information sets of each player
	 */
	public int[] getinformationSetSize() {
		return this.informationSetSize;

	}

	/**
	 * get the no.of players of the game
	 * 
	 * @return no. of players
	 */
	public int getPlayers() {
		return this.nPlayers;
	}

	/**
	 * get the constraint matrix of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th Player
	 * 
	 * @return constraint matrix
	 * 
	 * @throws Exception
	 *             if the i'th player is invalid
	 */
	public double[][] getConstraintMatrix(int iPlayer) throws Exception {
		if (iPlayer < nPlayers && iPlayer >= 0) {
			return (double[][]) this.constraintMatrices.get(iPlayer);
		}
		throw new Exception("Invalid Player");
	}

	/**
	 * get the utility matrix of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @return utility matrix
	 * @throws Exception
	 *             if the i'th player is invalid
	 */
	public double[][] getUtilityMatrix(int iPlayer) throws Exception {
		if (iPlayer < this.nPlayers && iPlayer >= 0) {
			return (double[][]) this.utilityMatrices.get(iPlayer);
		}
		throw new Exception("Invalid Player");
	}

	/**
	 * get the chance matrix of the chance player
	 * 
	 * @return chance matrix
	 */
	public double[][] getchanceMatrix() {
		return this.chanceMatrix;
	}

	/**
	 * This method generates the constraint matrices of each player from the
	 * extensive form game
	 */
	private void generateConstraintMatrices() {
		int iPlayer, iInfoSet, iChild;

		double[][] constraintMatrix = null;
		ArrayList informationSets = null;
		InformationSet informationSet = null;
		TreeNode treeNode = null;
		TreeNode parentNode = null;
		TreeNode childNode = null;

		// indices of a constraint matrix
		int ithRow, jthColumn;

		// System.out.println("Inside generateConstraintMatrices()");

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {

			constraintMatrix = (double[][]) constraintMatrices.get(iPlayer);

			// for realization plan x_i(phi) = 1
			constraintMatrix[0][0] = 1;

			informationSets = efg.getInformationSets(iPlayer);
			// System.out.println("Player " + (iPlayer + 1) + " InformationSet
			// size : " + informationSets.size());

			/*
			 * update the constraint matrix row by row. For each information
			 * set, update corresponding row
			 * 
			 * Row Equation: -x_i(sigma_h) + sum_{x_i(sigma_h c) = 0
			 */
			for (iInfoSet = 0; iInfoSet < informationSets.size(); iInfoSet++) {

				informationSet = (InformationSet) informationSets.get(iInfoSet);
				treeNode = informationSet.getTreeNode(0);
				// System.out.println("Tree Node type: " +
				// treeNode.getNodeType() + " Sequence Number: " +
				// treeNode.getSequenceNumber());
				parentNode = treeNode.getParentNode();
				// System.out.println("Parent Node type: " +
				// parent.getNodeType() + " Sequence Number: " +
				// parent.getSequenceNumber());

				jthColumn = 0;
				while (parentNode != null) {
					if (parentNode.getParentNode() != null) {
						if (parentNode.getParentNode().getPlayer() == treeNode
								.getPlayer()) {
							jthColumn = parentNode.getSequenceNumber();
						}
					}
					parentNode = parentNode.getParentNode();
				}
				ithRow = iInfoSet + 1;
				constraintMatrix[ithRow][jthColumn] = -1;

				for (iChild = 0; iChild < treeNode.getDegree(); iChild++) {
					childNode = treeNode.getChildAt(iChild);
					jthColumn = childNode.getSequenceNumber();
					constraintMatrix[ithRow][jthColumn] = 1;
				}
			}
		}
	}

	/**
	 * This method generates the utility matrices of each player from the
	 * extensive form game
	 */
	private void generateUtilityMatrices() {
		int iVector, iPlayer;
		int counter;

		double totalProbability, probability;

		// stores the sequence numbers of all players
		int[] seqNo = new int[nPlayers];

		ArrayList utilityList = null;
		UtilityVector utilityVector = null;
		ProbabilityVector probVector = null;
		TreeNode treeNode = null;
		TreeNode terminalNode = null;
		TreeNode childNode = null;
		ArrayList utilities = null;
		double[][] utilityMatrix = null;

		// get the utility vectors list from the extensive form game
		utilityList = efg.getUtilityList();
		for (iVector = 0; iVector < utilityList.size(); iVector++) {

			// get the i'th utility vector from the utility vectors list
			utilityVector = (UtilityVector) utilityList.get(iVector);
			// get the utilities from the utility vector object
			utilities = utilityVector.getUtilities();
			// get the corresponding terminal tree node
			terminalNode = utilityVector.getTerminalNode();
			// get the parent node of terminal node
			treeNode = terminalNode.getParentNode();
			// set child node to tree node is terminal node
			childNode = terminalNode;

			/*
			 * intially set the sequence number to all player to zero that means
			 * null sequence.
			 */
			for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
				seqNo[iPlayer] = 0;
			}

			/*
			 * get the sequence numbers of all players for this terminal node
			 */
			counter = 0;
			while (counter != nPlayers) {
				// get the player
				iPlayer = treeNode.getPlayer();

				// check if i'th player is chance player
				if (iPlayer != nPlayers) {
					// get the sequence number
					seqNo[iPlayer] = childNode.getSequenceNumber();
					// System.out.println("Sequence Number : " +
					// treeNode.getSequenceNumber());
					counter++;
				}
				// traverse towards to root node
				childNode = treeNode;
				treeNode = treeNode.getParentNode();
				if (treeNode == null) {
					break;
				}
			}

			/*
			 * find the probability placed by chance player on this sequence to
			 * terminal node
			 */
			treeNode = terminalNode.getParentNode();
			childNode = terminalNode;

			// intilizes with 1, it means that no probability placed on this
			// sequence
			totalProbability = 1;
			while (treeNode != null) {
				// check whether the node is chance player's node
				if (treeNode.getNodeType().equals("Chance")) {
					// get the probability vector
					probVector = treeNode.getProbabilityVector();
					// get the probability placed on this sequence at this node
					probability = probVector.getProbability(childNode);
					if (probability == 0) {
						System.out
								.println("Bug!! in extensive form game object. Inform immidiately to author");
					}
					totalProbability = totalProbability * probability;
				}
				// traverse towards to the root node
				childNode = treeNode;
				treeNode = treeNode.getParentNode();
			}

			// update the utility matrices of each player
			for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
				utilityMatrix = (double[][]) utilityMatrices.get(iPlayer);
				// System.out.println("Sequnces Numbers: Player 1 -> " +
				// seqNo[PLAYER1] + " Player2 -> " + seqNo[PLAYER2]);
				utilityMatrix[seqNo[PLAYER1]][seqNo[PLAYER2]] += ((Double) utilities
						.get(iPlayer)).doubleValue();
			}
			// stores the probability placed on this sequence
			chanceMatrix[seqNo[PLAYER1]][seqNo[PLAYER2]] = totalProbability;
		}
		return;
	}

	/**
	 * get the name of the given game
	 * 
	 * @return game name
	 */
	public String getGameName() {
		return efg.getGameName();
	}

	/**
	 * displays the sequence form game represenation
	 * 
	 * @return game represenation
	 */
	public String printSequenceFormGame() {
		StringBuffer stream = new StringBuffer();

		stream.append("Game Name : " + this.getGameName() + "\n\n");
		stream.append("\tPlayer 1 Sequences  :   " + this.sequences[PLAYER1]
				+ "\n");
		stream.append("\tPlayer 2 Sequences  :   " + this.sequences[PLAYER2]
				+ "\n\n");

		printUtilityMatrices(stream);
		printConstraintMatrices(stream);

		return stream.toString();
	}

	/**
	 * displays the constraint matrices of all players
	 * 
	 * @param stream :
	 *            the output stream
	 */
	public void printConstraintMatrices(StringBuffer stream) {
		int i, j, iPlayer;
		double[][] constraintMatrix = null;

		stream.append("::: Constraint Matrices :::\n\n");

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			constraintMatrix = (double[][]) constraintMatrices.get(iPlayer);

			stream.append("Player " + (iPlayer + 1) + " : \n");

			for (i = 0; i < informationSetSize[iPlayer] + 1; i++) {
				stream.append("\n");
				for (j = 0; j < sequences[iPlayer]; j++) {
					stream.append("  " + constraintMatrix[i][j]);
				}
			}
			stream.append("\n\n");
		}
		return;
	}

	/**
	 * displays the utility matrices of all players
	 * 
	 * @param stream :
	 *            the output stream
	 */
	public void printUtilityMatrices(StringBuffer stream) {
		int i, j, iPlayer;
		double[][] utilityMatrix = null;

		stream.append("::: Utility Matrices ::: \n\n");

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			utilityMatrix = (double[][]) utilityMatrices.get(iPlayer);

			stream.append("Player " + (iPlayer + 1) + " : \n");

			for (i = 0; i < sequences[0]; i++) {
				stream.append("\n");
				for (j = 0; j < sequences[1]; j++) {
					stream.append("  "
							+ roundValue(utilityMatrix[i][j]
									* chanceMatrix[i][j]));
				}
			}
			stream.append("\n\n");
		}
		return;
	}

	public String getMoveName(int iPlayer, int sequenceNumber) {

		if (sequenceNumber == 0) {
			return new String(" - ");
		}
		return efg.getMoveName(iPlayer, sequenceNumber);
	}

	public BehaviourStrategy getBehaviourStrategy(int iPlayer,
			double[] realizationProbabilities) throws Exception {
		return efg.getBehaviourStrategy(iPlayer, realizationProbabilities);
	}

	/**
	 * rounding the value for better readability
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
