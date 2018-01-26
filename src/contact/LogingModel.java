
package contact;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogingModel {
    Connection connection;
    public LogingModel()
    {
        try {
            connection=SqliteConnetion.Connector();//connector is from the class that we created 
        } catch (SQLException ex) {
           System.out.println("not connected");
        }
        if(connection==null){
            System.exit(1);
        }
    }
    
    public boolean IsDbConnected()
    {
        try {
            return !connection.isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(LogingModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
