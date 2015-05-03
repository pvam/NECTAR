/**
 * 
 */
package algorithms;

import games.NormalFormGame;
import lp_solver.*;

/**
 * @author kalyan
 * 
 * Algorithm Paper : "Mixed-integer Programming Methods for finding Nash
 * equilibria" by T. Sandholm, A. Gilpin, and V. Conitzer. In Proceedings of the
 * Twentieth National Conference on Artificial Intelligence, pages 495-501,
 * 2005.
 */
public class MIPAlgorithm implements Algorithm {

	// constants;
	final int PLAYER1 = 0;

	final int PLAYER2 = 1;

	private NormalFormGame nfg = null;

	private String additionalInformation = null;

	private String algorithmName = null;

	public MIPAlgorithm(String additionalInformation) {
		this.additionalInformation = additionalInformation;
		this.algorithmName = new String("Mixed Integer Programming Method");
	}

	/**
	 * It computes a sample Nash equilibrium point of the given normal form game
	 * (two player) and returns result object
	 * 
	 * @param nfg :
	 *            Normal form Game
	 * @return result object of Nash equilibrium point
	 */
	public ResultObject computeEquilibria(NormalFormGame nfg) throws Exception {
		this.nfg = nfg;

		ResultObject resultObject = null;

		if (additionalInformation.equals("formulation 1")) {
			
			resultObject = this.formulationOne();
		} else if (additionalInformation.equals("formulation 2")) {
			resultObject = this.formulationTwo();
		} else if (additionalInformation.equals("formulation 3")) {
			resultObject = this.formulationThree();
		} else if (additionalInformation.equals("formulation 4")) {
			resultObject = this.formulationFour();
		} else {
			throw new Exception("Invalid Formulation");
		}

		return resultObject;
	}

	/**
	 * It computes a sample Nash equilibrium point using formulation 1 specified
	 * in the paper
	 * 
	 * @return result object of Nash equilibrium point
	 * @throws Exception
	 */
	private ResultObject formulationOne() throws Exception {
		int i, j, k, l;
		int totalActions, nPlayers, iPlayer, nVariables;
		int[] cardinalities = null, profile = null;
		double[] umax;

		ResultObject resultObject = null;

		// var[] -> variables list of an optimization problem
		LPVariable[] var = null;
		// expr -> linear expression
		LPExpression expr = null;

		cardinalities = nfg.getAllCardinalities();
		nPlayers = nfg.getPlayers();

		if (nPlayers != 2) {
			throw new Exception("The given game is not a two person game");
		}
		/*else if(nPlayers == 2)
			throw new Exception("The given g person game");*/

		profile = new int[nPlayers];
		umax = new double[nPlayers];

		umax[PLAYER1] = find_max_diff(nfg, PLAYER1);
		umax[PLAYER2] = find_max_diff(nfg, PLAYER2);

		totalActions = 0;
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			totalActions += nfg.getCardinality(iPlayer);
		}

		nVariables = 4 * totalActions + 2;

		LPSolver lpSolver = null;

		// instantiate the lpSolve object
		lpSolver = LPAdapter.getSolver();

		/*
		 * setting lower and upper bounds to all variables
		 */
		var = new LPVariable[nVariables];

		for (i = 0; i < nVariables - totalActions; i++) {
			var[i] = lpSolver.variable(0.0, Double.MAX_VALUE);
		}
		for (; i < nVariables; i++) {
			var[i] = lpSolver.booleanVariable();
		}

