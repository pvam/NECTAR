/**
 * 
 */
package algorithms;

import java.util.ArrayList;

/**
 * @author kalyan and keyur
 * 
 * Algorithm Paper : "A revised implementation of the reverse searc vertex
 * enumeration algorithm", D. Avis. (Technical report)
 */
public class VertexEnumeration extends Pivoting {

	private int start;

	private ArrayList list = null;

	/**
	 * This method initializes all the data structures according to the given
	 * parameters
	 * 
	 * @param no
	 *            No of linear inequations
	 * @param d
	 *            No od decision variables
	 */
	private void initialize(int no, int d) {
		int i, j;

		columns = d + 1;
		rows = no + 1;
		start = columns;
		determinent = 1.0;

		// base and cobase stores the variables
		base = new int[rows];
		coBase = new int[columns];

		// row and column stores the indices of columns
		row = new int[rows];
		column = new int[columns];

		// matrix is a (m+1)X(d+1) size
		// zero'th row is objective function
		// zero'th column is b' values
		matrix = new double[rows][columns];
		// inverseBaseMatrix = new double[rows][rows];

		// initializing base and cobase
		for (i = 0; i < rows; i++) {
			base[i] = i;
			row[i] = i;
		}

		// initializing co-base and column
		for (j = 0; j < columns; j++, i++) {
			coBase[j] = i;
			column[j] = j + 1;
		}
		column[j - 1] = 0;
	}

	/**
	 * input : array, represent the m equation given by users in d dimension (m)
	 * x (d+1) is the dimension for the array x_{d+i} = b_i + \sigma a_{i,j}.x_j
	 */
	public ArrayList computeVertexEnumeration(double[][] array)
			throws Exception {
		int i, j, k, l;
		double temp;
		int count = 0;
		int flag;

		list = new ArrayList();
		int m = array.length;
		int d = array[0].length - 1;
		Index index = new Index();
		int[] base = new int[d];

		initialize(m, d);
/*
		System.out.println(" No. of equations = " + m + "   Dimensions = " + d);
		System.out.println("The Input");
		printArray(array);
*/

		// convert the equations from the form x_{d+i} = b_i + \sigma
		// a_{i,j}.x_j i=1...m,j=1,...d
		// to the form x_{i} = b'_i + \sigma a'_{i,j}.x_j i=1,..m,j=m+1,...m+d
		// prepivot each variable x_i with x_m+i , i=1....d

		// For each decision variable, coz, they are in cobase right now
		for (k = d; k > 0; k--) {
			// Finding suitable raw from the array to pivot it with the dicision
			// variable
			for (i = m - 1; i >= 0; i--) {
				flag = 0;
				// Check whether this row is not corresponding to decision
				// variable
				for (j = 0; j < d; j++) {
					if (base[j] == i) {
						flag = 1;
						break;
					}
				}
				// If this is the row correspondig to decision variable then we
				// can not pivot it
				if (flag == 1)
					continue;
				// This row is suitable for pivot if the pivotal element is not
				// zero!!
				if (array[i][k] != 0) {
					// pivoting
					index = new Index(i, k);
					for (l = 0; l < m; l++) {
						if (l != index.i) {
							for (j = 0; j < d + 1; j++) {
								if (j != index.j) {
									temp = (array[l][j]
											* array[index.i][index.j] - array[l][index.j]
											* array[index.i][j]);
									array[l][j] = temp / determinent;
								}
							}
						}
					}
					// updating a[r][j]
					for (j = 0; j < columns; j++) {
						if (j != index.j)
							array[index.i][j] = -array[index.i][j];
					}
					// updating a[r][s] and determinant(B)
					temp = determinent;
					determinent = array[index.i][index.j];
					array[index.i][index.j] = temp;
					// if determinent is negative then make it positive!
					if (determinent < 0) {
						for (l = 0; l < m; l++) {
							for (j = 0; j < d + 1; j++) {
								temp = (-1) * array[l][j];
								array[l][j] = temp;
								// System.out.print(" "+array[i][j]);
							}
							// System.out.println(" :");
						}
						determinent = -determinent;
					}
					// Now the decision variable corresponding to column k , is
					// being pivoted! so break.
					base[count++] = i;
					break;
				}
			}
		}

		if (count != d) {
			throw new Exception(
					"computeVertexEnumeration():: Can not be solved, Because each decision variable can not be expressed in terms of slack variable");
		}
		//System.out.println("Decision variables are defined in terms of slack variables...");
		//printArray(array);
		
		initializeMatrix(array, base);

		// intial basis matrix is optimal , so adding initial vertex into
		// vertices list
		double[] result = new double[columns - 1];
		for (k = 1; k < columns; k++) {
			result[k - 1] = matrix[k][0] / determinent;
		}

		list.add(result);

/*
		System.out.println("Initial Vertex");
		for (k = 1; k < columns; k++) {
			System.out.println("Variable " + (k + 1) + " -->  " + matrix[k][0]);
		}
*/
		// Calling Vertex Enemuratiion Algorithm
		search();

		return list;
	}

