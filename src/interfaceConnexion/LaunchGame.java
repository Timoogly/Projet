package interfaceConnexion;

import javax.swing.*;

import interfaceMenu.FenetreMenu1;
import testFenetreProjet.Utilisateur;
import testFenetreProjet.player;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
@SuppressWarnings({ "serial", "unused" })
public class LaunchGame extends JFrame implements ActionListener {
	
	private JPanel		pan;// panel qui contiendra les differents composant du panel 
	private JPanel 		pan2;
	private JLabel		JLB1;
	private JTextField	JTA1; // Jtextfield qui permettra à l'utilisateur d'entrer son pseudo 
	private JLabel		JLB2;
	private JPasswordField	JTA2;// JPasswordfield qui permettra à l'utilisateur d'entrer son mot de passe 

	private JButton JB;// bouton pour valider le login 
	private JButton JB2;// bouton pour créer un nouveau joueur pas encore inscrit dans la bdd du jeu 
	
    private String password = "";
    //lien de connexion à la BDD
    public Connection connexion ; 
    //recupere la liste des joueurs, permettra de vérfier que les données entrées par l'utilisateur sont correctes par rapport à la BDD 
    List <player> listPlayers;
    
	
 
	LaunchGame() {
		
				init();//lance la connexion à la BDD 
				this.listPlayers= new ArrayList<player>();
				this.setTitle("Connexion BlackJack");
				this.setSize(1000, 150);
				this.setLocationRelativeTo(null);
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				this.setContentPane(pan = new JPanel());
				
				pan.setLayout(new BoxLayout(pan,BoxLayout.Y_AXIS));
				
			
			
		
				Container fields = new Container();
				GridLayout fieldsLayout = new GridLayout(4,1);//répartition des composants dans le panel (4 lignes 2 colonnes de composants )
				fields.setLayout(fieldsLayout);
				fieldsLayout.setVgap(20);//espace entre les lignes 
				fieldsLayout.setHgap(60);// espace entre les colonnes 
		 
				for (Component comp : initFields()) fields.add(comp); // ajout des composants via la fonction initFields 
		 
				Container JBcontainer = new Container();
				ImageIcon img = new ImageIcon("blackjackf1.png");
				Image myNewImage = img.getImage().getScaledInstance(250, 50, Image.SCALE_DEFAULT);
				ImageIcon img2 = new ImageIcon(myNewImage);
				JLabel pic = new JLabel(img2);
				JBcontainer.setLayout(new FlowLayout(FlowLayout.CENTER));
				//ajout des boutons valider et nouveau joueur ainsi que leurs action
				JBcontainer.add(JB = new JButton("Valider"));
				JBcontainer.add(JB2=new JButton ("Nouveau Joueur"));
				JB.addActionListener(this);
				JB2.addActionListener(this);
		 
				pan.add(pic);
				pan.add(fields);
				pan.add(JBcontainer);
		 
		        this.pack();
				this.setLocationRelativeTo(null);
				this.setVisible(true);
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
			}
		 
