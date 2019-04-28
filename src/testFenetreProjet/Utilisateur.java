package testFenetreProjet;

import java.util.LinkedList;

public class Utilisateur <Connection> extends player<Connection>{
	
	public String identifiant ; //identifiant (mail) (UNIQUE)
	public String pseudo ;//pseudo du joueur (UNIQUE )
	public String password ; //mot de passe du compte du joueur //PUBLIC/PRIVATE ?!?!?!
	public Connection connexion ;//connexion à la BDD permet de vehiculer la connexion entre les différents jeux et menus en même temps que le joueur 
	public static int nbParties ; //nombre de parties 
	public static int gainGlobal ;//score total (somme de tous les scores du joueur)
	
	
	public boolean lheureDeMiser,lheureDeTirer;
	public int argentDepart;
	public int jeVaisMiser;
	public player<Connection> enCours;
	
	public Utilisateur(int a){	
		super();
		lheureDeMiser=false;
		lheureDeTirer=false;
		argentDepart=a;
		credit=a;
		total=0;
		dureeTour=12000;
		jeVaisMiser=0;
		hand= new LinkedList <Carte> ();
		hand.add(new Carte (1,2));
	}

	
	// constructeur avec TOUS les attributs du joueur (qui sera utilisé apres avoir recupéré les données du joueur dans la bdd) 
	public Utilisateur(String id, String pseudo, String password, int nbParties, int scoreGlobal) {
		super();
		this.identifiant = id ; 
		this.pseudo = pseudo;
		this.password = password;
		Utilisateur.nbParties = nbParties;
		Utilisateur.gainGlobal = scoreGlobal;
	}
	
	public Utilisateur(String id, String pseudo, String pwd, Connection connexion) {
		this.identifiant= id ;
		this.pseudo = pseudo ;
		this.password = pwd ; 
		this.connexion = connexion;
	}
	
	public int miser( int a) {
		credit-=a;
		return a;
	}
	public  boolean fautIlTirer(){
		if( enCours==this && lheureDeTirer && total<21 ) // si c'est le tour de l'utilisateur et si c'est l'heure de tirer. 
			return true;
		else return false;	
	}
	
	// on aurait pu réécrire la méthode tirer de player, pour que sous certaines conditions le choix soit donné au joueur d'avoir un as qui vaut 11 ou un as qui vaut 1.
	// Très franchement, on avait déjà  bien assez de boutons
	
	
	
	public void nouveauTour(LinkedList <Carte> sorties, Deck deck) {
		lheureDeTirer= false;
		jeVaisMiser=0;
		super.nouveauTour( deck);
	}
	
	
	public int setTour(int a){
		this.debutTour=a;
		this.finTour=a+this.dureeTour;
		return finTour;
	}
	
	public void nouveauTour( Deck jeu) {				// on apelle cette mÃ©thode Ã  la fin du tour pour rÃ©initialiser les attributs et la main (return la main pour que partie puisse stocker les cartes sorties)
		hand.clear();
		total= 0;
		
		
		
	}
	
	
	public Carte tirer (Deck sabot) {
		Carte tiree=  sabot.tirerCarte();
		this.hand.add (tiree);	// on a une nouvelle carte, qui correspond Ã  celle qui Ã©tait sur le dessus du deck
		if(((Carte) hand.get(hand.size()-1)).getFigure()== 1 && this.total>10){ // si on tire un as et que conter une valeur de onze fait perdre, 
			total++;
			((Carte) hand.get(hand.size()-1)).setValeur(1);    
			}                                      // on ajoute 1
		else{
			 total+= ((Carte) hand.get(hand.size()-1)).getValeur();          // sinon on ajoute la valeur prÃ©vue, 11 pour l'as et la valeur des cartes
		}
		return tiree;
		
	}



	public void gagner(int a) {
		credit+=a;
		
	}

	public String toString (){
		String a;
		a= "  identifiant :" + identifiant + "  pseudo: " + pseudo + "  MDP :" + password ;
		return a;
}
	
	// getter et setter de joueur 

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public void setNbParties(int nbParties) {
		Utilisateur.nbParties = nbParties;
	}



	public void setGainGlobal(int scoreGlobal) {
		Utilisateur.gainGlobal = scoreGlobal;
	}
	

}
