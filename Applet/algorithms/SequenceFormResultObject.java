/**
 * 
 */
package algorithms;

import java.util.ArrayList;
import games.*;

/**
 * @author kalyan
 * 
 */
public class SequenceFormResultObject {

	private int[] base = null;

	private int[] coBase = null;

	private double[] bValues = null;

	private SequenceFormGame sfg = null;

	private String algorithmName = null;

	public void setBase(int[] base) {
		this.base = base;
	}

	public int[] getBase() {
		return this.base;
	}

	public void setCoBase(int[] coBase) {
		this.coBase = coBase;
	}

	public int[] getCoBase() {
		return this.coBase;
	}

	public void setBValues(double[] bValues) {
		this.bValues = bValues;
	}

	public double[] getBValues() {
		return this.bValues;
	}

	public void setSequenceFormGame(SequenceFormGame sfg) {
		this.sfg = sfg;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getResult() throws Exception {
		int j, k, l;
		int iPlayer, nPlayers, rows, sequenceNumber;
		int[] sequences = null;
		int[] base = null;
		
		double[] bValues = null;
		double[] realizationProbabilities = null;

	    ArrayList list = new ArrayList(2);
		double[] x = null;

		StringBuffer stream = new StringBuffer();

		sequences = sfg.getSequences();
		nPlayers = sfg.getPlayers();
		
		base = getBase();
		bValues = getBValues();
		rows = base.length;

		stream.append("AlgorithmName : " + this.algorithmName + "\n");
		stream.append("Game Name : " + sfg.getGameName() + "\n\n");

		stream.append(":: Behaviour Strategies :: \n\n");

		k = rows + 1;

		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
			realizationProbabilities = new double[sequences[iPlayer]];
			
			//stream.append("Player " + (iPlayer + 1) + "\n");
			x = new double[sequences[iPlayer]];
			for (j = 0; j < sequences[iPlayer]; j++, k++) {

				int flag = 0;
				for (l = 0; l < base.length; l++) {
					if (k == base[l]) {
						sequenceNumber = k - (iPlayer * sequences[0])
								- (rows + 1);
						realizationProbabilities[sequenceNumber] = bValues[l];
						//stream.append("Sequence " + sequenceNumber + " -  " + roundValue(bValues[l]));
						//stream.append("\tMove : " + sfg.getMoveName(iPlayer, sequenceNumber) + "\n");
						
						x[j] = bValues[l];
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					sequenceNumber = k - (iPlayer * sequences[0]) - (rows + 1);
					realizationProbabilities[sequenceNumber] = 0;
					//stream.append("Sequence " + sequenceNumber + " -  0");
					//stream.append("\t\tMove : " + sfg.getMoveName(iPlayer, sequenceNumber) + "\n");
					x[j] = 0;
				}
			}
			sfg.getBehaviourStrategy(iPlayer, realizationProbabilities).display(stream);	
			list.add(iPlayer, x);
			stream.append("\n");
		}

		double expectedUtility;
		double[] temp = new double[sequences[0]];
		double[][] utilityMatrix = null;
		double[] y = null;

		double[][] chanceMatrix = sfg.getchanceMatrix();

		stream.append(":: Expected Utilities :: \n\n");
		
		for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {

			utilityMatrix = sfg.getUtilityMatrix(iPlayer);
			y = (double[]) list.get(1);

			for (j = 0; j < sequences[0]; j++) {
				temp[j] = 0;
				for (k = 0; k < sequences[1]; k++) {
					temp[j] = temp[j] + chanceMatrix[j][k]
							* utilityMatrix[j][k] * y[k];
				}
			}

			x = (double[]) list.get(0);
			expectedUtility = 0;
			for (j = 0; j < sequences[0]; j++) {
				expectedUtility = expectedUtility + x[j] * temp[j];
			}
					
			stream.append("\tPlayer  " + (iPlayer + 1) + "  expected utility : "
					+ roundValue(expectedUtility) + "\n");

		}

		return stream.toString();

	}

	private double roundValue(double x) {
		x = x * 1000;
		x = Math.round(x);
		x = ((double) x) / 1000;
		return x;
	}
}
