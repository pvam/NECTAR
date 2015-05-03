/**
 * 
 */
package lp_solver;

/**
 * @author kalyan
 * 
 *         It is an interface to concrete LPexpression adapter
 */
public interface LPExpression {

	public void addTerm(double value, LPVariable lpVariable) throws Exception;

}
