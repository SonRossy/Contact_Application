
package contact;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


public class FXMLDocumentController implements Initializable {
    LogingModel log=new LogingModel();//from our class
    
    @FXML
    private Label label;
     
    @FXML
    TextField NameField;
    
    @FXML
    TextField NumberField;
    
    // the listview of Name id for ui linking
    @FXML
    ListView<String> NameView;
    @FXML
    ListView<String> NumberView;
    
    // making two list to store result which only takes a list 
    ObservableList<String> Namelist;
    ObservableList<String> Numberlist;

    // making two normal list to pass to observable list
    List<String> Nnamelist = new ArrayList<String>();
    List<String> Nnumberlist = new ArrayList<String>();
    

     
    Logic logic;//stance of our logic class
     
    Connection connection;
    public FXMLDocumentController()
    {
        logic=new Logic();
        try {
            connection=SqliteConnetion.Connector();//connector is from the class that we created 
        } catch (SQLException ex) {
           System.out.println("not connected");
        }
        if(connection==null){
            System.exit(1);
        }
    }
    
    @FXML
    private void ShowAll(ActionEvent event) throws SQLException {
        String sql=" select Name as name, Numbe as Number from Phone_Number;";
         try {
           PreparedStatement   stmt = connection.prepareStatement( sql );
           String NameResult, NumberResult;
           logic.ShowAllLogic(stmt); 
         } catch (SQLException ex) {
             Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
         }
         
        Namelist = logic.Name;// name is global variable from Logic class
        Numberlist=logic.Number;
        NameView.setItems(Namelist);
        NumberView.setItems(Numberlist);
        
        Nnumberlist.clear();
        Nnamelist.clear();	
    }
    
     @FXML//without this, you cant transfer function to buttons
    public void Search(ActionEvent event)throws SQLException{
        String NameString= "";
        NameString=NameField.getText();
        String sql=" select Name as name, Numbe as Number from Phone_Number "+
                "where name like ?"; 
        PreparedStatement   stmt = connection.prepareStatement( sql );
        
        List<String> NlistName=new ArrayList<String>();
        List<String> NlistNumber=new ArrayList<String>();
        String NameResult=""; 
        String NumberResult="";
        
        if(!NameString.isEmpty()){
        stmt.setString(1, NameString);//we use setString when there is a ? in the sql statement, the second argument replace ?
        ResultSet res = stmt.executeQuery();
        
        while ( res.next() ) {
                NameResult=res.getString("name");
                NumberResult=res.getString("Number");
                NlistName.add(NameResult);
                NlistNumber.add(NumberResult);	
	}
        Namelist=FXCollections.observableArrayList(NlistName);
        Numberlist=FXCollections.observableArrayList(NlistNumber);
        NameView.setItems(Namelist);
        NumberView.setItems(Numberlist);
        Nnumberlist.clear();
        Nnamelist.clear();	
        }
        else if(NameString.isEmpty())
        {
            NlistName.add("SEARCH BOX BELOW is EMPTY");
            Namelist=FXCollections.observableArrayList(NlistName);
            Numberlist=FXCollections.observableArrayList(NlistNumber);
            NameView.setItems(Namelist);
            NumberView.setItems(Numberlist);
            Nnumberlist.clear();
            Nnamelist.clear();	
        }
        if(NameResult.isEmpty()&&!NameString.isEmpty())//meaning the name search gave no result then we output that no name in contact 
        {
            NlistName.add("NAME is NOT IN CONTACT");
            Namelist=FXCollections.observableArrayList(NlistName);
            Numberlist=FXCollections.observableArrayList(NlistNumber);
            NameView.setItems(Namelist);
            NumberView.setItems(Numberlist);
            Nnumberlist.clear();
            Nnamelist.clear();	
        }
        
    }
    
    @FXML
    public void Add(ActionEvent event)throws SQLException{
        String NameString= "";
        String NumberString="";
        NameString=NameField.getText();
        NumberString=NumberField.getText();
        String sql=" INSERT INTO Phone_Number (Name,Numbe) VALUES (?, ?);"; 
        PreparedStatement   stmt = connection.prepareStatement( sql );
        
        List<String> NlistName=new ArrayList<String>();
        List<String> NlistNumber=new ArrayList<String>();
        /*String NameResult=""; 
        String NumberResult="";*/
        
        if(!NameString.isEmpty()&&!NumberString.isEmpty()){
        stmt.setString(1, NameString);//we use setString when there is a ? in the sql statement, the second argument replace ?
        stmt.setString(2, NumberString);
        stmt.executeUpdate();//we use that to make change to table instead of res= stmt.executeQuery() since we dont have a result
        //ResultSet res = stmt.executeQuery();
        NlistName.add("NAME ADDED");
        Namelist=FXCollections.observableArrayList(NlistName);
        Numberlist=FXCollections.observableArrayList(NlistNumber);
        NameView.setItems(Namelist);
        NumberView.setItems(Numberlist);
        Nnumberlist.clear();
        Nnamelist.clear();	
        
        }
       else if(NameString.isEmpty()||NumberString.isEmpty())//meaning the name search gave no result then we output that no name in contact 
        {
            NlistName.add("NO BOX SHOULD BE EMPTY");
            NlistName.add("ENTER NAME AND NUMBER");
            Namelist=FXCollections.observableArrayList(NlistName);
            Numberlist=FXCollections.observableArrayList(NlistNumber);
            NameView.setItems(Namelist);
            NumberView.setItems(Numberlist);
            Nnumberlist.clear();
            Nnamelist.clear();	
        }         
    }
    
