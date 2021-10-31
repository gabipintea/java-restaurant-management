package ro.ase.csie.gabriel.pintea.ebuss.models.product;

public class ProductDrink extends Product {

    public ProductDrink(String title, int units, String mU, float price) {
        super(title, units, mU, price);
    }

    @Override
    public String showDescription() {
        return this.title + ' ' + this.units + "ml";
    }
}
