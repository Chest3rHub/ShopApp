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
