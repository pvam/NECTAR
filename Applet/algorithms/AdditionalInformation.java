/**
 * 
 */
package algorithms;

/**
 * @author kalyan
 * 
 */
public class AdditionalInformation {

	public byte FORMULATION1 = 1;

	public byte FORMULATION2 = 2;

	public byte FORMULATION3 = 4;

	public byte FORMULATION4 = 8;

	public byte ONE_NASH = 1;

	public byte ALL_NASH = 2;

	private byte ZERO = 0;

	private byte formulation;

	private byte type;

	/**
	 * sets the formulation
	 * 
	 * @param iFormulation :
	 *            i'th formulation
	 */
	public void setFormulation(byte iFormulation) {
		// OR operation
		this.formulation |= iFormulation;
	}

	/**
	 * It checks the particular formulation is set or not
	 * 
	 * @param iFormulation :
	 *            i'th formulation
	 * @return if sets returns true else returns false
	 */
	public boolean isFormulatinSet(byte iFormulation) {
		int value = this.formulation & iFormulation;

		if (value != ZERO) {
			return true;
		} else {
			return false;
		}
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return this.type;
	}

	public boolean isType(byte type) {
		if (this.type == type) {
			return true;
		} else {
			return false;
		}
	}

}
