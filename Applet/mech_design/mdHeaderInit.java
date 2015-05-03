/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mech_design;


import java.io.*;

/**
*
* @author Aravind S R
* 
*/
public class mdHeaderInit
{

    /**
     * static void mdHeaderInit(mdGameObject, String )
     * Given a mdGameObject and a filename, this function will initialize the header object in
     * mdGameObject. First the header object is cleared and then assigned with original data values.
     * The clearing is necessary as, if the program is run more than one time, the previous data
     * should not interfere with the current one.
     */
    static void mdHeadInit(mdGameObject mdobj, String fname)
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
                // Print the content on the console
                //System.out.println(strLine);

                if (strLine.contains("##"))
                {
                    break;
                } else if (strLine.contains("Players"))
                {
                    String[] data = strLine.split(" ");
                    mdobj.nPlayer = Integer.parseInt(data[2]);
                    //System.out.println("^^^^^^^^^^ " + mdobj.nPlayer);
                } else if (strLine.contains("Alternatives"))
                {
                    String[] data = strLine.split(" ");
                    mdobj.alternatives = Integer.parseInt(data[2]);
                    //System.out.println("^^^^^^^^^^ " + mdobj.nPlayer);
                } else if (strLine.contains("Types"))
                {
                    mdobj.type = new int[mdobj.nPlayer];
                    String[] data = strLine.split(" ");
                    for (int i = 0; i < mdobj.nPlayer; i++)
                    {
                        //Writing Data
                        mdobj.type[i] = Integer.parseInt(data[2 + i]);

                    }
                }
            }

            //Close the input stream
            in.close();
        } catch (Exception e)
        {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
