/**
 * 
 */
package gui;

import javax.swing.*;

import java.awt.*;

import javax.swing.UnsupportedLookAndFeelException;
import com.birosoft.liquid.LiquidLookAndFeel;

//import app

//import bi

/**
 * @author kalyan
 * 
 */
public class NectarFrame extends JFrame {

	public NectarFrame() {
		initGUI();
	}

	/**
	 * It initializes the GUI component
	 */
	private void initGUI() {
		this.setTitle("N E C T A R");
		this.setSize(740, 650);
		//this.setBackground(Color.MAGENTA);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	
		NectarPanel panel = new NectarPanel();

		this.getContentPane().add(panel);

		show();
	}

	/**
	 * This is the first method called
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			//javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows");
			
			javax.swing.UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);	// to decorate frames 
			JDialog.setDefaultLookAndFeelDecorated(true);
		
			//javax.swing.plaf.nimbus.NimbusLookAndFeel
		//com.sun.java.swing.plaf.nimbus
		
		//javax.swing.plaf.
		
//			com.sun.java.swing.plaf.windows
		
		//com.com.birosoft.liquid.LiquidLookAndFeel
		}
		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		NectarFrame frame = new NectarFrame();
		frame.setVisible(true);
	}
}
