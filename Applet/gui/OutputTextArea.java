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
public class OutputTextArea extends JTextArea {

	protected Mediator mediator = null;

	public OutputTextArea() {
		super();
		initGUI();
	}

	/**
	 * initializes the GUI
	 */
	private void initGUI() {
		this.mediator = Mediator.Instance();
		mediator.registerOutputTextArea(this);
		this.setForeground(Color.BLUE);
		// this.setBackground(Color.LIGHT_GRAY);
		this.setFont(new Font("Serif", Font.BOLD, 14));
		this.setText("");
		this.setEditable(false);

		return;
	}

	/**
	 * it displays the string information on the output text area
	 * 
	 * @param string :
	 *            information to be displayed on the output text area
	 */
	public void showOutput(String string) {
		this.setText("");
		this.append(string);

		return;
	}

	/**
	 * clears the output text area
	 */
	public void clearOutputArea() {
		this.showOutput("");
		return;
	}
}
