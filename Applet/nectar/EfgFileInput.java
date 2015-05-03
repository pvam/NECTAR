/**
 * 
 */
package nectar;

import games.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.StringTokenizer;

/*
 * @author kalyan
 * 
 */
public class EfgFileInput implements Input {

	private String fileName = null;

	public EfgFileInput(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * This functions generates extensive form object from the file
	 * 
	 * @return extensive form game
	 */
	public Object readGame() throws Exception {
		ExtensiveFormGame efg = null;
		BufferedReader br = null;
		String buffer = null;
		StringTokenizer st = null;
		String subString = null;
		int nPlayers, iPlayer;

		try {

			FileReader reader = new FileReader(this.fileName);
			br = new BufferedReader(reader);

			// read single line from the buffer
			buffer = br.readLine();
			st = new StringTokenizer(buffer);

			subString = st.nextToken();
			subString = st.nextToken();

			// getting no of players of the game
			nPlayers = Integer.parseInt(subString);
			efg = new ExtensiveFormGame(nPlayers);

			subString = st.nextToken();
			st.nextToken("\"");
			subString = st.nextToken();
			// System.out.println("Game Name : " + subString);

			// getting the Name of the Game
			efg.setGameName(subString);

			subString = st.nextToken(" ");
			while (!subString.equals("{")) {
				subString = st.nextToken();
			}

			// getting each player of the game
			if (subString.equals("{")) {
				for (iPlayer = 1; iPlayer <= nPlayers; iPlayer++) {

					subString = getNextString(st);
					// System.out.println("subString : " + subString);
					efg.setPlayerName(iPlayer, subString);
				}
			}

			buffer = br.readLine();
			while (!buffer.equals("")) {
				buffer = br.readLine();
			}

			efg.readGame(br);

			return efg;
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage() == null) {
				throw new Exception("Error: The input format is not matching");
			}
			throw new Exception("Error: " + e.getLocalizedMessage());
		}
	}

	/**
	 * gets the next string from the tokenizer
	 * 
	 * @param st :
	 *            string tokenizer
	 * @return next string
	 * @throws Exception
	 */
	private String getNextString(StringTokenizer st) throws Exception {

		String string = null;
		String subString = null;

		string = st.nextToken();

		while (string.charAt(string.length() - 1) != '"') {
			subString = st.nextToken();
			string = string.concat(" ");
			string = string.concat(subString);
		}

		return string;
	}

	public Object readGame(InputStream in) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object readGame(Reader rd) {
		// TODO Auto-generated method stub
		return null;
	}

}
