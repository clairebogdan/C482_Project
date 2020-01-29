package inventorymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.ValidationException;

 
public class Product {
    
    //default constructor
    public Product() {}
    
    //variables
    int productId;
    private String productName;
    private int productStock;
    private int productMax;
    private int productMin;
    private double productPrice;
    
    //detailed constructor
    public Product(int productId, String productName, int productStock, int productMax, int productMin, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productStock = productStock;
        this.productMax = productMax;
        this.productMin = productMin;
        this.productPrice = productPrice;
    }
    
    //getters and setters for Product
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public int getProductMax() {
        return productMax;
    }

    public void setProductMax(int productMax) {
        this.productMax = productMax;
    }

    public int getProductMin() {
        return productMin;
    }

    public void setProductMin(int productMin) {
        this.productMin = productMin;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
    public void setProductMaxPrice(int productMax) {
        this.productMax = productMax;
    }
    
    
    
    
    
    
    
    //Associated Parts
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    
    public ObservableList<Part> getAssociatedParts() {
        return associatedPartsList;
    }
    
    public void setAssociatedParts(ObservableList<Part> associatedPartsList) {
        this.associatedPartsList = associatedPartsList;
    }
    
    public void addAssociatedPart(Part newPart) {
        this.associatedPartsList.add(newPart);
    }
    
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedPartsList.remove(selectedAssociatedPart);
        return true;
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedPartsList;
    }
    
    
    
    
    
    
    //check for validity of entries
    public boolean isValidProduct() throws ValidationException {
        
        if (getProductName().equals("")) {
            throw new ValidationException("Name field is required. Please enter a name.");
        }
        
        if (getProductStock() <= 0) {
            throw new ValidationException("Inventory must be greater than 0. Please enter a valid amount.");
        }
        
        if (getProductPrice() <= 0.00) {
            throw new ValidationException("Price must be above $0.00. Please enter a valid amount.");
        }
        
        if (getProductMin() <= 0) {
            throw new ValidationException("Minimum inventory must be greater than 0. Please enter a valid amount.");
        }
        
        if (getProductMax() <= 0) {
            throw new ValidationException("Maximum inventory must be greater than 0. Please enter a valid amount. ");
        }
        
        if (getProductMax() == getProductMin()) {
            throw new ValidationException("Maximum and minimum inventory numbers cannot be the same. Please enter a valid amount.");
        }
        
        if (getProductMax() < getProductMin()) {
            throw new ValidationException("Maximum inventory cannot be less than minimum inventory. Please enter a valid amount.");
        }
        
        if (getProductMin() > getProductMax()) {
            throw new ValidationException("Minimum inventory cannot be more than maximum inventory. Please enter a valid amount.");
        }
        
        if ((getProductStock() < getProductMin()) || (getProductStock() > getProductMax())) {
            throw new ValidationException("Current inventory must be between minimum and maximum values. Please enter a valid amount.");
        }

        return true;
    }
}