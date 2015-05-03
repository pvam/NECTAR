/**
 * 
 */
package games;

/**
 * @author kalyan
 * 
 */
public class GameFactory {

	/**
	 * gets the particular concrete normal form game with normal form interface
	 * 
	 * @param nPlayers :
	 *            no.of players
	 * @param cardinalities :
	 *            no. of strategies of each player
	 * @return
	 */
	public static NormalFormGame getNormalFormGame(int nPlayers,
			int[] cardinalities) {
		if (nPlayers == 2) {
			return new BimatrixGame(nPlayers, cardinalities);
		} else {
			return new NPlayerGame(nPlayers, cardinalities);
		}
	}
}
