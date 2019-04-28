package interfaceMenu;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import interfaceConnexion.Inscription;
import testFenetreProjet.Deck;
import testFenetreProjet.Utilisateur;
import testFenetreProjet.partie;

import testFenetreProjet.player;


public class FenetreMenu1 extends JFrame implements ActionListener {

	JFrame f = new JFrame("BlackJack Menu");

	public int NbIA=1; //Récuperer le nb d'IA
	public Deck nbDeck;
	public int Solde;//Récupérer le solde de départ
	//JMenu menu;
	JButton jeu = new JButton(new ImageIcon(new ImageIcon("JOUER3.png").getImage().getScaledInstance(133, 33, Image.SCALE_DEFAULT))); //bouton pour lancer la partie
	JButton opt = new JButton(new ImageIcon(new ImageIcon("OPTIONS.png").getImage().getScaledInstance(133, 33, Image.SCALE_DEFAULT)));// bouton pour ouvir la fenêtre contenant leS infos sur le jeu 
	JButton leave = new JButton(new ImageIcon(new ImageIcon("quitter.png").getImage().getScaledInstance(133, 33, Image.SCALE_DEFAULT)));//bouton pour quitter le jeu depuis la fenetre du menu
	JButton buttonClassementG = new JButton ("Classement General par gain");// bouton pour ouvir la fenêtre contenant le classement des joueurs tous jeux confondus 
	
	
	public static Utilisateur joueur ;  // récupere le joueur qui s'est log lors du lancement / pour ensuite être récupéré par la classe du jeu pour mettre à jour les scores etc 
//	ArrayList <String> classement = new ArrayList<String>(); // contiendra la liste des joueurs ranger par ordre de score suite a l'appel de la fonction contenant la requete popur avoir le classement des joueurs 
	ImageIcon img = new ImageIcon("C:\\Users\\Timothée\\Desktop\\blackjack1.jpg");//image en fond pour le menu 
	
	String[] tab = {"4 joueurs", "3 joueurs", "2 joueurs", "1 joueur" }; //Nombre de joueurs dans une partie, au choix de l'utilisateur
	String[] tab2 = {"100€", "200€", "500€"}; //solde de départ, au choix de l'utilisateur
	JComboBox combo = new JComboBox(tab);// menu déroulant des joueurs
	JComboBox combo2 = new JComboBox(tab2); // menu déroulant du solde de départ
	Deck provisoire;
	ArrayList <String> classement = new ArrayList<String>(); // contiendra la liste des joueurs ranger par ordre de score suite a l'appel de la fonction contenant la requete popur avoir le 
	
    private String password = "";
    
    //lien de connexion à la BDD
     Connection connexion;
     player user;
     
    //recupere la liste des joueurs, permettra de vérfier que les données entrées par l'utilisateur sont correctes par rapport à la BDD 
    List <player> listPlayers;

	
	public FenetreMenu1(Connection connex,  player j) throws IOException {
		
		this.connexion = connex;
		this.user = j;
		
		
		combo.setBounds (550, 120,150,30); 
		combo2.setBounds (550, 120,150,30); 
		
		Image myNewImage = img.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT);
		ImageIcon img2 = new ImageIcon(myNewImage);
		JLabel pic = new JLabel(img2);
		
		
		//1er panel avec les menus déroulants
		JPanel pan = new JPanel();
		pan.add(combo);
		pan.add(combo2);
		
		
		
		//2eme panel avec les boutons play/options/quitter
		JPanel pan2 = new JPanel();
	
		
		// ajout des différents boutons au panel
		
		pan2.add(jeu); //BOUTON PLAY
		jeu.setBounds(10,HEIGHT-250,133,33);
		jeu.setOpaque(false);
		jeu.setContentAreaFilled(false);
		jeu.setBorderPainted(false);
		jeu.addActionListener(this);	
		
		pan2.add(opt); //BOUTON OPTIONS
		opt.setBounds(10,HEIGHT-250,133,33);
		opt.setOpaque(false);
		opt.setContentAreaFilled(false);
		opt.setBorderPainted(false);
		opt.addActionListener(this);
		
