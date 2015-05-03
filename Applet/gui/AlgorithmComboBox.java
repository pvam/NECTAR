/**
 * 
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author kalyan
 * 
 */
public class AlgorithmComboBox extends JComboBox implements Command {

	protected Mediator mediator = null;

	/**
	 * This constructor takes the action listener and list of labels and add
	 * these to this instance
	 * 
	 * @param listener :
	 *            action listener
	 * @param list :
	 *            list of labels
	 */
	public AlgorithmComboBox(ActionListener listener, String[] list) {
		super(list);
		initGUI(listener);
	}

	/**
	 * Initializes the GUI and registers with mediator object
	 * 
	 * @param listener :
	 *            Action Listener
	 */
	private void initGUI(ActionListener listener) {
		// get the mediator instance
		this.mediator = Mediator.Instance();
		// registers with mediator
		mediator.registerAlgorithmComboBox(this);

		// setting the GUI
		this.setForeground(Color.BLUE);
		this.setSize(25, 25);
		this.addActionListener(listener);
		this.updateUI();
		return;
	}

	/**
	 * It implements the command interface such that it handles its event
	 * itself. (See Command Pattern)
	 */
	public void execute() {
		String algorithmName = (String) this.getSelectedItem();
		mediator.setAlgorithm(algorithmName);
		return;
	}
}
