/**
 * 
 */
package algorithms;

import java.util.ArrayList;
import java.util.List;

import games.*;

/**
 * @author kalyan
 * 
 */
public class PureNashEquilibria implements Algorithm {

	private final int NONE = 0; // constants

	private final int PLAYER1 = 0;

	private NormalFormGame nfg = null; // Normal form game Object which

	// contains

	// all information of players

	private String algorithmName = null;

	public PureNashEquilibria() {
		this.algorithmName = new String("Pure Nash Equilibria");
	}

	/**
	 * Computes the Nash Euilibria inteligently, First it finds all best
	 * responses of player 1, By taking these profile list, it checks whether
	 * the given profile is best reponse to player 2 or not. if it is not then
	 * remove the profile from the list. This operation is done for rest of
	 * players. The remaining profile list is nothing but best responses of all
	 * players which means each profile is a nash equilibrium.
	 */
	public ResultObject computeEquilibria(NormalFormGame nfg) {
		int i, j;
		int[] profile = null;
		int iPlayer = NONE;

		this.nfg = nfg;

		List[] bestResponseListArray = null; // best responses list array
		List list = null;
		List bestResponsesList = null;

		int nPlayers = nfg.getPlayers();
		int[] strategyArray = nfg.getAllCardinalities();
		// computes best responses of a player
		bestResponseListArray = computeBestResponseListOfPlayer(iPlayer);

		bestResponsesList = bestResponseListArray[PLAYER1];
		for (i = 1; i < bestResponseListArray.length; i++) {
			if (bestResponseListArray[i].size() != 0)
				bestResponsesList.addAll(bestResponseListArray[i]);
		}
		// System.out.println("Total size : " + bestResponsesList.size());

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			if (iPlayer != PLAYER1) {
				for (j = 0; j < bestResponsesList.size(); j++) {
					profile = (int[]) bestResponsesList.get(j);
					list = getBestResponse(profile, iPlayer,
							strategyArray[iPlayer]);
					if (!isSameProfile(profile, iPlayer, list)) {
						bestResponsesList.remove(j--);
					}
				}
				/*
				 * System.out.println("After finding " + (i+1) + " size will be " +
				 * bestResponsesList.size());
				 */
			}
		}

		ResultObject resultObject = getResult(bestResponsesList);

