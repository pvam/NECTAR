/**
 * 
 */
package algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import games.DominatedStrategy;
import games.NormalFormGame;

/**
 * @author kalyan
 * 
 */
public class DominatedStrategyAlgorithm {

	final int STRONG = 2; // constants

	final int WEAK = 1;

	final int NONE = 0;

	private int I_STRATEGY = 0;

	private int J_STRATEGY = 1;

	private NormalFormGame nfg = null;

	/**
	 * Computes all dominated Strategies of all players, for each player
	 * generates a list of all combinations of strategies of size 2 and finds
	 * eigther one is strongly or weakly dominated strategy. If any one is
	 * strongly dominated then removes all combinations which has it.
	 */
	public ResultObject computeDominatedStrategies(NormalFormGame nfg) {
		int[] combination = null;
		int[] status = null;

		LinkedList combinationsList = null;
		ResultObject resultObject = null;
		this.nfg = nfg;

		ArrayList dominatedList = new ArrayList();

		int nPlayers = nfg.getPlayers();
		int[] cardinalities = nfg.getAllCardinalities();

		for (int iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			// System.out.println("iPlayer : " + (iPlayer + 1));
			combinationsList = generateCombinations(cardinalities[iPlayer]);
			while (combinationsList.size() != 0) {

				combination = (int[]) combinationsList.remove(0);
				// System.out.println("Combination : [ " + combination[0] + " ,
				// " + combination[1] + " ]");
				status = isDominatedStrategy(combination, iPlayer,
						cardinalities, nPlayers);
				addToList(status, iPlayer, combination, dominatedList);
				/*
				 * if a strategy is strongly dominated, then there is no need to
				 * check with other combinations...
				 */
				if (status[I_STRATEGY] == STRONG)
					removeDominatedStrategy(combination[I_STRATEGY],
							combinationsList);
				else if (status[J_STRATEGY] == STRONG)
					removeDominatedStrategy(combination[J_STRATEGY],
							combinationsList);
			}
		}

		resultObject = getResults(dominatedList);

		return resultObject;
	}

	/**
	 * Removes the all combinations from the combination list which has
	 * dominated strategy
	 * 
	 * @param strategy :
	 *            the dominated strategy
	 * @param list :
	 *            combination list
	 */
	private void removeDominatedStrategy(int strategy,
			LinkedList combinationList) {
		int[] combination = null;
		Iterator it = combinationList.iterator();
		while (it.hasNext()) {
			combination = (int[]) it.next();
			if ((combination[0] == strategy) || (combination[1] == strategy))
				it.remove();
		}
		return;
	}

	/**
	 * It compares the two strategies represented in combination array of i'th
	 * player. It checks whether any strategy has dominated and returns the
	 * status array
	 * 
	 * @param combination :
	 *            A array of two strategies of i'th player
	 * @param iPlayer :
	 *            i'th player in the game
	 * @param cardinalities :
	 *            No. of strategies of each player
	 * @param nPlayers :
	 *            No. of players
	 * 
	 * @return status array which represents which behaviour of two strategies
	 */
	private int[] isDominatedStrategy(int[] combination, int iPlayer,
			int[] cardinalities, int nPlayers) {
		int i;
		double iUtility, jUtility;
		int[] status = new int[2];
		int[] profile = new int[nPlayers];

		while (true) {

			profile[iPlayer] = combination[I_STRATEGY];
			iUtility = nfg.getUtility(profile, iPlayer);
			profile[iPlayer] = combination[J_STRATEGY];
			jUtility = nfg.getUtility(profile, iPlayer);

			// printProfile(profile);

			if (iUtility > jUtility) {
				// if previously strategy 'j' is set as weak
				if (status[J_STRATEGY] == WEAK)
					status[I_STRATEGY] = NONE;
				// if previously strategy 'j' is set as none
				else if (status[J_STRATEGY] == NONE)
					status[J_STRATEGY] = STRONG;
				if (status[I_STRATEGY] == STRONG || status[I_STRATEGY] == WEAK) {
					status[I_STRATEGY] = NONE;
					status[J_STRATEGY] = NONE;
					break;
				}
			} else if (iUtility == jUtility) {
				if (status[I_STRATEGY] == NONE && status[J_STRATEGY] == NONE) {
					status[I_STRATEGY] = WEAK;
					status[J_STRATEGY] = WEAK;
				}
			} else {

				if (status[I_STRATEGY] == WEAK)
					status[J_STRATEGY] = NONE;
				else if (status[I_STRATEGY] == NONE)
					status[I_STRATEGY] = STRONG;
				if (status[J_STRATEGY] == STRONG || status[J_STRATEGY] == WEAK) {
					status[I_STRATEGY] = NONE;
					status[J_STRATEGY] = NONE;
					break;
				}
			}

			for (i = 0; i < nPlayers; i++) {
				if (i != iPlayer) {
					profile[i]++;
					if (profile[i] == cardinalities[i]) {
						profile[i] = 0;
						if (i == (nPlayers - 1)
								|| (iPlayer == (nPlayers - 1) && i == (nPlayers - 2))) {
							return status;
						}
						continue;
					}
					break;
				}
			}
		}
		return status;
	}

	
	// Testing... 
/*
	private void printProfile(int[] profile) { 
		int i;
		
		System.out.print("Profile [ "); 
		for(i = 0; i < profile.length; i++) {
			System.out.print(profile[i] +" "); 
		} 
		System.out.println("]"); 
	}
*/
	// TODO: comments and finishing
	public LinkedList generateCombinations(int n) {
		int no, top = -1;
		int[] combination = null;
		LinkedList list = new LinkedList();
		int[] stack = new int[2];
		stack[++top] = 0;
		stack[++top] = 1;
		combination = (int[]) stack.clone();
		list.addLast(combination);

		while (top != -1) {
			no = stack[top--];
			if (no < n)
				while (top < 1 && no < n - 1)
					stack[++top] = ++no;
			else
				top--;
			if (top == 1) {
				combination = new int[2];
				combination[0] = stack[0];
				combination[1] = stack[1];
				list.addLast(combination);
			}
		}
/*		
		for(int i=0;i<list.size();i++) { 
			int[] c = (int[])list.get(i);
			System.out.println("["+c[0]+"," +c[1] +"]"); 
		}
*/
		return list;
	}

