/**
 * 
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

;

/**
 * @author kalyan
 * 
 */
public class NormalFormRadioButton extends JRadioButton implements Command {

	Mediator mediator = null;

	public NormalFormRadioButton(ActionListener listener) {
		super("Normal Form Game");
		initGUI(listener);
	}

	/**
	 * initializes the GUI
	 * 
	 * @param listener :
	 *            action listener
	 */
	private void initGUI(ActionListener listener) {
		this.mediator = Mediator.Instance();
		this.addActionListener(listener);
		mediator.registerNormalFormRadioButton(this);

		this.setForeground(Color.BLUE);
		this.setMnemonic(KeyEvent.VK_N);
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
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
		// this.setEnabled(false);
		mediator.normalFormSelected();
		//System.out.println("Normal Form Game has selected");
		return;
	}

}
