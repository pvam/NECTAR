/**
 * 
 */
package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * @author kalyan
 * 
 */
public class FileOpenButton extends JButton implements Command {

	protected Mediator mediator = null;

	protected JFileChooser fileChooser = null;

	/**
	 * This constructor takes the listener and sets to this instance
	 * 
	 * @param listener
	 *            Action Listener
	 */
	public FileOpenButton(ActionListener listener) {
		super("Browse..");
		initGUI(listener);
	}

	/**
	 * It initializes the GUI of this component and registers with mediator
	 * object.
	 * 
	 * @param listener
	 *            Action Listener
	 */
	private void initGUI(ActionListener listener) {
		// gets the mediator object
		this.mediator = Mediator.Instance();
		// registers with the mediator object
		mediator.registerFileOpen(this);
		// adds the action listener to this instance
		this.addActionListener(listener);

		// sets the gui
		Icon imageIcon = new ImageIcon("images/open.png");
		String toolTip = "Selects the specified file";
		String command = "open";

		this.setForeground(Color.BLUE);
		this.setMnemonic(KeyEvent.VK_I);
		this.setActionCommand(command);
		this.setToolTipText(toolTip);
		this.setIcon(imageIcon);
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(50, 50));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);

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
		openFile();
		return;
	}

	/**
	 * It provides the GUI to the user to select the file which has to open and
	 * passes this information to mediator object.
	 */
	private void openFile() {
		int returnValue;
		File file = null;
		String fileName = null;

		if (mediator.isNormalFormSelected()) {
			fileName = new String("examples/nfg");
		} else {
			fileName = new String("examples/efg");
		}

		fileChooser = new JFileChooser();

		fileChooser.setCurrentDirectory(new File(fileName));

		returnValue = fileChooser.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			mediator.fileOpenSelected(file);
			// System.out.println("Opened File : " + file.getName() +
			// "\tAbsolute Path " + file.getAbsolutePath());
			// TODO: IMPLEMENT THE REMAINING CODE
		} else {
			// FIXME: implement the remaining code
		}
		return;
	}
}
