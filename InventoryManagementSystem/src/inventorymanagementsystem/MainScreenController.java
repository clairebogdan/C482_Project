package inventorymanagementsystem;

import static inventorymanagementsystem.Inventory.allParts;
import static inventorymanagementsystem.Inventory.allProducts;
import static inventorymanagementsystem.Inventory.deletePart;
import static inventorymanagementsystem.Inventory.deleteProduct;
import static inventorymanagementsystem.Inventory.lookupPartId;
import static inventorymanagementsystem.Inventory.lookupPartName;
import static inventorymanagementsystem.Inventory.lookupProductId;
import static inventorymanagementsystem.Inventory.lookupProductName;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainScreenController implements Initializable {
    
    //constructor
    public MainScreenController(){}
    
    //configure Part table
    @FXML private TableView<Part> tableViewPart;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    
    //configure Product table
    @FXML private TableView<Product> tableViewProduct;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    
    //Search TextFields
    @FXML private TextField searchPartTextField;
    @FXML private TextField searchProductTextField;
    
    //prevent Main Screen tables from overpopulating
    static boolean initialData;
    
    //used to assist Modify Product data
    private static Product modifyThisProduct;

    
    
/////////////////////////PART SIDE OF MAIN SCREEN///////////////////////////////
    
    //"Search" Button pressed to search for a Part
    @FXML
    private void handleSearchPart(ActionEvent event) {
        String searchString = searchPartTextField.getText();
        
        if (searchString.isEmpty()) {
            //nothing was typed in to search
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Nothing searched");
            alert.setHeaderText("Nothing searched");
            alert.setContentText("Search text box is empty. Please type the Part ID or Part Name you are looking for, then press Search.");  
            alert.showAndWait();
        }
        else { //something was typed in to search
            try {
                //try to convert the search string to an integer (to match with Part ID)
                int searchInt = Integer.parseInt(searchString);
                tableViewPart.getSelectionModel().select(lookupPartId(searchInt));
                   
            }catch (NumberFormatException e) {
                //this happens when the search string is NOT an integer (to match with Part Name)
                tableViewPart.getSelectionModel().select(lookupPartName(searchString));
            }
        }
    }
   
    
    //"Add" Button to go to Add Part screen
    @FXML
    private void handleAddPart(ActionEvent event) throws IOException {
        System.out.println("Main Screen --> Add Part");
        Parent mainScreenParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }
    
    //"Modify" button to go to Modify Part screen
    @FXML
    private void handleModifyPart(ActionEvent event) throws IOException {
        if (tableViewPart.getSelectionModel().isEmpty()) {
            //nothing was selected to modify
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Nothing selected");
            alert.setHeaderText("Nothing selected");
            alert.setContentText("Nothing was selected to modify");  
            alert.showAndWait();
        }
        else {
            System.out.println("Main Screen --> Modify Part");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
            Parent mainScreenParent;
            mainScreenParent = loader.load();
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

            //defines the selected row to be modified
            ModifyPartController controller = loader.getController();
            Part selectedPart = tableViewPart.getSelectionModel().getSelectedItem();
            controller.setPart(selectedPart);
        }
    }
    
    //"Delete" Button pressed to delete a Part
    @FXML
    private void handleDeletePart(ActionEvent event) {
        if (tableViewPart.getSelectionModel().isEmpty()) {
            //nothing was selected to modify
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Nothing selected");
            alert.setHeaderText("Nothing selected");
            alert.setContentText("Nothing was selected to delete");  
            alert.showAndWait();
        }
        else {
            if (allParts.size() > 1) {
                //confirm deletion
                Part partToDelete = tableViewPart.getSelectionModel().getSelectedItem();      
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Delete Part");
                alert.setHeaderText("Deleting Part");
                alert.setContentText("Are you sure you want to delete " + partToDelete.getPartName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        deletePart(partToDelete);
                    }           
            }
            else {
                //cannot delete the last part
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("CANNOT DELETE");
                alert.setHeaderText("CANNOT DELETE");
                alert.setContentText("This is the last item! Part table cannot be empty.");
                alert.showAndWait();
            }
        }
    }
    
    
//////////////////////PRODUCT SIDE OF MAIN SCREEN///////////////////////////////
    
    //"Search" Button pressed to search for a Product
    @FXML
    private void handleSearchProduct(ActionEvent event) {
       String searchString = searchProductTextField.getText();
        
        if (searchString.isEmpty()) {
            //nothing was typed in to search
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Nothing searched");
            alert.setHeaderText("Nothing searched");
            alert.setContentText("Search text box is empty. Please type the Product ID or Product Name you are looking for, then press Search.");  
            alert.showAndWait();
        }
        else { //something was typed in to search
            try {
                //try to convert the search string to an integer (to match with Product ID)
                int searchInt = Integer.parseInt(searchString);
                tableViewProduct.getSelectionModel().select(lookupProductId(searchInt));                   
            }catch (NumberFormatException e) {
                //this happens when the search string is NOT an integer (to match with Product Name)
                tableViewProduct.getSelectionModel().select(lookupProductName(searchString));
            }
        }
    } 
    
    
    //"Add" Button pressed to go to the Add Product screen
    @FXML
    private void handleAddProduct(ActionEvent event) throws IOException {
        System.out.println("Main Screen --> Add Product");
        Parent mainScreenParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }
    
    //"Modify" Button pressed to go to the Modify Product screen
    @FXML
    private void handleModifyProduct(ActionEvent event) throws IOException{
        if (tableViewProduct.getSelectionModel().isEmpty()) {
            //nothing was selected to modify
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Nothing selected");
            alert.setHeaderText("Nothing selected");
            alert.setContentText("Nothing was selected to modify");  
            alert.showAndWait();
        }
        else {
            modifyThisProduct = tableViewProduct.getSelectionModel().getSelectedItem();
            setProductToModify(modifyThisProduct);
            //go to Modify Product Screen
            System.out.println("Main Screen --> Modify Product");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
            Parent mainScreenParent;
            mainScreenParent = loader.load();
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }
    
    
    //"Delete" Button pressed to delete a Product
    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        if (tableViewProduct.getSelectionModel().isEmpty()) {
            //nothing was selected to modify
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Nothing selected");
            alert.setHeaderText("Nothing selected");
            alert.setContentText("Nothing was selected to delete");  
            alert.showAndWait();
        }
        else {
            if (allProducts.size() > 1) {
                //confirm deletion
                Product productToDelete = tableViewProduct.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Delete Product");
                alert.setHeaderText("Deleting Product");
                alert.setContentText("Are you sure you want to delete " + productToDelete.getProductName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        deleteProduct(productToDelete);
                    } 
            }
            else {
                //cannot delete the last product
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("CANNOT DELETE");
                alert.setHeaderText("CANNOT DELETE");
                alert.setContentText("This is the last item! Product table cannot be empty.");
                alert.showAndWait();
            }
        }
    }
    
    
///////////////////////////////////EXIT BUTTON//////////////////////////////////
    //exit entire program
    @FXML
    private void handleExit(ActionEvent event) {
        //confirm exit
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit Program");
        alert.setHeaderText("Exiting...");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
    }
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Parts table columns
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        
        //Products table columns
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        
        //load both table data
        tableViewPart.setItems(Inventory.allParts);
        tableViewProduct.setItems(Inventory.allProducts);
        
        //initial data in the tables
        if (!initialData) {
            //Part table
            Inventory.addPart(new InHouse(1, "Beeswax", 3, 5, 1, 5.00, 1));
            Inventory.addPart(new InHouse(2, "Thread", 8, 25, 2, 7.00, 2));
            Inventory.addPart(new InHouse(3, "Work Lamp", 5, 10, 5, 20.00, 3));
            Inventory.addPart(new Outsourced(4, "Oboe Cane", 50, 100, 5, 3.20, "Hodge Products Inc."));
            Inventory.addPart(new Outsourced(5, "English Horn Cane", 50, 100, 5, 4.00, "Hodge Products Inc."));
            Inventory.addPart(new Outsourced(6, "Oboe D'Amore Cane", 50, 100, 5, 4.10, "Hodge Products Inc."));
            Inventory.addPart(new Outsourced(7, "Oboe D'Amore Staples", 50, 100, 5, 5.00, "Double or Nothing Reeds"));
            Inventory.addPart(new Outsourced(8, "Oboe Staples", 50, 100, 5, 3.35, "Double or Nothing Reeds"));
            Inventory.addPart(new Outsourced(9, "English Horn Staples", 50, 100, 5, 4.15, "Double or Nothing Reeds"));
            Inventory.addPart(new Outsourced(10, "Bassoon Cane", 2, 5, 1, 2.00, "Hodge Products Inc."));
            Inventory.addPart(new Outsourced(11, "Bassoon Staples", 3, 6, 3, 45.00, "Midwest Musical Imports"));
            Inventory.addPart(new Outsourced(12, "Reed Soaking Cup", 6, 10, 6, 5.00, "Forrest Music"));

            //Product table
            Inventory.addProduct(new Product(1, "Oboe Reed", 20, 50, 5, 25.00));
            Inventory.addProduct(new Product(2, "English Horn Reed", 10, 25, 3, 35.00));
            Inventory.addProduct(new Product(3, "Oboe D'Amore Reed", 5, 10, 2, 40.00));

            initialData = true;
        }     
    }
    
    //functions that assist with Modify Product
    public static Product getProductToModify() {
        return modifyThisProduct;
    }
    public void setProductToModify(Product modProd) {
        MainScreenController.modifyThisProduct = modProd;
    }
}
