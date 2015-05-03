/**
 * 
 */
package algorithms;

import lp_solver.*;
import java.util.*;

import games.NormalFormGame;

/**
 * @author kalyan
 * 
 * Algorithm Paper : "Simple Search Methods for finding a Nash equilibrium" by
 * B. Porter, E. Nudelman, and Y. Shoham. In Proceedings of the Nineteenth
 * Natinal Conference on Artificial Intelligence, 2004.
 */
public class SimpleSearchAlgorithm implements Algorithm {

	private int PLAYER1 = 0;

	private int PLAYER2 = 1;

	int[] supportList1;

	int[] supportList2;

	NormalFormGame nfg;

	String additionalInformation = null;

	String algorithmName = null;

	public SimpleSearchAlgorithm(String additionalInformation) {
		this.additionalInformation = additionalInformation;
		this.algorithmName = new String("Simple Search Method");
	}

	/**
	 * It computes a sample Nash equilibrium point using simple search method
	 * 
	 * @param nfg :
	 *            Normal form game
	 * @return result object of equilibrium point
	 */
	public ResultObject computeEquilibria(NormalFormGame nfg) throws Exception {

		int i, j, k;
		boolean flag, computeOne = false;

		int totalCombinations;
		int iStrategy, count = 0;
		int[] cardinalities = null;

		Support A2 = null, AA2 = null;
		Support support1 = null, support2 = null;

		ArrayList s1List = null;
		ArrayList s2List = null;
		Iterator iterator1 = null;
		Iterator iterator2 = null;

		NashResultObject resultObject = new NashResultObject();

		// System.out.println(additionalInformation);
		if (additionalInformation.equals("one")) {
			computeOne = true;
		} else if (additionalInformation.equals("all")) {
			computeOne = false;
		} else {
			throw new Exception("Invalid additional information");
		}

		this.nfg = nfg;

		int nPlayers = nfg.getPlayers();
		if (nPlayers != 2) {
			throw new Exception("The given game is not a two person game");
		}

		cardinalities = nfg.getAllCardinalities();

		resultObject.setAlgorithmName(this.algorithmName);
		resultObject.setGameName(nfg.getGameName());
		// System.out.println("\nGame Name : " + nfg.getGameName());
		// System.out.println("Algorithm Name : Simple Search Method \n");

		totalCombinations = cardinalities[PLAYER1] * cardinalities[PLAYER2];

		supportList1 = new int[totalCombinations];
		supportList2 = new int[totalCombinations];

		for (i = 0, k = 0; i < cardinalities[PLAYER1]; i++) {
			for (j = 0; j < cardinalities[PLAYER2]; j++, k++) {
				supportList1[k] = i + 1;
				supportList2[k] = j + 1;
			}
		}

		sortSupportLists(0, totalCombinations);
		// this.printSupportLists();

		A2 = new Support(cardinalities[PLAYER2]);
		AA2 = new Support(cardinalities[PLAYER2]);

		for (i = 0; i < totalCombinations; i++) {

			s1List = generateSupportList(supportList1[i],
					cardinalities[PLAYER1]);

			iterator1 = s1List.iterator();

			while (iterator1.hasNext()) {

				flag = false;
				AA2.setSize(0);
				support1 = (Support) iterator1.next();

				for (iStrategy = 0; iStrategy < cardinalities[PLAYER2]; iStrategy++) {
					if (isDominated(A2, support1, iStrategy, PLAYER2)) {
						// System.out.println("Strategy " + iStrategy +
						// " of Player 2 is dominated over player 1 support size
						// " + support1.getSize());
					} else {
						AA2.setStrategy(AA2.getSize(), iStrategy);
						AA2.increaseSize();
					}
				}

				for (j = 0; j < support1.getSize(); j++) {

					if (isDominated(support1, AA2, support1.getStrategy(j),
							PLAYER1)) {
						flag = true;
						break;
					}

				}

				if (flag) {
					// System.out.println("strategy " + support1.getStrategy(j)
					// + " of support 1 is dominated");
					flag = false;
					continue;
				} else {

					s2List = generateSupportList(supportList2[i],
							cardinalities[PLAYER2]);

					iterator2 = s2List.iterator();

					while (iterator2.hasNext()) {
						support2 = (Support) iterator2.next();

						if (!isSubset(support2, AA2)) {
							// System.out.println("support2 is not a subset of
							// support AA2");
							continue;
						} else {

							for (j = 0; j < support1.getSize(); j++) {
								if (isDominated(support1, support2, support1
										.getStrategy(j), PLAYER1)) {
									flag = true;
									break;
								}
							}
							if (flag) {
								// System.out.println("strategy " +
								// support1.getStrategy(j) + " of support 1 is
								// dominated");
								flag = false;
								continue;
							} else {

								boolean isFeasible = checkFeasibility(support1,
										support2, resultObject);
								if (isFeasible) {
									// System.out.println("Feasible set found");
									if (computeOne) {
										return resultObject;
									} else {
										count++;
										// System.out.println("\n Nash
										// Equlibrium ( " + (count) + " ) \n");
									}
								}
							}
						} // end of else
					} // end of inner while loop
				} // end of outer else
			} // end of outer while loop
		}// end of for loop

		if (count == 0) {
			throw new Exception("Can not be solved due to negative payoffs");
		}

		return resultObject;
	}

