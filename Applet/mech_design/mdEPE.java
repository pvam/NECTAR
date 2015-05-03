package mech_design;

import com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import mech_design.mdGameObject;

/**
*
* @author Aravind S R
* 
*/
class pair
{

    int strat;
    int scf;
}

public class mdEPE
{

    /**
     * String getStrat(String , int , int)
     *
     * Given current strategy, player and a type, this function will
     * return the strategy correspoing to the input type of input player
     */
    static String create_util(String cur_strat, int player, int alt)
    {
        String[] data = cur_strat.split(" ");
        String result = "";

        for (int i = 0; i < data.length; i++)
        {
            if (i != 1)
            {
                result = result + data[i] + " ";

            } else
            {
                result = result + (alt + 1) + " ";
            }

        }

        result = result.substring(0, result.length() - 1);

        //System.out.println(result + "*");

        return result;

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
     * String get_first_util_string(String , int , mdGameObject)
     *
     * Given the current strategies, a player, and mdGameObject this function
     * will return the index to the first entry in SCF
     */
    static String get_first_util_string(String cur_strat, int player, mdGameObject mdo)
    {
        int key = mdo.scf_matix.get(cur_strat);
        String[] data = cur_strat.split(" ");
        String result = "";

        result = "1 " + data[player];

        return result;

    }
    /**
     * int find_possible_epe(String , int , mdGameObject)
     *
     * Given the current strategies, a player, and mdGameObject this function
     * will return whether the current choise can be EPE or not. The computation
     * carried out here is non-increasing. The data set is also non-increasing
     */

    static int find_possible_epe(String cur_strat, int cur_alt, mdGameObject mdo)
    {
        Vector possible = new Vector();
        String p_cur_Ustrat;//= get_first_util_string(cur_strat, 0, mdo);
        String base_Ustart = get_util_string(cur_strat, 0, mdo);
        //System.out.println(base_Ustart + "*");
        int base_util = mdo.utiltiy_matrix.get(base_Ustart);



        //System.out.println(base_util + "**");
        //Initializing the player1's possibilities in vector
        for (int alt = 0; alt < mdo.alternatives; alt++)
        {
            if (alt + 1 == cur_alt)
            {
                continue;
            } else
            {
                //System.out.println("hrere1");
                p_cur_Ustrat = create_util(base_Ustart, 0, alt);
                //System.out.println("hrere2");
                if (mdo.utiltiy_matrix.get(p_cur_Ustrat) >= base_util)
                {
                    String[] starts = p_cur_Ustrat.split(" ");
                    pair pairtmp = new pair();
                    //pairtmp.strat = Integer.parseInt(starts[2]);
                    pairtmp.strat = 0;
                    pairtmp.scf = alt;
                    possible.add(pairtmp);
                }
            }

        }

        //System.out.println("hrere");

        //chk with all other players

        for (int player = 1; player < mdo.nPlayer; player++)
        {
            if (possible.isEmpty())
            {
                break;
            }
            base_Ustart = get_util_string(cur_strat, player, mdo);
            base_util = mdo.utiltiy_matrix.get(base_Ustart);

            for (int alt = 0; alt < mdo.alternatives; alt++)
            {
                if (alt + 1 == cur_alt)
                {
                    continue;
                } else
                {
                    p_cur_Ustrat = create_util(base_Ustart, player, alt);
                    if (mdo.utiltiy_matrix.get(p_cur_Ustrat) >= base_util)
                    {
                    } else
                    {
                        String[] starts = p_cur_Ustrat.split(" ");
                        pair pairtmp = new pair();
                        //pairtmp.strat = Integer.parseInt(starts[2]);
                        pairtmp.strat = 0;
                        pairtmp.scf = alt;

                        int flag = 0;
                        for (Enumeration e = possible.elements(); e.hasMoreElements();)
                        {
                            pair pairchk = (pair) e.nextElement();

                            if (pairchk.scf == pairtmp.scf)
                            {
                                if (pairchk.strat == pairtmp.strat)
                                {
                                    possible.remove((pair) pairchk);
                                    break;
                                }
                            }
                        }

//                        if (possible.contains(pairtmp))
//                        {
//                            possible.remove((Object) pairtmp);
//
//                        }
                    }
                }

            }
        }

        if (possible.isEmpty())
        {
            return 1;
        } else
        {
            return -1;
        }

    }

    /**
     * String typ(mdGameObject )
     *
     * This is the main EPE function. This will compute whether a given SCF is
     * EPE or not. This is done by comparing the possible epe's
     */
    static String find_epe(mdGameObject mdo)
    {
        String[] key_set = new String[mdo.scf_matix.size()];
        Set keys = mdo.scf_matix.keySet();
        int j = 0;
        int isEPE = 0;


        for (Iterator I = keys.iterator(); I.hasNext(); j++)
        {
            key_set[j] = (String) I.next();
        }
        for (int i = 0; i < mdo.scf_matix.size(); i++)
        {
            String cur_strat = key_set[i];
            int cur_alt = mdo.scf_matix.get(cur_strat);
            isEPE = find_possible_epe(cur_strat, cur_alt, mdo);
            if (isEPE == -1)
            {
                break;
            }
        }

        if (isEPE == -1)
        {
            return ("SCF is not EPE\n");
        } else
        {
            return ("SCF is EPE\n");
        }

    }
}
