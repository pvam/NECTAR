/**
 * 
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * @author kalyan
 * 
 */
public class StopButton extends JButton implements Command {

	protected Mediator mediator = null;

	public StopButton(ActionListener listener, Dimension dimension) {
		super();
		initGUI(listener, dimension);

	}

	/**
	 * initializes the GUI
	 * 
	 * @param listener :
	 *            action listener
	 * @param dimension :
	 *            dimension of the button
	 */
	private void initGUI(ActionListener listener, Dimension dimension) {
		this.mediator = Mediator.Instance();
		mediator.registerStopButton(this);

		Icon imageIcon = new ImageIcon("images/compute.png");
		String toolTip = "Stops the selected Algorithm";
		String command = "stop";

		this.setMnemonic(KeyEvent.VK_Q);
		this.setActionCommand(command);
		this.setToolTipText(toolTip);
		this.setIcon(imageIcon);
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(dimension);

		this.addActionListener(listener);

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
		// Note: This is not implemented
	}

}
