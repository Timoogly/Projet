package testFenetreProjet;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class testdeck {
	
	public static void main  (String[] args) throws IOException   {
		/*Deck deck1 = new Deck(2);
		deck1.Afficher();
		System.out.println("---------------------------------------------------------------------------------");
		deck1.Melanger();
		deck1.Afficher();
		System.out.println(deck1.nbCartes());
		deck1.tirerCarte();
		System.out.println(deck1.nbCartes());*/
		
		
		JFrame window = new JFrame();
		window.setSize(400,600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		JPanel pan = new JPanel(new BorderLayout());
		
		JLabel card = new JLabel(new ImageIcon(ImageIO.read(new File("lasvegas21.jpg"))));
		card.setSize(200,300);
		
		pan.add(card);
		window.add(pan);
		
		
	}
}