		/*
		 * modelling the optimization problem
		 */
		// constraint 1.a (player 1)
		expr = lpSolver.linearExpression();
		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			// var[i] -> p_s_1 (probability of strategy s of player 1)
			expr.addTerm(1, var[i]);
		}
		lpSolver.addEquation(1, expr);

		// constraint 1.b (player 2)
		expr = lpSolver.linearExpression();
		for (j = 0; j < cardinalities[PLAYER2]; i++, j++) {
			// var[i] -> p_s_2 (probability of strategy s of player 2)
			expr.addTerm(1, var[i]);
		}
		lpSolver.addEquation(1, expr);

		// constraints 2.a (player 1)
		for (profile[PLAYER1] = 0; profile[PLAYER1] < cardinalities[PLAYER1]; profile[PLAYER1]++) {
			profile[PLAYER2] = 0;
			expr = lpSolver.linearExpression();
			for (j = 0, k = cardinalities[PLAYER1]; j < cardinalities[PLAYER2]; j++, k++) {
				// var[k] -> p_s_2 (probability of strategy s of player 2)
				expr.addTerm(nfg.getUtility(profile, PLAYER1), var[k]);
				profile[PLAYER2]++;
			}
			// var[totalActions+profile[PLAYER1] -> u_s_1
			// (expected utility of player 1)
			expr.addTerm(-1, var[totalActions + profile[PLAYER1]]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addEquation(0, expr);
		}

		// constraints 2.b (player 2)
		for (profile[PLAYER2] = 0; profile[PLAYER2] < cardinalities[PLAYER2]; profile[PLAYER2]++) {
			profile[PLAYER1] = 0;
			expr = lpSolver.linearExpression();
			for (j = 0, k = 0; j < cardinalities[PLAYER1]; j++, k++) {
				// var[k] -> p_s_1 (probability of strategy s of player 1)
				expr.addTerm(nfg.getUtility(profile, PLAYER2), var[k]);
				profile[PLAYER1]++;
			}
			// var[totalActions +cardinalities[PLAYER1] +profile[PLAYER2]] ->
			// u_s_2
			// (expected utilility of player 2)
			expr.addTerm(-1, var[totalActions + cardinalities[PLAYER1]
					+ profile[PLAYER2]]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 3.a (player 1)
		j = 2 * totalActions;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(-1, var[j]);
			lpSolver.addGreaterEquation(0, expr);
		}
		j++;

		// constraints 3.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_2 (expected utility of playing strategy s of player
			// 2)
			expr.addTerm(1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(-1, var[j]);
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 4.a (player 1)
		j--;
		l = 2 * totalActions + 2;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++, l++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_1 ( regret of playing strategy s of player 1)
			expr.addTerm(-1, var[l]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 4.b (player 2)
		j++;
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++, l++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_2 (expected utility of player 2 playing strategy s)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_2 (regret of player 2 playing strategy s)
			expr.addTerm(-1, var[l]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 5 (for player 1 and player 2)
		j = 0;
		k = 3 * totalActions + 2;
		for (l = 0; l < totalActions; l++, j++, k++) {
			expr = lpSolver.linearExpression();
			// var[j] -> p_s_i (probability of player i playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> b_s_i (binary variable of player i playing strategy s)
			expr.addTerm(1, var[k]);
			lpSolver.addGreaterEquation(1, expr);
		}

		// constraints 6.a (player 1)
		j = 2 * totalActions + 2;
		k = 3 * totalActions + 2;
		for (i = 0; i < cardinalities[PLAYER1]; i++, j++, k++) {
			expr = lpSolver.linearExpression();
			// var[j] -> r_s_1 (regret of player 2 playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> b_s_1 (binary variable of player 2 playing strategy s)
			expr.addTerm(-umax[PLAYER1], var[k]);
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 6.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, j++, k++) {

			expr = lpSolver.linearExpression();
			// var[j] -> r_s_2 (regret of player 2 playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> b_s_2 (binary variable of player 2 playing strategy s)
			expr.addTerm(-umax[PLAYER2], var[k]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addGreaterEquation(0, expr);
		}

		// adding objective function improves the speed
		// here objective functions is maximizing the welfare function.
		expr = lpSolver.linearExpression();
		expr.addTerm(1, var[2 * totalActions]);
		expr.addTerm(1, var[2 * totalActions + 1]);
		lpSolver.addMaximize(expr);

		// lpSolve.exportModel("/home/kalyan/problem.lp");

		/*
		 * solving the optimization problem
		 */

		if (lpSolver.solve()) {

			double objValue = lpSolver.getObjectiveValue();
			double[] xval = lpSolver.getValues(var);

			resultObject = getResults(xval, objValue);
		} else {
			// System.out.println("Error: " + lpSolver.getStatus());
			throw new Exception("Can not be solved due to negative payoffs");
		}
		lpSolver.end();

		return resultObject;
	}

	/**
	 * It computes a sample Nash equilibrium point using formulation 2 specified
	 * in the paper
	 * 
	 * @return result object of Nash equilibrium point
	 * @throws Exception
	 */
	private ResultObject formulationTwo() throws Exception {
		int i, j, k, l;
		int totalActions, nPlayers, iPlayer, nVariables;
		int[] cardinalities = null, profile = null;
		double[] umax;

		ResultObject resultObject = null;

		// var[] -> variables list of an optimization problem
		LPVariable[] var = null;
		// expr -> linear expression
		LPExpression expr = null;

		cardinalities = nfg.getAllCardinalities();
		nPlayers = nfg.getPlayers();

		profile = new int[nPlayers];
		umax = new double[nPlayers];

		umax[PLAYER1] = find_max_diff(nfg, PLAYER1);
		umax[PLAYER2] = find_max_diff(nfg, PLAYER2);

		totalActions = 0;
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			totalActions += nfg.getCardinality(iPlayer);
		}

		nVariables = 5 * totalActions + 2;

		// instantiate the lpSolve object
		LPSolver lpSolver = LPAdapter.getSolver();

		/*
		 * setting lower and upper bounds to all variables
		 */
		var = new LPVariable[nVariables];

		for (i = 0; i < nVariables - (2 * totalActions); i++) {
			var[i] = lpSolver.variable(0.0, Double.MAX_VALUE);
		}
		for (; i < (nVariables - totalActions); i++) {
			var[i] = lpSolver.booleanVariable();
		}
		for (; i < nVariables; i++) {
			var[i] = lpSolver.variable(0.0, Double.MAX_VALUE);
		}

		/*
		 * modelling the optimization problem
		 */
		// constraint 1.a (player 1)
		expr = lpSolver.linearExpression();
		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			// var[i] -> p_s_1 (probability of strategy s of player 1)
			expr.addTerm(1, var[i]);
		}
		lpSolver.addEquation(1, expr);

		// constraint 1.b (player 2)
		expr = lpSolver.linearExpression();
		for (j = 0; j < cardinalities[PLAYER2]; i++, j++) {
			// var[i] -> p_s_2 (probability of strategy s of player 2)
			expr.addTerm(1, var[i]);
		}
		lpSolver.addEquation(1, expr);

		// constraints 2.a (player 1)
		for (profile[PLAYER1] = 0; profile[PLAYER1] < cardinalities[PLAYER1]; profile[PLAYER1]++) {
			profile[PLAYER2] = 0;
			expr = lpSolver.linearExpression();
			for (j = 0, k = cardinalities[PLAYER1]; j < cardinalities[PLAYER2]; j++, k++) {
				// var[k] -> p_s_2 (probability of strategy s of player 2)
				expr.addTerm(nfg.getUtility(profile, PLAYER1), var[k]);
				profile[PLAYER2]++;
			}
			// var[totalActions+profile[PLAYER1] -> u_s_1
			// (expected utility of player 1)
			expr.addTerm(-1, var[totalActions + profile[PLAYER1]]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addEquation(0, expr);
		}

		// constraints 2.b (player 2)
		for (profile[PLAYER2] = 0; profile[PLAYER2] < cardinalities[PLAYER2]; profile[PLAYER2]++) {
			profile[PLAYER1] = 0;
			expr = lpSolver.linearExpression();
			for (j = 0, k = 0; j < cardinalities[PLAYER1]; j++, k++) {
				// var[k] -> p_s_1 (probability of strategy s of player 1)
				expr.addTerm(nfg.getUtility(profile, PLAYER2), var[k]);
				profile[PLAYER1]++;
			}
			// var[totalActions +cardinalities[PLAYER1] +profile[PLAYER2]] ->
			// u_s_2
			// (expected utilility of player 2)
			expr.addTerm(-1, var[totalActions + cardinalities[PLAYER1]
					+ profile[PLAYER2]]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 3.a (player 1)
		j = 2 * totalActions;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(-1, var[j]);
			lpSolver.addGreaterEquation(0, expr);
		}
		j++;

		// constraints 3.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_2 (expected utility of playing strategy s of player
			// 2)
			expr.addTerm(1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(-1, var[j]);
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 4.a (player 1)
		j--;
		l = 2 * totalActions + 2;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++, l++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_1 ( regret of playing strategy s of player 1)
			expr.addTerm(-1, var[l]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 4.b (player 2)
		j++;
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++, l++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_2 (expected utility of player 2 playing strategy s)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_2 (regret of player 2 playing strategy s)
			expr.addTerm(-1, var[l]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 5 (for player 1 and player 2)
		j = 0;
		k = 3 * totalActions + 2;
		for (l = 0; l < totalActions; l++, j++, k++) {
			expr = lpSolver.linearExpression();
			// var[j] -> p_s_i (probability of player i playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> b_s_i (binary variable of player i playing strategy s)
			expr.addTerm(1, var[k]);
			lpSolver.addGreaterEquation(1, expr);
		}

		// constraints 7 (player 1 & player 2)
		j = 2 * totalActions + 2;
		k = 4 * totalActions + 2;
		for (i = 0; i < totalActions; i++, j++, k++) {
			expr = lpSolver.linearExpression();
			// var[j] -> r_s_i (regret of player i playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> f_s_i (penalize regret of player i playiing strategy s)
			expr.addTerm(-1, var[k]);
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 8.a (player 1)
		j = 3 * totalActions + 2;
		k = 4 * totalActions + 2;
		for (i = 0; i < cardinalities[PLAYER1]; i++, j++, k++) {

			expr = lpSolver.linearExpression();
			// var[j] -> b_s_1 (binary variable of player 1 playing strategy s)
			expr.addTerm(umax[PLAYER1], var[j]);
			// var[k] -> f_s_1 (penalize regret of player 1 playing strategy s)
			expr.addTerm(-1, var[k]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 8.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, j++, k++) {

			expr = lpSolver.linearExpression();
			// var[j] -> b_s_2 (binary variable of player 2 playing strategy s)
			expr.addTerm(umax[PLAYER2], var[j]);
			// var[k] -> b_s_2 (penalize regret of player 2 playing strategy s)
			expr.addTerm(-1, var[k]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addGreaterEquation(0, expr);
		}

		// adding objective function of formulation 2
		expr = lpSolver.linearExpression();

		j = 3 * totalActions + 2;
		k = 4 * totalActions + 2;
		for (i = 0; i < cardinalities[PLAYER1]; i++, j++, k++) {
			expr.addTerm(1, var[k]);
			expr.addTerm(-umax[PLAYER1], var[j]);
		}
		for (i = 0; i < cardinalities[PLAYER2]; i++, j++, k++) {
			expr.addTerm(1, var[k]);
			expr.addTerm(-umax[PLAYER2], var[j]);
		}
		lpSolver.addMinimize(expr);

		// lpSolver.exportModel("/home/kalyan/problem.lp");

		/*
		 * solving the optimization problem
		 */
		if (lpSolver.solve()) {

			double objValue = lpSolver.getObjectiveValue();
			double[] xval = lpSolver.getValues(var);

			resultObject = this.getResults(xval, objValue);

		} else {
			// System.out.println("Error: " + lpSolver.getStatus());
			throw new Exception("Can not be solved due to negative payoffs");
		}
		lpSolver.end();

		return resultObject;
	}

	/**
	 * It computes a sample Nash equilibrium point using formulation 3 specified
	 * in the paper
	 * 
	 * @return result object of Nash equilibrium point
	 * @throws Exception
	 */
	private ResultObject formulationThree() throws Exception {
		int i, j, k, l;
		int totalActions, nPlayers, iPlayer, nVariables;
		int[] cardinalities = null, profile = null;
		double[] umax;

		ResultObject resultObject = null;

		// var[] -> variables list of an optimization problem
		LPVariable[] var = null;
		// expr -> linear expression
		LPExpression expr = null;

		cardinalities = nfg.getAllCardinalities();
		nPlayers = nfg.getPlayers();

		profile = new int[nPlayers];
		umax = new double[nPlayers];

		umax[PLAYER1] = find_max_diff(nfg, PLAYER1);
		umax[PLAYER2] = find_max_diff(nfg, PLAYER2);

		totalActions = 0;
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			totalActions += nfg.getCardinality(iPlayer);
		}

		nVariables = 5 * totalActions + 2;

		// instantiate the lpSolver object
		LPSolver lpSolver = LPAdapter.getSolver();

		/*
		 * setting lower and upper bounds to all variables
		 */
		var = new LPVariable[nVariables];

		for (i = 0; i < nVariables - (2 * totalActions); i++) {
			var[i] = lpSolver.variable(0.0, Double.MAX_VALUE);
		}
		for (; i < (nVariables - totalActions); i++) {
			var[i] = lpSolver.booleanVariable();
		}
		for (; i < nVariables; i++) {
			var[i] = lpSolver.variable(0.0, Double.MAX_VALUE);
		}

		/*
		 * modelling the optimization problem
		 */
		// constraint 1.a (player 1)
		expr = lpSolver.linearExpression();
		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			// var[i] -> p_s_1 (probability of strategy s of player 1)
			expr.addTerm(1, var[i]);
		}
		lpSolver.addEquation(1, expr);

		// constraint 1.b (player 2)
		expr = lpSolver.linearExpression();
		for (j = 0; j < cardinalities[PLAYER2]; i++, j++) {
			// var[i] -> p_s_2 (probability of strategy s of player 2)
			expr.addTerm(1, var[i]);
		}
		lpSolver.addEquation(1, expr);

		// constraints 2.a (player 1)
		for (profile[PLAYER1] = 0; profile[PLAYER1] < cardinalities[PLAYER1]; profile[PLAYER1]++) {
			profile[PLAYER2] = 0;
			expr = lpSolver.linearExpression();
			for (j = 0, k = cardinalities[PLAYER1]; j < cardinalities[PLAYER2]; j++, k++) {
				// var[k] -> p_s_2 (probability of strategy s of player 2)
				expr.addTerm(nfg.getUtility(profile, PLAYER1), var[k]);
				profile[PLAYER2]++;
			}
			// var[totalActions+profile[PLAYER1] -> u_s_1
			// (expected utility of player 1)
			expr.addTerm(-1, var[totalActions + profile[PLAYER1]]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addEquation(0, expr);
		}

		// constraints 2.b (player 2)
		for (profile[PLAYER2] = 0; profile[PLAYER2] < cardinalities[PLAYER2]; profile[PLAYER2]++) {
			profile[PLAYER1] = 0;
			expr = lpSolver.linearExpression();
			for (j = 0, k = 0; j < cardinalities[PLAYER1]; j++, k++) {
				// var[k] -> p_s_1 (probability of strategy s of player 1)
				expr.addTerm(nfg.getUtility(profile, PLAYER2), var[k]);
				profile[PLAYER1]++;
			}
			// var[totalActions +cardinalities[PLAYER1] +profile[PLAYER2]] ->
			// u_s_2
			// (expected utilility of player 2)
			expr.addTerm(-1, var[totalActions + cardinalities[PLAYER1]
					+ profile[PLAYER2]]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 3.a (player 1)
		j = 2 * totalActions;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(-1, var[j]);
			lpSolver.addGreaterEquation(0, expr);
		}
		j++;

		// constraints 3.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_2 (expected utility of playing strategy s of player
			// 2)
			expr.addTerm(1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(-1, var[j]);
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 4.a (player 1)
		j--;
		l = 2 * totalActions + 2;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++, l++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_1 ( regret of playing strategy s of player 1)
			expr.addTerm(-1, var[l]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 4.b (player 2)
		j++;
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++, l++) {
			expr = lpSolver.linearExpression();
			// var[k] -> u_s_2 (expected utility of player 2 playing strategy s)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_2 (regret of player 2 playing strategy s)
			expr.addTerm(-1, var[l]);
			lpSolver.addEquation(0, expr);
		}

		// constraints 6.a (player 1)
		j = 2 * totalActions + 2;
		k = 3 * totalActions + 2;
		for (i = 0; i < cardinalities[PLAYER1]; i++, j++, k++) {
			expr = lpSolver.linearExpression();
			// var[j] -> r_s_1 (regret of player 2 playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> b_s_1 (binary variable of player 2 playing strategy s)
			expr.addTerm(-umax[PLAYER1], var[k]);
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 6.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, j++, k++) {

			expr = lpSolver.linearExpression();
			// var[j] -> r_s_2 (regret of player 2 playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> b_s_2 (binary variable of player 2 playing strategy s)
			expr.addTerm(-umax[PLAYER2], var[k]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 9 (player 1 & player 2)
		k = 4 * totalActions + 2;
		for (i = 0; i < totalActions; i++, k++) {
			expr = lpSolver.linearExpression();
			// var[j] -> p_s_i (probability of player i playing strategy s)
			expr.addTerm(1, var[i]);
			// var[k] -> g_s_i (penalize probability of player i playiing
			// strategy s)
			expr.addTerm(-1, var[k]);
			lpSolver.addGreaterEquation(0, expr);
		}

		// constraints 10 (player 1 and player 2)
		j = 3 * totalActions + 2;
		k = 4 * totalActions + 2;
		for (i = 0; i < totalActions; i++, j++, k++) {
			expr = lpSolver.linearExpression();
			// var[j] -> b_s_i (binary variable of player i playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> g_s_1 (penalize probability of player i playing
			// strategy s)
			expr.addTerm(1, var[k]);
			// System.out.println("expr : " + expr.toString());
			lpSolver.addGreaterEquation(1, expr);
		}

/*
		for( i = 0; i < nVariables; i++) { 
			System.out.println("variable " + i + " : " +  var[i].getType()); 
		}
*/

		/*
		 * adding objective function of formulation 3
		 */
		expr = lpSolver.linearExpression();

		j = 3 * totalActions + 2;
		k = 4 * totalActions + 2;
		for (i = 0; i < totalActions; i++, j++, k++) {
			expr.addTerm(1, var[k]);
			expr.addTerm(1, var[j]);
			// expr.addTerm()
		}
		lpSolver.addMinimize(expr);

		// lpSolver.exportModel("/home/kalyan/problem.lp");

		/*
		 * solving the optimization problem
		 */
		if (lpSolver.solve()) {

			double objValue = lpSolver.getObjectiveValue();
			double[] xval = lpSolver.getValues(var);

			resultObject = this.getResults(xval, objValue);
		} else {
			// System.out.println("Error: " + lpSolver.getStatus());
			throw new Exception("Can not be solved due to negative payoffs");
		}
		lpSolver.end();
		return resultObject;
	}

	/**
	 * It computes a sample Nash equilibrium point using formulation 4 specified
	 * in the paper
	 * 
	 * @return result object of Nash equilibrium point
	 * @throws Exception
	 */
	private ResultObject formulationFour() throws Exception {
		int i, j, k, l;
		int totalActions, nPlayers, iPlayer, nVariables;
		int[] cardinalities = null, profile = null;
		double[] umax;

		ResultObject resultObject = null;

		// var[] -> variables list of an optimization problem
		LPVariable[] var = null;
		// expr -> linear expression
		LPExpression expr = null;

		cardinalities = nfg.getAllCardinalities();
		nPlayers = nfg.getPlayers();

		profile = new int[nPlayers];
		umax = new double[nPlayers];

		umax[PLAYER1] = find_max_diff(nfg, PLAYER1);
		umax[PLAYER2] = find_max_diff(nfg, PLAYER2);

		totalActions = 0;
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			totalActions += nfg.getCardinality(iPlayer);
		}

		nVariables = 6 * totalActions + 2;

		// instantiate the lpSolver object
		LPSolver lpSolverr = LPAdapter.getSolver();

		/*
		 * setting lower and upper bounds to all variables
		 */
		var = new LPVariable[nVariables];

		int upto = nVariables - (3 * totalActions);
		for (i = 0; i < upto; i++) {
			var[i] = lpSolverr.variable(0.0, Double.MAX_VALUE);
		}
		upto = nVariables - (2 * totalActions);
		for (; i < upto; i++) {
			var[i] = lpSolverr.booleanVariable();
		}
		for (; i < nVariables; i++) {
			var[i] = lpSolverr.variable(0.0, Double.MAX_VALUE);
		}

		/*
		 * modelling the optimization problem
		 */
		// constraint 1.a (player 1)
		expr = lpSolverr.linearExpression();
		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			// var[i] -> p_s_1 (probability of strategy s of player 1)
			expr.addTerm(1, var[i]);
		}
		lpSolverr.addEquation(1, expr);

		// constraint 1.b (player 2)
		expr = lpSolverr.linearExpression();
		for (j = 0; j < cardinalities[PLAYER2]; i++, j++) {
			// var[i] -> p_s_2 (probability of strategy s of player 2)
			expr.addTerm(1, var[i]);
		}
		lpSolverr.addEquation(1, expr);

		// constraints 2.a (player 1)
		for (profile[PLAYER1] = 0; profile[PLAYER1] < cardinalities[PLAYER1]; profile[PLAYER1]++) {
			profile[PLAYER2] = 0;
			expr = lpSolverr.linearExpression();
			for (j = 0, k = cardinalities[PLAYER1]; j < cardinalities[PLAYER2]; j++, k++) {
				// var[k] -> p_s_2 (probability of strategy s of player 2)
				expr.addTerm(nfg.getUtility(profile, PLAYER1), var[k]);
				profile[PLAYER2]++;
			}
			// var[totalActions+profile[PLAYER1] -> u_s_1
			// (expected utility of player 1)
			expr.addTerm(-1, var[totalActions + profile[PLAYER1]]);
			// System.out.println("expr : " + expr.toString());
			lpSolverr.addEquation(0, expr);
		}

		// constraints 2.b (player 2)
		for (profile[PLAYER2] = 0; profile[PLAYER2] < cardinalities[PLAYER2]; profile[PLAYER2]++) {
			profile[PLAYER1] = 0;
			expr = lpSolverr.linearExpression();
			for (j = 0, k = 0; j < cardinalities[PLAYER1]; j++, k++) {
				// var[k] -> p_s_1 (probability of strategy s of player 1)
				expr.addTerm(nfg.getUtility(profile, PLAYER2), var[k]);
				profile[PLAYER1]++;
			}
			// var[totalActions +cardinalities[PLAYER1] +profile[PLAYER2]] ->
			// u_s_2
			// (expected utilility of player 2)
			expr.addTerm(-1, var[totalActions + cardinalities[PLAYER1]
					+ profile[PLAYER2]]);
			lpSolverr.addEquation(0, expr);
		}

		// constraints 3.a (player 1)
		j = 2 * totalActions;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++) {
			expr = lpSolverr.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(-1, var[j]);
			lpSolverr.addGreaterEquation(0, expr);
		}
		j++;

		// constraints 3.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++) {
			expr = lpSolverr.linearExpression();
			// var[k] -> u_s_2 (expected utility of playing strategy s of player
			// 2)
			expr.addTerm(1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(-1, var[j]);
			lpSolverr.addGreaterEquation(0, expr);
		}

		// constraints 4.a (player 1)
		j--;
		l = 2 * totalActions + 2;
		for (i = 0, k = totalActions; i < cardinalities[PLAYER1]; i++, k++, l++) {
			expr = lpSolverr.linearExpression();
			// var[k] -> u_s_1 (expected utility of playing strategy s of player
			// 1)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_1 (highest expected utility of player 1)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_1 ( regret of playing strategy s of player 1)
			expr.addTerm(-1, var[l]);
			lpSolverr.addEquation(0, expr);
		}

		// constraints 4.b (player 2)
		j++;
		for (i = 0; i < cardinalities[PLAYER2]; i++, k++, l++) {
			expr = lpSolverr.linearExpression();
			// var[k] -> u_s_2 (expected utility of player 2 playing strategy s)
			expr.addTerm(-1, var[k]);
			// var[j] -> u_2 (highest expected utility of player 2)
			expr.addTerm(1, var[j]);
			// var[l] -> r_s_2 (regret of player 2 playing strategy s)
			expr.addTerm(-1, var[l]);
			lpSolverr.addEquation(0, expr);
		}

		// constraints 11.a (player 1)
		j = 2 * totalActions + 2;
		k = 4 * totalActions + 2;
		for (i = 0; i < cardinalities[PLAYER1]; i++, j++, k++) {
			expr = lpSolverr.linearExpression();
			// var[j] -> r_s_1 (regret of player 2 playing strategy s)
			expr.addTerm(1 / umax[PLAYER1], var[j]);
			// var[k] -> f_s_1 (penalize regret of player 2 playing strategy s)
			expr.addTerm(-1, var[k]);
			lpSolverr.addGreaterEquation(0, expr);
		}

		// constraints 11.b (player 2)
		for (i = 0; i < cardinalities[PLAYER2]; i++, j++, k++) {

			expr = lpSolverr.linearExpression();
			// var[j] -> r_s_2 (regret of player 2 playing strategy s)
			expr.addTerm(1 / umax[PLAYER2], var[j]);
			// var[k] -> f_s_2 (penalize regret of player 2 playing strategy s)
			expr.addTerm(-1, var[k]);
			// System.out.println("expr : " + expr.toString());
			lpSolverr.addGreaterEquation(0, expr);
		}

		// constraints 12 (player 1 & player 2)
		j = 3 * totalActions + 2;
		k = 4 * totalActions + 2;
		for (i = 0; i < totalActions; i++, j++, k++) {
			expr = lpSolverr.linearExpression();
			// var[j] -> b_s_i (binary variable of player i playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> f_s_i (penalize regret of player i playiing strategy s)
			expr.addTerm(-1, var[k]);
			lpSolverr.addGreaterEquation(0, expr);
		}

		// constraints 9 (player 1 & player 2)
		k = 5 * totalActions + 2;
		for (i = 0; i < totalActions; i++, k++) {
			expr = lpSolverr.linearExpression();
			// var[j] -> p_s_i (probability of player i playing strategy s)
			expr.addTerm(1, var[i]);
			// var[k] -> g_s_i (penalize probability of player i playiing
			// strategy s)
			expr.addTerm(-1, var[k]);
			lpSolverr.addGreaterEquation(0, expr);
		}

		// constraints 10 (player 1 and player 2)
		j = 3 * totalActions + 2;
		k = 5 * totalActions + 2;
		for (i = 0; i < totalActions; i++, j++, k++) {
			expr = lpSolverr.linearExpression();
			// var[j] -> b_s_i (binary variable of player i playing strategy s)
			expr.addTerm(1, var[j]);
			// var[k] -> g_s_1 (penalize probability of player i playing
			// strategy s)
			expr.addTerm(1, var[k]);
			// System.out.println("expr : " + expr.toString());
			lpSolverr.addGreaterEquation(1, expr);
		}

		/*
		 * adding objective function of formulation 3
		 */
		expr = lpSolverr.linearExpression();

		j = 4 * totalActions + 2;
		k = 5 * totalActions + 2;
		for (i = 0; i < totalActions; i++, j++, k++) {
			expr.addTerm(1, var[k]);
			expr.addTerm(1, var[j]);
		}
		lpSolverr.addMinimize(expr);

		// lpSolver.exportModel("/home/kalyan/problem.lp");

		/*
		 * solving the optimization problem
		 */
		if (lpSolverr.solve()) {

			double objValue = lpSolverr.getObjectiveValue();
			double[] xval = lpSolverr.getValues(var);

			resultObject = this.getResults(xval, objValue);

		} else {
			// System.out.println("Error: " + lpSolverr.getStatus());
			throw new Exception("Can not be solved due to negative payoffs");
		}
		lpSolverr.end();

		return resultObject;
	}

	/**
	 * This function is a search algorithm to find minimum and maximum values on
	 * the given utilities.
	 * 
	 * @param nfg
	 *            Normal Form Game
	 * @param iPlayer
	 *            i'th Player
	 * @return difference between max. and min. utility of i'th Player
	 */
	private double find_max_diff(NormalFormGame nfg, int iPlayer) {
		double max, min;
		double utility;
		int[] strategyProfile = new int[2];

		strategyProfile[0] = 0;
		strategyProfile[1] = 0;

		// max => current maxium utility of i'th player
		max = nfg.getUtility(strategyProfile, iPlayer);
		// min => current minimum utility of i'th player
		min = nfg.getUtility(strategyProfile, iPlayer);

		for (strategyProfile[0] = 0; strategyProfile[0] < nfg.getCardinality(0); strategyProfile[0]++) {
			for (strategyProfile[1] = 0; strategyProfile[1] < nfg
					.getCardinality(1); strategyProfile[1]++) {

				utility = nfg.getUtility(strategyProfile, iPlayer);

				if (utility < min)
					min = utility;
				if (utility > max)
					max = utility;
			}
		}
		return (max - min);
	} // end of find_max_diff()

	/**
	 * constructs the result object from the results produced by the solver
	 * 
	 * @param xval :
	 *            variables values
	 * @param objValue :
	 *            objective value
	 * @return result object of a sample Nash equilibrium
	 */
	public ResultObject getResults(double[] xval, double objValue) {
		int iStrategy, iVariable;
		int[] cardinalities = nfg.getAllCardinalities();

		NashResultObject resultObject = new NashResultObject();
		NashEquilibrium equilibrium = new NashEquilibrium(nfg.getPlayers(), nfg
				.getAllCardinalities());
		double[] probabilityDistribution = null;

		resultObject.setGameName(nfg.getGameName());
		resultObject.setAlgorithmName(this.algorithmName);
		// System.out.println("\nGame Name : " + nfg.getGameName());
		// System.out.println("Algorithm Name : Mixed Integer Programming
		// Method");
		// System.out.println("Formulation Type : " + additionalInformation +
		// "\n");

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

		// System.out.println("\n\tPlayer 2 :");
		probabilityDistribution = new double[cardinalities[PLAYER2]];
		for (iStrategy = 0; iStrategy < cardinalities[PLAYER2]; iStrategy++, iVariable++) {
			probabilityDistribution[iStrategy] = xval[iVariable];
			// System.out.println("\t\tStrategy " + (iStrategy+1) + " --> " +
			// xval[iVariable]);
		}
		equilibrium
				.setProbabilityDistribution(PLAYER2, probabilityDistribution);
		// System.out.println("\n");

		iVariable = 2 * (cardinalities[PLAYER1] + cardinalities[PLAYER2]);

		equilibrium.setExpectedUtility(PLAYER1, xval[iVariable]);
		// System.out.println("\tPlayer 1 Expected Utility : " +
		// xval[iVariable]);
		iVariable++;
		equilibrium.setExpectedUtility(PLAYER2, xval[iVariable]);
		// System.out.println("\tPlayer 2 Expected Utility : " +
		// xval[iVariable]);

		resultObject.addEqulibrium(equilibrium);

		return resultObject;
	}

}
