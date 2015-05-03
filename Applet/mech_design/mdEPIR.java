/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mech_design;

import javax.print.DocFlavor.STRING;

/**
*
* @author Aravind S R
* 
*/
public class mdEPIR
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
     * int isEPIR(String , int , mdGameObject )
     *
     * Given the actual strategies, a player and mdGameObject this fucntion
     * will compute whether the SCF is EPIR for the input player
     */
    static int isEPIR(String actualset, int player, mdGameObject mdo)
    {

        String cur_util_string = get_util_string(actualset, player, mdo);
        int cur_util_int = mdo.utiltiy_matrix.get(cur_util_string);

        if (cur_util_int >= NOT_PART_UTIL)
        {
            return 1;
        } else
        {
            return 0;
        }
    }

    /**
     * String find_EPIR( mdGameObject )
     *
     * This is the main EPIR function. This will compute whether a given SCF is
     * EPIR or not for each of the players. Given the mdGameObject this fucntion
     * will compute whether the SCF is EPIR for the all the players
     */
    static String find_EPIR(mdGameObject mdo)
    {
        String actualset;
        actualset = getCurset(mdo);
        StringBuffer res = new StringBuffer();

        for (int player = 0; player < mdo.nPlayer; player++)
        {
            if (isEPIR(actualset, player, mdo) == 1)
            {
                res.append("SCF is EPIR for player" + (player + 1) + "\n");
            } else
            {
                res.append("SCF is Not EPIR for player" + (player + 1) + "\n");
            }

        }

        return res.toString();
    }
}
