/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.*;

/**
 * @author kalyan
 * 
 */
public class MessagePanel extends JPanel {

	// protected Mediator mediator = null;

	public MessagePanel() {
		super();
		initGUI();
	}

	/**
	 * initializes the GUI
	 */
	private void initGUI() {
		this.setBorder(new EtchedBorder());

		MessageLabel label = new MessageLabel();

		this.add(label);

		return;
	}

}
