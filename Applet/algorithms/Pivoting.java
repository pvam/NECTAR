/**
 * 
 */

package algorithms;

/**
 * @author kalyan
 * 
 */
public class Pivoting {

	final double INFINITY = 10000000.0;

	final int NOT_EXIST = -1;

	protected double[][] matrix = null;

	protected int[] base = null;

	protected int[] coBase = null;

	protected int[] row = null;

	protected int[] column = null;

	protected int rows;

	protected int columns;

	protected double determinent;

	protected double[][] inverseBaseMatrix = null;

	protected int previousVariable;

	public Pivoting() {

	}

	/**
	 * select the pivot index i.e. base variable index and cobase variable index
	 * 
	 * @param columnIndex
	 *            index of cobase variable
	 * 
	 * @return base and cobase variable indices
	 * 
	 * @throws Exception
	 */
	protected Index selectPivot(int columnIndex) throws Exception {
		Index index = null;
		int i;

		// finds the r of base
		i = lexMinRatio(columnIndex);

		// index.i = i;
		index = new Index(i, columnIndex);

		return index;
	}

	/**
	 * This method performs lexicographic minmum ratio test and returns the
	 * positon of base variable which is going to be removed by pivot operation
	 * 
	 * @param sIndex
	 *            actual column position of co-base variable
	 * @return position of base variable
	 */
	private int lexMinRatio(int sIndex) throws Exception {
		int i;
		int[] minArray = new int[rows];

		computeInverseBaseMatrix();

		// zero'th column checking
		findMinRatio(sIndex, minArray);
		// minArray[0] contains the no.of minimum ratios
		if (minArray[0] == 0) {
			throw new Exception("Can not be Solved");
		}

		if (minArray[0] == 1) {
			return minArray[1];
		}

		// here we have to check until find the unique
		// element. rows represents no. of columns in
		// the inverse base matrix

		// printMatrix();
		// printVariables();
		// System.out.println("Column Index " + sIndex);

		for (i = 0; i < rows; i++) {
			findMinRatio(sIndex, minArray, i);
			if (minArray[0] == 1)
				return minArray[1];
		}

		throw new Exception("Found Bug!!! check the lexMinRatio method");
	}

	/**
	 * (Method Overloading) This computes minimum ratios of a zero column of
	 * DMatrix and stores the result into minArray minArray[0] = no. of minimum
	 * ratios minArray[1...] = list of indices of minimum ratios
	 * 
	 * @param sIndex
	 *            actual column position of co-base variable
	 * @param minArray
	 *            contains the list minimum ratios
	 */
	protected void findMinRatio(int sIndex, int[] minArray) {
		double minimum = INFINITY;
		double ratio;
		int count = 0;
		int i;

		// printMatrix(matrix, determinent);
		// System.out.println("sIndex ; " + sIndex + "Matrix element [0][sIndex]
		// " + (-matrix[0][sIndex]));

		for (i = 0/* start */; i < rows; i++) {

			if ((-matrix[i][sIndex]) > 0) {

				ratio = matrix[i][columns - 1] / (-matrix[i][sIndex]);
				// System.out.println("Ratio : " + ratio);
				if (minimum >= ratio) {
					if (minimum == ratio) {
						minArray[++count] = i;
					} else {
						minimum = ratio;
						count = 1;
						minArray[count] = i;
					}
				}
			}
		}
		// System.out.println("Minimum ratio ; "+ minimum);
		minArray[0] = count;

		return;
	}

	/**
	 * (Method Overloading) This computes minimum ratios of a specified column
	 * of DMatrix and stores result in minArray minArray[0] = no. of minimum
	 * ratios minArray[1...] = list of indices of minimum ratios
	 * 
	 * @param sIndex
	 *            actual position of co-base variable
	 * @param minArray
	 *            contains the list minimum ratios
	 * @param columnNo
	 *            column no of DMatrix
	 */
	protected void findMinRatio(int sIndex, int[] minArray, int columnNo) {
		double minimum;
		double ratio;
		int count = 1;
		int i;
		int iterations = minArray[0];

		int index;

		index = getBaseIndex(columnNo);
		// System.out.println("Variable " + columnNo + " Index " + index);
		if (index == NOT_EXIST) {
			index = getCoBaseIndex(columnNo);

			minimum = inverseBaseMatrix[minArray[1]][columnNo]
					/ ((-matrix[minArray[1]][sIndex]) / determinent);
			// minimum = matrix[minArray[1]][column[index]] /
			// matrix[minArray[1]][sIndex];
			for (i = 2; i <= iterations; i++) {
				ratio = inverseBaseMatrix[minArray[i]][columnNo]
						/ ((-matrix[minArray[i]][sIndex]) / determinent);
				// ratio = matrix[minArray[i]][column[index]] /
				// matrix[minArray[i]][sIndex];
				if (minimum >= ratio) {
					if (minimum == ratio) {
						minArray[++count] = minArray[i];
					} else {
						count = 1;
						minArray[count] = minArray[i];
					}
				}
			}

			minArray[0] = count;
		} else {
			for (i = 1; i <= iterations; i++) {
				if (minArray[i] == row[index]) {
					for (; i < iterations; i++) {
						minArray[i] = minArray[i + 1];
					}
					minArray[0]--;
				}
			}
		}

		return;
	}

