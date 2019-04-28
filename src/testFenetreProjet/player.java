package testFenetreProjet;

import java.sql.Connection;
import java.util.LinkedList;

@SuppressWarnings("hiding")
public abstract class player <Connection> {
		
	public String identifiant ; //identifiant (mail) (UNIQUE)
	public static String pseudo ;//pseudo du joueur (UNIQUE )
	public String password ; //mot de passe du compte du joueur //PUBLIC/PRIVATE ?!?!?!
    public Connection connexion ;//connexion à la BDD permet de vehiculer la connexion entre les différents jeux et menus en même temps que le joueur 
	public int nbParties ; //nombre de parties 
	public int gainGlobal ;//score total (somme de tous les scores du joueur)
	
		 
		LinkedList <Carte> hand;
		public int dureeTour; 
		public int total, credit, limite, debutTour,finTour ;

	
		
		public  player() {
			total=0;
			hand= new LinkedList <Carte> ();
			hand.add(new Carte (1,2));
		}
		
		// constructeur avec TOUS les attributs du joueur (qui sera utilisé apres avoir recupéré les données du joueur dans la bdd) 
		public player(String id, String pseudo, String password, int nbParties, int scoreGlobal) {
			super();
			this.identifiant = id ; 
			this.pseudo = pseudo;
			this.password = password;
			this.nbParties = nbParties;
			this.gainGlobal = scoreGlobal;
		}
		
		public player (String id, String pseudo, String pwd, Connection connexion) {
			this.identifiant= id ;
			this.pseudo = pseudo ;
			this.password = pwd ; 
			this.connexion = connexion;
		}

		public abstract int miser(int a);
		public abstract boolean fautIlTirer();
		
	
		
		
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
			if(hand.get(hand.size()-1).getFigure()== 1 && this.total>10){ // si on tire un as et que conter une valeur de onze fait perdre, 
				total++;
				hand.get(hand.size()-1).setValeur(1);    
				}                                      // on ajoute 1
			else{
				 total+= hand.get(hand.size()-1).getValeur();          // sinon on ajoute la valeur prÃ©vue, 11 pour l'as et la valeur des cartes
			}
			return tiree;
			
		}
	
	
	
		public void gagner(int a) {
			credit+=a;
			
		}
	
		public String toString (){
			String a;
			a= "  identifiant :" + identifiant + "  pseudo: " + pseudo + "  MDP :" + password + "connec" + this.connexion;
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

		public int getNbParties() {
			return nbParties;
		}

		public void setNbParties(int nbParties) {
			this.nbParties = nbParties;
		}

		public int getGainGlobal() {
			return gainGlobal;
		}

		public void setGainGlobal(int scoreGlobal) {
			this.gainGlobal = scoreGlobal;
		}
		



}
