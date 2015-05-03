/**
 * 
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author kalyan
 * 
 */
public class AdditionalRadioButton extends JRadioButton implements Command {

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
	public AdditionalRadioButton(ActionListener listener, String buttonName) {
		super(buttonName);
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
		// getting the mediator instance
		this.mediator = Mediator.Instance();
		// registering
		mediator.registerAdditionalRadioButton(this);
		// adding the listener
		this.addActionListener(listener);
		// set alignment in the container(additional panel)
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
		mediator.additionalButtonSelected();
		System.out.println("One Nash Equilibrium Selected");
		return;
	}
}
