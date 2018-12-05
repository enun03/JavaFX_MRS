/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Views;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import movierentalsystem.Models.Customer;
import movierentalsystem.MySQLConnection;
/**
 * FXML Controller class
 *
 * @author Eli Núñez
 */
public class RegisterController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane apRegister;

    @FXML
    private TextField txtFisrtName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtCity;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnCancel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // On edit button click
        btnRegister.setOnAction((e) -> {    
            try {
                // TODO
                register();
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    

    private void register() throws IOException {
        // TODO          
        // Insert new user to database
        MySQLConnection conn = new MySQLConnection();
        PreparedStatement statement1;            
        PreparedStatement statement2;
        String newCustomerID = getNewCustID();
        String firstName = txtFisrtName.getText();
        String newUserName = txtUserName.getText();
        String newPassword = txtPassword.getText();

        if (firstName.isEmpty() || newUserName.isEmpty() || newPassword.isEmpty()) {
            System.out.println("Required fields are empty...");
        } else {
            try {
                // Update Cusotmer Data in SQL db
                statement1 = conn.connect().prepareStatement("INSERT INTO `customer` (`FirstName`, `LastName`, `City`, `BirthDate` ) VALUES (?, ?, ?, ?);");
                statement1.setString(1, firstName);
                statement1.setString(2, txtLastName.getText());
                statement1.setString(3, txtCity.getText());
                statement1.setString(4, dpBirthDate.getValue().toString());

                statement1.executeUpdate();
                statement1.close();

                // Ading new user
                statement2 = conn.connect().prepareStatement("INSERT INTO `users` (`Cust_ID`, `Role_ID`, `UserName`, `Password`,  `RegisteredDate`) VALUES (?, ?, ?, ?, NOW());");
                statement2.setString(1, newCustomerID);
                statement2.setString(2, "2");
                statement2.setString(3, newUserName);
                statement2.setString(4, newPassword);


                statement2.executeUpdate();
                statement2.close();

            } catch (SQLException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
            redirectUser();
        }
    }
    
    public String getNewCustID () {
        String newID = "";
        // TODO        
        MySQLConnection conn = new MySQLConnection();
        String sql = "SELECT MAX(ID) as ID FROM `customer`";
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                newID =Integer.toString(rs.getInt("ID"));
            }
            
            rs.close();
            statement.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Calculating new ID
        int ni = Integer.parseInt(newID) + 1;
        newID = Integer.toString(ni);
        
        return newID;
    }
    
    public void redirectUser() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        Stage loginWindow = (Stage) btnRegister.getScene().getWindow();
        loginWindow.close();
    };
    
    
}
