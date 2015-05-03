/**
 * 
 */
package games;

/**
 * BimatrixGame is a representation of two person normal form Game.
 * 
 * @author kalyan
 * 
 */
public class BimatrixGame implements NormalFormGame {

	// constants
	final private int PLAYER1 = 0;

	final private int PLAYER2 = 1;

	// game name
	private String gameName = null;

	// no. of players
	private int nPlayers;

	// no. of strategies of all players
	private int[] cardinalities = new int[2];

	// utility matrix of player 1
	private double[][] matrixA = null;

	// utility matrix of player 2
	private double[][] matrixB = null;

	// minimum utilities of all players
	private double[] minPayoffs = null;

	// maximum utilities of all players
	private double[] maxPayoffs = null;

	private boolean flag = false;

	/**
	 * this constructor initializes the required variables
	 * 
	 * @param nPlayers :
	 *            no. of players
	 * @param cardinalities :
	 *            no. of strategies of all players
	 */
	public BimatrixGame(int nPlayers, int[] cardinalities) {
		this.nPlayers = nPlayers;
		this.cardinalities = cardinalities;

		matrixA = new double[cardinalities[PLAYER1]][cardinalities[PLAYER2]];
		matrixB = new double[cardinalities[PLAYER1]][cardinalities[PLAYER2]];

		minPayoffs = new double[nPlayers];
		maxPayoffs = new double[nPlayers];
	}

	/**
	 * returns the no.of players in the game
	 * 
	 * @return no. of players
	 */
	public int getPlayers() {
		return this.nPlayers;
	}

	/**
	 * sets the no.of players in the game
	 * 
	 * @param nPlayers :
	 *            no.of players
	 */
	public void setPlayers(int nPlayers) {
		this.nPlayers = nPlayers;
	}

	/**
	 * returns the no.of strategies of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @return no. of strategies of i'th player
	 */
	public int getCardinality(int iPlayer) {
		return this.cardinalities[iPlayer];
	}

	/**
	 * sets the no.of strategies of i'th player
	 * 
	 * @param cardinality :
	 *            no. of strategies
	 * @param iPlayer :
	 *            i'th player
	 */
	public void setCardinality(int cardinality, int iPlayer) {

		this.cardinalities[iPlayer] = cardinality;
	}

	/**
	 * returns the no.of strategies of all players
	 * 
	 * @return no. of strategies of all players
	 */
	public int[] getAllCardinalities() {

		return this.cardinalities;
	}

	/**
	 * sets the no. of strategies of all players
	 * 
	 * @param cardinalities :
	 *            no. of strategies of all players
	 */
	public void setAllCardinalities(int[] cardinalities) {

		this.cardinalities = cardinalities;
	}

	/**
	 * gets the utility of i'th player at particular strategy profile.
	 * 
	 * @param strategyProfile :
	 *            strategy profile indices
	 * @param iPlayer :
	 *            i'th player
	 * @return utility of i'th player at particular strategy profile
	 */
	public double getUtility(int[] strategyProfile, int iPlayer) {

		if (iPlayer == PLAYER1)
			return this.matrixA[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]];
		else
			return this.matrixB[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]];
	}

	/**
	 * sets the utility of i'th player at particular strategy profile. At the
	 * same time it updates the minimum and maximum utilities of i'th player
	 * 
	 * @param strategyProfile :
	 *            strategy profile indices
	 * @param iPlayer :
	 *            i'th player
	 * @param utility :
	 *            utility of i'th player at strategy profile
	 */
	public void setUtility(int[] strategyProfile, int iPlayer, double utility) {

		if (iPlayer == PLAYER1)
			this.matrixA[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]] = utility;
		else
			this.matrixB[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]] = utility;

		if (flag) {
			if (utility < minPayoffs[iPlayer]) {
				minPayoffs[iPlayer] = utility;
			}

			if (utility > maxPayoffs[iPlayer]) {
				maxPayoffs[iPlayer] = utility;
			}
		} else {
			minPayoffs[iPlayer] = utility;
			maxPayoffs[iPlayer] = utility;
			flag = true;
		}
	}

	public void removeDominatedStrategy(int iPlayer, int strategy) {
		// TODO Auto-generated method stub
	}

	/**
	 * convert strategy profile to offset
	 * 
	 * @param strategyProfile :
	 *            strategy profile indices
	 */
	public int getOffset(int[] strategyProfile) {
		return strategyProfile[PLAYER1] * this.getCardinality(PLAYER2)
				+ strategyProfile[PLAYER2];
	}

	/**
	 * displays this two player game
	 * 
	 * @return string form output stream
	 */
	public String displayGame() {
		int i, j;

		StringBuffer stream = new StringBuffer();

		stream.append("Game Name : " + gameName + "\n\n");

		stream.append("Player 1 Strategies  -->  "
				+ this.getCardinality(PLAYER1) + "\n");
		stream.append("Player 2 Strategies  -->  "
				+ this.getCardinality(PLAYER2) + "\n\n");
		stream.append("Player 1 utilities : \n\n");

		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			for (j = 0; j < cardinalities[PLAYER2]; j++) {
				stream.append("\t" + roundValue(matrixA[i][j]));
			}
			stream.append("\n");
		}

		stream.append("\nPlayer 2 utilities : \n\n");

		for (i = 0; i < cardinalities[PLAYER1]; i++) {
			for (j = 0; j < cardinalities[PLAYER2]; j++) {
				stream.append("\t" + roundValue(matrixB[i][j]));
			}
			stream.append("\n");
		}

		return stream.toString();
	}

	/**
	 * sets the game name
	 * 
	 * @param name :
	 *            game name
	 */
	public void setGameName(String name) {
		this.gameName = name;
	}

	/**
	 * gets the game name
	 */
	public String getGameName() {

		return this.gameName;
	}

	/**
	 * gets the minimum utility of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @return minimum utility i'th player
	 */
	public double getMinPayoff(int iPlayer) {
		return this.minPayoffs[iPlayer];
	}

	/**
	 * gets the maximum utility of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th Player
	 * @return maximum utility of i'th player
	 */
	public double getMaxPayoff(int iPlayer) {
		return this.maxPayoffs[iPlayer];
	}

	/**
	 * rounding the value for better readability
	 * 
	 * @param x :
	 *            double value
	 * @return rouded value
	 */
	private double roundValue(double x) {
		x = x * 1000;
		x = Math.round(x);
		x = ((double) x) / 1000;
		return x;
	}
}