	/**
	 * It checks the feasibility for the given supports specified in the paper
	 * 
	 * @param support1 :
	 *            player 1 support
	 * @param support2 :
	 *            player 2 support
	 * @param resultObject :
	 *            result obejct
	 * @return true if it is feasible or false
	 * @throws Exception
	 */
	private boolean checkFeasibility(Support support1, Support support2,
			NashResultObject resultObject) throws Exception {
		int i;

		int nPlayers, iPlayer, nVariables, iVariable, iStrategy, jStrategy;
		double utility;

		int[] cardinalities = null;
		int[] profile = null;

		LPSolver lpSolver = null;
		LPVariable[] var = null;
		LPExpression expr = null;
		LPExpression probExpr = null;

		nPlayers = nfg.getPlayers();
		cardinalities = nfg.getAllCardinalities();
		profile = new int[nPlayers];

		nVariables = nPlayers;
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			nVariables += cardinalities[iPlayer];
		}

		lpSolver = LPAdapter.getSolver();
		var = new LPVariable[nVariables];

		/*
		 * Setting upper-bound and lower-bound to the variables
		 */
		// first (nVariables-nPlayers) -> probability distributions over the
		// strategies of players
		for (iVariable = 0; iVariable < (nVariables - nPlayers); iVariable++) {
			var[iVariable] = lpSolver.variable(0, Double.MAX_VALUE);
		}
		// last two variables represents expected payoff of player 1 and player
		// 2
		for (; iVariable < nVariables; iVariable++) {
			var[iVariable] = lpSolver.variable(Double.MIN_VALUE,
					Double.MAX_VALUE);
		}

