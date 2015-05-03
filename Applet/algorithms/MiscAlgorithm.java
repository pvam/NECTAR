/**
 * 
 */
package algorithms;

import games.*;

/**
 * @author kalyan
 * 
 */
public class MiscAlgorithm {

	final private int CONSTANT = 5;

	final private int PLAYER1 = 0;

	final private int PLAYER2 = 1;

	final private int REPLICATED = 1;

	/**
	 * It adds some constant value to every utility and returns the new game
	 * 
	 * @param nfg
	 *            Normal Form Game
	 * @return modifield normal form game
	 */
	public NormalFormGame getNewGame(NormalFormGame nfg) {
		int nPlayers, iStrategy, jStrategy;
		int[] cardinalities, indices;

		nPlayers = nfg.getPlayers();
		cardinalities = nfg.getAllCardinalities();

		indices = new int[nPlayers];

		NormalFormGame newNfg = GameFactory.getNormalFormGame(nfg.getPlayers(),
				nfg.getAllCardinalities());
		newNfg.setGameName(nfg.getGameName());

		for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++) {
			indices[PLAYER1] = iStrategy;
			for (jStrategy = 0; jStrategy < cardinalities[PLAYER2]; jStrategy++) {
				indices[PLAYER2] = jStrategy;

				newNfg.setUtility(indices, PLAYER1, (nfg.getUtility(indices,
						PLAYER1) + CONSTANT));
				newNfg.setUtility(indices, PLAYER2, (nfg.getUtility(indices,
						PLAYER2) + CONSTANT));
			}
		}

		return newNfg;
	}

	/**
	 * It removes the replicated strategies on the given normal form game and
	 * returns new game
	 * 
	 * @param nfg :
	 *            normal form game
	 * @return reduced normal form game
	 * @throws Exception
	 */
	public NormalFormGame getReducedGame(NormalFormGame nfg) throws Exception {

		int i, j, iPlayer, jPlayer, nPlayers;
		int iStrategy, jStrategy, xCount, yCount;
		NormalFormGame reducedNfg = null;
		int[] xStrategies = null, yStrategies = null;
		int[] cardinalities = null;
		int[] oldProfile = null, newProfile = null;
		double utility;

		nPlayers = nfg.getPlayers();
		if (nPlayers != 2) {
			throw new Exception(
					"The given normal form game is not a two-person game");
		}

		cardinalities = nfg.getAllCardinalities();

		iPlayer = PLAYER1;
		jPlayer = PLAYER2;
		xStrategies = new int[cardinalities[PLAYER1]];
		xCount = getReplicatedStrategies(iPlayer, jPlayer, nfg, xStrategies);
		yStrategies = new int[cardinalities[PLAYER2]];
		yCount = getReplicatedStrategies(jPlayer, iPlayer, nfg, yStrategies);

		int[] oldXStrategies = null;
		int[] oldYStrategies = null;

		if (xCount != 0 || yCount != 0) {

			cardinalities[PLAYER1] -= xCount;
			cardinalities[PLAYER2] -= yCount;

			oldXStrategies = new int[cardinalities[PLAYER1]];
			oldYStrategies = new int[cardinalities[PLAYER2]];

			for (i = 0, j = 0; i < xStrategies.length; i++) {
				if (xStrategies[i] != REPLICATED) {
					oldXStrategies[j++] = i;
				}
			}
			for (i = 0, j = 0; i < yStrategies.length; i++) {
				if (yStrategies[i] != REPLICATED) {
					oldYStrategies[j++] = i;
				}
			}

			reducedNfg = GameFactory.getNormalFormGame(nPlayers, cardinalities);
			reducedNfg.setGameName(nfg.getGameName());

			newProfile = new int[nPlayers];
			oldProfile = new int[nPlayers];

			for (iStrategy = 0; iStrategy < cardinalities[PLAYER1]; iStrategy++) {
				oldProfile[PLAYER1] = oldXStrategies[iStrategy];
				newProfile[PLAYER1] = iStrategy;
				for (jStrategy = 0; jStrategy < cardinalities[PLAYER2]; jStrategy++) {
					oldProfile[PLAYER2] = oldYStrategies[jStrategy];
					newProfile[PLAYER2] = jStrategy;
					for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
						utility = nfg.getUtility(oldProfile, iPlayer);
						reducedNfg.setUtility(newProfile, iPlayer, utility);
					}
				}
			}

			nfg = reducedNfg;
		}
		return nfg;
	}

	private int getReplicatedStrategies(int xPlayer, int yPlayer,
			NormalFormGame nfg, int[] strategies) {

		int nPlayers, iPlayer, iStrategy, jStrategy, othersStrategy, count;
		int[] cardinalities;
		int[] iProfile, jProfile;
		double iUtility, jUtility;
		boolean flag = false;

		nPlayers = nfg.getPlayers();

		cardinalities = nfg.getAllCardinalities();
		iProfile = new int[nPlayers];
		jProfile = new int[nPlayers];

		for (iStrategy = 0, count = 0; iStrategy < cardinalities[xPlayer]; iStrategy++) {
			iProfile[xPlayer] = iStrategy;
			for (jStrategy = iStrategy + 1; jStrategy < cardinalities[xPlayer]; jStrategy++) {
				jProfile[xPlayer] = jStrategy;
				flag = false;
				for (othersStrategy = 0; othersStrategy < cardinalities[yPlayer]; othersStrategy++) {
					iProfile[yPlayer] = othersStrategy;
					jProfile[yPlayer] = othersStrategy;

					for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
						iUtility = nfg.getUtility(iProfile, iPlayer);
						jUtility = nfg.getUtility(jProfile, iPlayer);

						if (iUtility != jUtility) {
							flag = true;
							break;
						}
					}

					if (flag) {
						break;
					}
				}
				if (!flag) {
					strategies[iStrategy] = REPLICATED;
					count++;
					break;
				}
			}
		}

		return count;
	}

}
