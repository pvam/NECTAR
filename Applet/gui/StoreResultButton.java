/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * @author kalyan
 * 
 */
public class StoreResultButton extends JButton implements Command {
	protected Mediator mediator = null;

	protected JFileChooser fileChooser = null;

	public StoreResultButton(ActionListener listener, Dimension dimension) {
		super();
		initGUI(listener, dimension);
	}

	/**
	 * initializes the GUI
	 * 
	 * @param listener :
	 *            action listener
	 * @param dimension :
	 *            dimension of the button
	 */
	private void initGUI(ActionListener listener, Dimension dimension) {
		this.mediator = Mediator.Instance();
		mediator.registerStoreResultButton(this);

		Icon imageIcon = new ImageIcon("images/store.png");
		String toolTip = "Stores the results in a specified file";
		String command = "store";

		this.setMnemonic(KeyEvent.VK_S);
		this.setActionCommand(command);
		this.setToolTipText(toolTip);
		this.setIcon(imageIcon);
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(dimension);

		this.addActionListener(listener);

		return;
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
		File file = this.storeResultsIntoFile();
		mediator.fileSaveSelected(file);
	}

	private File storeResultsIntoFile() {
		int returnValue;
		File file = null;

		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("results"));

		returnValue = fileChooser.showSaveDialog(this);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			// System.out.println("Saved File : " + file.getName() + "\tAbsolute
			// Path " + file.getAbsolutePath());
			return file;
		}

		return null;
	}

}
