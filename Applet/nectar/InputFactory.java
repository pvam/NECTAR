/**
 * 
 */
package nectar;

/**
 * @author kalyan
 * 
 */
public class InputFactory {

	public static Input getInput(String gameType, String fileName)
			throws Exception {
		if (gameType.equals("extensive")) {
			return new EfgFileInput(fileName);
		} else if (gameType.equals("normal")) {
			return new NfgFileInput(fileName);
		} else {
			throw new Exception("Invalid Parameter");
		}
	}
}
