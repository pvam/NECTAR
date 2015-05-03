/**
 * 
 */
package algorithms;

/**
 * @author kalyan
 * 
 */
public class MiscResultObject implements ResultObject {

	StringBuffer stream = null;

	public void setResults(StringBuffer stream) {
		this.stream = stream;
	}

	public String getResults() {
		return this.stream.toString();
	}
}
