package inventorymanagementsystem;

public class Outsourced extends Part {

    
    //default constructor
    public Outsourced() {}
    
    //allow for added component Company Name
    public String companyName;
    
    //detailed constructor
    public Outsourced(int partId, String partName, int partStock, int partMax, int partMin, double partPrice, String companyName) {
        super(partId, partName, partStock, partMax, partMin, partPrice);
        this.companyName = companyName;
    }

    //getters and setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }  
}
