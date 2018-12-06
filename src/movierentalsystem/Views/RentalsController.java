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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import movierentalsystem.Models.Rental;
import movierentalsystem.MySQLConnection;
/**
 * FXML Controller class
 *
 * @author Eli Núñez
 */
public class RentalsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<Rental> tblRentals;

    @FXML
    private TableColumn<Rental, String> colMovieID;

    @FXML
    private TableColumn<Rental, String> colTitle;

    @FXML
    private TableColumn<Rental, String> colCustID;

    @FXML
    private TableColumn<Rental, String> colFirstName;

    @FXML
    private TableColumn<Rental, String> colLastName;

    @FXML
    private TableColumn<Rental, String> colRentedDate;

    @FXML
    private Button btnReturn;

    @FXML
    private Button btnAddRental;
    
    ObservableList<Rental> oblRentalTableData = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // TableView CellData Listeners
        colMovieID.setCellValueFactory(cellData -> cellData.getValue().MovieIdProperty());
        colTitle.setCellValueFactory(cellData -> cellData.getValue().MovieTitleProperty());
        colCustID.setCellValueFactory(cellData -> cellData.getValue().CustIdProperty());
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().FirstNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().LastNameProperty());
        colRentedDate.setCellValueFactory(cellData -> cellData.getValue().RentedDateProperty());
        
        // On return button click
        btnReturn.setOnAction((e) -> {    
            try {
                // TODO
                showAlert();
            } catch (ParseException | IOException ex) {
                Logger.getLogger(RentalsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        getRentals();
    }    
    
    public void getRentals () {
        // TODO        
        MySQLConnection conn = new MySQLConnection();
        String sql = "SELECT movie.ID AS MovieID, movie.Title, customer.ID AS CustID, customer.FirstName, customer.LastName, rental.RentedDate FROM ((rental INNER JOIN customer ON rental.customer_id = customer.ID) INNER JOIN movie ON rental.movie_id = movie.ID)";
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while(rs.next()) {
                oblRentalTableData.add(new Rental(
                    Integer.toString(rs.getInt("MovieID")),
                    rs.getString("Title"),
                    Integer.toString(rs.getInt("CustID")),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getDate("RentedDate").toString()
                ));
            }
            
            rs.close();
            statement.close();
            
            tblRentals.setItems(oblRentalTableData);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void returnMovie(Rental rental) {
        // TODO     
        MySQLConnection conn = new MySQLConnection();
        
        // Statements init
        String sql1= "DELETE FROM `rental` WHERE movie_id='" + rental.getMovieId() +"';";
        String sql2= "Update `movie` SET Status='Available';";
        PreparedStatement statement1;
        PreparedStatement statement2;
        
        try {
            // Removing rental from db
            statement1 = conn.connect().prepareStatement(sql1);
            statement1.executeUpdate(sql1);
            statement1.close();
            
            // Updating movie status
            statement2 = conn.connect().prepareStatement(sql2);
            statement2.executeUpdate(sql2);
            statement2.close();
            
            // Removing rental from TableView
            int selectedIndex = tblRentals.getSelectionModel().getSelectedIndex();
            tblRentals.getItems().remove(selectedIndex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }
    
    // Shows alert confirmation for rental
    public void showAlert() throws ParseException, IOException {
        // Get selected rental
        Rental selectedRental = tblRentals.getSelectionModel().getSelectedItem();
        
        if (selectedRental != null) {
            // Format dates
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String rentedDateStr = selectedRental.getRentedDate();    
            String returnDateStr = java.time.LocalDate.now().toString();

            Date rentedDate = new java.sql.Date(df.parse(rentedDateStr).getTime());
            Date returnDate = new java.sql.Date(df.parse(returnDateStr).getTime());

            // Calculating rented days
            long daysRented = getDifferenceDays(rentedDate, returnDate);

            // Calculating total cost
            long totalCost = (long) (daysRented * 2);

            // Formats cost to US currency $0.00
            NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);

            // Configuring alert template
            String template = "Rental Summary: \n\n"
                + "Movie:   " + selectedRental.getMovieTitle() + "\n"
                + "Customer:    " + selectedRental.getFirstName() + " " + selectedRental.getLastName() + "\n"
                + "Rented On:    " + rentedDateStr + "\n"
                + "Returned On:    " + returnDateStr + "\n"
                + "Rate:    $2.00 per day \n"
                + "Total Days:    " + daysRented + "\n"
                + "Total Cost:    " + n.format(totalCost) + "\n\n";

            // Init and Displaying Alert
            Alert alert = new Alert(AlertType.CONFIRMATION, template, ButtonType.YES, ButtonType.CANCEL);
            alert.setHeaderText("Complete this return?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                // Return Movie
                returnMovie(selectedRental);
            }
        }
    }
    
    // Gets days between dates
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }    
}
