package src.main.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener{

	private ElevatorView view;
	
	public Controller(){
		 view = new ElevatorView();
		 view.addExitListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
