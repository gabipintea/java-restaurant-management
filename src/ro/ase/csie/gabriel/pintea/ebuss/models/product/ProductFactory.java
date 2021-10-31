package ro.ase.csie.gabriel.pintea.ebuss.models.product;

public class ProductFactory {

    public static Product getProduct(String title, float price, ProductType productType) {
        switch(productType) {
            case BREAKFAST:
                return new ProductBreakfast(title, 100, "g", price);
            case LUNCH:
                return new ProductLunch(title,250, "g", price);
            case DINNER:
                return new ProductDinner(title, 200, "g", price);
            case DRINK:
                return new ProductDrink(title, 330, "ml", price);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
