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
public class ChangeFileButton extends JButton implements Command {
	protected Mediator mediator = null;

	/**
	 * This constructor takes action listener and dimension of this button
	 * object and sets.
	 * 
	 * @param listener
	 *            action listener
	 * @param dimension
	 *            dimension of the button
	 */
	public ChangeFileButton(ActionListener listener, Dimension dimension) {
		super();
		initGUI(listener, dimension);

	}

	/**
	 * It adds the listener and initializes the GUI and registers with mediator
	 * 
	 * @param listener
	 *            Action Listener
	 * @param dimension
	 *            Dimension of the button
	 */
	private void initGUI(ActionListener listener, Dimension dimension) {
		// getting the mediator object
		this.mediator = Mediator.Instance();
		// registers with the mediator object
		mediator.registerChangeFileButton(this);
		// adds the action listener
		this.addActionListener(listener);

		// setting the GUI
		Icon imageIcon = new ImageIcon("images/changefile.png");
		String toolTip = "Changes the inpute file";
		String command = "file";
		this.setMnemonic(KeyEvent.VK_F);
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
		mediator.changeFile();
	}
}
