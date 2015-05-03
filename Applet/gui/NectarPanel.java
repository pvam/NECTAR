/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * @author kalyan
 * 
 */
public class NectarPanel extends JPanel {

	Mediator mediator = null;

	public NectarPanel() {
		super();
		initGUI();
	}

	/**
	 * initializes the GUI and adds the all panels and registers with mediator
	 * object
	 */
	private void initGUI() {
		mediator = Mediator.Instance();

		this.setLayout(new BorderLayout(5, 5));
		this.setBorder(new EtchedBorder());

		MenuPanel menuPanel = new MenuPanel();
		InputPanel inputPanel = new InputPanel();
		OutputPanel outputPanel = new OutputPanel();
		MessagePanel messagePanel = new MessagePanel();

		this.add(menuPanel, BorderLayout.NORTH);
		this.add(inputPanel, BorderLayout.WEST);
		this.add(outputPanel, BorderLayout.CENTER);
		this.add(messagePanel, BorderLayout.SOUTH);

		mediator.setInitialStatus();
	}

}
