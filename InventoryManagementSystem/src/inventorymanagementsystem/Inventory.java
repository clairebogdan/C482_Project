package inventorymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    
///////////////////////////PARTS & PRODUCT LISTS////////////////////////////////
    
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();
       
    
///////////////////////////////PART FUNCTIONS///////////////////////////////////    
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    } 
    
    public static void updatePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartId() == selectedPart.partId) {
                allParts.set(i, selectedPart);
            }
        }
    }
    
    public static Part lookupPartName(String searchPartName) {
        for (Part part : allParts) {
            if (part.getPartName().equalsIgnoreCase(searchPartName)) {
                return part;
            }
        }
        return null;
    }
    
    public static Part lookupPartId(int searchPartId) {
        for (Part part : allParts) {
            if (part.getPartId() == (searchPartId)) {
                return part;
            }
        }
        return null;
    }

  

/////////////////////////////PRODUCT FUNCTIONS//////////////////////////////////
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    } 
    
    public static void updateProduct(Product selectedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductId() == selectedProduct.productId) {
                allProducts.set(i, selectedProduct);
            }
        }
    }
    
    public static Product lookupProductName(String searchProductName) {
        for (Product product : allProducts) {
            if (product.getProductName().equalsIgnoreCase(searchProductName)) {
                return product;
            }
        }
        return null;
    }
    
    public static Product lookupProductId(int searchProductId) {
        for (Product product : allProducts) {
            if (product.getProductId() == searchProductId) {
                return product;
            }
        }
        return null;
    }
}