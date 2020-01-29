package inventorymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


//Claire Bogdan
//WGU C482 (Software I) Performance Assessment


public class InventoryManagementSystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Inventory Management System");
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
