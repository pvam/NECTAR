/**
 * 
 */
package gui;

import java.io.*;
import javax.swing.*;

import nectar.*;

/**
 * @author kalyan
 * 
 */
public class Mediator { // implements Runnable {

	private FileTextField fileTextField = null;

	private FileOpenButton fileOpenButton = null;

	private ComputeButton computeButton = null;

	private ShowGameButton showGameButton = null;

	private ClearButton clearButton = null;

	private ChangeAlgoButton algoButton = null;

	private ChangeFileButton fileButton = null;

	private StoreResultButton storeButton = null;

	private ShowExtensiveFormButton showExtButton = null;

	private ShowSequenceFormButton showSeqButton = null;

	private StopButton stopButton = null;

	private OutputPanel outputPanel = null;

	private OutputTextArea outputTextArea = null;

	private TimeLabel timeLabel = null;

	private MessageLabel messageLabel = null;

	private AlgorithmComboBox algoComboBox = null;

	private ExtensiveComboBox extensiveComboBox = null;

	private CoopGameComboBox coopComboBox = null;

	private MDComboBox mdComboBox = null;
	
	private NormalFormRadioButton normalButton = null;

	private CoOperativeRadioButton cooperativeButton = null;

	private MDRadioButton mdButton = null;
	
	private BayesianRadioButton bayesianButton = null;

	private ExtensiveFormRadioButton extensiveButton = null;

	private AdditionalRadioButton[] simpleSearchRadioButtons = null;

	private AdditionalRadioButton[] mipRadioButtons = null;

	private Nectar nectar = null;

	private File file = null;

	private String algorithmName = null;

	private String additionalInformation = null;

	private int simpleSearchCount;

	private int mipCount;

	private boolean additionalSelected = false;

	/**
	 * This is for Singleton pattern. This class instantiates only one object.
	 * If anybody invokes once again, it simply returns the same object. This
	 * causes, only one Mediator object such that every object will interact
	 * with this unique object
	 */
	static private boolean instance_flag = false; // true if 1 instance

	static private Mediator mediator = null;

	/**
	 * A static method which is used for getting the same mediator object to
	 * all.
	 */
	static public Mediator Instance() {
		if (!instance_flag) {
			instance_flag = true;
			mediator = new Mediator();
			return mediator;
		} else {
			return mediator;
		}
	}

	/**
	 * Constructor which is private, so that on other class can't instantiates
	 * directly through this constructor.
	 */
	private Mediator() {
		// initializing the variables
		simpleSearchRadioButtons = new AdditionalRadioButton[2];
		mipRadioButtons = new AdditionalRadioButton[4];
		simpleSearchCount = 0;
		mipCount = 0;
		// creating nectar object
		nectar = new Nectar();
		// System.out.println("Mediator object has created");
	}

	/**
	 * Sets the initial status to GUI.
	 */
	public void setInitialStatus() {
		// initializing the variables
		file = null;
		algorithmName = null;
		additionalSelected = false;
		simpleSearchCount = 0;
		mipCount = 0;

		/*
		 * Enable the radio buttons for the selection of the game type
		 */
		normalButton.setEnabled(true);
		extensiveButton.setEnabled(true);
		cooperativeButton.setEnabled(true);
		mdButton.setEnabled(true);
		bayesianButton.setEnabled(true);
		// remove the additional panel
		outputPanel.removeAdditionalPanel();

		// setting all buttons into initial state
		showGameButton.setEnabled(false);
		computeButton.setEnabled(false);
		fileButton.setEnabled(false);
		algoButton.setEnabled(false);
		clearButton.setEnabled(false);
		storeButton.setEnabled(false);
		showExtButton.setEnabled(false);
		showSeqButton.setEnabled(false);

		algoComboBox.setEnabled(false);
		extensiveComboBox.setEnabled(false);
		coopComboBox.setEnabled(false);
		mdComboBox.setEnabled(false);

		fileOpenButton.setEnabled(false);
		fileTextField.setInitialState();

		// clear the output area
		outputTextArea.clearOutputArea();

		// reseting the time
		timeLabel.reset();
		// clear the nectar object
		nectar.clear();
		return;
	}

