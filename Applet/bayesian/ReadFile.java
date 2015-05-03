package bayesian;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.FileReader;
import java.util.HashMap;


public class ReadFile {

	 private String inputFile = null;

	    public int readFile(HashMap<String, Integer> util, Integer noOfPlayers) throws IOException {

	        //System.out.println("n = " + noOfPlayers);
	        BufferedReader br = new BufferedReader(new FileReader(getInputFile()));
	        String line = new String();
	        line = br.readLine();
	        noOfPlayers =  Integer.parseInt(line);
	      // System.out.println("n = "+noOfPlayers);
	        while ((line = br.readLine()) != null && line.contains(" ")) {
	            
	            util.put(line.substring(0, line.lastIndexOf(' ')), Integer.parseInt(line.substring(line. lastIndexOf(' ') + 1)));
	        }
	        return noOfPlayers;
	    }

		void setInputFile(String inputFile) {
			this.inputFile = inputFile;
		}

		String getInputFile() {
			return inputFile;
		}
}
