/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Views.Customers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import movierentalsystem.Models.Customer;

/**
 * FXML Controller class
 *
 * @author Eli Núñez
 */
public class CustomersDialogController implements Initializable {
    @FXML
    private Button btnUpdate;

    @FXML
    private Label lblID;

    @FXML
    private TextField txtFirstName;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtCity;

    @FXML
    private TextArea taAddr;

    @FXML
    private TextField txtZipCode;

    @FXML
    private TextField txtPhone;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // On cancel btn clicked
        btnCancel.setOnAction((e) -> {
            closeCustStage();
        });
    }    
    //Gets selected customer from CustomersController
    public void setSelectedCustomer(ObservableList<Customer> selectedCustomer) {
        lblID.setText(selectedCustomer.get(0).ID.getValue());
        txtFirstName.setText(selectedCustomer.get(0).firstName.getValue());
        txtLastName.setText(selectedCustomer.get(0).lastName.getValue());
        txtPhone.setText(selectedCustomer.get(0).phone.getValue());
        taAddr.setText(selectedCustomer.get(0).address.getValue());
        txtCity.setText(selectedCustomer.get(0).city.getValue());
        txtZipCode.setText(selectedCustomer.get(0).zipCode.getValue());
    }
    
    public void closeCustStage() {
        // get a handle to the stage
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
}
