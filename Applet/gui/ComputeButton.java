/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;

/**
 * @author kalyan
 * 
 */
public class ComputeButton extends JButton implements Command {

	protected Mediator mediator = null;

	/**
	 * This constructor takes the action listener and dimension of the button
	 * and sets to this object
	 * 
	 * @param listener :
	 *            action listener
	 * @param dimension :
	 *            dimension of the button
	 */
	public ComputeButton(ActionListener listener, Dimension dimension) {
		super();
		initGUI(listener, dimension);

	}

	/**
	 * It sets the GUI and adds the listener and registers with mediator object.
	 * 
	 * @param listener :
	 *            action listener
	 * @param dimension :
	 *            dimension of this button
	 */
	private void initGUI(ActionListener listener, Dimension dimension) {
		// gets the mediator object
		this.mediator = Mediator.Instance();
		// registers with the mediator object
		mediator.registerComputeButton(this);
		// adds action listener to this object
		this.addActionListener(listener);

		// sets the GUI
		Icon imageIcon = new ImageIcon("images/compute.png");
		String toolTip = "Computes the selected Algorithm";
		String command = "compute";

		this.setMnemonic(KeyEvent.VK_C);
		this.setActionCommand(command);
		this.setToolTipText(toolTip);
		this.setIcon(imageIcon);
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(dimension);
		
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Command#execute()
	 */
	/**
	 * It implements the command interface such that it handles its event
	 * itself. (See Command Pattern)
	 */
	public void execute() {
		mediator.computeAlgorithm();
	}

}
