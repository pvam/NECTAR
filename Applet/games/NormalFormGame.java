/**
 * 
 */
package games;

/**
 * @author kalyan
 * 
 */
public interface NormalFormGame {

	public int getPlayers();

	public void setPlayers(int nPlayers);

	public int getCardinality(int iPlayer);

	public void setCardinality(int cardinality, int iPlayer);

	public int[] getAllCardinalities();

	public void setAllCardinalities(int[] cardinalities);

	public double getUtility(int[] strategyProfile, int iPlayer);

	public void setUtility(int[] strategyProfile, int iPlayer, double utility);

	public void removeDominatedStrategy(int iPlayer, int strategy);

	public int getOffset(int[] strategyProfile);

	public void setGameName(String name);

	public String getGameName();

	public String displayGame();

	public double getMinPayoff(int iPlayer);

	public double getMaxPayoff(int iPlayer);

}
