/**
 * 
 */
package algorithms;

import games.NormalFormGame;
import lp_solver.*;

/**
 * @author kalyan
 * 
 */
public class CorrelatedEquilibriumAlgorithm implements Algorithm {
	private NormalFormGame nfg = null;

	private String algorithmName = null;

	public CorrelatedEquilibriumAlgorithm() {
		this.algorithmName = new String("Correlated Equilibrium");
	}

	/**
	 * It computes the correlated equilibrium of the given normal form game. It
	 * builds the lp-problem from the given normal form given and solves the
	 * lp-problem using the lp-solver and gives the result
	 * 
	 * @param :
	 *            Normal form game
	 * @return result object which contains the details of correlated
	 *         equilibrium
	 */
	public ResultObject computeEquilibria(NormalFormGame nfg) throws Exception {

		int i;
		int nVariables; // total strategy profiles

		// var[] -> variables list of an optimization problem
		LPVariable[] var = null;
		// expr -> linear expression
		LPExpression expr = null;
		LPExpression objExpr = null;
		// cplex -> cplex object
		LPSolver lpSolver = null;
		// resultObject -> contains the computational result
		ResultObject resultObject = null;

		// contains all players strategy cardinalities
		int[] cardinalities = null;
		// is used to select a strategy profile -> (s_1,.., s_i, .. s_n)
		int[] profile = null;

		// variables corresponds to normal form game
		int nPlayers, iPlayer, iStrategy, eStrategy;

		// variables corresponds to linear equations
		double totalUtility, iUtility, eUtility;
		int iVariable;

		boolean flag;

		this.nfg = nfg;

		nPlayers = nfg.getPlayers();
		cardinalities = nfg.getAllCardinalities();

		profile = new int[nPlayers];

		nVariables = 1;
		for (i = 0; i < nPlayers; i++) {
			nVariables *= cardinalities[i];
		}

		// instantiate the lpSolver object
		lpSolver = LPAdapter.getSolver();

		var = new LPVariable[nVariables];
		for (i = 0; i < nVariables; i++) {
			var[i] = lpSolver.variable(0.0, 1.0);
		}

		// for objective function
		objExpr = lpSolver.linearExpression();

		/*
		 * for getting coefficients of variables of objective function each
		 * coefficient is sum of utilities of all players at that profile
		 */
		while (profile[nPlayers - 1] < cardinalities[nPlayers - 1]) {

			for (profile[0] = 0; profile[0] < cardinalities[0]; profile[0]++) {
				totalUtility = 0;
				for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
					totalUtility += nfg.getUtility(profile, iPlayer);
				}

				// System.out.println("Profile : [ " + profile[0] + " " +
				// profile[1] + " ]" );

				// finds offset in the variables array from the given profile
				iVariable = nfg.getOffset(profile);

				// System.out.println("iVariable : " + iVariable + " total
				// Utiliey :: " + totalUtility);

				objExpr.addTerm(totalUtility, var[iVariable]);
			}

			// for traversing all profiles
			for (iPlayer = 1; iPlayer < nPlayers; iPlayer++) {

				profile[iPlayer]++;
				if (profile[iPlayer] == cardinalities[iPlayer]
						&& iPlayer != nPlayers - 1) {
					profile[iPlayer] = 0;
					continue;
				}
				break;
			}
		}
		/*
		 * adding objective function, maximizing the utilities of all players
		 * over the while strategy profiles
		 */
		lpSolver.addMaximize(objExpr);

