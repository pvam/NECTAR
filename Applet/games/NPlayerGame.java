/**
 * 
 */
package games;

/**
 * @author kalyan
 * 
 */
public class NPlayerGame implements NormalFormGame {
	// game name
	private String gameName = null;

	// No. of players
	private int nPlayers;

	// No. of strategies of each player
	private int[] cardinalities = null;

	// Utility Matrices of n-players
	private MultiDimensionalArray[] utilityMatrix = null;

	// minimum utilities of all players
	private double[] minPayoffs = null;

	// maximum utilities of all players
	private double[] maxPayoffs = null;

	private boolean flag = false;

	/**
	 * Constructor takes the No. of players and strategies of each player and
	 * dynamically allocates memory for required size to class variables
	 * 
	 * @param nPlayers :
	 *            No. of Players in a Game
	 * @param cardinalities :
	 *            No. of strategies of each player
	 */
	public NPlayerGame(int nPlayers, int[] cardinalities) {

		int iPlayer;

		this.nPlayers = nPlayers;
		this.utilityMatrix = new MultiDimensionalArray[nPlayers];
		this.cardinalities = cardinalities;

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			utilityMatrix[iPlayer] = new MultiDimensionalArray(cardinalities);
		}

		minPayoffs = new double[nPlayers];
		maxPayoffs = new double[nPlayers];

	}

	/**
	 * Gives the No. of players in the Game
	 * 
	 * @return No. of Players
	 */
	public int getPlayers() {
		return this.nPlayers;
	}

	/**
	 * Sets the No. of players in the Game
	 * 
	 * @param nPlayers :
	 *            No. of Players
	 */
	public void setPlayers(int nPlayers) {
		this.nPlayers = nPlayers;
	}

	/**
	 * Gives the no. of strategies of i'th player
	 * 
	 * @return no. of strategies of the i'th player
	 */
	public int getCardinality(int iPlayer) {
		return this.cardinalities[iPlayer];
	}

	/**
	 * Sets the no. of strategies of i'th player
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
	 * Gives No. of strategies of each player
	 * 
	 * @return No. of strategies of each player
	 */
	public int[] getAllCardinalities() {
		return this.cardinalities;
	}

	/**
	 * Sets No. of strategies of each player
	 * 
	 * @param strategyArray :
	 *            No. of strategies of each player
	 */
	public void setAllCardinalities(int[] cardinalities) {
		this.cardinalities = cardinalities;

	}

	/**
	 * Gives the utility of i'th player of the given strategy profile
	 * 
	 * @param strategyProfile :
	 *            a particular strategy profile
	 * @param iPlayer :
	 *            i'th player
	 * 
	 * @return utility of i'th player at the given strategy profile
	 */
	public double getUtility(int[] strategyProfile, int iPlayer) {
		return utilityMatrix[iPlayer].getDataElement(strategyProfile);
	}

	/**
	 * Sets the utility of i'th player at the given strategy profile
	 * 
	 * @param strategyProfile :
	 *            a particular strategy profile
	 * @param iPlayer :
	 *            i'th player
	 * @param utility :
	 *            utitlity of i'th player at the given strategy pfroile
	 */
	public void setUtility(int[] strategyProfile, int iPlayer, double utility) {
		utilityMatrix[iPlayer].setDataElement(strategyProfile, utility);

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
		return;
	}

	public void removeDominatedStrategy(int iPlayer, int strategy) {
		// TODO Auto-generated method stub
	}

	/**
	 * Finds the offset of single dimension array by taking all indices of n
	 * dimensions. Simply calculates a profile position
	 * 
	 * @param strategyProfile :
	 *            indices of all n dimensions
	 * 
	 * @return position of strategy profile in one dimensional array
	 */
	public int getOffset(int[] strategyProfile) {
		return utilityMatrix[0].getOffset(strategyProfile);
	}

	/**
	 * displays the game, it returns the string form of the output stream
	 */
	public String displayGame() {
		int[] indices = new int[nPlayers];
		int iPlayer;
		double utility;

		StringBuffer stream = new StringBuffer();
		stream.append("Game Name : " + gameName + "\n\n");

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			stream.append("Player " + (iPlayer + 1) + " Strategies : "
					+ cardinalities[iPlayer] + "\n");
		}

		stream.append("\n Utilities of Players (  profile -> utilities) \n\n");

		while (indices[nPlayers - 1] < cardinalities[nPlayers - 1]) {
			for (indices[0] = 0; indices[0] < cardinalities[0]; indices[0]++) {
				addProfile(stream, indices);
				stream.append("      [    ");
				for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
					utility = this.getUtility(indices, iPlayer);
					stream.append(roundValue(utility) + "    ");
				}
				stream.append("]\n");
			}

			for (iPlayer = 1; iPlayer < nPlayers; iPlayer++) {

				indices[iPlayer]++;
				if (indices[iPlayer] == cardinalities[iPlayer]
						&& iPlayer != nPlayers - 1) {
					indices[iPlayer] = 0;
					continue;
				}
				break;
			}
		}

		return stream.toString();
	}

	/**
	 * it adds strategy profile indices to the stream
	 * 
	 * @param stream :
	 *            output stream
	 * @param indices :
	 *            strategy profile indices
	 */
	private void addProfile(StringBuffer stream, int[] indices) {
		int iPlayer;

		stream.append("   [  ");
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			stream.append((indices[iPlayer] + 1) + "  ");
		}
		stream.append("]   -->");

		return;
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
	 * @return minimum utility
	 */
	public double getMinPayoff(int iPlayer) {
		return this.minPayoffs[iPlayer];
	}

	/**
	 * gets the maximum utility of i'th player
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * @return maximum utility
	 */
	public double getMaxPayoff(int iPlayer) {
		return this.maxPayoffs[iPlayer];
	}

	/**
	 * rounds the double value for better readability
	 * 
	 * @param x :
	 *            double value
	 * @return rounded value
	 */
	private double roundValue(double x) {
		x = x * 1000;
		x = Math.round(x);
		x = ((double) x) / 1000;
		return x;
	}
}
