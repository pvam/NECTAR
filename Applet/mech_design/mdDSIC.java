/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mech_design;

import java.util.Iterator;
import java.util.Set;

/**
*
* @author Aravind S R
* 
*/
public class mdDSIC
{

    /**
     * String get_util_string(String , int , mdGameObject)
     *
     * Given the current strategies, a player, and mdGameObject this function
     * will return the index to the corresponding utility for that player. This
     * function essentially transforms strategies in SCF to indexes in
     * utility matrix
     */
    static String get_util_string(String cur_strat, int player, mdGameObject mdo)
    {
        int key = mdo.scf_matix.get(cur_strat);
        String[] data = cur_strat.split(" ");
        String result = "";

        result = (player + 1) + " " + key + " " + data[player];

        //System.out.println(result + "*he he");

        return result;

    }

    /**
     * String get_util_string(String , int , mdGameObject)
     *
     * Given the current strategies, a player, mdGameObject and an alt
     * (alternative) this function will return the index to the corresponding
     * utility for that player based on the alternative. This function
     * essentially transforms strategies in SCF to indexes in utility matrix.
     * This function essentially computes the utility when player is not
     * telling the truth
     */
    static String get_tmp_util_string(String cur_strat, int player, mdGameObject mdo, int alt)
    {
        int key = mdo.scf_matix.get(cur_strat);
        String[] data = cur_strat.split(" ");
        String result = "";

        result = player + 1 + " " + (alt + 1) + " " + data[player];

        //System.out.println(result + "*he he");

        return result;

    }

    /**
     * String getStrat(String , int , int)
     *
     * Given current strategy, player and a type, this function will
     * return the stragegy correspoing to the input type of input player
     */
    static String getStrat(String cur_strat, int player, int typ)
    {
        String[] data = cur_strat.split(" ");
        String result = "";

        for (int i = 0; i < data.length; i++)
        {
            if (i != player)
            {
                result = result + data[i] + " ";

            } else
            {
                result = result + (typ + 1) + " ";
            }

        }

        result = result.substring(0, result.length() - 1);

        //System.out.println(result + "*");

        return result;
    }

    /**
     * String typ(mdGameObject )
     *
     * This is the main DSIC function. This will compute whether a given SCF is
     * DSIC or not. This is done by comparing the utilities when he/she is
     * telling truth with when he/she is not telling the truth
     */
    static String typ(mdGameObject mdo)
    {
        int player;
        for (player = 0; player < mdo.nPlayer; player++)
        {
            String[] key_set = new String[mdo.scf_matix.size()];
            Set keys = mdo.scf_matix.keySet();
            int j = 0;
            for (Iterator I = keys.iterator(); I.hasNext(); j++)
            {
                key_set[j] = (String) I.next();
            }

            int i = 0;
            for (i = 0; i < mdo.scf_matix.size(); i++)
            {
                String cur_strat = key_set[i];
                int cur_alt = mdo.scf_matix.get(cur_strat);
                String cur_Ustart = get_util_string(cur_strat, player, mdo);
                int cur_util = mdo.utiltiy_matrix.get(cur_Ustart);

                String[] data = cur_strat.split(" ");
                int cur_type = Integer.parseInt(data[player]);

//                if (Integer.parseInt(data[player]) != cur_type)
//                {
//                    continue;
//                }

                int typ;
                for (typ = 0; typ < mdo.type[player]; typ++)
                {
                    if (typ + 1 == cur_type)
                    {
                        continue;
                    } else
                    {
                        String tmp_strat = getStrat(cur_strat, player, typ);
                        String tmp_Ustart = get_util_string(tmp_strat, player, mdo);
                        int tmp_util = mdo.utiltiy_matrix.get(tmp_Ustart);
                        int tmp_alt = mdo.scf_matix.get(tmp_strat);

                        if (cur_alt != tmp_alt)
                        {
                            if (cur_util < tmp_util)
                            {
                                break;
                            }
                        }
                    }
                }

                if (typ < mdo.type[player])
                {
                    break;
                }

            }

            if (i < mdo.scf_matix.size())
            {
                break;
            }
        }

        if (player < mdo.nPlayer)
        {
            return ("SCF is not DSIC\n");

        } else
        {
            return ("SCF is DSIC\n");

        }
    }
}
