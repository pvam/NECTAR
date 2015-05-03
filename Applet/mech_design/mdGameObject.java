package mech_design;

import java.util.HashMap;

/**
 * @author Aravind S R
 * This is the Game Object class. All the input parameters and configurations
 * are stored here. The whole game is represented by this object
 */
public class mdGameObject
{

    int nPlayer;        //No: of players
    int type[];         //Type set of each player
    int alternatives;   //Total no.of alternatives in SCF
    HashMap<String, Integer> utiltiy_matrix;    //utiltiy matrix
    HashMap<String, Integer> scf_matix;         //scf matix
    HashMap<Integer, Integer> true_type;        //true type of each player
    HashMap<Integer, String> belif_fn;          //belif fn  of each player
}
