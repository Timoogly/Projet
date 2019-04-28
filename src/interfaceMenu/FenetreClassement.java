package interfaceMenu;


	import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
	
	
	public class FenetreClassement extends JFrame {
	
		private static final long serialVersionUID = 1L;
		JTextArea scores = new JTextArea(10, 20);
		JPanel contentPanel = new JPanel();
		public static final Font FONT = new Font("Monospaced", Font.BOLD, 15);
		
		public FenetreClassement(ArrayList<String> resultats) {
			
				setTitle("Classement");
				scores.setFont(FONT);
				scores.setEditable(false);
			// remplis le JtextArea avec le contenus de l'arraylist
		        for (String  score : resultats) {
			    	
				      scores.setText(scores.getText()+ "\n" +score );
				        }
		  //permettra d'avoir une barre de déroulement sur le coté de la fenêtre 
		        JScrollPane jp = new JScrollPane(scores);
		        add(jp, BorderLayout.CENTER);
		        pack();
		  // dimension de la fenêtre 
		        setSize(300, 240);
		        setLocationByPlatform(true);
		        setVisible(true);


		}


	
	
	

}
