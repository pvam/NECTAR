/**
 * 
 */
package algorithms;

import games.NormalFormGame;

/**
 * @author kalyan
 * 
 * All the algorithms computing Nash equilibrium points must implements this
 * interface. Other objects communicate the concrete objects with this interface
 * only
 */
public interface Algorithm {

	public ResultObject computeEquilibria(NormalFormGame nfg)
			throws Exception;

}
