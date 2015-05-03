/**
 * 
 */
package algorithms;

import games.NormalFormGame;

import java.util.ArrayList;

/**
 * @author kalyan
 * 
 * Algorithm Paper : "Equilibrium Points of Bimatrix Games" by O.L. Mangasarian,
 * Journal of the Society for Industrial and Applied Mathematics, 12(4):778-780,
 * December 1964
 */
public class MangasarianAlgorithm implements Algorithm {

	NormalFormGame nfg = null;

	final int PLAYER1 = 0;

	final int PLAYER2 = 1;

	private String algorithmName = null;

	public MangasarianAlgorithm() {
		this.algorithmName = new String("Mangasarian's Algorithm");
	}

	/**
	 * It computes the all extreme equilibrium points of the given normal form
	 * game
	 */
	public ResultObject computeEquilibria(NormalFormGame nfg) throws Exception {
		double[][] array = null;
		int rows;
		int columns1, columns2;
		int i, j;
		int[] cardinality = null;
		int[] profile;
		int nPlayers;

		this.nfg = nfg;

		VertexEnumeration vertex = new VertexEnumeration();

		nPlayers = nfg.getPlayers();
		cardinality = nfg.getAllCardinalities();
		profile = new int[nPlayers];

		if (nPlayers != 2) {
			throw new Exception("The given game is not a two person game");
		}

		rows = cardinality[PLAYER1] + cardinality[PLAYER2] + 1;

		/*
		 * the no. of columns are equals to the no. of strategies of player i
		 * and the expected utility and the last one is for right side
		 * values(b). i.e.
		 * 
		 * b + cx > 0 form
		 */
		columns1 = cardinality[PLAYER1] + 2;
		columns2 = cardinality[PLAYER2] + 2;

		array = new double[rows][columns1];

		/*
		 * Here first column represents 'b' value second column represents alpha
		 * or beta remaining columns represents strties of player i
		 */
		// forming player 1's equations
		// constraint summation over the probability distribution
		for (i = 2; i < columns1; i++) {
			array[0][i] = 1;
		}
		array[0][0] = -1;
		array[0][1] = 0;

		// update the constraints of the form --> Ay - (alpha)
		for (profile[PLAYER1] = 0, i = 2; profile[PLAYER1] < cardinality[PLAYER1]; profile[PLAYER1]++, i++) {
			for (profile[PLAYER2] = 0, j = 1; profile[PLAYER2] < cardinality[PLAYER2]; profile[PLAYER2]++, j++) {
				array[j][i] = -nfg.getUtility(profile, PLAYER2);
			}
		}
		for (i = 1; i <= cardinality[PLAYER2]; i++) {
			array[i][0] = 0;
			array[i][1] = 1;
		}

		// constraints on the probability on each strategy of the form --> y > 0
		int rowIndex = 1 + cardinality[PLAYER2];
		int columnIndex = 2;
		for (i = 0; i < cardinality[PLAYER1]; i++) {
			array[rowIndex + i][columnIndex + i] = 1;
		}

		// for testing
		// this.printArray(array);

		// get the all vertices of the polyhedral set in the array
		ArrayList list1 = vertex.computeVertexEnumeration(array);

		/*
		 * Do the same to the other player
		 */
		array = new double[rows][columns2];

		// forming player 2's equations
		for (i = 2; i < columns2; i++) {
			array[0][i] = 1;
		}
		array[0][0] = -1;
		array[0][1] = 0;

		for (profile[PLAYER1] = 0, i = 1; profile[PLAYER1] < cardinality[PLAYER1]; profile[PLAYER1]++, i++) {
			for (profile[PLAYER2] = 0, j = 2; profile[PLAYER2] < cardinality[PLAYER2]; profile[PLAYER2]++, j++) {
				array[i][j] = -nfg.getUtility(profile, PLAYER1);
			}
		}
		for (i = 1; i <= cardinality[PLAYER1]; i++) {
			array[i][0] = 0;
			array[i][1] = 1;
		}

		rowIndex = 1 + cardinality[PLAYER1];
		columnIndex = 2;
		for (i = 0; i < cardinality[PLAYER2]; i++) {
			array[rowIndex + i][columnIndex + i] = 1;
		}

		// for testing
		// this.printArray(array);

		ArrayList list2 = vertex.computeVertexEnumeration(array);

		// testing prints the result
		/*
		 * printVertexList(list1); printVertexList(list2);
		 */
		// find the all extreme equlibrium points
		ArrayList list = findExtremeEquilibria(list1, list2);

		// get the result in a stream form
		NashResultObject resultObject = getResults(list);
		// return the computation result
		return resultObject;
	}

	
	// This is for debugging.. 
/*	private void printArray(double[][] array) {
		int i,j; 
		
		System.out.println("\n");
		for ( i = 0; i< array.length; i++) { 
			System.out.println(); 
			for ( j=0; j < array[0].length; j++) 
				System.out.print(" " + (array[i][j])); 
		}
		System.out.println("\n"); 
	}
*/	
	// This is for debugging 
/*
  	private void printVertexList(ArrayList list) {
	  
		int i,j; double[] values = null;
	  
		System.out.println("Vertices List : "); 
		for(i=0; i < list.size(); i++) {
			values = (double[])list.get(i);
			for(j = 0; j < values.length; j++) { 
				System.out.print(" " + roundValue(values[j])); 
			} 
			System.out.println(); 
		} 
		return; 
	}
*/

