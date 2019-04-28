package testFenetreProjet;

import java.awt.image.*;
public class Carte {

private int Figure; //de l'as au roi
private int Famille; //Pique / Trefle / Coeur / Carreau
private int Valeur; // 1 à 11
private BufferedImage Image;
private static String[] Figures = {"Rien","As","Deux","Trois","Quatre","Cinq","Six","Sept","Huit","Neuf","Dix","Valet","Dame","Roi"};
private static String[] Familles = {"Pique","Trefle","Coeur","Carreau"};

public Carte(int Famille, int entree,BufferedImage Image)
{
    this.Figure=entree;
    this.Famille=Famille;
    this.Image=Image;
    
    if(Figure>10) { //Valet , dame ou roi , ils ont la même valeur
        Valeur=10;
    }
    else if(Figure==1) { //Ici, l'as vaut 11
		Valeur=11;
    }
		else
		{
			Valeur=Figure;
		}
}

public Carte(int Famille, int entree) {

    this.Figure=entree;
    this.Famille=Famille;
    
    if(Figure>10) {
        Valeur=10;
    }
    else if(Figure==1) {
		Valeur=11;
    }
		else
		{
			Valeur=Figure;
		}
}

public String toString()
{
    return Figures[Figure]+" de "+Familles[Famille] + " vaut " + Valeur;
}

public int getFigure()
{
    return Figure;
}

public int getFamille()
{
    return Famille;
}

public int getValeur()
{
   
    return Valeur;
}
public BufferedImage getImage(){
	return Image;
}

public void setValeur(int set)
{
    Valeur = set;
}
}
