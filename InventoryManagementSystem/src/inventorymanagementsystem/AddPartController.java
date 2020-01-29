package inventorymanagementsystem;

import static inventorymanagementsystem.Inventory.addPart;
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

public class AddPartController implements Initializable {
    
    //initialize radio buttons, changing label, and toggle group
    @FXML private RadioButton radioInHouseBtn;
    @FXML private RadioButton radioOutsourcedBtn;
    @FXML private Label radioLabel;
    private ToggleGroup inHouseOrOutsourced;
    private boolean isOutsourced;
    
    //initialize textfields
    @FXML private TextField partIdField; //autogenerated
    @FXML private TextField partNameField;
    @FXML private TextField partInvField;
    @FXML private TextField partPriceField;
    @FXML private TextField partMaxField;
    @FXML private TextField partMinField;
    @FXML private TextField partMachOrCompField;

    
    
    //button actions   
    @FXML
    private void radioInHouse(ActionEvent event) throws IOException {
        //When radio button In-House is selected, Machine ID is displayed
        isOutsourced = false;
        System.out.println("In House selected");
        radioLabel.setText("Machine ID");
        partMachOrCompField.setPromptText("Mach ID");
    }
    
    @FXML
    private void radioOutsourced(ActionEvent event) throws IOException {
        //When radio button Outsourced is selected, Company Name is displayed
        isOutsourced = true;
        System.out.println("Outsourced selected");
        radioLabel.setText("Company Name");
        partMachOrCompField.setPromptText("Comp Nm");
    }
    
    
///////////////////////SAVE BUTTON CODE BLOCK START/////////////////////////////    
    @FXML
    private void handleAddPartSave(ActionEvent event) throws IOException {
        //User clicks Save and the entered data is saved and added to the Part table.
            
        //initialize user-entered data
        String partName = partNameField.getText();
        String partInv = partInvField.getText();
        String partPrice = partPriceField.getText();
        String partMax = partMaxField.getText();
        String partMin = partMinField.getText();
        String partMachOrComp = partMachOrCompField.getText();
        
        //Create the autogenerated partId for the added part
        int newPartId = 1;
        for (Part a : Inventory.getAllParts()) {
            if (a.getPartId() >= newPartId) {
                newPartId = a.getPartId() + 1;
            }
        }
        
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
            Outsourced newPartOutsourced = new Outsourced();
            newPartOutsourced.setPartId(newPartId);
            newPartOutsourced.setPartName(partName);
            newPartOutsourced.setCompanyName(partMachOrComp);
            try {
                newPartOutsourced.setPartStock(Integer.parseInt(partInv));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Inventory must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            } 
            try {
                newPartOutsourced.setPartMax(Integer.parseInt(partMax));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Maximum must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                newPartOutsourced.setPartMin(Integer.parseInt(partMin));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Minimum must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                newPartOutsourced.setPartPrice(Double.parseDouble(partPrice));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Price must be entered as a decimal number (#.##)");
                alert.showAndWait();
            }
            
                
          
            //Final test to make sure input is valid for Outsourced            
            try {
                newPartOutsourced.isValid();
                    if (newPartOutsourced.isValid() == true) {
                        addPart(newPartOutsourced);
                        System.out.println("A new outsourced Part was added");
                    }
                //Go back to main screen automatically
                System.out.println("Saved Add Part Outsourced --> Main Screen");
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
        
//////////////////IN-HOUSE RADIO BUTTON SELECTED FOR SAVING/////////////////////
        
        else { //isOutsourced is false, therefore it is IN-HOUSE
            //setting attributes to new object. Using try catch to force correct format
            InHouse newPartInHouse = new InHouse();
            newPartInHouse.setPartId(newPartId);
            newPartInHouse.setPartName(partName);
            try {
                newPartInHouse.setPartStock(Integer.parseInt(partInv));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Inventory must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                newPartInHouse.setPartMax(Integer.parseInt(partMax));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Max must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                newPartInHouse.setPartMin(Integer.parseInt(partMin));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Min must be entered as an integer (1, 2, 3...)");
                alert.showAndWait();
            }
            try {
                newPartInHouse.setPartPrice(Double.parseDouble(partPrice));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Price must be entered as a decimal number (#.##)");
                alert.showAndWait();
            }
            try {
                newPartInHouse.setMachineId(Integer.parseInt(partMachOrComp));
            }catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INPUT ERROR");
                alert.setHeaderText("INPUT ERROR");
                alert.setContentText("Machine ID must be entered as an (1, 2, 3...)");
                alert.showAndWait();
            }
            
            
            
            //Final test to make sure input is valid for In-House
            try {
                newPartInHouse.isValid();
                    if (newPartInHouse.isValid() == true) {
                        addPart(newPartInHouse);
                        System.out.println("A new in-house Part was added");
                    }
                //Go back to main screen automatically
                System.out.println("Saved Add Part In House --> Main Screen");
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
    private void handleAddPartCancel(ActionEvent event) throws IOException {
        //User clicks Cancel and is brought back to the Main Screen
        //confirm Cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Add Part");
        alert.setHeaderText("Return to Main Screen");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Add Part --> Main Screen");
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            }      
    }
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //only 1 radio button can be selected at a time, default selection is Outsourced
        inHouseOrOutsourced = new ToggleGroup();
        this.radioInHouseBtn.setToggleGroup(inHouseOrOutsourced);
        this.radioOutsourcedBtn.setToggleGroup(inHouseOrOutsourced);
        this.radioOutsourcedBtn.setSelected(true);
        isOutsourced = true;
        
        //sets ID TextField to "Auto Gen - Disabled" and disables its function
        partIdField.setText("Auto Gen - Disabled");
        partIdField.setDisable(true);     
    }        
}