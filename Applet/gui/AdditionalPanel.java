/**
 * 
 */
package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author kalyan
 * 
 */
public class AdditionalPanel extends JPanel implements ActionListener {

	public AdditionalPanel() {
		initGUI();
	}

	/**
	 * Initializes the GUI
	 */
	private void initGUI() {
		this.setLayout(new GridLayout(0, 1));

		JLabel label = new JLabel("Select One :");
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.add(label);
	}

	/**
	 * It adds the radio buttons to the additional panel and sets the
	 * corresponding label for simple search method algorithm.
	 */
	public void addSimpleSearchRadioButtons() {
		AdditionalRadioButton oneNashRadioButton = new AdditionalRadioButton(
				this, "One Nash Equilibrium");
		AdditionalRadioButton allNashRadioButton = new AdditionalRadioButton(
				this, "All Nash Equilibria");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(oneNashRadioButton);
		buttonGroup.add(allNashRadioButton);

		this.add(oneNashRadioButton);
		this.add(allNashRadioButton);

		return;
	}

	/**
	 * It adds the radio buttons to the addtional panel and sets the
	 * correponding label for mixed integer programming method algorithm.
	 */
	public void addMIPRadioButtons() {
		int i;
		AdditionalRadioButton[] additionalRadioButtons = new AdditionalRadioButton[4];

		ButtonGroup buttonGroup = new ButtonGroup();

		for (i = 0; i < 4; i++) {
			additionalRadioButtons[i] = new AdditionalRadioButton(this,
					("Formulation " + (i + 1)));
			buttonGroup.add(additionalRadioButtons[i]);
			this.add(additionalRadioButtons[i]);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	/**
	 * This method implements the action listener interface when an event
	 * receives it gets corresponding command object and calls the execute()
	 * method
	 */
	public void actionPerformed(ActionEvent e) {

		Command command = (Command) e.getSource();
		command.execute();

		return;
	}

}