			private List<Component> initFields() {//fonction qui ajoute les composants 
				ArrayList<Component> result = new ArrayList<>();
				result.add(JLB1	= new JLabel(" Pseudo"));
				result.add(JTA1	= new JTextField("[Entrez le pseudo]"));
				JTA1.setBackground(Color.lightGray);
				result.add(JLB2	= new JLabel("PassWord"));
				result.add(JTA2	= new JPasswordField("mmmmm"));
				JTA2.setBackground(Color.lightGray);
				
				return result;
			}
		 
//methode qui permet de définir les événements en fonction du contenus des JtextField et des boutons pressés 
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == JB) {
			getPlayer();
			String pwdString =new String( JTA2.getPassword()); 
			System.out.println(pwdString);
			
			String pseudo = JTA1.getText();
			player j = joueurDansBDD(pseudo,pwdString );//vérifie que le joueur qui se connecte est bien enregistré dans la BDD (ce n'est pas un nouveau joueur et que le pseudo et mot de passe "match bien ensmeble ")
			
			System.out.println(j);
			
			if( j != null ) {//si le joueur existe bien on recupère le jeu qu il a selectionné dans le menu déroulant et lançons le constructeur du jeu correspondant 
				
				try {
					FenetreMenu1 mp = new FenetreMenu1(connexion, j);
					
					dispose();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		} 
		else if(e.getSource()== JB2) {
			setVisible(false);
			Inscription ins = new Inscription(connexion);
			dispose();
			
			
		}
	}
	
	
	//configuration de la connexion à la base de données 
	 public void init () {

		try {
			Class.forName("org.postgresql.Driver");//driver du postgresql
			System.out.println("Driver ok !");
			String url = "jdbc:postgresql://localhost:5432/JOUEURSBJACK";//JOUEUR nom de la Base de données 
			String user = "postgres";// nom d'utilisateur 
			String pwd = "root";//mot de passe pour la connexion à la BDD 
			
			//C:\Program Files\PostgreSQL\11\pgAdmin 4\bin POUR LANCER LA BDD
			
			Connection connexion = DriverManager.getConnection(url, user, pwd);//on récupere la connexion de la BDD 
			System.out.println("Connexion établie");
			
			  //Création d'un objet Statement
		      Statement state = connexion.createStatement();
		    
		      
			this.connexion = connexion ;	
			connexion.setAutoCommit(true);// mise à jour dynamique de la BDD 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	
	public void execute (String arret)
	{
		try {
			Statement statement = connexion.createStatement();
			statement.executeQuery(arret);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// Le shutdown va sauvegarder les modifications dans la base de donnÃ©es
	
	public void shutdown ()
	{
		execute("SHUTDOWN");
		try {
			connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//methode pour obtenir la liste des Joueurs de la BDD 
	//(contient la requete)
	public void getPlayer () 
	{
		try {
			Statement statement = connexion.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT * FROM tablejoueurs");//requete qui renvoit tous les joueurs de la table tablesjoueurs
			while (resultat.next()){//tant qu il y à un résultat on ajoute le joueur (resultat ) dans la liste des joueurs (listPlayers)
				//crée les nouveaux joueur avec les joueurs de la BDD
				player p = new Utilisateur(resultat.getString("identifiant"),resultat.getString("pseudo"),resultat.getString("pswd"),resultat.getInt("nbparties"),resultat.getInt("gainglobal"));
				p.connexion= this.connexion ; 
				listPlayers.add(p);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	//methode pour vérifie que le joueur qui se connecte est bien enregistré dans la BDD (ce n'est pas un nouveau joueur) t
	public player joueurDansBDD(String pseudo1,String pwd1) {
		player j = null ; 
		for(int i = 0 ; i<listPlayers.size();i++) {
			// pour tous les joueurs de la bdd compare le pseudo et le mot de passe entré (les 2 doivent coincider )
			if(listPlayers.get(i).getPseudo().equals(pseudo1) && listPlayers.get(i).getPassword().equals(pwd1)){
				
				 j = new Utilisateur(listPlayers.get(i).identifiant,listPlayers.get(i).pseudo,listPlayers.get(i).password,listPlayers.get(i).nbParties,listPlayers.get(i).gainGlobal) ;
				 j.connexion = this.connexion ;
			}
		}
		// si le joueur n'est pas trouvé par rapport aux informations trouvés informe l'utilisateur
		if(j == null) {JOptionPane.showMessageDialog(null,
				"Erreur de login, Pseudo ou mot de passe incorrect(s) si vous n'êtes pas encore inscris veuillez procéder à l'inscription (nouveau joueur)" );
		}
		return j ;//si le joueur est trouvé j n'est pas null 
	}

	public static void main (String []args) {
		      
			
		     LaunchGame LG= new LaunchGame();
		//     Inscription ins = new Inscription(connexion);
		     
		     
		}
		
	}