		/*
		 * adding strategic incentive constraints. Reference: "Game Theory" by
		 * Roger Myerson, fifth printing page No: 253, condition (6.4), chapter
		 * 6
		 */
		// iPlayer -> for every player in the game
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {

			// debugging
			// System.out.println(" iPlayer : " + iPlayer);

			// c_i (iStrategy) -> for every strategy c of i'th player in the
			// game
			for (iStrategy = 0; iStrategy < cardinalities[iPlayer]; iStrategy++) {

				// debugging
				// System.out.println(" iStrategy : " + iStrategy);

				// e_i (eStrategy) -> for every other strategy e != c of i'th
				// player in the game
				for (eStrategy = 0; eStrategy < cardinalities[iPlayer]; eStrategy++) {

					// debugging
					// System.out.println("eStrategy : " + eStrategy);

					// e_i != c_i
					if (eStrategy != iStrategy) {
						// initializing the strategy profile to get the
						// utilities from the game
						for (i = 0; i < nPlayers; i++) {
							profile[i] = 0;
						}
						expr = lpSolver.linearExpression();

						i = 0;
						// c_(-i) -> for every strategy of other players in the
						// game
						while (true) {
							// for updating the profile[] (strategy profile)
							// array
							if (i != iPlayer) {

								profile[iPlayer] = eStrategy;
								// u_i(e, c_(-i)) -> utility of i'th player when
								// i'th player
								// playing strategy e != c and others playing
								// strategies c_(-i)
								eUtility = nfg.getUtility(profile, iPlayer);

								profile[iPlayer] = iStrategy;
								// u_i(c) -> utility of i'th player when playing
								// strategy profile c
								iUtility = nfg.getUtility(profile, iPlayer);

								// debugging
								// printProfile(profile);

								expr.addTerm(iUtility - eUtility, var[nfg
										.getOffset(profile)]);

								// going to next strategy profile
								profile[i]++;
								// checking whether the corresponding strategies
								// of player i is over
								if (profile[i] < cardinalities[i]) {
									continue;
								} else {
									// checking whether it is last strategy
									// profile of c_(_i)
									if (i == nPlayers - 1
											|| (iPlayer == nPlayers - 1 && i == nPlayers - 2)) {
										// goto next constraint
										break;
									}

									// going to next strategy profile
									profile[i] = 0;
									i++;
									flag = false;
									for (; i < nPlayers; i++) {
										if (i != iPlayer) {
											profile[i]++;
											// checking whether the
											// corresponding strategies of
											// player i is over
											if (profile[i] < cardinalities[i]) {
												// going to next strategy
												// profile (c_(-i))
												break;
											}
											// checking whether it is last
											// strategy profile of c_(_i)
											else if (i == nPlayers - 1
													|| (iPlayer == nPlayers - 1 && i == nPlayers - 2)) {
												// going to next constraint
												flag = true;
												break;
											}
											profile[i] = 0;
										}
									}
									if (flag) {
										// going to next constraint
										break;
									}
									i = 0;
								}

							} else {
								// checking whether all strategy profiles are
								// over (c_(-i))
								if (i == nPlayers - 1) {
									// going to next constraint
									break;
								}
								i++;
							}
						} // end of while loop
						lpSolver.addLesserEquation(0, expr);
					}
				}
			}
		}

		// probability constraint
		expr = lpSolver.linearExpression();
		for (iVariable = 0; iVariable < nVariables; iVariable++) {
			expr.addTerm(1, var[iVariable]);
		}
		// adding probability constraint
		lpSolver.addEquation(1, expr);

		// for testing
		// lpSolver.exportModel("/home/kalyan/problem.lp");

		/*
		 * solving the optimization problem
		 */
		if (lpSolver.solve()) {

			resultObject = getResults(lpSolver, var);

			// Testing
/*
			double objValue = lpSolver.getObjValue(); 
			double[] xval = lpSolver.getValues(var); 
			double total = 0;
			
			System.out.println("Objective Value : " + objValue);
			System.out.println(":: Result ::"); 
			for(i = 0; i < nVariables; i++) { 
				System.out.println("Variable " + i + " : " + xval[i]); total += xval[i]; 
			}
			System.out.println("Total Probability : " + total);
*/	 
		} else {
			throw new Exception(lpSolver.getStatus().toString());
		}
		lpSolver.end();

		return resultObject;
	}

	/**
	 * update the resultObject with the computational results, which can be
	 * displayed to user at the application level
	 * 
	 * @param lpSolver :
	 *            lp-solver interface
	 * @param var :
	 *            variables list
	 */
	private ResultObject getResults(LPSolver lpSolver, LPVariable[] var)
			throws Exception {

		CorrelatedEquilibrium equilibrium = new CorrelatedEquilibrium();

		double[] xval = lpSolver.getValues(var);

		equilibrium.setProbabilityDistribution(xval);

		CorrelatedResultObject resultObject = new CorrelatedResultObject();

		resultObject.setAlgorithmName(this.algorithmName);
		resultObject.setNormalFormGame(this.nfg);
		resultObject.addEquilibrium(equilibrium);

		return resultObject;
	}

	// Debugging purpose
/*
 	private void printProfile(int[] profile) { 
		int i;
		
		System.out.print("Strategy Profile [ "); 
		for( i = 0; i < profile.length; i++) { 
			System.out.print(profile[i] + " "); 
		} 
		System.out.println(" ]");
		return; 
	}
*/
}