		return resultObject;
	}

	private ResultObject getResult(List bestResponsesList) {
		int i, iPlayer, nPlayers;
		int[] profile = null, cardinalities = null;

		NashResultObject resultObject = new NashResultObject();
		NashEquilibrium equilibrium = null;
		double[] probabilityDistribution = null;

		resultObject.setAlgorithmName(this.algorithmName);
		resultObject.setGameName(nfg.getGameName());

		// System.out.println("Game Name : " + nfg.getGameName());
		// System.out.println("Algorithm Name : Pure Nash Equilibria \n");

		nPlayers = nfg.getPlayers();
		cardinalities = nfg.getAllCardinalities();

		if (bestResponsesList.size() != 0) {
			// System.out.println("\tNo . of Equilibria : " +
			// bestResponsesList.size());
			for (i = 0; i < bestResponsesList.size(); i++) {
				equilibrium = new NashEquilibrium(nPlayers, cardinalities);
				// System.out.print("\n\t("+ (i+1) + ") ----> Strategy Profile [
				// ");
				profile = (int[]) bestResponsesList.get(i);
				for (iPlayer = 0; iPlayer < profile.length; iPlayer++) {
					probabilityDistribution = new double[cardinalities[iPlayer]];
					probabilityDistribution[profile[iPlayer]] = 1;
					equilibrium.setProbabilityDistribution(iPlayer,
							probabilityDistribution);
					// System.out.print( (profile[iPlayer]+1) + " ");
				}
				// System.out.println("]\n");

				for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
					equilibrium.setExpectedUtility(iPlayer, nfg.getUtility(
							profile, iPlayer));
					// System.out.println("\t\tPlayer " + (iPlayer + 1) + "
					// Utility : " + nfg.getUtility(profile, iPlayer));
				}
				resultObject.addEqulibrium(equilibrium);
			}
		} else {
			// System.out.println("\tNo Pure Nash Equilibrium \n");
		}

		return resultObject;
	}

	/**
	 * Computes Best response list of i'th player over S_i and arranges best
	 * response profiles strategy wise i.e. if best response of particular s_i
	 * is some set of set of strategies of player 'i' then corresponding
	 * strategy profiles arranged according to strategies of player 'i' and
	 * returns whole bestResponseListArray of player 'i'.
	 * 
	 * @param iPlayer :
	 *            i'th player
	 * 
	 * @return Best response list array of Player i
	 */
	private List[] computeBestResponseListOfPlayer(int iPlayer) {
		int i;

		int nPlayers = nfg.getPlayers();
		int[] indices = new int[nPlayers];
		int[] cardinalities = nfg.getAllCardinalities();
		int[] bestIndex = new int[cardinalities[iPlayer]];

		List[] bestResponseListArray = new ArrayList[cardinalities[iPlayer]];
		List bestResponseList = null;

		// defining
		for (i = 0; i < cardinalities[iPlayer]; i++)
			bestResponseListArray[i] = new ArrayList();

		while (indices[nPlayers - 1] < cardinalities[nPlayers - 1]) {

			indices[iPlayer] = 0;
			bestResponseList = getBestResponse(indices, iPlayer,
					cardinalities[iPlayer]);

			while (bestResponseList.size() != 0) {
				bestIndex = (int[]) bestResponseList.remove(0);
				bestResponseListArray[bestIndex[iPlayer]].add(bestIndex);
			}

			for (i = 0; i < nPlayers; i++) {
				if (i == iPlayer) {
					continue;
				}
				indices[i]++;
				if (indices[i] == cardinalities[i] && i != nPlayers - 1) {
					indices[i] = 0;
					continue;
				}
				break;
			}
		}

		return bestResponseListArray;
	}

	/**
	 * Finds Best responses of i'th player for a particular profile s_i and
	 * returns the bestResponseList.
	 * 
	 * @param strategyProfile :
	 *            a strategy profile
	 * @param iPlayer :
	 *            i'th player
	 * @param strategyArray :
	 *            nStrategies
	 * 
	 * @return Best Response List
	 */
	private List getBestResponse(int[] profile, int iPlayer, int nStrategies) {
		int i, j;
		int count;
		double bestUtility, utility;
		int[] bestStrategies = new int[nStrategies];
		List bestResponseList = new ArrayList();

		int[] indices = (int[]) profile.clone();
		int[] indicesClone = null;
		indices[iPlayer] = 0;

		count = 0;
		bestUtility = nfg.getUtility(indices, iPlayer);
		for (i = 1; i < nStrategies; i++) {
			indices[iPlayer] = i;
			utility = nfg.getUtility(indices, iPlayer);
			if (utility > bestUtility) {
				bestUtility = utility;
				count = 0;
				bestStrategies[count] = i;
			} else if (utility == bestUtility) {
				bestStrategies[++count] = i;
			}
		}
		for (i = 0; i <= count; i++) {
			// indicesClone = (int[])indices.clone();

			indicesClone = new int[indices.length];
			for (j = 0; j < indices.length; j++) {
				indicesClone[j] = indices[j];
			}
			indicesClone[iPlayer] = bestStrategies[i];

			bestResponseList.add(indicesClone);
		}
		return bestResponseList;
	}

	/**
	 * Checks whether the specified strategyProfile is in the bestResponseList
	 * 
	 * @param strategyProfile :
	 *            a strategy profile
	 * @param iPlayer :
	 *            i'th player
	 * @param bestResponseList :
	 *            best response list of i'th player
	 * 
	 * @return status
	 */
	private boolean isSameProfile(int[] strategyProfile, int iPlayer,
			List bestResponseList) {
		int[] indices = null;
		for (int i = 0; i < bestResponseList.size(); i++) {
			indices = (int[]) bestResponseList.get(i);
			if (strategyProfile[iPlayer] == indices[iPlayer])
				return true;
		}
		return false;
	}
}
