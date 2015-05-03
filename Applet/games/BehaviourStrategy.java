/**
 * 
 */
package games;

import java.util.*;

/**
 * @author kalyan
 * 
 */
public class BehaviourStrategy {

	private int iPlayer;

	private ExtensiveFormGame efg = null;

	private ArrayList[] movesLists = null;

	private ArrayList[] probsLists = null;

	public BehaviourStrategy(int iPlayer, ExtensiveFormGame efg) {
		this.iPlayer = iPlayer;
		this.efg = efg;
		this.initialize();
	}

	/**
	 * It initializes all the variables
	 */
	private void initialize() {

		int totalSets, iSet;
		InformationSet infoSet = null;
		TreeNode treeNode = null;

		ArrayList informationSetList = efg.getInformationSets(iPlayer);

		totalSets = informationSetList.size();

		movesLists = new ArrayList[totalSets];
		probsLists = new ArrayList[totalSets];

		for (iSet = 0; iSet < totalSets; iSet++) {
			movesLists[iSet] = new ArrayList();
			probsLists[iSet] = new ArrayList();
		}

		for (iSet = 0; iSet < totalSets; iSet++) {
			infoSet = (InformationSet) informationSetList.get(iSet);
			treeNode = infoSet.getTreeNode(0);
			movesLists[iSet] = treeNode.getMovesList();
		}
	}

	/**
	 * It sets the probability on a particular move in the given information set
	 * 
	 * @param informationSetNumber :
	 *            information set number
	 * @param probability :
	 *            probability on a move
	 * @param moveName :
	 *            move name
	 * @throws Exception
	 */
	public void setProbability(int informationSetNumber, double probability,
			String moveName) throws Exception {

		int iMove;

		ArrayList movesList = movesLists[informationSetNumber - 1];

		for (iMove = 0; iMove < movesList.size(); iMove++) {
			if (movesList.get(iMove).equals(moveName)) {
				// System.out.println("Player : " + (iPlayer + 1) + "\tInfo Set
				// : " + informationSetNumber + "\tMove : " + moveName);
				probsLists[informationSetNumber - 1].add(iMove, new Double(
						probability));
				return;
			}
		}

		throw new Exception("Bug!! Bug!! inform to the Author");
	}

	/**
	 * It displays the probabilities on moves in the specified stream
	 * 
	 * @param stream :
	 *            buffered stream
	 */
	public void display(StringBuffer stream) {
		int iSet, iMove;

		stream.append("Player " + (iPlayer + 1) + "\n\n");

		for (iSet = 0; iSet < movesLists.length; iSet++) {

			stream.append("\tInformation Set " + (iSet + 1) + "\n\n");
			for (iMove = 0; iMove < movesLists[iSet].size(); iMove++) {
				stream.append("\t\t"
						+ movesLists[iSet].get(iMove)
						+ "\t->\t"
						+ roundValue(((Double) probsLists[iSet].get(iMove))
								.doubleValue()) + "\n");
			}
			stream.append("\n");
		}
	}

	/**
	 * It rounds the double value
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
