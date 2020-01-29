package inventorymanagementsystem;
 
import javax.xml.bind.ValidationException;

public abstract class Part {
    
    //default constructor
    public Part() {}
    
    //variables
    int partId;
    String partName;
    double partPrice;
    int partStock;
    int partMax;
    int partMin;
    
    //detailed constructor
    public Part(int partId, String partName, int partStock, int partMax, int partMin, double partPrice) {
        this.partId = partId;
        this.partName = partName;
        this.partStock = partStock;
        this.partMax = partMax;
        this.partMin = partMin;
        this.partPrice = partPrice;
    }

    //getters and setters
    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public double getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(double partPrice) {
        this.partPrice = partPrice;
    }

    public int getPartStock() {
        return partStock;
    }

    public void setPartStock(int partStock) {
        this.partStock = partStock;
    }

    public int getPartMax() {
        return partMax;
    }

    public void setPartMax(int partMax) {
        this.partMax = partMax;
    }

    public int getPartMin() {
        return partMin;
    }

    public void setPartMin(int partMin) {
        this.partMin = partMin;
    }    
    
    
    
    
    
    //check for validity of entries
    public boolean isValid() throws ValidationException {
        
        if (getPartName().equals("")) {
            throw new ValidationException("Name field is required. Please enter a name.");
        }
        
        if (getPartStock() <= 0) {
            throw new ValidationException("Inventory must be greater than 0. Please enter a valid amount.");
        }
        
        if (getPartPrice() <= 0.00) {
            throw new ValidationException("Price must be above $0.00. Please enter a valid amount.");
        }
        
        if (getPartMin() <= 0) {
            throw new ValidationException("Minimum inventory must be greater than 0. Please enter a valid amount.");
        }
        
        if (getPartMax() <= 0) {
            throw new ValidationException("Maximum inventory must be greater than 0. Please enter a valid amount. ");
        }
        
        if (getPartMax() == getPartMin()) {
            throw new ValidationException("Maximum and minimum inventory numbers cannot be the same. Please enter a valid amount.");
        }
        
        if (getPartMax() < getPartMin()) {
            throw new ValidationException("Maximum inventory cannot be less than minimum inventory. Please enter a valid amount.");
        }
        
        if (getPartMin() > getPartMax()) {
            throw new ValidationException("Minimum inventory cannot be more than maximum inventory. Please enter a valid amount.");
        }
        
        if ((getPartStock() < getPartMin()) || (getPartStock() > getPartMax())) {
            throw new ValidationException("Current inventory must be between minimum and maximum values. Please enter a valid amount.");
        }

        return true;
    }
}

