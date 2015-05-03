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
public class ShowExtensiveFormButton extends JButton implements Command {
	protected Mediator mediator = null;

	public ShowExtensiveFormButton(ActionListener listener, Dimension dimension) {
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
		mediator.registerShowExtensiveFormButton(this);

		Icon imageIcon = new ImageIcon("images/extform.png");
		String toolTip = "Displays the Extensive form game";
		String command = "show_ext";

		this.setMnemonic(KeyEvent.VK_E);
		this.setActionCommand(command);
		this.setToolTipText(toolTip);
		this.setIcon(imageIcon);
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(dimension);

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
		mediator.showExtGame();
	}

}
