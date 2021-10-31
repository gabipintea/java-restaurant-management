package ro.ase.csie.gabriel.pintea.ebuss.models.product;

public class ProductDinner extends Product {

    public ProductDinner(String title, int units, String mU, float price) {
        super(title, units, mU, price);
    }

    @Override
    public String showDescription() {
        return this.title + ' ' + this.units + "g - Dinner Only";
    }
}
