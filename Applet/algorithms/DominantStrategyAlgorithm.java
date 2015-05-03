/**
 * 
 */
package algorithms;

import games.*;

import java.util.*;

/**
 * @author kalyan
 * 
 */
public class DominantStrategyAlgorithm {

	private final int NONE = 0;

	private final int WEAK = 1;

	private final int STRONG = 2;

	private NormalFormGame nfg = null;

	/**
	 * Computes Weakly and Strongly dominant strategies of all players It checks
	 * the each strategy s_i of player i is strongly dominant,
	 * 
	 * @return Results of the computation
	 */
	public ResultObject computeDominantStrategies(NormalFormGame nfg) {
		int iPlayer, nPlayers, iStrategy, result;

		this.nfg = nfg;
		ArrayList dominantList = new ArrayList();
		DominatedStrategy strategy = null;

		nPlayers = nfg.getPlayers();
		int[] cardinalities = nfg.getAllCardinalities();

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			for (iStrategy = 0; iStrategy < cardinalities[iPlayer]; iStrategy++) {
				result = checkDominantStrategy(iPlayer, iStrategy);
				if (result == NONE) {
					continue;
				} else {
					strategy = new DominatedStrategy();
					strategy.setPlayer(iPlayer);
					strategy.setStrategy(iStrategy);
					if (result == STRONG) {
						strategy.setStronglyDominated(true);
					} else {
						strategy.setStronglyDominated(false);
					}
					dominantList.add(strategy);
				}
			}
		}

		ResultObject resultObject = getResults(dominantList);
		return resultObject;
	}

	/**
	 * It checks the whether the given i'th strategy is dominant Strategy of
	 * i'th player by comparing the utilities with his other strategies. It
	 * returns whether the given strtegy is Strong or Weak dominant strategy or
	 * none.
	 * 
	 * @param iPlayer :
	 *            i'th Player
	 * @param iStrategy :
	 *            i'th Strategy of i'th Player
	 * @return dominance of the i'th strategy
	 */
	private int checkDominantStrategy(int iPlayer, int iStrategy) {
		int jPlayer, jStrategy;
		int nPlayers;
		double iUtility, jUtility;
		boolean weak, strong, flag;
		boolean totalWeak;

		nPlayers = nfg.getPlayers();
		int[] cardinalities = nfg.getAllCardinalities();
		int[] indices = new int[nPlayers];

		totalWeak = false;

		for (jStrategy = 0; jStrategy < cardinalities[iPlayer]; jStrategy++) {
			if (jStrategy != iStrategy) {

				flag = false;
				weak = false;
				strong = false;

				while (true) {
					indices[iPlayer] = iStrategy;
					iUtility = nfg.getUtility(indices, iPlayer);
					indices[iPlayer] = jStrategy;
					jUtility = nfg.getUtility(indices, iPlayer);

					if (iUtility < jUtility) {
						return NONE;
					} else if (iUtility == jUtility) {
						weak = true;
					} else if (iUtility > jUtility) {
						strong = true;
					}

					for (jPlayer = 0; jPlayer < nPlayers; jPlayer++) {
						if (jPlayer != iPlayer) {
							indices[jPlayer]++;
							if (indices[jPlayer] == cardinalities[jPlayer]) {
								indices[jPlayer] = 0;
								if (jPlayer == (nPlayers - 1)
										|| (iPlayer == (nPlayers - 1) && jPlayer == (nPlayers - 2))) {
									flag = true;
									break;
								}
								continue;
							}
							break;
						}
					}
					if (flag == true) {
						break;
					}
				}
				if (weak && !strong) {
					return NONE;
				} else if (weak && strong) {
					totalWeak = true;
				}
			}
		}

		if (totalWeak) {
			return WEAK;
		} else {
			return STRONG;
		}
	}

	/**
	 * It copies all the dominant strategies information into result object and
	 * returns result object.
	 * 
	 * @param dominantList :
	 *            dominant strategy lists
	 * @return result object
	 */
	private ResultObject getResults(ArrayList dominantList) {
		StringBuffer stream = new StringBuffer();
		MiscResultObject resultObject = new MiscResultObject();
		DominatedStrategy strategy = null;

		stream.append("Game Name : " + nfg.getGameName() + "\n\n");

		if (dominantList.size() == 0) {
			stream.append("\tThere are no Dominant Strategies \n");
		}

		Iterator iterator = dominantList.iterator();

		while (iterator.hasNext()) {
			strategy = (DominatedStrategy) iterator.next();
			stream.append("\tPlayer " + (strategy.getPlayer() + 1) + "\n");
			stream.append("\t\tStrategy " + (strategy.getStrategy() + 1)
					+ " is the ");
			if (strategy.isDominated()) {
				stream.append("Strongly dominant strategy");
			} else {
				stream.append("Weakly dominant strategy");
			}
			stream.append("\n\n");
		}

		resultObject.setResults(stream);

		return resultObject;
	}

	/**
	 * Displays Dominant Strategies of all players
	 * 
	 * @param status :
	 *            status of players
	 */
	/*private void displayDominantStrategies(int[][] status, int nPlayers) {
		int stronglyDominantStrategies = 0;
		int weaklyDominantStrategies = 0;
		int i;
		System.out.println(":: Dominant Strategies ::\n");
		for (i = 0; i < nPlayers; i++) {
			if (status[i][0] == STRONG) {
				System.out.println("\tPlayer " + (i + 1) + " has Strongly"
						+ " Dominant Strategy -> " + (status[i][1] + 1));
				stronglyDominantStrategies++;
			} else if (status[i][0] == WEAK) {
				System.out.println("\tPlayer " + (i + 1) + " has Weakly "
						+ "Dominant Strategy   -> " + (status[i][1] + 1));
				weaklyDominantStrategies++;
			} else
				System.out.println("\tPlayer " + (i + 1) + " has no"
						+ " Dominant Strategy");
		}
		System.out.println("\n");
		System.out.println(":: Dominant Equlibria ::\n");

		if (stronglyDominantStrategies == nPlayers) {
			System.out.println("\tStrongly Dominant Nash Equilibria: ");
			System.out.print("\t\tProfile : (  ");
			for (i = 0; i < nPlayers; i++)
				System.out.print((status[i][1] + 1) + "  ");
			System.out.println(")");
		} else if ((stronglyDominantStrategies + weaklyDominantStrategies) == nPlayers) {
			System.out.println("\tWeakly Dominant Nash Equilibria: ");
			System.out.print("\t\tProfile : (  ");
			for (i = 0; i < nPlayers; i++)
				System.out.print((status[i][1] + 1) + "  ");
			System.out.println(")");
		} else {
			System.out.println("\tNo Dominant Nash Equilibria");
		}
		System.out.println("\n\n");
	}*/
}