	/**
	 * Checks dominated strategies of a i'th player and stores the result into
	 * DominatedStrategy object's list
	 * 
	 * @param status :
	 *            status
	 * @param iPlayer :
	 *            i'th player
	 * @param combination :
	 *            two strategies set
	 */
	private void addToList(int[] status, int iPlayer, int[] combination,
			ArrayList dominatedList) {
		if ((status[I_STRATEGY] == WEAK && status[J_STRATEGY] == WEAK)
				|| status[I_STRATEGY] == NONE && status[J_STRATEGY] == NONE)
			return;

		DominatedStrategy dominated = new DominatedStrategy();
		dominated.setPlayer(iPlayer);

		if (status[I_STRATEGY] == STRONG) {
			dominated.setStrategy(combination[J_STRATEGY]);
			dominated.setDominatedStrategy(combination[I_STRATEGY]);
			dominated.setStronglyDominated(true);
			/*
			 * System.out.println("\tPlayer "+(iPlayer+1)+": Strategy '" +
			 * (combination[I_STRATEGY]+1) + "' Strongly Dominated by '" +
			 * (combination[J_STRATEGY]+1) + "'");
			 */

		} else if (status[I_STRATEGY] == WEAK) {
			dominated.setStrategy(combination[J_STRATEGY]);
			dominated.setDominatedStrategy(combination[I_STRATEGY]);
			dominated.setStronglyDominated(false);
			/*
			 * System.out.println("\tPlyaer "+(iPlayer+1)+": Strategy '" +
			 * (combination[I_STRATEGY]+1) + "' Weakly Dominated by '" +
			 * (combination[J_STRATEGY]+1) + "'");
			 */
		} else if (status[J_STRATEGY] == STRONG) {
			dominated.setStrategy(combination[I_STRATEGY]);
			dominated.setDominatedStrategy(combination[J_STRATEGY]);
			dominated.setStronglyDominated(true);
			/*
			 * System.out.println("\tPlayer "+(iPlayer+1)+": Strategy '" +
			 * (combination[J_STRATEGY]+1) + "' Strongly Dominated by '" +
			 * (combination[I_STRATEGY]+1) + "'");
			 */
		} else if (status[J_STRATEGY] == WEAK) {
			dominated.setStrategy(combination[I_STRATEGY]);
			dominated.setDominatedStrategy(combination[J_STRATEGY]);
			dominated.setStronglyDominated(false);
			/*
			 * System.out.println("\tPlyaer "+(iPlayer+1)+": Strategy '" +
			 * (combination[J_STRATEGY]+1) + "' Weakly Dominated by '" +
			 * (combination[I_STRATEGY]+1) + "'");
			 */
		}

		dominatedList.add(dominated);
		return;
	}

	private ResultObject getResults(ArrayList dominatedList) {

		StringBuffer stream = new StringBuffer();
		MiscResultObject resultObject = new MiscResultObject();
		DominatedStrategy dominatedStrategy = null;

		stream.append("Game Name : " + nfg.getGameName() + "\n\n");

		if (dominatedList.size() == 0) {
			stream.append("\tThere are no Dominated Strategies \n");
		}

		Iterator iterator = dominatedList.iterator();

		while (iterator.hasNext()) {
			dominatedStrategy = (DominatedStrategy) iterator.next();
			stream.append("\t Player " + (dominatedStrategy.getPlayer() + 1)
					+ "\n");
			stream.append("\t\t Strategy "
					+ (dominatedStrategy.getDominatedStrategy() + 1));
			stream.append(" is dominated by strategy "
					+ (dominatedStrategy.getStrategy() + 1));
			stream.append("\n\n");
		}

		resultObject.setResults(stream);

		return resultObject;
	}

}
