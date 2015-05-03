package bayesian;

import java.io.IOException;
import java.util.HashMap;

/**
* BayesianGame class for Bayesian Game Player objects
*/
public class BayesianGame {
	
	private String File;
	private int numStrategies;				/* players strategies */
	private int numTypes;					/* type profile of player */
		
	
	
	BayesianGame() throws IOException, Exception {
		
		File = null;
		numStrategies = 0;
		numTypes = -0;
	}
	
	
	void setFile(String file) {
		this.File = file;
	}

}
