package lpsolve;

import lp_solver.LPVariable;

public class LPVariableImpl implements LPVariable {
	//data members
	int i; // identifies the variable.
	double lower; //lowest possible value for this variable
	double upper; //upper bound on this variable.
	public boolean binary; //Is this variable a binary ie can take only 0 or 1 values?

	//constructor
	public LPVariableImpl(int i) {
		this.i = i;
		binary = false;
	}
}
