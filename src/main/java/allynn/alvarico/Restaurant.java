package allynn.alvarico;

import allynn.alvarico.file.FileManagement;
import allynn.alvarico.gui.UserGraphicsInterface;
import allynn.alvarico.product.Product;

import java.awt.*;

public class Restaurant {

    private final Color whiteBackgrond = Color.WHITE;
    private Font comicSansMS = new Font("Comic Sans MS", Font.BOLD, 18);

    public Restaurant(){
        FileManagement fileManagement = new FileManagement();
        fileManagement.setProductData();
//        fileManagement.fileInputStream();
//        fileManagement.bufferedOutputStream();
//        fileManagement.dataOutputStream();
        UserGraphicsInterface window = new UserGraphicsInterface(fileManagement.getProducts(), comicSansMS, whiteBackgrond);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
