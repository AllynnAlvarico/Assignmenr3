package org.example;

public record Product(int productID, String productName, String category, double productPrice) {

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private int productID;
        private String productName;
        private String productCategory;
        private double productPrice;

        public Builder id(int productID){
            this.productID = productID;
            return this;
        }
        public Builder name(String productName){
            this.productName = productName;
            return this;
        }
        public Builder category(String category){
            this.productCategory = category;
            return this;
        }
        public Builder price(double price){
            this.productPrice = price;
            return this;
        }

        public Product build() {
            return new Product(productID, productName, productCategory, productPrice);
        }

    }


}
