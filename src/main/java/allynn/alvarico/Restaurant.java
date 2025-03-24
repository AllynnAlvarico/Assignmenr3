package allynn.alvarico;

import allynn.alvarico.file.FileManagement;
import allynn.alvarico.gui.UserGraphicsInterface;

public class Restaurant {

    public Restaurant(){
        FileManagement fileManagement = new FileManagement();
        fileManagement.setProductData();
        OrderItem orderItem = new OrderItem();
        UserGraphicsInterface window = new UserGraphicsInterface(fileManagement.getProducts(), orderItem);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
