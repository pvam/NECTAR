package bayesian;

import java.io.*;
import java.text.DecimalFormat;

/**
 * 
 * @author Deepak
 */

public class Main {

	/**
	 * Matrix to store players utilities
	 */
	class utility {
		float payoff[]; // array of payoffs of players for a given strategy
		int status; // equilibrium status
	};

	/**
	 * Matix to store common prior distribution and corresponding utility
	 * matices
	 */
	class Common_Prior {
		float prior;
		utility mat_utility[];
	};

	/**
	 * Struture to store bayesian player
	 */
	public class game_players {
		int num_strategy;
		int num_types;
		int bayes_player; // bayesian player index, used in case of selten

		int theta_type; // bayesian palyer's theta type, the basis for the

		public game_players() {

			num_types = 0;
		}
	};

	Common_Prior common_prior[]; /* Common prior */

	int compute_type[]; /* Array to navigate among plyers using their type info */
	int compute_strategies[]; /*
							 * Array to navigate among plyers using their
							 * strategies
							 */
	int compute_selten[]; /*
						 * Array to navigate among plyers selten players using
						 * selten strategies
						 */
	int bayes_theta[]; /* Number of types of Bayesian players */

	// bayesian game players
	game_players[] player = null;
	// selten game players
	game_players[] s_player = null;

	int num_bayes_players; /* Number of Bayesian players */
	int num_selten_players; /* Number of Selten players */

	public static String main(String args[]) throws NumberFormatException,
			IOException {

		// initialise global var
		Main mb = new Main();

		mb.get_bayesian(args[0]);
		String result = mb.write_selten(mb.player, mb.num_bayes_players,
				mb.common_prior);
		return result;
	}

	/**
	 * Convert the index passed as parameter into corresponding profile, using
	 * the compute array
	 */
	void index_to_profile(int index, int num_players, int[] compute,
			int[] profile) {
		int i, tmp_index = index;

		for (i = num_players - 1; i >= 0; i--) {
			profile[i] = (int) tmp_index / compute[i] + 1;
			tmp_index = tmp_index % compute[i];
		}

		return;// profile;

	}

	/**
	 * Read Bayesian game from the file passed as argument
	 */

	int get_bayesian(String fp) throws NumberFormatException, IOException {
		// struct game_players * player = NULL;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new DataInputStream(new FileInputStream(fp))));

		int num_players = 0, i, types = 1;
		int rows = 1, l;
		int s_index = 0, t_index = 0;

		int s_profile[], t_profile[];
		String s_string;

		String t_string;

		common_prior = null;
		player = null;
		String line;

		while ((line = br.readLine()).contains("Players") == false)
			;

		num_players = Integer.parseInt(line
				.substring(line.lastIndexOf(" ") + 1));

		num_bayes_players = num_players;

		player = new game_players[num_players];
		for (i = 0; i < num_players; i++) {
			player[i] = new game_players();
		}

		line = br.readLine();

		line = line.substring(line.lastIndexOf(":") + 2);
		String[] act = line.split(" ");

		line = br.readLine();

		line = line.substring(line.lastIndexOf(":") + 2);
		String[] type = line.split(" ");

		for (i = 0; i < num_players; i++) {

			player[i].num_strategy = Integer.parseInt(act[i]);
			player[i].num_types = Integer.parseInt(type[i]);
		}

		compute_type = new int[num_players];

		compute_strategies = new int[num_players];

		s_profile = new int[num_players];

		t_profile = new int[num_players];

		compute_type[0] = 1; // 1 for player one
		types = player[0].num_types;

		for (i = 1; i < num_players; i++) {
			compute_type[i] = compute_type[i - 1] * player[i - 1].num_types;
			types = types * player[i].num_types;

		}

		compute_strategies[0] = 1;
		rows = player[0].num_strategy;
		for (i = 1; i < num_players; i++) {
			compute_strategies[i] = compute_strategies[i - 1]
					* player[i - 1].num_strategy;
			rows = rows * player[i].num_strategy;
		}

		// allocate space for common prior and utilities
		common_prior = new Common_Prior[types];
		for (i = 0; i < types; i++) {
			common_prior[i] = new Common_Prior();
		}

		// allocated space for utility matrices for each type profile
		for (t_index = 0; t_index < types; t_index++) {
			common_prior[t_index].mat_utility = new utility[rows];
			for (i = 0; i < rows; i++)
				common_prior[t_index].mat_utility[i] = new utility();

			for (s_index = 0; s_index < rows; s_index++) {

				common_prior[t_index].mat_utility[s_index].payoff = new float[num_players];

			}
		}
		do {
			line = br.readLine();
		} while (line.contains("Common Prior") == false);

		line = br.readLine();

		for (t_index = 0; t_index < types; t_index++) {

			index_to_profile(t_index, num_players, compute_type, t_profile);

			t_string = sprint_profile(t_profile, num_players);
			line = line.substring(line.lastIndexOf(":") + 1);

			common_prior[t_index].prior = Float.parseFloat(line);
		}
		do {
			line = br.readLine();
		} while (!line.contains("Player Utilities"));

		do {
			line = br.readLine();
		} while (!line.contains("#"));

		for (t_index = 0; t_index < types; t_index++) {

			for (s_index = 0; s_index < rows; s_index++) {

				line = br.readLine();

				line = line.substring(line.lastIndexOf(":") + 2);
				String payoff[] = line.split(" ");
				for (i = 0; i < num_players; i++) {
					common_prior[t_index].mat_utility[s_index].payoff[i] = Float
							.parseFloat(payoff[i]);

				}

			}
			line = br.readLine();
		}

		return 0;
	}

