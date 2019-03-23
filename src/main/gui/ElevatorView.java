package src.main.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ElevatorView extends JFrame {

	private JButton[][] elevatorSubsystem;
	private JMenuBar bar;
	private JMenu menu;
	private JMenuItem exit;
	private JPanel panel;

	public ElevatorView() {

		super("Elevator Subsystem");
		elevatorSubsystem = new JButton[22][4];
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(1000, 1000);
		panel = new JPanel(new GridLayout(22,4));

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
			for(int b = 0; b<4 ;b++){
				if(b==0){
					elevatorSubsystem[a][b] = new JButton(String.format("%d", a+1));
					panel.add(elevatorSubsystem[a][b]);
				}
				if(b==1){
					elevatorSubsystem[a][b] = new JButton("");
					panel.add(elevatorSubsystem[a][b]);
				}
				if(b==2){
					elevatorSubsystem[a][b] = new JButton("UP");
					panel.add(elevatorSubsystem[a][b]);
				}
				if(b==3){
					elevatorSubsystem[a][b] = new JButton("DOWN");
					panel.add(elevatorSubsystem[a][b]);
				}
			}
		}

		this.setContentPane(panel);
		this.pack();
		this.setVisible(true);
	}

	//add listener for exit menu item
	public void addExitListener(ActionListener actionListener){
		exit.addActionListener(actionListener);
	}

	//add listener for exit menu item
	public void addButtonListener(ActionListener actionListener){
		for(int i = 0; i<22; i++){
			for(int j = 0; j<4; j++){
				elevatorSubsystem[i][j].addActionListener(actionListener);
			}
		}
	}

	//exit gui
	public void exitClicked(){
		System.exit(0);
	}

}
