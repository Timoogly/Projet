package testFenetreProjet;

import java.util.LinkedList;

public class Croupier extends player{
	
	
	
	public Croupier(int a){
		super();
		credit=10*a;
		
		dureeTour=200; //2s
	}
		
		
		
	
	public  int miser(int a) { // le croupier ne mise pas au Black Jack, on a implémenté la méthode par respect de la classe mère et pour ne pas déranger le fonctionnement du programme
		return 0;
		}
		
	public int donnerArgent( int a)	{
		credit-=a;
		return 2*a;
	}
		
	public  boolean fautIlTirer(){
		if (total<=17)
			return true;
		else return false;
	}

}
