/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Views.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import movierentalsystem.Models.Customer;
import movierentalsystem.MySQLConnection;
/**
 *
 * @author Eli Núñez
 */
public class CustomersController implements Initializable {
    
    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private TableColumn<Customer, String> col_id;
    
    @FXML
    private TableColumn<Customer, String> col_firstName;

    @FXML
    private TableColumn<Customer, String> col_lastName;

    @FXML
    private TableColumn<Customer, String> col_addr;

    @FXML
    private TableColumn<Customer, String> col_city;

    @FXML
    private TableColumn<Customer, String> col_zip;

    @FXML
    private TableColumn<Customer, String> col_phone;
    
    @FXML
    private Button btnEditTableData;  
    
    @FXML
    private Button btnDelete;
    
    ObservableList<Customer> oblCustTableData = FXCollections.observableArrayList();
    
    ObservableList<Customer> selectedCustomer = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TableView CellData Listeners
        col_id.setCellValueFactory(cellData -> cellData.getValue().IDProperty());
        col_firstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col_lastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col_addr.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        col_city.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        col_zip.setCellValueFactory(cellData -> cellData.getValue().zipCodeProperty());
        col_phone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        
        // TableView Init Editable
        tblCustomers.setEditable(true);
        col_firstName.setCellFactory(TextFieldTableCell.forTableColumn());

        
        //Gets Customer on TableView Item Selected
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCustomer = tblCustomers.getSelectionModel().getSelectedItems();
                // Enable edit btn
                btnEditTableData.setDisable(false);
                System.out.println(selectedCustomer);
            }
        });
        
        // On edit button click
        btnEditTableData.setOnAction((e) -> {
            try {
                // Passing selectedCustomer and navigating to CustomerDialogView
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomersDialog.fxml"));
                Parent root = (Parent) loader.load();
                CustomersDialogController custDialogCntrl = loader.getController();
                custDialogCntrl.setSelectedCustomer(selectedCustomer);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Customer Manager");
                stage.show();                
            } catch (IOException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // On cancel btn clicked
        btnDelete.setOnAction((e) -> {
            try {
                deleteCustomer(selectedCustomer.get(0).ID.getValue());
            } catch (IOException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // TODO
        //Disable edit btn
        btnEditTableData.setDisable(true);
        
        // Query Table Data
        getTableData();
    }
    
    public void getTableData() {
        // TODO        
        MySQLConnection conn = new MySQLConnection();
        String sql = "SELECT * FROM `customer`";
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            oblCustTableData.removeAll(oblCustTableData);
            while(rs.next()) {
                oblCustTableData.add(new Customer(
                    Integer.toString(rs.getInt("ID")),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Address"),
                    rs.getString("City"),
                    rs.getString("ZipCode"),
                    rs.getString("Phone")               
                ));
            }
            
            rs.close();
            statement.close();
            
            tblCustomers.setItems(oblCustTableData);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void onFirstNameChanged(TableColumn.CellEditEvent<Customer,String> ev) {
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        customer.setFirstName(ev.getNewValue());
    }
    
    public void deleteCustomer (String id) throws IOException {
        // TODO     
        MySQLConnection conn = new MySQLConnection();
        String sql = "DELETE FROM `customer` WHERE ID='" + id +"'";
        
        // Remove Cusotmer Data
        PreparedStatement statement;
        try {
            // Removing customer from db
            statement = conn.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
            statement.close();
            
            // Removing customer from TableView
            int selectedIndex = tblCustomers.getSelectionModel().getSelectedIndex();
            tblCustomers.getItems().remove(selectedIndex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }    
}
