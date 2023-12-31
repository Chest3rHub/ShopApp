package DTOs;

import Models.Products.Product;
import Models.Products.Size;

public class ProductInCartDTO {
    int idProduct;
    Size size;
    int quantity;

    public ProductInCartDTO(int idProduct, Size size, int quantity) {
        this.idProduct = idProduct;
        this.size = size;
        this.quantity = quantity;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }
    public String getProductName(){
        return Product.allProducts.get(this.getIdProduct()).getName();
    }
    public double getProductCost(){
        return Product.allProducts.get(this.getIdProduct()).getPrice();
    }
    public String getProductBrand(){
        return Product.allProducts.get(this.getIdProduct()).getBrand();
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return Product.allProducts.get(idProduct) +
                ";" + size +
                ";" + quantity;
    }
    public String getCartShortInfo(){
        return idProduct + ";"
                + size + ";"
                + quantity;
    }

}
