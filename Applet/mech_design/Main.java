
package mech_design;

/**
*
* @author Aravind S R
* 
*/
public class Main
{

    
	
	/**
     * static String main(String[])
     *
     * This is the main module of Mechanism design. This module is
     * integrated to nectar framework. All the algorithms in
     * mechanism design are specified here.
     */
    public static String main(String[] args)
    {
        mdGameObject mdobj = new mdGameObject();
        mdInitializer.init_md_gameobj(mdobj, args[0]);
        String result=null;
        String Header="MECHANISM DESIGN\n\n";

        if (args[1].equals("EPE"))
        {
        	Header+="ALGORITHM EX-POST EFFICIENT\n\n\n";
            result=mdEPE.find_epe(mdobj);

        } else if (args[1].equals("Dictatorial"))
        {
        	Header+="ALGORITHM DICTATORSHIP\n\n\n";
           result= mdDOM.find_domn(mdobj);
        } else if (args[1].equals("DSIC"))
        {
        	Header+="ALGORITHM DSIC\n\n\n";
            result=mdDSIC.typ(mdobj);
        } else if (args[1].equals("BIC"))
        {
        	Header+="ALGORITHM BIC\n\n\n";
            result=mdBIC.typ(mdobj);
        } else if (args[1].equals("Ex-Post IR"))
        {
        	Header+="ALGORITHM EX-POST IR\n\n\n";
           result= mdEPIR.find_EPIR(mdobj);
        } else if (args[1].equals("IIR"))
        {
        	Header+="ALGORITHM IIR\n\n\n";
            result=mdIIR.find_IIR(mdobj);
        } else if (args[1].equals("Ex-Ante IR"))
        {
        	Header+="ALGORITHM EX-ANTE IR\n\n\n";
            result=mdEAIR.find_EAIR(mdobj);
        }
        return Header+result;

    }
}