	public boolean isNormalFormSelected() {
		if (extensiveButton.isSelected()) {
			return false;
		} else {
			return true;
		}
	}
	public void bayesianSelected() {

		// disable the radiobuttons
		normalButton.setEnabled(false);
		extensiveButton.setEnabled(false);
		cooperativeButton.setEnabled(false);
		mdButton.setEnabled(false);
		bayesianButton.setEnabled(false);
		// enable the clear or home button
		clearButton.setEnabled(true);
		algorithmName = "Bayesian";
		computeButton.setEnabled(true);
		// enable input related components
		fileOpenButton.setEnabled(true);
		fileTextField.setEnabled(true);
		return;
	}
	public void coopSelected() {

		// disable the radiobuttons
		normalButton.setEnabled(false);
		extensiveButton.setEnabled(false);
		cooperativeButton.setEnabled(false);
		mdButton.setEnabled(false);
		bayesianButton.setEnabled(false);
		// enable the clear or home button
		clearButton.setEnabled(true);
		// enable the choice combo box
		coopComboBox.setEnabled(true);
		// enable input related components
		fileOpenButton.setEnabled(true);
		fileTextField.setEnabled(true);
		return;
	}

	
	public void mdSelected() {

		// disable the radiobuttons
		normalButton.setEnabled(false);
		extensiveButton.setEnabled(false);
		cooperativeButton.setEnabled(false);
		mdButton.setEnabled(false);
		bayesianButton.setEnabled(false);
		
		
		// enable the clear or home button
		clearButton.setEnabled(true);
		// enable the choice combo box
		mdComboBox.setEnabled(true);
		// enable input related components
		fileOpenButton.setEnabled(true);
		fileTextField.setEnabled(true);
		return;
	}
	
	
	
	/**
	 * It will be called when the user selects normal form game. It changes the
	 * states of required components
	 */
	public void normalFormSelected() {

		// disable the radiobuttons
		normalButton.setEnabled(false);
		extensiveButton.setEnabled(false);
		cooperativeButton.setEnabled(false);
		mdButton.setEnabled(false);
		bayesianButton.setEnabled(false);
		
		// enable the clear or home button
		clearButton.setEnabled(true);
		// enable the algorithms combo box
		algoComboBox.setEnabled(true);
		// enable input related components
		fileOpenButton.setEnabled(true);
		fileTextField.setEnabled(true);
		return;
	}

	/**
	 * It will be called when the user selects extensive form game. It changes
	 * the states of corresponding compoents
	 */
	public void extensiveFormSelected() {

		// disable the radiobuttons
		normalButton.setEnabled(false);
		extensiveButton.setEnabled(false);
		cooperativeButton.setEnabled(false);
		mdButton.setEnabled(false);
		bayesianButton.setEnabled(false);
		
		// enable the clear or home button
		clearButton.setEnabled(true);
		// enable the extensive form related combo box
		extensiveComboBox.setEnabled(true);
		// enable input related components
		fileOpenButton.setEnabled(true);
		fileTextField.setEnabled(true);
		return;
	}

	/**
	 * It will be called when the user selects any algorithm in the extensive
	 * form related combo box. It takes the algorithm name and stores into it.
	 * 
	 * @param algorithmName
	 *            : Name of the algorithm
	 */
	public void setExtensiveAlgorithm(String algorithmName) {
		// disable the extensive form related combo box
		extensiveComboBox.setEnabled(false);

		/*
		 * if the algorithm is to convert extensive to normal form then it
		 * enables the algorithm combo box for further computation
		 */
		if (algorithmName.equals("Extensive to Normal Form")) {
			algoComboBox.setEnabled(true);
		} else {
			this.algorithmName = algorithmName;
		}
		return;
	}

