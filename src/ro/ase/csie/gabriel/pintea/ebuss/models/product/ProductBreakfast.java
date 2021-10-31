package ro.ase.csie.gabriel.pintea.ebuss.models.product;

public class ProductBreakfast extends Product {

    public ProductBreakfast(String title, int units, String mU, float price) {
        super(title, units, mU, price);
    }

    @Override
    public String showDescription() {
        return this.title + ' ' + this.units + "g - Breakfast Only";
    }
}
