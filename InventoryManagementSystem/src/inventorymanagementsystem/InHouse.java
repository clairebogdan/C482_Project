package inventorymanagementsystem;

public class InHouse extends Part {

    //default constructor
    public InHouse() {}
    
    //allow for added component of Machine ID 
    public int machineId;  
    
    //detailed constructor
    public InHouse(int partId, String partName, int partStock, int partMax, int partMin, double partPrice, int machineId) {
        super(partId, partName, partStock, partMax, partMin, partPrice);
        this.machineId = machineId;
    }

    //getters and setters
    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }   
}
