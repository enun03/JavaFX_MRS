/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import movierentalsystem.Models.Customer;
import movierentalsystem.Models.Movie;
import movierentalsystem.MySQLConnection;
/**
 * FXML Controller class
 *
 * @author Eli Núñez
 */
public class ManageMoviesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnAddMovie;
    
    @FXML
    private Button btnDelete;

    @FXML
    private TableView<Movie> tblMovies;

    @FXML
    private TableColumn<Movie, String> colID;

    @FXML
    private TableColumn<Movie, String> colTitle;

    @FXML
    private TableColumn<Movie, String> colReleaseDate;

    @FXML
    private TableColumn<Movie, String> colGenre;

    @FXML
    private TableColumn<Movie, String> colRunTime;

    @FXML
    private TableColumn<Movie, String> colRate;

    @FXML
    private Label lblInfo;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtDirector;

    @FXML
    private TextField txtWriters;

    @FXML
    private TextField txtReleaseDate;

    @FXML
    private TextField txtRunningTime;

    @FXML
    private TextField txtGenre;

    @FXML
    private TextField txtRated;

    @FXML
    private TextArea taCast;

    @FXML
    private Button btnUploadImage;
    
    @FXML
    private Label lblUpload;
    
    ObservableList<Movie> oblMovieTableData = FXCollections.observableArrayList();
    
    File uploadedImage = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // TableView CellData Listeners
        colID.setCellValueFactory(cellData -> cellData.getValue().IDProperty());
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colReleaseDate.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
        colGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        colRunTime.setCellValueFactory(cellData -> cellData.getValue().runningTimeProperty());
        colRate.setCellValueFactory(cellData -> cellData.getValue().ratedProperty());
        
        // TableView Init Editable
        tblMovies.setEditable(true);
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colReleaseDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colGenre.setCellFactory(TextFieldTableCell.forTableColumn());
        colRunTime.setCellFactory(TextFieldTableCell.forTableColumn());
        colRate.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //Gets Movie on TableView Item Selected
        tblMovies.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblInfo.setText("Double-click field to update info, press Enter to save changes...");
            }
        });
        
        // On add movie clicked
        btnAddMovie.setOnAction((e) -> {
            try {
                addMovie();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManageMoviesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // On delete movie clicked
        btnDelete.setOnAction((e) -> {
            deleteMovie();
        });
        
        // On upload image clicked
        btnUploadImage.setOnAction((e) -> {
            fileChooser();
        });
        
        getMovies();
    }  
    
    // Gets movies from database
    public void getMovies() {
        // TODO        
        MySQLConnection conn = new MySQLConnection();
        String sql = "SELECT * FROM `movie`;";
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                oblMovieTableData.add(new Movie(
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
                ));
            }
            
            rs.close();
            statement.close();
            
            // Populating ListView
            tblMovies.setItems(oblMovieTableData);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addMovie() throws FileNotFoundException {
        // TODO
        if (txtTitle.getText().isEmpty()) {
            txtTitle.setText("Title is required...");
        } else {
            // Get last movie id
            Movie lastMovie = oblMovieTableData.get(oblMovieTableData.size() - 1);
            int newID = Integer.parseInt(lastMovie.getID()) + 1;
            
            // Get uploaded image blob
            FileInputStream moviePoster = new FileInputStream(uploadedImage);
            
            // Get New movie Data
            Movie newMovie = new Movie(
                Integer.toString(newID),
                txtTitle.getText(),
                txtDirector.getText(),
                txtWriters.getText(),
                txtReleaseDate.getText(),
                txtRunningTime.getText(),
                txtRated.getText(),
                txtGenre.getText(),
                taCast.getText(),
                "Available",
                null
            );
            
            // Insert new customer to database
            MySQLConnection conn = new MySQLConnection();
            PreparedStatement statement1; 
            
            try {
                // Update Cusotmer Data in SQL db
                statement1 = conn.connect().prepareStatement("INSERT INTO `movie` (`Title`, `Director`, `Writers`, `ReleaseDate`, `RunningTime`, `Rated`, `Genre`, `Cast`, `Status`, `Image`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                statement1.setString(1, newMovie.getTitle());
                statement1.setString(2, newMovie.getDirector());
                statement1.setString(3, newMovie.getWriters());
                statement1.setString(4, newMovie.getReleaseDate());
                statement1.setString(5, newMovie.getRunningTime());
                statement1.setString(6, newMovie.getRated());
                statement1.setString(7, newMovie.getGenre());
                statement1.setString(8, newMovie.getCast());
                statement1.setString(9, newMovie.getStatus());
                statement1.setBlob(10, moviePoster);
                
                statement1.executeUpdate();
                statement1.close();
                
                // Add new movie to tableview
                oblMovieTableData.add(newMovie);
                lblInfo.setText("New movie added sucessfully...");
            } catch (SQLException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    // Updates Movie Data
    // Updates Title
    public void onTitleChanged(TableColumn.CellEditEvent<Customer,String> ev) { 
        // TODO  
        Movie movie = tblMovies.getSelectionModel().getSelectedItem();
        String movieID = movie.getID();
        String nv = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `movie` SET Title = ? WHERE ID = ?";
        PreparedStatement statement;
       
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.setString(1, nv);
            statement.setString(2, movieID);
            statement.executeUpdate();
            statement.close();
            
            // Update in TableView
            movie.setTitle(nv);
            lblInfo.setText(movie.getTitle() + "'s Title updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Updates ReleaseDate
    public void onReleaseDateChanged(TableColumn.CellEditEvent<Customer,String> ev) { 
        // TODO  
        Movie movie = tblMovies.getSelectionModel().getSelectedItem();
        String movieID = movie.getID();
        String nv = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `movie` SET ReleaseDate = ? WHERE ID = ?";
        PreparedStatement statement;
       
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.setString(1, nv);
            statement.setString(2, movieID);
            statement.executeUpdate();
            statement.close();
            
            // Update in TableView
            movie.setReleaseDate(nv);
            lblInfo.setText(movie.getTitle() + "'s Release Date updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Updates Genre
    public void onGenreChanged(TableColumn.CellEditEvent<Customer,String> ev) { 
        // TODO  
        Movie movie = tblMovies.getSelectionModel().getSelectedItem();
        String movieID = movie.getID();
        String nv = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `movie` SET Genre = ? WHERE ID = ?";
        PreparedStatement statement;
       
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.setString(1, nv);
            statement.setString(2, movieID);
            statement.executeUpdate();
            statement.close();
            
            // Update in TableView
            movie.setGenre(nv);
            lblInfo.setText(movie.getTitle() + "'s Genre updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Updates Running Time
    public void onRunningTimeChanged(TableColumn.CellEditEvent<Customer,String> ev) { 
        // TODO  
        Movie movie = tblMovies.getSelectionModel().getSelectedItem();
        String movieID = movie.getID();
        String nv = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `movie` SET RunningTime = ? WHERE ID = ?";
        PreparedStatement statement;
       
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.setString(1, nv);
            statement.setString(2, movieID);
            statement.executeUpdate();
            statement.close();
            
            // Update in TableView
            movie.setRunningTime(nv);
            lblInfo.setText(movie.getTitle() + "'s Running Time updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Updates Rated
    public void onRatedChanged(TableColumn.CellEditEvent<Customer,String> ev) { 
        // TODO  
        Movie movie = tblMovies.getSelectionModel().getSelectedItem();
        String movieID = movie.getID();
        String nv = ev.getNewValue();
        
        MySQLConnection conn = new MySQLConnection();
        String sql = "UPDATE `movie` SET Rated = ? WHERE ID = ?";
        PreparedStatement statement;
       
        try {
            // Update Cusotmer Data in SQL db
            statement = conn.connect().prepareStatement(sql);
            statement.setString(1, nv);
            statement.setString(2, movieID);
            statement.executeUpdate();
            statement.close();
            
            // Update in TableView
            movie.setRated(nv);
            lblInfo.setText(movie.getTitle() + "'s Rated updated...");
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Deletes Movie
    public void deleteMovie() {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        
        if (selectedMovie != null) {
            // TODO     
            MySQLConnection conn = new MySQLConnection();
            String sql1= "DELETE FROM `movie` WHERE ID='" + selectedMovie.getID() +"';";                
            // Remove Cusotmer Data
            PreparedStatement statement1;

            try {
                // Removing user from db
                statement1 = conn.connect().prepareStatement(sql1);
                statement1.executeUpdate(sql1);
                statement1.close();

                // Removing customer from TableView
                int selectedIndex = tblMovies.getSelectionModel().getSelectedIndex();
                tblMovies.getItems().remove(selectedIndex);
            } catch (SQLException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }       
    }
    
    // File chooser method
    public void fileChooser() {
        // TODO
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Images", "*.jpeg", "*.jpg", "*.png"));
        uploadedImage = fc.showOpenDialog(null);
        
        if (uploadedImage != null) {
            System.out.print(uploadedImage.getAbsolutePath());
            lblUpload.setText(uploadedImage.getName());
        }
    }

}
