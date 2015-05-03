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
public class TimeLabel extends JLabel {

	Mediator mediator = null;

	public TimeLabel() {
		super(": 0.0 Sec", new ImageIcon("images/time.png"), Label.RIGHT);
		initGUI();
	}

	/**
	 * initializes the GUI component
	 */
	private void initGUI() {
		this.setHorizontalAlignment(Label.RIGHT);
		this.setFont(new Font("Serif", Font.BOLD, 14));
		this.setForeground(Color.BLUE);

		this.mediator = Mediator.Instance();
		mediator.registerTimeLabel(this);

		return;
	}

	/**
	 * sets the time in time label
	 * 
	 * @param time :
	 *            time in milli seconds
	 */
	public void setTime(long time) {
		String str = new String(": " + ((double) time / 1000) + " Sec");
		this.setText(str);
		this.setEnabled(true);
		return;
	}

	/**
	 * reset time to zero
	 */
	public void reset() {
		this.setText(": 0.0 Sec");
	}

}
