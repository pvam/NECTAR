package mech_design;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
*
* @author Aravind S R
* 
*/
public class mdBIC
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

        result = (player + 1) + " " + alt + " " + data[player];

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
     * void find_all_utils(mdGameObject , int , int , HashMap<String, Integer> )
     * 
     * Given a mdGameObject a player, a type and a hashmap, this fuction will
     * scan the utility matrix and put all the utilities which in which the
     * input player plays the input type into the hash map
     */

    static void find_all_utils(mdGameObject mdo, int player, int cur_type, HashMap<String, Integer> poss)
    {

        String[] key_set = new String[mdo.scf_matix.size()];
        Set keys = mdo.scf_matix.keySet();
        int j = 0;
        for (Iterator I = keys.iterator(); I.hasNext(); j++)
        {
            key_set[j] = (String) I.next();
        }

        for (int i = 0; i < key_set.length; i++)
        {
            String[] data = key_set[i].split(" ");
            int tmp_type = Integer.parseInt(data[player]);
            if (tmp_type == cur_type)
            {
                poss.put(key_set[i], mdo.scf_matix.get(key_set[i]));
            }
        }

    }

    /**
     * float expected_util(mdGameObject , int , int , String , String )
     *
     * Given an mdGameObject, a player, a type, temporary stragegy and current
     * stragegy, this function will compute the expected utility for that player
     * ie it computers E(@-1)[u(x,@)]
     */

    static float expected_util(mdGameObject mdo, int player, int cur_type, String tmp_strat, String cur_strat)
    {
        float retval = 0;
        int j = 0;

        String prob = mdo.belif_fn.get(player + 1);
        String probs[] = prob.split(";");
        HashMap<String, Integer> poss = new HashMap<String, Integer>();
        find_all_utils(mdo, player, cur_type, poss);
        Set keys = poss.keySet();

        for (Iterator I = keys.iterator(); I.hasNext(); j++)
        {
            String tp_strat = (String) I.next();

            String tp_Ustart = get_tmp_util_string(cur_strat, player, mdo, poss.get(tp_strat));

            float tp_util = mdo.utiltiy_matrix.get(tp_Ustart);

            String split_tp_strat[] = tp_strat.split(" ");

            if (cur_type != Integer.parseInt(split_tp_strat[player]))
            {
                continue;
            }

            for (int kkk = 0; kkk < mdo.nPlayer; kkk++)
            {
                if (kkk == player)
                {
                    continue;
                }

                String cdata = probs[kkk];
                String cdatas[] = cdata.split(" ");
                int index = Integer.parseInt(split_tp_strat[kkk]);
                index--;
                tp_util = (float) (tp_util * Double.parseDouble(cdatas[index]));
            }
            retval += tp_util;
        }
        return retval;
    }

    /**
     * String typ(mdGameObject )
     *
     * This is the main BIC function. This will compute whether a given SCF is
     * BIC or not. This is done by comparing the expected values.
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
//                String cur_Ustart = get_util_string(cur_strat, player, mdo);
//                int cur_util = mdo.utiltiy_matrix.get(cur_Ustart);

                float cur_util = 0;

                String[] data = cur_strat.split(" ");
                int cur_type = Integer.parseInt(data[player]);
                //Entering the probalilites for players

                cur_util = expected_util(mdo, player, cur_type, cur_strat, cur_strat);


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


                        String[] tm_datas = tmp_strat.split(" ");
                        int tm_cur_types = Integer.parseInt(tm_datas[player]);

                        float tmp_util = expected_util(mdo, player, tm_cur_types, tmp_Ustart, cur_strat);
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
            return ("SCF is not BIC\n");

        } else
        {
            return ("SCF is BIC\n");

        }
    }
}
