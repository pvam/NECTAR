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
public class ShowSequenceFormButton extends JButton implements Command {
	protected Mediator mediator = null;

	public ShowSequenceFormButton(ActionListener listener, Dimension dimension) {
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
		mediator.registerShowSequenceFormButton(this);

		Icon imageIcon = new ImageIcon("images/seqform.png");
		String toolTip = "Displays the Sequence form game";
		String command = "show_seq";

		this.setMnemonic(KeyEvent.VK_Q);
		this.setActionCommand(command);
		this.setToolTipText(toolTip);
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(dimension);
		this.setIcon(imageIcon);

		this.addActionListener(listener);

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
		mediator.showSeqGame();
	}

}
