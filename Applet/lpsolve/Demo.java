package lpsolve;

public class Demo {
	public static void main(String[] args) {
		String path = System.getProperty("java.library.path");
		System.out.println(path);
		try {
			// System.loadLibrary("lpsolve55j.jar");
			// Create a problem with 4 variables and 0 constraints

			LpSolve solver = LpSolve.makeLp(0, 4);

			// add constraints

			// solver.setBasisvar(arg0, arg1)
			solver.strAddConstraint("3.0 2.0 2.0 1.0", LpSolve.LE, 4);

			solver.strAddConstraint("0.0 4.0 3.0 1.0", LpSolve.GE, 3);

			// set objective function

			solver.strSetObjFn("2 3 -2 3");
			solver.setMinim();
			// solve the problem

			LPSolverImpl x = new LPSolverImpl(0);
			x.solver = solver;
			System.out.println(x);
			solver.solve();

			// print solution

			System.out.println("Value of objective function:"
					+ solver.getObjective());
			double[] var = solver.getPtrVariables();
			for (int i = 0; i < var.length; i++) {
				System.out.println("Value of var[" + i + "] = " + var[i]);
			}
			// delete the problem and free memory
			solver.deleteLp();
		}

		catch (LpSolveException e) {
			e.printStackTrace();
		}
	}
}