     @FXML
    public void Delete(ActionEvent event)throws SQLException{
        String NameString= "";
        String NameResult="";
        NameString=NameField.getText();
        String sql="DELETE FROM Phone_Number WHERE Name like ?;"; 
        String sql2="select Name as name from Phone_Number where Name like ?;";
        
        PreparedStatement   stmt = connection.prepareStatement( sql );
        PreparedStatement stmt2=connection.prepareStatement( sql2 );
        
       
        
        List<String> NlistName=new ArrayList<String>();
        List<String> NlistNumber=new ArrayList<String>();
       // NameField.setText(NameView.getSelectionModel().getSelectedItem());
        
        stmt2.setString(1, NameString);
        ResultSet res = stmt2.executeQuery();
        
        while ( res.next() ) {
                NameResult=res.getString("name");
               //System.out.print(NameResult);
                NlistName.add(NameResult);         	
	}
        
        if(!NameString.isEmpty() && !NlistName.isEmpty() ){
         NlistName.clear();
       
        stmt.setString(1, NameString);//we use setString when there is a ? in the sql statement, the second argument replace ?
        stmt.executeUpdate();//we use that to make change to table instead of res= stmt.executeQuery() since we dont have a result      
        
        NlistName.add("NAME DELETED");
        //rest of the code is just to print out the message Name Deleted
        Namelist=FXCollections.observableArrayList(NlistName);
        Numberlist=FXCollections.observableArrayList(NlistNumber);
        NameView.setItems(Namelist);
        NumberView.setItems(Numberlist);
        Nnumberlist.clear();
        Nnamelist.clear();
        }
        
       else if(!NameString.isEmpty()&& NlistName.isEmpty() )
        {
            NlistName.add("NAME IS NOT IN CONTACT");
            Namelist=FXCollections.observableArrayList(NlistName);
            Numberlist=FXCollections.observableArrayList(NlistNumber);
            NameView.setItems(Namelist);
            NumberView.setItems(Numberlist);
            Nnumberlist.clear();
            Nnamelist.clear();	
            
        }
       else if(NameString.isEmpty())//meaning the name search gave no result then we output that no name in contact 
        {
            NlistName.add("NO NAME TO DELETE");
            NlistName.add("PLEASE ADD NAME");
            NlistName.add("IN THE BOX TO DELETE");
            //NlistName.add("ENTER NAME AND NUMBER");
            Namelist=FXCollections.observableArrayList(NlistName);
            Numberlist=FXCollections.observableArrayList(NlistNumber);
            NameView.setItems(Namelist);
            NumberView.setItems(Numberlist);
            Nnumberlist.clear();
            Nnamelist.clear();	
        
       }
    }
    
     
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("create table Phone_number(Name vachar(25) not null , Numbe vachar(15) not null);");
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*
        following code make scrolls on listview go up and down together*/
        Platform.runLater(new Runnable() {
    @Override
    public void run() {
        Node n = NameView.lookup(".scroll-bar");
          Node n2 = NumberView.lookup(".scroll-bar");
        if (n instanceof ScrollBar) {
            final ScrollBar bar = (ScrollBar) n;
            if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                final ScrollBar bar2 = (ScrollBar) n2;
                // get the second scrollbar of another listview and bind values of them
                bar.valueProperty().bindBidirectional(bar2.valueProperty());
            }
        }
    }
});
NameView.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            NameField.setText(NameView.getSelectionModel().getSelectedItem());
            //listView.getSelectionModel().getSelectedIndex(); give the number of index of the item selected
            //listView.getSelectionModel().select(2); select the 1 item in the list
            //listView.getFocusModel().focus(N);tableView.getSelectionModel().clearSelection();
            
            //next line select item in the numberview if an item in the nameView is selected
            NumberView.getSelectionModel().select(NameView.getSelectionModel().getSelectedIndex());
        }
    });

}  
   
}


