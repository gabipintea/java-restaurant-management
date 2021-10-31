package ro.ase.csie.gabriel.pintea.ebuss.models.foodmenu;

import ro.ase.csie.gabriel.pintea.ebuss.models.product.Product;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import static java.util.Objects.isNull;


public class FoodMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Product> productList;

    public FoodMenu() {
        this.productList = new HashMap<String, Product>();
    }

    public int getSize() {
        return productList.size();
    }

    public HashMap<String, Product> getProductList() {
        return productList;
    }

    public boolean addProduct(Product product) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String genKey = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return isNull(productList.put(genKey,product));
    }

    public boolean removeProduct(String key) {
        return !isNull(productList.remove(key));
    }

    @Override
    public String toString() {
        if(productList.isEmpty()) {
            return "No items on the menu";
        } else {
            for(String key : productList.keySet()) {
                System.out.println("(" + key + ") " + productList.get(key).showDescription() + " - " + productList.get(key).getPrice() + " lei");
            }
            System.out.println("\n");
        }
        return "Menu END";
    }
}
