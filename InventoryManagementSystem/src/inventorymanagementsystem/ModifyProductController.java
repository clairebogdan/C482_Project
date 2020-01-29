package inventorymanagementsystem;

import static inventorymanagementsystem.Inventory.lookupPartId;
import static inventorymanagementsystem.Inventory.lookupPartName;
import static inventorymanagementsystem.Inventory.updateProduct;
import static inventorymanagementsystem.MainScreenController.getProductToModify;
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

public class ModifyProductController implements Initializable {

    
    private Product modifyThisProduct;
    //constructor
    public ModifyProductController() {
        this.modifyThisProduct = getProductToModify();
    }
      
    //configure Available Parts table
    @FXML private TableView<Part> availablePartsTableModify;
    @FXML private TableColumn<Part, Integer> availablePartIdColumn;
    @FXML private TableColumn<Part, String> availablePartNameColumn;
    @FXML private TableColumn<Part, Integer> availablePartInventoryColumn;
    @FXML private TableColumn<Part, Double> availablePartPriceColumn;
    
    //configure New Product Parts (Associated Parts) table
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
    
    private ObservableList<Part> tempAssocParts = FXCollections.observableArrayList();
    
    
    //button actions
    @FXML
    private void handleSearchAvailableParts(ActionEvent event) {
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
                availablePartsTableModify.getSelectionModel().select(lookupPartId(searchInt));
            }catch (NumberFormatException e) {
                //this happens when the search string is NOT an integer (to match with Part Name)
                availablePartsTableModify.getSelectionModel().select(lookupPartName(searchString));
                    
            }
        }
    }
    
    
    @FXML
    private void handleAddToAssociatedParts(ActionEvent event) {
        //Adds selected Available Part to the Associated Parts Table
        Part selectedPart = availablePartsTableModify.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                tempAssocParts.add(selectedPart);
                associatedPartsTable.setItems(tempAssocParts);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Nothing selected");
                alert.setHeaderText("Nothing selected");
                alert.setContentText("Nothing was selected to add to the Associated Parts table.");  
                alert.showAndWait();
            }
    }
    
    @FXML
    private void handleDeleteAssociatedPart(ActionEvent event) {
       //Deletes an associated part from the new product
        if (tempAssocParts.size() > 1) {
            Part part = associatedPartsTable.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Removing part from your new product");
                alert.setHeaderText("Please Confirm...");
                alert.setContentText("Would you like to remove this part from your product?");
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
    
    
    @FXML
    private void handleModifyProductSave(ActionEvent event) throws IOException {
        //Saves everything by a) saving any modifications the new product entered in the textfields, and b) associates the parts in the New Product Parts Table with the modified Product  
        //initialize user-entered data
        String productId = productIdField.getText(); //edits are disabled
        String productName = productNameField.getText();
        String productInv = productInvField.getText();
        String productPrice = productPriceField.getText();
        String productMax = productMaxField.getText();
        String productMin = productMinField.getText();
        
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
        Product modProduct = new Product();
        modProduct.setProductId(Integer.parseInt(productId));
        modProduct.setProductName(productName);
        try {
            modProduct.setProductStock(Integer.parseInt(productInv));
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setHeaderText("INPUT ERROR");
            alert.setContentText("Inventory must be entered as an integer (1, 2, 3...)");
            alert.showAndWait();
        } 
        try {
            modProduct.setProductMax(Integer.parseInt(productMax));
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setHeaderText("INPUT ERROR");
            alert.setContentText("Maximum must be entered as an integer (1, 2, 3...)");
            alert.showAndWait();
        }
        try {
            modProduct.setProductMin(Integer.parseInt(productMin));
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setHeaderText("INPUT ERROR");
            alert.setContentText("Minimum must be entered as an integer (1, 2, 3...)");
            alert.showAndWait();
        }
        try {
            modProduct.setProductPrice(Double.parseDouble(productPrice));
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setHeaderText("INPUT ERROR");
            alert.setContentText("Price must be entered as a decimal number (#.##)");
            alert.showAndWait();
        }
        
        
        //associating parts with new product
        for (Part assoc : tempAssocParts) {
            modProduct.addAssociatedPart(assoc);
        }

        
        //Final test to make sure input is valid the modified product            
        try {
            modProduct.isValidProduct();
                if (modProduct.isValidProduct() == true) {
                    updateProduct(modProduct);
                    System.out.println("Selected Product was modified");
                }
            //Go back to main screen automatically
            System.out.println("Saved Modify Product  --> Main Screen");
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

        } catch (ValidationException e) {
            //either there is no name, or the numbers for Inventory, Max, Min, and Price may not be valid
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validation Error for Modify Product");
            alert.setHeaderText("INVALID PRODUCT");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    
    @FXML
    private void handleModifyProductCancel(ActionEvent event) throws IOException {
        //User clicks Cancel and is brought back to the Main Screen
        //confirm Cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modify Product");
        alert.setHeaderText("Return to Main Screen");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Modify Product --> Main Screen");
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            } 
    }
    
    
    
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //disable edits to ID field
        productIdField.setDisable(true);
        
        //populate the "modifyThisProduct" presaved textfields
        productIdField.setText(Integer.toString(modifyThisProduct.getProductId()));
        productNameField.setText(modifyThisProduct.getProductName());
        productInvField.setText(Integer.toString(modifyThisProduct.getProductStock()));
        productPriceField.setText(Double.toString(modifyThisProduct.getProductPrice()));
        productMaxField.setText(Integer.toString(modifyThisProduct.getProductMax()));
        productMinField.setText(Integer.toString(modifyThisProduct.getProductMin()));

        //Available Parts table columns
        availablePartIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        availablePartNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        availablePartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        availablePartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));   
        
        //Associated Parts table columns
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("partId"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        
        //load Available Parts table data
        availablePartsTableModify.setItems(Inventory.allParts);
        
        //make the table load with associated parts, not temp parts, and load
        tempAssocParts = modifyThisProduct.getAssociatedParts();
        associatedPartsTable.setItems(tempAssocParts);    
    }    
}