	public void setDesignChoice(String choiceName) {
		// TODO Auto-generated method stub
		mdComboBox.setEnabled(false);
		algoButton.setEnabled(true);
		if (file != null) {
			computeButton.setEnabled(true);
		}
		this.algorithmName = choiceName;

		return;
	}

	
	
	
	public void setCoOperativeChoice(String choiceName) {
		coopComboBox.setEnabled(false);
		algoButton.setEnabled(true);
		if (file != null) {
			computeButton.setEnabled(true);
		}
		this.algorithmName = choiceName;

		return;
	}

	/**
	 * It will be called when the user selects any algorithm in the algorithm
	 * combo box. .It takes the algorithm name and stores into it.
	 * 
	 * @param algorithmName
	 */
	public void setAlgorithm(String algorithmName) {
		this.algorithmName = algorithmName;

		// disable the algorithm combo box
		algoComboBox.setEnabled(false);
		// enable the algorithm button to change algorithm
		algoButton.setEnabled(true);
		// set the algorithm name in nectar object
		nectar.setAlgorithm(algorithmName);

		/*
		 * if the algorithm is either mixed integer type or simplem search
		 * method, then enable the additional panel for additional options
		 */
		if (algorithmName.equals("Mixed Integer Programming")
				|| algorithmName.equals("Simple Search Method")) {
			outputPanel.addAdditionalPanel();
			return;
		}
		/*
		 * if the input file has already selected then enable the compute button
		 * to compute equilibrium point
		 */
		if (file != null) {
			computeButton.setEnabled(true);
		}
		return;
	}

	/**
	 * This is called when the user presses the change algorithm button to
	 * change the algorithm and enables the algorithm combo box for selecting
	 * the new algorithm
	 */
	public void changeAlgorithm() {

		if (algorithmName.equals("Core")
				|| algorithmName.equals("Shapley Value")
				|| algorithmName
						.equals("Approximate Shapley Value using Sampling")
						|| algorithmName.equals("Nucleolus")) {
			// disable the compute button
			computeButton.setEnabled(false);
			// enable the choice combo box for selecting new algorithm
			coopComboBox.setEnabled(true);
			// disable the change algorithm button
			algoButton.setEnabled(false);
			// disable the store button
			// storeButton.setEnabled(false);
			// reset the time label
			timeLabel.reset();
			// reset the algorithm name
			algorithmName = null;
			// clears the output area
			outputTextArea.clearOutputArea();
			// reset the algorithm name in nectar object
			nectar.setAlgorithm(algorithmName);

		} 
		
		else if (algorithmName.equals("EPE")
				|| algorithmName.equals("Dictatorial")
				|| algorithmName.equals("DSIC")
				|| algorithmName.equals("BIC")
				|| algorithmName.equals("Ex-Post IR")
				|| algorithmName.equals("IIR")
				|| algorithmName.equals("Ex-Ante IR")
				
		
		) {
			// disable the compute button
			computeButton.setEnabled(false);
			// enable the choice combo box for selecting new algorithm
			mdComboBox.setEnabled(true);
			// disable the change algorithm button
			algoButton.setEnabled(false);
			// disable the store button
			// storeButton.setEnabled(false);
			// reset the time label
			timeLabel.reset();
			// reset the algorithm name
			algorithmName = null;
			// clears the output area
			outputTextArea.clearOutputArea();
			// reset the algorithm name in nectar object
			nectar.setAlgorithm(algorithmName);

		} 
		
		
		else {

			/*
			 * If the previous algorithm is mixed integer programming or simple
			 * search method then remove the additional panel and reset the
			 * corresponding values.
			 */

			if (algorithmName.equals("Mixed Integer Programming")) {
				mipCount = 0;
				outputPanel.removeAdditionalPanel();
				additionalSelected = false;
			} else if (algorithmName.equals("Simple Search Method")) {
				simpleSearchCount = 0;
				outputPanel.removeAdditionalPanel();
				additionalSelected = false;
			}

			// disable the compute button
			computeButton.setEnabled(false);
			// enable the algorithm combo box for selecting new algorithm
			algoComboBox.setEnabled(true);
			// disable the change algorithm button
			algoButton.setEnabled(false);
			// disable the store button
			storeButton.setEnabled(false);
			// reset the time label
			timeLabel.reset();
			// reset the algorithm name
			algorithmName = null;
			// clears the output area
			outputTextArea.clearOutputArea();
			// reset the algorithm name in nectar object
			nectar.setAlgorithm(algorithmName);
		}
		return;
	}

