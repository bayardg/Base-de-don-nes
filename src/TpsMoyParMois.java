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

public class TpsMoyParMois {
  static final String recupIdV = "SELECT v.idV, SUM(DateArrivee - DateDepart)/(COUNT (*)) FROM Deplacement d, Vehicule v WHERE v.idV=d.idV AND DateDepart Between TO_DATE(?, 'MM-DD-YYYY') and TO_DATE(?,'MM-DD-YYYY') AND DateArrivee Between TO_DATE(?, 'MM-DD-YYYY') and TO_DATE(?,'MM-DD-YYYY') GROUP BY  v.idV";



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
        ** On veut le temps moyen d'utilisation par véhicule pour un mois donné
        ** On connait le mois voulu donc on connait la date du premier jour du mois
        ** et celle du dernier jour du mois */

        pstmt=connection.prepareStatement(recupIdV);
	      pstmt.setString(1,args[0]);
        pstmt.setString(2,args[1]);
        pstmt.setString(3,args[0]);
        pstmt.setString(4,args[1]);
        ResultSet rst = pstmt.executeQuery();
        String idVehicule = " ";
        long resultat = 0;
        if(rst.next()){
          idVehicule = rst.getString(1);
          resultat = rst.getInt(2);
        }
        System.out.println("l'idV est :" + idVehicule);
        System.out.println(" le resultat est :" + resultat);
        pstmt.close();


        connection.close();
      }catch (SQLException e){
      e.printStackTrace();
    }
  }


}
