/**
 * 
 */
package algorithms;

import games.*;
import lp_solver.*;

/**
 * @author kalyan
 * 
 */
public class TwoPersonConstantSumAlgorithm implements Algorithm {

	final private int PLAYER1 = 0;

	final private int PLAYER2 = 1;

	private NormalFormGame nfg = null;

	private String algorithmName = null;

	public TwoPersonConstantSumAlgorithm() {
		this.algorithmName = new String("Two Person Constant Sum Algorithm");
	}

	/**
	 * It computes a sample Nash equilibrium point of a two-player constant sum
	 * game by solving the linear program.
	 */
	public ResultObject computeEquilibria(NormalFormGame nfg) throws Exception {

		int nPlayers, nVariables1, iVariable, iStrategy, jStrategy, nVariables2;
		int[] cardinalities = null;
		int[] profile = null;

		double utility;
		double u = 0, v = 0;
		double[] x = null;
		double[] y = null;

		this.nfg = nfg;
		nPlayers = nfg.getPlayers();
		cardinalities = nfg.getAllCardinalities();

		if (nPlayers != 2) {
			throw new Exception("The given game is not a two person game");
		}

		profile = new int[nPlayers];

		if (!isConstantSumGame()) {
			throw new Exception("The given game is not a constant-sum game");
		}

		LPSolver lpSolver1 = null;
		LPSolver lpSolver2 = null;
		LPExpression expr1 = null;
		LPExpression expr2 = null;
		LPVariable[] var1 = null;
		LPVariable[] var2 = null;

		// for Player 1 strategies
		nVariables1 = cardinalities[PLAYER1] + 1;
		nVariables2 = cardinalities[PLAYER2] + 1;

		lpSolver1 = LPAdapter.getSolver();
		lpSolver2 = LPAdapter.getSolver();

		var1 = new LPVariable[nVariables1];
		var2 = new LPVariable[nVariables2];
		/*
		 * setting boundary conditions to the lp 1
		 */
		for (iVariable = 0; iVariable < (nVariables1 - 1); iVariable++) {
			var1[iVariable] = lpSolver1.variable(0, Double.MAX_VALUE);
		}
		var1[iVariable] = lpSolver1
				.variable(Double.MIN_VALUE, Double.MAX_VALUE);

		/*
		 * setting boundary conditions to the lp 2
		 */
		for (iVariable = 0; iVariable < (nVariables2 - 1); iVariable++) {
			var2[iVariable] = lpSolver2.variable(0, Double.MAX_VALUE);
		}
		var2[iVariable] = lpSolver2
				.variable(Double.MIN_VALUE, Double.MAX_VALUE);

		// get the constantSum value
		double constantSum = nfg.getUtility(profile, PLAYER1)
				+ nfg.getUtility(profile, PLAYER2);

		/*
		 * adding contraints to the lp 1
		 */
		for (jStrategy = 0; jStrategy < cardinalities[PLAYER2]; jStrategy++) {

			profile[PLAYER2] = jStrategy;
			expr1 = lpSolver1.linearExpression();
			for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++) {
				profile[PLAYER1] = iStrategy;
				utility = nfg.getUtility(profile, PLAYER1);
				expr1.addTerm(-utility, var1[iStrategy]);
			}
			expr1.addTerm(1, var1[nVariables1 - 1]);
			lpSolver1.addGreaterEquation(0, expr1);
		}
		/*
		 * adding probability constraint to the lp 1 model
		 */
		expr1 = lpSolver1.linearExpression();
		for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++) {
			expr1.addTerm(1, var1[iStrategy]);
		}
		lpSolver1.addEquation(1, expr1);

		/*
		 * initializing the profile
		 */
		profile[PLAYER1] = 0;
		profile[PLAYER2] = 0;

		/*
		 * adding contraints to the lp 2
		 */
		for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++) {

			profile[PLAYER1] = iStrategy;
			expr2 = lpSolver2.linearExpression();
			for (jStrategy = 0; jStrategy < cardinalities[PLAYER1]; jStrategy++) {
				profile[PLAYER2] = jStrategy;
				utility = nfg.getUtility(profile, PLAYER1);
				expr2.addTerm(-utility, var2[jStrategy]);
			}
			expr2.addTerm(1, var2[nVariables1 - 1]);
			lpSolver2.addLesserEquation(0, expr2);
		}
		/*
		 * adding probability constraint to the lp 2 model
		 */
		expr2 = lpSolver2.linearExpression();
		for (jStrategy = 0; jStrategy < cardinalities[PLAYER2]; jStrategy++) {
			expr2.addTerm(1, var2[jStrategy]);
		}
		lpSolver2.addEquation(1, expr2);

		/*
		 * adding objective function to the lp 1
		 */
		expr1 = lpSolver1.linearExpression();
		expr1.addTerm(1, var1[nVariables1 - 1]);
		lpSolver1.addMaximize(expr1);

		/*
		 * adding objective function to the lp 2
		 */
		expr2 = lpSolver2.linearExpression();
		expr2.addTerm(1, var2[nVariables2 - 1]);
		lpSolver2.addMinimize(expr2);

		// debugging purpose
		// lpSolver1.exportModel("/home/kalyan/problem1.lp");
		// lpSolver2.exportModel("/home/kalyan/problem2.lp");

		/*
		 * solving the optimization problem 1
		 */
		if (lpSolver1.solve()) {
			u = lpSolver1.getObjectiveValue();
			y = lpSolver1.getValues(var1);
/*			
			System.out.println("Objective Value : " + u);
			System.out.println(":: Result ::"); 
			for(iVariable = 0; iVariable < nVariables1; iVariable++) { 
				System.out.println("Variable " + iVariable + " : " + y[iVariable]); 
			}
*/			
		} else {
			//System.out.println("Error: " + lpSolver1.getStatus());
			throw new Exception("Can not be solved");
		}
		lpSolver1.end();

		/*
		 * solving the optimization problem 2
		 */
		if (lpSolver2.solve()) {
			v = lpSolver2.getObjectiveValue();
			x = lpSolver2.getValues(var2);
/*			
			System.out.println("Objective Value : " + v);
			System.out.println(":: Result ::"); 
			for(iVariable = 0; iVariable < nVariables2; iVariable++) { 
				System.out.println("Variable " + iVariable + " : " + x[iVariable]); 
			}
*/
		} else {
			//System.out.println("Error: " + lpSolver2.getStatus());
			throw new Exception("Can not be solved");
		}
		lpSolver2.end();

		return getResults(u, v, y, x, constantSum);
	}

	private ResultObject getResults(double u, double v, double[] x, double[] y,
			double constantSum) {
		int iStrategy, jStrategy;

		NashResultObject resultObject = new NashResultObject();
		NashEquilibrium equilibrium = new NashEquilibrium(nfg.getPlayers(), nfg
				.getAllCardinalities());
		double[] probabilityDistribution = null;

		resultObject.setAlgorithmName(this.algorithmName);
		resultObject.setGameName(nfg.getGameName());
		// System.out.println("Game Name : " + nfg.getGameName());
		// System.out.println("Algorithm Name : Two person constant-sum game
		// \n");

		probabilityDistribution = new double[x.length - 1];
		// System.out.println("\tPlayer 1 :");
		for (iStrategy = 0; iStrategy < (x.length - 1); iStrategy++) {
			probabilityDistribution[iStrategy] = x[iStrategy];
			// System.out.println("\t\tStrategy " + (iStrategy+1) + " --> " +
			// x[iStrategy]);
		}
		equilibrium
				.setProbabilityDistribution(PLAYER1, probabilityDistribution);

		probabilityDistribution = new double[y.length - 1];
		// System.out.println("\n\tPlayer 2 :");
		for (jStrategy = 0; jStrategy < (y.length - 1); jStrategy++) {
			probabilityDistribution[jStrategy] = y[jStrategy];
			// System.out.println("\t\tStrategy " + (jStrategy+1) + " --> " +
			// y[jStrategy]);
		}
		equilibrium
				.setProbabilityDistribution(PLAYER2, probabilityDistribution);
		// System.out.println("\n");

		equilibrium.setExpectedUtility(PLAYER1, u);
		equilibrium.setExpectedUtility(PLAYER2, (constantSum - u));

		// System.out.println("Player 1 expected utility : " + u);
		// System.out.println("Player 2 expected utility : " + (constantSum -
		// u));

		resultObject.addEqulibrium(equilibrium);

		return resultObject;
	}

	/**
	 * It checks that whether the given game is a constant sum game or not.
	 * 
	 * @return false if it is not a constant sum game true if it is a constant
	 *         sum game
	 */
	private boolean isConstantSumGame() {

		int iStrategy, jStrategy;
		double uUtility, vUtility;
		double sum;

		int[] cardinalities = nfg.getAllCardinalities();
		int[] profile = new int[cardinalities.length];

		sum = nfg.getUtility(profile, PLAYER1)
				+ nfg.getUtility(profile, PLAYER2);

		for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++) {
			profile[PLAYER1] = iStrategy;
			for (jStrategy = 0; jStrategy < cardinalities[PLAYER2]; jStrategy++) {
				profile[PLAYER2] = jStrategy;
				uUtility = nfg.getUtility(profile, PLAYER1);
				vUtility = nfg.getUtility(profile, PLAYER2);
				if (sum != (uUtility + vUtility)) {
					return false;
				}
			}
		}
		return true;
	}

}