	/**
	 * This method is called when the user selects the input file by pressing
	 * file open button or gives the file name in the file text field. It stores
	 * the takes the corresponding file object and updates the related states.
	 * 
	 * @param file
	 *            File object for the input file
	 */
	public void fileOpenSelected(File file) {
		this.file = file;

		/*
		 * if the file is an extensive form game then enables the show extensive
		 * form game button
		 */
		if (extensiveButton.isSelected()) {
			showExtButton.setEnabled(true);
			/*
			 * if the algorithm name sequence form algorithm then enables the
			 * show sequence form game button
			 */
			if (algorithmName != null
					&& algorithmName.equals("Sequence Form Algorithm")) {
				showSeqButton.setEnabled(true);
			}
		}
		// enable the show normal form game button
		if (algorithmName == null 
				|| (algorithmName.equals("Core")
						|| algorithmName.equals("Shapley Value") || algorithmName
						.equals("Approximate Shapley Value using Sampling")
						|| algorithmName.equals("Nucleolus")
						||algorithmName.equals("EPE")
								|| algorithmName.equals("Dictatorial")
								|| algorithmName.equals("DSIC")
								|| algorithmName.equals("BIC")
								|| algorithmName.equals("Ex-Post IR")
								|| algorithmName.equals("IIR")
								|| algorithmName.equals("Ex-Ante IR")
								
						
						 ) == false)

			// if(coopComboBox.isEnabled() == false)
			showGameButton.setEnabled(true);
		else
			showGameButton.setEnabled(false);

		/*
		 * if the algorithm has selected already then enable the compute button
		 */
		if (algorithmName != null) {
			/*
			 * if the algorithm is either mixed integer type of simple search
			 * method then check whether the additional options are selected or
			 * not
			 */
			if (algorithmName.equals("Mixed Integer Programming")
					|| algorithmName.equals("Simple Search Method")) {
				if (additionalSelected) {
					// enable the compute button
					computeButton.setEnabled(true);
				}
			} else {
				// else disable the compute button
				computeButton.setEnabled(true);
			}
		}

		// display the file name in the file text field
		fileTextField.setFile(file);
		// disable the file open button
		fileOpenButton.setEnabled(false);
		// enable the change file button for changing the input file
		fileButton.setEnabled(true);

		// set the file in the nectar object
		nectar.setFile(file);
		return;
	}

	/**
	 * This method is called when the change file button has selected.
	 */
	public void changeFile() {
		// disable the display game buttons
		showGameButton.setEnabled(false);
		showExtButton.setEnabled(false);
		showSeqButton.setEnabled(false);

		// enable the file open fields for new input file
		fileOpenButton.setEnabled(true);
		fileTextField.setEnabled(true);

		// disable relevent buttons
		fileButton.setEnabled(false);
		storeButton.setEnabled(false);
		computeButton.setEnabled(false);

		// resets the file object
		file = null;
		// resets the time label
		timeLabel.reset();
		// clears the output area
		outputTextArea.clearOutputArea();
		// reset the file in the nectar object
		nectar.resetFile();
		nectar.setAlgorithm(algorithmName);
	}

	/**
	 * This is called when the user presses the store button, it writes the
	 * output content to the file.
	 * 
	 * @param file
	 *            The file object
	 */
	public void fileSaveSelected(File file) {
		try {
			FileWriter out = new FileWriter(file);
			out.write(outputTextArea.getText());
			out.close();

		} catch (IOException e) {
			System.out.println("Error : " + e.getLocalizedMessage());
		}
		return;
	}