		pan2.add(leave); //BOUTON QUITTER
		leave.setBounds(10,HEIGHT-250,133,33);
		leave.setOpaque(false);
		leave.setContentAreaFilled(false);
		leave.setBorderPainted(false);
		leave.addActionListener(this);
		


		pan.add(buttonClassementG); //BOUTON CLASSEMENT
		buttonClassementG.setBounds(10,HEIGHT-250,133,33);
		buttonClassementG.setOpaque(false);
		buttonClassementG.setContentAreaFilled(true);
		buttonClassementG.setBorderPainted(true);
		buttonClassementG.addActionListener(this);
		
	
		

		f.add(pic);
		f.add(pan, BorderLayout.NORTH);
		f.add(pan2, BorderLayout.SOUTH);
		f.setSize(650, 650);
		f.setVisible(true);
		
		provisoire= new Deck();
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	
		//BOUTON PLAY
		
		if(e.getSource()== jeu){
		//	int nbIA = combo.getNbIA();
			int nbDeck= NbIA+1;
			provisoire.creerPourDeVrai(nbDeck);
			this.setVisible(false);
			
			//BOUTON NB IA (COMBO)			
			if(combo.getSelectedIndex() == 0)
			{
			 NbIA = 3;
			 }
			if(combo.getSelectedIndex() == 1)
			{
			NbIA = 2;
			 }
			if(combo.getSelectedIndex() == 2)
			{
			NbIA= 1;
			 }
			if(combo.getSelectedIndex() == 3)
			{
			NbIA = 0;
			 }
			
			
			//BOUTON SOLDE (COMBO2)
			
			if(combo2.getSelectedIndex() == 0)
			{
				
				Solde = 100;
			 }
			if(combo2.getSelectedIndex() == 1)
			{
				Solde = 250;
			
			 }
			if(combo2.getSelectedIndex() == 2)
			{
				Solde = 500;
				
			 }
				
			System.out.println(this.user.pseudo + "dans fenetre menu 1");
			
			partie game= new partie(NbIA, provisoire, Solde, connexion, user);
			
			for (int i=0;i<provisoire.PC.length;i++){
				provisoire.PC[i]=provisoire.PC[i]*nbDeck;
			}
			
			f.dispose();

		}//fin bouton play
		
		
		//BOUTON INFO - INDICATIONS SUR LE JEU
		
		if(e.getSource()== opt){
			openWebPage("http://www.guide-blackjack.com/technique-HI-LOW-Hi-LO-comptage-de.html","http://www.guide-blackjack.com/Regles-du-black-jack.html" );
		}
		
		
		//BOUTON QUITTER
		if(e.getSource()== leave){
			 f.dispose();
			 //TODO maj BDD ulysse trolleur // Update : Pas ici mais dans PArtie
		}
		
		
		//BOUTON CLASSEMENT
		if(e.getSource()==buttonClassementG) { // bouton classement pour obtenir le classement global 
			classement.clear();// avant de faire le classement on vide la liste classement
			getClassementGlobal();
			FenetreClassement scores = new FenetreClassement(classement);// ouverture de la fenêtre contenant le classement

		}
	}
	
	
	// procédure concernant la requête pour l'obtention du classement des joueurs global
				public void getClassementGlobal () {
					 Statement statement;
					   try {
					     statement = ((java.sql.Connection) connexion).createStatement();
						ResultSet resultat = statement.executeQuery("SELECT * FROM tablejoueurs ORDER BY gainglobal DESC");
						while (resultat.next()){
							//on recupere le pseudo et le score de chaque joueur (de celui qui a le plu sde point à celui qui en a le moins)
							classement.add(resultat.getString("pseudo") + " gain global : "+ resultat.getInt("gainglobal") + ".") ;
						}
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		
		
	
	//PERMETS L'OUVERTURE DES PAGES WEB
	public void openWebPage(String url, String url2){
		   try {         
		     java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		     java.awt.Desktop.getDesktop().browse(java.net.URI.create(url2)); 
		   }
		   catch (java.io.IOException e) {
		       System.out.println(e.getMessage());
		   }
	}
	
	
	public int getNbIA(){
		return NbIA;
	}
	
	public int getSolde(){
		return Solde;
	}
}


