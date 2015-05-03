package mech_design;


import java.io.*;
import java.util.HashMap;
/**
*
* @author Aravind S R
* 
*/
public class mdSCFinit
{

    /**
     * static void mdScfInit(mdGameObject, String )
     * Given a mdGameObject and a filename, this function will initialize the scf object in
     * mdGameObject. First the scf object is cleared and then assigned with original data values.
     * The clearing is necessary as, if the program is run more than one time, the previous data
     * should not interfere with the current one.
     */
    static void mdScfInit(mdGameObject mdobj, String fname)
    {
        try
        {
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(fname);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)
            {
                if (strLine.contains("##"))
                {
                    break;
                }
            }
            while ((strLine = br.readLine()) != null)
            {
                if (strLine.contains("##"))
                {
                    break;
                }
            }

            mdobj.scf_matix = new HashMap<String, Integer>();
            while ((strLine = br.readLine()) != null)
            {
                // Print the content on the console
                //System.out.println(strLine);
                if (strLine.contains("##"))
                {
                    break;
                }

                String index;


                String[] data = strLine.split("=");
                //System.out.println(data[0]);
                int pos1;
                int pos2;

                pos1 = data[0].indexOf('[');
                pos2 = data[0].indexOf(']');
                index = data[0].substring(pos1 + 1, pos2);

                pos1 = data[1].indexOf('[');
                pos2 = data[1].indexOf(']');
                // System.out.println(index);

                //Writing data
                mdobj.scf_matix.put(index, Integer.parseInt(data[1].substring(pos1 + 1, pos2)));
            }

            //Close the input stream
            in.close();
        } catch (Exception e)
        {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
