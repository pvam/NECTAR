package bayesian;

import java.io.IOException;
import java.util.HashMap;

/**
* Seltengame class for Selten Game Player objects
*/
public class SeltenGame {
	
	private String File;
	private int numPlayers;
	private int bayes_player;           	/* bayesian player index, used in case of selten player */
	private int theta_type;			/* bayesian player theta type which is used to create this player  */
	
	SeltenGame() throws IOException, Exception {
		
		File = null;
		numPlayers = 0;
		bayes_player = 0;
		theta_type = 0;
	}
	
}
