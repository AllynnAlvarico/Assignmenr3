package allynn.alvarico;

import allynn.alvarico.file.FileManagement;
import allynn.alvarico.gui.UserGraphicsInterface;

import java.awt.*;

public class Restaurant {

    private final Color whiteBackgrond = Color.WHITE;
    private Font comicSansMS = new Font("Comic Sans MS", Font.BOLD, 18);
    private String iconPath = "resource\\images\\icon.png";

    public Restaurant(){
        FileManagement fileManagement = new FileManagement();
        fileManagement.setProductData();

        UserGraphicsInterface window = new UserGraphicsInterface(
                fileManagement.getProducts(), comicSansMS, whiteBackgrond, iconPath);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
