/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;

/**
 * @author kalyan
 * 
 */
public class OutputPanel extends JPanel {

	private Mediator mediator = null;

	private boolean additionalPanel = false;

	public OutputPanel() {
		super();
		this.initGUI();
	}

	/**
	 * initializes the GUI
	 */
	private void initGUI() {
		this.setBorder(new EtchedBorder());
		this.setLayout(new BorderLayout());

		this.mediator = Mediator.Instance();
		mediator.registerOutputPanel(this);

		addOutputTextArea();

		this.updateUI();
	}

	/**
	 * adds the output text area
	 */
	private void addOutputTextArea() {
		OutputTextArea outputTextArea = new OutputTextArea();

		JScrollPane scrollPane = new JScrollPane(outputTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scrollPane, BorderLayout.CENTER);

		return;
	}

	/**
	 * It adds the additional panel to this panel
	 */
	public void addAdditionalPanel() {
		AdditionalPanel additionalPanel = new AdditionalPanel();

		String algorithmName = mediator.getAlgorithmName();

		if (algorithmName.equals("Simple Search Method")) {
			additionalPanel.addSimpleSearchRadioButtons();
		} else if (algorithmName.equals("Mixed Integer Programming")) {
			additionalPanel.addMIPRadioButtons();
		} else {
			// TODO:
		}
		this.add(additionalPanel, BorderLayout.NORTH, 0);
		this.additionalPanel = true;

		this.updateUI();
		return;
	}

	/**
	 * It removes the additional panel from this panel
	 */
	public void removeAdditionalPanel() {
		if (additionalPanel == true) {
			this.remove(0);
			this.additionalPanel = false;
		}

		this.updateUI();

		return;
	}
}
