package mech_design;

/**
 *
 * @author Aravind S R
 * 
 */
import java.io.*;
import java.util.HashMap;

public class mdTrueType
{

    /**
     * static void mdScfInit(mdGameObject, String )
     * Given a mdGameObject and a filename, this function will initialize the truetype object in
     * mdGameObject. First the truetype object is cleared and then assigned with original data values.
     * The clearing is necessary as, if the program is run more than one time, the previous data
     * should not interfere with the current one.
     */
    static void mdtruetypeInit(mdGameObject mdobj, String fname)
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
            while ((strLine = br.readLine()) != null)
            {
                if (strLine.contains("##"))
                {
                    break;
                }
            }

            mdobj.true_type = new HashMap<Integer, Integer>();
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
                //System.out.println(index);

                mdobj.true_type.put(Integer.parseInt(index), Integer.parseInt(data[1].substring(pos1 + 1, pos2)));
            }

            //Close the input stream
            in.close();
        } catch (Exception e)
        {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
