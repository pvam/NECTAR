/**
 * 
 */
package games;

/**
 * @author kalyan
 * 
 */
public class TwoPersonZeroSum implements NormalFormGame {

	// constants
	final private int PLAYER1 = 0;

	final private int PLAYER2 = 1;

	private String gameName = null; // game name

	private int nPlayers; // no. of players

	private int[] cardinalities = new int[2]; // cardinalities of players

	private double[][] matrix = null; // utility matrix of player1

	private double[] minPayoffs = null;

	private double[] maxPayoffs = null;

	private boolean flag = false;

	public TwoPersonZeroSum(int nPlayers, int[] cardinalities) {
		this.nPlayers = nPlayers;
		this.cardinalities = cardinalities;

		matrix = new double[cardinalities[PLAYER1]][cardinalities[PLAYER2]];

		minPayoffs = new double[nPlayers];
		maxPayoffs = new double[nPlayers];
	}

	public int getPlayers() {
		return this.nPlayers;
	}

	public void setPlayers(int nPlayers) {
		this.nPlayers = nPlayers;
	}

	public int getCardinality(int iPlayer) {
		return this.cardinalities[iPlayer];
	}

	public void setCardinality(int cardinality, int iPlayer) {
		this.cardinalities[iPlayer] = cardinality;

	}

	public int[] getAllCardinalities() {
		return this.cardinalities;
	}

	public void setAllCardinalities(int[] cardinalities) {
		this.cardinalities = cardinalities;

	}

	public double getUtility(int[] strategyProfile, int iPlayer) {

		if (iPlayer == PLAYER1) {
			return matrix[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]];
		} else {
			return -matrix[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]];
		}
	}

	public void setUtility(int[] strategyProfile, int iPlayer, double utility) {
		if (iPlayer == PLAYER1) {
			this.matrix[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]] = utility;
		} else {
			this.matrix[strategyProfile[PLAYER1]][strategyProfile[PLAYER2]] = -utility;
		}

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

	public int getOffset(int[] strategyProfile) {

		return strategyProfile[PLAYER1] * cardinalities[PLAYER1]
				+ strategyProfile[PLAYER2];
	}

	public String displayGame() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see games.NormalFormGame#setGameName(java.lang.String)
	 */
	public void setGameName(String name) {
		this.gameName = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see games.NormalFormGame#getGameName()
	 */
	public String getGameName() {
		return this.gameName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see games.NormalFormGame#getMinPayoff(int)
	 */
	public double getMinPayoff(int iPlayer) {
		return this.minPayoffs[iPlayer];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see games.NormalFormGame#getMaxPayoff(int)
	 */
	public double getMaxPayoff(int iPlayer) {
		return this.maxPayoffs[iPlayer];
	}

}
