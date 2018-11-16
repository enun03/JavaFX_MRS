/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import movierentalsystem.Models.DataTableModel;
import movierentalsystem.MySQLConnection;
/**
 *
 * @author Eli Núñez
 */
public class LoginController implements Initializable {
    
    @FXML
    private Button button;

    @FXML
    private Label label;

    @FXML
    private TableView<DataTableModel> tblData;

    @FXML
    private TableColumn<DataTableModel, String> col_id;

    @FXML
    private TableColumn<DataTableModel, String> col_value;
    
    @FXML
    private Button btnEditTableData;
     
    ObservableList<DataTableModel> obDataTableList = FXCollections.observableArrayList();
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        // Init EventHandlers
        // On edit button click
        btnEditTableData.setOnAction((e) -> {
            Stage stage = null;
            Parent myNewScene = null;
            try {
                stage = (Stage) btnEditTableData.getScene().getWindow();            
                myNewScene = FXMLLoader.load(getClass().getResource("EditTableData.fxml"));
                
                Scene scene = new Scene(myNewScene);
                stage.setScene(scene);
                stage.setTitle("My New Scene");
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // Fetching Table Data
        getTableData();
    }  
    
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
    
    public void getTableData() {
        // TODO
        MySQLConnection conn = new MySQLConnection();
        String sql = "SELECT * FROM `test`";
        
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while(rs.next()) {
                obDataTableList.add(new DataTableModel(
                    Integer.toString(rs.getInt("id")),
                    rs.getString("value")
                ));
            }
            
            rs.close();
            statement.close();
            
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_value.setCellValueFactory(new PropertyValueFactory<>("value"));
            
            tblData.setItems(obDataTableList);
            
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
