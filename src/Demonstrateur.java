import src.*;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import java.sql.*;
import java.math.*;
import java.util.Scanner;
import oracle.jdbc.driver.OracleDriver;
import java.text.ParseException;
/**
 * Bonjour,
 * Voici la suite de requetes à effectuer :
 * placez vous dans ProjetBD et entrez
 *
 * mkdir bin
 *
 * javac -d ./bin -classpath ojdbc7.jar:. src/Demonstrateur.java
 *
 * java -classpath ojdbc7.jar:./bin Demonstrateur
 *
 * Connexion classique à ensioracle1.
 */


public class Demonstrateur {



    static final String stmtParmisNous = "SELECT * FROM Utilisateur WHERE numCB='";
    static final String ajoutUtilisateur = "INSERT INTO Utilisateur" + "(numCB, nomAbonne, prenomAbonne, dateNaissance, addrU)" + " VALUES (?,?,?,?,?)";
    static final String ajoutDeplacement = "INSERT INTO Deplacement" + "(DateDepart,DateArrivee,idV,numCB,stationDepart,stationArrivee)" + " VALUES(?,?,?,?,?,?)";
    static final String finaliserDeplacement="UPDATE INTO Deplacement SET DateArrivee=?,stationArrivee=? WHERE numCB=? AND stationArrivee IS NULL" ;
    static final String recupPrix = "SELECT prix FROM Forfait WHERE idForfait=? AND catV=? ";
    static final String aUnForfait = "SELECT COUNT(*) FROM Forfait WHERE numCB =? AND catV =? "; //si vaut 0 alors non sinon oui
    static final String recupIdForfait = "SELECT idForfait FROM Forfait WHERE numCB =? AND catV =? ";
    static final String recupNbLoc = "SELECT nbLoc FROM ForfaitDI WHERE idForfait =?";
    static final String stationDansBD = "SELECT COUNT (*) FROM Station WHERE nom_station='";
    static final String getNombreV = "SELECT (*) FROM Vehicule WHERE stationCourante= ? AND catV=? ";
    static final String choisirIdv = "SELECT idV FROM Vehicule WHERE catV= ? AND stationcourante=?  ";
    static final String recupIdV = "SELECT idV FROM Deplacement Where stationArrivee is NULL AND numCB =?";
    static final String ajoutForfait="INSERT INTO forfait(idForfait,numCB,catV) VALUES(?,?,?)";
    static final String ajoutForfaitDL = "INSERT INTO ForfaitDL (dateDebut, dateFin, prix, idForfait) VALUES(NOW(),?,?,?)";
    static final String ajoutForfaitDI = "INSERT INTO ForfaitDI (prix, nbLoc,idForfait) VALUES (?,?,?)";
    static final String nbVehiculeStationParCat = "SELECT COUNT(*) FROM Vehicule WHERE stationCourante = ? AND catV =?  ";
    static final String recupDateDepart="SELECT DateDepart FROM Deplacement WHERE numCB=? AND DateArrivee IS NULL";
    static final String getCaution="SELECT caution FROM Categorie WHERE catV=?";
    static final String getPrixHoraire="SELECT prixHoraire FROM Categorie WHERE catV=?";
    static final String getDureeMaxCatV="SELECT dureeMaxUtil FROM Categorie WHERE catV=?";
    static final String getCatV="SELECT catV FROM Vehicule WHERE idV=?";
    static final String appartientDL="SELECT * FROM ForfaitDL WHERE idForfait=?";
    static final String enleverForfait="DELETE FROM FORFAIT WHERE idForfait=?";
    static final String getNbPlacesOccupeesInit = "SELECT count(MAX(DateArrivee)) AS NPOI FROM Deplacement WHERE stationArrivee=? AND DateArrivee IS NOT NULL AND DateArrivee<? AND DateDepart<? GROUP BY idV";
    static final String recupDateFin="SELECT dateFin FROM ForfaitDL WHERE idForfait=?";

    public static int getAnneeNaissance(String date){
	if(date.length() != 8){
	    System.out.println("mauvais format de date, doit être du type jjmmaaaa");
	    return 0;

	}
	else{
	    String sub=date.substring(3,6);  //on extrait aaaa
	    int annee=  Integer.parseInt(sub); //on le convertit proprement en int
	    return annee;
	}
    }

