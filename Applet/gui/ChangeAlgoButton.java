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
public class ChangeAlgoButton extends JButton implements Command {

	protected Mediator mediator = null;

	/**
	 * This constructor takes the listener and dimension of this button and sets
	 * to this instance
	 * 
	 * @param listener
	 *            Action Listener
	 * @param dimension
	 *            dimension of the button
	 */
	public ChangeAlgoButton(ActionListener listener, Dimension dimension) {
		super();
		initGUI(listener, dimension);

	}

	/**
	 * It initializes the GUI of this component and registers with mediator
	 * object.
	 * 
	 * @param listener
	 *            Action Listener
	 * @param dimension
	 *            Dimension of the button
	 */
	private void initGUI(ActionListener listener, Dimension dimension) {
		// get the mediator object
		this.mediator = Mediator.Instance();
		// register with the mediator object
		mediator.registerChangeAlgoButton(this);
		// add the action listner
		this.addActionListener(listener);

		// sets the GUI
		Icon imageIcon = new ImageIcon("images/changealgo.png");
		String toolTip = "Changes the algorithm";
		String command = "compute";

		this.setMnemonic(KeyEvent.VK_A);
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
		mediator.changeAlgorithm();
	}
}
