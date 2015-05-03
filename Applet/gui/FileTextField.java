/**
 * 
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * @author kalyan
 * 
 */
public class FileTextField extends JTextField implements Command {
	protected Mediator mediator = null;

	public FileTextField(ActionListener listener) {
		super(20);
		initGUI(listener);
	}

	/**
	 * Sets the gui environment, registers with mediator and set the action
	 * listener
	 * 
	 * @param listener :
	 *            A listener which will be called an event has occured
	 */
	private void initGUI(ActionListener listener) {
		// get the mediator project
		this.mediator = Mediator.Instance();
		mediator.registerFileTextField(this);
		// sets the listener
		this.addActionListener(listener);
		// set gui
		this.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.setEditable(true);

		return;
	}

	/**
	 * set to the initial state
	 */
	public void setInitialState() {
		this.setEnabled(false);
		this.setToolTipText("Selects the file");
		this.setText(" ");
		return;
	}

	/**
	 * It sets the file name in the text file
	 * 
	 * @param file :
	 *            A file object which contains the details of file
	 */
	public void setFile(File file) {
		this.setText(file.getAbsolutePath());
		this.setToolTipText(file.getAbsolutePath());
		this.setEnabled(false);
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gui.Command#execute()
	 */
	/**
	 * This is called when an event occured i.e when a user writes a file path
	 * in the text field and presses enter. It implements the command interface
	 * such that it handles its event itself. (See Command Pattern)
	 */
	public void execute() {

		String fileName = this.getText();
		File file = null;

		if (fileName != null) {
			file = new File(fileName);
			/*
			 * intimate to mediator object and send file object to the mediator
			 */
			mediator.fileOpenSelected(file);
		} else {
			// TODO: WRITE RELATED CODE HERE
		}
		return;
	}
}