	/**
	 * Prints the profile into the string passed
	 */

	String sprint_profile(int[] profile, int num_players) {
		int i;
		StringBuffer sb = new StringBuffer();
		for (i = 0; i < num_players; i++) {
			if (i == 0)
				sb.append("[ " + profile[i]);

			else if (i == num_players - 1)
				sb.append(" " + profile[i] + " ]");

			else
				sb.append(" " + profile[i]);

		}
		return sb.toString();

	}

	/**
	 * 
	 * Converts Bayesian game into Selten game and write the NF game into a
	 * file
	 */

	String write_selten(game_players player[], int num_players,
			Common_Prior cp[]) throws IOException {

		StringBuffer sb = new StringBuffer();
		int selten_players = 0;
		int selten_rows = 0, row_index = 0;
		int i, j, k;

		BufferedWriter out = new BufferedWriter(new FileWriter("outfile"));
		String selten_file;
		int s_profile[];
		String strprofile;

		float s_profile_payoff[];
		selten_file = "outfile" + "game";

		for (i = 0; i < num_players; i++)
			selten_players += player[i].num_types;

		num_selten_players = selten_players;
		s_profile = new int[num_selten_players];

		compute_selten = new int[num_selten_players];

		s_profile_payoff = new float[num_selten_players];

		s_player = new game_players[num_selten_players];
		for (i = 0; i < num_selten_players; i++) {
			s_player[i] = new game_players();
		}

		k = 0;
		for (i = 0; i < num_players; i++)
			/* for every Bayesian player */
			for (j = 0; j < player[i].num_types; j++) {
				s_player[k].num_strategy = player[i].num_strategy;

				s_player[k].bayes_player = i; /* the bayesian palyer selten */

				s_player[k].theta_type = j + 1;
				k++;
			}

		/* write into selten game */

		sb.append("NFS Game Generated by Nectar\n");
		sb.append("Selten game\n");

		DecimalFormat df = new DecimalFormat("0.000000");

		sb.append("Players:\t" + num_selten_players);
		sb.append("\nActions:\t");
		for (i = 0; i < num_selten_players; i++)
			sb.append(s_player[i].num_strategy + " ");
		sb.append("\n\n");

		/* num of rows in the Selten utility matrix */

		selten_rows = s_player[0].num_strategy;
		compute_selten[0] = 1;
		for (i = 1; i < num_selten_players; i++) {
			selten_rows *= s_player[i].num_strategy;
			compute_selten[i] = compute_selten[i - 1]
					* s_player[i - 1].num_strategy;
		}

		for (row_index = 0; row_index < selten_rows; row_index++) {
			index_to_profile(row_index, num_selten_players, compute_selten,
					s_profile);

			s_profile_payoff = selten_payoff(row_index, player,
					num_selten_players, cp, s_profile_payoff);

			strprofile = sprint_profile(s_profile, num_selten_players); // print

			for (i = 0; i < num_selten_players; i++) {

				sb.append(" " + df.format(s_profile_payoff[i]));
			}
		}

		try {

			out.write(sb.toString());
			out.close();
		} catch (IOException e) {
		}

		return sb.toString();
	}

	/**
	 * Computes utilities for all the players of the selten game
	 */

	float[] selten_payoff(long selten_row_index, game_players[] player,
			int num_selten_players, Common_Prior[] cp, float[] payoff) {
		int i;

		/* compute utilities of every player of the selten game */
		for (i = 0; i < num_selten_players; i++) {
			payoff[i] = compute_payoff(i, (int) selten_row_index, player);
		}

		return payoff;
	}

	/**
	 * Return the index value from a prorile string usting the compute array
	 */
	int profile_to_index(int profile[], int num_players, int[] compute) {
		int index = 0, i = 0;
		int[] tmp_profile = null;

		tmp_profile = new int[num_players];

		for (i = 0; i < num_players; i++)
			tmp_profile[i] = profile[i] - 1;

		for (i = 0; i < num_players; i++) {
			index = index + tmp_profile[i] * compute[i];
		}

		return index;
	}

