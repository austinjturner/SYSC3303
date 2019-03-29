package src.main.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class ElevatorView extends JFrame {

	private JLabel[] elevatorRepresentation;
	private JMenuBar bar;
	private JMenu menu;
	private JMenuItem exit;
	private JPanel panel;

	public ElevatorView(ElevatorModel model) {

		super("Elevator Subsystem");
		elevatorRepresentation = new JLabel[4];
		Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(1000, 1000);
		panel = new JPanel(new GridLayout(1,4));

		//add menu bar and menu items
		bar = new JMenuBar();
		setJMenuBar(bar);
		menu = new JMenu("File");
		bar.add(menu);

		//Setup for exit
		exit = new JMenuItem("Exit");
		menu.add(exit);

		//add Elevator labels
		for(int a = 0; a<4; a++){
			elevatorRepresentation[a] = new JLabel(String.format("Elevator %d", a+1), SwingConstants.CENTER);
			elevatorRepresentation[a].setBorder(border);
			panel.add(elevatorRepresentation[a]);
		}

		this.setContentPane(panel);
		this.pack();
		this.setVisible(true);
	}

	//add listener for exit menu item
	public void addExitListener(ActionListener actionListener){
		exit.addActionListener(actionListener);
	}

	//exit gui
	public void exitClicked(){
		System.exit(0);
	}

	/**
	 * refresh the view with the new state of the model
	 */
	public void udpateUI(ElevatorModel model) {

		for(int i = 0; i <4; i++){
			//elevatorRepresentation[i]
		}
	}

}
