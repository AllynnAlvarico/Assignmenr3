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


    public void fileInputStream() {
        String message = "Hello World";
        try {
            FileOutputStream fos = new FileOutputStream("text.txt");
            FileInputStream fis = new FileInputStream("text.txt");
            fos.write(message.getBytes());
            fos.close();
            int i = 0;
            while((i = fis.read()) != -1){
                System.out.print((char)i);
            }
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bufferedOutputStream(){
        try {
            FileOutputStream fos = new FileOutputStream("oosdfile.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos); // BufferedOutputStream is efficient for reading bytes
            String text = "Studying OOSD3.";
            byte data[] = text.getBytes();
            bos.write(data);
            bos.flush();
            bos.close();
            fos.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Success");
    }

    public void dataOutputStream(){
        try{
            FileOutputStream fos = new FileOutputStream("test.txt");
            DataOutputStream dos = new DataOutputStream(fos);
//            dos.writeBoolean(false);
            dos.writeChars("Hello World");
//            dos.writeDouble(1.1);
            dos.flush();
            dos.close();
            System.out.println("Success");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