    private static void printCategorie(){
	System.out.println("Entrez le numéro de la catégorie de véhicule souhaitée : 0, 1, 2, 3 ,4");
	System.out.println("\t 0. voiture électrique");
	System.out.println("\t 1. vélo");
	System.out.println("\t 2. vélo électrique");
	System.out.println("\t 3. vélo avec remorque");
	System.out.println("\t 4. petit utilitaire");
    }

    private static void printForfait(){
	System.out.println("Entrez le type de forfait souhaité :0 ou 1 ");
	System.out.println("\t 0. nombre illimité de locations pendant une durée limitée");
	System.out.println("\t 1. nombre limité de locations pendant une durée illimitée");
    }

    private static void printChoix(){
	System.out.println("Choisir une option :1,2,3,4");
	System.out.println("\t 1. Souscrire ");
	System.out.println("\t 2. Louer");
	System.out.println("\t 3. déposer véhicule");
	System.out.println("\t 4. Exit");
	System.out.println("\t 5. Taux d'occupations");
    }



    public static void main(String [] arg){
	try{
	    //--Lecteur de l'entrée
	    Scanner sc = new Scanner(System.in);
	    //--Enregistrement d'un pilote JDBC
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	    //--Connexion avec la base de données
	    String url = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
	    System.out.println("Entrez votre login :");
	    String user = sc.nextLine();
	    System.out.println("Entrez votre mot de passe :");
	    String pwd = sc.nextLine();
	    String catV= " ";
	    Connection connection = DriverManager.getConnection(url, user, pwd);
	    System.out.println("Connection établie !");
	    //--Introduire une requête
	    printChoix();
	    java.util.Date dateDebut=new Date();
	    int query=sc.nextInt();
	    String dateNaissance=" ";
	    Statement stmt = connection.createStatement();
	    PreparedStatement pstmt;

	    while(query!=4){
			switch(query){
				case 1: //SOUSCRIPTION

				    System.out.println("Veuillez entrez votre numéro de carte bancaire :");
				    String numCB;
				    while((numCB=sc.nextLine()).equals(""));
					    	
					    	

				    if(! stmt.executeQuery(stmtParmisNous+numCB+"'").next()){
					String prenomAbonne,addrU;
					pstmt=connection.prepareStatement(ajoutUtilisateur);

					System.out.println("Veuillez entrer votre prénom :");
					while((prenomAbonne=sc.nextLine()).equals(""));
					System.out.println("Veuillez entrer votre date de naissance (jjmmaaaa) :");

							
								
					while((dateNaissance=sc.nextLine()).equals(""));
					while(dateNaissance.length() != 8){
					    System.out.println("mauvais format de date, doit être du type jjmmaaaa");
					    System.out.println("Veuillez entrer votre date de naissance (jjmmaaaa) :");
					    dateNaissance=sc.nextLine();
					}
					System.out.println("Veuillez entrer votre addresse :");
					while((addrU=sc.nextLine()).equals(""));
					pstmt.setString(1,numCB);
					pstmt.setString(2,user.substring(0,user.length()-1));
					pstmt.setString(3,prenomAbonne);
					pstmt.setString(4,dateNaissance);
					pstmt.setString(5,addrU);
					pstmt.executeUpdate();
					pstmt.close();

				    }
				    printCategorie();
				    int cat=sc.nextInt();
				    switch(cat){
				    case 0:
					catV=new String("VoiE");
					break;
				    case 1:
					catV=new String("Velo");
					break;
				    case 2:
					catV=new String("VelE");
					break;
				    case 3:
					catV=new String("Vrem");
					break;
				    case 4:
					catV=new String("Util");
					break;
				    default:
					System.out.println("Mauvaux choix, veuillez recommencer");
					break;
				    }
						

				    printForfait();
				    int choixforfait = sc.nextInt();
				    int anneeactuelle=Calendar.getInstance().get(Calendar.YEAR);
				    String idForfait=numCB+catV;
				    pstmt=connection.prepareStatement(ajoutForfait);
				    pstmt.setString(1,idForfait);
				    pstmt.setString(2,numCB);
				    pstmt.setString(3,catV);
				    pstmt.executeUpdate();
				    pstmt.close();

				    switch(choixforfait){
				    case 0:  //FORFAIT DI
								
					int nbLoc=50;
					int prixDI=200;   
					if(anneeactuelle- getAnneeNaissance(dateNaissance)< 25||anneeactuelle-getAnneeNaissance(dateNaissance) >65){
					    prixDI=150; //reduction de 25%
					    System.out.println("Vous avez le droit à une réduction de 25%\n");
					}
					System.out.println("Vous avez choisis un forfait à Durée illimitée\n");
					System.out.println("vous avez le droit à " + nbLoc + " locations,pour un prix de " + prixDI +"€\n");
								
					pstmt=connection.prepareStatement(ajoutForfaitDI); //mise à jour de la table Forfait
					pstmt.setInt(1,prixDI);
					pstmt.setInt(2,nbLoc);
					pstmt.setString(3,idForfait);
							
					pstmt.executeUpdate();
					pstmt.close();
					break;
				    case 1:  //FORFAIT DL
					
					dateDebut=Calendar.getInstance().getTime();  //obtient la date actuelle
					String dateDebutString=dateDebut.toString();
					int prixDL=200; //on peut modifier
					if(anneeactuelle- getAnneeNaissance(dateNaissance) < 25||anneeactuelle- getAnneeNaissance(dateNaissance) >65){
					    prixDL=150;
					    System.out.println("Vous avez le droit à une réduction de 25%");
					}
					System.out.println("Vous avez choisis un forfait à Durée limitée\n");
					System.out.println("vous avez le droit à autant de locations,  pour un prix de " + prixDL +"€\n");					
					pstmt=connection.prepareStatement(ajoutForfaitDL);
					
					pstmt.setString(1,dateDebutString);
					pstmt.setInt(2,prixDL);
					pstmt.setString(3,idForfait);
					pstmt.executeUpdate();
					pstmt.close();
					//mise à jour de la table DL
					break;
				    default:

					System.out.println("Mauvais choix, tapez 0 ou 1");
					break;	
				    }

				    //pstmt.close();
					   
				   	
				    break;
				case 2:  //LOCATION
			    	    
				    System.out.println("Veuillez entrer votre numéro de carte bleue");

				    while((numCB=sc.nextLine()).equals(""));
				    if(stmt.executeQuery(stmtParmisNous+numCB+"'").next()){   //il est bien dans la BD, on rentre
					System.out.println("Quel est le nom de votre station de départ ? ");
					String stationUtilisateur;
					while((stationUtilisateur=sc.nextLine()).equals(""));
					if(stmt.executeQuery((stationDansBD+stationUtilisateur+"'")).next()){  //A REVOIR, accolade fermante ligne 267
					    printCategorie();
					    catV=" ";
					    query=sc.nextInt();
								
								
					    switch(query){
					    case 0:
						catV=new String("Voiture Electrique");
										
						break;
					    case 1:
						catV=new String("Velo");
										
						break;
					    case 2:
						catV=new String("Velo Electrique");
										
						break;
					    case 3:
						catV=new String("Velo Rem");
										
						break;
					    case 4:
						catV=new String("Utilitaire");
										
						break;
					    default:
						System.out.println("Mauvais choix, veuillez recommener\n");
						printCategorie();
						query=sc.nextInt();
						continue;
					    }
					    pstmt=connection.prepareStatement(nbVehiculeStationParCat);
					    pstmt.setString(1,stationUtilisateur);
					    pstmt.setString(2,catV);
					    int vDispo=pstmt.executeQuery().getInt(1);
					    pstmt.close();
					    if(vDispo>0){ //on teste s'il y a bien des véhicules de la catégorie dans sa station
						boolean eligible=false;
						pstmt=connection.prepareStatement(recupIdForfait);
						pstmt.setString(1,numCB);
						pstmt.setString(2,catV);
						idForfait=pstmt.executeQuery().getString(1);
						pstmt.close();
						pstmt=connection.prepareStatement(appartientDL);
						pstmt.setString(1,idForfait);
						if(pstmt.executeQuery().next()){
						    pstmt.close();
						    Date dateDepart=Calendar.getInstance().getTime();	
						    pstmt=connection.prepareStatement(recupDateFin);
						    pstmt.setString(1,idForfait);
						    Date dateFin=pstmt.executeQuery().getDate(1);
						    if((dateFin.getTime()-dateDepart.getTime())>0)
							eligible=true;
						    else{
							pstmt.close();
							pstmt=connection.prepareStatement(recupNbLoc);
							pstmt.setString(1,idForfait);
							if(pstmt.executeQuery().getInt(1)>0)
							    eligible=true;
							pstmt.close();

						    }



						    if(eligible==true){  //il a un nombre de location positif ou son forfaitDL est encore éligible 
										
							System.out.println("des véhicules sont disponibles :");
							pstmt=connection.prepareStatement(choisirIdv);
							pstmt.setString(1,catV);
							pstmt.setString(2,stationUtilisateur);
							String idvChoisi=pstmt.executeQuery().getString(1);
							pstmt.close();
							//On choisit le premier véhicule qui est disponible dans la table
										

										
							String dateDepartString=dateDepart.toString();
							pstmt=connection.prepareStatement(ajoutDeplacement); //mets à jour la table Deplacement
										
							pstmt.setString(1,dateDepartString);
							pstmt.setString(2,null);
							pstmt.setString(3,idvChoisi);
							pstmt.setString(4,numCB);
							pstmt.setString(5,stationUtilisateur);
							pstmt.setString(6,null);
							pstmt.executeUpdate();    //On insère un nouveau déplacement
							pstmt.close();
										

							System.out.println("le véhicule choisi est "+ idvChoisi+",bon voyage");
						    }
						    else{
							idForfait="ty";
							pstmt=connection.prepareStatement(enleverForfait);
							pstmt.setString(1,idForfait);
							pstmt.executeUpdate();
							pstmt.close();
							System.out.println("Vous avez plus de locations disponibles, veuillez re-souscrire");
						    }
						}
					    }
					    else{
						System.out.println("Il n'y plus de véhicules de ce type, veuillez choisir un autre véhicule\n");

					    }

					}
				    }
				    else{
					System.out.println("Vous n'avez pas de compte, veuillez vous créer des forfaits\n");
				    }
							
							
							



					
						
				    break;
				case 3:   //Rendre un véhicule
				    System.out.println("Veuillez entrer votre numéro de carte bleue");
				    while((numCB=sc.nextLine()).equals(""));
				    pstmt=connection.prepareStatement(recupIdV); 
				    pstmt.setString(1,numCB);
				    String idV=" ";
				    ResultSet result=pstmt.executeQuery();
				    while(result.next()){
					System.out.println(result.getString(1));
				    }//on récupère l'ID du véhicule
				    if(! pstmt.executeQuery().next()){
					pstmt.close();
					System.out.println("Quelle est votre station");
					String stationFinale;
					while((stationFinale=sc.nextLine()).equals(""));
					if(stmt.executeQuery((stationDansBD+stationFinale+"'")).next()){
					    SimpleDateFormat format=new SimpleDateFormat("dd-mm-aaaa HH-mm-ss");
					
					    pstmt=connection.prepareStatement(recupDateDepart); //on récupère la date de départ pour calculer la différence
					    pstmt.setString(1,numCB);
					    System.out.println("lol");
					    ResultSet rst = pstmt.executeQuery();
					    System.out.println("lollololo");
					    if(! rst.next()){
						System.out.println("hahahahaha");
						dateDebut=rst.getDate(1);
					    }
				    					
					    pstmt.close();
				    					
									

			    						
			    						
			    						
			    						pstmt=connection.prepareStatement(finaliserDeplacement); //mets à jour les station arrivée et date d'arrivée
			    						
			    						pstmt.setString(1,stationFinale);
			    						pstmt.setString(2,numCB);
			    						pstmt.executeUpdate();    //finalise la table déplacement
			    						pstmt.close();
				    					 //on récupère la date de départ 
									pstmt=connection.prepareStatement(recupIdV);
									pstmt.setString(1,numCB);
									
									
									if(! pstmt.executeQuery().next()){
										System.out.println("ijofggr");
										idV=pstmt.executeQuery().getString(1);
									}
									pstmt.close();
									pstmt=connection.prepareStatement(getCatV);
									pstmt.setString(1,idV);
									if(pstmt.executeQuery().next())
										catV=pstmt.executeQuery().getString(1);
									pstmt.close();
									Date dateFin=Calendar.getInstance().getTime(); //la date actuelle

									
					    long diff= ((dateFin.getTime()-dateDebut.getTime())/(1000*3600)); //on convertit le champs en heures
									
					    pstmt=connection.prepareStatement(recupIdForfait);
					    pstmt.setString(1,numCB);
					    pstmt.setString(2,catV);

					    idForfait=pstmt.executeQuery().getString("idForfait");

					    pstmt.close();
					    boolean dL=false;
					    int duree=-1;
					    int caution=0;
					    pstmt=connection.prepareStatement(getDureeMaxCatV);
					    pstmt.setString(1,catV);
					    int dureeMaxCatV=pstmt.executeQuery().getInt("dureeMaxUtil");
					    pstmt.close();
					    pstmt=connection.prepareStatement(appartientDL);
					    pstmt.setString(1,idForfait);
					    if(pstmt.executeQuery().next());     //possède un forfait DL
					    dL=true; //pour rentrer dans le cas où le client a un foorfait DL
					    pstmt.close();
					    if(diff < 1){
						System.out.println("Utilisation gratuite");

									



									
									
					    }else if(diff > dureeMaxCatV && dL){  //possede un forfait DL et a dépassé la durée d'utilisation max du véhicule
										
						pstmt=connection.prepareStatement(getCaution);
						pstmt.setString(1,catV);
						caution=pstmt.executeQuery().getInt(1);
						pstmt.close();
						System.out.println("Vous avez dépassé la durée maximale d'utilisation, la caution a pétée pour"+caution+"€");
										


					    }
					    else{	//il paye juste le prix horaire 
										
										
										
							pstmt=connection.prepareStatement(getPrixHoraire);
							pstmt.setString(1,catV);
							int prixHoraire=pstmt.executeQuery().getInt(1);
							pstmt.close();
							long total=diff*prixHoraire;
											
							System.out.println("Vous devez payer pour votre location une somme de "+total+"€");
										


					    }




			    						

					}
					else{
					    System.out.println("Cette station n'existe pas, recommencez");
					}
				    }
				    else{
					System.out.println("Vous avez rien loué, vous n'avez rien à rendre \n");
				    }
				    pstmt.close();
				    break;
			        
			case 4:
			    break;
			case 5:
			    /**
			     * Taux d'occupation des stations sur la journée
			     *
			     * On détermine le nb de places occupées au début de la journée considérée. Ensuite on 
			     * parcourt l'ensemble des déplacements faisant intervenir la station sur la journée, 
			     * afin de récupérer le plus grands nombre d'arrivée de véhicule sur la station :
			     * une arrivée implique +1, un départ -1. Enfin on détermine le nombre de places totales
			     * de la station. 
			     *
			     * Entrees : String nom_station, String journee
			     * Sortie : int nb_places_occupees_debut, int nb_places_totales, int ratio.
			     */
			    String nomStation, journee;
			    int nbPlacesOccupeesInit;

			    //Recuperation des entrees.
			    System.out.println("Quelle est la station à considérer?");
			    while((nomStation=sc.nextLine()).equals(""));
			    System.out.println("Sur quelle journée ? (Au format jj-MMM-aaaa, ex : 8-JUN-90)");
			    while((journee=sc.nextLine()).equals(""));
			   
			    pstmt=connection.prepareStatement(getNbPlacesOccupeesInit);
			    pstmt.setString(1,nomStation);
			    pstmt.setString(2,journee);
			    pstmt.setString(3,journee);
			    ResultSet rst=pstmt.executeQuery();

			    if(rst.next())
				System.out.println("nbPlacesOccupeesInitialement : " + (nbPlacesOccupeesInit=rst.getInt("NPOI")));
			    
			    rst.close();
			    pstmt.close();
			    break;
			    /*
			      SELECT count(MAX(DateArrivee)) AS NB FROM Deplacement WHERE stationArrivee='Chavant' AND DateArrivee IS NOT NULL AND DateArrivee<'14-NOV-90' AND DateDepart<'14-NOV-90' GROUP BY idV;
			      SELECT SUM(nbPlacesVelo, nbPlacesVelE, nbPlacesVoiE, nbPlacesVrem, nbPlacesUtil) FROM Deplacement;
			    */

			default:
			    System.out.println("mauvais choix, taper 1, 2 ou 3");  //erreur, on recommence
			    break;
			}
			printChoix();
			query=sc.nextInt();
	    }
	    connection.close();
	}catch (SQLException e){
	    e.printStackTrace();
	}
    }
}
    
