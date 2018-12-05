/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Eli Núñez
 */
public class MainController implements Initializable {

    /**
     * Initializes the controller class.
     */        
    @FXML
    private BorderPane mainPane;

    @FXML
    private void handleShowView(ActionEvent e) {
        String view = (String) ((Node)e.getSource()).getUserData();
        loadFXML(getClass().getResource(view));
    }

    private void loadFXML(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            mainPane.setCenter(loader.load());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }    
    
}
