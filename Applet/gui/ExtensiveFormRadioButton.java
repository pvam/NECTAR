/**
 * 
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author kalyan
 * 
 */
public class ExtensiveFormRadioButton extends JRadioButton implements Command {

	protected Mediator mediator = null;

	/**
	 * This constructor takes the listener interface and the label and assigns
	 * to it.
	 * 
	 * @param listener :
	 *            Action listener for handling event
	 * @param buttonName :
	 *            label for this radio button instance
	 */
	public ExtensiveFormRadioButton(ActionListener listener) {
		super("Extensive Form Game");
		initGUI(listener);
	}

	/**
	 * It initializes the GUI and adds the listener which handles the action
	 * event. It also registers with mediator object.
	 * 
	 * @param listener :
	 *            Action listener for handling event
	 */
	private void initGUI(ActionListener listener) {

		// gets the mediator object
		this.mediator = Mediator.Instance();
		// registers with the mediator object
		mediator.registerExtensiveFromRadioButton(this);
		// adds the actionlistener to this object
		this.addActionListener(listener);

		// sets the gui
		this.setForeground(Color.BLUE);
		this.setMnemonic(KeyEvent.VK_E);
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		mediator.extensiveFormSelected();
		//System.out.println("Extensive Form Game has selected");
	}

}
