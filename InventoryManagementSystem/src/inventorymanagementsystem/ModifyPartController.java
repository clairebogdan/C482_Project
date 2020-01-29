package inventorymanagementsystem;

import static inventorymanagementsystem.Inventory.updatePart;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.bind.ValidationException;

public class ModifyPartController implements Initializable {
    
    //radio buttons and changing label
    @FXML private RadioButton radioInHouseBtnModify;
    @FXML private RadioButton radioOutsourcedBtnModify;
    @FXML private Label radioLabelModify;
    private ToggleGroup inHouseOrOutsourcedModify;
    boolean isOutsourced;
    
    //textfields
    @FXML private TextField partIdField;
    @FXML private TextField partNameField;
    @FXML private TextField partInvField;
    @FXML private TextField partPriceField;
    @FXML private TextField partMaxField;
    @FXML private TextField partMinField;
    @FXML private TextField partMachOrCompField;
    
    @FXML
    private void radioInHouseModify(ActionEvent event) throws IOException {
        System.out.println("In House selected");
        radioLabelModify.setText("Machine ID");
        isOutsourced = false;
    }
    
    @FXML
    private void radioOutsourcedModify(ActionEvent event) throws IOException {
        System.out.println("Outsourced selected");
        radioLabelModify.setText("Company Name");
        isOutsourced = true;
    }
    @FXML
    private void handleModifyPartSave(ActionEvent event) throws IOException{
        //User clicks Save and the entered data is saved and added to the Part table.
        String partId = partIdField.getText();
        String partName = partNameField.getText();
        String partInv = partInvField.getText();
        String partPrice = partPriceField.getText();
        String partMax = partMaxField.getText();
        String partMin = partMinField.getText();
        String partMachOrComp = partMachOrCompField.getText();
        
        //change any "left-blank" entries to String 0
        if ("".equals(partInv)) {
            partInv = "0";
        }
        if ("".equals(partMax)) {
            partMax = "0";
        }
        if ("".equals(partMin)) {
            partMin = "0";
        }
        if ("".equals(partPrice)) {
            partPrice = "0";
        }
        if ("".equals(partMachOrComp)) {
            partMachOrComp = "0";
        }

/////////////////OUTSOURCED RADIO BUTTON SELECTED FOR SAVING////////////////////
        if (isOutsourced == true) {
            //setting attributes to new object. Using try catch to force correct format
            Outsourced modPartOutsourced = new Outsourced();
            modPartOutsourced.setPartId(Integer.parseInt(partId)); //stays the same since edits are disabled
            modPartOutsourced.setPartName(partName);
            modPartOutsourced.setCompanyName(partMachOrComp);
            try {
                modPartOutsourced.setPartStock(Integer.parseInt(partInv));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Inventory must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            } 
            try {
                modPartOutsourced.setPartMax(Integer.parseInt(partMax));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Maximum must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                modPartOutsourced.setPartMin(Integer.parseInt(partMin));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Minimum must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                modPartOutsourced.setPartPrice(Double.parseDouble(partPrice));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Price must be entered as a decimal number (#.##)");
                alert.showAndWait();
            }
            
                
          
            //Final test to make sure input is valid for Outsourced            
            try {
                modPartOutsourced.isValid();
                    if (modPartOutsourced.isValid() == true) {
                        updatePart(modPartOutsourced);
                        System.out.println("The selected part was modified");
                    }
                //Go back to main screen automatically
                System.out.println("Saved Modify Part Outsourced --> Main Screen");
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
                
            } catch (ValidationException e) {
                //either there is no name, or the numbers for Inventory, Max, Min, and Price may not be valid
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation Error for Modify Part");
                alert.setHeaderText("INVALID PART");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
//////////////////IN-HOUSE RADIO BUTTON SELECTED FOR SAVING/////////////////////
        
        else { //isOutsourced is false, therefore it is IN-HOUSE
            //setting attributes to new object. Using try catch to force correct format
            InHouse modPartInHouse = new InHouse();
            modPartInHouse.setPartId(Integer.parseInt(partId));
            modPartInHouse.setPartName(partName);
            try {
                modPartInHouse.setPartStock(Integer.parseInt(partInv));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Inventory must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                modPartInHouse.setPartMax(Integer.parseInt(partMax));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Max must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                modPartInHouse.setPartMin(Integer.parseInt(partMin));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Min must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                modPartInHouse.setPartPrice(Double.parseDouble(partPrice));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Price must be entered as a decimal number (#.##)");
                alert.showAndWait();
            }
            try {
                modPartInHouse.setMachineId(Integer.parseInt(partMachOrComp));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Machine ID must be entered as an (1, 2, 3...)");
                alert.showAndWait();
            }
            
            
            
            //Final test to make sure input is valid for In-House
            try {
                modPartInHouse.isValid();
                    if (modPartInHouse.isValid() == true) {
                        updatePart(modPartInHouse);
                        System.out.println("The selected part was modified");
                    }
                //Go back to main screen automatically
                System.out.println("Saved Modify Part In House --> Main Screen");
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
                
            } catch (ValidationException e) {
                //either there is no name, or the numbers for Inventory, Max, Min, and Price may not be valid
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation Error for Add Part");
                alert.setHeaderText("INVALID PART");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }   
        }     
    }
/////////////////////////END OF SAVE BUTTON/////////////////////////////////////
        
    
    @FXML
    private void handleModifyPartCancel(ActionEvent event) throws IOException{
        //User clicks Cancel and is brought back to the Main Screen
        //confirm Cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modify Part");
        alert.setHeaderText("Return to Main Screen");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Modify Part --> Main Screen");
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            } 
    }
    
    
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inHouseOrOutsourcedModify = new ToggleGroup();
        this.radioInHouseBtnModify.setToggleGroup(inHouseOrOutsourcedModify);
        this.radioOutsourcedBtnModify.setToggleGroup(inHouseOrOutsourcedModify);
        this.radioOutsourcedBtnModify.setSelected(true);
        
        //makes ID Field not able to be changed
        partIdField.setDisable(true);
    }    
    
    //used in Main Controller to load selected row to Modify Part screen 
    Part part;
    public void setPart(Part part) {
        this.part = part;
        partIdField.setText(Integer.toString(part.getPartId()));
        partNameField.setText(part.getPartName());
        partInvField.setText(Integer.toString(part.getPartStock()));
        partPriceField.setText(Double.toString(part.getPartPrice()));
        partMaxField.setText(Integer.toString(part.getPartMax()));
        partMinField.setText(Integer.toString(part.getPartMin()));
        if (part instanceof InHouse) {
            InHouse inHousePart = (InHouse)part;
            partMachOrCompField.setText(Integer.toString(inHousePart.getMachineId()));
            radioLabelModify.setText("Machine ID");
            radioInHouseBtnModify.setSelected(true);
            isOutsourced = false;
        }
        else {
            Outsourced outsourcedPart = (Outsourced)part;
            partMachOrCompField.setText(outsourcedPart.getCompanyName());
            radioLabelModify.setText("Company Name");
            radioOutsourcedBtnModify.setSelected(true);
            isOutsourced = true;
        }
    }
}
