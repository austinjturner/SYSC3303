package src.main.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener{

	private ElevatorView view;

	public Controller(ElevatorModel model){
		view = new ElevatorView(model);
		view.addExitListener(this);
	}
	
	public void updateView(ElevatorModel model) {
		view.updateUI(model);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		//if exit was pressed update view
		if(event.getActionCommand().equals("Exit")){
			view.exitClicked();
		}
		
	}
}
