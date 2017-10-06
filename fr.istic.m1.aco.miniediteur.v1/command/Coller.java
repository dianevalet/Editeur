package command;

import receiver.MoteurEdition;

public class Coller implements Command {
	
	MoteurEdition me;
			
	/**
	 * Constructeur
	 * @param me Moteur d'�dition
	 */
	public Coller(MoteurEdition me) {
		this.me = me;
	}

	@Override
	public void execute() {
		me.coller();
	}

}
