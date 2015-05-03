/**
 * 
 */
package lp_solver;

import lpsolve.LPSolverImpl;

/**
 * @author kalyan
 * 
 */
public class LPAdapter {

	/**
	 * it gets the specific solver adapter, for now we have only CplexSolver
	 * Adatper
	 * 
	 * @return concrete solver adapter
	 * @throws Exception
	 */
	public static LPSolver getSolver() throws Exception {
		return new LPSolverImpl(0);// new CplexSolver();
	}
}