		for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++) {

			profile[PLAYER1] = iStrategy;
			expr = lpSolver.linearExpression();
			probExpr = lpSolver.linearExpression();

			probExpr.addTerm(1, var[iStrategy]);

			// constraint for expected utility of player 1
			iVariable = cardinalities[PLAYER1];
			for (jStrategy = 0; jStrategy < cardinalities[PLAYER2]; jStrategy++, iVariable++) {
				profile[PLAYER2] = jStrategy;
				utility = nfg.getUtility(profile, PLAYER1);
				expr.addTerm(utility, var[iVariable]);
			}
			expr.addTerm(-1, var[nVariables - 2]);

			if (support1.isInSupport(iStrategy)) {
				// constraints where i'th strategy of player 1 is in support
				lpSolver.addEquation(0, expr);
				// constraint for probability on strategy i
				lpSolver.addLesserEquation(0, probExpr);
			} else {
				lpSolver.addGreaterEquation(0, expr);
				lpSolver.addEquation(0, probExpr);
			}

		}

		for (iStrategy = 0; iStrategy < cardinalities[PLAYER2]; iStrategy++) {

			profile[PLAYER2] = iStrategy;
			expr = lpSolver.linearExpression();
			probExpr = lpSolver.linearExpression();
			probExpr.addTerm(1, var[iStrategy + cardinalities[PLAYER1]]);

			iVariable = 0;
			for (jStrategy = 0; jStrategy < cardinalities[PLAYER1]; jStrategy++, iVariable++) {
				profile[PLAYER1] = jStrategy;
				utility = nfg.getUtility(profile, PLAYER2);
				expr.addTerm(utility, var[iVariable]);
			}

			expr.addTerm(-1, var[nVariables - 1]);
			if (support2.isInSupport(iStrategy)) {
				lpSolver.addEquation(0, expr);
				lpSolver.addLesserEquation(0, probExpr);
			} else {
				lpSolver.addGreaterEquation(0, expr);
				lpSolver.addEquation(0, probExpr);
			}
		}

		expr = lpSolver.linearExpression();
		for (iVariable = 0; iVariable < cardinalities[PLAYER1]; iVariable++) {
			expr.addTerm(1, var[iVariable]);
		}
		lpSolver.addEquation(1, expr);

		expr = lpSolver.linearExpression();
		for (i = 0; i < cardinalities[PLAYER2]; i++, iVariable++) {
			expr.addTerm(1, var[iVariable]);
		}
		lpSolver.addEquation(1, expr);

		expr = lpSolver.linearExpression();
		expr.addTerm(1, var[nVariables - 2]);
		expr.addTerm(1, var[nVariables - 1]);
		lpSolver.addMaximize(expr);

		// debugging purpose
		// lpSolver.exportModel("/home/kalyan/problem.lp");

		/*
		 * solving the optimization problem
		 */
		if (lpSolver.solve()) {

			double objValue = lpSolver.getObjectiveValue();
			double[] xval = lpSolver.getValues(var);

			for (iVariable = 0; iVariable < cardinalities[PLAYER1]; iVariable++) {
				if (support1.isInSupport(iVariable)) {
					if (roundValue(xval[iVariable]) == 0) {
						return false;
					}
				}
			}

			for (jStrategy = 0; jStrategy < cardinalities[PLAYER2]; iVariable++, jStrategy++) {
				if (support2.isInSupport(iVariable - cardinalities[PLAYER1])) {
					if (roundValue(xval[iVariable]) == 0) {
						return false;
					}
				}
			}

			lpSolver.end();
			return getResults(xval, objValue, resultObject);

		} else {
			// System.out.println("Error: " + lpSolver.getStatus());
		}
		lpSolver.end();
		return false;
	}

	/**
	 * It checks the i'th support is subset of j'th support
	 * 
	 * @param iSupport :
	 *            i'th support
	 * @param jSupport :
	 *            j'th support
	 * @return true if subset else false
	 */
	private boolean isSubset(Support iSupport, Support jSupport) {
		int size;
		boolean result;
		int i, j;

		size = 0;
		result = false;
		if (iSupport.getSize() <= jSupport.getSize()) {
			for (i = 0; i < iSupport.getSize(); i++) {
				for (j = 0; j < jSupport.getSize(); j++) {
					if (iSupport.getStrategy(i) == jSupport.getStrategy(j)) {
						size++;
						break;
					}
				}
			}
			if (size == iSupport.getSize())
				result = true;
		}
		return result;
	}

	/**
	 * It checks conditional dominance over the specified supports
	 * 
	 * @param iSupport :
	 *            i'th support of player i
	 * @param jSupport :
	 *            j'th support of player j
	 * @param iStrategy :
	 *            i'th strategy of i'th player
	 * @param iPlayer :
	 *            i'th player
	 * @return true if i'th strategy is dominated or false
	 */
	private boolean isDominated(Support iSupport, Support jSupport,
			int iStrategy, int iPlayer) {
		int i, j;
		int nPlayers = nfg.getPlayers();
		int[] profile = new int[nPlayers];
		boolean dominated = false;
		double utility1, utility2;

		if (iPlayer == PLAYER1) {
			for (i = 0; i < iSupport.getSize(); i++) {
				if (iSupport.getStrategy(i) != iStrategy) {
					dominated = true;
					for (j = 0; j < jSupport.getSize(); j++) {
						profile[PLAYER1] = iSupport.getStrategy(i);
						profile[PLAYER2] = jSupport.getStrategy(j);
						utility1 = nfg.getUtility(profile, iPlayer);

						profile[PLAYER1] = iStrategy;
						utility2 = nfg.getUtility(profile, iPlayer);

						if (utility1 <= utility2) {
							return false;
						}

					}
				}
			}

		} else {
			for (j = 0; j < iSupport.getSize(); j++) {
				if (iSupport.getStrategy(j) != iStrategy) {
					dominated = true;
					for (i = 0; i < jSupport.getSize(); i++) {
						profile[PLAYER1] = jSupport.getStrategy(i);
						profile[PLAYER2] = iSupport.getStrategy(j);
						utility1 = nfg.getUtility(profile, iPlayer);

						profile[PLAYER2] = iStrategy;
						utility2 = nfg.getUtility(profile, iPlayer);

						if (utility1 <= utility2) {
							return false;
						}

					}
				}
			}
		}
		return dominated;
	}

	/**
	 * It generates the support lists
	 * 
	 * @param supportSize :
	 *            size of support
	 * @param nStrategies :
	 *            no.of strategies
	 * @return list of supports of specific size
	 */
	private ArrayList generateSupportList(int supportSize, int nStrategies) {

		int i, no;
		int[] strategies = null; // acts as stack array
		int top = -1; // top of the stack

		Support support = null;
		ArrayList list = null;
		strategies = new int[supportSize];

		// generate first support of size
		for (i = 0; i < supportSize; i++) {
			strategies[++top] = i;
		}

		list = new ArrayList();

		support = new Support(supportSize, strategies);
		list.add(support);

		while (true) {
			// iterate until stack empty
			if (top == -1)
				break;

			// popping
			no = strategies[top--];

			if (no < nStrategies) {
				while ((top < supportSize - 1) && (no < nStrategies - 1))
					// pushing
					strategies[++top] = ++no;
			} else
				top--;

			if (top == supportSize - 1) {
				support = new Support(supportSize, strategies);
				list.add(support);

			}
		}

		return list;
	}

	/**
	 * sort the all support lists using quick sort
	 * 
	 * @param beg :
	 *            start position
	 * @param end :
	 *            end position
	 */
	private void sortSupportLists(int beg, int end) {
		int[] pivot = new int[2];
		int temp;
		int left, right;

		if (end > beg + 1) {
			pivot[0] = supportList1[beg];
			pivot[1] = supportList2[beg];

			left = beg + 1;
			right = end;

			while (left < right) {

				if (Math.abs(supportList1[left] - supportList2[left]) < Math
						.abs(pivot[0] - pivot[1])) {
					left++;
				} else if ((Math.abs(supportList1[left] - supportList2[left]) == Math
						.abs(pivot[0] - pivot[1]))
						&& (supportList1[left] + supportList2[left]) <= (pivot[0] + pivot[1])) {
					left++;
				} else {
					right--;

					// swapping
					temp = supportList1[left];
					supportList1[left] = supportList1[right];
					supportList1[right] = temp;

					temp = supportList2[left];
					supportList2[left] = supportList2[right];
					supportList2[right] = temp;
				}
			}
			left--;

			// swapping
			temp = supportList1[left];
			supportList1[left] = supportList1[beg];
			supportList1[beg] = temp;

			temp = supportList2[left];
			supportList2[left] = supportList2[beg];
			supportList2[beg] = temp;

			sortSupportLists(beg, left);
			sortSupportLists(right, end);

		}
	}

	/**
	 * constructs the result object from the results produced by the solver
	 * 
	 * @param xval :
	 *            variables values
	 * @param objValue :
	 *            objective value
	 * @param resultObject :
	 *            result object
	 * @return result object of a sample Nash equilibrium
	 */
	private boolean getResults(double[] xval, double objValue,
			NashResultObject resultObject) {
		int iStrategy, iVariable;

		int[] cardinalities = nfg.getAllCardinalities();

		NashEquilibrium equilibrium = new NashEquilibrium(nfg.getPlayers(),
				cardinalities);
		double[] probabilityDistribution = null;

		iVariable = 0;

		// System.out.println("\tPlayer 1 : ");
		probabilityDistribution = new double[cardinalities[PLAYER1]];
		for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++, iVariable++) {
			probabilityDistribution[iStrategy] = xval[iVariable];
			// System.out.println("\t\tStrategy " + (iStrategy+1) + " --> " +
			// xval[iVariable]);
		}
		equilibrium
				.setProbabilityDistribution(PLAYER1, probabilityDistribution);

		// System.out.println("\n\tPlayer 2 : ");
		probabilityDistribution = new double[cardinalities[PLAYER2]];
		for (iStrategy = 0; iStrategy < cardinalities[PLAYER2]; iStrategy++, iVariable++) {
			probabilityDistribution[iStrategy] = xval[iVariable];
			// System.out.println("\t\tStrategy " + (iStrategy+1) + " --> " +
			// xval[iVariable]);
		}
		equilibrium
				.setProbabilityDistribution(PLAYER2, probabilityDistribution);
		// System.out.println("\n");

		equilibrium.setExpectedUtility(PLAYER1, xval[iVariable]);
		// System.out.println("\tPlayer 1 Expected Utility : " +
		// xval[iVariable]);
		iVariable++;
		equilibrium.setExpectedUtility(PLAYER2, xval[iVariable]);
		// System.out.println("\tPlayer 2 Expected Utility : " +
		// xval[iVariable]);

		resultObject.addEqulibrium(equilibrium);
		// System.out.println("\n\tObjective Value : " + roundValue(objValue) +
		// "\n");

		return true;
	}

	/**
	 * Gives the Rounded value of a given double value
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

	/**
	 * Note: Debug purpose
	 */
/*	
	private void printSupportLists() { 
		int i=0;
		System.out.println("Support List of Players"); 
		for(i = 0 ; i < supportList1.length; i++) { 
			System.out.println("S1 -> " + supportList1[i] + "S2 -> " + supportList2[i]); 
		} 
		return;
	}
*/
}
