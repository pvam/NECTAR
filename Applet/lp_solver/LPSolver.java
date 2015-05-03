/**
 * 
 */
package lp_solver;


/**
 * @author kalyan
 * 
 *         It is an interface to concrete lp solver adapter
 */
public interface LPSolver {
	public LPVariable variable(double lowerBound, double upperBound)
			throws Exception;

	public LPVariable booleanVariable() throws Exception;

	public LPExpression linearExpression() throws Exception;

	public void addEquation(double rhsValue, LPExpression expression)
			throws Exception;

	public void removeObjective();

	public void addGreaterEquation(double rhsValue, LPExpression expression)
			throws Exception;

	public void addLesserEquation(double rhsValue, LPExpression expression)
			throws Exception;

	public void addMaximize(LPExpression expression) throws Exception;

	public void addMinimize(LPExpression expression) throws Exception;

	public boolean solve() throws Exception;

	public double getObjectiveValue() throws Exception;

	public double[] getValues(LPVariable[] variables) throws Exception;

	public String getStatus() throws Exception;

	public void end() throws Exception;
}
