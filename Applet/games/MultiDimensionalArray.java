/**
 * 
 */
package games;

/**
 * @author kalyan
 * 
 */
public class MultiDimensionalArray {

	private int[] dimensions = null;

	/*
	 * for finding offset on single dimension array which represents
	 * multi-dimensions.
	 */
	private int[] factors = null;

	private double[] data = null;

	public MultiDimensionalArray(int[] dimensions) {
		this.dimensions = new int[dimensions.length];
		factors = new int[dimensions.length];
		int product = 1;
		for (int i = dimensions.length - 1; i >= 0; --i) {
			this.dimensions[i] = dimensions[i];
			factors[i] = product;
			product *= this.dimensions[i];
		}
		data = new double[product];
	}

	/**
	 * Gives the data element at the specified location
	 * 
	 * @param indices :
	 *            location in a multidimensional array
	 * 
	 * @return data element at the location
	 */
	public double getDataElement(int[] indices) {
		int offset = getOffset(indices);
		return this.data[offset];
	}

	/**
	 * Sets the data element at the specified location
	 * 
	 * @param indices :
	 *            location in a muliti-dimensional array
	 * @param dataElement :
	 *            data element
	 */
	public void setDataElement(int[] indices, double dataElement) {
		int offset = getOffset(indices);
		this.data[offset] = dataElement;
		return;
	}

	/**
	 * Finds the offset of single dimension array by taking all indices of n
	 * dimensions. Simply calculates a profile position
	 * 
	 * @param indices :
	 *            indices of all n dimensions
	 * 
	 * @return position of strategy profile in one dimensional array
	 */
	public int getOffset(int[] indices) {
		if (indices.length != dimensions.length)
			throw new IllegalArgumentException(
					"Indices Array size is not equal to No. of players");
		int offset = 0;
		for (int i = 0; i < dimensions.length; i++) {

			// System.out.println("indices["+i+"] " + indices[i] );
			if ((indices[i] < 0) || (indices[i] >= dimensions[i]))
				throw new IndexOutOfBoundsException("Array out of Bound Error");
			offset += factors[i] * indices[i];
		}
		return offset;
	}

}
