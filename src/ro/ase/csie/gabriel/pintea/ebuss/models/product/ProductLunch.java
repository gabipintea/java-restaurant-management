package ro.ase.csie.gabriel.pintea.ebuss.models.product;

public class ProductLunch extends Product {

    public ProductLunch(String title, int units, String mU, float price) {
        super(title, units, mU, price);
    }

    @Override
    public String showDescription() {
        return this.title + ' ' + this.units + "g - Lunch Only";
    }
}
