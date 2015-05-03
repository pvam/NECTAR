package staticloader;

public class Loader {

	static {
		System.out.println("Static initlizing");
		System.loadLibrary("lpsolve55j");
		System.out.println("Static done");
	}

	// required to work with JDK 6 and JDK 7
	public static void main(String[] args) {
	}
}
