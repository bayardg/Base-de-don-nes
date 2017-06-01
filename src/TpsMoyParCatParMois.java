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

public class TpsMoyParCatParMois {
  static final String recupIdV = "SELECT SUM(DateArrivee - DateDepart)/(COUNT (*)) FROM Deplacement d, Vehicule v WHERE v.idV=d.idV AND catV=? AND DateDepart Between TO_DATE(?, 'MM-DD-YYYY') and TO_DATE(?,'MM-DD-YYYY') AND DateArrivee Between TO_DATE(?, 'MM-DD-YYYY') and TO_DATE(?,'MM-DD-YYYY') GROUP BY catV, v.idV";



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
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);


        pstmt=connection.prepareStatement(recupIdV);
        pstmt.setString(1,args[0]);
        pstmt.setString(2,args[1]);
        pstmt.setString(3,args[2]);
        pstmt.setString(4,args[1]);
        pstmt.setString(5,args[2]);
        ResultSet rst = pstmt.executeQuery();
        //String idVehicule = " ";
        long resultat = 0;
        if(rst.next()){
          //idVehicule = rst.getString(1);
          resultat = rst.getInt(1);
          System.out.println("dans if");
        }
        //System.out.println("L'idVehicule est " + idVehicule);
        System.out.println("Le résultat est " + resultat);
        pstmt.close();


        connection.close();
      }catch (SQLException e){
      e.printStackTrace();
    }
  }


}
