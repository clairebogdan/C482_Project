package inventorymanagementsystem;

import static inventorymanagementsystem.Inventory.lookupPartId;
import static inventorymanagementsystem.Inventory.lookupPartName;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.ValidationException;

public class AddProductController implements Initializable {
    
    //constructor
    public AddProductController() {}
    
    //configure Available Parts table
    @FXML private TableView<Part> availablePartsTableAdd;
    @FXML private TableColumn<Part, Integer> availablePartIdColumn;
    @FXML private TableColumn<Part, String> availablePartNameColumn;
    @FXML private TableColumn<Part, Integer> availablePartInventoryColumn;
    @FXML private TableColumn<Part, Double> availablePartPriceColumn;
    
    //configure New Product Parts table
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, Integer> associatedPartIdColumn;
    @FXML private TableColumn<Part, String> associatedPartNameColumn;
    @FXML private TableColumn<Part, Integer> associatedPartInventoryColumn;
    @FXML private TableColumn<Part, Double> associatedPartPriceColumn;
    
    //configure TextFields
    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private TextField productInvField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMaxField;
    @FXML private TextField productMinField;
    @FXML private TextField searchAvailablePartsField;
    
    Product newProduct = new Product();
    private ObservableList<Part> tempAssocParts = FXCollections.observableArrayList();
    
    
    
    //button actions
    @FXML
    private void handleAvailablePartsSearch(ActionEvent event) {
        //searches the Available Parts Table
        String searchString = searchAvailablePartsField.getText();
        
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
                availablePartsTableAdd.getSelectionModel().select(lookupPartId(searchInt));     
            }catch (NumberFormatException e) {
                //this happens when the search string is NOT an integer (to match with Part Name)
                availablePartsTableAdd.getSelectionModel().select(lookupPartName(searchString));
            }
        }
    }
    
    
    @FXML
    private void handleAddToAssociatedParts(ActionEvent event) throws IOException {
        //Adds selected Available Part to the New Product Parts Table (Associated Parts)
        
        Part selectedPart = availablePartsTableAdd.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                tempAssocParts.add(selectedPart);
                associatedPartsTable.setItems(tempAssocParts);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("Nothing selected");
                alert.setHeaderText("Nothing selected");
                alert.setContentText("Nothing was selected to add to the New Product Parts (Associated Parts) Table");
                alert.showAndWait();
            }    
    }
    
    @FXML
    private void handleDeleteAssociatedPart(ActionEvent event) {
        //Deletes an associated part from the new product
        Part part = associatedPartsTable.getSelectionModel().getSelectedItem();
        if (part != null) {
            if (tempAssocParts.size() > 1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Removing part from your new product");
                alert.setHeaderText("Please Confirm...");
                alert.setContentText("Would you like to remove this part from your new product?");
                Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                       tempAssocParts.remove(part);
                    }                
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("CANNOT REMOVE");
                alert.setHeaderText("CANNOT REMOVE");
                alert.setContentText("You must have at least 1 part associated with your product.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("NOTHING SELECTED");
            alert.setHeaderText("NOTHING SELECTED");
            alert.setContentText("Nothing was selected to delete.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleNewProductSave(ActionEvent event) throws IOException {
        //Saves everything by a) adding the new product entered in the textfields, and b) associates the parts in the New Product Parts Table with the newly created Product
        if (tempAssocParts.size() >= 1) {       
            //initialize user-entered data
            String productName = productNameField.getText();
            String productInv = productInvField.getText();
            String productPrice = productPriceField.getText();
            String productMax = productMaxField.getText();
            String productMin = productMinField.getText();


            //Create the autogenerated partId for the added part
            int newProductId = 1;
            for (Product a : Inventory.getAllProducts()) {
                if (a.getProductId() >= newProductId) {
                    newProductId = a.getProductId() + 1;
                }
            }

            //change any "left-blank" entries to String 0
            if ("".equals(productInv)) {
                productInv = "0";
            }
            if ("".equals(productMax)) {
                productMax = "0";
            }
            if ("".equals(productMin)) {
                productMin = "0";
            }
            if ("".equals(productPrice)) {
                productPrice = "0";
            }

            //setting attributes to new object. Using try catch to force correct format
            newProduct.setProductId(newProductId);
            newProduct.setProductName(productName);
            try {
                newProduct.setProductStock(Integer.parseInt(productInv));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Inventory must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            } 
            try {
                newProduct.setProductMax(Integer.parseInt(productMax));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Maximum must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                newProduct.setProductMin(Integer.parseInt(productMin));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Minimum must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                newProduct.setProductPrice(Double.parseDouble(productPrice));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Price must be entered as a decimal number (#.##)");
                alert.showAndWait();
            }

            //assign the parts to the new product
            for (Part assoc : tempAssocParts) {
                newProduct.addAssociatedPart(assoc);
            }


            //Final test to make sure input is valid the new product            
            try {
                newProduct.isValidProduct();
                    if (newProduct.isValidProduct() == true) {
                        Inventory.addProduct(newProduct);
                        System.out.println("A new Product was added");
                    }
                //Go back to main screen automatically
                System.out.println("Saved Add Product  --> Main Screen");
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();

            } catch (ValidationException e) {
                //either there is no name, or the numbers for Inventory, Max, Min, and Price may not be valid
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation Error for Add Product");
                alert.setHeaderText("INVALID PRODUCT");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } 
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Must have a part");
            alert.setHeaderText("Must have at least 1 part");
            alert.setContentText("You must add at least 1 part to your new product.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleAddProductCancel(ActionEvent event) throws IOException {
        //User clicks Cancel and is brought back to the Main Screen
        //confirm Cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Add Product");
        alert.setHeaderText("Return to Main Screen");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Add Product --> Main Screen");
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            } 
    }
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Available Parts table columns
        availablePartIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        availablePartNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        availablePartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        availablePartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        
        
        //New Product Parts (Associated Parts) table columns
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        
        //load both table data
        availablePartsTableAdd.setItems(Inventory.allParts);
        associatedPartsTable.setItems(tempAssocParts);
        
        //make ID field disabled and updated on its own
        productIdField.setText("Auto Gen - Disabled");
        productIdField.setDisable(true);       
    }      
}
