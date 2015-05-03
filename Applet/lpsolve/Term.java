package lpsolve;

import lp_solver.LPVariable;

public class Term {
	//data members
	double coeficient; // coeffiecient in the term eg. 3x here 3 is a coefficient
	LPVariable variable;

	//constructor
	public Term(double coeficient, LPVariable variable) {
		this.coeficient = coeficient;
		this.variable = variable;
	}

	//member functions.
	@Override
	public String toString() {
		return "" + coeficient + "";
	}
}
