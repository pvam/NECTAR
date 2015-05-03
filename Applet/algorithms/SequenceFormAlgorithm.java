/**
 * 
 */
package algorithms;

import games.*;

/**
 * @author kalyan
 * 
 * Algorithm Paper : "Efficient coputation of equilibria for extensive
 * two-person games", D. Koller, N. Megiddo, and B. von Stengel. Games and
 * Economic Behavior, 14(2):247-259, 1996
 */
public class SequenceFormAlgorithm extends Pivoting {

	private final byte PLAYER1 = 0;

	private final byte PLAYER2 = 1;

	private double[] bValues = null;

	private SequenceFormGame sfg = null;

	private String algorithmName = null;

	public SequenceFormAlgorithm(SequenceFormGame sfg) {
		this.sfg = sfg;
		this.algorithmName = new String("Sequence Form Algorithm");
	}

	/**
	 * Computes the equilibrium point of the sequence form game
	 * 
	 * @return the result object
	 * @throws Exception
	 */
	public SequenceFormResultObject computeEquilibrium() throws Exception {
		int variable;
		int index = 0;

		// get the no.of players
		int nPlayers = sfg.getPlayers();

		// this algorithm works for only 2 players game
		if (nPlayers != 2) {
			throw new Exception("The given game is not a two person game");
		}

		// initializing the matrix which contains the set of linear equations
		initializeMatrix();
		// do initial pivoting
		setBasicVariables();

		// printMatrix();
		// printVariables();
		while (true) {
			// get the missed base variable
			variable = getMissedBaseVariable();
			// System.out.println("Previous Variable : " + previousVariable);
			// System.out.println("Missed Base Variable : " + variable);

			// if the variable is zero mean pivoting operation is over
			if (variable == 0) {
				// System.out.println("Successfully Operation finished \n\n");
				// printMatrix();
				return getResults();
			}

			// finds the column index of missed variable
			index = findColumnIndex(variable);
			// System.out.println("Index in Cobase :" + index);

			// selects the base variable for pivoting
			Index in = selectPivot(index);
			// System.out.println("Leaving Variable : " + base[in.i]);

			pivot(in);
			previousVariable = base[in.i];
			// swap the variables
			swap(in);
			// printMatrix();
			// printVariables();
		}
	}

	private void initializeMatrix() throws Exception {
		int i, j, k, l, m;
		// no. of sequences of all players
		int[] sequences = null;
		// no. of information sets of all players
		int[] informationSetSize = null;
		// utility matrix of i'th player
		double[][] utilityMatrix = null;
		// contraint matrix of i'th player
		double[][] constraintMatrix = null;

		sequences = sfg.getSequences();
		informationSetSize = sfg.getinformationSetSize();

		rows = sequences[PLAYER1] + sequences[PLAYER2] + 2
				* (informationSetSize[PLAYER1] + informationSetSize[PLAYER2])
				+ 4;
		columns = rows + 2;

		// initialize the matrix
		matrix = new double[rows][columns];

		/*
		 * find the maximum utility
		 */
		double maxUtility = 0;

		utilityMatrix = sfg.getUtilityMatrix(PLAYER2);
		maxUtility = findMaximum(utilityMatrix, maxUtility);
		// System.out.println("Maximum Utility : " + maxUtility);
		utilityMatrix = sfg.getUtilityMatrix(PLAYER1);
		maxUtility = findMaximum(utilityMatrix, maxUtility);
		// System.out.println("Maximum Utility : " + maxUtility);

		/*
		 * update the matrix elements with utilities and constraints
		 */
		k = sequences[PLAYER1];
		for (i = 0; i < sequences[PLAYER1]; i++) {
			for (j = 0; j < sequences[PLAYER2]; j++) {
				matrix[i][j + k] = -(utilityMatrix[i][j] - maxUtility);
			}
		}
		// get the utility matrix of player 2
		utilityMatrix = sfg.getUtilityMatrix(PLAYER2);

		for (i = 0; i < sequences[PLAYER1]; i++) {
			for (j = 0; j < sequences[PLAYER2]; j++) {
				matrix[j + k][i] = -(utilityMatrix[i][j] - maxUtility);
			}
		}
		// get the constraint matrix of player 1
		constraintMatrix = sfg.getConstraintMatrix(PLAYER1);

		k = sequences[PLAYER1] + sequences[PLAYER2];
		l = 1 + informationSetSize[PLAYER1];

		for (i = 0; i <= informationSetSize[PLAYER1]; i++) {
			for (j = 0; j < sequences[PLAYER1]; j++) {
				matrix[j][i + k] = constraintMatrix[i][j];
				matrix[j][i + k + l] = -constraintMatrix[i][j];
				matrix[i + k][j] = -constraintMatrix[i][j];
				matrix[i + k + l][j] = constraintMatrix[i][j];
			}
		}
		// get the constraint matrix of player 2
		constraintMatrix = sfg.getConstraintMatrix(PLAYER2);

		k = k + 2 * (1 + informationSetSize[PLAYER1]);
		l = sequences[PLAYER1];
		m = 1 + informationSetSize[PLAYER2];

		for (i = 0; i <= informationSetSize[PLAYER2]; i++) {
			for (j = 0; j < sequences[PLAYER2]; j++) {
				matrix[j + l][i + k] = constraintMatrix[i][j];
				matrix[j + l][i + k + m] = -constraintMatrix[i][j];
				matrix[i + k][j + l] = -constraintMatrix[i][j];
				matrix[i + k + m][j + l] = constraintMatrix[i][j];
			}
		}

		// set the rhs values
		bValues = new double[columns];

		k = sequences[PLAYER1] + sequences[PLAYER2];
		bValues[k] = 1;
		k = k + 1 + informationSetSize[PLAYER1];
		bValues[k] = -1;
		k = k + 1 + informationSetSize[PLAYER1];
		bValues[k] = 1;
		k = k + 1 + informationSetSize[PLAYER2];
		bValues[k] = -1;

		for (i = 0; i < rows; i++) {
			matrix[i][columns - 2] = 1;
			matrix[i][columns - 1] = bValues[i];
		}

		// initializes the inverse matrix for pivoting
		this.inverseBaseMatrix = new double[rows][rows];
		// initializing for pivoting operation
		this.base = new int[rows];
		this.row = new int[rows];
		this.coBase = new int[columns];
		this.column = new int[columns];
		this.determinent = 1;

		for (i = 0; i < rows; i++) {
			base[i] = i + 1;
			row[i] = i;
		}
		for (j = 0; j < columns; j++, i++) {
			coBase[j] = i + 1;
			column[j] = j;
		}
		coBase[columns - 2] = 0;

		return;
	}

