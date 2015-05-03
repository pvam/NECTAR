/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.*;

/**
 * @author kalyan
 * 
 */
public class InputPanel extends JPanel implements ActionListener {

	AdditionalPanel additionalPanel = null;

	public InputPanel() {
		super();
		initGUI();
	}

	/**
	 * initializes the GUI
	 */
	private void initGUI() {
		this.setLayout(new GridLayout(0, 1));
		this.setBorder(new EtchedBorder());

		addGameTypeRadioButtons();
		addExtensiveAlgoComboBOx();
		addNormalAlgoComboBox();
		addCoopGameComboBox();
		addMDComboBox();
		addFileOpenButton();

		this.updateUI();
	}

	/**
	 * adds the game type radion buttons to this panel
	 */
	public void addGameTypeRadioButtons() {
		JLabel label = new JLabel("Set game type :");
		label.setForeground(Color.BLUE);
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.add(label);

		NormalFormRadioButton normalRadioButton = new NormalFormRadioButton(
				this);
		ExtensiveFormRadioButton extRadioButton = new ExtensiveFormRadioButton(
				this);
		
		CoOperativeRadioButton coopRadioButton = new CoOperativeRadioButton(
				this);
		
		MDRadioButton mdRadioButton = new MDRadioButton(
				this);
		BayesianRadioButton byRadioButton = new BayesianRadioButton(
				this);
        
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(normalRadioButton);
		buttonGroup.add(extRadioButton);
		buttonGroup.add(coopRadioButton);
		buttonGroup.add(mdRadioButton);
		buttonGroup.add(byRadioButton);

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));
		panel.setSize(5,3);
		panel.add(normalRadioButton);
		panel.add(extRadioButton);
		panel.add(coopRadioButton);
		panel.add(mdRadioButton);
		panel.add(byRadioButton);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(panel);

		return;
	}

	/**
	 * adds the file open button to this panel
	 */
	public void addFileOpenButton() {
		FileOpenPanel panel = new FileOpenPanel();

		panel.setLayout(new GridLayout(3, 1));

		JLabel label = new JLabel("Select input file :");
		label.setForeground(Color.BLUE);
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.add(label);

		FileTextField fileTextField = new FileTextField(this);
		this.add(fileTextField);

		FileOpenButton fileOpenButton = new FileOpenButton(this);
		this.add(fileOpenButton);

		return;
	}

	/**
	 * adds the normal form algorithms combo box to this panel
	 */
	public void addNormalAlgoComboBox() {
		String[] algoList = { "Correlated Equilibrium", "Lemke-Howson",
				"Mangasarian Algorithm", "Mixed Integer Programming",
				"Pure Nash Equilibrium", "Simple Search Method",
				"Two Person Zero-sum Game", };
		this.add(new AlgorithmComboBox(this, algoList));
		this.updateUI();
		return;
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 100);
	}

	public void addCoopGameComboBox()  {
		String[] algoList = { "Core",
		"Shapley Value", "Approximate Shapley Value using Sampling" , "Nucleolus" };

this.add(new CoopGameComboBox(this, algoList));
this.updateUI();
return;
	}
	
	
	public void addMDComboBox()  {
		String[] algoList = { "EPE",
		"Dictatorial", "DSIC", "BIC", "Ex-Post IR", "IIR", "Ex-Ante IR" };

this.add(new MDComboBox(this, algoList));
this.updateUI();
return;
	}
	
	
	
	/**
	 * adds the extensive form algorithms combo box to this panel
	 */
	public void addExtensiveAlgoComboBOx() {
		String[] algoList = { "Extensive to Normal Form",
				"Sequence Form Algorithm" };

		this.add(new ExtensiveComboBox(this, algoList));
		this.updateUI();
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		/*
		 * This is implemented with command design pattern. This object doesn't
		 * know exactly which is the source object for this event. It gets the
		 * object with command interface and calls execute method.
		 */
		Command command = (Command) e.getSource();
		command.execute();

		return;
	}

}
