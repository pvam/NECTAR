package lpsolve;

import java.util.ArrayList;
import java.util.Iterator;

import lp_solver.LPExpression;
import lp_solver.LPVariable;

/*
 * This class is implementing interface LPExpression.
 * Each expression is list of terms.
 * Expression : [term1, term2 term3, term4,...]
 * More about 'Term' is explained in class Term
 * */
public class LPExpressionImpl implements LPExpression {
	//data members
	ArrayList<Term> vars = new ArrayList<Term>();

	
	//member functions
	@Override
	/*
	 * Adding new term in expression. More about Term is given in Term class.
	 */
	public void addTerm(double value, LPVariable lpVariable) throws Exception {
		Term impl = new Term(value, lpVariable);
		vars.add(impl);
		impl.coeficient = value;
	}

	@Override
	/* Object's toString Method */
	public String toString() {
		if (LPSolverImpl.count >= vars.size())
			return smartRes();
		String res = "", tmp = "";
		Iterator<Term> it = vars.iterator();
		//Generating string of space separated coefficients
		//Space separated coefficient list is input expression format for LPSolve
		while (it.hasNext()) {
			Term x = it.next();
			res += tmp + x.coeficient;
			tmp = " ";
		}
		return res;
	}

	/*
	 * Purpose: This method is used to give correct input to LPSolve expression.
	 * In LPSolve expression all variable's coefficients need to be specified in
	 * the expression string, even if some of the coefficients of the variables
	 * are zero. But in LPExpression interface allows us to neglect the
	 * variables which have zero coefficients as interface is treating each
	 * variable as object and it is referencing those objects. So smartRes
	 * method is putting coefficients as zero to those variables which does not
	 * appear in the expression.
	 */
	private String smartRes() {
		String res = "";
		for (int i = 1; i <= LPSolverImpl.count; i++) {
			Iterator<Term> it = vars.iterator();
			boolean found = false;
			while (it.hasNext()) {
				Term x = it.next();
				LPVariableImpl v = (LPVariableImpl) x.variable;
				if (v.i == i) {
					res += "" + x.coeficient + " "; // if variable is present then append it's coefficient
					found = true;
					break;
				}
			}
			if (!found)
				res += "0.0 "; //append zero as coefficient for those variables which are not in the expression
		}
		return res; //return expression as needed for the lpsolve
	}

	/*
	 * This method created for testing the working of this class during
	 * development
	 */
	public static void main(String[] args) {
		LPExpressionImpl x = new LPExpressionImpl();
		try {
			x.addTerm(12, new LPVariableImpl(12));
			x.addTerm(12, new LPVariableImpl(12));
			x.addTerm(12, new LPVariableImpl(12));
			x.addTerm(12, new LPVariableImpl(12));
			System.out.println(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