	/**
	 * It finds all the extreme equilibrium points from the vertices of
	 * polyhedral sets S and T, represented in the paper.
	 * 
	 * @param sList:
	 *            polyhedral set S
	 * @param tList:
	 *            polyhedral set T
	 * @return the extreme equilibrium points list
	 */
	private ArrayList findExtremeEquilibria(ArrayList sList, ArrayList tList)
			throws Exception {
		int iVertex, jVertex;
		ArrayList equilibriaList = new ArrayList();
		double[] x = null;
		double[] y = null;
		ArrayList equilibrium = null;
		boolean isEquilibrium;

		/*
		 * check the condition for each vertex in set S and each vertex in the
		 * set T.
		 */
		for (iVertex = 0; iVertex < sList.size(); iVertex++) {

			x = (double[]) sList.get(iVertex);

			for (jVertex = 0; jVertex < tList.size(); jVertex++) {

				y = (double[]) tList.get(jVertex);

				isEquilibrium = isExtremeEquilibrium(nfg, x, y);

				// if it is equilibrium point then add to the list
				if (isEquilibrium) {
					equilibrium = new ArrayList(2);
					equilibrium.add(0, x);
					equilibrium.add(1, y);
					equilibriaList.add(equilibrium);
				}
			}
		}
		return equilibriaList;
	}

	/**
	 * It checks whether the given two vertices of S and T forms equilibrium or
	 * not by satisfying the condition specified in the algorithm.
	 * 
	 * @param nfg :
	 *            Normal form game
	 * @param x :
	 *            probability distribution over player 1 and expected utility of
	 *            player 2
	 * @param y :
	 *            probability distribution over player 2 and expected utility of
	 *            player 1
	 * @return true, if satisfies the condition mentioned in the Mangasarian's
	 *         paper or returns false
	 * 
	 * Condition -> x(A + B)y - alpha - beta = 0
	 */
	private boolean isExtremeEquilibrium(NormalFormGame nfg, double[] x,
			double[] y) throws Exception {
		int[] profile = new int[2];
		int[] cardinality = null;
		int i, j;
		cardinality = nfg.getAllCardinalities();
		double[] temp = new double[cardinality[1]];
		double total;

		for (i = 0; i < cardinality[1]; i++) {
			profile[1] = i;
			temp[i] = 0.0;
			for (j = 0; j < cardinality[0]; j++) {
				profile[0] = j;
				temp[i] = temp[i]
						+ x[j + 1]
						* (nfg.getUtility(profile, 0) + nfg.getUtility(profile,
								1));
			}
		}

		total = 0;
		for (i = 0; i < cardinality[1]; i++) {
			total = total + temp[i] * y[i + 1];
		}

		total = total - x[0] - y[0];

		/*
		 * rounding the values i.e. if the difference is so small then that is
		 * equilibrium point
		 */
		if (Math.abs(total) <= 0.0001) {
			// System.out.println("The Total = "+total);
			return true;
		}

		return false;
	}

	/**
	 * Arrange the result into stream form which can be easily displayed by GUI
	 * module
	 * 
	 * @param equilibriaList :
	 *            extreme equilibrium points list
	 * @returns result object
	 */
	private NashResultObject getResults(ArrayList equilibriaList) {
		int iEquilibrium, jStrategy, iPlayer;
		ArrayList utilityList = null;
		double[] result = null;

		int[] cardinalities = nfg.getAllCardinalities();
		NashResultObject resultObject = new NashResultObject();
		NashEquilibrium equilibrium = null;
		double[] probabilityDistribution = null;

		resultObject.setGameName(nfg.getGameName());
		resultObject.setAlgorithmName(this.algorithmName);

		// System.out.println("Game Name : " + nfg.getGameName());
		// System.out.println("Algorithm Name : " + this.algorithmName + "\n");
		// System.out.println("No. of Extreme Nash Equilibria : " +
		// equlibriaList.size() + "\n");

		for (iEquilibrium = 0; iEquilibrium < equilibriaList.size(); iEquilibrium++) {
			// System.out.println("Nash Equlibrium ( " + (iEquilibrium + 1) + "
			// ) \n");

			equilibrium = new NashEquilibrium(nfg.getPlayers(), nfg
					.getAllCardinalities());

			utilityList = (ArrayList) equilibriaList.get(iEquilibrium);
			for (iPlayer = 0; iPlayer < utilityList.size(); iPlayer++) {

				result = (double[]) utilityList.get(iPlayer);
				// System.out.println("\tPlayer " + (iPlayer + 1));

				probabilityDistribution = new double[cardinalities[iPlayer]];
				for (jStrategy = 1; jStrategy < result.length; jStrategy++) {
					probabilityDistribution[jStrategy - 1] = result[jStrategy];
					// System.out.println("\t\tStrategy " + (jStrategy) + " -->
					// " + result[jStrategy]);
				}
				equilibrium.setProbabilityDistribution(iPlayer,
						probabilityDistribution);
				equilibrium.setExpectedUtility(iPlayer, result[0]);

				// System.out.println("\n\tPlayer " + (iPlayer + 1) + " Expected
				// Utility --> " + result[0] + "\n");
			}
			resultObject.addEqulibrium(equilibrium);
		}

		return resultObject;
	}

}