	/**
	 * This method is called when the user clicks the compute button. It
	 * computes the corresponding algorithm by calling the nectar object and
	 * display the results into output area. It also finds the time taken to
	 * comute and update the time label.
	 */
	public void computeAlgorithm() {
		// time values in milliseconds
		long startTime, endTime;
		String result = null;

		try {

			if (algorithmName.equals("Core")
					|| algorithmName.equals("Shapley Value")
					|| algorithmName
							.equals("Approximate Shapley Value using Sampling")
							|| algorithmName.equals("Nucleolus")) {
				startTime = System.currentTimeMillis();
				String[] args = new String[2];
				args[0] = file.toString();
				args[1] = algorithmName;

				result = coop.Main.main(args);

				endTime = System.currentTimeMillis();
			}
			else if (algorithmName.equals("EPE")
					|| algorithmName.equals("Dictatorial")
					|| algorithmName.equals("DSIC")
					|| algorithmName.equals("BIC")
					|| algorithmName.equals("Ex-Post IR")
					|| algorithmName.equals("IIR")
					|| algorithmName.equals("Ex-Ante IR")
					) {
				startTime = System.currentTimeMillis();
				String[] args = new String[2];
				args[0] = file.toString();
				args[1] = algorithmName;

				result = mech_design.Main.main(args);

				endTime = System.currentTimeMillis();
			}
			
			else if (algorithmName.equals("Bayesian")) {
				startTime = System.currentTimeMillis();
				String[] args = new String[2];
				args[0] = file.toString();
				args[1] = algorithmName;

				result = bayesian.Main.main(args);

				endTime = System.currentTimeMillis();
				
			}
			
			
			/*
			 * if the algorithm is sequence form type then call the
			 * corresponding algorithm.
			 */
			else if (algorithmName.equals("Sequence Form Algorithm")) {
				// get the current time in milli seconds format
				startTime = System.currentTimeMillis();
				result = nectar.computeSequenceFormAlgorithm();
				endTime = System.currentTimeMillis();
			} else {
				/*
				 * if the game is extensive form type and does not converted
				 * into normal form them convert into normal form.
				 */
				if (extensiveButton.isSelected()) {
					nectar.convertToNormalForm();
				}

				startTime = System.currentTimeMillis();
				result = nectar.compute(additionalInformation);
				endTime = System.currentTimeMillis();
			}

			// display the results into output area
			outputTextArea.showOutput(result);
			// update the time
			timeLabel.setTime(endTime - startTime);
			// enable the store button
			storeButton.setEnabled(true);

			// System.out.println(result);
			// System.out.println("Time elapsed : " + (endTime - startTime) +
			// "(m Secs)");
		} catch (Exception e) {
			System.out.println("Error : " + e.toString());
			JOptionPane.showMessageDialog(outputPanel, e.getLocalizedMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		return;
	}

	/**
	 * This method is called when the user selects the show normal form game
	 * button is selcted. It display the normal form game into the output area.
	 */
	public void showGame() {
		String result = null;
		try {
			/*
			 * if the game is extensive form type then converts extensive form
			 * to normal form game.
			 */
			if (extensiveButton.isSelected()) {
				result = nectar.convertToNormalForm();
			} else {
				result = nectar.showGame();
			}
			// display the results in output area
			outputTextArea.showOutput(result);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(outputPanel, e.getLocalizedMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		return;
	}

	/**
	 * This method is called when the user selects the show extensive form game
	 * button. It displayes the extensive form in the output area
	 */
	public void showExtGame() {
		try {
			String output = nectar.showExtensiveGame();
			outputTextArea.showOutput(output);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(outputPanel, e.getLocalizedMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * This method is called when the user selects the show sequence form game
	 * button. It converts the extensive form game to sequence form
	 * representation and displays in the output area
	 */
	public void showSeqGame() {
		try {
			String output = nectar.covnertToSequenceForm();
			outputTextArea.showOutput(output);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(outputPanel, e.getLocalizedMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		return;
	}

	/**
	 * This method is called when the user selects the additional options for
	 * simple search and mixed integer algorithms.
	 */
	public void additionalButtonSelected() {
		int i;

		if (algorithmName.equals("Simple Search Method")) {
			if (simpleSearchRadioButtons[0].isSelected()) {
				additionalInformation = "one";
			} else {
				additionalInformation = "all";
			}
		} else {
			for (i = 0; i < mipCount; i++) {
				if (mipRadioButtons[i].isSelected()) {
					additionalInformation = "formulation " + (i + 1);
					break;
				}
			}
		}

		additionalSelected = true;
		/*
		 * if the input file has already taken, then enable the compute button
		 * for finding equilibrium
		 */
		if (file != null) {
			computeButton.setEnabled(true);
		}
		return;
	}

	/*
	 * It returns the current algorithm
	 */
	public String getAlgorithmName() {
		return this.algorithmName;
	}

	/*
	 * The following methods for registering each component with mediator
	 * object.
	 */
	public void registerFileOpen(FileOpenButton button) {
		this.fileOpenButton = button;
	}

	public void registerAlgorithmComboBox(AlgorithmComboBox algoComboBox) {
		this.algoComboBox = algoComboBox;
	}

	public void registerCoopGameComboBox(CoopGameComboBox coopComboBox) {
		this.coopComboBox = coopComboBox;
	}

	public void registerExtensiveComboBox(ExtensiveComboBox extensiveComboBox) {
		this.extensiveComboBox = extensiveComboBox;
	}

	public void registerNormalFormRadioButton(NormalFormRadioButton normalButton) {
		this.normalButton = normalButton;
	}

	public void registerExtensiveFromRadioButton(
			ExtensiveFormRadioButton extensiveButton) {
		this.extensiveButton = extensiveButton;
	}

	public void registerCoOperativeRadioButton(
			CoOperativeRadioButton cooperativeButton) {
		this.cooperativeButton = cooperativeButton;
	}
	
	public void registerMDRadioButton(
			MDRadioButton mdButton) {
		this.mdButton = mdButton;
	}
	
	public void registerBayesianRadioButton(
			BayesianRadioButton bayesianButton) {
		this.bayesianButton = bayesianButton;
	}
	

	public void registerFileTextField(FileTextField fileTextField) {
		this.fileTextField = fileTextField;
	}

	public void registerOutputTextArea(OutputTextArea outputTextArea) {
		this.outputTextArea = outputTextArea;
	}

	public void registerComputeButton(ComputeButton computeButton) {
		this.computeButton = computeButton;
	}

	public void registerShowGameButton(ShowGameButton showGameButton) {
		this.showGameButton = showGameButton;
	}

	public void registerOutputPanel(OutputPanel outputPanel) {
		this.outputPanel = outputPanel;
	}

	public void registerClearButton(ClearButton clearButton) {
		this.clearButton = clearButton;
	}

	public void registerAdditionalRadioButton(
			AdditionalRadioButton additionalRadioButton) {
		if (algorithmName.equals("Simple Search Method")) {
			this.simpleSearchRadioButtons[simpleSearchCount] = additionalRadioButton;
			simpleSearchCount++;
		} else {
			this.mipRadioButtons[mipCount] = additionalRadioButton;
			mipCount++;
		}
		return;
	}

	public void registerStopButton(StopButton stopButton) {
		this.stopButton = stopButton;
	}

	public void registerChangeAlgoButton(ChangeAlgoButton algoButton) {
		this.algoButton = algoButton;
	}

	public void registerChangeFileButton(ChangeFileButton fileButton) {
		this.fileButton = fileButton;
	}

	public void registerStoreResultButton(StoreResultButton storeButton) {
		this.storeButton = storeButton;
	}

	public void registerShowExtensiveFormButton(
			ShowExtensiveFormButton showExtButton) {
		this.showExtButton = showExtButton;
	}

	public void registerShowSequenceFormButton(
			ShowSequenceFormButton showSeqButton) {
		this.showSeqButton = showSeqButton;
	}

	public void registerTimeLabel(TimeLabel timeLabel) {
		this.timeLabel = timeLabel;
	}

	public void registerMessageLabel(MessageLabel messageLabel) {
		this.messageLabel = messageLabel;
	}

	
	public void registerMDComboBox(MDComboBox mdComboBox) {
		// TODO Auto-generated method stub
		this.mdComboBox = mdComboBox;
	}

}
