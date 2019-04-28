package testFenetreProjet;

import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Deck {
	
public ArrayList<Carte> provisoire;	
public ArrayList<Carte> deck;
public int nbJeux;
public int[]PC={0,4,4,4,4,4,4,4,4,4,4,4,4,4};

public Deck() throws IOException
{
	nbJeux=0;
	provisoire=new ArrayList<Carte>();
    deck = new ArrayList<Carte>();
	for(int i=0; i<4; i++)
		{
			for(int j=1; j<=13; j++)
			{
				this.provisoire.add(new Carte(i,j,ImageIO.read(new File(i+"_"+j+".png"))));
			}
		}
}

public void creerPourDeVrai( int nbDeck){		// Dans le programme, Deck est instancié en deux étapes.
	this.nbJeux=nbDeck;
	for(int n=0; n<nbJeux; n++){				// En effet, la partie qui se trouve dans le constructeur nécessite d'être apellée par une méthode qui peut gérer les IOExceptions
		this.deck.addAll(this.provisoire);		// La méthode CreerPourDeVrai est ensuite apellée par un ActionPerformed (qui lui ne peut pas gérer ces exceptions). Sans ce problême dû aux images, il 
	}											// aurait été plus simple et plus logique de juste créer le Deck dans le ActionPerformed. Il existe sans doute d'autres solutions plus élégantes 
	Melanger();									// pour régler ce problème mais je ne maitrise pas du tout la gestion d'images, alors j'ai fait comme ça.
}

public void Melanger()
{
    Random random = new Random();
    Carte temp;
    for(int i=0; i<200; i++)
    {
        int index1 = random.nextInt(deck.size()-1);
        int index2 = random.nextInt(deck.size()-1);
        temp = deck.get(index2);
        deck.set(index2, deck.get(index1));
        deck.set(index1, temp);
    }
}

public Carte tirerCarte()
{
    return deck.remove(0);
}

public void Afficher(){
	for(int i=0;i<deck.size();i++){
	System.out.println(deck.get(i));
	}
}

public int nbCartes(){

	return this.deck.size();
}



}