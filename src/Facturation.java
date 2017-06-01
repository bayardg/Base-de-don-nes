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

public class Facturation {


  static final String forfaitExiste = "SELECT COUNT(*) FROM Forfait WHERE idForfait =? "; //si vaut 0 alors non sinon oui
  static final String aUnForfaitDI = "SELECT COUNT(*) FROM ForfaitDI WHERE idForfait =?";
  static final String aUnForfaitDL = "SELECT COUNT(*) FROM ForfaitDL WHERE idForfait =?";
  static final String recupDateDebut = "SELECT dateDebut FROM ForfaitDL WHERE idForfait =?";
  static final String recupDateFin = "SELECT dateFin FROM ForfaitDL WHERE idForfait =?";
  static final String recupPrixHor = "SELECT prix FROM ForfaitDL WHERE idForfait =?";
  static final String recupPrixDI = "SELECT prix FROM ForfaitDI WHERE idForfait =?";
  static final String recupNumCB = "SELECT numCB FROM Forfait WHERE idForfait =?";
  static final String recupDateNaiss = "SELECT dateNaissance FROM Utilisateur WHERE numCB =?";



  public static void main(String[] args){

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

        PreparedStatement pstmt;





        /*
        ** Je vais m'occuper maintenant de la Facturation
        ** On suppose qu'on connait l'idForfait du forfait que l'on va facturer
        ** et que ce forfait est arrivé à expiration.
        ** On cherche alors le prix que l'utilisateur doit payer. */

        // Vérifions que ce forfait existe bien dans notre base de donnée :
        boolean forfExiste = false;
        pstmt=connection.prepareStatement(forfaitExiste);
	      pstmt.setString(1,args[0]);
	      ResultSet rst = pstmt.executeQuery();
        int cpt = 0;
        if(rst.next()){
            cpt = rst.getInt(1);
        }
        if (cpt == 1){
          forfExiste = true;
        }
        pstmt.close();
        // System.out.println(args[0]);
        // System.out.println(forfExiste);


        // Si le forfaitExiste :
        if (forfExiste) {
          //On veut savoir si c'est un forfait DL ou DI :

          //Verifions si ce forfait est un forfaitDL :
          boolean forfDLExiste = false;
          pstmt=connection.prepareStatement(aUnForfaitDL);
  	      pstmt.setString(1,args[0]);
  	      rst = pstmt.executeQuery();
          cpt = 0;
          if(rst.next()){
              cpt = rst.getInt(1);
          }
          if (cpt == 1){
            forfDLExiste = true;
          }
          pstmt.close();
          // System.out.println(args[0]);
          // System.out.println(forfDLExiste);

          //Verifions si ce forfait est un forfaitDI :
          boolean forfDIExiste = false;
          pstmt=connection.prepareStatement(aUnForfaitDI);
  	      pstmt.setString(1,args[0]);
  	      rst = pstmt.executeQuery();
          cpt = 0;
          if(rst.next()){
              cpt = rst.getInt(1);
          }
          if (cpt == 1){
            forfDIExiste = true;
          }
          pstmt.close();
          // System.out.println(args[0]);
          // System.out.println(forfDIExiste);

          long facturationSansReduc = 0;
          //Si il sagit bien dun forfaitDL :
          if (forfDLExiste){
            //On récupère la dateDebut et la dateFin :
            //dateDebut :
            pstmt=connection.prepareStatement(recupDateDebut);
    	      pstmt.setString(1,args[0]);
    	      rst = pstmt.executeQuery();
            Date dateDeb = new Date();
            if(rst.next()){
              dateDeb = rst.getDate("dateDebut");
            }
            // System.out.println(dateDeb);

            //dateFin :
            pstmt=connection.prepareStatement(recupDateFin);
    	      pstmt.setString(1,args[0]);
    	      rst = pstmt.executeQuery();
            Date dateF = new Date();
            if(rst.next()){
              dateF = rst.getDate("dateFin");
            }
            // System.out.println(dateF);

            //on convertit le champs en heures
            long diff= ((dateF.getTime()-dateDeb.getTime())/(1000*3600));
            // System.out.println(diff);
            //On recupère le prix horaire :
            pstmt=connection.prepareStatement(recupPrixHor);
    	      pstmt.setString(1,args[0]);
    	      rst = pstmt.executeQuery();
            long prixHor = 0;
            if(rst.next()){
              prixHor = rst.getInt("prix");
            }
            // System.out.println(prixHor);
            facturationSansReduc = diff * prixHor;
            // System.out.println(facturationSansReduc);
          }

          if (forfDIExiste) {
            //On récupère le prix :
            pstmt=connection.prepareStatement(recupPrixDI);
    	      pstmt.setString(1,args[0]);
    	      rst = pstmt.executeQuery();
            long prixDI = 0;
            if(rst.next()){
              prixDI = rst.getInt("prix");
            }
            // System.out.println(prixDI);
            facturationSansReduc = prixDI;
          }

          //Vérifions si l'utilisateur a droit à une réduction :

          //On récupère d'abord le numCB lié à l'idForfait :
          pstmt=connection.prepareStatement(recupNumCB);
          pstmt.setString(1,args[0]);
          rst = pstmt.executeQuery();
          String ncb = " ";
          if(rst.next()){
            ncb = rst.getString("numCB");
          }
          // System.out.println(ncb);

          //On récupère ensuite la date de naissance de l'utilisateur concerné :
          pstmt=connection.prepareStatement(recupDateNaiss);
          pstmt.setString(1,args[0]);
          rst = pstmt.executeQuery();
          Date dateN = new Date();
          if(rst.next()){
            dateN = rst.getDate("dateNaissance");
          }
          // System.out.println(dateN);
          //On récupère la date actuelle :
          Date dateCour = Calendar.getInstance().getTime();

          long diffAge = ((dateCour.getTime()-dateN.getTime())/(1000*3600*24));
          // System.out.println(diffAge);
          long facturationApresReduc = facturationSansReduc;
          if (diffAge <= 25 || diffAge >= 65){
            facturationApresReduc -= 0.25 * facturationApresReduc;
          }
          System.out.println("vous devez payer"+facturationApresReduc);
        }
        else {
          System.out.println("Ce forfait n'existe pas !");
        }

        connection.close();
      }catch (SQLException e){
      e.printStackTrace();
    }
  }
}
