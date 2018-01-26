
package contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

public class Logic {
    
    //global varaible to pass to the other class
    ObservableList<String> Name;
    ObservableList<String> Number;
    TextField NameFieldLogic;
    
    public Logic(){
        //to do some initializing
    }
    
    public void ShowAllLogic(PreparedStatement stmt) throws SQLException{
        
        List<String> NlistName=new ArrayList<String>();
        List<String> NlistNumber=new ArrayList<String>();
        String NameResult, NumberResult;
        ResultSet res = stmt.executeQuery();
        while ( res.next() ) {
                NameResult=res.getString("name");
                NumberResult=res.getString("Number");
                NlistName.add(NameResult);
                NlistNumber.add(NumberResult);	
	}
        Name = FXCollections.observableArrayList(NlistName);
        Number=FXCollections.observableArrayList(NlistNumber);            
    }
    
   
    
}
