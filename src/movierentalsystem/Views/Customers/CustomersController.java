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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private Label lblCustInfo;
    
    @FXML
    private Button btnAdd;    
    
    @FXML
    private Button btnDelete;
    
    @FXML
    private TextField txtFirstName;
    
    @FXML
    private TextField txtLastName;

    @FXML
    private TextArea taAddr;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtZipCode;
    
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
        col_lastName.setCellFactory(TextFieldTableCell.forTableColumn());
        col_addr.setCellFactory(TextFieldTableCell.forTableColumn());
        col_city.setCellFactory(TextFieldTableCell.forTableColumn());
        col_zip.setCellFactory(TextFieldTableCell.forTableColumn());
        col_phone.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //Gets Customer on TableView Item Selected
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCustomer = tblCustomers.getSelectionModel().getSelectedItems();
                lblCustInfo.setText("Double-click field to update info, press Enter to save changes...");
                System.out.println(selectedCustomer);
            }
        });
        
        // On edit button click
        btnAdd.setOnAction((e) -> {    
            // TODO
            addNewCustomer();
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
        
        // Get Customer Table Data
        getCustTableData();
    }
    
    // Gets Customer Data
    public void getCustTableData() {
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
    
    // Adds new Customer
    public void addNewCustomer() {
        if (txtFirstName.getText().isEmpty()) {
            lblCustInfo.setText("First Name is required...");
        } else {
            // Get last customer id
            Customer lastCust = oblCustTableData.get(oblCustTableData.size() - 1);
            int newID = Integer.parseInt(lastCust.getID()) + 1;
            // Get New Customer Data
            Customer newCust = new Customer(
                Integer.toString(newID),
                txtFirstName.getText(),
                txtLastName.getText(),
                taAddr.getText(),
                txtCity.getText(),
                txtZipCode.getText(),
                txtPhone.getText()
            );
            // Insert new customer to database
            MySQLConnection conn = new MySQLConnection();
            PreparedStatement statement;
            try {
                // Update Cusotmer Data in SQL db
                statement = conn.connect().prepareStatement("INSERT INTO `customer` (`FirstName`, `LastName`, `Address`, `City`, `ZipCode`, `Phone`) VALUES (?, ?, ?, ?, ?, ?);");
                statement.setString(1, newCust.getFirstName());
                statement.setString(2, newCust.getLastName());
                statement.setString(3, newCust.getAddress());
                statement.setString(4, newCust.getCity());
                statement.setString(5, newCust.getZipCode());
                statement.setString(6, newCust.getPhone());

                statement.executeUpdate();
                statement.close();

                // Add new customer to tableview
                oblCustTableData.add(newCust);
                lblCustInfo.setText("New customer added sucessfully...");
            } catch (SQLException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    // Updates Customer Data
    // Updates First Name
    public void onFirstNameChanged(TableColumn.CellEditEvent<Customer,String> ev) { 
        // TODO  
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        String custID = customer.getID();
        String fn = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `customer` SET FirstName='" + fn + "' WHERE ID='" + custID +"'";
        PreparedStatement statement;
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
            statement.close();
            
            // Update in TableView
            customer.setFirstName(fn);
            lblCustInfo.setText(customer.getFirstName() + "'s First Name updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Updates Last Name
    public void onLastNameChanged(TableColumn.CellEditEvent<Customer,String> ev) {        
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        String custID = customer.getID();
        String ln = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `customer` SET LastName='" + ln + "' WHERE ID='" + custID +"'";
        PreparedStatement statement;
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
            statement.close();
            
            // Update in TableView
            customer.setLastName(ln);
            lblCustInfo.setText(customer.getFirstName() + "'s Last Name updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    // Updates Address
    public void onAddrChanged(TableColumn.CellEditEvent<Customer,String> ev) {        
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        String custID = customer.getID();
        String addr = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `customer` SET Address='" + addr + "' WHERE ID='" + custID +"'";
        PreparedStatement statement;
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
            statement.close();
            
            // Update in TableView
            customer.setAddress(addr);
            lblCustInfo.setText(customer.getFirstName() + "'s Address updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    // Updates City
    public void onCityChanged(TableColumn.CellEditEvent<Customer,String> ev) {        
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        String custID = customer.getID();
        String city = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `customer` SET City='" + city + "' WHERE ID='" + custID +"'";
        PreparedStatement statement;
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
            statement.close();
            
            // Update in TableView
            customer.setCity(city);
            lblCustInfo.setText(customer.getFirstName() + "'s City updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    // Updates ZipCode
    public void onZipCodeChanged(TableColumn.CellEditEvent<Customer,String> ev) {        
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        String custID = customer.getID();
        String zipCode = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `customer` SET ZipCode='" + zipCode + "' WHERE ID='" + custID +"'";
        PreparedStatement statement;
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
            statement.close();
            
            // Update in TableView
            customer.setZipCode(zipCode);
            lblCustInfo.setText(customer.getFirstName() + "'s ZipCode updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    // Updates Phone
    public void onPhoneChanged(TableColumn.CellEditEvent<Customer,String> ev) {        
        Customer customer = tblCustomers.getSelectionModel().getSelectedItem();
        String custID = customer.getID();
        String phone = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `customer` SET Phone='" + phone + "' WHERE ID='" + custID +"'";
        PreparedStatement statement;
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.executeUpdate(sql);
            statement.close();
            
            // Update in TableView
            customer.setPhone(phone);
            lblCustInfo.setText(customer.getFirstName() + "'s Phone updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    // Delete Customer
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
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }    
}
