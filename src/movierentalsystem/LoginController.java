/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Eli Núñez
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        label.setText("Connecting...");
        
        MySQLConnection conn = new MySQLConnection();
        System.out.println("Connecting...");
        String sql = "SELECT * FROM `test`";
        
        List<String> data = new ArrayList<>();
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while(rs.next()) {
                String id = Integer.toString(rs.getInt("id"));
                String val = rs.getString("value");
                data.add(val);
            }
            
            rs.close();
            statement.close();
            
            System.out.println(data);
            label.setText("Connected Succesfully!");
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            conn.disconnect();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