	/**
	 * input - matrix array of dimension (m)x(d+1) x_ = x_ = : : x_ = x_ = x_ = :
	 * x_ = OUTPUT matrix matrix (m+1)x(d+1) x_0 = x_1 = x_2 = : : =
	 */
	public void initializeMatrix(double[][] array, int[] base) throws Exception {
		int i, j, k, l;
		int flag;

		// if any element of the 'b' column is negative, then make it positive
		// by pivoting with some column!!
		prePivot(array, base);

		// initialize row 0 of matrix
		for (i = 0; i < columns; i++)
			matrix[0][i] = -determinent;
		matrix[0][0] = 0;

		// initialize raw 1 to raw d
		for (i = base.length; i > 0; i--) {
			for (j = 0; j < columns; j++) {
				matrix[i][j] = array[base[base.length - i]][j];
			}
		}
		// initialize row, d+1 onwards..
		for (i = 0, l = base.length + 1; i < rows - 1; i++) {

			flag = 0;
			for (k = 0; k < base.length; k++) {
				if (base[k] == i) {
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				for (j = 0; j < columns; j++) {
					matrix[l][j] = array[i][j];
				}
				l++;
			}
		}
		// System.out.println("Matrix Initialized");
		//	printMatrix();
	}

	/**
	 * if any element of the 'b' column is negative, then make it positive by
	 * pivoting with some column!!
	 * 
	 * @param array
	 * @param base -
	 *            map each decision variable to row of the array matrix
	 * @throws Exception
	 */
	private void prePivot(double[][] array, int[] base) throws Exception {

		int i, j;
		int rIndex = -1, cIndex = -1;
		double temp;

		while (true) {
			// get the row containing negative 'b' value
			rIndex = getBNegativeIndex(array, base);

			// if no such row then return
			if (rIndex == NOT_EXIST) {
				return;
			}
			// if the row with negative 'b' is corresponding to decision
			// variable , then its UNABLE to do :-(
			for (i = 0; i < base.length; i++) {
				if (rIndex == base[i]) {
					throw new Exception(
							"prePivot():: Unable to do pivot operation");
				}
			}
			// find suitable column for pivoting
			for (i = 1; i < array[0].length; i++) {
				if (array[rIndex][i] > 0) {
					cIndex = i;
					break;
				}
			}

			// if no column is found , then UNABLE to proceed :-(
			if (i == array[0].length) {
				throw new Exception(
						"prePivot():: Unable to find positive value in a row");
			}

			// pivoting..
			for (i = 0; i < array.length; i++) {
				if (i != rIndex) {
					for (j = 0; j < array[0].length; j++) {
						if (j != cIndex) {
							temp = (array[i][j] * array[rIndex][cIndex] - array[i][cIndex]
									* array[rIndex][j]);
							array[i][j] = temp / determinent;
						}
					}
				}
			}
			// updating a[r][j]
			for (j = 0; j < columns; j++) {
				if (j != cIndex)
					array[rIndex][j] = -array[rIndex][j];
			}
			// updating a[r][s] and determinant(B)
			temp = determinent;
			determinent = array[rIndex][cIndex];
			array[rIndex][cIndex] = temp;

			// System.out.println("Pivot : r = " + rIndex + ", s=" + cIndex);
			// printArray(array);
		}
	}

	/**
	 * 
	 * @param array
	 * @return - the index of the row, containing negative 'b' value
	 */
	private int getBNegativeIndex(double[][] array, int[] base) {
		int i, j;
		int flag = 0;
		for (i = 0; i < array.length; i++) {
			if (array[i][0] < 0) {
				for (j = 0; j < base.length; j++) {
					if (i == base[j])
						flag = 1;
				}
				if (flag == 0)
					return i;
			}
			flag = 0;
		}
		return NOT_EXIST;
	}

	/**
	 * it finds co-base index of a variable which represents column j in the
	 * matrix
	 * 
	 * @param j
	 *            column j of the matrix
	 * @return index of this variable in co-base
	 */
	private int findCobaseVariableIndex(int j) throws Exception {
		int index;

		for (index = 0; index < columns - 1; index++) {

			if (column[index] == j)
				return index;
		}
		
		throw new Exception("Bug!! Check findCobaseVariableIndex()");
	}	

	/**
	 * it finds base index of a variable which represents row i in the matrix
	 * 
	 * @param i
	 *            row i of the matrix
	 * @return index of this variable in base
	 */
	private int findBaseVariableIndex(int i) throws Exception {
		int index;

		// no need to check first start = d+1 rows
		for (index = start; index < rows; index++) {
			if (row[index] == i) {
				return index;
			}
		}
	
		throw new Exception("Bug!! Check findBaseVariableIndex()");
	}	
		
	/**
	 * after pivot operation we have to reorder the base and co-base sets. This
	 * method updates the base and co-base and sort the variable list
	 * 
	 * @param base_i
	 *            base index
	 * @param coBase_j
	 *            co-base index
	 */
	private void updateBaseAndCoBase(int base_i, int coBase_j) {
		int temp;

		temp = base[base_i];
		base[base_i] = coBase[coBase_j];
		coBase[coBase_j] = temp;

		sortVariables(base, row, start);
		sortVariables(coBase, column, 0);
		// printMatrix();
	}

	/**
	 * sort the variables in the list. insertion sort method used here
	 * 
	 * @param array
	 *            either base or co-base sets
	 * @param array1
	 *            either row or column set
	 * @param start
	 *            starting location
	 */
	public void sortVariables(int[] array, int[] array1, int start) {
		int i;
		int value, location;
		int value1;

		for (i = start; i < array.length; i++) {

			value = array[i];
			value1 = array1[i];
			location = i - 1;

			while (location >= start && array[location] > value) {

				array[location + 1] = array[location];
				array1[location + 1] = array1[location];
				location = location - 1;
			}

			array[location + 1] = value;
			array1[location + 1] = value1;
		}
		return;
	}

	/**
	 * This method checks the current basis is lexicographically minimum or not.
	 * If the basis is not a lexicographically minimum means it has to satisfy
	 * three conditions mentioned in paper proposition 5.1.
	 * 
	 * @return returns true if the current basis is lexicographically minimum
	 *         and vice versa.
	 */
	private boolean lexMin() {
		int i, j;
		int r;
		int s;

		// the first 0....d elements always present in the base
		// there fore no need for checking
		for (i = start, j = 0; i < rows; i++) {

			r = base[i];
			s = coBase[j];

			// checking all bases variables with all coBase variables
			while (r > s) {
				// base variable is greater than coBase variable

				// checking the degenerate case
				if (matrix[row[i]][0] == 0) {
					// checking the pivot element, if it is not zero
					// we can get lexicographically minimum basis by
					// pivot operation.
					if (matrix[row[i]][column[j]] != 0) {
						return false;
					}
				}
				j++;
				s = coBase[j];
			}
			j = 0;
		}
		// current basis is lexicographically minimum basis
		return true;
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

		for (i = start; i < rows; i++) {

			if ((-matrix[i][sIndex]) / determinent > 0) {
				ratio = matrix[i][0] / (-matrix[i][sIndex]);
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

			// minimum = inverseBaseMatrix[minArray[1]][columnNo]
			// /((-matrix[minArray[1]][sIndex])/determinent);
			minimum = matrix[minArray[1]][column[index]]
					/ matrix[minArray[1]][sIndex];
			for (i = 2; i <= iterations; i++) {
				// ratio = inverseBaseMatrix[minArray[i]][columnNo] /
				// ((-matrix[minArray[i]][sIndex])/determinent);
				ratio = matrix[minArray[i]][column[index]]
						/ matrix[minArray[i]][sIndex];
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

		// computeInverseBaseMatrix();

		// zero'th column checking
		findMinRatio(sIndex, minArray);
		// minArray[0] contains the no.of minimum ratios
		if (minArray[0] == 1) {
			return minArray[1];
		}

		if (minArray[0] == 0) {
			return 0;
		}

		// here we have to check until find the unique
		// element. rows represents no. of columns in
		// the inverse base matrix
		for (i = 1; i < rows; i++) {

			findMinRatio(sIndex, minArray, i);
			if (minArray[0] == 1)
				return minArray[1];
		}
		throw new Exception("Bug!! Check lexMinRatio method");
	}

	protected Index selectPivot() throws Exception {
		Index index = null;
		int i, j;

		// finds the s of co-base
		for (j = 0; j < columns - 1; j++) {
			if (matrix[0][column[j]] / determinent > 0)
				break;
		}

		if (j == columns - 1) {

			// System.out.println("SelectPivot() failed :: Returning (-1,-1)");
			index = new Index(-1, -1);
			return index;
		}

		// finds the r of base
		i = lexMinRatio(column[j]);

		index = new Index(i, column[j]);

		// System.out.println("In SelectPivot() : Returning  (i,j) = (" + 
		// findBaseVariableIndex(i) + ", " + j + ")");

		return index;
	}

	protected void printMatrix() {

		int i, j;
		System.out.println("\n");

		for (i = 0; i < rows; i++) {
			System.out.println();
			for (j = 0; j < columns; j++)
				System.out.print("  " + (matrix[row[i]][column[j]]));
			// System.out.print(" " + (matrix[i][j]));
		}
		System.out.println("\n Determinent :" + determinent);

		System.out.print("Base (");
		for (i = 0; i < rows; i++) {
			System.out.print("  " + base[i]);
		}
		System.out.print("  )");

		System.out.print("    Co-Base (");
		for (i = 0; i < columns; i++) {
			System.out.print("  " + coBase[i]);
		}
		System.out.println("  )");

		System.out.print("Rows (");
		for (i = 0; i < rows; i++) {
			System.out.print("  " + row[i]);
		}
		System.out.print("  )");

		System.out.print("    Column (");
		for (i = 0; i < columns; i++) {
			System.out.print("  " + column[i]);
		}
		System.out.println("  )");

		System.out.println();

		System.out.println("\n");
	}

	private void printArray(double[][] array) {
		int i, j;
		System.out.println("\n");

		for (i = 0; i < array.length; i++) {
			System.out.println();
			for (j = 0; j < array[0].length; j++)
				System.out.print("  " + (array[i][j]));
		}
		System.out.println("\n Determinent :" + determinent);
		System.out.println("\n");
	}

	/**
	 * 
	 * 
	 * @param sIndex
	 *            actual column position
	 * @return results
	 */
	private Index reverse(int s) throws Exception {
		int i, j;
		Index index = null;
		int r, sIndex, rIndex; // r, s are actual row , column positions
		// rIndex and sIndex are corresponding indices

		// System.out.println("In Reverse() : LexminRatio Returns s ="+
		// this.findCobaseVariableIndex(s) );
		// System.out.println("In Reverse() : LexminRatio Returns s = "+ s);

		if (matrix[0][s] / determinent < 0) {

			r = lexMinRatio(s);

			if (r == 0) {
				// System.out.println("Reverse Failed :: Its Ray");
				return null;
			}

			// System.out.println("In Reverse() : LexminRatio Returns r ="+
			// this.findBaseVariableIndex(r) );
			// System.out.println("In Reverse() : LexminRatio Returns r = "+ r);
			rIndex = findBaseVariableIndex(r);
			sIndex = findCobaseVariableIndex(s);

			for (i = 0; i < columns - 1 && coBase[i] < base[rIndex]; i++) {
				if (i != sIndex) {
					j = column[i];
					if ((matrix[0][j] * matrix[r][s]) < (matrix[0][s] * matrix[r][j])) {
						// System.out.println("In Reverse() : false2");
						return null;
					}
				}
			}

			// System.out.println("In Reverse() : returns r = "+
			// this.findBaseVariableIndex(r));
			// System.out.println("In Reverse() : returns r = "+ r);
			index = new Index(r, s);
			return index;

		}
		// System.out.println("In Reverse() : false1");
		return null;
	}

	public void search() throws Exception {
		int j, i, v;
		Index index = null;
		j = 0;

		// System.out.println("Inside lrsSearch()::");
		// printMatrix();
		
		do {

			while (j < columns - 1) {
				// v is exact column position in matrix
				v = column[j];

				index = reverse(v);

				if (index != null) {

					pivot(index);
					// updates and sorts the base and co-base sets
					i = findBaseVariableIndex(index.i);
					updateBaseAndCoBase(i, j);
					// indication for reverse pivot operation has performed

					// it checks whether current base is lexicographically
					// minimum or not in the case of degenerate case
					if (lexMin()) {

						double[] result = new double[columns - 1];
						
						// System.out.println("Lexmin :: TRUE ");
						// System.out.println("Vertex Found:: ");
						

						for (int k = 1; k < columns; k++) {
							result[k - 1] = matrix[k][0] / determinent;
							// System.out.println("Variable " + (k + 1)
							// 	+ " -->  " + matrix[k][0]);
						}
						// System.out.println();
						list.add(result);
					} else {
						// System.out.println("Lexmin :: FALSE ");
					}
					j = 0;
				} else {
					j = j + 1;
				}
			}

			// System.out.println("Backtraking to previous node :: calling selectpivot()");

			index = selectPivot();

			if (index.i == -1 && index.j == -1) {
				throw new Exception("search():: Unpredictable error");

			}

			if (index.i != -1 && index.j != -1) {

				pivot(index);
				j = findCobaseVariableIndex(index.j);
				i = findBaseVariableIndex(index.i);
				int variable = base[i];
				updateBaseAndCoBase(i, j);
				j = getCoBaseIndex(variable);
			}

			j = j + 1;

		} while (j < columns - 1 || (base[rows - 1] != rows - 1));

		// System.out.println("vertexEnumeration Program exited normally");
	}
}
