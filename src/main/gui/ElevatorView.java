package src.main.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class ElevatorView extends JFrame {

	private JButton[][] elevatorSubsystem;
	private JMenuBar bar;
	private JMenu menu;
	private JMenuItem reset;
	private JMenuItem exit;
	//variable for the model.

	public ElevatorView() {

		super("Elevator Subsystem");
		elevatorSubsystem = new JButton[22][4];

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(1000, 1000);
		this.setLayout(new GridLayout(22,4));

		//add menu bar and menu items
		bar = new JMenuBar();
		setJMenuBar(bar);
		menu = new JMenu("File");
		bar.add(menu);

		//Setup for exit
		exit = new JMenuItem("Exit");
		menu.add(exit);

		//add direction arrows for floors
		for(int a = 21; a>=0; a--){
			elevatorSubsystem[a][0] = new JButton(String.format("%d", a+1));
			add(elevatorSubsystem[a][0]);
		}

		this.setVisible(true);
	}

}
