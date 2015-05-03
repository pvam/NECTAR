package lpsolve;

import lp_solver.LPExpression;
import lp_solver.LPSolver;
import lp_solver.LPVariable;
import lpsolve.*;

@SuppressWarnings("unused")
public class LPSolverImpl implements LPSolver {
	//data members
	LpSolve solver; //LPSolve object
	static int count; //Number of variables in the given LP(about to solve)
	static {
		count = 0; //Initialization.
	}

	//Constructor
	public LPSolverImpl(int n) throws LpSolveException {
		solver = LpSolve.makeLp(0, n);
		count = n;
	}

	
	//member functions
	@Override
	public LPVariable variable(double lowerBound, double upperBound)
			throws Exception {
		//create new variable and assign it new id
		LPVariableImpl x = new LPVariableImpl(count + 1);
		//set upper and lower bounds on this variable
		x.lower = lowerBound;
		x.upper = upperBound;
		count++;//update count of variables
		return x;
	}

	@Override
	public LPVariable booleanVariable() throws Exception {
		//create new variable and assign it new id
		LPVariableImpl x = new LPVariableImpl(count + 1);
		x.binary = true;
		return x;
	}

	@Override
	//create new expression(empty initially)
	public LPExpression linearExpression() throws Exception {
		LPExpressionImpl x = new LPExpressionImpl();
		return x;
	}

	@Override
	//add equal to (eqn = rhsValue)equation.
	public void addEquation(double rhsValue, LPExpression expression)
			throws Exception {
		solver.strAddConstraint(expression.toString(), LpSolve.EQ, rhsValue);
	}

	@Override
	//removes objective function from the LP
	public void removeObjective() {
		try {
			solver.strSetObjFn("");
		} catch (LpSolveException e) {
			e.printStackTrace();
		}
	}

	@Override
	//adds less than equal (equation <= rhsvalue)
	public void addGreaterEquation(double rhsValue, LPExpression expression)
			throws Exception {
		matchVars(expression);
		solver.strAddConstraint(expression.toString(), LpSolve.LE, rhsValue);
	}

	//This method sees if there are enough number of variables present in the lp
	//if not it will add new variable in the lp
	//So according to incomming expression lp will keep on updating itself.
	//ie. it can accomodate any number of variables dynamically.
	private void matchVars(LPExpression expression) throws LpSolveException {
		LPExpressionImpl e = (LPExpressionImpl) expression;
		if (e.vars.size() != solver.getNcolumns())
			for (int i = solver.getNcolumns(); i < e.vars.size(); i++)
				solver.addColumn(new double[solver.getNrows()]);
	}

	@Override
	//adds greater than equal (equation >= rhsvalue)
	public void addLesserEquation(double rhsValue, LPExpression expression)
			throws Exception {
		matchVars(expression);
		solver.strAddConstraint(expression.toString(), LpSolve.GE, rhsValue);
	}

	@Override
	//adds objective function which we have to maximize.
	public void addMaximize(LPExpression expression) throws Exception {
		matchVars(expression);
		solver.strSetObjFn(expression.toString());
		solver.setMaxim();
	}

	@Override
	//adds objective function which we have to minimize
	public void addMinimize(LPExpression expression) throws Exception {
		matchVars(expression);
		solver.strSetObjFn(expression.toString());
		solver.setMinim();
	}

	@Override
	//call the solver function.
	//This will compute the solution for the lp.
	public boolean solve() throws Exception {
		int res = solver.solve();
		return res == 0;
	}

	@Override
	//return the objective value returned by the solver.
	public double getObjectiveValue() throws Exception {
		return solver.getObjective();
	}

	@Override
	//get variables values.
	public double[] getValues(LPVariable[] variables) throws Exception {
		return solver.getPtrVariables();
	}

	@Override
	//Based on status returned by the solver we can predict following about the results.
	public String getStatus() throws Exception {
		solver.getStatus();
		String res = "";
		switch (solver.getStatus()) {
		case -2:
			res = "NO MEMORY";
			break;
		case 0:
			res = "OPTIMAL";
			break;
		case 1:
			res = "SUBOPTIMAL";
			break;
		case 2:
			res = "INFEASIBLE";
			break;
		case 3:
			res = "UNBOUNDED";
			break;
		case 4:
			res = "DEGENERATE";
			break;
		case 5:
			res = "NUMFAILURE";
			break;
		case 6:
			res = "USERABORT";
			break;
		case 7:
			res = "TIMEOUT";
			break;
		case 9:
			res = "PRESOLVED";
			break;
		case 10:
			res = "PROCFAIL";
			break;
		case 11:
			res = "PROCBREAK";
			break;
		case 12:
			res = "FEASFOUND";
			break;
		case 13:
			res = "NOFEASFOUND";
			break;
		}
		return res;
	}

	@Override
	//End the current lp.
	//reinitialize the solver
	public void end() throws Exception {
		solver = LpSolve.makeLp(0, 0);
	}

	@Override
	//to string.
	public String toString() {
		try {
			return "LPSolverImpl [booleanVariable()=" + booleanVariable()
					+ ", linearExpression()=" + linearExpression()
					+ ", solve()=" + solve() + ", getObjectiveValue()="
					+ getObjectiveValue() + ", getStatus()=" + getStatus()
					+ "]";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//This method was written to test whether LPSolverImpl was working.
	public static void main(String[] args) throws Exception {
		LPSolver solver = new LPSolverImpl(0);
		LPExpressionImpl e1, e2;
		e1 = new LPExpressionImpl();
		e2 = new LPExpressionImpl();
		LPExpressionImpl e3 = new LPExpressionImpl();
		LPVariable[] var = new LPVariableImpl[4];
		for (int i = 0; i < 4; i++)
			var[i] = solver.variable(0, Double.MAX_VALUE);
		// 3 2 2 1
		e1.addTerm(3, var[0]);
		e1.addTerm(2, var[1]);
		e1.addTerm(2, var[2]);
		e1.addTerm(1, var[3]);
		System.out.println(e1);
		// 0 4 3 1
		e2.addTerm(0, var[0]);
		e2.addTerm(4, var[1]);
		e2.addTerm(3, var[2]);
		e2.addTerm(1, var[3]);
		System.out.println(e2);
		// "2 3 -2 3"
		e3.addTerm(2, var[0]);
		e3.addTerm(3, var[1]);
		e3.addTerm(-2, var[2]);
		e3.addTerm(3, var[3]);
		System.out.println(e3);
		// System.out.println(solver);
		solver.addGreaterEquation(3, e2);
		solver.addLesserEquation(4, e1);
		solver.addMaximize(e3);
		solver.solve();
		System.out.println(solver.getObjectiveValue());
		double[] x = solver.getValues(var);
		for (double d : x) {
			System.out.println(d);
		}
	}
}
