package receiver;

import java.util.ArrayList;
import java.util.List;

import observer.Observer;

public class MoteurEdition_Impl implements MoteurEdition {

	private PressePapier pressePapier;
	private Selection selection;
	private Buffer buffer;
	private List<Observer> obs = null;

	/**
	 * Constructeur Instancie un presse-papier, une selection et un buffer
	 */
	public MoteurEdition_Impl() {
		this.pressePapier = new PressePapier();
		this.selection = new Selection();
		this.buffer = new Buffer();
		obs = new ArrayList<Observer>();
	}
	
	/**
	 * 
	 * @return Le buffer
	 */
	public Buffer getBuffer(){
		return buffer;
	}
	
	/**
	 * 
	 * @return La selection
	 */
	public Selection getSelection(){
		return selection;
	}
	
	/**
	 * 
	 * @return Le presse papier
	 */
	public PressePapier getPressePapier(){
		return pressePapier;
	}

	/**
	 * D�fini le texte du presse papier � partir de la selection et supprime le
	 * contenu selectionn� du buffer
	 */
	public void couper() {
		String aCouper = buffer.recuperer(selection.getDebut(), selection.getFin());
		pressePapier.setContenu(aCouper);
		buffer.supprimer(selection.getDebut(), selection.getFin());
		selection.setFin(selection.getDebut());; // Annule la selection
		notifyObservers(); //Notifie l'action aux observateurs
	}

	/**
	 * D�fini le texte du presse papier � partir de la selection
	 */
	public void copier() {
		String aCopier = buffer.recuperer(selection.getDebut(), selection.getFin());
		pressePapier.setContenu(aCopier);
		notifyObservers(); //Notifie l'action aux observateurs
	}

	/**
	 * Ajouter du texte au contenu du buffer � partir du contenu du
	 * presse-papier selon la position de d�but et de fin de la selection
	 */
	public void coller() {
		int debutSelect = selection.getDebut();
		int finSelect = selection.getFin();
		buffer.remplacer(debutSelect, finSelect, pressePapier.getContenu());

		int newPosition = debutSelect + pressePapier.getContenu().length();
		selection.setPosition(newPosition, newPosition); // Repositionne le curseur de selection et annule la selection
		
		notifyObservers(); //Notifie l'action aux observateurs
	}

	/**
	 * Insert du texte dans le buffer � la place de la selection courante, le
	 * cas �ch�ant � la position du curseur
	 */
	public void insererTexte(String texte) {
		int debutSelect = selection.getDebut();
		int finSelect = selection.getFin();

		buffer.remplacer(debutSelect, finSelect, texte);
		
		int newPosition = debutSelect + texte.length();
		selection.setPosition(newPosition, newPosition); // Repositionne le curseur de selection et annule la selection
		
		notifyObservers(); 
	}

	/**
	 * D�finit le d�but et la longueur de la selection
	 * 
	 * @param debut
	 *            Position de d�part de la s�lection
	 * @param fin
	 *            Position de fin de la selection
	 */
	public void selectionner(int debut, int fin) {
		selection.setPosition(debut, fin);
	}

	/**
	 * Supprime la selection du contenu du buffer 
	 * sinon le caract�re pr�c�dent la position courante
	 */
	public void retourArriere() {
		int debutSelect = selection.getDebut();
		int finSelect = selection.getFin();
		int longueurSelect = finSelect - debutSelect;
		
		if (longueurSelect > 0) {
			buffer.supprimer(debutSelect, finSelect);
			selection.setFin(debutSelect); // Annule la selection pr�c�dente
		} else {
			if(debutSelect > 0){
				buffer.supprimer((debutSelect-1), debutSelect);
				selection.setPosition((debutSelect - 1), (debutSelect - 1)); // Repositionne le curseur
			}
		}
		notifyObservers(); //Notifie l'action aux observateurs
	}
	
	/**
	 * Supprime la selection du contenu du buffer 
	 * sinon le caract�re suivant la position courante
	 */
	public void supprimer() {
		int debutSelect = selection.getDebut();
		int finSelect = selection.getFin();
		int longueurSelect = finSelect - debutSelect;
		
		if (longueurSelect > 0) {
			buffer.supprimer(debutSelect, finSelect);
			selection.setFin(debutSelect); // Annule la selection pr�c�dente
		} else {
			if(debutSelect > 0){
				buffer.supprimer(debutSelect, debutSelect+1);
				selection.setPosition(debutSelect, debutSelect); // Repositionne le curseur
			}
		}
		notifyObservers(); //Notifie l'action aux observateurs
	}	
	//-------------------------------------------
	// 			METHODES DE L'OBSERVER
	//-------------------------------------------
	
	/**
	 * Enregistre un nouvel observateur si celuici n'est pas d�j� enregistr�
	 */
	public void register(Observer o) {
		if (o == null) {
			throw new IllegalArgumentException("o is null");
		}
		if (obs.contains(o)) {
			throw new IllegalArgumentException("o is registered already");
		}
		obs.add(o);
	}

	/**
	 * Supprime un observateur si celui ci est enregistr�
	 */
	public void unregister(Observer o) {
		if (o == null) {
			throw new IllegalArgumentException("o is null");
		}
		if (!obs.contains(o)) {
			throw new IllegalArgumentException("o is not registered");
		}
		obs.remove(o);
	}

	/**
	 * V�rifie la pr�sence d'un objet dans la collection des observateurs
	 */
	public boolean isAttach(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("o is null");
		}
		return obs.contains(o);
	}

	/**
	 * Mise � jour des observateurs
	 */
	public void notifyObservers() {
		for(Observer observer : obs){
			observer.update(this);
		}
	}
	
	/**
	 * Surchage toString
	 */
	public String toString() {
		return buffer.toString();
	}
}