	/**
	 * It computes Inverse Matrix
	 */
	protected void computeInverseBaseMatrix() {
		int i, j;
		int index;

		// here variable i = x_i
		for (i = 1; i <= rows; i++) {
			// updating inverse matrix column by column

			// if x_i is base variable then it returns position
			// else returns -1
			index = getBaseIndex(i);

			if (index == NOT_EXIST) {
				// x_i is not base variable
				// copies the corresponding column in Matrix[][cobase]
				for (j = 0; j < rows; j++) {
					// get the position of x_i
					index = getCoBaseIndex(i);

					// inverseBaseMatrix[j][i] = matrix[row[j]][column[index]];
					inverseBaseMatrix[j][i - 1] = -matrix[j][column[index]]
							/ determinent;
				}
			} else {
				// x_i is base variable
				// So, i'th column is unit vector
				for (j = 0; j < rows; j++) {
					// all elements are 0 except one place where the position of
					// x_i
					if (row[index] != j) {
						inverseBaseMatrix[j][i - 1] = 0;
					} else {
						inverseBaseMatrix[j][i - 1] = 1;
					}
				}
			}
		}
	}

	/**
	 * does the pivot operation
	 * 
	 * @param index
	 *            indices of base and coBase variables which are going to pivot
	 */
	protected void pivot(Index index) {
		int i, j;
		double temp;

		// updating a[i][j] suchthat (i!=r) and (j!=s)
		for (i = 0; i < rows; i++) {
			if (i != index.i) {
				for (j = 0; j < columns; j++) {
					if (j != index.j) {
						temp = (matrix[i][j] * matrix[index.i][index.j] - matrix[i][index.j]
								* matrix[index.i][j]);
						matrix[i][j] = temp / determinent;
					}
				}
			}
		}

		// updating a[r][j]
		for (j = 0; j < columns; j++) {
			if (j != index.j)
				matrix[index.i][j] = -matrix[index.i][j];
		}

		// updating a[r][s] and determinant(B)
		temp = determinent;
		determinent = matrix[index.i][index.j];
		matrix[index.i][index.j] = temp;

		if (determinent < 0) {

			determinent = -determinent;

			for (i = 0; i < rows; i++) {
				for (j = 0; j < columns; j++) {
					matrix[i][j] = -matrix[i][j];
				}
			}
		}

		// Note: a[i][s] won't change
		return;
	}

	/**
	 * it checks that whether the given varible is present in the base set or
	 * not. if it is there, it returns the corresponding position else -1 (NOT
	 * EXIST)
	 * 
	 * @param variable
	 *            the varible which is going to be checked
	 * @return position of the variable
	 */
	protected int getBaseIndex(int variable) {
		int i;
		for (i = 0; i < rows; i++) {
			if (base[i] == variable) {
				return i;
			}
		}
		return NOT_EXIST;
	}

	/**
	 * it checks that whether the given variable is present in the co-base set
	 * or not. if it is there, it returns the corresponding position else -1
	 * (NOT EXIST)
	 * 
	 * @param variable
	 *            the variable which is going to be checked
	 * @return position of the variable
	 */
	protected int getCoBaseIndex(int variable) {
		int i;

		for (i = 0; i < columns; i++) {
			if (coBase[i] == variable) {
				return i;
			}
		}
		return NOT_EXIST;
	}

	/**
	 * finds the column index of cobase varible
	 * 
	 * @param variable
	 * @return
	 */
	protected int findColumnIndex(int variable) {
		int i;
		int temp;
		int var;

		for (i = 0; i < columns - 1; i++) {
			temp = coBase[i];

			if (temp > rows) {
				var = temp - rows;
			} else {
				var = temp;
			}

			if (var == variable && previousVariable != coBase[i]) {
				return i;
			}
		}
		return NOT_EXIST;
	}

	/**
	 * finds the missed varaible in the basis
	 * 
	 * @return
	 */
	protected int getMissedBaseVariable() {
		int[] test = new int[rows];
		int i, j;

		for (i = 0; i < rows; i++) {
			j = base[i];
			if (j > rows) {
				j = j - rows;
			}
			if (j != 0) {
				test[j - 1] = 1;
			}
		}

		for (i = 0; i < rows; i++) {
			if (test[i] == 0)
				return i + 1;
		}

		return 0;
	}

	/**
	 * swap the base and cobase variables
	 * 
	 * @param index
	 */
	protected void swap(Index index) {
		int temp;

		temp = base[index.i];
		base[index.i] = coBase[index.j];
		coBase[index.j] = temp;

		return;
	}

	// For testing
	/**
	 * displays the list of present base and cobase variables
	 */
	protected void printVariables() {
/*		
		int i;
		
		System.out.print("\nBase {"); 
		for(i=0; i< rows; i++) {
			System.out.print(" "+base[i]); 
		} 
		System.out.print(" }\tCobase {");
		
		for(i=0; i < columns; i++) 
		{ 
			System.out.print(" "+coBase[i]); 
		}
		System.out.println(" }\n");
		 
		return;
*/
	}

	// For testing
	/**
	 * displays the matrix elements
	 */
	protected void printMatrix() {
/*		
		int i,j;
		  
		System.out.println("Matrix Values ::: "); 
		for ( i=0; i < rows; i++) {
			System.out.println(); 
			for(j =0; j < columns; j++) {
				System.out.print(" " + round(matrix[i][j]/determinent)); 
			}
			//System.out.println("\t" + bValues[i]); 
		} 
		System.out.println();
*/
	}
}
