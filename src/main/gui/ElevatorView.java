package src.main.gui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

import src.main.settings.Settings;

public class ElevatorView extends JFrame {
	
	private static final long serialVersionUID = 1L;  // Fixes compiler warning
	
	private final int MAX_SCREEN_REFRESH_TIME_MS = 30;
	private JMenuBar bar;
	private JMenu menu;
	private JMenuItem exit;
	
	private boolean updating = false;
	private ElevatorModel latestModel = null;
	
	/**
	 * Initialize UI elements
	 * @param model
	 */
	public ElevatorView(ElevatorModel model) {
		super("Elevator Subsystem");
		this.setPreferredSize(new Dimension(1250, 400));
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//add menu bar and menu items
		bar = new JMenuBar();
		setJMenuBar(bar);
		menu = new JMenu("File");
		bar.add(menu);

		//Setup for exit
		exit = new JMenuItem("Exit");
		menu.add(exit);
		
		// Draw initial state using provided model
		updateUI(model);  
	}


	/**
	 * refresh the view with the new state of the model
	 */
	public void updateUI(ElevatorModel newModel) {
		// Limit frequency that screen is drawn
		if (updating) {
			this.latestModel = newModel;
			return;
		}
		this.latestModel = null;
		
		JPanel updatedPanel = new JPanel(new GridLayout(1,Settings.NUMBER_OF_ELEVATORS));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1 + Settings.NUMBER_OF_ELEVATORS);
		
		//add Elevator labels
		for(int a = 0; a < Settings.NUMBER_OF_ELEVATORS; a++){
			
			Color textColor = Color.BLACK;
			
			StringBuilder sb = new StringBuilder(128);
			
			String columnLine =  "----------------------------------------------------------------";
			String columnEmpty =  "                                                                ";
			String lineRow = "<tr><td colspan=\"2\">"+columnLine+"</td></tr>";
			String emptyRow = "<tr><td colspan=\"2\">"+columnEmpty+"</td></tr>";
			

			/*
			 * Display elevator ID
			 */
			sb.append("<html>");
			sb.append("<table border='0'>");
			sb.append("<tr>");
			sb.append("<td colspan=\"2\" align='center'><span style=\"font-size: 150%\">Elevator "+(a+1)+"</span></td>");
			sb.append(lineRow);
			
			/*
			 * Display floor number
			 */
			sb.append("<td align='center'>Current Floor:</td>");
			sb.append("<td align='center'>"+newModel.getCurrentFloor(a)+"</td>");
			sb.append("</tr>");
			sb.append("<tr>");
			
			/*
			 * Display elevator direction
			 */
			sb.append("<td align='center'>Direction:</td>");
			if (!newModel.getMoving(a)) {
				sb.append("<td align='center'>Stopped</td>");
			} else if(newModel.isGoingUp(a)) {
				sb.append("<td align='center'>Going Up</td>");
			} else {
				sb.append("<td align='center'>Going Down</td>");
			}
			sb.append("</tr>");
			sb.append(emptyRow);
			sb.append("<tr>");
			
			
			/*
			 * Display errors
			 */
			if (newModel.getError(a) == null) {
				sb.append(emptyRow);
			} else {
				sb.append("<td colspan=\"2\" align='center'>FAULT: "+newModel.getError(a)+"</td>");
				textColor = Color.RED;
				if (newModel.getError(a).contains("FailedToStop")) {
					sb.append("</tr><tr>");
					sb.append("<td colspan=\"2\" align='center'><span style=\"font-size: 150%\">Unrecoverable Failure</span></td>");
				}
			}
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("</html>");
			
			
			JLabel jl = new JLabel(sb.toString(), SwingConstants.CENTER);
			jl.setForeground(textColor);
			jl.setBorder(border);
			updatedPanel.add(jl);
		}
		
		this.setContentPane(updatedPanel);
		this.pack();
		this.setVisible(true);
		
		// Kick off next update
		this.updating = true;
		new ScreenTimer(this).start();
	}
	

	/**
	 * Add listener for exit menu item
	 * @param actionListener
	 */
	public void addExitListener(ActionListener actionListener){
		exit.addActionListener(actionListener);
	}

	/**
	 * Exit SchedulerSubsystem
	 */
	public void exitClicked(){
		System.exit(0);
	}
	
	
	/**
	 * The ScreenTimer ensures that we do not re-draw the screen more than
	 * once ever MAX_SCREEN_REFRESH_TIME_MS milliseconds.
	 * This prevents flickering in the GUI
	 * 
	 * @author austinjturner
	 *
	 */
	class ScreenTimer extends Thread {
		ElevatorView ev;
		ScreenTimer(ElevatorView ev){
			this.ev = ev;
		}
		
		public void run() {
			try {
				Thread.sleep(ev.MAX_SCREEN_REFRESH_TIME_MS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ev.updating = false;
			if (ev.latestModel != null) {
				ev.updateUI(ev.latestModel);
			}
		}
	}
}
