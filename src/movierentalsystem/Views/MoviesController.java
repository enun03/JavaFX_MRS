 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Views;

import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import movierentalsystem.Models.AvailMovie;
import movierentalsystem.Models.Movie;
import movierentalsystem.Models.User;
import movierentalsystem.MySQLConnection;
import movierentalsystem.Views.CustomersController;

/**
 * FXML Controller class
 *
 * @author Eli Núñez
 */
public class MoviesController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    
     @FXML
    private ListView<String> lvAvailableMovies;
     
     @FXML
    private ImageView imgPoster;
     
    @FXML
    private Label lblID;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDirector;

    @FXML
    private Label lblWriters;

    @FXML
    private Label lblReleaseDate;

    @FXML
    private Label lblRuningTime;

    @FXML
    private Label lblRated;

    @FXML
    private Label lblGenre;

    @FXML
    private TextArea taCast;
    
    @FXML
    private Button btnHoldMovie;
    
    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserID;
     
    ObservableList<String> availMovies = FXCollections.observableArrayList();
    
    User userInfo = null;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // Disable Hold Btn
        btnHoldMovie.setDisable(true);
        
        //Gets Customer on TableView Item Selected
        lvAvailableMovies.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            String selectedMovie;
            if (newSelection != null) {
                btnHoldMovie.setDisable(false);
                selectedMovie = lvAvailableMovies.getSelectionModel().getSelectedItem();
                showMovieDetails(selectedMovie);
            }
        });
        
        // On edit button click
        btnHoldMovie.setOnAction((e) -> {    
            // TODO
            String selectedMovie = lblID.getText();
            holdMovie(selectedMovie);
        });
        
        getAvailableMovies();        
    }    
    
    public void getAvailableMovies() {
        // TODO        
        MySQLConnection conn = new MySQLConnection();
        String sql = "SELECT Title FROM `movie` WHERE Status='Available';";
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                availMovies.add(rs.getString("Title"));
            }
            
            rs.close();
            statement.close();
            
            // Populating ListView
            lvAvailableMovies.setItems(availMovies);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showMovieDetails(String title) {
        MySQLConnection conn = new MySQLConnection();
        try {
            PreparedStatement statement = conn.connect().prepareStatement("SELECT * FROM `movie` WHERE Title = ?;");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            Movie movie = null;
            while(rs.next()) {                
                movie = new Movie(
                    Integer.toString(rs.getInt("ID")),
                    rs.getString("Title"),
                    rs.getString("Director"),
                    rs.getString("Writers"),
                    rs.getString("ReleaseDate"),
                    rs.getString("RunningTime"),
                    rs.getString("Rated"),
                    rs.getString("Genre"),
                    rs.getString("Cast"),
                    rs.getString("Status"),
                    rs.getBlob("Image")
                );
            }
            
            rs.close();
            statement.close();
            
            // Populating Movie Details
            lblID.setText(movie.getID());
            lblTitle.setText(movie.getTitle());
            lblDirector.setText(movie.getDirector());
            lblWriters.setText(movie.getWriters());
            lblReleaseDate.setText(movie.getReleaseDate());
            lblRuningTime.setText(movie.getRunningTime());
            lblRated.setText(movie.getRated());
            lblGenre.setText(movie.getGenre());
            taCast.setText(movie.getCast());
            
            // Populate Image
            Blob imageBlob = movie.getImage();
            InputStream binaryStream = imageBlob.getBinaryStream();
            Image poster = new Image(binaryStream);
            Image test = poster;
            System.out.print(test);
            imgPoster.setImage(test);
            
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Holds Movie For Rent
    public void holdMovie(String movie_id) {
        MySQLConnection conn = new MySQLConnection();
        PreparedStatement statement1;
        PreparedStatement statement2;
        try {
            // Update Cusotmer Data in SQL db
            statement1 = conn.connect().prepareStatement("UPDATE `movie` SET Status='Rented' WHERE ID=?");
            statement1.setString(1, movie_id);
            statement1.executeUpdate();
            statement1.close();
            
            // Insert movie to rentals table
            statement2 = conn.connect().prepareStatement("INSERT INTO `rental` (`movie_id`, `customer_id`, `RentedDate`, `CostRate`) VALUES (?, ?, NOW(), 2.00);");
            statement2.setString(1, lblID.getText());
            statement2.setString(2, lblUserID.getText());
            statement2.executeUpdate();
            statement2.close();
            
            // Remove from movie list
            int selectedIndex = lvAvailableMovies.getSelectionModel().getSelectedIndex();
            lvAvailableMovies.getItems().remove(selectedIndex);
           
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void setUserInfo (User user) {
        userInfo = user;
        lblUserName.setText(user.getUserName());
        lblUserID.setText(user.getID());
    }
    
}
