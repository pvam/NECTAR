/**
 * 
 */
package nectar;

import java.io.*;

/**
 * @author kalyan
 * 
 * This is common interface to the input objects
 */
public interface Input {

	public Object readGame() throws Exception;

	public Object readGame(InputStream in) throws Exception;

	public Object readGame(Reader rd) throws Exception;

}
