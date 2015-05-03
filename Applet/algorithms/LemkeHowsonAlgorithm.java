/**
 * 
 */
package algorithms;

import games.NormalFormGame;

/**
 * @author kalyan
 * 
 * Algorithm Paper : "Equilibrium points of bimatrix games" by C.E. Lemke and
 * J.T. Howson, Journal of the Society for Industrial and Applied Mathematics,
 * 12(2): 413-423, 1964.
 */
public class LemkeHowsonAlgorithm extends Pivoting implements Algorithm {

	private final int PLAYER1 = 0;

	private final int PLAYER2 = 1;

	private boolean flag = false;

	private String algorithmName = null;

	NormalFormGame nfg = null;

	NormalFormGame oldNfg = null;

	public LemkeHowsonAlgorithm() {
		this.algorithmName = new String("Lemke - Howson Algorithm");
	}

	/**
	 * It computes a Nash equilibrium point for the given normal form game using
	 * the Lemke-Howson's algorithm ane returns the result object
	 * 
	 * @param nfg :
	 *            Normal Form Game
	 * @return the result object which contains the equilibrium details
	 */
	public ResultObject computeEquilibria(NormalFormGame nfg) throws Exception {
		int i, j, k;
		int nPlayers;
		int[] cardinalities = null;
		int[] profile = null;

		this.nfg = nfg;

		cardinalities = nfg.getAllCardinalities();
		nPlayers = nfg.getPlayers();

		if (nPlayers != 2) {
			throw new Exception("The given game is not a two person game");
		}

		// initializing variables
		rows = cardinalities[PLAYER1] + cardinalities[PLAYER2];
		columns = rows + 1;
		matrix = new double[rows][columns];
		profile = new int[nPlayers];

		k = cardinalities[PLAYER1];

		// copying player 1 utilities to the matrix
		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			profile[PLAYER1] = i;
			for (j = 0; j < cardinalities[PLAYER2]; j++) {
				profile[PLAYER2] = j;
				matrix[i][j + k] = -nfg.getUtility(profile, PLAYER1);
			}
		}

