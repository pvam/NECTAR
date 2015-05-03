/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

/**
 * @author kalyan
 * 
 */
public class MenuPanel extends JPanel implements ActionListener {

	JToolBar toolBar = null;

	JPanel panel = null;

	Dimension dimension = null;

	JLabel timeLabel = null;

	public MenuPanel() {
		super();
		initGUI();
	}

	/**
	 * initializes the GUI
	 */
	private void initGUI() {
		panel = new JPanel();

		this.setBorder(new EtchedBorder());

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		dimension = new Dimension(40, 40);

		this.addClearButton();
		this.addComputeButton();
		this.addShowGameButton();
		// this.addStopButton();
		this.addChangeAlgoButton();
		this.addChangeFileButton();
		this.addStoreButton();
		this.addShowExtensiveFormButton();
		this.addShowSequenceFormButton();

		this.add(toolBar);

		this.addLabel();
	}

	/**
	 * adds the time label to this panel
	 */
	public void addLabel() {
		TimeLabel timeLabel = new TimeLabel();
		this.add(timeLabel);
		return;
	}

	/**
	 * adds the compute button to the tool bar
	 */
	public void addComputeButton() {
		ComputeButton computeButton = new ComputeButton(this, dimension);
		toolBar.add(computeButton);
		return;
	}

	/**
	 * adds the show sequuence form button to the tool bar
	 */
	public void addShowSequenceFormButton() {
		ShowSequenceFormButton button = new ShowSequenceFormButton(this,
				dimension);
		toolBar.add(button);
		return;
	}

	/**
	 * adds the show extensive form button to the tool bar
	 */
	public void addShowExtensiveFormButton() {
		ShowExtensiveFormButton button = new ShowExtensiveFormButton(this,
				dimension);
		toolBar.add(button);
		return;
	}

	/**
	 * adds the show normal form button to the tool bar
	 */
	public void addShowGameButton() {
		ShowGameButton showGameButton = new ShowGameButton(this, dimension);
		toolBar.add(showGameButton);
		return;
	}

	/**
	 * adds the stop Button to the tool bar
	 */
	public void addStopButton() {
		StopButton stopButton = new StopButton(this, dimension);
		toolBar.add(stopButton);
		return;
	}

	/**
	 * adds the clear(Home) button to the tool bar
	 */
	public void addClearButton() {
		ClearButton clearButton = new ClearButton(this, dimension);
		toolBar.add(clearButton);
		return;
	}

	/**
	 * adds the change algorithm button to the tool bar
	 */
	public void addChangeAlgoButton() {
		ChangeAlgoButton algoButton = new ChangeAlgoButton(this, dimension);
		toolBar.add(algoButton);
		return;
	}

	/**
	 * adds the change file button to the tool bar
	 */
	public void addChangeFileButton() {
		ChangeFileButton fileButton = new ChangeFileButton(this, dimension);
		toolBar.add(fileButton);
		return;
	}

	/**
	 * adds the store result button to the tool bar
	 */
	public void addStoreButton() {
		StoreResultButton storeButton = new StoreResultButton(this, dimension);
		toolBar.add(storeButton);
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	/**
	 * It handles the action event
	 */
	public void actionPerformed(ActionEvent e) {
		/*
		 * This is implemented with command design pattern. This object dont
		 * know exactly which is the source object for this event. It gets the
		 * object with command interface and calls execute method.
		 */
		Command command = (Command) e.getSource();
		command.execute();

		return;
	}

}
