package ui.aquacare;

import java.sql.Connection;
import java.sql.DriverManager;

public class FishDBManager {

    public Connection databaseLink;

    public Connection getDBConnection(){
        String databaseName = "fishinfo";
        String databaseUser = "root";
        String databasePassword = "0000q";
        String url = "jdbc:mysql://localhost/"+ databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }
}
