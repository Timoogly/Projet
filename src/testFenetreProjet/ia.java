package testFenetreProjet;

import java.util.LinkedList;
public class ia extends player{
		
	public  ia (int a){
		super();
		credit=a;
		dureeTour=200; //2s
		limite= (int) (Math.random()*(19-13) +13);    // il existe une limite � partir de laquelle une ia ne tire plus
													//(qui est entre 13 et 19). 
													 //Elle d�pend de l'instance d'ia, pour plus de vari�t�s.
		
		
	}	


	
	public boolean fautIlTirer() {
		if (total< limite) {
			return true;
		}
		else {
			return false;	
		}
	}
	
	public int miser (int a){		// il y a bien une entr�e pour correspondre avec la d�claration g�n�rale, mais on ne l'utilise pas
		int mise= (int)(0.168*credit); // on consid�re qu'une ia mise toujours 16,8 % de son cr�dit (par plaisir)
		if (mise< 1)  mise=1;
		credit-=mise;
		return  mise ;
	}
	
	
	
}
