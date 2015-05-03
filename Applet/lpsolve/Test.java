package lpsolve;

import lp_solver.LPExpression;
import lp_solver.LPSolver;
import lp_solver.LPVariable;

public class Test {
	public static void main(String[] args) throws Exception {
		LPSolver solver = new LPSolverImpl(4);
		LPExpression e1, e2;
		e1 = solver.linearExpression();
		e2 = solver.linearExpression();
		LPExpression e3 = solver.linearExpression();
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
		solver.addGreaterEquation(4, e1);
		solver.addLesserEquation(3, e2);
		solver.addMinimize(e3);
		solver.solve();
		double[] x = solver.getValues(var);
		for (double d : x) {
			System.out.println(d);
		}
		System.out.println(solver.getObjectiveValue());
	}
}
