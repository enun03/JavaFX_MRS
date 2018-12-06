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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import movierentalsystem.Models.User;
import movierentalsystem.MySQLConnection;

/**
 * FXML Controller class
 *
 * @author Eli Núñez
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private AnchorPane aLogin;
     
    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;
    
    @FXML
    private Label lblInfo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        aLogin.requestFocus();
        
        // On Login button click
        btnLogin.setOnAction((e) -> {    
            try {
                // TODO
                login();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // On Register button click
        btnRegister.setOnAction((e) -> {    
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("Register.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Register");
                stage.resizableProperty().setValue(Boolean.FALSE);
                stage.show();

                Stage loginWindow = (Stage) btnRegister.getScene().getWindow();
                loginWindow.close();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }   
    
    // Logs in user
    public void login() throws IOException {
        // TODO        
        MySQLConnection conn = new MySQLConnection();
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        User user = null;
        
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password is required...");
            lblInfo.setText("* Username and password is required...");
        } else {
            try {
                PreparedStatement statement = conn.connect().prepareStatement("SELECT * FROM `users` WHERE UserName=? AND Password=?");
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    user = new User(
                        Integer.toString(rs.getInt("ID")),
                        rs.getString("Cust_ID"),
                        rs.getString("Role_ID"),
                        rs.getString("UserName"),
                        "*********",
                        rs.getString("RegisteredDate")            
                    );
                }

                rs.close();
                statement.close();

                if (user != null) {
                    // Redirect User
                    redirectUser(user);
                } else {
                    System.out.println("Username or password is incorrect...");
                    lblInfo.setText("* Username or password is incorrect...");
                }

            } catch (SQLException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    // Returns user info
    public User getUserInfo(String username) {
        // TODO        
        MySQLConnection conn = new MySQLConnection();
        String sql = "SELECT * FROM `users`";
        User user = null;
        try {
            PreparedStatement statement = conn.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                user = new User(
                    Integer.toString(rs.getInt("ID")),
                    rs.getString("Cust_ID"),
                    rs.getString("Role_ID"),
                    rs.getString("UserName"),
                    "*********",
                    rs.getString("RegisteredDate")            
                );
            }
            
            rs.close();
            statement.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return user;
    }

    public void redirectUser(User user) throws IOException {
        String role = checkUserRole(user);
        
        if (role.equals("Admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Movie Rental System Admin");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
            
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Movies.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            MoviesController moviesCntrl = loader.getController();
            moviesCntrl.setUserInfo(user);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Available Movies");
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
        }
        
        Stage loginWindow = (Stage) btnLogin.getScene().getWindow();
        loginWindow.close();
    }

    public String checkUserRole(User user) {
        String roleID = user.getRoleID();
        String role;
        
        if (roleID.equals("1")) {
            role = "Admin";
        } else {
            role = "User";
        }
        
        return role;
    }
    
}
