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
public class ClearButton extends JButton implements Command {

	protected Mediator mediator = null;

	/**
	 * This constructor takes the listener and dimension of the button and
	 * assign to this instance.
	 * 
	 * @param listener
	 *            action listener
	 * @param dimension
	 *            dimension of the button
	 */
	public ClearButton(ActionListener listener, Dimension dimension) {
		super();
		initGUI(listener, dimension);
	}

	/**
	 * It sets the GUI and registers with mediator
	 * 
	 * @param listener
	 *            action listener
	 * @param dimension
	 *            dimension of the button
	 */
	private void initGUI(ActionListener listener, Dimension dimension) {
		// get the mediator instance
		this.mediator = Mediator.Instance();
		// register with the mediator object
		mediator.registerClearButton(this);
		// add the action listener
		this.addActionListener(listener);

		// setting the GUI
		Icon imageIcon = new ImageIcon("images/home.png");
		String toolTip = "Clears the whole game";
		String command = "clear";

		this.setActionCommand(command);
		this.setToolTipText(toolTip);
		this.setIcon(imageIcon);
		this.setMnemonic(KeyEvent.VK_H);
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
		mediator.setInitialStatus();
		return;
	}

}
