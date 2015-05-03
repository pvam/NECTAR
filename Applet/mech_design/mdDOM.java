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
public class mdDOM
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

        result = player + 1 + " " + key + " " + data[player];

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
     * String find_domn(mdGameObject )
     *
     * This is the main dominator function. This will compute whether a given SCF is
     * dictatorial or not. This is done by comparing the utility of each player
     * against all alternatives
     */
    static String find_domn(mdGameObject mdo)
    {
        StringBuffer res = new StringBuffer();

        for (int player = 0; player < mdo.nPlayer; player++)
        {
            String[] key_set = new String[mdo.scf_matix.size()];
            Set keys = mdo.scf_matix.keySet();
            int j = 0;
            int domnflag = 1;
            for (Iterator I = keys.iterator(); I.hasNext(); j++)
            {
                key_set[j] = (String) I.next();
            }

            for (int i = 0; i < mdo.scf_matix.size(); i++)
            {

                String cur_strat = key_set[i];
                int cur_alt = mdo.scf_matix.get(cur_strat);
                String cur_Ustart = get_util_string(cur_strat, player, mdo);
                int cur_util = mdo.utiltiy_matrix.get(cur_Ustart);

                int alt;
                for (alt = 0; alt < mdo.alternatives; alt++)
                {
                    if (alt + 1 == cur_alt)
                    {
                        continue;
                    } else
                    {
                        String[] data = cur_strat.split(" ");
//                        String tmp_strat = (alt + 1) + " " + data[player];
                        String tmp_Ustart = get_tmp_util_string(cur_strat, player, mdo, alt);
                        int tmp_util = mdo.utiltiy_matrix.get(tmp_Ustart);

                        if (cur_util < tmp_util)
                        {
                            break;
                        }
                    }

                }

                if (alt < mdo.alternatives)
                {
                    domnflag = 0;
                    break;
                }

            }


            if (domnflag == 1)
            {
                res.append("player " + (player + 1) + " is Dominator\n");
            } else
            {
                res.append("player " + (player + 1) + " is Not Dominator\n");
            }


        }

        return res.toString();
    }
}
