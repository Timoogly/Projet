package testFenetreProjet;

import javax.swing.*;

import interfaceMenu.FenetreMenu1;


import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class partie<Connection> extends JFrame implements ActionListener {
	
	LinkedList <player> joueurs; 		
	Deck deck;
	LinkedList <Integer> mises;
	LinkedList <Carte> cartesSorties;
	Timer mt; 
	int t, nbJeux, tempsTotal;
	player enCours, prevCours;
	Croupier dealer;
	utilisateur user;
	boolean tourPrepare, aBienMise, perdu, gagne;
	int solde;
	
	Connection connexion;
	public player joueur ;
	
	// Les Widgets à déclarer en dehors du constructeur
	private JTextArea credit;
	private JLabel Credit;
	private JLabel maMise;
	private JButton HIT;
	private JButton STAND;
	private JButton UN;
	private JButton CINQ;
	private JButton DIX;
	private JButton menuconseil;
	private JButton VALIDER;
	private JButton ANNULER;
	private JButton Quitter;
	private JTextArea Sabot;			// non, on les a pas déclarés à l'horizontale, on voulait remporter le concours du plus grand nombre de lignes de code
	private JTextArea Main;
	private JTextArea affHistorique;
	private JLabel Player ;
	private JLabel IA1 ;
	private JLabel IA2 ;
	private JLabel IA3 ;
	private JLabel IA4 ;
	private JLabel Croupier ;
	private JLabel PlayerS ;
	private JLabel IA1S ;
	private JLabel IA2S ;
	private JLabel IA3S ;
	private JLabel IA4S ;
	private JLabel CroupierS ;
	private JLabel PlayerM ;
	private JLabel IA1M ;
	private JLabel IA2M ;
	private JLabel IA3M ;
	private JLabel IA4M ;
	private JLabel CroupierM ;
	private JLabel carte1;
	private JLabel carte2;
	private JLabel carte3;
	private JLabel carte4;
	private JLabel carte5;
	private JLabel carte6;	
	private JLabel WinOrLose;
	
	// pour le comptage des cartes on a besoin de : 
		
	JFrame F;
	JLabel hal;
	JLabel T;
	int taille=0;
	String res;
	JLabel R;
	String r;
	JLabel re;
	private String title[]={"rien","as","deux","trois","quatre","cinq","six","sept","huit","neuf","dix","valet","dame","roi"};
	private Object[][]P;
	private double [] pr;	
	private double HL;
	private double halves;
	private int []A;
	/** Note sur l'accès aux variables dans cette classe.
	 * 
	 * La classe partie a été conçue comme un genre de conteneur chargé de simuler tous les objets qui jouent, leurs comportements, et de tout afficher. A ce titre, il possède comme attributs des instances
	 * de chaque classe du programme, parfois simplement pour reconnaitre un élément particulier dans une liste, parfois au contraire pour pouvoir le modifier, et ainsi faire avancer le jeu.
	 * L'accès aux variables dans cet classe demande donc peut-être clarification. 
	 * Sachant que la quasi totalité des attributs pouvait être apellée en une seule méthode, on a décidé de n'utiliser que peu ou pas de getter et setter. 
	 * On peut donc accéder à toute information sur les éléments du jeu dans toutes les méthodes. En particulier, l'utilisateur du programme, dont les actions sont gérées comme des évenements,
	 *  est représenté par une InnerClass à l'intérieur de partie. Ses attributs sont modifiés par plusieurs méthodes de la classe partie, en particulier faireJouer() et ActionPerformed. 
	 * Il s'agit de faciliter l'éxécution (et aussi l'écriture) du programme. 
	 * On s'est cependant fixé comme règle: - tout attribut non primitif susceptible d'être modifié par une méthode est donné en entrée à cette méthode.
	 * 										- un attribut simple dont la méthode a besoin comme valeur pour un calcul est accédé par simple appel
	 * */
	
	public partie( int nbJoueurs, Deck jeu, int argent, Connection connex, player pl){
		
		this.connexion = connex;
		this.joueur = pl;
		this.solde = argent;
		this.deck= jeu;
		joueurs= new  LinkedList<player>();
		
		user= new utilisateur (argent);				//l'utilisateur est le premier à jouer, même si il est affiché au centre de l'écran: c'est une condition pour le fonctionnement de l'algorithme
		joueurs.add( user); 
		for (int i=0; i<nbJoueurs; i++){
			joueurs.add( new ia(argent));
		}
		dealer= new Croupier(argent);
		joueurs.add (dealer); 						// le croupier est le joueur à la fin de la liste
		
		
		cartesSorties= new LinkedList<Carte>();				
		// permet de définir les élements qui constituent le JTable donnant les probas reeles
	/*	pr=tbp();
		P=new Object[1][title.length];
		for(int i =0;i<pr.length;i++){
			P[0][i]=pr[i];
		}
		*/
		mises= new LinkedList<Integer>();		//création de la dernière LL
		this.t=0;								// et initialisation de toutes les variables nécessaires à la suite du programme
		tourPrepare=false;
		perdu= false;
		gagne=false;
		aBienMise=false;
	
		
		taille=deck.deck.size();
		/**  L'affichage est initialisé à partir d'ici */
		
		
		this.setTitle("Interface Partie");
		this.setLayout(null);
		this.setSize(850,600);
		this.setLocation(20,20);
		// Pour empêcher le redimensionnement de la fenêtre
		this.setResizable(false);
		// Pour permettre la fermeture de la fenêtre lors de l'appui sur la croix rouge
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	
		//WinOrLose
		WinOrLose = new JLabel("");
		WinOrLose.setBounds(375,250,200,30);      				
		//WinOrLose.setVisible(false);
		
		// Affichage des cartes
		carte1=new JLabel();
		carte1.setBounds(350,250,50,80);
		carte1.setVisible(false);
		
		carte2=new JLabel();
		carte2.setBounds(350+50,250,50,80);
		carte2.setVisible(false);
		
		carte3=new JLabel();
		carte3.setBounds(350+100,250,50,80);
		carte3.setVisible(false);
		
		carte4=new JLabel();										//On a créé autant d'instances que de cartes/joueurs AU MAX. On en a donc toujours plus que nécessaire. C'est ptetre pas forcement opti															
		carte4.setBounds(350+150,250,50,80);						// mais on savait pas comment faire autrement, ça posait problême avec les noms des composants, pour les apeller dans le programme.
		carte4.setVisible(false);
		
		carte5=new JLabel();
		carte5.setBounds(350+200,250,50,80);
		carte5.setVisible(false);
		
		carte6=new JLabel();
		carte6.setBounds(350+250,250,50,80);
		carte6.setVisible(false);
		
		//Affichage de la mise du joueur
		maMise = new JLabel();
		maMise.setText("les mises");
		maMise.setBounds(10,400,150,40);
		Font police = new Font(" Courier New ",Font.BOLD,18);
		maMise.setFont(police);
		maMise.setForeground(new Color(255,215,0));
		
		//bouton de mise UN
		UN = new JButton();
		UN.setBounds(15,500,60,70);
		UN.setIcon(new ImageIcon("1.png"));
		UN.setBorderPainted(false);
		UN.addActionListener(this);
		UN.setOpaque(false);
		UN.setContentAreaFilled(false);
	
		//bouton de mise CINQ
		CINQ = new JButton();
		CINQ.setBounds(80,500,60,70);
		CINQ.setIcon(new ImageIcon("5.png"));
		CINQ.setBorderPainted(false);
		CINQ.addActionListener(this);
		CINQ.setOpaque(false);
		CINQ.setContentAreaFilled(false);
		
		//bouton de mise 10
		DIX = new JButton();
		DIX.setBounds(145,500,60,70);
		DIX.setIcon(new ImageIcon("10.png"));
		DIX.setBorderPainted(false);
		DIX.addActionListener(this);
		DIX.setOpaque(false);
		DIX.setContentAreaFilled(false);
		
		//bouton HIT (piocher une carte)
		HIT= new JButton();
		HIT.setBounds(260,450,105,43);
		HIT.setIcon(new ImageIcon("hit.png"));
		HIT.setBorderPainted(false);
		HIT.addActionListener(this);
		HIT.setOpaque(false);
		HIT.setContentAreaFilled(false);
		HIT.setVisible(false);
		
		//bouton Stand (terminer mon tour)
		STAND = new JButton();
		STAND.setBounds(390,450,175,47);
		STAND.setIcon(new ImageIcon("Stand.png"));
		STAND.setBorderPainted(false);
		STAND.addActionListener(this);
		STAND.setOpaque(false);
		STAND.setVisible(false);
		STAND.setContentAreaFilled(false);
		
		
		
		//bouton fini de miser
		VALIDER= new JButton(new ImageIcon(new ImageIcon("VALIDER.png").getImage().getScaledInstance(100, 33, Image.SCALE_DEFAULT)));
		VALIDER.setBounds(250, 525, 100,33);
		VALIDER.addActionListener(this);
		VALIDER.setVisible(false);
		
		// bouton "ho mince alors je sais pas compter je me suis trompé de mise"
		ANNULER= new JButton(new ImageIcon(new ImageIcon("ANNULER.png").getImage().getScaledInstance(100, 33, Image.SCALE_DEFAULT)));
		ANNULER.setBounds(350, 525, 100,33);
		ANNULER.addActionListener(this);
		ANNULER.setVisible(false);
		
		//Affichage de l'argent restant
		Credit = new JLabel();
		Credit.setText("Credit");
		Credit.setBounds(700,450,150,50);
		Credit.setFont(police);
		Credit.setForeground(new Color(255,215,0));
		

		
	/***************************************************************************************************************/	
		// Les joueurs
		//Player
		Player = new JLabel();
		Player.setBounds(410,330,100,30);
		Player.setText("Player");
		Player.setForeground(Color.red);
		//IA1
		IA1= new JLabel();
		IA1.setBounds(700,230,100,30);
		IA1.setText("IA1");
		IA1.setForeground(new Color(255,215,0));
		//IA2															//idem
		IA2= new JLabel();
		IA2.setBounds(580,300,100,30);
		IA2.setText("IA2");
		IA2.setForeground(new Color(255,215,0));				
		//IA3													
		IA3= new JLabel();
		IA3.setBounds(240,300,100,30);
		IA3.setText("IA3");
		IA3.setForeground(new Color(255,215,0));
		//IA4
		IA4= new JLabel();
		IA4.setBounds(130,230,100,30);
		IA4.setText("IA4");
		IA4.setForeground(new Color(255,215,0));
		//Croupier
		Croupier = new JLabel();
		Croupier.setBounds(410,150,100,30);
		Croupier.setText("Croupier");
		Croupier.setForeground(Color.red);
	/*****************************************************************************************************************/	
		// Les Mises
		//Player
		PlayerM = new JLabel();
		PlayerM.setBounds(410,330+30,100,30);
		PlayerM.setText("PlayerM");
		PlayerM.setForeground(Color.red);
		//IA1
		IA1M= new JLabel();
		IA1M.setBounds(700,230+30,100,30);
		IA1M.setText("IA1M");
		IA1M.setForeground(new Color(255,215,0));
		//IA2
		IA2M= new JLabel();
		IA2M.setBounds(580,300+30,100,30);
		IA2M.setText("IA2M");										//idem
		IA2M.setForeground(new Color(255,215,0));
		//IA3
		IA3M= new JLabel();
		IA3M.setBounds(240,300+30,100,30);
		IA3M.setText("IA3M");
		IA3M.setForeground(new Color(255,215,0));
		//IA4
		IA4M= new JLabel();
		IA4M.setBounds(130,230+30,100,30);
		IA4M.setText("IA4M");
		IA4M.setForeground(new Color(255,215,0));
		
		/*le croupier ne mise pas donc pas de label mise pour lui*/
	/******************************************************************************************************************/	
		// Les Scores
		//Player
		PlayerS = new JLabel();
		PlayerS.setBounds(410,330+60,100,30);
		PlayerS.setText("PlayerS");
		PlayerS.setForeground(Color.red);
		//IA1
		IA1S= new JLabel();
		IA1S.setBounds(700,230+60,100,30);
		IA1S.setText("IA1S");
		IA1S.setForeground(new Color(255,215,0));
		//IA2
		IA2S= new JLabel();
		IA2S.setBounds(580,300+60,100,30);
		IA2S.setText("IA2S");
		IA2S.setForeground(new Color(255,215,0));
		//IA3
		IA3S= new JLabel();
		IA3S.setBounds(240,300+60,100,30);
		IA3S.setText("IA3S");
		IA3S.setForeground(new Color(255,215,0));
		//IA4
		IA4S= new JLabel();
		IA4S.setBounds(130,230+60,100,30);
		IA4S.setText("IA4S");
		IA4S.setForeground(new Color(255,215,0));
		//Croupier
		CroupierS = new JLabel();
		CroupierS.setBounds(410,150+30,100,30);
		CroupierS.setText("CroupierS");
		CroupierS.setForeground(Color.red);
		
		
	//	TODO REMPLACER MENU CONSEIL PAR QUITTER TABLE ?
		//Menu conseil 
		/*
		menuconseil=new JButton();
		menuconseil.setBounds(575,25,250,100);
		menuconseil.setIcon(new ImageIcon("MENU_CONSEIL.png"));
		menuconseil.addActionListener(this);
		F=new JFrame();
		F.setBounds(0,0,1380,250);
		*/
		
		
		//affichage du bouton quitter table
		Quitter= new JButton(new ImageIcon(new ImageIcon("quitterTable.png").getImage().getScaledInstance(100, 33, Image.SCALE_DEFAULT)));
		Quitter.setBounds(575, 25, 80,60);
		Quitter.addActionListener(this);
		Quitter.setVisible(true);
		
		
		
        //label fond d'�cran
        JLabel fond = new JLabel(new ImageIcon(new ImageIcon("blackjack-table2.png").getImage().getScaledInstance(850, 600, Image.SCALE_DEFAULT)));
		fond.setBounds(0,0,850,600);
		
		// Mon panneau Global
		 
		JPanel panneauGlobal = new JPanel();
		
		panneauGlobal.setBounds(0,0,850,600);
		//panneauGlobal.add(menuconseil);
		panneauGlobal.add(maMise);
		panneauGlobal.add(Credit);
		panneauGlobal.add(UN);
		panneauGlobal.add(CINQ);
		panneauGlobal.add(DIX);
		panneauGlobal.add(HIT);
		panneauGlobal.add(STAND);
		panneauGlobal.add(VALIDER);
		panneauGlobal.add(ANNULER);
		panneauGlobal.add(Quitter);
		
		panneauGlobal.add(Player);
		panneauGlobal.add(IA1);
		panneauGlobal.add(IA2);
		panneauGlobal.add(IA3);
		panneauGlobal.add(IA4);
		panneauGlobal.add(Croupier);
		
		panneauGlobal.add(PlayerS);
		panneauGlobal.add(IA1S);
		panneauGlobal.add(IA2S);
		panneauGlobal.add(IA3S);
		panneauGlobal.add(IA4S);
		panneauGlobal.add(CroupierS);
		
		panneauGlobal.add(PlayerM);
		panneauGlobal.add(IA1M);
		panneauGlobal.add(IA2M);
		panneauGlobal.add(IA3M);
		panneauGlobal.add(IA4M);
		
		panneauGlobal.add(carte1);
		panneauGlobal.add(carte2);
		panneauGlobal.add(carte3);
		panneauGlobal.add(carte4);
		panneauGlobal.add(carte5);
		panneauGlobal.add(carte6);
		
		panneauGlobal.add(WinOrLose);  //modif aymen
		panneauGlobal.add(fond);
		panneauGlobal.setLayout(null);
		
		
		
		
		this.setContentPane(panneauGlobal);
	
		
		// Pour rendre la fenêtre visible
		this.setVisible(true);
		

		
		
		for( player i: joueurs){ 						 
			i.nouveauTour( deck);    	// on prépare tout le monde à commencer un tour
		}
		
		
		mt = new Timer (10,this);
		tempsTotal=this.gestionTours(this.joueurs);	// positionnement des bornes temporelles
		mt.start();									//lancement du Timer
						// A partir de ce point, le timer permet une gestion du temps au travers de la variable t, et chaque joueurs a son intervalle de temps où il joue
													// Bref, c'est du tour-par-tour
		
		
		
	} 
	
	
	
	
	
	public void actionPerformed( ActionEvent e) {
		/** Remarque: on voulait pr�voir le cas o� le joueur se barre pendant un tour. Il aurait fallu soit lui laisser le choix de mettre en pause, soit lui demander avant de commencer un nouveau tour */
		 
		t++;
		
		
		if (e.getSource()==mt){		// apellé toutes les 10ms, c'est là que ce trouvent l'essentiel des instructions importantes
			
			
			
		/*	
			pr=tbp();
			for(int i =0;i<pr.length;i++){
				P[0][i]=pr[i];
			}
			JPanel Stats =new JPanel();
			Stats.setBounds(0,0,1000,150);
			JLabel HILOW = new JLabel("HILOW");
			F.setLayout(null);
			Stats.setLayout(null);
			HILOW.setBounds(0,0,250,30);
			Stats.add(HILOW);
			R=new JLabel();
			R.setBounds(80,0,650,30);
			Stats.add(R);
			JLabel Halves = new JLabel("Halves");
			Halves.setBounds(0,40,60,30);
			Stats.add(Halves);
			re=new JLabel();
			re.setBounds(80,40,650,30);
			Stats.add(re);
			T = new JLabel();
			T.setBounds(50,0,30,30);
			Stats.add(T);	
			JLabel hal= new JLabel();
			hal.setBounds(50,40,30,30);
			Stats.add(hal);
			JLabel PR = new JLabel("Probabilitees reelles en % (interessant dans le cadre d'apprentissage des methodes):" );
			PR.setBounds(0,60,550,30);
			Stats.add(PR);
			F.add(Stats);
			JTable tableau = new JTable(P,title);
			tableau.setBounds(0,190,1000,200);		    
			getContentPane().add(tableau.getTableHeader(), BorderLayout.NORTH);
			getContentPane().add(tableau, BorderLayout.CENTER);
			JScrollPane Tab=new JScrollPane(tableau);
			Tab.setBounds(0,100,1000,50);
			F.add(Tab);	
			T.setText(Double.toString(HL));
			hal.setText(Double.toString(halves));
			re.setText(r);
			R.setText(res);
			
			*/
			
			
			
			if(t%tempsTotal==0 && !tourPrepare) {           				// grace au modulo, et à l'incrémentation de t, cette condition n'est remplie qu'en début du tour n°2, et des suivants
/**important*/	debutTour( mises, joueurs, dealer);   						//au début d'un tour on lance une méthode qui met tout en place pour le nouveau tour
			}
			
			int i=0;
			while (  !( (t%tempsTotal) >=joueurs.get(i).debutTour && (t%tempsTotal) < joueurs.get(i).finTour) ){
				i++;                        								// on cherche le joueur dont c'est le tour (i.e.  le temps est dans l'intervalle qui lui est attribué pour jouer)
			}
			prevCours= enCours;
			enCours=joueurs.get(i);
			
			
			
			if(this.perdu==true ) {
				mt.stop();	
			}	
			if( this.gagne==true){
				mt.stop();
			}																									
																	
			if(prevCours!= enCours){						//si on change de tour	
				
				if (prevCours==user){ 						//si le tour de l'utilisateur vient de s'achever
					this.user.lheureDeMiser=false;			//on termine le tour de l'utilisateur:  l'affichage et un ou deux autres trucs sont mis à jours en conséquence
					this.user.lheureDeTirer=false;
					HIT.setVisible(false);
					STAND.setVisible(false);
					VALIDER.setVisible(false);
					ANNULER.setVisible(false);
					carte1.setVisible(false);
					carte2.setVisible(false);
					carte3.setVisible(false);
					carte4.setVisible(false);
					carte5.setVisible(false);
					carte6.setVisible(false);
					
					
					if(aBienMise==false){	
						this.mises.add(0);				// Le joueur doit toujours miser quelquechose, même 0, pour éviter des problèmes d'indices dans les listes. Le ActionPerformed sert à choisir la valeur de la mise
						misesEtPremieresCartes();		// ca a l'inconvénient de faire des boucles sans fin si on est à user vs dealer et que l'utilisateur mise pas
					}
					aBienMise=false;					// on remet ces variables à comme il faut pour le tour d'après
					this.user.jeVaisMiser=0;
				}											
				
/**important*/	this.faireJouer(enCours ); 			 		// si on vient de changer de tour, on "fait jouer" le nouveau joueur, càd qu'on lance ses actions
				tourPrepare=false; 							// commentaire sur ce booléen à la fin de la méthode debutTour()
				
			}
			
			
				if(enCours==this.user){
					PlayerS.setText("Score : "+Integer.toString(enCours.total));				// on affiche ce qu'il faut pour le tour de l'utilisateur
					
					if(aBienMise){
						PlayerM.setText(Integer.toString(mises.get(0)) +"$");
						Credit.setText("Credit : "+ Integer.toString(this.user.credit)+"$");
					}else{
							maMise.setText("Ma Mise : "+Integer.toString(this.user.jeVaisMiser) +"$");		//en gérant les fois où il n'a pas fini de miser
					}
					
					STAND.setVisible(true);
					HIT.setVisible(true);
					VALIDER.setVisible(true);
					ANNULER.setVisible(true);
					
					
			}else if(enCours==this.dealer){
					CroupierS.setText("Score : " + Integer.toString(enCours.total));		// same pour le dealer

			} else if(i==1 ){IA1S.setText("Score : "+Integer.toString(enCours.total));IA1M.setText(Integer.toString(mises.get(i))+"$");}				//et les ia
				else if(i==2){IA2S.setText("Score : "+Integer.toString(enCours.total));IA2M.setText(Integer.toString(mises.get(i))+"$");}
				else if(i==3){IA3S.setText("Score : "+Integer.toString(enCours.total));IA3M.setText(Integer.toString(mises.get(i))+"$");}
				else if(i==4){IA4S.setText("Score : "+Integer.toString(enCours.total));IA4M.setText(Integer.toString(mises.get(i))+"$");}
			
			
			
		}
		
		
		if (e.getSource()==menuconseil){
			F.setVisible(true);
			}
		
		if (e.getSource()== Quitter)
		{
			user.nbParties++;
			user.credit = (enCours.credit) - this.solde;
			System.out.println(solde);
			System.out.println(credit);
			
			  miseAJour(user.nbParties, user.credit);
		      	dispose();
		      	FenetreMenu1 m=null;
				try {
					m = new FenetreMenu1((java.sql.Connection) this.connexion, this.user);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		      	m.joueur = (Utilisateur) enCours;
		}
		
		//gestion de tous les boutons sur lesquels peut appuyer l'utilisateur, avec des conditions supplémentaires pour le forcer à tout bien faire dans le bon ordre et au bon moment
		
		if (e.getSource()== UN && this.user.lheureDeMiser && this.user.jeVaisMiser<=this.user.credit-1){
			this.user.jeVaisMiser++; 
		}
		if (e.getSource()== CINQ && this.user.lheureDeMiser && this.user.jeVaisMiser<=this.user.credit-5){
			this.user.jeVaisMiser+=5;
		}
		if (e.getSource()== DIX && this.user.lheureDeMiser && this.user.jeVaisMiser<=this.user.credit-10){
			this.user.jeVaisMiser+=10; 	
		}
		
		if ( e.getSource() == HIT  &&   this.user.fautIlTirer()){
			this.cartesSorties.add(this.user.tirer(this.deck));	
			for(int j=0;j<this.user.hand.size();j++){			//Boucle + switch sur des Jpanels qui affichent les cartes du joueurs
						switch(j){								// on gère tous les cas de mains possible en dessous de 7 cartes (parce qu'au dessus c'est statistiquement improbable)
								case 0:
									carte1.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT)));
									carte1.setVisible(true);
									break;
								case  1:	
									carte2.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT)));
									carte2.setVisible(true);
									break;
								case 2:
									carte3.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT)));
									carte3.setVisible(true);
									break;
								case 3:
									carte4.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT)));
									carte4.setVisible(true);
									break;
								case 4:
									carte5.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT)));
									carte5.setVisible(true);
									break;
								case 5:
									carte6.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT)));
									carte6.setVisible(true);
									break;	
								default:
										System.out.println("on ne peut pas afficher cette carte. Il s'agit de " + this.user.hand.get(j));
										break;
						}		
			}
					         
		}
		
		if( e.getSource() == STAND && this.enCours==this.user)
			this.t+=(this.user.finTour-t%tempsTotal); 	// on termine le tour du l'utilisateur si il appuie sur STAND
		 
		if(e.getSource()==VALIDER && aBienMise==false && this.user.lheureDeMiser==true){
			aBienMise=true;				// si le joueur valide qu'il a misé, alors on applique vraiment sa décision
			mises.add(this.user.miser(this.user.jeVaisMiser));		
			cartesSorties.add(this.user.tirer(this.deck));			//on lui fait piocher deux cartes, car son tour commence
			cartesSorties.add(this.user.tirer(this.deck));
			for(int j=0;j<this.user.hand.size();j++){
						switch(j){
								case 0:
									carte1.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT))); //on les affiche
									carte1.setVisible(true);
									break;
								case  1:	
									carte2.setIcon(new ImageIcon(((Carte) this.user.hand.get(j)).getImage().getScaledInstance(50, 80, Image.SCALE_DEFAULT)));
									carte2.setVisible(true);
									break;
								default:
									System.out.println("on ne peut pas afficher la troisème carte");// par définition il n'y a pas de troisième carte à ce moment là, le message c'est pour la forme
									break;
						}		
			}
			misesEtPremieresCartes();						//on lance cette méthode, relative au déroulement de la partie (cf les commentaires de la-dite méthode)
			Credit.setText("Credit : "+ Integer.toString(this.user.credit)+"$");
			for(int i=1;i<joueurs.size();i++){		
			         if(i==1){IA1S.setText("Score : "+Integer.toString(joueurs.get(i).total));IA1M.setText(Integer.toString(mises.get(i))+"$");}				
				else if(i==2){IA2S.setText("Score : "+Integer.toString(joueurs.get(i).total));IA2M.setText(Integer.toString(mises.get(i))+"$");}
				else if(i==3){IA3S.setText("Score : "+Integer.toString(joueurs.get(i).total));IA3M.setText(Integer.toString(mises.get(i))+"$");}
				else if(i==4){IA4S.setText("Score : "+Integer.toString(joueurs.get(i).total));IA4M.setText(Integer.toString(mises.get(i))+"$");}
				else if(i==joueurs.size()-1){CroupierS.setText("Score : " + Integer.toString(joueurs.get(i).total));}		// 
			}
			this.user.lheureDeMiser=false;					//On indique l'action suivante à réaliser
			this.user.lheureDeTirer=true;			
			
		}
		
	
	
	
		if(e.getSource()==ANNULER && this.user.lheureDeMiser && !aBienMise){
			this.user.jeVaisMiser=0;				//Le bouton ANNULER permet de saisir une seconde fois la somme que l'on veut miser
		}
	
	
	}
	
	
	
	
	
	
	
	
	// Au BJ, chaque joueur décide de miser, puis le croupier distribue deux cartes à chacun. Enfin le tour commence.  Ici, on le modélise d'une manière légèrement différente, afin de gérer tous les 
	//comportements possibles de l'utilisateur: (1) celui-ci mise, (2) chaque joueur dont le comportement est géré par le programme mise et reçoit deux cartes. Cette seconde étape est gérée ici
	public void misesEtPremieresCartes(){
		for(int i=1; i<joueurs.size();i++){
			mises.add(joueurs.get(i).miser(0));
			cartesSorties.add(joueurs.get(i).tirer(this.deck));
			cartesSorties.add(joueurs.get(i).tirer(this.deck));
		}
		
	}
	

	public int gestionTours( LinkedList <player> joueurs){
		int a=0;
		for (int i=0; i<joueurs.size(); i++){
			a=joueurs.get(i).setTour(a);			//on donne a chaque joueur les bornes temporelles où il peut jouer
		}
		return a;		                       // on renvoie la valeur du temps total d'un tour
	}
	
	
	
	
	
	
	
	public void debutTour(LinkedList<Integer> a, LinkedList <player> joueurs, Croupier b) {
		// NB: toute cette méthode aurait été plus simple et moins lourde avec des tableaux, mais le reste du programme impliquait des LL, alors il a fallu faire avec														
	  	
	  	
	  	for (int i=0; i<joueurs.size()-1; i++) {	//boucle pour gérer les gains à la fin d'un tour, et les éventuels perdants.
			if( b.total> 21  ){
				if(joueurs.get(i).total<=21){
					int c= b.donnerArgent( (int) a.get(i));//modif aymen
					joueurs.get(i).gagner(c);		// Au BJ, si le croupier perd, tout les joueurs à moins de 21 ont gagné
					if(joueurs.get(i)==this.user){//modif aymen
					WinOrLose.setText("You Win :   "+ Integer.toString(c) +" $"); //modif aymen
					WinOrLose.setForeground(Color.green);//modif aymen
				        }
				}
				else {
					b.gagner((int) a.get(i));
					if(joueurs.get(i)==this.user){//modif aymen
					WinOrLose.setText("You Lose :   "+ Integer.toString((int) a.get(i)) +" $");
					WinOrLose.setForeground(Color.green);
				        }//modif aymen
					
				}
			}
			else {//modif aymen
				if(joueurs.get(i).total>b.total && joueurs.get(i).total<=21){ // Si le croupier n'est pas au-dessus de 21
					int c= b.donnerArgent( (int) a.get(i));
					joueurs.get(i).gagner(c); // on fait gagner le double de leur mise à tous les joueurs qui ont un meilleur total que le croupier. La création d'une méthode donnerArgent chez le croupier n'était pas nécessaire dans l'absolu, mais c'est plus cohérent dans la logique POO, et ça permet de suivre l'argent "du casino".
					if(joueurs.get(i)==this.user){
					WinOrLose.setText("You Win :   "+ Integer.toString(c) +" $");
					WinOrLose.setForeground(Color.green);
				        }//modif aymen
									}
				else {if (joueurs.get(i).total==b.total){//modif aymen
						joueurs.get(i).gagner((int) a.get(i)); 			// si même total, on récupère juste sa mise
						if(joueurs.get(i)==this.user){
					WinOrLose.setText("You Win :   "+ Integer.toString((int) a.get(i)) +" $");
					WinOrLose.setForeground(Color.yellow);
				        }//modif aymen
						
					}			
					else {//modif aymen
						b.gagner((int) a.get(i));   			 // total plus bas, la mise est gagnée par le croupier
						if(joueurs.get(i)==this.user){
					WinOrLose.setText("You Lose :   "+ Integer.toString((int) a.get(i)) +" $");
					WinOrLose.setForeground(Color.red);
				        }//modif aymen
						
					}
				}		
			}	
	  		
	  		if(joueurs.get(i).credit<=0){	     			  		// si un joueur a perdu
				if (joueurs.get(i)==this.dealer){ 					// on vérifie que ce n'est pas la fin de la partie
					this.gagne=true;
				}  
				if(joueurs.get(i)==this.user){
					 this.perdu=true;
				}
				joueurs.remove(joueurs.get(i));  			 	// on l'enlève de la liste des joueurs 
				a.remove (a.get(i));							// et sa mise de la liste des mises
				i--;
											// on fait artificiellement correspondre l'indice du for à celui de la liste, qui vient de réduire de 1 		
	  		}
	 	
	 	
	 }
	 	
		if (this.user.total>= 3*this.user.argentDepart) {
			this.gagne=true; // autre raison de fin de partie 
	 	}
	 	
	 	if (this.deck.nbCartes()<(this.deck.nbJeux*52)/4){		// On traite le cas d'un jeu trop vidé (pour éviter les OutOfBoundsException et rester cohérent avec les règles du Black Jack)
				this.deck.deck.addAll(this.cartesSorties);		// Notre solution n'est certes pas très élégante, et pourrait même causer des out of bounds exceptions si un tour pouvait consommer 
				this.deck.Melanger();							// un quart des cartes initiales (on prend le risque étant donné qu'on adapte le nombre de cartes au nombre de joueurs, pour éviter ce genre de situations)
				this.cartesSorties= new LinkedList <Carte>();   //  mais elle répond à un problème important: on ne peux pas réinstancier un deck depuis une méthode apellée par
				halves=0;//ActionPerformed, car celle-ci ne peux pas implémenter le code throws IOException, qui est nécessaire pour le constructeur de Deck (celui-ci utilise des images)	
				HL=0;		//Il était donc impossible de simplement faire un nouveau deck, ce qui semblait pourtant la solution la plus logique
		}																					
																
		
	 	for( player i: joueurs){ 	//nouvelle boucle nécessaire, car sinon problème en cas de suppression du premier joueur
			i.nouveauTour( deck);  	// on prépare tout le monde à recommencer un tour
	 	}
	 	a.clear();  				//la liste des mises est éffacée, pour être remplie de nouveau au prochain tour
	 	
	 	
	 	WinOrLose.setVisible(true);//modif aymen
	 	
	 	PlayerS.setText("Score : 0");		//changement d'affichage pour le début du nouveau tour
	 	CroupierS.setText("Score : 0");
	 	IA1S.setText("Score : 0");
	 	IA2S.setText("Score : 0");	
	 	IA3S.setText("Score : 0");	
	 	IA4S.setText("Score : 0");	
	 	PlayerM.setText("0 $");
	 	IA1M.setText("0 $");
	 	IA2M.setText("0 $");	
	 	IA3M.setText("0 $");	
	 	IA4M.setText("0 $");					
	 	Credit.setText("Credit : "+ Integer.toString(this.user.credit)+"$");				
	 	this.tempsTotal=gestionTours(joueurs);						// on refixe le temps de jeu de chacun, en fonction des joueurs encore en jeu
	 	
	
	
		//	HILOW();
		//	Halves();
			taille=deck.nbCartes();
	// Mathématiquement, il existe des cas où après avoir enlevé un joueur et rechangé les bornes temporelles de jeu
	//(t+1)%tempSsTotal=0    dans ces cas là la méthode debutTour est lancée une deuxième fois, et le programme plante parce que le tableau de mises est vide
	//on empêche ça avec le booléen suivant: 
		tourPrepare=true;
								
	}															
	
	
	
	
	
	
	
	public void faireJouer (player a )  {
			if (a== this.user){						// Les tours de l'utilisateur sont gérés en évenementiel, donc il suffit de changer un booléen pour que le tour "démarre officiellement"
				this.user.lheureDeMiser=true;
			}						
			else {
				// tour d'une ia : on pioche jusqu'à plus soif
				while(a.fautIlTirer()){
					cartesSorties.add(a.tirer(this.deck));													
				}
			}
				
	}
	  
	
	
	
	/*
	
	public  double HILOW(){
		HL=0;
		for(Carte car:cartesSorties){
		  if(car.getValeur()>=10){
			HL--;
		  }
		  if(car.getValeur()<7){
		 	HL++;						
		  }		
		}
		HL=HL/((double)taille/52);
		if (HL<0.5){
		  res="mise minimale";
		}
		if(HL>=0.5 && HL<2){
		  res="mise intermediaire";
		}
		if(HL>2){
		  res="grosse mise ";
		}
		
		return HL;
	}
	
	public  double[] tbp(){
		double[]P=new double[14];
		A=new int[P.length];
		int Cb=0;
		int[]Figures={0,1,2,3,4,5,6,7,8,9,10,11,12,13};
			
		for(int i=0;i<14;i++){
			for(Carte c:cartesSorties){
				if(c.getFigure()==Figures[i]) {
					Cb++;
				}
				
				
			}
				
			A[i]=deck.PC[i]-Cb;
			P[i]=(double)((deck.PC[i])-Cb)/(deck.nbCartes())*100.0;
			Cb=0;		
		}
			
		return P;
	}
	
	
	public double Halves(){
		halves=0;
		for(Carte car:cartesSorties){
			if(car.getValeur()>=10){
				halves--;
			}
			else if(car.getValeur()==2 || car.getValeur()==7 ){
				halves += 0.5;
			}
			else if(car.getValeur()==5){
				halves += 1.5;
			}
			else if (car.getValeur()==9){
				 halves+=-0.5;
			}
			else {
				halves+=1;
			}	
		}
		halves= halves/((double)taille/52);
		
		if (halves<0.5){
			r="mise minimale";
		}
		if(halves>=0.5 && halves<2){
			r="mise intermediaire";
		}
		if(halves>2){
			r="grosse mise";
		}
		return halves;
	}
	*/
	
	
	
	
	
	
	
	
	
	/** utilisateur est une Innerclass, car ses variables, notamment les booléens, doivent être accédées en permanence. On s'est juste facilité la tache. */
	
	public class utilisateur extends player{
			
			public boolean lheureDeMiser,lheureDeTirer;
			public final int argentDepart;
			public int jeVaisMiser;
			public String pseudo;
			public utilisateur(int a){	
				super();
				lheureDeMiser=false;
				lheureDeTirer=false;
				argentDepart=a;
				credit=a;
				total=0;
				limite=1000; //on a pas de limite, c'est l'utilisateur qui prendra ou non des décisions
				dureeTour=12000;
				jeVaisMiser=0;
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
			// Très franchement, on avait déjà bien assez de boutons, et puis ça collait même pas vraiment avec les règles du vrai jeu
			
			
			
			public void nouveauTour(LinkedList <Carte> sorties, Deck deck) {
				lheureDeTirer= false;
				jeVaisMiser=0;
				super.nouveauTour( deck);
			}
		
	}

	 public void miseAJour(int nbParties, int gainGlobal) {
		   Statement statement;
		   try {
		     statement = ((java.sql.Connection) connexion).createStatement();
		     statement.executeUpdate("UPDATE tablejoueurs SET gainglobal = "+ gainGlobal +", nbparties ="+ nbParties+"WHERE pseudo ='"+user.pseudo+"'");
		     statement.close();

		   } catch (SQLException e) {
		     e.printStackTrace();
		   }
		 }

}