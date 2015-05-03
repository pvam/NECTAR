package lpsolve;

import lp_solver.LPExpression;
import lp_solver.LPSolver;
import lp_solver.LPVariable;

public class Test2 {
	public static void main(String[] args) throws Exception {
		LPSolver solver = new LPSolverImpl(0);
		LPExpression e1, e2;
		e1 = solver.linearExpression();
		e2 = solver.linearExpression();
		LPExpression e3 = solver.linearExpression();
		LPVariable[] var = new LPVariableImpl[2];
		for (int i = 0; i < 2; i++)
			var[i] = solver.variable(0, Double.MAX_VALUE);
		// 3 2 2 1
		e1.addTerm(1, var[0]);
		e1.addTerm(1, var[1]);
		System.out.println(e1);
		// 0 4 3 1
		e2.addTerm(1, var[0]);
		e2.addTerm(1, var[1]);
		System.out.println(e2);
		// "2 3 -2 3"
		e3.addTerm(1, var[0]);
		e3.addTerm(1, var[1]);
		System.out.println(e3);
		// System.out.println(solver);
		solver.addLesserEquation(1, e2);// (3, e2);
		solver.addGreaterEquation(3, e1);// (4, e1);
		//solver.addMaximize(e3);
		solver.addMinimize(e3);
		 LPSolverImpl impl = (LPSolverImpl) solver;
		// impl.solver.setMaxim();
		solver.solve();
		double[] x = solver.getValues(var);
		for (double d : x) {
			System.out.println(d);
		}
		System.out.println(impl.solver.getObjective());
	}
}
