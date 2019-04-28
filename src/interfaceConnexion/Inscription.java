package interfaceConnexion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import interfaceMenu.FenetreMenu1;
import testFenetreProjet.Utilisateur;
import testFenetreProjet.player;


public class Inscription extends JFrame implements ActionListener  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel		pan;// panel qui contiendra les differents composant du panel 
	private JLabel		JLB;
	private JTextField	JTA; // Jtextfield qui permettra à l'utilisateur d'entrer son identifiant
	private JLabel		JLB1;
	private JTextField	JTA1;// Jtextfield qui permettra à l'utilisateur d'entrer son pseudo 
	private JLabel		JLB2;
	private JPasswordField	JTA2;// JPasswordfield qui permettra à l'utilisateur d'entrer son mot de passe 
	private JLabel		JLB3;
	private JButton		JB = new JButton("Valider");//bouton de validation de l'inscription 
	public player<Connection> joueur;
	
	//recupere la liste des joueurs, permettra de vérfier que les données entrées par l'utilisateur sont correctes par rapport à la BDD 
	List <player> listPlayers = new ArrayList<player>();
	
	
	//lien de connexion à la BDD
	Connection connexion ;
	
 
	Inscription(Connection connex) { //On doit garder la connexion du même joueur
		
		 		connexion = connex ; 
				this.setTitle("Inscription");// titre de la fenêtre 
				this.setSize(500, 500);
				this.setLocationRelativeTo(null);
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				this.setContentPane(pan = new JPanel());
				pan.setLayout(new BoxLayout(pan,BoxLayout.Y_AXIS));
				JB.addActionListener(this);// ajout de l'action lors de l'appui sur le bouton valider 
				Container fields = new Container();
				GridLayout fieldsLayout = new GridLayout(4,2);//répartition des composants dans le panel (4 lignes 2 colonnes de composants )
				fields.setLayout(fieldsLayout);
				fieldsLayout.setVgap(20);//espace entre les lignes 
				fieldsLayout.setHgap(60);// espace entre les colonnes 
		 
				for (Component comp : initFields())fields.add(comp);// ajout des composants via la fonction initFields 
		 
				Container JBcontainer = new Container();
				JBcontainer.setLayout(new FlowLayout(FlowLayout.CENTER));
				JBcontainer.add(JB);// ajout du bouton valider 
				
		 
				pan.add(fields);
				pan.add(JBcontainer);
		 
		        this.pack();
				this.setLocationRelativeTo(null);
				this.setVisible(true);
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		 
	
	//fonction qui ajoute les composants 
	
			private List<Component> initFields()
			{ 
				ArrayList<Component> result = new ArrayList<>();
				result.add(JLB	= new JLabel("Identifiant"));
				result.add(JTA	= new JTextField("ex: name@hotmail.fr"));
				JTA.setBackground(Color.lightGray);
				result.add(JLB1	= new JLabel(" Pseudo"));
				result.add(JTA1	= new JTextField(" Pseudo"));
				JTA1.setBackground(Color.lightGray);
				result.add(JLB2	= new JLabel("PassWord"));
				result.add(JTA2	= new JPasswordField("PassWord"));
				JTA2.setBackground(Color.lightGray);
				
				return result;
			}
		
		 

//methode qui permet de définir les événements en fonction du contenus des JtextField et des boutons pressés 
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == JB) {
			System.out.println("appel a getplayer");
			getPlayer (); 
			System.out.println("fin getplayer");
			String pwdString =new String( JTA2.getPassword()); 
			String id = JTA.getText() ;
			String pseudo = JTA1.getText();
			System.out.println("pseudo :" + pseudo);
			System.out.println("pseudo :" + pwdString);
			
			player j = new Utilisateur(id, pseudo, pwdString, connexion);
			
		
			//Si le joueur n'est pas dans la BDD (verification avec son pseudo et id)
			//alors on l'inscrit dans la BDD et on peut lancer la fenetre du Menu
			if(!joueurDansBDD(pseudo,id)) { 
					setVisible(false);					
					try {
						FenetreMenu1 monmenu = new FenetreMenu1(connexion, j);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.joueur = j ; 
					System.out.println("nouveau joueur :" +j);
				
					insertPlayer(j) ; 			
				
			}
		} 

}
	
	
	//methode pour obtenir la liste des Joueurs de la BDD 
	//(contient la requete)
	public void getPlayer () 
	{
		try {
			Statement statement = ((java.sql.Connection) this.connexion).createStatement();
			ResultSet resultat = statement.executeQuery("SELECT * FROM tablejoueurs");//requete qui renvoit tous les joueurs de la table tablesjoueurs
			while (resultat.next()){//tant qu il y à un résultat on ajoute le joueur (resultat ) dans la liste des joueurs (listPlayers)
				//crée les nouveaux joueur avec les joueurs de la BDD

				player<Connection> p = new Utilisateur<Connection> (resultat.getString("identifiant"),resultat.getString("pseudo"),resultat.getString("pswd"),resultat.getInt("nbparties"),resultat.getInt("gainglobal"));
				p.connexion= this.connexion ; 
				listPlayers.add((Utilisateur<Connection>) p);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
//vérifie que la valeur contenue dans les champs de l'inscription ne sont pas déjà pris par d'autres utilisateurs déjà inscrits 
	public boolean joueurDansBDD(String pseudo1,String id) {
		boolean j = false ; 
		for(int i = 0 ; i<listPlayers.size();i++) {
			if(listPlayers.get(i).getPseudo().equals(pseudo1)||listPlayers.get(i).getIdentifiant().equals(id)){// identifiant et pseudo sont uniques 
				
				 j = true ;
				 
			}
		}// informe l'utilisateur de l'échec de l'inscription 
		if(j ) {JOptionPane.showMessageDialog(null,
				"Erreur nous sommes désolé le pseudo ou l'identifiant est deja pris !! " );
		}
		return j ;
	}
	
	
	
//méthode permettant d'ajouter un joueur à la BDD 
	public void insertPlayer (player j)
	{
		Statement statement;
		try {
			statement = ((java.sql.Connection) this.connexion).createStatement();
			//requête d'insertion d'un joueur à la table tablejoueurs
			statement.executeUpdate("INSERT INTO tablejoueurs (identifiant,pseudo,pswd,nbparties,gainglobal)"
					+ " VALUES ('"+j.identifiant+"', '"+j.pseudo+"', '"+j.password+"', '"+j.nbParties+"' ,'"+j.gainGlobal+"')");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
