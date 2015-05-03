/**
 * 
 * 
 */
package nectar;

import games.*;

import java.io.*;
import java.util.StringTokenizer;

/**
 * @author kalyan
 * 
 */
public class NfgFileInput implements Input {

	NormalFormGame nfg = null;

	String fileName = null;

	public NfgFileInput(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * reads the game from the file
	 */
	public Object readGame() throws Exception {
		int i;
		int nPlayers = 0;
		int[] cardinalities = null;

		String gameName = null;
		String buffer = null;
		String str = new String();
		StringTokenizer st;

		try {

			Reader rd = new FileReader(fileName);

			BufferedReader br = new BufferedReader(rd);

			buffer = br.readLine();
			gameName = br.readLine();

			while (true) {
				buffer = br.readLine();
				if (buffer == null) {
					throw new Exception("Warning: Invalid input format");
				}
				st = new StringTokenizer(buffer);

				if (st.hasMoreTokens()) {
					str = st.nextToken();

					if (str.equals("Players:")) {
						break;
					}
				}
			}

			// getting information about players
			str = st.nextToken();
			nPlayers = Integer.parseInt(str);
			cardinalities = new int[nPlayers];

			// getting information about of number of strategies of each player
			buffer = br.readLine();
			st = new StringTokenizer(buffer);
			if (st.hasMoreTokens()) {
				str = st.nextToken();
			}
			if (str.equals("Actions:")) {
				i = 0;
				while (st.hasMoreTokens()) {
					str = st.nextToken();
					cardinalities[i++] = Integer.parseInt(str);
				}
				if (i != nPlayers) {
					throw new Exception(
							"Warning: Invalid Actions in given input file");
				}
			}

			// initilizing the game object
			nfg = GameFactory.getNormalFormGame(nPlayers, cardinalities);

			// nfg.setGameName(new String(gameName) + "");
			nfg.setGameName(gameName);
			while (true) {
				buffer = br.readLine();
				if (buffer.length() == 0) {
					break;
				}
			}

			buffer = br.readLine();

			// System.out.println ("creating normal form game");
			addUtilitiesToGame(buffer);
/*			
			System.out.println ("\n\nNo.of Players : " + nPlayers);
			System.out.println ("No. of Strategies of Each Player :\n"); 
			for (i=0; i< nPlayers; i++) { 
				System.out.println ("\tPlayer" + (i+1) + "---> " +cardinalities[i]); 
			} 
			System.out.println("\n\n");
			System.out.println("successfully updated game");
*/			 
			return nfg;

		} catch (Exception e) {
			// e.printStackTrace();
			throw new Exception("Error: " + e.getLocalizedMessage());
		}

	}

	/**
	 * add utilities to the normal form game
	 * 
	 * @param buffer :
	 *            string buffer
	 * @throws Exception
	 */
	private void addUtilitiesToGame(String buffer) throws Exception {

		int[] cardinalities = nfg.getAllCardinalities();
		int nPlayers = nfg.getPlayers();
		int[] indices = new int[nPlayers];
		StringTokenizer st = new StringTokenizer(buffer);
		int iPlayer;
		Double utility = null;

		while (indices[nPlayers - 1] < cardinalities[nPlayers - 1]) {

			for (indices[0] = 0; indices[0] < cardinalities[0]; indices[0]++) {
				for (iPlayer = 0; iPlayer < nPlayers; iPlayer++) {
					utility = new Double(st.nextToken());
					nfg.setUtility(indices, iPlayer, utility.doubleValue());
				}
				// printIndices(indices);
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

		if (st.hasMoreTokens()) {
			throw new Exception("Over Data");
		}
		return;
	}
/*
	private void printIndices(int[] indices) { 
		int i;
	 
		for( i = 0; i < indices.length; i++) { 
			System.out.print(" " +indices[i]); 
		} 
		System.out.println(); 
	}
*/	

	/*
	 * (non-Javadoc)
	 * 
	 * @see nectar.Input#readGame(java.io.Reader)
	 */
	public Object readGame(Reader rd) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nectar.Input#readGame(java.io.Reader)
	 */
	public Object readGame(InputStream in) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
