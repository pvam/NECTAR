/**
 * 
 */
package gui;

import javax.swing.*;

import java.awt.*;

/**
 * @author kalyan
 * 
 */
public class MessageLabel extends JLabel {

	private Mediator mediator = null;

	private String defaultTip = null;

	public MessageLabel() {
		super("NECTAR - Nash Equilibria CompuTAtion Resource");
		initGUI();
	}

	/**
	 * initializes the GUI
	 */
	private void initGUI() {
		this.setHorizontalAlignment(Label.LEFT);
		this.setFont(new Font("Serif", Font.BOLD, 14));
		this.setForeground(Color.DARK_GRAY);
		this.defaultTip = new String(
				"NECTAR - Nash Equilibria CompuTAtion Resource");

		this.mediator = Mediator.Instance();
		mediator.registerMessageLabel(this);

		return;
	}

	/**
	 * sets the tip
	 * 
	 * @param tip
	 */
	public void setTip(String tip) {
		this.setText(tip);
		// this.setEnabled(true);
		return;
	}

	/**
	 * resets the tip and puts default tip
	 */
	public void reset() {
		this.setText(defaultTip);
	}
}
