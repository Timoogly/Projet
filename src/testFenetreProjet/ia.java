package testFenetreProjet;

import java.util.LinkedList;
public class ia extends player{
		
	public  ia (int a){
		super();
		credit=a;
		dureeTour=200; //2s
		limite= (int) (Math.random()*(19-13) +13);    // il existe une limite à  partir de laquelle une ia ne tire plus
													//(qui est entre 13 et 19). 
													 //Elle dépend de l'instance d'ia, pour plus de variétés.
		
		
	}	


	
	public boolean fautIlTirer() {
		if (total< limite) {
			return true;
		}
		else {
			return false;	
		}
	}
	
	public int miser (int a){		// il y a bien une entrée pour correspondre avec la déclaration générale, mais on ne l'utilise pas
		int mise= (int)(0.168*credit); // on considère qu'une ia mise toujours 16,8 % de son crédit (par plaisir)
		if (mise< 1)  mise=1;
		credit-=mise;
		return  mise ;
	}
	
	
	
}