	/**
	 * updates result object with the obtained results
	 * 
	 * @return result object of the equilibrium point
	 */
	private SequenceFormResultObject getResults() {
		int i;

		SequenceFormResultObject result = new SequenceFormResultObject();

		double[] bValues = new double[rows];
		for (i = 0; i < rows; i++) {
			bValues[i] = matrix[i][columns - 1] / determinent;
		}

		result.setAlgorithmName(this.algorithmName);
		result.setBase(base);
		result.setCoBase(coBase);
		result.setBValues(bValues);
		result.setSequenceFormGame(sfg);

		return result;
	}

	/**
	 * finds the maximum utility from the given matrix, if it is larger than
	 * maxUtility then update the maxUtility with new new maximum utility
	 * 
	 * @param utilityMatrix
	 *            utility matrix
	 * @param maxUtility
	 *            maximum utility
	 * @return maximum utility
	 */
	private double findMaximum(double[][] utilityMatrix, double maxUtility) {
		int i, j;

		for (i = 0; i < utilityMatrix.length; i++) {
			for (j = 0; j < utilityMatrix[0].length; j++) {

				if (maxUtility < utilityMatrix[i][j]) {
					maxUtility = utilityMatrix[i][j];
				}
			}
		}
		return maxUtility;
	}

	/**
	 * setting the intial basis variables by intial pivoting
	 */
	private void setBasicVariables() {
		int i;

		int rowIndex = 0;
		int columnIndex = column[columns - 2];

		double minimum = matrix[0][columns - 1];

		for (i = 1; i < rows; i++) {
			if (minimum >= matrix[i][columns - 1]) {
				minimum = matrix[i][columns - 1];
				rowIndex = i;
			}
		}

		Index index = new Index(rowIndex, columnIndex);
		pivot(index);
		previousVariable = base[index.i];
		// swaping the variables
		swap(index);

		return;
	}

	/**
	 * (Method Overloading) This computes minimum ratios of a specified column
	 * of DMatrix and stores result in minArray minArray[0] = no. of minimum
	 * ratios minArray[1...] = list of indices of minimum ratios
	 * 
	 * @param sIndex
	 *            actual position of co-base variable
	 * @param minArray
	 *            contains the list minimum ratios
	 * @param columnNo
	 *            column no of DMatrix
	 */
	protected void findMinRatio(int sIndex, int[] minArray, int columnNo) {
		double minimum;
		double ratio;
		int count = 1;
		int i;
		int iterations = minArray[0];

		minimum = inverseBaseMatrix[minArray[1]][columnNo]
				/ ((-matrix[minArray[1]][sIndex]) / determinent);

		for (i = 2; i <= iterations; i++) {

			ratio = inverseBaseMatrix[minArray[i]][columnNo]
					/ ((-matrix[minArray[i]][sIndex]) / determinent);

			if (minimum >= ratio) {
				if (minimum == ratio) {
					minArray[++count] = minArray[i];
				} else {
					count = 1;
					minArray[count] = minArray[i];
				}
			}
		}
		minArray[0] = count;
		return;
	}
}
