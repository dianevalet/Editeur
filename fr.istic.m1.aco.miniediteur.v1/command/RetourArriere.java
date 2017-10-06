package command;

import receiver.MoteurEdition;

public class RetourArriere implements Command {

	MoteurEdition me;
	
	/**
	 * Constructeur
	 * @param me Moteur d'�dition
	 */
	public RetourArriere(MoteurEdition me) {
		this.me = me;
	}

	@Override
	public void execute() {
		me.retourArriere();
	}

}