	/**
	 * Compute payoff for the selten_payer with selten_row_index (NFG row index)
	 * selten_index
	 */

	float compute_payoff(int selten_player, int selten_index,
			game_players[] player) {
		float utility = 0.0f, prior_sum_total = 0.0f;
		int[] selten_profile = null;
		int i;

		int[] prior_profile = null;
		int prior_index, util_index, tmp_prior_index;
		int[] bayes_util_profile = null;
		int base_player = s_player[selten_player].bayes_player;

		prior_profile = new int[num_bayes_players];
		bayes_util_profile = new int[num_bayes_players];

		selten_profile = new int[num_selten_players];
		index_to_profile(selten_index, num_selten_players, compute_selten,
				selten_profile);

		/* rest of possible combinations of theta profiles */
		for (i = 0; i < num_bayes_players; i++)
			if (i == base_player)
				prior_profile[i] = s_player[selten_player].theta_type;
			else
				prior_profile[i] = 1;

		/* get the utility martrix for the above prior */

		prior_index = profile_to_index(prior_profile, num_bayes_players,
				compute_type); // it is the first prior of type
		tmp_prior_index = prior_index;

		do {
			/* build bayes_util_profile */

			get_bayes_profile(selten_profile, prior_index, bayes_util_profile,
					selten_player);
			util_index = profile_to_index(bayes_util_profile,
					num_bayes_players, compute_strategies);

			utility += common_prior[prior_index].prior
					* common_prior[prior_index].mat_utility[util_index].payoff[base_player];

		} while ((prior_index = next_prior(prior_index, base_player)) != -1); /*
																			 * get
																			 * the
																			 * next
																			 * prior
																			 */

		/* get the sum of all total of allthe prior combinatoins with fixed type */
		prior_sum_total = get_sum_total_prior(tmp_prior_index, base_player);

		if (prior_sum_total == 0) {
			// printf("\nfatal error! divide by zero\n");
		}

		utility = utility / prior_sum_total;

		return utility;
	}

	/**
	 * Returns number of combinations of types of all players except the player
	 * iPlayer
	 */
	int num_theta_rest(int iPlayer, game_players[] player) {
		int ret = 1, i = 0;

		for (i = 0; i < num_bayes_players; i++) {
			if (i != iPlayer)
				ret = ret * player[i].num_types;
		}
		return ret;
	}

	/**
	 * Returns the next index to the prior else, ERROR
	 */
	int next_prior(int prior_index, int base_player) {
		int ret = -1, i;
		int[] prior_profile = null;

		prior_profile = new int[num_bayes_players];
		index_to_profile(prior_index, num_bayes_players, compute_type,
				prior_profile);

		/*
		 * increment prior profile by one...from left..check for valid prior
		 * profile, keeping the value of base_payer type constant
		 */

		for (i = 0; i < num_bayes_players; i++) {
			if (i == base_player) /* base player's type is constant */
				continue;

			if (prior_profile[i] < player[i].num_types) {
				prior_profile[i]++;
				ret = profile_to_index(prior_profile, num_bayes_players,
						compute_type); /* get the prior index */
				break;
			} else {
				prior_profile[i] = 1;
			}
		}

		return ret;
	}

	/**
	 * populate bayesian strategy profile from given selten profile, selten
	 * player and common prior index
	 */
	void get_bayes_profile(int selten_profile[], int prior_index,
			int bayes_util_profile[], int selten_player) {
		int[] prior_profile = null;
		int base_player = s_player[selten_player].bayes_player;
		int index = 0, i, type = 0, j, shift = 0;

		prior_profile = new int[num_bayes_players];

		index_to_profile(prior_index, num_bayes_players, compute_type,
				prior_profile);

		for (i = 0; i < num_bayes_players; i++) {
			shift = 0;

			if (i == base_player)
				index = selten_player;
			else /*
				 * compute the index of selten profile to copy strategy int
				 * bayesian strategy profile
				 */
			{

				for (j = 0; j < i; j++) {
					shift += player[j].num_types;
				}

				index = shift + prior_profile[i] - 1;
			}

			bayes_util_profile[i] = selten_profile[index];
		}

		return;
	}

	/**
	 * Return the sum of priors of all the combinations of types rest of players
	 * and fixed type of current player
	 */
	float get_sum_total_prior(int prior_index, int base_player) {
		float p_sum = 0f;

		while (prior_index != -1) {

			p_sum += common_prior[prior_index].prior;

			prior_index = next_prior(prior_index, base_player);
		}

		return p_sum;
	}
}
