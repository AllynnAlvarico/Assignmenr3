package allynn.alvarico.product;

public record Product(int productID, String productName, String category, double productPrice, int productPrepTime, String path) {

    public Product {
        if (productID < 0) {
            throw new IllegalArgumentException("Product ID cannot be negative");
        }
        if (productPrice < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        if (productPrepTime < 0) {
            throw new IllegalArgumentException("Product preparation time cannot be negative");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int productID;
        private String productName;
        private String productCategory;
        private double productPrice;
        private int productPrepTime;
        private String path;

        public Builder id(int productID) {
            this.productID = productID;
            return this;
        }

        public Builder name(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder category(String category) {
            this.productCategory = category;
            return this;
        }

        public Builder price(double price) {
            this.productPrice = price;
            return this;
        }

        public Builder prepTime(int prepTime) {
            this.productPrepTime = prepTime;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Product build() {
            return new Product(productID, productName, productCategory, productPrice, productPrepTime, path);
        }
    }
}