		// copying player 2 utilities to the matrix
		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			profile[PLAYER1] = i;
			for (j = 0; j < cardinalities[PLAYER2]; j++) {
				profile[PLAYER2] = j;
				matrix[j + k][i] = -nfg.getUtility(profile, PLAYER2);
			}
		}

		// updating b values in the linear equations
		for (i = 0, j = columns - 1; i < rows; i++) {
			matrix[i][j] = 1;
		}

		initialize();

		ResultObject resultObject = compute();
		return resultObject;
	}

	/**
	 * It initializes the all variables
	 * 
	 */
	public void initialize() {
		this.inverseBaseMatrix = new double[rows][rows];

		this.base = new int[rows];
		this.row = new int[rows];
		this.coBase = new int[columns];
		this.column = new int[columns];

		determinent = 1;

		int i, j;

		for (i = 0; i < rows; i++) {
			base[i] = i + 1;
			row[i] = i;
		}
		for (j = 0; j < columns; j++, i++) {
			coBase[j] = i + 1;
			column[j] = j;
		}

		// printVariables();
		// printMatrix(matrix,determinent);
	}

	/**
	 * It computes the Nash equilibrium point
	 * 
	 * @return result object which contains equilibrium details
	 * 
	 * @throws Exception
	 */
	public ResultObject compute() throws Exception {
		int variable;
		int index = 0;

		setBasicVariables();
		// System.out.println("After Setting Basic Variablees: ");
		// printMatrix(matrix, determinent);

		try {

			while (true) {
				variable = getMissedBaseVariable();
				// System.out.println("Previous Variable:" + previousVariable);
				// System.out.println("Mised Base Variable : " + variable);

				if (variable == 0) {
					// System.out.println("Successfully Operation finished
					// \n\n");
					// printMatrix(matrix,determinent);
					break;
				}

				index = findColumnIndex(variable);
				// System.out.println("Missing Variable :" + variable);
				// System.out.println("Index in Cobase :" + index);
				// System.out.println("Entering Variable " + coBase[index]);

				Index in = selectPivot(index);
				// System.out.println("Leaving Variable: " + base[in.i]);

				// System.out.println("matrix[r][s] " +
				// matrix[in.i][in.j]/determinent);
				// printMatrix(matrix, determinent);
				pivot(in);
				// System.out.println("After pivot ::: matrix[r][s] " +
				// matrix[in.i][in.j]/determinent);
				previousVariable = base[in.i];
				swap(in);
				// printMatrix(matrix,determinent);
				printVariables();
			}

			if (flag) {
				nfg = oldNfg;
			}

			return getResults();
		} catch (Exception e) {

			if (!flag) {
				oldNfg = nfg;
				flag = true;
			}
			MiscAlgorithm misc = new MiscAlgorithm();
			nfg = misc.getNewGame(nfg);
			return this.computeEquilibria(nfg);

		}

	}

	private void setBasicVariables() {
		int i;
		int variable;
		int rowIndex = 0;
		int columnIndex = 0;

		double minimum = matrix[nfg.getCardinality(PLAYER1)][columnIndex];
		for (i = nfg.getCardinality(PLAYER1); i < rows; i++) {
			if (minimum >= matrix[i][columnIndex]) {
				minimum = matrix[i][columnIndex];
				rowIndex = i;
			}
		}

		printVariables();
		printMatrix();

		Index index = new Index(rowIndex, columnIndex);
		pivot(index);

		previousVariable = base[index.i];
		// System.out.println("previous Variable : " + previousVariable);
		swap(index);

		printVariables();
		printMatrix();

		variable = getMissedBaseVariable();

		columnIndex = findColumnIndex(variable);
		// System.out.println("columnIndex : " + columnIndex);

		minimum = matrix[0][columnIndex];
		for (i = 0; i < nfg.getCardinality(PLAYER1); i++) {
			if (minimum >= matrix[i][columnIndex]) {
				minimum = matrix[i][columnIndex];
				rowIndex = i;
			}
		}

		index = new Index(rowIndex, columnIndex);
		pivot(index);
		previousVariable = base[index.i];
		swap(index);

		printVariables();
		printMatrix();

		return;
	}

	private ResultObject getResults() {
		int i, j, k;
		int size = base.length;
		int iStrategy;

		NashResultObject resultObject = new NashResultObject();

		double[] variables = new double[size];
		double expectedU, expectedV;
		int[] cardinalities = nfg.getAllCardinalities();
		int[] profile = new int[nfg.getPlayers()];

		// Testing
/*		
		System.out.println("Variables :"); 
		for(i = 0; i < size; i++) {
			System.out.println((base[i] - 1) + " --> " + 
					(matrix[i][columns-1]/determinent) ); 
		}
*/

		for (i = 0; i < size; i++) {
			if (base[i] > rows) {
				iStrategy = base[i] - rows - 1;
				variables[iStrategy] = matrix[i][columns - 1] / determinent;
			}
		}

		expectedU = 0;
		for (i = 0; i < nfg.getCardinality(PLAYER1); i++) {
			expectedU += variables[i];
		}

		expectedV = 0;
		for (; i < size; i++) {
			expectedV += variables[i];
		}

		resultObject.setAlgorithmName(this.algorithmName);
		resultObject.setGameName(nfg.getGameName());
		// System.out.println("Game Name : " + nfg.getGameName());
		// System.out.println("Algorithm Name : " + "Lemke - Howson Algorithm
		// \n");

		// System.out.println("\t Player 1 :");
		double[] probabilityDistribution = null;
		NashEquilibrium equilibrium = new NashEquilibrium(nfg.getPlayers(), nfg
				.getAllCardinalities());
		probabilityDistribution = new double[nfg.getCardinality(PLAYER1)];

		for (i = 0; i < nfg.getCardinality(PLAYER1); i++) {

			variables[i] = variables[i] / expectedU;
			probabilityDistribution[i] = variables[i];
			System.out.println("\t\tStrategy " + (i + 1) + " --> "
					+ (variables[i] / expectedU));
		}
		equilibrium
				.setProbabilityDistribution(PLAYER1, probabilityDistribution);

		// System.out.println("\n\t Player 2 : ");
		probabilityDistribution = new double[nfg.getCardinality(PLAYER2)];
		for (j = 0; i < size; i++, j++) {
			variables[i] = variables[i] / expectedV;
			probabilityDistribution[j] = variables[i];
			// System.out.println("\t\tStrategy " + (j+1) + " --> " +
			// (variables[i]/ expectedV) + "\n");
		}
		equilibrium
				.setProbabilityDistribution(PLAYER2, probabilityDistribution);

		// stream.append("\n\n");

		expectedU = 0;
		expectedV = 0;
		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			profile[PLAYER1] = i;
			for (j = 0, k = cardinalities[PLAYER1]; j < cardinalities[PLAYER2]; j++, k++) {
				profile[PLAYER2] = j;
				expectedU += nfg.getUtility(profile, PLAYER1) * variables[i]
						* variables[k];
				expectedV += nfg.getUtility(profile, PLAYER2) * variables[i]
						* variables[k];
			}
		}
		equilibrium.setExpectedUtility(PLAYER1, expectedU);
		equilibrium.setExpectedUtility(PLAYER2, expectedV);

		resultObject.addEqulibrium(equilibrium);

		// System.out.println("\t Player 1 Expected Utility : " + expectedU);
		// System.out.println("\t Player 2 Expected Utility : " + expectedV);

		return resultObject;
	}

}
