/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mech_design;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
*
* @author Aravind S R
* 
*/
public class mdEAIR
{

    static int NOT_PART_UTIL = 0;

    /**
     * String getCurset(mdGameObject mdo)
     *
     * Given a game object, this function will return a type profile
     * when all the players are playing their true types
     */
    static String getCurset(mdGameObject mdo)
    {
        String ret = "";
        for (int i = 0; i < mdo.nPlayer; i++)
        {
            ret = ret + mdo.true_type.get(i + 1) + " ";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

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
     * void find_all_utils(mdGameObject , int , int , HashMap<String, Integer> )
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
     * float isEAIR(int player, mdGameObject mdo))
     *
     * Given an mdGameObject and a player, a type, this fucntion will compute
     * whether the given SCF is EAIR for the input player
     */
    static int isEAIR(int player, mdGameObject mdo)
    {

        float cur_util_int = 0;
        String prob = mdo.belif_fn.get(player + 1);
        String probs[] = prob.split(";");
        String pdata = probs[player];
        String pdatas[] = pdata.split(" ");


        for (int i = 0; i < mdo.type[player]; i++)
        {


            String curset = "";
            for (int j = 0; j < mdo.nPlayer; j++)
            {
                if (j == player)
                {
                    curset = curset + (i + 1) + " ";
                } else
                {
                    curset = curset + 1 + " ";
                }
            }

            curset = curset.substring(0, curset.length() - 1);

            String[] data = curset.split(" ");
            int cur_type = Integer.parseInt(data[player]);
            //Entering the probalilites for players

            cur_util_int += (float) Double.parseDouble(pdatas[i]) * expected_util(mdo, player, cur_type, curset, curset);


        }


        if (cur_util_int >= NOT_PART_UTIL)
        {
            return 1;
        } else
        {
            return 0;
        }
    }

    /**
     * String find_EAIR(mdGameObject )
     *
     * This is the main EAIR function. This will compute whether a given SCF is
     * EAIR or not for each of the players
     */
    static String find_EAIR(mdGameObject mdo)
    {

        StringBuffer res = new StringBuffer();
        for (int player = 0; player < mdo.nPlayer; player++)
        {
            if (isEAIR(player, mdo) == 1)
            {
                res.append("SCF is EAIR for player" + (player + 1) + "\n");
            } else
            {
                res.append("SCF is Not EAIR for player" + (player + 1) + "\n");
            }

        }
        return res.toString();
    }
}
