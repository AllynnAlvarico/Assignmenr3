package allynn.alvarico.file;

import allynn.alvarico.product.Product;

import java.io.*;
import java.util.ArrayList;

public class FileManagement {

    String filepath = "resource\\products\\ProductDataBase.csv";
    private final String[] headers = {"pkey", "name", "category", "price", "prepTime", "path"};
    private String line;
    private String splitBy = ",";
    private ArrayList<Product> products = new ArrayList<>();

    public FileManagement()
    {
        File userDataFile = new File(filepath);
        if (!userDataFile.exists()){
            createCsvFile();
        }
    }
    public void createCsvFile(){
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(this.filepath))) {
            csvWriter.println(String.join(splitBy, this.headers));
        } catch (IOException e) {e.printStackTrace();}
    }

    public void setProductData(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(this.filepath));
            while ((line = reader.readLine()) != null) {
                String[] item = line.split(splitBy);
                if (!line.contains(headers[0])){
                    Product product = createObjProduct(item);
                    products.add(product);
                }
            }
        }catch (IOException e) {e.printStackTrace();}
    }

    private Product createObjProduct(String[] item) {
         Product p = Product.builder()
                .id(Integer.parseInt(item[0]))
                .name(item[1])
                 .category(item[2])
                 .price(Double.parseDouble(item[3]))
                 .prepTime(Integer.parseInt(item[4]))
                 .path(item[5])
                .build();
        return p;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
