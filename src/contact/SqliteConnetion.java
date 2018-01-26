/*
 always use this class to connect sqlite database 
 */
package contact;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteConnetion {
    
    //WE NEED THE FOLLWOING CODE FOR A SQLITE CONNECTION
    public static Connection Connector() throws SQLException
    {  
       
    try {
        Class.forName("org.sqlite.JDBC");
        Connection conn=DriverManager.getConnection("jdbc:sqlite:Contacto.sqlite");
        return conn;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteConnetion.class.getName()).log(Level.SEVERE, null, ex);
        }
      return null;
    }

    
}
